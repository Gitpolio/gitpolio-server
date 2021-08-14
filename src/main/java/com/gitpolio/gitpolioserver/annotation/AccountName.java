package com.gitpolio.gitpolioserver.annotation;

import com.gitpolio.gitpolioserver.validatior.AccountNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AccountNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountName {
    String message() default "계정 이름에는 특수문자를 사용하실 수 없습니다!";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
