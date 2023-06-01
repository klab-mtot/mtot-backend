package org.konkuk.klab.mtot.exception;

public class JourneyNotFoundException extends RuntimeException {
    public JourneyNotFoundException() {
        super("여정이 존재하지 않습니다.");
    }
}
