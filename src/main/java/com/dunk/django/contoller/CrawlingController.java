package com.dunk.django.contoller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import com.dunk.django.domain.Recipe;
import com.dunk.django.domain.UserFridge;
import com.dunk.django.dto.RecipeDTO;
import com.dunk.django.service.RecipeService;
import com.dunk.django.service.UserFridgeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequiredArgsConstructor
public class CrawlingController {

    private final UserFridgeService fridgeService;

    @GetMapping("/crawling")
    public String crawling(@RequestParam("username") String username) {
        log.info("=================================================");
        log.info("====================/crawling====================");
        List<UserFridge> ingr = fridgeService.get(username);
        ingr.forEach(row -> {
            log.info(row.getIngr_name());
            /* 크롤링할 URL */
            String URL = "https://www.10000recipe.com/recipe/list.html?q=" + fridgeService.searchCategory(row.getIngr_name());
            Document doc = null;
            List<String> list = new ArrayList<>(); // img src를 담을 list
            List<String> imgList = new ArrayList<>(); // 불필요한 src를 뺀 list
            List<String> urlList = new ArrayList<>(); // 레시피 URL
            try {
                doc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements recipeData = doc.select(".rcp_m_list2");
            /*
             * 음식 이미지 URL 크롤링 1.이미지 태그를 긁는다. 2.src속성만 빼서 list에 저장한다.
             */
            Elements imgElements = recipeData.select(".common_sp_thumb").select("img");
            imgElements.forEach(element -> list.add(element.attr("src")));
            list.stream().distinct().filter(keyword -> !keyword.contains("icon")).forEach(url -> imgList.add(url));
            /*
             * 레시피 이름 크롤링 레시피 이름 전체 크롤링한 것을 줄바꿈('\n') 기준으로 잘라서 recipeNameList에 담는다.
             */
            List<String> recipeNameList = Arrays.asList(recipeData.select(".common_sp_caption_tit").html().split("\n"));
            /*
             * 레시피 URL 크롤링 1. 레시피 URL이 있는 a태그를 긁는다. 2. href 속성만 urlList에 담는다.
             */
            Elements urlElements = recipeData.select(".common_sp_thumb").select("a");
            urlElements.forEach(
                    element -> urlList.add("https://www.10000recipe.com/" + element.attr("href").substring(1)));
            log.info("urlList.size() : " + urlList.size());
            for (int i = 0; i < urlList.size(); i++) {
                RecipeDTO dto = RecipeDTO.builder().recipe_name(recipeNameList.get(i))
                        .ingr_list(getIngredient(urlList.get(i))).recipe(urlList.get(i)).img(imgList.get(i)).build();
                // log.info(dto);
                // recipeServie.register(dto);
            }
        });
        return "redirect:/django/myFridge";
    }

    // 현재 페이지의 각각의 음식들에 필요한 재료들을 가져온다.
    public String getIngredient(String URL) {
        // 특정 음식의 상세페이지 URL
        String detailContentURL = URL; // foodURL.get[0];
        Document doc = null; // 해당 페이지 전체내용을 받은 변수
        List<String> list = new ArrayList<>();
        StringJoiner sj = new StringJoiner(",");
        try {
            doc = Jsoup.connect(detailContentURL).get(); // 해당 페이지 전체 html내용
        } catch (Exception e) {
            e.printStackTrace();
        }
        // select를 이용하여 원하는 태그를 선택한다. select는 원하는 값을 가져오기 위한 중요한 기능이다.
        Elements ingredients = doc.select("div.ready_ingre3").select("li");
        // 구분자 사용을 위해 컬렉션(List)에 각 재료의 데이터를 추가하는 반복문.
        for (int i = 0; i < ingredients.size(); i++) {
            // log.info("1 : " + ingredients);
            list.add(ingredients.get(i).text());
        }
        // ingredients.forEach(ingredient->log.info(" 2 : " + ingredient));
        // 구분자를 넣기위한 반복분.
        for (int i = 0; i < list.size(); i++) {
            sj.add(list.get(i));
        }
        return sj.toString();
    }
}