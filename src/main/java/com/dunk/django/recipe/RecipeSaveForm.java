package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RecipeSaveForm {
    private String name;
    private String description;
    Set<Ingredient> ingredients = new HashSet<>();
    Set<CookingMethod> cookingMethods = new HashSet<>();
    private FoodNation foodNation; //한중일양식
    private FoodType foodType;  //국, 반찬, 찌개...
    private Integer cookingTime;
    private int servings;
    private String thumbnail;

    public Recipe toEntity() {
        return Recipe.builder()
                .thumbnail(thumbnail)
                .name(name)
                .description(description)
                .cookingTime(cookingTime)
                .servings(servings)
                .foodType(foodType)
                .foodNation(foodNation)
                .cookingMethods(cookingMethods)
                .ingredients(ingredients)
                .build();
    }
}
