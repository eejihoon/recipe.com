package com.recipe.like.repository;


import com.recipe.like.domain.Like;
import com.recipe.member.domain.Member;
import com.recipe.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Integer> countByRecipe(Recipe recipe);

    Optional<Like> findByMemberAndRecipe(Member member, Recipe recipe);
}
