package com.dunk.django.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Builder
@Getter @Setter
@Entity
public class Recipe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String thumbnail;

    private String description;

    @Lob
    private String fullDescription;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sequence ASC")
    Set<CookingMethod> cookingMethods = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private FoodNation foodNation; //한중일양식

    @ManyToOne(fetch = FetchType.LAZY)
    private FoodType foodType;  //국, 반찬, 찌개...

    private Integer cookingTime;

    private int servings;

    @Builder
    public Recipe(String title, String thumbnail, String description, String fullDescription, Set<Ingredient> ingredients, Integer cookingTime) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.fullDescription = fullDescription;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
    }
}
