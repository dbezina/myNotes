package com.bezina.myNotes.annotations;

import com.bezina.myNotes.validators.PasswordMatchersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchersValidator.class)
@Documented
public @interface PasswordMatchers {
    String message() default "Passwords are not the same";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
