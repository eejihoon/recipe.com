package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Like;
import com.dunk.django.domain.Member;
import com.dunk.django.domain.Recipe;
import com.dunk.django.member.LikeRepository;
import com.dunk.django.member.MemberRepository;
import com.dunk.django.member.WithMockCutstomUser;
import com.dunk.django.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@WithMockCutstomUser
@SpringBootTest
class LikeQueryRepositoryTest {
    @Autowired LikeQueryRepository likeQueryRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired LikeRepository likeRepository;

    @Test
    void testFindScrappedRecipe() {
        Member member = memberRepository.findAll().get(0);

        addRecipe(member);
        addLike(member);

        Pageable pageable = PageRequest.of(0, 10);

        Page<RecipeDto> likedRecipe = likeQueryRepository.findLikedRecipe(member, pageable);

        likedRecipe.getContent().forEach(recipeDto -> System.out.println(recipeDto));

        assertTrue(likedRecipe.getContent().size() > 0);
    }

    private void addLike(Member member) {
        List<Recipe> recipes = recipeRepository.findAll();

        for (Recipe recipe : recipes) {
            likeRepository.save(new Like(recipe, member));
        }
    }

    private void addRecipe(Member member) {
        for (int i = 0; i < 10; i++) {
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
                    .member(member)
                    .build();
            recipeRepository.save(recipe);
        }
    }


}