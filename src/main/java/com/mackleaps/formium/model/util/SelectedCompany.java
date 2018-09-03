package com.mackleaps.formium.model.util;

public class SelectedCompany {

    private Long id;
    private String name;

    public SelectedCompany (Long id, String name) {

        if(!areValidValues(id,name))
            throw new IllegalArgumentException("Not valid values for selected company");

        this.id = id;
        this.name = name;
    }

    public SelectedCompany() {
    }

    public void select (Long id, String name) {
        if(areValidValues(id,name)) {
            this.id = id;
            this.name = name;
            return;
        }
        throw new IllegalArgumentException("Not valid values for selected company");
    }

    public void unselect () {
        this.id = null;
        this.name = null;
    }

    /**
     * If a company is selected, it has an id and a name.
     * If a selected company has one of them and not the other, these values are not valid
     * */
    private boolean areValidValues (Long id, String name) {
        return !(id == null && name != null || id != null && name == null);
    }

    //constants
    public static final String ANSWERING_FOR_IDENTIFIER = "AnsweringFor";
    public static final SelectedCompany NO_COMPANY_SELECTED = null;

    public static boolean isCompanySelected (SelectedCompany company) {
        if(company == NO_COMPANY_SELECTED)
            return false;

        return (company.getId() != null && company.getName() != null);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
