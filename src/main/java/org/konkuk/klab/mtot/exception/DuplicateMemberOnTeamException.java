package org.konkuk.klab.mtot.exception;

public class DuplicateMemberOnTeamException extends RuntimeException{
    public DuplicateMemberOnTeamException(){
        super("그룹에 이미 존재하는 멤버입니다.");
    }
}
