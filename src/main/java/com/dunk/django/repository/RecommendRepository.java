package com.dunk.django.repository;

import java.util.List;
import java.util.Optional;

import com.dunk.django.domain.Recommend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    @Query("select rid, itemID, value from Recommend where rid=:rid")
    List<Object[]> select(@Param("rid") Long rid);

    // @Query("select rid, itemID, value from Recommend where rid=:rid")

    List<Recommend> findByRid(Long rid);

}