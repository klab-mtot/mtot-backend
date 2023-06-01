package org.konkuk.klab.mtot.exception;

public class TeamAccessDeniedException extends RuntimeException {
    public TeamAccessDeniedException() {
        super("멤버가 그룹에 속하지 않습니다.");
    }
}
