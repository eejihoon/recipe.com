package com.recipe.like;

import com.recipe.member.MemberAdapter;
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
    public ResponseEntity<List<String>> getLikeCount(@PathVariable Long recipeId ,@AuthenticationPrincipal MemberAdapter memberAdapter) {
        List<String> resultData = likeService.count(recipeId, memberAdapter);

        log.info("likeCount : {} ", resultData);

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @ApiOperation(value = "좋아요 취소")
    @DeleteMapping("/like/{recipeId}")
    public ResponseEntity<String> cancelLike(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                             @PathVariable Long recipeId) {
        if (memberAdapter != null) {
            likeService.cancelLike(memberAdapter.getMember(), recipeId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "좋아요 등록")
    @PostMapping("/like/{recipeId}")
    public ResponseEntity<String> addLike(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                          @PathVariable Long recipeId) {
        boolean result = false;

        if (memberAdapter != null) {
            result = likeService.addLike(memberAdapter.getMember(), recipeId);
        }

        return result ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
