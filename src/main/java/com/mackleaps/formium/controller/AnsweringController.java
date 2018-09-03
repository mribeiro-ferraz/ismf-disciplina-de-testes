package com.mackleaps.formium.controller;

import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.model.dto.AnswerLikertQuestionDTO;
import com.mackleaps.formium.model.dto.CategoryDTO;
import com.mackleaps.formium.model.dto.SurveyDTO;
import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.model.survey_application.Answer;
import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.LikertAnswer;
import com.mackleaps.formium.model.util.ListUtils;
import com.mackleaps.formium.model.util.Message;
import com.mackleaps.formium.model.util.PersistedEntitiesObjectMapper;
import com.mackleaps.formium.model.util.SelectedCompany;
import com.mackleaps.formium.security.core.AuthenticationInterface;
import com.mackleaps.formium.service.survey.IContainerService;
import com.mackleaps.formium.service.survey.ISurveyService;
import com.mackleaps.formium.service.survey_application.IAnswerService;
import com.mackleaps.formium.service.survey_application.ICompanyService;
import com.mackleaps.formium.service.survey_application.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.mackleaps.formium.model.util.SelectedCompany.ANSWERING_FOR_IDENTIFIER;
import static com.mackleaps.formium.model.util.SelectedCompany.NO_COMPANY_SELECTED;

@Controller
@RequestMapping("/answer")
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class AnsweringController {

    private IAnswerService answerService;
    private AuthenticationInterface authService;
    private IEmployeeService employeeService;
    private ISurveyService surveyService;
    private PersistedEntitiesObjectMapper em;
    private IContainerService containerService;
    private ICompanyService companyService;
    private Environment messageSource;

    @Autowired
    public AnsweringController(IAnswerService answerService,
                               AuthenticationInterface authService,
                               IEmployeeService employeeService,
                               ISurveyService surveyService,
                               PersistedEntitiesObjectMapper em,
                               IContainerService containerService,
                               ICompanyService companyService,
                               Environment messageSource) {

        this.answerService = answerService;
        this.authService = authService;
        this.employeeService = employeeService;
        this.surveyService = surveyService;
        this.em = em;
        this.containerService = containerService;
        this.companyService = companyService;
        this.messageSource = messageSource;
    }

    @GetMapping("/associated-companies")
    public ModelAndView listAssociatedCompany(HttpSession session) {

        session.setAttribute(ANSWERING_FOR_IDENTIFIER, NO_COMPANY_SELECTED);

        User loggedUser = (User) authService.getUserDetails();
        List<Company> companies = employeeService.getCompaniesToWhichEmployeeIsAssociatedTo(loggedUser.getId());

        ModelAndView mav = new ModelAndView();
        mav.addObject("companies", companies);
        mav.setViewName("associated_companies");
        return mav;
    }

    @PostMapping("/associated-companies")
    public String chooseCompanyToAnswerFor(@ModelAttribute("selected-company-id") Long id, HttpSession session) {

        Company company = companyService.getCompanyForUse(id);
        SelectedCompany selected = new SelectedCompany(company.getId(), company.getName());

        session.setAttribute(ANSWERING_FOR_IDENTIFIER, selected);
        return "redirect:/answer";
    }

    @GetMapping
    public ModelAndView showPageForAnsweringSurveys(HttpSession session){

        SelectedCompany selected = (SelectedCompany) session.getAttribute(ANSWERING_FOR_IDENTIFIER);
        if(!SelectedCompany.isCompanySelected(selected)) // no company selected
            return new ModelAndView("redirect:/answer/associated-companies");

        //TODO show only surveys to which the company is associated to
        List<Survey> surveys = surveyService.getAllSurveys();

        ModelAndView mav = new ModelAndView();
        mav.addObject("surveys", surveys);
        mav.setViewName("answer-surveys");
        return mav;

    }

    @GetMapping("/container/{containerId}")
    public ModelAndView showPageForSpecificContainer(HttpSession session,
                                                     @PathVariable Long containerId,
                                                     @ModelAttribute("message") Message message) {

        SelectedCompany selected = (SelectedCompany) session.getAttribute(ANSWERING_FOR_IDENTIFIER);
        if(!SelectedCompany.isCompanySelected(selected)) // no company selected
            return new ModelAndView("redirect:/answer/associated-companies");

        Container container = containerService.getContainerForUse(containerId);

        List<Category> subcategories = containerService.getCategoriesForContainer(containerId);
        List<Answer> mergedAnswers = answerService.getMergedListOfQuestionsAndAnswersForCompany(containerId,selected.getId());
        List<LikertAnswer> mergedLikertAnswer = ListUtils.convertIntoMoreSpecificTypeOfList(mergedAnswers,
                                                                                            Answer.class,
                                                                                            LikertAnswer.class);
        List<AnswerLikertQuestionDTO> answersDto = em.convertListOfEntitiesIntoDtos(mergedLikertAnswer,
                                                                                    AnswerLikertQuestionDTO.class);

        //adding objects to mav to sent to view
        ModelAndView mav = new ModelAndView();
        mav.addObject("answersWrapper", new AnswerLikertQuestionDTO.Wrapper(containerId,answersDto));
        mav.addObject("categories", em.convertListOfEntitiesIntoDtos(subcategories, CategoryDTO.class));
        mav.addObject("path", containerService.pathToSurvey(containerId));
        if(container.isSurvey()) {
            mav.addObject("survey", em.convertEntityToDTO((Survey) container, SurveyDTO.class));
        } else {
            mav.addObject("category", em.convertEntityToDTO((Category) container, CategoryDTO.class));
        }

        if(message != null && !message.isEmpty())
            mav.addObject("message",message);

        mav.setViewName("answer-category");
        return mav;

    }

    @PostMapping(value = "/category/")
    public String receiveAnswersForContainer(@ModelAttribute AnswerLikertQuestionDTO.Wrapper answers,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {

        //getting the company from the session
        SelectedCompany selected = (SelectedCompany) session.getAttribute(ANSWERING_FOR_IDENTIFIER);
        if(!SelectedCompany.isCompanySelected(selected)) // no company selected
            return "redirect:/answer/associated-companies";

        answers.removeNotSelected();

        User loggedUser = (User) authService.getUserDetails();
        List<LikertAnswer> answersForCurrentContainer = em.convertListOfDTOsToEntities(answers.getAnswersList(),
                                                                                       LikertAnswer.class);
        //providing answers to services
        answerService.provideMultipleAnswers(answersForCurrentContainer,
                                             selected.getId(),
                                             loggedUser.getId(),
                                             answers.getIdCurrentContainer());

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("answers.success");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/answer/container/{id}"
                .replace("{id}", answers.getIdCurrentContainer().toString());
    }

}
