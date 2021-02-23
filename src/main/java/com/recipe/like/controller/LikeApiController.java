package com.recipe.like.controller;

import com.recipe.config.security.LoginMember;
import com.recipe.like.service.LikeService;
import com.recipe.member.domain.Member;
import com.recipe.member.utils.MemberAdapter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LikeApiController {
    private final LikeService likeService;

    @ApiOperation(value = "좋아요 카운트")
    @GetMapping("/like/{recipeId}")
    public ResponseEntity<List<String>> getLikeCount(@PathVariable Long recipeId ,@LoginMember Member loginMember) {
        List<String> resultData = likeService.count(recipeId, loginMember);

        log.info("likeCount : {} ", resultData);

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @ApiOperation(value = "좋아요 취소")
    @DeleteMapping("/like/{recipeId}")
    public ResponseEntity<String> cancelLike(@LoginMember Member loginMember,
                                             @PathVariable Long recipeId) {
        if (loginMember != null) {
            likeService.cancelLike(loginMember, recipeId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "좋아요 등록")
    @PostMapping("/like/{recipeId}")
    public ResponseEntity<String> addLike(@LoginMember Member loginMember,
                                          @PathVariable Long recipeId) {
        boolean result = false;

        result = likeService.addLike(loginMember, recipeId);

        return result ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
