package com.recipe.recipe.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.recipe.recipe.domain.CookingMethod;
import com.recipe.recipe.domain.Ingredient;
import com.recipe.recipe.domain.Recipe;
import com.recipe.recipe.repository.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Component
public class RecipeDataInsert {
    private final RecipeRepository recipeRepository;

    //@PostConstruct
    public void initRecipeData() {
        if (recipeRepository.count() == 0) {
            String url =
                    "http://openapi.foodsafetykorea.go.kr/api/API-KEY/COOKRCP01/json/1/699";

            RestTemplate testRestTemplate = new RestTemplate();

            ResponseEntity<String> forEntity = testRestTemplate.getForEntity(url, String.class);

            JsonParser jsonParser = new JsonParser();

            JsonElement parse = jsonParser.parse(forEntity.getBody());


            JsonArray asJsonArray = parse.getAsJsonObject()
                    .get("COOKRCP01")
                    .getAsJsonObject()
                    .get("row")
                    .getAsJsonArray();

            List<Recipe> recipes = new ArrayList<>();

            for (JsonElement el : asJsonArray) {
                JsonObject object = el.getAsJsonObject();
                String title = replaceQuotes(object.get("RCP_NM"));
                String thumbnail = replaceQuotes(object.get("ATT_FILE_NO_MAIN"));
                String originalImage = replaceQuotes(object.get("ATT_FILE_NO_MK"));
                String hashTag = getHashTag(object);

                String natrium = replaceQuotes(object.get("INFO_NA"));
                String fat = replaceQuotes(object.get("INFO_FAT"));
                String protein = replaceQuotes(object.get("INFO_PRO"));
                String carbohydrate = replaceQuotes(object.get("INFO_CAR"));
                String calorie = replaceQuotes(object.get("INFO_ENG"));

                //재료
                String ingredientStr = replaceQuotes(object.get("RCP_PARTS_DTLS")).replaceAll("\\\\n", "");
                String[] split = ingredientStr.trim().split(",");
                Set<Ingredient> ingredients = new HashSet<>();

                for (String ingr : split) {
                    ingredients.add(new Ingredient(ingr));
                }

                //CookingMethod
                Set<CookingMethod> cookingMethods = new HashSet<>();
                for (int i = 1; i <= 20; i++) {
                    if (Objects.nonNull(object.get("MANUAL0" + i))) {
                        int sequence = i;
                        String manual = replaceQuotes(object.get("MANUAL0" + i));

                        if (Objects.nonNull(object.get("MANUAL_IMG0" + i))) {
                            String manualImage = replaceQuotes(object.get("MANUAL_IMG0" + i));
                            cookingMethods.add(new CookingMethod(sequence, manual, manualImage));
                        } else {
                            cookingMethods.add(new CookingMethod(sequence, manual, ""));
                        }

                    }
                }

//                Recipe recipe = Recipe.builder()
//                        .title(title)
//                        .thumbnail(thumbnail)
//                        .originalImage(originalImage)
//                        .calorie(Float.parseFloat(calorie))
//                        .carbohydrate(Float.parseFloat(carbohydrate))
//                        .fat(Float.parseFloat(fat))
//                        .natrium(Float.parseFloat(natrium))
//                        .protein(Float.parseFloat(protein))
//                        .hashTag(hashTag)
//                        .ingredients(ingredients)
//                        .cookingMethods(cookingMethods)
//                        .build();
//
//                ingredients.forEach(ingredient -> ingredient.add(recipe));
//                cookingMethods.forEach(cookingMethod -> cookingMethod.addRecipe(recipe));
//
//                recipes.add(recipe);
            }

            log.info("recipe size : " + recipes.size());

            recipeRepository.saveAll(recipes);
        }
    }

    private String getHashTag(JsonObject object) {
        if (Objects.nonNull(replaceQuotes(object.get("HASH_TAG")))) {
            return replaceQuotes(object.get("HASH_TAG"));
        } else {
            return replaceQuotes(object.get("RCP_PAT2"));
        }
    }

    private String replaceQuotes(JsonElement el) {
        return el.toString().replaceAll("\"", "");
    }
}
