package com.soccerlocation.controller;

import com.soccerlocation.domain.Member;
import com.soccerlocation.exception.InvalidRequest;
import com.soccerlocation.exception.InvalidSingingInformation;
import com.soccerlocation.repository.MemberRepository;
import com.soccerlocation.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;

    @PostMapping("/auth/login")
    public Member login(@RequestBody Login login){
         // json 아이디/ 비밀번호 받기
        log.info(">>> {}", login);

        // DB에서 조회
        Member user = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSingingInformation::new);



        // 토큰을 응답
        return user;
    }
}
