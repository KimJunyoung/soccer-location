package com.soccerlocation.exception;

/**
 * 401
 */
public class Unauthorized extends MainException{

    private static final String MESSAGE = "인증이 필요합니다..";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int statusCode(){
        return 401;
    }

}
