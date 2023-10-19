package com.soccerlocation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerlocation.domain.Member;
import com.soccerlocation.domain.Session;
import com.soccerlocation.repository.MemberRepository;
import com.soccerlocation.repository.SessionRepository;
import com.soccerlocation.request.Login;
import com.soccerlocation.request.PostCreate;
import com.soccerlocation.request.Signup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void testV1() throws Exception {

        memberRepository.save(Member.builder()
                .name("김준영")
                .email("985151@naver.com")
                .password("!wns6713")
                .build());

        /**
         * 보낼 내용 :
         *  글 제목, 글 내용
         */

        Login login = Login.builder()
                .email("985151@naver.com")
                .password("!wns6713")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andDo(print());
    }


    @Test
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void testV2() throws Exception {

        Member user = memberRepository.save(Member.builder()
                .name("김준영")
                .email("985151@naver.com")
                .password("!wns6713")
                .build());

        /**
         * 보낼 내용 :
         *  글 제목, 글 내용
         */

        Login login = Login.builder()
                .email("985151@naver.com")
                .password("!wns6713")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andDo(print());

        Member loggedInUser = memberRepository.findById(user.getId())
                .orElseThrow(RuntimeException::new);

        Assertions.assertEquals(1, loggedInUser.getSessions().size());
    }


    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void testV3() throws Exception {

        Member user = memberRepository.save(Member.builder()
                .name("김준영")
                .email("985151@naver.com")
                .password("!wns6713")
                .build());

        /**
         * 보낼 내용 :
         *  글 제목, 글 내용
         */

        Login login = Login.builder()
                .email("985151@naver.com")
                .password("!wns6713")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
               .andExpect(jsonPath("$.accessToken").value(user.getSessions().get(0).getAccessToken()))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation[0].fieldName").value("title"))
                .andDo(print());


    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test4() throws Exception {
        // given

        Member user = memberRepository.save(Member.builder()
                .name("김준영")
                .email("985151@naver.com")
                .password("!wns6713")
                .build());

        Session session = user.addSession();
        memberRepository.save(user);


        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    @DisplayName("로그인 후 검증되지 않은 세션 값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        // given

        Member user = memberRepository.save(Member.builder()
                .name("김준영")
                .email("985151@naver.com")
                .password("!wns6713")
                .build());

        Session session = user.addSession();
        memberRepository.save(user);


        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-o")
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @DisplayName("회원가입.")
    void test6() throws Exception {
        // given
        Signup signup = Signup.builder()
                .password("!wns6713")
                .email("985151@naver.com")
                .name("김준영")
                .build();

        // expected
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON)
                ) // application/json
                .andExpect(status().isOk())
                .andDo(print());

    }


}