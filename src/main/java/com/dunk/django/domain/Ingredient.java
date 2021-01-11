package com.dunk.django.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor
@Entity
public class Ingredient {
    @Id @GeneratedValue
    Long id;

    @Builder
    public Ingredient(Recipe recipe, String ingredient, String quantity, IngredientType ingredientType) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.ingredientType = ingredientType;
    }

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String ingredient;

    private String quantity;

    @ManyToOne
    IngredientType ingredientType;
}
