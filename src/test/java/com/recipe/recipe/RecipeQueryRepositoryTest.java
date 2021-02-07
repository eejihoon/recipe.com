package com.recipe.recipe;

import com.recipe.domain.Ingredient;
import com.recipe.domain.Member;
import com.recipe.domain.Recipe;
import com.recipe.member.MemberRepository;
import com.recipe.member.WithMockCutstomUser;
import com.recipe.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class RecipeQueryRepositoryTest {
    @Autowired RecipeQueryRepository recipeQueryRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired MemberRepository memberRepository;

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

    @DisplayName("내가 등록한 레시피 조회")
    @WithMockCutstomUser
    @Test
    void testFindByMember() {
        Pageable of = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = memberRepository.findAll().get(0);

        Page<RecipeDto> byMember = recipeQueryRepository.findByMember(member, of);

        assertTrue(byMember.isEmpty());

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

        recipe.setMember(member);

        recipeRepository.save(recipe);

        Page<RecipeDto> registeredMember = recipeQueryRepository.findByMember(member, of);

        assertFalse(registeredMember.isEmpty());
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