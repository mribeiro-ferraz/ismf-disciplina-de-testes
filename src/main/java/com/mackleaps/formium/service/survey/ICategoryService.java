package com.mackleaps.formium.service.survey;

import com.mackleaps.formium.exceptions.ComponentNotFoundException;
import com.mackleaps.formium.exceptions.DuplicatedComponentException;
import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.Container;

public interface ICategoryService {

    Category addCategory(Category newCategory, Long parentContainerId) throws DuplicatedComponentException;
    Category editCategory (Category category);
    Category getCategory(Long categoryId);
    Category getCategoryForUse(Long categoryId) throws ComponentNotFoundException;
    void deleteCategory (Long categoryId);
    Container getParent(Long categoryId);

}
