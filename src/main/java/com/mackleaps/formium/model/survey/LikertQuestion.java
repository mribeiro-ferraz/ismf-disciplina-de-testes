package com.mackleaps.formium.model.survey;

import com.mackleaps.formium.model.survey_application.LikertAnswer;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LikertQuestion extends Question <LikertAnswer> {

    @Column(length = 50, nullable = false)
    private String leftText;

    @Column(length = 50, nullable = false)
    private String rightText;

    @Column(nullable = false)
    private Integer leftValue;

    @Column(nullable = false)
    private Integer rightValue;

    @java.beans.ConstructorProperties({"leftText", "rightText", "leftValue", "rightValue"})
    public LikertQuestion(String leftText, String rightText, Integer leftValue, Integer rightValue) {
        this.leftText = leftText;
        this.rightText = rightText;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public LikertQuestion() {
    }

    @Override
    public Class<LikertAnswer> getAnswerType() {
        return LikertAnswer.class;
    }

    public String getLeftText() {
        return this.leftText;
    }

    public String getRightText() {
        return this.rightText;
    }

    public Integer getLeftValue() {
        return this.leftValue;
    }

    public Integer getRightValue() {
        return this.rightValue;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public void setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
    }

    public void setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
    }
}
