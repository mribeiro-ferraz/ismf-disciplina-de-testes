package com.mackleaps.formium.service.survey_application;

import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.survey.Question;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.model.survey_application.Answer;
import com.mackleaps.formium.model.survey_application.Company;
import com.mackleaps.formium.model.survey_application.Employee;
import com.mackleaps.formium.model.survey_application.SurveyResults;
import com.mackleaps.formium.repository.survey.QuestionRepository;
import com.mackleaps.formium.repository.survey_application.AnswerRepository;
import com.mackleaps.formium.repository.survey_application.CompanyRepository;
import com.mackleaps.formium.repository.survey_application.EmployeeRepository;
import com.mackleaps.formium.repository.survey_application.SurveyResultsRepository;
import com.mackleaps.formium.service.survey.ICategoryService;
import com.mackleaps.formium.service.survey.IContainerService;
import com.mackleaps.formium.service.survey.ISurveyService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService implements IAnswerService {

    private ICategoryService categoryService;
    private CompanyRepository companyRepository;
    private SurveyResultsRepository surveyResultsRepository;
    private AnswerRepository answerRepo;
    private ISurveyService surveyService;
    private EmployeeRepository employeeRepository;
    private QuestionRepository questionRepository;
    private IContainerService containerService;

    @Autowired
    public AnswerService (ICategoryService categoryService,
                          ISurveyService surveyService,
                          CompanyRepository companyRepository,
                          SurveyResultsRepository surveyResultsRepository,
                          AnswerRepository answerRepo,
                          EmployeeRepository employeeRepository,
                          QuestionRepository questionRepository,
                          IContainerService containerService) {

        this.categoryService = categoryService;
        this.companyRepository = companyRepository;
        this.surveyResultsRepository = surveyResultsRepository;
        this.answerRepo = answerRepo;
        this.surveyService = surveyService;
        this.employeeRepository = employeeRepository;
        this.questionRepository = questionRepository;
        this.containerService = containerService;
    }

    @Override
    public List<Answer> getAnswersForCompanyAndContainer(Long containerId, Long companyId) throws ResourceNotFoundException,
                                                                                                  IllegalStateException {

        Validate.notNull(containerId);
        Validate.notNull(companyId);

        //TODO remove this
        provisionalAssociation(companyId, containerId);

        //checking existence of used entities
        Company currentCompany = companyRepository.findOne(companyId);
        Container currentContainer = containerService.getContainer(containerId);
        if(currentCompany == null || currentContainer == null) {
            throw new ResourceNotFoundException();
        }

        //getting the results for company and category
        //validating if company is associated to survey
        Long currentSurveyId = currentContainer.getSurveyForCurrentContainer().getId();
        if(!isCompanyAnsweringSurvey(currentSurveyId,currentCompany.getId()))
            throw new IllegalStateException("Trying to get answers given to a survey to " +
                                            "which current company is not associated to");

        SurveyResults results = surveyResultsRepository
                .findByCompanyIdAndAndCorrespondingSurveyId(currentCompany.getId(), currentSurveyId);

        if(results == null)
            throw new IllegalArgumentException("Company is not associated to survey");

        return answerRepo
                .findAnswersByCorrespondingResultsIdAndCorrespondingQuestionParentId(results.getId(), containerId);
    }

    @Override
    public SurveyResults associateCompanyToSurvey(Long surveyId, Long companyId) throws ResourceNotFoundException,
                                                                                        IllegalStateException {
        Validate.notNull(surveyId);
        Validate.notNull(companyId);

        //checking existence of used entities
        Survey correspondingSurvey = surveyService.getSurvey(surveyId);
        Company correspondingCompany = companyRepository.findOne(companyId);
        if(correspondingSurvey == null || correspondingCompany == null || !correspondingSurvey.isSurvey())
            throw new ResourceNotFoundException();

        if(isCompanyAnsweringSurvey(surveyId, companyId))
            throw new IllegalStateException("Trying to associate a company to a survey to which it is already associated");

        SurveyResults newAssociation = new SurveyResults(correspondingCompany, correspondingSurvey);
        return surveyResultsRepository.saveAndFlush(newAssociation);
    }

    @Override
    public boolean isCompanyAnsweringSurvey(Long surveyId, Long companyId) {

        Validate.notNull(surveyId);
        Validate.notNull(companyId);

        return surveyResultsRepository.existsSurveyResultsByCompanyIdAndCorrespondingSurveyId(companyId,surveyId);
    }

    @Override
    public SurveyResults provideMultipleAnswers(List<? extends Answer> answers, Long companyId,
                                                Long userId, Long currentContainerId) {
        Validate.notNull(answers);
        Validate.notNull(companyId);
        Validate.notNull(userId);
        Validate.notNull(currentContainerId);

        //TODO remove this
        provisionalAssociation(companyId, currentContainerId);

        //checking existence of used entities
        Company company = companyRepository.findOne(companyId);
        Employee employee = employeeRepository.findEmployeeByUserId(userId);
        if(company == null || employee == null)
            throw new ResourceNotFoundException();

        if(!company.isAssociatedToCompany(employee))
            throw new IllegalArgumentException("Informed user is not associated to company");

        Long correspondingSurveyId = categoryService.getCategoryForUse(currentContainerId)
                                                    .getSurveyForCurrentCategory()
                                                    .getId();

        if(!isCompanyAnsweringSurvey(correspondingSurveyId, companyId))
            throw new IllegalStateException("Company is not answering informed survey");

        SurveyResults results = surveyResultsRepository
                .findByCompanyIdAndAndCorrespondingSurveyId(companyId,correspondingSurveyId);

        //adding given answer to results
        for(Answer currentAnswer : answers) {
            Question currentQuestion = questionRepository.findOne(currentAnswer.getCorrespondingQuestion().getId());
            if(currentQuestion == null)
                throw new ResourceNotFoundException();

            currentAnswer.setCorrespondingQuestion(currentQuestion);
        }

        results.provideAnswers(answers);
        return surveyResultsRepository.saveAndFlush(results);
    }

    @Override
    public List<Answer> getMergedListOfQuestionsAndAnswersForCompany(Long currentContainer, Long companyId) {

        Validate.notNull(currentContainer);
        Validate.notNull(companyId);

        //TODO remove this
        provisionalAssociation(companyId, currentContainer);

        List<Question> questions = containerService.getContainerForUse(currentContainer)
                                                   .getQuestions();

        List<Answer> answersGiven = getAnswersForCompanyAndContainer(currentContainer,companyId);

        List<Answer> mergedAnswers = new ArrayList<>();
        for(Question currentQuestion : questions) {

            boolean found = false;

            for(Answer currentAnswer : answersGiven) {
                //has an answer. just adding to the list
                if(currentAnswer.getCorrespondingQuestion().getId().equals(currentQuestion.getId())) {
                    mergedAnswers.add(currentAnswer);
                    found = true;
                    break;
                }
            }

            if(!found) {
                //does not have an answer to current question
                //creating new instance and setting corresponding question
                try {
                    Answer newAnswer = (Answer) currentQuestion.getAnswerType().newInstance();
                    newAnswer.setCorrespondingQuestion(currentQuestion);
                    mergedAnswers.add(newAnswer);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ClassCastException();
                }
            }
        }

        return mergedAnswers;
    }

    //TODO remove this. This will be removed when the process to associate company to survey is defined
    @Deprecated
    private void provisionalAssociationCompanyAndSurvey(Long companyId, Long containerId) {

        try {

            Long surveyId = categoryService.getCategoryForUse(containerId)
                                            .getSurveyForCurrentCategory()
                                            .getId();

            if(!isCompanyAnsweringSurvey(surveyId, companyId))
                associateCompanyToSurvey(surveyId, companyId);

        } catch (IllegalStateException e) {
            throw new ResourceNotFoundException();
        }

    }

    private void provisionalAssociation(Long companyId, Long containerId) {

        try {

            Long surveyId = containerService.getContainerForUse(containerId)
                                            .getSurveyForCurrentContainer()
                                            .getId();

            if(!isCompanyAnsweringSurvey(surveyId, companyId))
                associateCompanyToSurvey(surveyId, companyId);

        } catch (IllegalStateException e) {
            throw new ResourceNotFoundException();
        }

    }
}
