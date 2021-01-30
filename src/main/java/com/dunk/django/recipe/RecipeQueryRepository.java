package com.dunk.django.recipe;

import com.dunk.django.domain.QIngredient;
import com.dunk.django.domain.QRecipe;
import com.dunk.django.domain.Recipe;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Slf4j
@Repository
public class RecipeQueryRepository extends QuerydslRepositorySupport {
    public RecipeQueryRepository() {
        super(Recipe.class);
    }

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
}
