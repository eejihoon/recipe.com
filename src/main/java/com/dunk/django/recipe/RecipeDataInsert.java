package com.dunk.django.recipe;

import com.dunk.django.domain.*;
import com.dunk.django.recipe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
//@Service
public class RecipeDataInsert {
    private final RecipeRepository recipeRepository;
    private final FoodTypeRepository foodTypeRepository;
    private final FoodNationRepository foodNationRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientTypeRepository ingredientTypeRepository;
    private final CookingMethodRepository cookingMethodRepository;

    @Transactional
    @PostConstruct
    public void initRecipeService() throws IOException {
        if (foodNationRepository.count() == 0) {
            Resource resource = new ClassPathResource("nation.csv");

            List<FoodNation> nations = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> {
                        String[] split = line.split(",", 2);

                        return FoodNation.builder()
                                .id(Long.parseLong(split[0]))
                                .nation(split[1])
                                .build();
                    }).collect(Collectors.toList());

            foodNationRepository.saveAll(nations);
        }

        if (foodTypeRepository.count() == 0) {
            Resource resource = new ClassPathResource("foodType.csv");

            List<FoodType> foodTypes = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> {
                        String[] split = line.split(",", 2);

                        return FoodType.builder()
                                .id(Long.parseLong(split[0]))
                                .type(split[1])
                                .build();

                    }).collect(Collectors.toList());

            foodTypeRepository.saveAll(foodTypes);
        }

        if (recipeRepository.count() == 0) {
            Resource resource = new ClassPathResource("Recipe.csv");

            List<Recipe> recipes = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> {
                        String[] split = line.split(",", 9);

                        return Recipe.builder()
                                .id(Long.parseLong(split[0]))
                                .name(split[1])
                                .foodNation(foodNationRepository.findById(Long.parseLong(split[2])).get())
                                .foodType(foodTypeRepository.findById(Long.parseLong(split[3])).get())
                                .cookingTime(Integer.parseInt(split[4].replaceAll("분", "")))
                                .calorie(Integer.parseInt(split[5].replaceAll("Kcal", "")))
                                .servings(Integer.parseInt(split[6].replaceAll("인분", "")))
                                .thumbnail(split[7])
                                .description(split[8])
                                .build();

                    }).collect(Collectors.toList());

            recipeRepository.saveAll(recipes);
        }

        if (cookingMethodRepository.count() == 0) {
            Resource resource = new ClassPathResource("Cooking-Method.csv");

            List<CookingMethod> cookingMethodList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> {

                        String[] split = line.split(",", 4);

                        System.out.println("split[0] : " + split[0]);

                        Recipe recipe = recipeRepository.findById(Long.parseLong(split[0])).get();

                        System.out.println("***************************************************************");
                        System.out.println("split[0] : " + split[0]);
                        System.out.println("recipe : " + recipe);
                        System.out.println("id : " + recipe.getId());
                        System.out.println("name : " + recipe.getName());
                        System.out.println("description : " + recipe.getDescription());
                        System.out.println("***************************************************************");

                        return CookingMethod.builder()
                                .recipe(recipeRepository.findById(Long.parseLong(split[0])).get())
                                .sequence(Integer.parseInt(split[1]))
                                .image(split[2])
                                .description(split[3])
                                .build();
                    }).collect(Collectors.toList());

            cookingMethodRepository.saveAll(cookingMethodList);
        }

//        if (ingredientTypeRepository.count() == 0) {
//            Resource resource = new ClassPathResource("IngredientType.csv");
//
//            List<IngredientType> ingredientTypes = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
//                    .stream()
//                    .map(foodTypeCsv -> {
//                        String[] split = foodTypeCsv.split(",");
//
//                        return IngredientType.builder()
//                                .id(Long.parseLong(split[0]))
//                                .ingredientType(split[1])
//                                .build();
//
//                    }).collect(Collectors.toList());
//
//            ingredientTypeRepository.saveAll(ingredientTypes);
//        }

        if (ingredientRepository.count() == 0) {
            Resource resource = new ClassPathResource("ingredient.csv");

            List<Ingredient> ingredients = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> {
                        String[] split = line.split(",", 4);

                        return Ingredient.builder()
                                .recipe(recipeRepository.findById(Long.parseLong(split[0])).get())
                                .quantity(split[1])
                                .ingredientType(ingredientTypeRepository.findById(Long.parseLong(split[2])).get())
                                .ingredient(split[3])
                                .build();

                    }).collect(Collectors.toList());

            ingredientRepository.saveAll(ingredients);
        }
    }
}
