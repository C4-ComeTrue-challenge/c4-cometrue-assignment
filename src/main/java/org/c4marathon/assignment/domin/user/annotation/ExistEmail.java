package org.c4marathon.assignment.domin.user.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.c4marathon.assignment.domin.user.validation.ExistEmailValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistEmailValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistEmail {
    String message() default "이미 존재하는 이메일입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
