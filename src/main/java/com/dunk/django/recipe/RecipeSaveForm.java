package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data @NoArgsConstructor
public class RecipeSaveForm {
    private Long id;
    private String thumbnail;
    private String title;
    private String description;
    private String fullDescription;
    private String ingredients;
    private Integer cookingTime;

    @Builder
    public RecipeSaveForm(Long id, String thumbnail, String title, String description, String fullDescription, String ingredients, Integer cookingTime) {
        this.id = id;
        this.ingredients = "";
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.fullDescription = fullDescription;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
    }

    public Recipe toEntity() {
        Set<Ingredient> ingredientSet = ingredientsToSet();

        Recipe recipe = Recipe.builder()
                .thumbnail(thumbnail)
                .title(title)
                .description(description)
                .fullDescription(fullDescription)
                .cookingTime(cookingTime)
                .ingredients(ingredientSet)
                .build();

        return recipe;
    }

    private Set<Ingredient> ingredientsToSet() {
        if (ingredients == null)
            return new HashSet<>();

        // '깻잎, 상추, 된장' 형태로 들어온 문자열을 Set으로 바꾼다.
        String[] ingrSplit = ingrSplit = ingredients.split(",");
        Set<Ingredient> ingredientSet = Arrays.stream(ingrSplit)
                .map(Ingredient::new)
                .collect(Collectors.toSet());
        return ingredientSet;
    }
}
