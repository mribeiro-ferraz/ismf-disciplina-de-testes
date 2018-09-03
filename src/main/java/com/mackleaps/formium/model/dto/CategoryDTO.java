package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.annotations.UniqueDescriptor;
import com.mackleaps.formium.model.survey.Category;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@UniqueDescriptor(message = "O título desta categoria deve ser único por nível hierárquico")
public class CategoryDTO extends DTO<Category,CategoryDTO> implements SurveyComponentDTO<Long> {

    @Id
    private Long id;

    @NotNull
    private Long parentId;

    @NotNull(message = "O título de uma categoria não pode estar vazio")
    @Size(min = 1, max = 300, message = "O título deve ter no máximo 300 caracteres")
    private String title;

    @Size(max = 2000, message = "A descrição de uma categoria pode ter no máximo 2000 caracteres")
    private String description;

    @java.beans.ConstructorProperties({"id", "parentId", "title", "description"})
    public CategoryDTO(Long id, Long parentId, String title, String description) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.description = description;
    }

    public CategoryDTO() {
    }

    @Override
    public CategoryDTO convertEntityToDto(Category entity, Class<CategoryDTO> dtoClass) {

        //the mapping ahead was done so that we can get the parent id

        Long parentId = entity.getParent().getId();
//        String createdBy = entity.getCreatedBy().getUsername();
//        String lastEditedBy = entity.getLastModifiedBy().getUsername();

        PropertyMap<Category,CategoryDTO> propertyMap = new PropertyMap<Category, CategoryDTO>() {
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

    @Override
    public String getDescriptor() {
        return this.title;
    }

    @Override
    public boolean isQuestion() {
        return false;
    }

    public Long getId() {
        return this.id;
    }

    public Long getParentId() {
        return this.parentId;
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

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
