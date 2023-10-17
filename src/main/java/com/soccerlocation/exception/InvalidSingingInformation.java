package com.soccerlocation.exception;

public class InvalidSingingInformation extends MainException{

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSingingInformation() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
