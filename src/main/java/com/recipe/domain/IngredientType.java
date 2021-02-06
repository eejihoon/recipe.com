package com.recipe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class IngredientType {
    @Id @GeneratedValue
    private Long id;

    private String ingredientType;

    public IngredientType(String ingredientType) {
        this.ingredientType = ingredientType;
    }
}
