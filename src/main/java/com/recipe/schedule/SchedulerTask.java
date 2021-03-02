package com.recipe.schedule;

import com.recipe.member.domain.Member;
import com.recipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SchedulerTask {
    private final MemberRepository memberRepository;

    /*
    *   매일 오전 04시에 한 번만 실행
    * */
    @Scheduled(cron = "0 0 4 * * ?")
    public void athenticationTimeOutMemberDeleteTask() {
        log.info("Schedule Task Run... Time : {} ", LocalDateTime.now());
        List<Member> allMember = memberRepository.findAll();

        log.info("==========================================================|");
        for (Member member : allMember) {
            if (member.isAuthenticationTimeOut()) {
                log.info("---------Target Member email: {} ", member.getEmail());
                log.info("---------Target Member nickname: {} ", member.getNickname());
                memberRepository.delete(member);
            }
        }
        log.info("==========================================================|");
    }
}
