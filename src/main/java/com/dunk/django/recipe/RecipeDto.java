package com.dunk.django.recipe;

import com.dunk.django.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @Getter @Setter @ToString
public class RecipeDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String description;
    private int viewCount;
    private String nickname;
    private Long likeCount;

}
