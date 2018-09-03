package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.model.survey.LikertQuestion;
import com.mackleaps.formium.model.survey_application.LikertAnswer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AnswerLikertQuestionDTO  extends DTO <LikertAnswer, AnswerLikertQuestionDTO> {

    @Id
    private Long id;
    private Integer selectedValue; //this needs to be a non primitive type
    private Long correspondingQuestionId;
    private String correspondingQuestionHeader;
    private String correspondingQuestionLeftText;
    private String correspondingQuestionRightText;
    private int correspondingQuestionLeftValue;
    private int correspondingQuestionRightValue;

    @java.beans.ConstructorProperties({"id", "selectedValue", "correspondingQuestionId", "correspondingQuestionHeader", "correspondingQuestionLeftText", "correspondingQuestionRightText", "correspondingQuestionLeftValue", "correspondingQuestionRightValue"})
    public AnswerLikertQuestionDTO(Long id, Integer selectedValue, Long correspondingQuestionId, String correspondingQuestionHeader, String correspondingQuestionLeftText, String correspondingQuestionRightText, int correspondingQuestionLeftValue, int correspondingQuestionRightValue) {
        this.id = id;
        this.selectedValue = selectedValue;
        this.correspondingQuestionId = correspondingQuestionId;
        this.correspondingQuestionHeader = correspondingQuestionHeader;
        this.correspondingQuestionLeftText = correspondingQuestionLeftText;
        this.correspondingQuestionRightText = correspondingQuestionRightText;
        this.correspondingQuestionLeftValue = correspondingQuestionLeftValue;
        this.correspondingQuestionRightValue = correspondingQuestionRightValue;
    }

    public AnswerLikertQuestionDTO() {
    }

    @Override
    public AnswerLikertQuestionDTO convertEntityToDto(LikertAnswer entity, Class<AnswerLikertQuestionDTO> dtoClass) {

        Converter<LikertAnswer, AnswerLikertQuestionDTO> converter = context -> {

            AnswerLikertQuestionDTO dto = new AnswerLikertQuestionDTO();

            dto.setId(entity.getId());
            dto.setSelectedValue(entity.getSelectedValue());
            dto.setCorrespondingQuestionId(entity.getCorrespondingQuestion().getId());
            dto.setCorrespondingQuestionHeader(entity.getCorrespondingQuestion().getHeader());
            dto.setCorrespondingQuestionLeftText(entity.getCorrespondingQuestion().getLeftText());
            dto.setCorrespondingQuestionRightText(entity.getCorrespondingQuestion().getRightText());
            dto.setCorrespondingQuestionLeftValue(entity.getCorrespondingQuestion().getLeftValue());
            dto.setCorrespondingQuestionRightValue(entity.getCorrespondingQuestion().getRightValue());

            return dto;
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(converter);
        modelMapper.map(entity,this);
        return this;
    }

    @Override
    public LikertAnswer convertToExistingEntity(LikertAnswer existingEntity) {
        existingEntity.setSelectedValue(this.getSelectedValue());
        return existingEntity;
    }

    @Override
    public LikertAnswer convertToNonExistingEntity(Class<LikertAnswer> classType) {

        LikertQuestion correspondingQuestion = new LikertQuestion();
        correspondingQuestion.setId(this.getCorrespondingQuestionId());

        LikertAnswer answer = new LikertAnswer();
        answer.setId(this.getId());
        answer.setSelectedValue(this.getSelectedValue());
        answer.setCorrespondingQuestion(correspondingQuestion);

        return answer;
    }

    public Long getId() {
        return this.id;
    }

    public Integer getSelectedValue() {
        return this.selectedValue;
    }

    public Long getCorrespondingQuestionId() {
        return this.correspondingQuestionId;
    }

    public String getCorrespondingQuestionHeader() {
        return this.correspondingQuestionHeader;
    }

    public String getCorrespondingQuestionLeftText() {
        return this.correspondingQuestionLeftText;
    }

    public String getCorrespondingQuestionRightText() {
        return this.correspondingQuestionRightText;
    }

    public int getCorrespondingQuestionLeftValue() {
        return this.correspondingQuestionLeftValue;
    }

    public int getCorrespondingQuestionRightValue() {
        return this.correspondingQuestionRightValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSelectedValue(Integer selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void setCorrespondingQuestionId(Long correspondingQuestionId) {
        this.correspondingQuestionId = correspondingQuestionId;
    }

    public void setCorrespondingQuestionHeader(String correspondingQuestionHeader) {
        this.correspondingQuestionHeader = correspondingQuestionHeader;
    }

    public void setCorrespondingQuestionLeftText(String correspondingQuestionLeftText) {
        this.correspondingQuestionLeftText = correspondingQuestionLeftText;
    }

    public void setCorrespondingQuestionRightText(String correspondingQuestionRightText) {
        this.correspondingQuestionRightText = correspondingQuestionRightText;
    }

    public void setCorrespondingQuestionLeftValue(int correspondingQuestionLeftValue) {
        this.correspondingQuestionLeftValue = correspondingQuestionLeftValue;
    }

    public void setCorrespondingQuestionRightValue(int correspondingQuestionRightValue) {
        this.correspondingQuestionRightValue = correspondingQuestionRightValue;
    }

    public String toString() {
        return "AnswerLikertQuestionDTO(id=" + this.getId() + ", selectedValue=" + this.getSelectedValue() + ", correspondingQuestionId=" + this.getCorrespondingQuestionId() + ", correspondingQuestionHeader=" + this.getCorrespondingQuestionHeader() + ", correspondingQuestionLeftText=" + this.getCorrespondingQuestionLeftText() + ", correspondingQuestionRightText=" + this.getCorrespondingQuestionRightText() + ", correspondingQuestionLeftValue=" + this.getCorrespondingQuestionLeftValue() + ", correspondingQuestionRightValue=" + this.getCorrespondingQuestionRightValue() + ")";
    }

    /**
     * This will be used to display the questions and get all the answers at the same time
     * TODO this needs to incorporate all types of questions. Think about this
     * */
    public static class Wrapper {

        @NotNull
        private Long idCurrentContainer;

        private List<AnswerLikertQuestionDTO> answersList;

        @java.beans.ConstructorProperties({"idCurrentContainer", "answersList"})
        public Wrapper(Long idCurrentContainer, List<AnswerLikertQuestionDTO> answersList) {
            this.idCurrentContainer = idCurrentContainer;
            this.answersList = answersList;
        }

        public Wrapper() {
        }

        /**
         * Removing items that are not going to be used from the list
         * Because the answer was not given before, therefore is not persisted
         * As well as it's value is not selected
         *
         * Allowing not selected values to pass through for it will be use to delete unselected answers
         * */
        public void removeNotSelected(){
            for(int i = answersList.size() - 1; i >= 0; i--)
                if(answersList.get(i).getSelectedValue() == null && answersList.get(i).getId() == null)
                    answersList.remove(i);

        }

        public String toString() {
            return "AnswerLikertQuestionDTO.Wrapper(idCurrentContainer=" + this.idCurrentContainer + ", answersList=" + this.answersList + ")";
        }

        public Long getIdCurrentContainer() {
            return this.idCurrentContainer;
        }

        public List<AnswerLikertQuestionDTO> getAnswersList() {
            return this.answersList;
        }

        public void setIdCurrentContainer(Long idCurrentContainer) {
            this.idCurrentContainer = idCurrentContainer;
        }

        public void setAnswersList(List<AnswerLikertQuestionDTO> answersList) {
            this.answersList = answersList;
        }
    }

}
