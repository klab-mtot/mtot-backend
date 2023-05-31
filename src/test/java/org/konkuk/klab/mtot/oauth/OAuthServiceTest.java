package org.konkuk.klab.mtot.oauth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.MemberTeamRepository;
import org.konkuk.klab.mtot.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MemberTeamRepository memberTeamRepository;
    
    @Test
    @DisplayName("Google User가 주어졌을 때 회원가입을 올바르게 처리한다.")
    public void oAuthSignInTest(){
        String email = "abc@mail.com";
        GoogleUser user = GoogleUser.builder()
                .name("donghoony")
                .email(email)
                .picture("http://picture_in_here.something.url")
                .build();

        OAuthResponse response = oAuthService.login(user);
        assertThat(response.getEmail()).isEqualTo(email);

        Optional<Member> byEmail = memberRepository.findByEmail(email);
        assertThat(byEmail).isNotEmpty();
        assertThat(byEmail.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Google User가 주어졌을 때 로그인을 올바르게 처리한다.")
    public void oAuthLoginTest(){
        String email = "abc@mail.com";
        Member member = new Member("Donghoony", email);
        memberRepository.save(member);

        GoogleUser user = GoogleUser.builder()
                .name("Donghoony")
                .email(email)
                .build();

        OAuthResponse response = oAuthService.login(user);
        assertThat(response.getEmail()).isEqualTo(email);

        Optional<Member> byEmail = memberRepository.findByEmail(email);
        assertThat(byEmail).isNotEmpty();
        assertThat(byEmail.get().getEmail()).isEqualTo(email);

        // memberRepository에 넣어서 진행하므로 그룹이 자동으로 생성되지 않음 (로그인 성공)
        assertThat(teamRepository.findAll()).isEmpty();
    }

    @AfterEach
    public void tearDown(){
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
        memberRepository.deleteAll();
    }
}