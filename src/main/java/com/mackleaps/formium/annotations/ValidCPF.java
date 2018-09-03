package com.mackleaps.formium.annotations;

import com.mackleaps.formium.model.util.validators.CPFValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE,FIELD,ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CPFValidator.class)
@Documented
public @interface ValidCPF {

    String message () default "CPF format is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
