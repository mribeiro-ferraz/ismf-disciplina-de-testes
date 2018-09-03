package com.mackleaps.formium.model.survey_application;

import com.mackleaps.formium.model.survey.Survey;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SurveyResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    private Survey correspondingSurvey; /**this will correspond to a publication*/

    @ManyToOne
    private Company company;

    public SurveyResults (Company company, Survey correspondingSurvey) {

        Validate.notNull(company);
        Validate.notNull(correspondingSurvey);

        if(!correspondingSurvey.isSurvey())
            throw new IllegalArgumentException("Not a survey");

        this.company = company;
        this.correspondingSurvey = correspondingSurvey;
    }

    @java.beans.ConstructorProperties({"id", "answers", "correspondingSurvey", "company"})
    public SurveyResults(Long id, List<Answer> answers, Survey correspondingSurvey, Company company) {
        this.id = id;
        this.answers = answers;
        this.correspondingSurvey = correspondingSurvey;
        this.company = company;
    }

    public SurveyResults() {
    }

    /**
     * check if update or adding operation
     * and then do it
     * */
    public void provideAnswer(Answer answer) {

        Validate.notNull(answer);

        if(answer.getId() == null)
            addNewAnswer(answer);
        else
            updateExistingAnswer(answer);
    }

    public void provideAnswers(List<? extends Answer> answers) {

        Validate.notNull(answers);

        for(Answer answer : answers)
            provideAnswer(answer);
    }

    public void addNewAnswer(Answer answer) {
        Validate.notNull(answer);
        Validate.isTrue(answer.getId() == null, "Trying to add an already persisted answer");
        answers.add(answer);
        answer.setCorrespondingResults(this);
    }

    public void updateExistingAnswer(Answer answer) {

        Validate.notNull(answer);
        Validate.notNull(answer.getId()); //if you are going to update, the answer needs to have an id

        for(int i = 0; i < answers.size(); i++) {
            if(correspondToTheSameAnswer(answer,answers.get(i))) {
                answers.set(i, answer);
                return;
            }
        }

        throw new IllegalStateException();
    }

    //Just used when updating. Both need to have an id
    private boolean correspondToTheSameAnswer(Answer first, Answer second) {

        Validate.notNull(first.getId());
        Validate.notNull(second.getId());

        return first.getId().equals(second.getId());
    }

    public Long getId() {
        return this.id;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public Survey getCorrespondingSurvey() {
        return this.correspondingSurvey;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void setCorrespondingSurvey(Survey correspondingSurvey) {
        this.correspondingSurvey = correspondingSurvey;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
