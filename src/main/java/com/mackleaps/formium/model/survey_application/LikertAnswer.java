package com.mackleaps.formium.model.survey_application;

import com.mackleaps.formium.model.survey.LikertQuestion;

import javax.persistence.Entity;

@Entity
public class LikertAnswer extends Answer<LikertQuestion> {

    private Integer selectedValue;

    @java.beans.ConstructorProperties({"selectedValue"})
    public LikertAnswer(Integer selectedValue) {
        this.selectedValue = selectedValue;
    }

    public LikertAnswer() {
    }

    public boolean withinRange() {

        boolean isLeftBigger = getCorrespondingQuestion().getLeftValue() >= getCorrespondingQuestion().getRightValue();

        return isLeftBigger ?  selectedValue <= getCorrespondingQuestion().getLeftValue()  &&
                               selectedValue >= getCorrespondingQuestion().getRightValue()
                :
                               selectedValue >= getCorrespondingQuestion().getRightValue() &&
                               selectedValue <= getCorrespondingQuestion().getLeftValue();
    }

    @Override
    boolean isValueNotAnsweredQuestion() {
        return selectedValue == null || !withinRange();
    }

    public Integer getSelectedValue() {
        return this.selectedValue;
    }

    public void setSelectedValue(Integer selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String toString() {
        return "LikertAnswer(selectedValue=" + this.getSelectedValue() + ")";
    }
}
