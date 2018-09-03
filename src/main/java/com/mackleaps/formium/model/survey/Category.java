package com.mackleaps.formium.model.survey;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
public class Category extends Container implements SurveyComponent<Category> {

    @ManyToOne
    @JoinColumn
    private Container parent;

    @Column(length = 300, nullable = false)
    private String title;

    @Lob
    @Column(length = 2000)
    private String description;

    @java.beans.ConstructorProperties({"parent", "title", "description"})
    public Category(Container parent, String title, String description) {
        this.parent = parent;
        this.title = title;
        this.description = description;
    }

    public Category() {
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isUniqueComparedTo (Category otherCategory) {
        return !this.getTitle().equals(otherCategory.getTitle());
    }

    @Override
    public boolean isUniqueComparedTo(String otherCategoryTitle) {
        return !this.getTitle().equals(otherCategoryTitle);
    }

    @Override
    public boolean sameEntity(Category otherCategory) {

        Validate.notNull(otherCategory);

        //validating comparison data
        if(this.getId() == null || otherCategory.getId() == null)
            throw new NullPointerException("Trying to compare objects with one of them having a null Id");

        return this.getId().equals(otherCategory.getId());
    }

    @Override
    public void associateToParent(Container parent) {
        Validate.notNull(parent);
        this.parent = parent;
    }

    @PreRemove
    @Override
    public void disassociateFromParent() {

        if(this.parent == null)
            throw new IllegalStateException("Trying to disassociate parent from a component that does not have one.");

        this.parent = null;
    }

    @Override
    public boolean isSurvey() {
        return false;
    }

    @Override
    public String descriptor() {
        return this.title;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Category)) {
            return false;
        }
        return sameEntity((Category) o);
    }

    public Survey getSurveyForCurrentCategory () {

        if(parent.isSurvey())
            return (Survey) parent;
        else
            return ((Category) parent).getSurveyForCurrentCategory();
    }

    public Container getParent() {
        return this.parent;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
