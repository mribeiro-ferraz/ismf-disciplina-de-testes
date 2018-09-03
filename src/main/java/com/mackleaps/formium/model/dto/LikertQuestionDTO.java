package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.annotations.UniqueDescriptor;
import com.mackleaps.formium.model.survey.LikertQuestion;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@UniqueDescriptor(message = "O cabeçalho da pergunta deve ser único por categoria")
public class LikertQuestionDTO extends DTO<LikertQuestion, LikertQuestionDTO> implements SurveyComponentDTO<Long>{

    @Id
    private Long id;
    private Long parentId;

    @NotNull(message = "O cabeçalho da questão não pode estar vazio")
    @Size(min = 1, max = 2000, message = "O cabeçalho da questão pode ter no máximo 2000 caracteres")
    private String header;

    @NotNull(message = "O campo de valor à esquerda não pode estar vazio")
    @Size(min = 1, max = 50, message = "No máximo 50 caracteres para o campo de texto da extrema esquerda")
    private String leftText;

    @NotNull(message = "O campo de valor à direita não pode estar vazio")
    @Size(min = 1, max = 50, message = "No máximo 50 caracteres para os campos de texto da extrema direita")
    private String rightText;

    @Min(value = 0)
    @Max(value = 1)
    private Integer leftValue;

    @Min(value = 2)
    @Max(value = 7)
    private Integer rightValue;

    @java.beans.ConstructorProperties({"id", "parentId", "header", "leftText", "rightText", "leftValue", "rightValue"})
    public LikertQuestionDTO(Long id, Long parentId, String header, String leftText, String rightText, Integer leftValue, Integer rightValue) {
        this.id = id;
        this.parentId = parentId;
        this.header = header;
        this.leftText = leftText;
        this.rightText = rightText;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public LikertQuestionDTO() {
    }

    @Override
    public String getDescriptor() {
        return this.header;
    }

    @Override
    public boolean isQuestion() {
        return true;
    }

    @Override
    public LikertQuestionDTO convertEntityToDto(LikertQuestion entity, Class<LikertQuestionDTO> dtoClass) {

        Long parentId = entity.getParent().getId();

        PropertyMap<LikertQuestion,LikertQuestionDTO> propertyMap = new PropertyMap<LikertQuestion, LikertQuestionDTO>() {
            @Override
            protected void configure() {
                map().setParentId(parentId);
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);
        modelMapper.map(entity, this);
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public String getHeader() {
        return this.header;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setHeader(String header) {
        this.header = header;
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
