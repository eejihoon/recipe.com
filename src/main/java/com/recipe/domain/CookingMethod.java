package com.recipe.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class CookingMethod {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipe recipe;

    private int sequence;

    private String description;

    private String image;

    public CookingMethod(int sequence, String description, String image) {
        this.sequence = sequence;
        this.description = description;
        this.image = image;
    }

    public void addRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
