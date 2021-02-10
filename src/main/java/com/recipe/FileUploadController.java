package com.recipe;

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
@Controller
public class FileUploadController {

    private final UploadService uploadService;

    @GetMapping("/file-upload")
    public String form () {

        log.info("file-upload form");

        return "/file-upload";
    }

    @PostMapping("/file-upload")
    @ResponseBody
    public ResponseEntity<HttpEntity> uploadPost(@RequestPart List<MultipartFile> files) throws IOException {
        HttpEntity httpEntity = null;

        for ( MultipartFile file : files ) {
            String upload = uploadService.upload(file, "static");
            httpEntity = new HttpEntity(upload);
        }

        return new ResponseEntity<>(httpEntity, HttpStatus.OK);
    }


}
