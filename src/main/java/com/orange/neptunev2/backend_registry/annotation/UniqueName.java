package com.orange.neptunev2.backend_registry.annotation;

import com.orange.neptunev2.backend_registry.validator.UniqueNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  UniqueName {

    String message() default "{backend.name.already.exist}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
