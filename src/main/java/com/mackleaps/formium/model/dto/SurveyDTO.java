package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.model.survey.Survey;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SurveyDTO extends DTO<Survey,SurveyDTO> {

    @Id
    private Long id;

    @Size(max = 10)
    private String prefix;

    @NotNull(message = "O título do questionário não pode estar vazio")
    @Size(min = 1, max = 300, message = "O título deve ter entre 1 e 300 caracteres")
    private String title;

    @Size(max = 2000, message = "O corpo pode ter no máximo 2000 caracteres")
    private String description;

    @java.beans.ConstructorProperties({"id", "prefix", "title", "description"})
    public SurveyDTO(Long id, String prefix, String title, String description) {
        this.id = id;
        this.prefix = prefix;
        this.title = title;
        this.description = description;
    }

    public SurveyDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
