package com.mackleaps.formium.model.survey_application;

import com.mackleaps.formium.model.survey.Question;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Answer <T extends Question> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Question.class)
    private T correspondingQuestion;

    @ManyToOne
    private SurveyResults correspondingResults;

    @java.beans.ConstructorProperties({"id", "correspondingQuestion", "correspondingResults"})
    public Answer(Long id, T correspondingQuestion, SurveyResults correspondingResults) {
        this.id = id;
        this.correspondingQuestion = correspondingQuestion;
        this.correspondingResults = correspondingResults;
    }

    public Answer() {
    }

    abstract boolean isValueNotAnsweredQuestion();

    public Long getId() {
        return this.id;
    }

    public T getCorrespondingQuestion() {
        return this.correspondingQuestion;
    }

    public SurveyResults getCorrespondingResults() {
        return this.correspondingResults;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCorrespondingQuestion(T correspondingQuestion) {
        this.correspondingQuestion = correspondingQuestion;
    }

    public void setCorrespondingResults(SurveyResults correspondingResults) {
        this.correspondingResults = correspondingResults;
    }
}
