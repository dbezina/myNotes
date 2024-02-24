package com.bezina.myNotes.validators;

import com.bezina.myNotes.annotations.PasswordMatchers;
import com.bezina.myNotes.payload.request.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

public class PasswordMatchersValidator implements ConstraintValidator<PasswordMatchers, Object>{
    Logger LOG = LoggerFactory.getLogger(PasswordMatchersValidator.class);
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) o;
        boolean isEqual = signupRequest.getPassword().equals(signupRequest.getConfirmPassword());
        LOG.info("PasswordMatchersValidator / is Valid ()" + isEqual);
        return isEqual;
    }

    @Override
    public void initialize(PasswordMatchers constraintAnnotation) {
      //  ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
