package com.recipe.like;

import com.recipe.TestSet;
import com.recipe.like.domain.Like;
import com.recipe.member.domain.Member;
import com.recipe.recipe.domain.Recipe;
import com.recipe.member.WithMockCutstomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
public class LikeApiControllerTest extends TestSet {
    private final String LIKE_URL = "/like";

    @DisplayName("좋아요 카운트")
    @WithMockCutstomUser
    @Test
    void testGetCount() throws Exception {
        //given
        Recipe recipe = addRecipe();

        //when
        mockMvc.perform(get(API_URL+LIKE_URL+"/"+recipe.getId()))
                .andExpect(status().isOk());
    }

    @DisplayName("좋아요 취소")
    @WithMockCutstomUser
    @Test
    void testCancelLike() throws Exception {
        //given
        Member member = memberRepository.findByEmail(USER_EMAIL).orElseThrow();
        Recipe recipe = addRecipe();
        likeRepository.save(new Like(recipe, member));

        List<Like> allLike = likeRepository.findAll();

        assertNotNull(allLike.get(0).getId());
        assertTrue(allLike.size() > 0);

        //when
        mockMvc.perform(delete(API_URL+LIKE_URL+"/"+recipe.getId()))
                .andExpect(status().isOk());

        //then
        List<Like> likes = likeRepository.findAll();
        assertTrue(likes.isEmpty());
        assertTrue(likes.size() == 0);

    }

    @DisplayName("좋아요 등록 테스트")
    @WithMockCutstomUser
    @Test
    void testCreateLike() throws Exception {
        //given
        Recipe recipe = addRecipe();

        //when
        mockMvc.perform(post(API_URL+LIKE_URL+"/"+recipe.getId()))
                .andExpect(status().isOk());

        //then
        Like like = likeRepository.findAll().get(0);

        assertNotNull(like);
        assertNotNull(like.getMember().getId());
        assertNotNull(like.getRecipe().getId());
    }

    @DisplayName("좋아요 중복 테스트 - fail")
    @WithMockCutstomUser
    @Test
    void testDuplicateLike() throws Exception {
        //given
        Recipe recipe = addRecipe();

        //when
        mockMvc.perform(post(API_URL+LIKE_URL+"/"+recipe.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(post(API_URL+LIKE_URL+"/"+recipe.getId()))
                .andExpect(status().isBadRequest());

        //then
        Like like = likeRepository.findAll().get(0);

        assertNotNull(like);
        assertNotNull(like.getMember().getId());
        assertNotNull(like.getRecipe().getId());
    }
}