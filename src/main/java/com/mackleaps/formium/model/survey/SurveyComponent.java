package com.mackleaps.formium.model.survey;

public interface SurveyComponent <T extends SurveyComponent> {

    Container getParent();
    boolean isLeaf();
    boolean isUniqueComparedTo (T otherComponent);
    boolean isUniqueComparedTo (String otherComponentDescriptor);
    boolean sameEntity (T otherComponent);

    void associateToParent(Container parent);
    void disassociateFromParent();

}
