package org.konkuk.klab.mtot.oauth;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Transactional
    public OAuthResponse login(GoogleUser googleUser){
        String token;
        Member member = memberRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> memberRepository.save(new Member(googleUser.getName(), googleUser.getEmail())));
        token = jwtService.generateToken(member.getEmail());
        return new OAuthResponse(token, member.getEmail());
    }
}
