package com.mackleaps.formium.controller;

import com.mackleaps.formium.model.dto.CategoryDTO;
import com.mackleaps.formium.model.dto.LikertQuestionDTO;
import com.mackleaps.formium.model.dto.SurveyDTO;
import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.LikertQuestion;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.model.util.Message;
import com.mackleaps.formium.model.util.PersistedEntitiesObjectMapper;
import com.mackleaps.formium.service.survey.IContainerService;
import com.mackleaps.formium.service.survey.ISurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/system/surveys")
@PropertySource(value = "classpath:/messages.properties", encoding = "UTF-8")
public class SurveyController {

    private ISurveyService surveyService;
    private IContainerService containerService;
    private PersistedEntitiesObjectMapper em;
    private Environment messageSource;

    @Autowired
    public SurveyController(ISurveyService surveyService,
                            IContainerService containerService,
                            PersistedEntitiesObjectMapper em,
                            Environment messageSource) {

        this.surveyService = surveyService;
        this.containerService = containerService;
        this.em = em;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ModelAndView listSurveys() {

        List<SurveyDTO> surveys = em.convertListOfEntitiesIntoDtos(surveyService.getAllSurveys(), SurveyDTO.class);

        ModelAndView mav = new ModelAndView();
        mav.addObject("surveys", surveys);
        mav.setViewName("index");

        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView listElementsSurvey (@PathVariable Long id, @ModelAttribute("message") Message message) {

        Survey currentSurvey = surveyService.getSurveyForUse(id);
        List<LikertQuestion> questions = containerService.getQuestionsForContainer(id, LikertQuestion.class);
        List<Category> subcategories = containerService.getCategoriesForContainer(id);

        ModelAndView mav = new ModelAndView();
        mav.addObject("currentSurvey", em.convertEntityToDTO(currentSurvey, SurveyDTO.class));
        mav.addObject("questions", em.convertListOfEntitiesIntoDtos(questions, LikertQuestionDTO.class));
        mav.addObject("subcategories", em.convertListOfEntitiesIntoDtos(subcategories, CategoryDTO.class));
        mav.addObject("path", containerService.pathToSurvey(id));
        if(message != null && !message.isEmpty())
            mav.addObject("message", message);

        mav.setViewName("survey_components");

        return mav;
    }

    @GetMapping(value = "/new")
    public ModelAndView newSurveyForm() {

        ModelAndView mav = new ModelAndView("survey_form");
        mav.addObject("survey", new SurveyDTO());

        return mav;
    }

    @PostMapping(value = "/new")
    public String newSurvey(@Valid @ModelAttribute("survey") SurveyDTO survey, BindingResult result,
                            ModelMap model, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            model.addAttribute("message", new Message(result.getAllErrors()));
            return "survey_form";
        }

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("surveys.creatingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        surveyService.addSurvey(em.convertDTOToEntity(survey, Survey.class));
        return "redirect:/system/surveys/";
    }

    @GetMapping(value = "/edit/{surveyId}")
    public ModelAndView editSurveyForm(@PathVariable Long surveyId) {

        SurveyDTO survey = em.convertEntityToDTO(surveyService.getSurveyForUse(surveyId), SurveyDTO.class);

        ModelAndView mav = new ModelAndView();
        mav.addObject("survey", survey);
        mav.setViewName("survey_form");

        return mav;
    }

    @PostMapping(value = "/edit")
    public String editSurvey(@Valid @ModelAttribute("survey") SurveyDTO survey, BindingResult result,
                             ModelMap model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("message", new Message(result.getAllErrors()));
            return "survey_form";
        }

        surveyService.editSurvey(em.convertDTOToEntity(survey, Survey.class));

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("surveys.editingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/surveys/{id}"
                .replace("{id}", survey.getId().toString());

    }

    @PostMapping(value = "/delete/{surveyId}")
    public String processDeletingSurvey(@PathVariable Long surveyId, RedirectAttributes redirectAttributes) {

        surveyService.deleteSurvey(surveyId);

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("surveys.detingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/surveys/";
    }

}
