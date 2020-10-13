package com.dunk.django.service;

import java.util.List;

import javax.transaction.Transactional;

import com.dunk.django.domain.Recipe;
import com.dunk.django.domain.Recommend;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;
import com.dunk.django.dto.RecipeDTO;
import com.dunk.django.dto.RecommendDTO;

import org.springframework.data.jpa.repository.Query;

@Transactional
public interface RecommendService {

   
    public List<Recommend> select(Long rid);

 

    //DTO객체를 Entity(VO)객체로 바꾼다.
    default Recommend bindToEntity(RecommendDTO dto) {
        Recommend entity = Recommend.builder()
        .rno(dto.getRno())
        .rid(dto.getRid())
        .itemID(dto.getItemID()) 
        .value(dto.getValue())
        .build();
        
        return entity;
    }

    default RecommendDTO bindToDTO(Recommend entity) {
        RecommendDTO dto = RecommendDTO.builder()
        .rno(entity.getRno())
        .rid(entity.getRid())
        .itemID(entity.getItemID()) 
        .value(entity.getValue())
        .build();

        return dto;
    }
    
}