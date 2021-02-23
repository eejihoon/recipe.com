package com.recipe.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ChangePasswordRequest {
    @Length(min = 8)
    @NotBlank
    String password;

    @Length(min = 8, max = 16)
    @NotBlank
    String confirmPassword;
}
