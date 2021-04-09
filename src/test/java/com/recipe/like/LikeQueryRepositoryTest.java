package com.recipe.like;

import com.recipe.TestSet;
import com.recipe.like.domain.Like;
import com.recipe.member.domain.Member;
import com.recipe.recipe.domain.Recipe;
import com.recipe.like.repository.LikeQueryRepository;
import com.recipe.member.WithMockCustomUser;
import com.recipe.recipe.dto.RecipeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class LikeQueryRepositoryTest extends TestSet {
    @Autowired
    LikeQueryRepository likeQueryRepository;

    @Test
    @WithMockCustomUser
    void testFindScrappedRecipe() {
        //given
        Member member = memberRepository.findAll().get(0);

        addRecipe();
        addLike(member);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<RecipeDto> likedRecipe = likeQueryRepository.findLikedRecipe(member, pageable);

        //then
        assertTrue(likedRecipe.getContent().size() > 0);
    }

    private void addLike(Member member) {
        List<Recipe> recipes = recipeRepository.findAll();

        for (Recipe recipe : recipes) {
            likeRepository.save(new Like(recipe, member));
        }
    }

}