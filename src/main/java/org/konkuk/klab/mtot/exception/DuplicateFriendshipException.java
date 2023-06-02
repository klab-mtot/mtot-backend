package org.konkuk.klab.mtot.exception;

public class DuplicateFriendshipException extends RuntimeException{
    public DuplicateFriendshipException(){
        super("친구 요청을 이미 보냈습니다.");
    }
}
