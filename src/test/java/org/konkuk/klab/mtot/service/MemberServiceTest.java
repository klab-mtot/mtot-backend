package org.konkuk.klab.mtot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.MemberSignUpRequest;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test
    @DisplayName("회원 가입을 성공적으로 진행한다")
    public void signUpTest(){
        String mail = "abc@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("donghoony", mail, "q1w2e3r4");
        memberService.join(request);

        Optional<Member> users = memberRepository.findByEmail(mail);
        assertThat(users).isNotEmpty();
        assertThat(users.get().getEmail()).isEqualTo(mail);
    }

    @Test
    @DisplayName("회원 중복 가입을 방지한다")
    public void duplicateUserSignUpTest(){
        String mail = "abc@naver.com";
        MemberSignUpRequest request = new MemberSignUpRequest("donghoony", mail, "q1w2e3r4");
        memberService.join(request);

        assertThatThrownBy(()-> memberService.join(request))
                .isInstanceOf(RuntimeException.class);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }
}