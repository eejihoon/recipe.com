package com.dunk.django.contoller;

import java.util.ArrayList;
import java.util.Optional;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dunk.django.dto.UserFridgeDTO;
import com.dunk.django.service.UserFridgeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/scan")
@RequiredArgsConstructor
@Log4j2
public class OCRController {
  private final ResourceLoader resourceLoader;
  // [START spring_vision_autowire]
  private final CloudVisionTemplate cloudVisionTemplate;

  private final AmazonS3Client s3Client;

  private final UserFridgeService service;

  @PostMapping("/upload")
  public String upload(MultipartFile[] files, Authentication auth, RedirectAttributes rttr) {

    log.info(files);
    log.info(files.length);

    String bucket = "seokhwan";

    if (files.length > 0) {
      for (MultipartFile file : files) {
        log.info(file.getOriginalFilename());
        log.info(file.getContentType());

        String fileName = file.getOriginalFilename();

        try {
          // Object Meta Data
          ObjectMetadata metadata = new ObjectMetadata();
          metadata.setContentType(file.getContentType());
          metadata.setContentLength(file.getSize());

          PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead);

          s3Client.putObject(putObjectRequest);

          String thumbnailUri = s3Client.getUrl(bucket, fileName).toString();
          String textFromImage = this.cloudVisionTemplate
              .extractTextFromImage(this.resourceLoader.getResource(thumbnailUri));

          // textFromImage json에 넣기
          final JsonPrimitive firstHost = new JsonPrimitive(textFromImage);
          final JsonArray jArray = new JsonArray();
          jArray.add(firstHost);

          final JsonObject jObj = new JsonObject();
          jObj.add("virtual_hosts", jArray);

          // String[] words = textFromImage.split(("\\\n"));

          // 숫자, 특수문자 제거
          String text = stringReplace(textFromImage);

          // 문자열 쪼개기
          String[] words2 = text.split(("\\\n"));

          // 긁어온 텍스트 단어들 배열값 저장한 리스트
          ArrayList<String> arr = new ArrayList<>();
          // 텍스트에서 긁어온 재료 목록을 저장한 리스트
          ArrayList<String> arr2 = new ArrayList<>();
          // 빈배열제거
          for (String temp : words2) {
            if (temp.trim().length() > 0)
              arr.add(temp);
          }
          log.info("==============목록================");
          // 이미지에서 긁어온 text arraylist 목록
          log.info(arr);
          log.info("==============필터================");

          // 면세로 시작하는 단어찾기
          Optional<String> target = arr.stream().filter(str -> str.startsWith("면세")).findFirst();
          // 총 품목으로 시작하는 단어찾기
          Optional<String> target2 = arr.stream().filter(str -> str.startsWith("총 품목")).findFirst();
          // 액이 포함된 단어 찾기
          Optional<String> target3 = arr.stream().filter(str -> str.contains("액")).findFirst();

          if (target3.isPresent()) {
            String textC = target3.get();
            int idx3 = arr.indexOf(textC);

            if (target.isPresent()) {
              String textA = target.get();
              int idx = arr.indexOf(textA);

              for (int i = idx3 + 1; i < idx; i++) {
                log.info(i + " : " + arr.get(i));
                arr2.add(arr.get(i));
              }
            } else if (target2.isPresent()) {
              String textB = target2.get();
              int idx2 = arr.indexOf(textB);
              for (int i = idx3 + 1; i < idx2; i++) {
                log.info(i + " : " + arr.get(i));
                arr2.add(arr.get(i));
              }
            }
            log.info("==============================");
            log.info(arr2);
            log.info("==============================");

            for (int i = 0; i < arr2.size(); i++) {
              log.info(arr2.get(i));

              UserFridgeDTO dto = UserFridgeDTO.builder().username(auth.getName()).ingr_name(arr2.get(i))
                  .cno(service.getCategoryCno(arr2.get(i))) // 추가부분.
                  .build();
              // service.register(dto);
              service.scanAndRegist(dto); // 밑의 메서드로 변경.
            }

            log.info("==============================");

            // 텍스트에서 긁은 재료 목록 (arr2)를 냉장고 db에 insert해야한다
            // service.insert();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    }

    rttr.addAttribute("username", auth.getName());

    return "/crawling"+auth.getName();
  }

  // 특수문자 and 숫자 제거
  public static String stringReplace(String str) {
    String match = "[!,@.#$%^n&*:()]";

    // [^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]
    // 특수문자제거

    str = str.replaceAll(match, "");

    // 숫자제거
    String str2 = str.replaceAll("[0-9]", "");

    return str2;
  }
}