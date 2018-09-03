package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.exceptions.DuplicatedComponentException;
import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.repository.survey.CategoryRepository;
import com.mackleaps.formium.repository.survey.ContainerRepository;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    private CategoryRepository categoryRepository;
    private ContainerRepository containerRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ContainerRepository containerRepository) {
        this.categoryRepository = categoryRepository;
        this.containerRepository = containerRepository;
    }

    @Override
    public Category addCategory(Category newCategory, Long parentContainerId) throws DuplicatedComponentException {

        Validate.notNull(newCategory);
        Validate.notNull(parentContainerId);

        //if parent does not exist
        Container parentFromRepo = containerRepository.findOne(parentContainerId);
        if(parentFromRepo == null)
            throw new ComponentNotFoundException();

        //adding to parent
        parentFromRepo.addCategory(newCategory);
        return categoryRepository.saveAndFlush(newCategory);
    }

    @Override
    public Category editCategory(Category category) {

        Validate.notNull(category);
        Validate.notNull(category.getId());
        Validate.notNull(category.getParent());
        Validate.notNull(category.getParent().getId());

        //if not persisted
        if(!categoryRepository.exists(category.getId()))
            throw new ComponentNotFoundException();

        //if parent does not exist
        Long parentId = category.getParent().getId();
        Container parent = containerRepository.findOne(parentId);
        if(parent == null)
            throw new ComponentNotFoundException();

        parent.editCategory(category);
        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public Category getCategory(Long categoryId) {
        Validate.notNull(categoryId);
        return categoryRepository.findOne(categoryId);
    }

    @Override
    public Category getCategoryForUse(Long categoryId) throws ComponentNotFoundException {

        Validate.notNull(categoryId);

        Category fromRepo = categoryRepository.findOne(categoryId);
        if(fromRepo == null)
            throw new ComponentNotFoundException();

        return fromRepo;
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Validate.notNull(categoryId);

        if(!categoryRepository.exists(categoryId))
            throw new ComponentNotFoundException();

        categoryRepository.delete(categoryId);
    }

    @Override
    public Container getParent(Long categoryId) {

        Validate.notNull(categoryId);

        Category category = getCategoryForUse(categoryId);
        return category.getParent();
    }
}
