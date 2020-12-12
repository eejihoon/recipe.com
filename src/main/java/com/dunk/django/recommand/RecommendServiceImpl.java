package com.dunk.django.recommand;

import java.util.List;

import com.dunk.django.domain.Recommend;

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