package com.dunk.django.repository;

import javax.transaction.Transactional;

import com.dunk.django.domain.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> { // JpaRepository<Entity, ID>

    @Modifying
    @Transactional
    @Query("update Board b set b.title=:title, b.content = :content where bno = :bno ")
    int updateQuery(@Param("title")String title, @Param("content")String content, @Param("bno")Long bno);

}