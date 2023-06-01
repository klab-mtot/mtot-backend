package org.konkuk.klab.mtot.exception;

public class PinNotFound extends RuntimeException{
    public PinNotFound() {
        super("핀이 존재하지 않습니다.");
    }
}
