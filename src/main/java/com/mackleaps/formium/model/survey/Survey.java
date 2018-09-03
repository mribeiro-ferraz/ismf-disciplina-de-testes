package com.mackleaps.formium.model.survey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Survey extends Container {

    @Column(length = 10)
    private String prefix;

    @Column(length = 300, nullable = false)
    private String title;

    @Lob
    @Column(length = 2000)
    private String description;

    @java.beans.ConstructorProperties({"prefix", "title", "description"})
    public Survey(String prefix, String title, String description) {
        this.prefix = prefix;
        this.title = title;
        this.description = description;
    }

    public Survey() {
    }

    @Override
    public boolean isSurvey() {
        return true;
    }

    @Override
    public String descriptor() {
        return this.title;
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
