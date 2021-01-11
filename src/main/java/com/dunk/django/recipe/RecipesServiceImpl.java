package com.dunk.django.recipe;

import java.util.List;

import com.dunk.django.domain.Recipes;

import com.dunk.django.recommand.RecommendService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class RecipesServiceImpl implements RecipesService {

    private final RecipesRepository repository;

    private final RecommendService recommendService;

    @Override
    public Long register(RecipeDTO dto) {

        Recipes entity = bindToEntity(dto);

        repository.save(entity);

        return entity.getItemId();
    }

    @Override
    public Recipes get(Long bno) {

        return repository.findById(bno).get();
    }

    @Override
    public Long modify(RecipeDTO dto) {

        Recipes entity = bindToEntity(dto);

        repository.save(entity);

        return entity.getItemId();
    }

    @Override
    public void remove(Long recipe_no) {

        repository.deleteById(recipe_no);

    }

    @Override
    public GenericListDTO<RecipeDTO, Recipes> getList(PageDTO pageDTO) {
        final Sort sort = Sort.by("itemId").descending();

        Pageable pageable = pageDTO.makePageable(sort);

        Page<Recipes> result = repository.findAll(pageable);

        log.info(result);

        GenericListDTO<RecipeDTO, Recipes> listDTO = new GenericListDTO<>(result, en -> bindToDTO(en));

        return listDTO;
    }


    @Override
    public List<Recipes> getRecommendList(String userId) {

//        Member dm = new Member();
//        List<Recipe> result = new ArrayList<>();
//
//        Optional<Member> member = checkService.getMno(userId);
//
//        if (member.isPresent()) {
//
//            dm = member.get();
//
//            for (int i = 0; i < recommendService.select(dm.getUserId()).size(); i++) {
//
//                result.add(repository.findByItemId(recommendService.select(dm.getUserId()).get(i).getItemID()));
//
//            } // end for
//        } // end if
//
//        // log.info("======================="+dm.getUserId());
//        // log.info("======================="+checkService.getMno("coco"));
//
//        // 결과값
//        log.info(result);

        return null;
    }

}