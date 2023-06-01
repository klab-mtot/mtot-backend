package org.konkuk.klab.mtot.exception;

public class DuplicatePostException extends RuntimeException{
    public DuplicatePostException() {
        super("이미 포스트가 존재합니다.");
    }
}
