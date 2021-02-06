package com.recipe.recipe;

import com.recipe.domain.Ingredient;
import com.recipe.domain.Like;
import com.recipe.domain.Member;
import com.recipe.domain.Recipe;
import com.recipe.member.LikeRepository;
import com.recipe.member.MemberRepository;
import com.recipe.member.WithMockCutstomUser;
import com.recipe.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LikeApiControllerTest {
    @Autowired MemberRepository memberRepository;
    @Autowired LikeRepository likeRepository;
    @Autowired MockMvc mockMvc;
    @Autowired RecipeRepository recipeRepository;
    @Autowired TestRestTemplate restTemplate;

    @DisplayName("좋아요 카운트")
    @WithMockCutstomUser
    @Test
    void testGetCount() throws Exception {
        Recipe recipe = addRecipe();
        Member member = memberRepository.findByEmail("test@email.com").orElseThrow();

//        ResponseEntity<ResponseEntity> forEntity = restTemplate.getForEntity("/like/" + recipe.getId(), ResponseEntity.class);
//
//        assertEquals(forEntity.getStatusCode(), HttpStatus.OK);

        mockMvc.perform(get("/like/"+recipe.getId()))
                .andExpect(status().isOk());
    }

    @DisplayName("좋아요 취소")
    @WithMockCutstomUser
    @Test
    void testCancelLike() throws Exception {
        Member member = memberRepository.findByEmail("test@email.com").orElseThrow();
        Recipe recipe = addRecipe();
        likeRepository.save(new Like(recipe, member));

        List<Like> allLike = likeRepository.findAll();

        assertNotNull(allLike.get(0).getId());
        assertTrue(allLike.size() > 0);

        mockMvc.perform(delete("/like/"+recipe.getId()))
                .andExpect(status().isOk());

        List<Like> likes = likeRepository.findAll();

        assertTrue(likes.isEmpty());
        assertTrue(likes.size() == 0);

    }

    @DisplayName("좋아요 테스트")
    @WithMockCutstomUser
    @Test
    void testCreateLike() throws Exception {
        Recipe recipe = addRecipe();

        mockMvc.perform(post("/like/"+recipe.getId()))
                .andExpect(status().isOk());

        Like like = likeRepository.findAll().get(0);

        assertNotNull(like);
        assertNotNull(like.getMember().getId());
        assertNotNull(like.getRecipe().getId());
    }

    @DisplayName("좋아요 중복 테스트 - fail")
    @WithMockCutstomUser
    @Test
    void testDuplicateLike() throws Exception {
        Recipe recipe = addRecipe();

        mockMvc.perform(post("/like/"+recipe.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/like/"+recipe.getId()))
                .andExpect(status().isBadRequest());

        Like like = likeRepository.findAll().get(0);

        assertNotNull(like);
        assertNotNull(like.getMember().getId());
        assertNotNull(like.getRecipe().getId());
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