package com.vincenzoracca.webflux.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidatorHandler {

    private final Validator validator;

    public <T> void validate(T o) {
        Set<ConstraintViolation<T>> validate = validator.validate(o);
        if(! validate.isEmpty()) {
            ConstraintViolation<T> violation = validate.stream().iterator().next();
            throw new ServerWebInputException(violation.toString());
        }
    }
}
