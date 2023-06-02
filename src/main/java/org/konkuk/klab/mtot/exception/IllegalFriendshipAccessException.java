package org.konkuk.klab.mtot.exception;

public class IllegalFriendshipAccessException extends RuntimeException {
    public IllegalFriendshipAccessException() {
        super("유효한 친구신청이 아닙니다.");
    }
}
