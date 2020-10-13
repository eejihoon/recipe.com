package com.dunk.django.repository;

import java.util.List;
import javax.transaction.Transactional;

import com.dunk.django.domain.UserFridge;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// 메서드 추가
public interface UserFridgeRepository extends JpaRepository<UserFridge, Long> {

    @Query(value = "select f from UserFridge f where f.username = :userid")
    List<UserFridge> getUserNameAndPage(@Param("userid") String userid, Sort sort);

    @Transactional
    @Modifying
    @Query(value = "delete from UserFridge f where f.username = :userid ")
    void removeUsername(@Param("userid") String userid);

    // 식재료를 넣을때 그 식재료의 카테고리를 getCategoryCno메서드로 받아서 넣고.
    // 넣은 카테고리 값으로 유통기한까지 자동으로 계산하게 한다.
    // register 메서드 대신 이 메서드를 사용해서 스캔한 다음 재료들을 등록한다.
    @Transactional
    @Modifying
    @Query(value = "insert into user_fridge (username, ingr_name, cno , expirationdate)"
            + "values (:#{#fridge.username} , :#{#fridge.ingr_name} ,:#{#fridge.cno},"
            + "(select date_add(now(), interval(select c.expirationdate from tbl_category c join user_fridge f "
            + "where c.cno = :#{#fridge.cno} group by c.cno )day)))", nativeQuery = true)
    int insertAfterCalDate(@Param("fridge") UserFridge fridge);

    // insertCalDate2 메서드의 cno값으로 지정하기 위한 메서드
    // 식재료를 넣어서 식재료의 이름이 들어간 태그를 찾아서 그 cno값을 받아온다.
    // int인 이유는 카테고리를 분류할땐 한 재료가 하나의 카테고리를 갖기때문이다.
    @Query(value = "select c.cno from tbl_category c where tag like concat('%', :ingr_name, '%')", nativeQuery = true)
    Long getCategoryCno(@Param("ingr_name") String ingr_name);

    // 등록한 식재료를 태그랑 비교해서 태그를 가지고있는 카테고리 뽑아오기.
    @Query(value = "select c.ingr_category from user_fridge u , tbl_category c "
            + "where c.tag like concat ('%', :ingr_name, '%') " + "group by c.tag", nativeQuery = true)
    String searchCategory(@Param("ingr_name") String ingr_name);
}