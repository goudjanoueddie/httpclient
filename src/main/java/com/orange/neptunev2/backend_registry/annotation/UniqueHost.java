package com.orange.neptunev2.backend_registry.annotation;



import com.orange.neptunev2.backend_registry.validator.UniqueHostValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueHostValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueHost {

    String message() default "{backend.host.already.exist}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
