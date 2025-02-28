package com.cydeo.annotations;

import com.cydeo.config.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface EmailExist {
    String message() default "Username or Email exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
