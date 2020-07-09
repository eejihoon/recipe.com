package com.dunk.django.service;

import javax.transaction.Transactional;

import com.dunk.django.domain.Board;
import com.dunk.django.dto.BoardDTO;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;

@Transactional
public interface BoardService {

    Long register(BoardDTO dto);

    Board get(Long bno);

    /*<DTO, Entity>*/
    GenericListDTO<BoardDTO, Board> getList(PageDTO pageDTO);

    Long modify(BoardDTO dto);
    
    void remove(Long bno);

    //DTO객체를 Entity(VO)객체로 바꾼다.
    default Board bindToEntity(BoardDTO dto) {
        Board entity = Board.builder()
        .bno(dto.getBno())
        .title(dto.getTitle())
        .content(dto.getContent()) 
        .writer(dto.getWriter())
        .build();
        
        return entity;
    }

    default BoardDTO bindToDTO(Board entity) {
        BoardDTO dto = BoardDTO.builder()
        .bno(entity.getBno())
        .title(entity.getTitle())
        .content(entity.getContent())
        .writer(entity.getWriter())
        .build();

        return dto;
    }
    
}