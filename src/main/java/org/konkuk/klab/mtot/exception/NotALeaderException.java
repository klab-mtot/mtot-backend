package org.konkuk.klab.mtot.exception;

public class NotALeaderException extends RuntimeException{
    public NotALeaderException() {
        super("해당 멤버는 그룹의 리더가 아닙니다.");
    }
}
