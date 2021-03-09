package com.orange.neptunev2.backend_registry.validator;

import com.orange.neptunev2.backend_registry.annotation.UniqueHost;
import com.orange.neptunev2.backend_registry.repository.BackendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueHostValidator implements ConstraintValidator<UniqueHost, String> {

    @Autowired
    BackendRepository backendRepository;

    @Override
    public boolean isValid(String host, ConstraintValidatorContext constraintValidatorContext) {

        return !backendRepository.existsByHost(host);

    }
}
