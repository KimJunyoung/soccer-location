package com.soccerlocation.domain;

import com.soccerlocation.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1(){
            Member member = Member.builder()
                    .name("김준영")
                    .email("985151@naver.com")
                    .password("!wns6713")
                    .createdAt(LocalDateTime.now())
                    .build();

                em.persist(member);
        }

    }
}
