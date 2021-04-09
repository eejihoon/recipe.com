package com.recipe.mail;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String message;
}
