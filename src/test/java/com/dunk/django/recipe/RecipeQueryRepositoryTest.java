package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Recipe;
import com.dunk.django.member.MemberRepository;
import com.dunk.django.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional
@SpringBootTest
class RecipeQueryRepositoryTest {
    @Autowired RecipeQueryRepository recipeQueryRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void addRecipe() {
        for (int i = 0; i < 100; i++) {
            Set<Ingredient> ingredients = new HashSet();
            ingredients.add(new Ingredient("ingr1"));
            ingredients.add(new Ingredient("ingr2"));
            ingredients.add(new Ingredient("ingr3"));

            Recipe recipe = Recipe.builder()
                    .thumbnail("test")
                    .title("recipe-title!!!!!")
                    .fullDescription("test")
                    .description("test")
                    .ingredients(ingredients)
                    .cookingTime(11)
                    .build();

            recipeRepository.save(recipe);
        }
    }

    @Test
    void testFindByRecipeTitleWithLikeCount () {
        Pageable of = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<RecipeDto> byRecipeTitleWithLikeCount = recipeQueryRepository.findAllRecipeAndSearchWithPaging(null, of);

        for (RecipeDto recipeDto : byRecipeTitleWithLikeCount) {
            System.out.println("recipeDto : " + recipeDto);
        }
    }
}