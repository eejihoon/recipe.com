package com.dunk.django.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dunk.django.domain.DjangoMember;
import com.dunk.django.domain.MemberRole;
import com.dunk.django.dto.DjangoMemberDTO;
import com.dunk.django.repository.MemberRepository;

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

    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        log.info("load User ..." + username);
        
        Optional<DjangoMember> result = repository.findById(username);
        if(result.isPresent()) {

            DjangoMember member = result.get();

            List<SimpleGrantedAuthority> list = member.getRoleSets().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
            .collect(Collectors.toList());

            DjangoMemberDTO dto = new DjangoMemberDTO(member.getUserId() ,member.getId()
            ,member.getPassword(),member.getName(), list);

            return dto;
        }
        
        return null;
    }

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