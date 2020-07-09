package com.dunk.django.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFridgeDTO {

    private Long fno;

    private String username;

    private String ingr_name;

    private Long cno;

    private int expirationdate;

    private LocalDateTime regdate, updatedate;

    
}