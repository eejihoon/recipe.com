package com.dunk.django.recipe;

import com.dunk.django.member.MemberAdapter;
import com.dunk.django.member.MemberService;
import com.dunk.django.recipe.utils.AuthorVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeApiController {
    private final RecipeService recipeService;

    @PostMapping("/recipe")
    public ResponseEntity<Long> save(@RequestBody RecipeSaveForm recipeSaveForm,
                                     @AuthenticationPrincipal MemberAdapter memberAdapter) {
        log.info("recipeSaveForm : {}", recipeSaveForm);
        log.info("member : {}", memberAdapter);

        Long id = recipeService.save(recipeSaveForm, memberAdapter.getMember());

        return ResponseEntity.ok(id);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Long> update(@RequestBody RecipeSaveForm recipeSaveForm,
                                       @PathVariable Long id,
                                       @AuthenticationPrincipal MemberAdapter memberAdapter) throws AccessDeniedException {
        log.info("recipeSaveForm : {}", recipeSaveForm);

        recipeService.update(recipeSaveForm, id, memberAdapter.getMember());

        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id,
                                         @AuthenticationPrincipal MemberAdapter memberAdapter) throws AccessDeniedException {
        recipeService.remove(id, memberAdapter.getMember());
        return ResponseEntity.ok().build();
    }
}
