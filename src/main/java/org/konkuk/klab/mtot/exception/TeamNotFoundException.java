package org.konkuk.klab.mtot.exception;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(){
        super("그룹을 찾을 수 없습니다.");
    }
}
