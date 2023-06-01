package org.konkuk.klab.mtot.exception;

public class FriendshipNotFoundException extends RuntimeException{
    public FriendshipNotFoundException(){
        super("친구 관계를 찾을 수 없습니다.");
    }
}
