package com.mackleaps.formium.controller;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.model.dto.LikertQuestionDTO;
import com.mackleaps.formium.model.survey.LikertQuestion;
import com.mackleaps.formium.model.util.Message;
import com.mackleaps.formium.model.util.PersistedEntitiesObjectMapper;
import com.mackleaps.formium.service.survey.IContainerService;
import com.mackleaps.formium.service.survey.IQuestionService;
import org.slf4j.Logger;
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

@Controller
@RequestMapping(value = "/system/questions/")
@PropertySource(value = "classpath:/messages.properties", encoding = "UTF-8")
public class QuestionController {

    private IQuestionService questionService;
    private IContainerService containerService;
    private PersistedEntitiesObjectMapper em;
    private Environment messageSource;

    @Autowired
    public QuestionController(IQuestionService questionService,
                              IContainerService containerService,
                              PersistedEntitiesObjectMapper em,
                              Environment messageSource) {

        this.questionService = questionService;
        this.containerService = containerService;
        this.em = em;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "/new/{parentId}")
    public ModelAndView initLikertCreation(@PathVariable Long parentId) {

        if(!containerService.doesContainerExist(parentId))
            throw new ComponentNotFoundException();

        LikertQuestionDTO likertQuestion = new LikertQuestionDTO();
        likertQuestion.setParentId(parentId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("question", likertQuestion);
        mav.setViewName("question_form");

        return mav;
    }

    @PostMapping(value = "/new/")
    public String processCreation(@Valid @ModelAttribute("question") LikertQuestionDTO likertQuestionDTO,
                                  BindingResult result,
                                  ModelMap model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("message", new Message(result.getAllErrors()));
            return "question_form";
        }

        LikertQuestion likertQuestion = em.convertDTOToEntity(likertQuestionDTO, LikertQuestion.class);
        questionService.addQuestion(likertQuestion, likertQuestionDTO.getParentId());

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("questions.creatingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/container/components/{id}"
               .replace("{id}", likertQuestionDTO.getParentId().toString());
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView initEditing(@PathVariable Long id) {

        LikertQuestion likertQuestion = questionService.getQuestionForUse(id,LikertQuestion.class);
        LikertQuestionDTO likertQuestionDTO = em.convertEntityToDTO(likertQuestion,LikertQuestionDTO.class);

        ModelAndView mav = new ModelAndView();
        mav.addObject("question", likertQuestionDTO);
        mav.setViewName("question_form");

        return mav;
    }

    @PostMapping(value = "/edit/")
    public String processEditingLikertQuestion(@Valid @ModelAttribute("question") LikertQuestionDTO likertQuestionDTO,
                                               BindingResult results,
                                               ModelMap model, RedirectAttributes redirectAttributes) {

        if(results.hasErrors()) {
            model.addAttribute("message", new Message(results.getAllErrors()));
            return "question_form";
        }

        Long parentId = containerService.getContainerForUse(likertQuestionDTO.getParentId()).getId();

        LikertQuestion likertQuestion = em.convertDTOToEntity(likertQuestionDTO,LikertQuestion.class);
        questionService.editQuestion(likertQuestion,LikertQuestion.class);

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("questions.editingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/container/components/{parentId}"
               .replace("{parentId}", parentId.toString());
    }

    @PostMapping(value = "/delete/{id}")
    public String processDeletingQuestion(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Long parentId = questionService.getParent(id).getId();
        questionService.deleteQuestion(id);

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("questions.detingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/container/components/{parentId}"
                .replace("{parentId}", parentId.toString());

    }

}
