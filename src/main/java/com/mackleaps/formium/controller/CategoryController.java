package com.mackleaps.formium.controller;

import com.mackleaps.formium.model.dto.CategoryDTO;
import com.mackleaps.formium.model.dto.LikertQuestionDTO;
import com.mackleaps.formium.model.survey.Category;
import com.mackleaps.formium.model.survey.LikertQuestion;
import com.mackleaps.formium.model.util.Message;
import com.mackleaps.formium.model.util.PersistedEntitiesObjectMapper;
import com.mackleaps.formium.service.survey.ICategoryService;
import com.mackleaps.formium.service.survey.IContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/system/categories/")
@PropertySource(value = "classpath:/messages.properties", encoding = "UTF-8")
public class CategoryController {

    private ICategoryService categoryService;
    private PersistedEntitiesObjectMapper em;
    private IContainerService containerService;
    private Environment messageSource;

    @Autowired
    public CategoryController(ICategoryService categoryService,
                              PersistedEntitiesObjectMapper em,
                              IContainerService containerService,
                              Environment messageSource) {

        this.categoryService = categoryService;
        this.em = em;
        this.containerService = containerService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{categoryId}")
    public ModelAndView listElementsCategory (@PathVariable Long categoryId, @ModelAttribute Message message) {

        Category currentCategory = categoryService.getCategoryForUse(categoryId);
        List<LikertQuestion> questions = currentCategory.getQuestionByType(LikertQuestion.class);
        List<Category> subcategories = currentCategory.getSubcategories();

        ModelAndView mav = new ModelAndView();
        mav.addObject("currentCategory", em.convertEntityToDTO(currentCategory, CategoryDTO.class));
        mav.addObject("questions", em.convertListOfEntitiesIntoDtos(questions, LikertQuestionDTO.class));
        mav.addObject("subcategories", em.convertListOfEntitiesIntoDtos(subcategories, CategoryDTO.class));
        mav.addObject("path", containerService.pathToSurvey(categoryId));
        if(message != null && !message.isEmpty())
            mav.addObject("message", message);

        mav.setViewName("component");

        return mav;
    }

    @GetMapping("/new/{parentId}")
    public ModelAndView initNewCategory(@PathVariable Long parentId) {

        CategoryDTO category = new CategoryDTO();
        category.setParentId(parentId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("category", category);
        mav.setViewName("component_form");

        return mav;
    }

    @PostMapping("/new/")
    public String processNewCategory(@Valid @ModelAttribute("category") CategoryDTO category, BindingResult result,
                                     ModelMap model, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            model.addAttribute("message", new Message(result.getAllErrors()));
            return "component_form";
        }

        Category newCategory = em.convertDTOToEntity(category,Category.class);
        Category newlyCreated = categoryService.addCategory(newCategory, category.getParentId());

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("categories.creatingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/container/components/{id}"
               .replace("{id}", newlyCreated.getId().toString());
    }

    @GetMapping("/edit/{categoryId}")
    public ModelAndView initCategoryEditing(@PathVariable Long categoryId) {

        Category category = categoryService.getCategoryForUse(categoryId);
        CategoryDTO dto = em.convertEntityToDTO(category,CategoryDTO.class);

        ModelAndView mav = new ModelAndView();
        mav.addObject("category", dto);
        mav.setViewName("component_form");

        return mav;
    }

    @PostMapping("/edit/")
    public String processEditingCategory(@Valid @ModelAttribute ("category") CategoryDTO category, BindingResult result,
                                         ModelMap model, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            model.addAttribute("message", new Message(result.getAllErrors()));
            return "component_form";
        }

        Category currentCategory = em.convertDTOToEntity(category, Category.class);
        categoryService.editCategory(currentCategory);

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("categories.editingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/system/categories/{id}"
               .replace("{id}", currentCategory.getId().toString());
    }

    @PostMapping("/delete/{categoryId}")
    public ModelAndView processDeletingCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {

        Long parentId = categoryService.getParent(categoryId).getId();
        categoryService.deleteCategory(categoryId);

        //redirecting message to next controller
        String header = messageSource.getProperty("success.header");
        String body = messageSource.getProperty("categories.deletingwentok");
        Message message = new Message(header, body, Message.SUCCESS);
        redirectAttributes.addFlashAttribute("message", message);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/system/container/components/{id}"
                        .replace("{id}", parentId.toString()));

        return mav;
    }

}
