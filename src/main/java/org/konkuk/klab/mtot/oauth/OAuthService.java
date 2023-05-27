package org.konkuk.klab.mtot.oauth;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public OAuthResponse login(GoogleUser googleUser){
        Optional<Member> byEmail = memberRepository.findByEmail(googleUser.getEmail());
        if (byEmail.isPresent()){
            Member member = byEmail.get();
            String token = jwtService.generateToken(member.getEmail());
            return new OAuthResponse(token, member.getEmail());
        }
        else{
            Member member = new Member(googleUser.getName(), googleUser.getEmail());
            memberRepository.save(member);
            String token = jwtService.generateToken(member.getEmail());
            return new OAuthResponse(token, member.getEmail());
        }
    }
}
