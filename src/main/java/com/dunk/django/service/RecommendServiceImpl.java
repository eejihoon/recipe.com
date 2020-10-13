package com.dunk.django.service;

import java.util.List;
import java.util.Optional;

import com.dunk.django.domain.Recipe;
import com.dunk.django.domain.Recommend;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;
import com.dunk.django.dto.RecipeDTO;
import com.dunk.django.repository.RecommendRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository repository;

    @Override
    public List<Recommend> select(Long rid) {

        List<Recommend> list = repository.findByRid(rid);

        return list;

    }

}