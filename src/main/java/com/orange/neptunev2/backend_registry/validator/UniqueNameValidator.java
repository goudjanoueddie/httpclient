package com.orange.neptunev2.backend_registry.validator;

import com.orange.neptunev2.backend_registry.annotation.UniqueName;
import com.orange.neptunev2.backend_registry.repository.BackendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    @Autowired
    BackendRepository backendRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !backendRepository.existsByName(name) ;
    }

    @Override
    public void initialize(UniqueName constraintAnnotation) {
        constraintAnnotation.message();
    }
}
