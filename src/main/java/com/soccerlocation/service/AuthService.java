package com.soccerlocation.service;

import com.soccerlocation.crypto.ScryptPasswordEncoder;
import com.soccerlocation.domain.Member;
import com.soccerlocation.exception.AlreadyExistsEmailException;
import com.soccerlocation.exception.InvalidSingingInformation;
import com.soccerlocation.repository.MemberRepository;
import com.soccerlocation.request.Login;
import com.soccerlocation.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final ScryptPasswordEncoder passwordEncoder;


    public Long signIn(Login login){
        Member user = memberRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSingingInformation::new);

        boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());

        if (!matches) {
            throw new InvalidSingingInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<Member> userOptional = memberRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encodedPw = passwordEncoder.encrypt(signup.getPassword());


        Member user = Member.builder()
                .name(signup.getName())
                .email(signup.getEmail())
                .password(encodedPw)
                .build();
        memberRepository.save(user);
    }
}
