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
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String ingredient;

    private String quantity;

    @ManyToOne
    IngredientType ingredientType;

    public Ingredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void add(Recipe recipe) {
        this.recipe = recipe;
    }
}
