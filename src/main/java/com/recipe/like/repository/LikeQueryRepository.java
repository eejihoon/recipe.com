package com.recipe.like.repository;

import com.recipe.like.domain.QLike;
import com.recipe.member.domain.Member;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.member.domain.QMember;
import com.recipe.recipe.domain.QRecipe;
import com.recipe.recipe.dto.RecipeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class LikeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<RecipeDto> findLikedRecipe(Member targetMember, Pageable pageable) {
        QLike like = QLike.like;
        QRecipe recipe = QRecipe.recipe;
        QMember member = QMember.member;
        QueryResults<RecipeDto> recipeDtoQueryResults = queryFactory
                .select(Projections.constructor(
                        RecipeDto.class,
                        recipe.id,
                        recipe.title,
                        recipe.thumbnail,
                        recipe.description,
                        recipe.viewCount,
                        member.nickname))
                .from(like)
                .leftJoin(like.recipe, recipe)
                .leftJoin(like.member, member)
                .where(member.eq(targetMember))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(like.id.desc())
                .fetchResults();

        List<RecipeDto> contents = recipeDtoQueryResults.getResults();

        long total = recipeDtoQueryResults.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }
}
