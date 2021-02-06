package com.recipe.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Recipe extends BaseEntity {
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
    @OrderBy("id ASC")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int viewCount;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();

    @Builder
    public Recipe(Member member, String title, String thumbnail, String description, String fullDescription, Set<Ingredient> ingredients, Integer cookingTime) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.fullDescription = fullDescription;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
        this.member = member;
    }

    public void update(Recipe recipe) {
        this.thumbnail = recipe.thumbnail;
        this.title = recipe.title;
        this.description = recipe.getDescription();
        this.fullDescription = recipe.getFullDescription();
        this.cookingTime = recipe.getCookingTime();
        if (recipe.getIngredients().size() != 0) {
            getIngredients().forEach(ingredient -> ingredient.removeId());
            this.ingredients = recipe.getIngredients();
            this.ingredients.forEach(ingredient -> ingredient.add(this));
        }
    }

    public String getIngredientsString() {
        String result="";

        for (Ingredient ingredient : ingredients)
            result += ingredient.getIngredient() + ",";

        return result;
    }

    public void viewCount() {
        this.viewCount++;
    }
}
