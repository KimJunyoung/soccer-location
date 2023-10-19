package com.soccerlocation.service;

import com.soccerlocation.crypto.ScryptPasswordEncoder;
import com.soccerlocation.domain.Member;
import com.soccerlocation.exception.AlreadyExistsEmailException;
import com.soccerlocation.exception.InvalidSingingInformation;
import com.soccerlocation.repository.MemberRepository;
import com.soccerlocation.request.Login;
import com.soccerlocation.request.Signup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
            Signup signup = Signup.builder()
                    .password("!wns6713")
                    .email("youngkim6713@hanmail.net")
                    .name("김준영")
                    .build();


        // when
        authService.signup(signup);
        // then
        assertEquals(1, memberRepository.count());

        Member user = memberRepository.findAll().iterator().next();
        assertNotEquals("!wns6713", user.getPassword());
        assertNotNull(user.getPassword());

    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        // given
            Signup signup = Signup.builder()
                    .password("!wns6713")
                    .email("985151@naver.com")
                    .name("김준영")
                    .build();


        Signup signup2 = Signup.builder()
                .password("!wns6713")
                .email("985151@naver.com")
                .name("김호돌")
                .build();


        // when
        authService.signup(signup);

        // then
        Assertions.assertThatThrownBy(() -> authService.signup(signup2)).isInstanceOf(AlreadyExistsEmailException.class);

    }

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptPw = encoder.encrypt("1234");

        Member user = Member.builder()
                .email("985151@naver.com")
                .password(encryptPw)
                .name("짱돌맨")
                .build();

        memberRepository.save(user);

        Login login = Login.builder()
                .email("985151@naver.com")
                .password("1234")
                .build();


        // when
        Long userId = authService.signIn(login);

        // then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인 실패")
    void test4() {
        // given
            Signup signup = Signup.builder()
                    .password("!wns6713")
                    .email("985151@naver.com")
                    .name("김준영")
                    .build();

        authService.signup(signup);

        Login login = Login.builder()
                .email("985151@naver.com")
                .password("!1234")
                .build();


        // Expected
        Assertions.assertThatThrownBy(() -> authService.signIn(login)).isInstanceOf(InvalidSingingInformation.class);
    }




}