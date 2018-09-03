package com.mackleaps.formium.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {

	private static final String NOT_FOUND_PAGE = "404";
	private static final String INTERNAL_ERROR_PAGE = "500"; //non captured exceptions

	/**
	 * This binding will turn empty strings into null values
	 * */
	@InitBinder
	public void initBinder (WebDataBinder binder) {
		StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringTrimmer);
	}

    //If needed, implement here you @ExceptionHandler controller

    @Component
    public static class ErrorPageRegister implements ErrorViewResolver {

		@Override
		public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
			
			ModelAndView mv = new ModelAndView();

			switch(status.value()) {
				case 404:
					mv.setViewName(NOT_FOUND_PAGE);
					break;
				case 500:
					mv.setViewName(INTERNAL_ERROR_PAGE);
					break;
				default: 
					mv.setViewName(NOT_FOUND_PAGE);
			}
			
			return mv;
		}
    }
	
}
