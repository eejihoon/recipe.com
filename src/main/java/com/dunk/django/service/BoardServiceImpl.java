package com.dunk.django.service;

import java.util.Optional;

import com.dunk.django.domain.Board;
import com.dunk.django.dto.BoardDTO;
import com.dunk.django.dto.GenericListDTO;
import com.dunk.django.dto.PageDTO;
import com.dunk.django.repository.BoardRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO dto) {
        Board entity = bindToEntity(dto);

        boardRepository.save(entity);

        return entity.getBno();
    }

    @Override
    public Board get(Long bno) {
        Optional<Board> entity = boardRepository.findById(bno);

        return entity.get();
    }

    @Override
    public Long modify(BoardDTO dto) {
        boardRepository.updateQuery(dto.getTitle(), dto.getContent(), dto.getBno());

        return dto.getBno();
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public GenericListDTO<BoardDTO, Board> getList(PageDTO pageDTO) {
        final Sort sort = Sort.by("bno").descending();

        Pageable pageable = pageDTO.makePageable(sort);

        Page<Board> result = boardRepository.findAll(pageable);

        log.info(result);

        GenericListDTO<BoardDTO, Board> listDTO
        = new GenericListDTO<>(result, en -> bindToDTO(en));

        return listDTO;
    }
    
}