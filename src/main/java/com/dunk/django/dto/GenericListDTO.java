package com.dunk.django.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

@Data
public class GenericListDTO<D, E> {
    private List<D> dtoList;

    private int totalPage, page, start, end;
    private List<Integer> pageList;
    private boolean prev, next;

    public GenericListDTO(Page<E> result, Function<E, D> fn) {
        // map은 a를 b로 바꾼다.
        // 이거 한 줄이면 페이지에 있는 entity리스트를 dto리스트로 바꾼다.
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        this.totalPage = result.getTotalPages();

        makePages(result.getPageable());
    }

    private void makePages(Pageable pageInfo) {
        this.page = pageInfo.getPageNumber() + 1;

        if (pageInfo.isPaged()) {
            pageList = new ArrayList<>();
        }
        // temp end page
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}