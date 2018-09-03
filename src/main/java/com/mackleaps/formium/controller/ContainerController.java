package com.mackleaps.formium.controller;

import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.model.util.Message;
import com.mackleaps.formium.service.survey.ICategoryService;
import com.mackleaps.formium.service.survey.IContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/system/container/")
public class ContainerController {

    private ICategoryService categoryService;
    private IContainerService containerService;

    @Autowired
    public ContainerController (ICategoryService categoryService,
                                IContainerService containerService) {

        this.categoryService = categoryService;
        this.containerService = containerService;
    }

    @GetMapping("/parent/{childId}")
    public String backToParent(@PathVariable Long childId, @ModelAttribute("message") Message message,
                               RedirectAttributes redirectAttributes) {

        if(message != null && !message.isEmpty())
            redirectAttributes.addFlashAttribute("message", message);

        Container parent = categoryService.getParent(childId);
        return getContainerUrl(parent.getId());
    }

    @GetMapping("/components/{parentId}")
    public String goToContainerComponents(@PathVariable Long parentId, @ModelAttribute("message") Message message,
                                          RedirectAttributes redirectAttributes) {

        if(message != null && !message.isEmpty())
            redirectAttributes.addFlashAttribute("message", message);

        return getContainerUrl(parentId);
    }

    private String getContainerUrl(Long containerId) {

        //getting to the appropriated redirection address
        //if survey:   redirect:/system/surveys/{surveyId}
        //if category: redirect:/system/categories/{category}

        Container container = containerService.getContainerForUse(containerId);
        String typeContainer = container.isSurvey() ? "surveys" : "categories";

        return "redirect:/system/{typeContainer}/{id}"
                .replace("{typeContainer}", typeContainer)
                .replace("{id}", container.getId().toString());

    }

}
