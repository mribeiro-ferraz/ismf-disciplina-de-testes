package com.mackleaps.formium.init;

import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.LikertQuestion;
import com.mackleaps.formium.model.survey.Survey;
import com.mackleaps.formium.repository.auth.PersonRepository;
import com.mackleaps.formium.repository.auth.RoleRepository;
import com.mackleaps.formium.repository.survey.CategoryRepository;
import com.mackleaps.formium.repository.survey.QuestionRepository;
import com.mackleaps.formium.repository.survey.SurveyRepository;
import com.mackleaps.formium.service.survey_application.CompanyAssociationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
@PropertySource(value = "classpath:values_survey_components.properties", encoding = "UTF-8")
public class BootstrappingDataTesting {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BootstrappingDataTesting.class);
    private PersonRepository personRepository;
    private RoleRepository roleRepository;

    private SurveyRepository surveyRepository;
    private CategoryRepository categoryRepository;
    private QuestionRepository questionRepository;
    private CompanyAssociationService companyService;

    @Autowired
    public BootstrappingDataTesting (PersonRepository personRepository,
                                     RoleRepository roleRepository,
                                     SurveyRepository surveyRepository,
                                     CategoryRepository categoryRepository,
                                     QuestionRepository questionRepository,
                                     CompanyAssociationService companyService) {

        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.surveyRepository = surveyRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.companyService = companyService;
    }

    private @Value("${survey.prefix}") String surveyPrefix;
    private @Value("${survey.title}") String surveyTitle;
    private @Value("${survey.description}") String surveyDescription;

    private @Value("${governanca.title}") String governancaTitle;
    private @Value("${governanca.description}") String governancaDescription;

    private @Value("${primeiro.aspecto.title}") String aspectTitle;

    private @Value("${primeiro.index.title}") String firstIndexTitle;
    private @Value("${primeiro.index.description}") String firstIndexDescription;

    private @Value("${segundo.index.title}") String secondIndexTitle;
    private @Value("${segundo.index.description}") String secondIndexDescription;

    private @Value("${terceiro.index.title}") String thirdIndexTitle;
    private @Value("${terceiro.index.description}") String thirdIndexDescription;

    private @Value("${primeira.questao.header}") String firstQuestionHeader;
    private @Value("${segunda.questao.header}") String secondQuestionHeader;
    private @Value("${terceira.questao.header}") String thirdQuestionHeader;
    private @Value("${quarta.questao.header}") String fourthQuestionHeader;

    private @Value("${likert.texto.esquerda}") String leftText;
    private @Value("${likert.texto.direita}") String rightText;
    private @Value("#{new Integer('${likert.valor.esquerda}')}") Integer leftValue;
    private @Value("#{new Integer('${likert.valor.direita}')}")  Integer rightValue;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void surveys () {

        Survey survey = new Survey();
        survey.setTitle(surveyTitle);
        survey.setDescription(surveyDescription);
        survey.setPrefix(surveyPrefix);
        surveyRepository.saveAndFlush(survey);

        Category category = new Category();
        category.setTitle(governancaTitle);
        category.setDescription(governancaDescription);
        category.associateToParent(survey);
        categoryRepository.saveAndFlush(category);

        Category aspect = new Category();
        aspect.setTitle(aspectTitle);
        aspect.associateToParent(category);
        categoryRepository.saveAndFlush(aspect);

        Category index1 = new Category();
        index1.setTitle(firstIndexTitle);
        index1.setDescription(firstIndexDescription);
        index1.associateToParent(aspect);
        categoryRepository.saveAndFlush(index1);

        Category index2 = new Category();
        index2.setTitle(secondIndexTitle);
        index2.setDescription(secondIndexDescription);
        index2.associateToParent(aspect);
        categoryRepository.saveAndFlush(index2);

        Category index3 = new Category();
        index3.setTitle(thirdIndexTitle);
        index3.setDescription(thirdIndexDescription);
        index3.associateToParent(aspect);
        categoryRepository.saveAndFlush(index3);

        LikertQuestion first = new LikertQuestion();
        first.setHeader(firstQuestionHeader);
        first.associateToParent(index1);
        first.setLeftText(leftText);
        first.setRightText(rightText);
        first.setLeftValue(leftValue);
        first.setRightValue(rightValue);
        questionRepository.saveAndFlush(first);

        LikertQuestion second = new LikertQuestion();
        second.setHeader(secondQuestionHeader);
        second.associateToParent(index2);
        second.setLeftText(leftText);
        second.setRightText(rightText);
        second.setLeftValue(leftValue);
        second.setRightValue(rightValue);
        questionRepository.saveAndFlush(second);

        LikertQuestion third = new LikertQuestion();
        third.setHeader(secondQuestionHeader);
        third.associateToParent(index3);
        third.setLeftText(leftText);
        third.setRightText(rightText);
        third.setLeftValue(leftValue);
        third.setRightValue(rightValue);
        questionRepository.saveAndFlush(third);

        log.info("Dummy survey created");
    }
}
