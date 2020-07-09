package com.dunk.django.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.dunk.django.domain.Preferences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PreferenceRepository extends JpaRepository<Preferences, Long> {
    
    /*연관관계 매핑을 하지 않는다면?*/
    List<Preferences> findByUserId(Long userId);

    Preferences findByUserIdAndItemId(Long userId, Long itemId);

    @Transactional
    @Modifying
    @Query("UPDATE Preferences p SET p.preference=:preference WHERE p.userId=:userId AND p.itemId=:itemId")
    int update(@Param("preference")float preference, @Param("userId")Long userId, @Param("itemId")Long itemId);


    /* 연관관계
    //전체 리스트를 불러온다. 근데 굳이 조인을 걸 필요가 있을까?
    @Query(value = "SELECT p.user_id, p.item_id, p.preference " 
                    +"FROM taste_preferences p INNER JOIN django_member d ON p.user_id = d.mno"
    , nativeQuery = true)
    @Query(value = "SELECT user_id, item_id, preference FROM taste_preferences", nativeQuery=true)
    List<Object[]> list();

    @Query(value = "SELECT user_id, item_id, preference FROM taste_preferences WHERE user_id = user_id AND item_id = item_id"
    , nativeQuery = true)
    List<Object[]> get(@Param("user_id")Long user_id, @Param("item_id")Long item_id);
    */

}