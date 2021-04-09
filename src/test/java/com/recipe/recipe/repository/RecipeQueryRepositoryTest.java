package com.recipe.recipe.repository;

import com.recipe.TestSet;
import com.recipe.member.domain.Member;
import com.recipe.member.WithMockCustomUser;
import com.recipe.recipe.dto.RecipeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class RecipeQueryRepositoryTest extends TestSet {
    @Autowired
    RecipeQueryRepository recipeQueryRepository;

    @DisplayName("내가 등록한 레시피 조회")
    @WithMockCustomUser
    @Test
    void testFindByMember() {
        //given
        Pageable of = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = memberRepository.findAll().get(0);

        Page<RecipeDto> byMember = recipeQueryRepository.findByMember(member, of);
        assertTrue(byMember.isEmpty());

        //when
        addRecipeDummies();

        //then
        Page<RecipeDto> registeredMember = recipeQueryRepository.findByMember(member, of);
        assertFalse(registeredMember.isEmpty());
        assertEquals(registeredMember.getSize(), 10);
        assertEquals(registeredMember.getContent().size(), 5);
    }

    @DisplayName("레시피 정보와 Like Count까지 조인해서 조회 - keyword가 null일 때 전체 조회")
    @WithMockCustomUser
    @Test
    void testFindByRecipeTitleWithLikeCount() {
        //given
        addRecipeDummies();

        //when
        Pageable of = PageRequest.of(0, 10, Sort.by("id").descending());

        //then
        Page<RecipeDto> byRecipeTitleWithLikeCount =
                recipeQueryRepository
                        .findAllRecipeAndSearchWithPaging(null, of);

        assertFalse(byRecipeTitleWithLikeCount.isEmpty());
        assertEquals(byRecipeTitleWithLikeCount.getTotalPages(), 1);

    }

    private void addRecipeDummies() {
        for (int i = 0; i < 5; i++) {
            //레시피 5개 등록
            addRecipe();
        }
    }
}