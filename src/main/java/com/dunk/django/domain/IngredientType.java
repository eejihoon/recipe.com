package com.dunk.django.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity @AllArgsConstructor @Builder @NoArgsConstructor
public class IngredientType {
    @Id
    private Long id;

    private String ingredientType;
}
