package com.recipe.recipe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String ingredient;

    public Ingredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void add(Recipe recipe) {
        this.recipe = recipe;
    }

    public void removeId() {
        this.recipe = null;
    }
}
