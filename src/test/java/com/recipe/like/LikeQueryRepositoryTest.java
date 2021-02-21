package com.recipe.like;

import com.recipe.ControllerTest;
import com.recipe.domain.Ingredient;
import com.recipe.domain.Like;
import com.recipe.domain.Member;
import com.recipe.domain.Recipe;
import com.recipe.member.LikeRepository;
import com.recipe.member.MemberRepository;
import com.recipe.member.WithMockCutstomUser;
import com.recipe.recipe.RecipeDto;
import com.recipe.recipe.repository.RecipeRepository;
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
@SpringBootTest
class LikeQueryRepositoryTest extends ControllerTest {
    @Autowired LikeQueryRepository likeQueryRepository;

    @Test
    @WithMockCutstomUser
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