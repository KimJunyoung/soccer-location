package com.soccerlocation.exception;

import com.soccerlocation.config.validation.ValidationTuples;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class MainException extends RuntimeException{

    public final List<ValidationTuples> validation = new ArrayList<>();

    public MainException() {
    }

    public MainException(String message) {
        super(message);
    }

    public MainException(String message, Throwable cause) {
        super(message, cause);
    }


    public abstract int statusCode();

    public void addValidation(String fieldName, String message){
        validation.add(new ValidationTuples(fieldName, message));
    }
}
