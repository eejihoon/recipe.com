package com.dunk.django.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Ingredient {
    @Id @GeneratedValue
    Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String ingredient;

    private String quantity;

    @ManyToOne
    IngredientType ingredientType;

    @Builder
    public Ingredient(Recipe recipe, String ingredient, String quantity, IngredientType ingredientType) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.ingredientType = ingredientType;
    }
}
