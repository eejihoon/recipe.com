package com.recipe.member;


import com.recipe.domain.Like;
import com.recipe.domain.Member;
import com.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Integer> countByRecipe(Recipe recipe);

    Optional<Like> findByMemberAndRecipe(Member member, Recipe recipe);

    List<Like> findByMember(Member member);
}
