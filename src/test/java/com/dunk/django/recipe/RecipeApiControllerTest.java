package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Like;
import com.dunk.django.domain.Member;
import com.dunk.django.domain.Recipe;
import com.dunk.django.member.LikeRepository;
import com.dunk.django.member.MemberRepository;
import com.dunk.django.member.WithMockCutstomUser;
import com.dunk.django.recipe.repository.IngredientRepository;
import com.dunk.django.recipe.repository.IngredientTypeRepository;
import com.dunk.django.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
*   TODO
*    RecipeControllerTest, RecipeApiControllerTest
*       중복되는 부분 리팩토링 하기
*       @WithMockCustomUser랑 addRecipe()!!
* */

@WithMockCutstomUser
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class RecipeApiControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired RecipeRepository recipeRepository;
    @Autowired IngredientTypeRepository ingredientTypeRepository;
    @Autowired IngredientRepository ingredientRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired MemberRepository memberRepository;
    @Autowired LikeRepository likeRepository;

    @BeforeEach
    void deleteAll() {
        recipeRepository.deleteAll();
    }

    @DisplayName("레시피 등록 테스트")
    @WithMockCutstomUser
    @Test
    void testRecipeSave() throws Exception {
        RecipeSaveForm recipeSaveForm = new RecipeSaveForm();
        recipeSaveForm.setTitle("Test Recipe!!");
        recipeSaveForm.setFullDescription("full Description!");
        recipeSaveForm.setIngredients("재료,이렇게,들어와,콤마로,구문해,태그를,썼기,때문이야");
        recipeSaveForm.setDescription("Test Description");
        recipeSaveForm.setThumbnail("image URL");
        recipeSaveForm.setCookingTime(20);

        String requesetJson = objectMapper.writeValueAsString(recipeSaveForm);
        System.out.println("requesetJson : " + requesetJson);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .content(requesetJson))
                .andExpect(status().isOk());

        List<Recipe> recipes = recipeRepository.findAll();

        recipes.get(0).getIngredients().forEach(row -> System.out.println(row.getIngredient()));

        assertTrue(recipes.get(0).getIngredients().size() > 0);
        assertEquals(recipes.get(0).getTitle(), recipeSaveForm.getTitle());
        assertEquals(recipes.get(0).getMember().getEmail(), "test@email.com");
    }

    @DisplayName("레시피 수정 처리")
    @Test
    void testRecipeModifyPut() throws Exception {
        Recipe recipe = addRecipe();

        RecipeSaveForm recipeUpdateForm = RecipeSaveForm.builder()
                .thumbnail("recipe-thumbnail")
                .title("recipe-title")
                .description("recipe-description")
                .fullDescription("recipe-full-description")
                .ingredients("tag1,tag2,tag3,tag4,tag5")
                .cookingTime(22)
                .member(memberRepository.findAll().get(0))
                .build();

        mockMvc.perform(put("/recipe/"+recipe.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(recipeUpdateForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Recipe updatedRecipe = recipeRepository.findWithAllById(recipe.getId());

        assertEquals(updatedRecipe.getTitle(), recipeUpdateForm.getTitle());
        assertEquals(updatedRecipe.getThumbnail(), recipeUpdateForm.getThumbnail());
        assertEquals(updatedRecipe.getFullDescription(), recipeUpdateForm.getFullDescription());
        assertEquals(updatedRecipe.getIngredients().size(), recipeUpdateForm.toEntity().getIngredients().size());
        assertEquals(recipe.getMember().getEmail(), "test@email.com");

    }

    @DisplayName("레시피 삭제")
    @Test
    void testRemoveRecipe() throws Exception {
        Recipe recipe = addRecipe();

        assertTrue(recipeRepository.findById(recipe.getId()).isPresent());

        mockMvc.perform(delete("/recipe/"+recipe.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertFalse(recipeRepository.findById(recipe.getId()).isPresent());
        assertEquals(recipe.getMember().getEmail(), "test@email.com");
    }

    private Recipe addRecipe() {
        Recipe recipe = Recipe.builder()
                .thumbnail("test")
                .title("test-recipe")
                .fullDescription("test")
                .description("test")
                .ingredients(new HashSet<Ingredient>(Arrays.asList(new Ingredient("a"), new Ingredient("b"), new Ingredient("c"))))
                .cookingTime(11)
                .member(memberRepository.findAll().get(0))
                .build();

        Recipe save = recipeRepository.save(recipe);

        return save;
    }

}
