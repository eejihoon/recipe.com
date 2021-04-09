package com.recipe.upload;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileUploadApiController {

    private final UploadService uploadService;

    @ApiOperation(value = "파일 등록")
    @PostMapping("/api/file-upload")
    public ResponseEntity<HttpEntity> uploadPost(@RequestPart List<MultipartFile> files) throws IOException {
        HttpEntity httpEntity = null;

        for (MultipartFile file : files) {
            String upload = uploadService.upload(file);
            httpEntity = new HttpEntity(upload);
        }

        return new ResponseEntity<>(httpEntity, HttpStatus.OK);
    }
}
