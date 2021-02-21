package com.recipe.recipe;

import com.recipe.TestSet;
import com.recipe.domain.*;
import com.recipe.member.WithMockCutstomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithMockCutstomUser
@Transactional
@SpringBootTest
class RecipeControllerTest extends TestSet {

    @DisplayName("자신의 게시물 조회하기 - 조회수 안 오름")
    @Test
    void testMineRecipeView() throws Exception {
        Recipe newRecipe = addRecipe();

        assertEquals(newRecipe.getViewCount(),0);

        for (int i=0; i<10; i++) {
            mockMvc.perform(get("/recipe?id="+ newRecipe.getId()))
                    .andExpect(status().isOk());

            Recipe recipe = recipeRepository.findById(newRecipe.getId()).orElseThrow();

            assertEquals(recipe.getViewCount(),0);
        }
    }

    @DisplayName("레시피 메인 폼")
    @Test
    void testRecipeIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipes"))
                .andDo(print());
    }

    @DisplayName("레시피 수정 폼")
    @Test
    void testRecipeModify() throws Exception {
        Recipe recipe = addRecipe();

        mockMvc.perform(get("/modify/"+recipe.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));
    }


    @DisplayName("레시피 검색")
    @Test
    void testFindByTitle() throws Exception {
       for (int i=0;i<10;i++) recipeRepository.save(addRecipe());

       mockMvc.perform(get("/")
       .param("title","recipe"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("recipes"));
    }

}