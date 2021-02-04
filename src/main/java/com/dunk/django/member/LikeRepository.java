package com.dunk.django.member;


import com.dunk.django.domain.Like;
import com.dunk.django.domain.Member;
import com.dunk.django.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Integer> countByRecipe(Recipe recipe);

    Optional<Like> findByMemberAndRecipe(Member member, Recipe recipe);
}
