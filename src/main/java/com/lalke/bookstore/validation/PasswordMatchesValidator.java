package com.lalke.bookstore.validation;

import com.lalke.bookstore.dto.UserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserDto user = (UserDto) obj;

        if (user.getPassword() == null || user.getMatchingPassword() == null) {
            return false;
        }

        return user.getPassword().equals(user.getMatchingPassword());
    }
}