package com.mackleaps.formium.model.dto;

public interface SurveyComponentDTO<ID> {

	String getDescriptor();
	ID getParentId();
	ID getId();
	boolean isQuestion();
	
}
