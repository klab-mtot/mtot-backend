package org.konkuk.klab.mtot.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("이메일로 JWT 토큰을 생성한다")
    public void createJwtToken(){
        String token = jwtService.generateToken("abc@mail.com");
        assertThat(token)
                .isNotBlank()
                .isNotNull()
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("생성한 토큰을 검증한다")
    public void verifyToken(){
        String token = jwtService.generateToken("abc@mail.com");
        assertThatNoException().isThrownBy(()-> jwtService.verifyToken(token));
    }

    @Test
    @DisplayName("토큰에서 정보를 가져온다.")
    public void extractPayloadFromToken(){
        String email = "abc@mail.com";
        String token = jwtService.generateToken(email);
        assertThat(jwtService.getPayload(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("만료된 토큰은 예외를 발생한다.")
    public void raiseExceptionWhenExpiredTokenIsGiven(){
        JwtService newJwtService = new JwtService("SECRET", -1L);
        String token = newJwtService.generateToken("abc@mail.com");
        assertThatThrownBy(()-> newJwtService.verifyToken(token))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 예외를 발생한다.")
    public void raiseExceptionWhenInvalidateTokenIsGiven(){
        String token = "abcde.ablkasndv.asklnwe";
        assertThatThrownBy(()-> jwtService.verifyToken(token))
                .isInstanceOf(RuntimeException.class);
    }

}