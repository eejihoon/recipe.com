package com.recipe.member.vaildation;

import com.recipe.member.dto.ChangePasswordRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ChangePasswordRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordRequest changePasswordRequest = (ChangePasswordRequest) target;
        if (passwordConfirm(changePasswordRequest))
            errors.rejectValue("confirmPassword", "wrong.value", "비밀번호가 일치하지 않습니다.");
    }

    private boolean passwordConfirm(ChangePasswordRequest changePasswordRequest) {
        return !changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword());
    }
}
