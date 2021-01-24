package com.dunk.django.recipe;

import com.dunk.django.domain.Ingredient;
import com.dunk.django.domain.Recipe;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    }

    @DisplayName("레시피 수정 처리")
    @WithMockUser
    @Test
    void testRecipeModifyPut() throws Exception {
        Recipe recipe = addRecipe();

        recipe.getIngredients().forEach(ingredient -> System.out.println(ingredient.getIngredient()));

        RecipeSaveForm recipeUpdateForm = RecipeSaveForm.builder()
                .thumbnail("recipe-thumbnail")
                .title("recipe-title")
                .description("recipe-description")
                .fullDescription("recipe-full-description")
                .ingredients("tag1,tag2,tag3,tag4,tag5")
                .cookingTime(22)
                .build();

        mockMvc.perform(put("/recipe/"+recipe.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(recipeUpdateForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Recipe updatedRecipe = recipeRepository.findWithAllById(recipe.getId());

        updatedRecipe.getIngredients().forEach(ingredient -> System.out.println(ingredient.getIngredient()));
        System.out.println("-----------------------");
        Recipe withAllById = recipeRepository.findWithAllById(updatedRecipe.getId());
        withAllById.getIngredients().forEach(i-> System.out.println(i));
        System.out.println("------------------------");
        System.out.println(withAllById.getIngredientsString());

        assertEquals(updatedRecipe.getTitle(), recipeUpdateForm.getTitle());
        assertEquals(updatedRecipe.getThumbnail(), recipeUpdateForm.getThumbnail());
        assertEquals(updatedRecipe.getFullDescription(), recipeUpdateForm.getFullDescription());
        assertEquals(updatedRecipe.getIngredients().size(), recipeUpdateForm.toEntity().getIngredients().size());
    }

    @DisplayName("레시피 삭제")
    @WithMockUser
    @Test
    void testRemoveRecipe() throws Exception {
        Recipe recipe = addRecipe();

        assertTrue(recipeRepository.findById(recipe.getId()).isPresent());

        mockMvc.perform(delete("/recipe/"+recipe.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertFalse(recipeRepository.findById(recipe.getId()).isPresent());
    }

    private Recipe addRecipe() {
        Recipe recipe = Recipe.builder()
                .thumbnail("test")
                .title("test-recipe")
                .fullDescription("test")
                .description("test")
                .ingredients(new HashSet<Ingredient>(Arrays.asList(new Ingredient("a"), new Ingredient("b"), new Ingredient("c"))))
                .cookingTime(11)
                .build();

        Recipe save = recipeRepository.save(recipe);

        return save;
    }

}
