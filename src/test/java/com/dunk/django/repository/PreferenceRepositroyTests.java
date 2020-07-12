package com.dunk.django.repository;

import com.dunk.django.domain.Preferences;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PreferenceRepositroyTests {
    
    @Autowired
    PreferenceRepository repository;

    @Test
    public void testConnect() {
        System.out.println(repository);
    }

    /*연관관계 매핑을 하지 않는다면 */
    @Test
    public void testFindByUserId() {
        repository.findByUserId(1L).forEach(row->System.out.println(row));
    }

    @Test
    public void testFindByUserIdAndItemId() {
        System.out.println(repository.findByUserIdAndItemId(1L, 7L));
    }

    @Test
    public void testInsert() {
        
        Preferences entity = Preferences.builder()
        .userId(1L)
        .itemId(6L)
        .preference(1)
        .build();

        repository.save(entity);

    }

    @Test
    public void testUpdate() {
        //업데이트 할 사용자 정보
        Preferences dto = repository.findByUserIdAndItemId(1L, 6L);

        System.out.println("dto : " + dto);

        // System.out.println("pre : " + entity);

        //기존 점수 +1
        // float preference = entity.getPreference()+1;

        // System.out.println("preference : " + preference);

        //set
        // entity.setPreference(preference);

        //update
        // repository.update(preference, entity.getUserId(), entity.getItemId());

    }

    /* 연관관계 테스트
    @Transactional
    @Test
    public void testGetList(){

        //outer join날려서 불필요한 데이터까지 다 가져옴.
        repository.findAll().forEach(row -> System.out.println(row.toString()));
        
    }

    @Test
    public void testQuerAnnoGetList(){

        repository.list().forEach(row->System.out.println(Arrays.toString(row)));     

    }

    @Test
    public void get() {

        repository.get(1L,3L).forEach(row -> System.out.println(Arrays.toString(row)));

    }
    */

}