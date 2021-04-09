package com.recipe.recipe.domain;

import com.recipe.domain.BaseEntity;
import com.recipe.like.domain.Like;
import com.recipe.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
public class Recipe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String thumbnail;

    private String description;

    private String fullDescription;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sequence ASC")
    Set<CookingMethod> cookingMethods = new HashSet<>();

    private Integer cookingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int viewCount;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();

    /*음식 정보*/
    float calorie;    //열량
    float carbohydrate;   //탄수화물
    float protein;    //단백질
    float fat;    //지방
    float natrium;   //나트륨
    String originalImage;
    String hashTag;

    //사용자가 레시피를 등록할 때 사용하는 생성자
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
        String result = "";

        for (Ingredient ingredient : ingredients)
            result += ingredient.getIngredient() + ",";

        return result;
    }

    public void viewCount() {
        this.viewCount++;
    }
}
