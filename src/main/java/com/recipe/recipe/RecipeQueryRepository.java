package com.recipe.recipe;

import com.recipe.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

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

    /*
    *   TODO
    *   삭제 예정
    * */
    public Page<Recipe> findByRecipeTitle(String keyword, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;

        JPQLQuery<Recipe> query = from(recipe);

        if (Objects.nonNull(keyword)) {
            if (!keyword.equals("")) {
                QIngredient ingredient = QIngredient.ingredient1;
                query.leftJoin(recipe.ingredients, ingredient)
                        .on(ingredient.ingredient.containsIgnoreCase(keyword))
                        .where(recipe.title.containsIgnoreCase(keyword));
            }
        }

        JPQLQuery<Recipe> recipeJPQLQuery = getQuerydsl().applyPagination(pageable, query);

        QueryResults<Recipe> fetchResults = recipeJPQLQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    public Page<RecipeDto> findAllRecipeAndSearchWithPaging(String keyword, Pageable pageable) {
        QueryResults<RecipeDto> recipeDtoQueryResults = queryFactory
                .select(Projections.constructor(RecipeDto.class,
                        recipe.id,
                        recipe.title,
                        recipe.thumbnail,
                        recipe.description,
                        recipe.viewCount,
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

    private BooleanExpression titleContains(String keyword) {
        return keyword == null ? null : recipe.title.containsIgnoreCase(keyword);
    }
}
