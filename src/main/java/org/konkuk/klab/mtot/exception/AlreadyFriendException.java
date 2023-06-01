package org.konkuk.klab.mtot.exception;

public class AlreadyFriendException extends RuntimeException{
    public AlreadyFriendException() {
        super("이미 친구입니다.");
    }
}
