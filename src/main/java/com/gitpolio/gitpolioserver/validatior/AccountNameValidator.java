package com.gitpolio.gitpolioserver.validatior;

import com.gitpolio.gitpolioserver.annotation.AccountName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AccountNameValidator implements ConstraintValidator<AccountName, String>{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !Pattern.compile("[ !@#$%^&*(),.?\\\":{}|<>]").matcher(value).find();
    }
}
