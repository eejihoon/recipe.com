package com.dunk.django.member;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
public class SignupRequestDto {

    @Length(min = 4, max = 12)
    @NotBlank
    String account;

    @Length(min = 8, max = 16)
    @NotBlank
    String password;

    @Length(min = 8, max = 16)
    @NotBlank
    String confirmPassword;

    @NotBlank
    String name;
}
