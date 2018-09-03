package com.mackleaps.formium.model.survey;

import com.mackleaps.formium.model.survey_application.Answer;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question <T extends Answer> extends AuditableEntity implements SurveyComponent<Question> {

    @ManyToOne
    @JoinColumn
    private Container parent;

    @Lob
    @Column(length = 2000, nullable = false)
    private String header;

    @java.beans.ConstructorProperties({"parent", "header"})
    public Question(Container parent, String header) {
        this.parent = parent;
        this.header = header;
    }

    public Question() {
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isUniqueComparedTo(Question otherQuestion) {
        return !this.header.equals(otherQuestion.getHeader());
    }

    @Override
    public boolean isUniqueComparedTo(String otherQuestionHeader) {
        return !this.header.equals(otherQuestionHeader);
    }

    @Override
    public boolean sameEntity(Question otherQuestion) {

        Validate.notNull(otherQuestion);

        //validating comparison data
        if(this.getId() == null || otherQuestion.getId() == null)
            throw new NullPointerException("Trying to compare objects with one of them having a null Id");

        return this.getId().equals(otherQuestion.getId());
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
    public boolean equals(Object o) {
        if(!(o instanceof Question)) {
            return false;
        }
        return sameEntity((Question) o);
    }

    public abstract Class<T> getAnswerType();

    public Container getParent() {
        return this.parent;
    }

    public String getHeader() {
        return this.header;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
