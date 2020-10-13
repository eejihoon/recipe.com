package com.dunk.django.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dunk.django.domain.DjangoMember;
import com.dunk.django.domain.Recipe;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;
import com.dunk.django.dto.RecipeDTO;
import com.dunk.django.repository.RecipeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;

    private final RecommendService recommendService;

    private final MemberCheckService checkService;

    @Override
    public Long register(RecipeDTO dto) {

        Recipe entity = bindToEntity(dto);

        repository.save(entity);

        return entity.getItemId();
    }

    @Override
    public Recipe get(Long bno) {

        return repository.findById(bno).get();
    }

    @Override
    public Long modify(RecipeDTO dto) {

        Recipe entity = bindToEntity(dto);

        repository.save(entity);

        return entity.getItemId();
    }

    @Override
    public void remove(Long recipe_no) {

        repository.deleteById(recipe_no);

    }

    @Override
    public GenericListDTO<RecipeDTO, Recipe> getList(PageDTO pageDTO) {
        final Sort sort = Sort.by("itemId").descending();

        Pageable pageable = pageDTO.makePageable(sort);

        Page<Recipe> result = repository.findAll(pageable);

        log.info(result);

        GenericListDTO<RecipeDTO, Recipe> listDTO = new GenericListDTO<>(result, en -> bindToDTO(en));

        return listDTO;
    }

    
    @Override
    public List<Recipe> getRecommendList(String userId) {

        DjangoMember dm = new DjangoMember();
        List<Recipe> result = new ArrayList<>();

        Optional<DjangoMember> member = checkService.getMno(userId);

        if (member.isPresent()) {

            dm = member.get();

            for (int i = 0; i < recommendService.select(dm.getUserId()).size(); i++) {

                result.add(repository.findByItemId(recommendService.select(dm.getUserId()).get(i).getItemID()));

            } // end for
        } // end if

        // log.info("======================="+dm.getUserId());
        // log.info("======================="+checkService.getMno("coco"));

        // 결과값
        log.info(result);

        return result;
    }

}