package com.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class RecipeJsonParsingTest {
    //@Test
    void testReadRecipeApi() throws JsonProcessingException {
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        String sampleUrl = "http://openapi.foodsafetykorea.go.kr/api/sample/COOKRCP01/json/1/1";

        ResponseEntity<String> forEntity = testRestTemplate.getForEntity(sampleUrl, String.class);

        JsonParser jsonParser = new JsonParser();

        JsonElement parse = jsonParser.parse(forEntity.getBody());


        JsonArray asJsonArray = parse.getAsJsonObject()
                .get("COOKRCP01")
                .getAsJsonObject()
                .get("row")
                .getAsJsonArray();

        System.out.println("======================================================");

        for (JsonElement el : asJsonArray) {
            JsonObject object = el.getAsJsonObject();

            System.out.println("게시물 번호---------------");
            System.out.println(replaceQuotes(object.get("RCP_SEQ")));
            System.out.println();

            System.out.println("음식 이름---------------");
            System.out.println(replaceQuotes(object.get("RCP_NM")));
            System.out.println();

            System.out.println("정보");
            System.out.println("열량 : " + replaceQuotes(object.get("INFO_ENG")));
            System.out.println("탄수화물 : " + replaceQuotes(object.get("INFO_CAR")));
            System.out.println("단밸질 : " + replaceQuotes(object.get("INFO_PRO")));
            System.out.println("지방 : " + replaceQuotes(object.get("INFO_FAT")));
            System.out.println("나트륨 : " + replaceQuotes(object.get("INFO_NA")));
            System.out.println();


            System.out.println(replaceQuotes(object.get("RCP_PAT2")));
            System.out.println("#HashTag");
            if (replaceQuotes(object.get("HASH_TAG")).length() == 0
                    && replaceQuotes(object.get("HASH_TAG")).length() == 1) {
                System.out.println("#" + replaceQuotes(object.get("HASH_TAG")));
            } else {
                System.out.println(replaceQuotes(object.get("RCP_PAT2")));
            }
            System.out.println();

            System.out.println("재료--------------------------");
            System.out.println(replaceQuotes(object.get("RCP_PARTS_DTLS")).replaceAll("\\\\n", " "));
            System.out.println();

            System.out.println("thumbnail");
            System.out.println(replaceQuotes(object.get("ATT_FILE_NO_MAIN")));
            System.out.println("originalImage");
            System.out.println(replaceQuotes(object.get("ATT_FILE_NO_MK")));
            System.out.println();

            System.out.println("조리 방법");
            for (int i=1; i<=20; i++) {
                if (Objects.nonNull(object.get("MANUAL0"+i))) {
                    System.out.println("sequence : " + i);
                    System.out.println(replaceQuotes(object.get("MANUAL0"+i)));
                }

                if (Objects.nonNull(object.get("MANUAL_IMG0"+i))) {
                    System.out.println(replaceQuotes(object.get("MANUAL_IMG0"+i)));
                } else {
                    System.out.println("");
                }
            }
        }

        System.out.println("======================================================");
    }

    private String replaceQuotes(JsonElement el) {
        return el.toString().replaceAll("\"", "");
    }
}
