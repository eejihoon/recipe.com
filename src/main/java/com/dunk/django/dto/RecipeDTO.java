package com.dunk.django.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipeDTO {

    private Long itemId;
    private String recipe_name;
    private String ingr_list;
    private String recipe,img;
    private LocalDateTime regdate, moddate;

    
}