package com.recipe.recipe.controller;

import com.recipe.config.security.LoginMember;
import com.recipe.member.domain.Member;
import com.recipe.member.utils.MemberAdapter;
import com.recipe.recipe.dto.RecipeSaveForm;
import com.recipe.recipe.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecipeApiController {
    private final RecipeService recipeService;

    @ApiOperation(value = "레시피 등록", notes = "성공 200 OK, 실패 400 ERROR")
    @PostMapping("/api/recipe")
    public ResponseEntity<Long> save(@RequestBody @Valid RecipeSaveForm recipeSaveForm, Errors errors,
                                     @LoginMember Member loginMember) {
        log.info("recipeSaveForm : {}", recipeSaveForm);
        log.info("loginMember : {}", loginMember);
        log.info("errors : {} ", errors);

        if (errors.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long id = recipeService.save(recipeSaveForm, loginMember);
        log.info("id : {} ", id);

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "레시피 수정", notes = "성공 200 OK + recipeId반환")
    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Long> update(@RequestBody @Valid RecipeSaveForm recipeSaveForm, Errors errors,
                                       @PathVariable Long id,
                                       @LoginMember Member loginMember) throws AccessDeniedException {
        log.info("recipeSaveForm : {}", recipeSaveForm);

        if (errors.hasErrors()) {
            return new ResponseEntity<>(-1L, HttpStatus.NOT_MODIFIED);
        }

        recipeService.update(recipeSaveForm, id, loginMember);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "레시피 삭제", notes = "성공 200 OK")
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id,
                                         @LoginMember Member loginMember) throws AccessDeniedException {
        recipeService.remove(id, loginMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
