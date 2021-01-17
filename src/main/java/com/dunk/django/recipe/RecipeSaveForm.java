package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import lombok.Data;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RecipeSaveForm {
    private String thumbnail;
    private String title;
    private String description;
    private String fullDescription;
    private String ingredients;
    private Integer cookingTime;

    public Recipe toEntity() {

        String[] ingrSplit = ingredients.split(",");

        Set<Ingredient> ingredientSet = Arrays.stream(ingrSplit)
                                            .map(Ingredient::new)
                                            .collect(Collectors.toSet());

        Recipe recipe = Recipe.builder()
                .thumbnail(thumbnail)
                .title(title)
                .description(description)
                .fullDescription(fullDescription)
                .cookingTime(cookingTime)
                .build();

        ingredientSet.forEach(row -> row.add(recipe));

        recipe.setIngredients(ingredientSet);

        return recipe;
    }
}
