package org.konkuk.klab.mtot.exception;

public class DuplicateFriendshipException extends RuntimeException{
    public DuplicateFriendshipException(){
        super("이미 존재하는 친구 관계 입니다.");
    }
}
