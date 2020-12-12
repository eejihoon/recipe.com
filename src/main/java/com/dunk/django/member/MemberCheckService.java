package com.dunk.django.member;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dunk.django.domain.DjangoMember;
import com.dunk.django.domain.MemberRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional
public class MemberCheckService implements UserDetailsService, MemberService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder pwEncoder;

    

    /**
     *  유저 정보를 dto로 변환해서 return한다. 
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        log.info("load User ..." + username);
        //아이디가 존재하는 지 체크
        Optional<DjangoMember> result = repository.findById(username);
        
        if(result.isPresent()) {    //아이디가 존재하는 경우
            //해당 아이디 정보를 가져 온다.
            DjangoMember member = result.get();
            //해당 아이디의 권한을 가져 온다.
            List<SimpleGrantedAuthority> list = member.getRoleSets().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
            .collect(Collectors.toList());
            //DTO로 변환한다.
            DjangoMemberDTO dto = new DjangoMemberDTO(member.getUserId() ,member.getId()
            ,member.getPassword(),member.getName(), list);
            
            return dto;
        }
        
        return null;
    }

    /**
     *  회원 등록하는 메서드
     */
    @Override
    public void register(DjangoMember member) {
        log.info("==============================");
        log.info(member);

        MemberRole role = MemberRole.builder().roleName("ROLE_MEMBER").build();

        member.setPassword(pwEncoder.encode(member.getPassword()));

        member.addRole(role);

        log.info(member);

        repository.save(member);
    }

    @Override
    public Optional<DjangoMember> getMno(String id) {
        return repository.findById(id);
    }
}