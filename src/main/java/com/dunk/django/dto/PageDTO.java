package com.dunk.django.dto;

import com.google.auto.value.AutoValue.Builder;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class PageDTO {
    private int page;
    private int size;
    
    public PageDTO() {
        this.page = 1;
        this.size = 10;
    }

    //모든 list페이지를 처리할 때 공통으로 사용
    public Pageable makePageable(Sort sort) {
        return PageRequest.of(this.page-1, this.size, sort);
    }
}