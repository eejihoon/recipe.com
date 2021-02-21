package com.recipe.recipe.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recipe.like.domain.QLike;
import com.recipe.member.domain.Member;
import com.recipe.member.domain.QMember;
import com.recipe.recipe.domain.QRecipe;
import com.recipe.recipe.domain.Recipe;
import com.recipe.recipe.dto.RecipeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class RecipeQueryRepository extends QuerydslRepositorySupport {
    private JPAQueryFactory queryFactory;
    private QRecipe recipe;
    private QLike like;
    private QMember member;

    public RecipeQueryRepository(JPAQueryFactory queryFactory) {
        super(Recipe.class);
        this.queryFactory = queryFactory;
        recipe = QRecipe.recipe;
        like = QLike.like;
        member = QMember.member;
    }

    public Page<RecipeDto> findAllRecipeAndSearchWithPaging(String keyword, Pageable pageable) {
        QueryResults<RecipeDto> recipeDtoQueryResults = queryFactory
                .select(Projections.constructor(RecipeDto.class,
                        recipe.id,
                        recipe.title,
                        recipe.thumbnail,
                        recipe.description,
                        recipe.viewCount,
                        recipe.regdate,
                        member.nickname,
                        like.count()))
                .from(recipe)
                .leftJoin(recipe.likes, like)
                .leftJoin(recipe.member, member)
                .where(titleContains(keyword))
                .groupBy(recipe.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recipe.id.desc())
                .fetchResults();

        List<RecipeDto> content = recipeDtoQueryResults.getResults();

        long total = recipeDtoQueryResults.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
    //내가 쓴 게시물 조회
    public Page<RecipeDto> findByMember(Member findMember, Pageable pageable) {
        QueryResults<RecipeDto> recipeDtoQueryResults = queryFactory
                .select(Projections.constructor(RecipeDto.class,
                        recipe.id,
                        recipe.title,
                        recipe.thumbnail,
                        recipe.description,
                        recipe.viewCount,
                        recipe.regdate,
                        member.nickname,
                        like.count()))
                .from(recipe)
                .leftJoin(recipe.member, member)
                .leftJoin(recipe.likes, like)
                .where(recipe.member.eq(findMember))
                .groupBy(recipe.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recipe.regdate.desc())
                .fetchResults();

        List<RecipeDto> results = recipeDtoQueryResults.getResults();

        long total = recipeDtoQueryResults.getTotal();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression titleContains(String keyword) {
        return keyword == null ? null : recipe.title.containsIgnoreCase(keyword);
    }
}
