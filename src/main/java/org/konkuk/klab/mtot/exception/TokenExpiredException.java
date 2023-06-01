package org.konkuk.klab.mtot.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("토큰이 만료되었습니다.");
    }
}
