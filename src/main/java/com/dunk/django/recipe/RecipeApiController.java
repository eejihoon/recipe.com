package com.dunk.django.recipe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeApiController {
    private final RecipeService recipeService;

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<Long> savePost(@RequestBody RecipeSaveForm recipeSaveForm) {
        log.info("recipeSaveForm : {}", recipeSaveForm);
        Long id = recipeService.save(recipeSaveForm);

        return ResponseEntity.ok(id);
    }
}
