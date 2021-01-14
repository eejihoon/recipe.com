package com.dunk.django.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter @Setter
@AllArgsConstructor
public class Page {
    private int page;
    private int size;
    private int totalPage;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;

    public Page() {
        this.page = 0;
        this.size = 10;
    }

    Pageable toPageable(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;

        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;

        return null;
    }


}
