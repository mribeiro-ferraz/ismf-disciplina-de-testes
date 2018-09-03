package com.mackleaps.formium.model.util.validators;

import com.mackleaps.formium.annotations.UniqueDescriptor;
import com.mackleaps.formium.model.dto.SurveyComponentDTO;
import com.mackleaps.formium.model.survey.Container;
import com.mackleaps.formium.repository.survey.ContainerRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueDescriptorValidator implements ConstraintValidator<UniqueDescriptor, SurveyComponentDTO<Long>> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UniqueDescriptorValidator.class);
    private ContainerRepository containerRepository;

	@Autowired
	public UniqueDescriptorValidator(ContainerRepository containerRepository) {
		this.containerRepository = containerRepository;
	}

	@Override
	public void initialize(UniqueDescriptor unique) {

	}

	@Override
	public boolean isValid(SurveyComponentDTO<Long> value, ConstraintValidatorContext context) {

		String descriptor = value.getDescriptor();
		Long parentId = value.getParentId();
		Long componentId = value.getId();
		boolean isQuestion = value.isQuestion();

		return canInsertComponent(descriptor, parentId, componentId, isQuestion);
	}
	
	private boolean canInsertComponent(String descriptor, Long parentId, Long id, boolean isQuestion) {

		Container parent = containerRepository.findOne(parentId);
		return isQuestion ? parent.canQuestionBeInserted(descriptor) : parent.canCategoryBeInserted(descriptor);

	}
	
}
