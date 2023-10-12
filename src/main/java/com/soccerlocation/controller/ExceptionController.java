package com.soccerlocation.controller;

import com.soccerlocation.config.validation.ValidationTuples;
import com.soccerlocation.exception.InvalidRequest;
import com.soccerlocation.exception.MainException;
import com.soccerlocation.exception.PostNotFound;
import com.soccerlocation.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    /**
     * ControllerAdvice 만 달아두면 return 값을 json 형태로 바로 반환하지 못한다.
     *  그래서 ResponseBody 를 달아줘야한다.
     * */

     @ResponseStatus(HttpStatus.BAD_REQUEST)
     @ExceptionHandler(MethodArgumentNotValidException.class)
     @ResponseBody
     public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){

         List<ValidationTuples> ex = new ArrayList<>();

         ErrorResponse response = ErrorResponse
                 .builder()
                 .code("400")
                 .message("잘못된 요청입니다.")
                 .validation(ex)
                 .build();

         for(FieldError fieldError : e.getFieldErrors()){
             response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
         }

         return response;

     }

     @ResponseBody
     @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e){
         ErrorResponse response = ErrorResponse
                 .builder()
                 .code("404")
                 .message(e.getMessage())
                 .build();

         return response;
     }


     @ResponseBody
//     @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MainException.class)
    public ResponseEntity<ErrorResponse> MainException(MainException e){

         int statusCode = e.statusCode();

         ErrorResponse body = ErrorResponse
                 .builder()
                 .code(String.valueOf(statusCode))
                 .message(e.getMessage())
                 .validation(e.getValidation())
                 .build();

         // 응답 json validation -> title : 제목을 바보를 포함할 수 없습니다.



         ResponseEntity<ErrorResponse> response = (ResponseEntity<ErrorResponse>) ResponseEntity
                 .status(statusCode)
                 .body(body);

         return response;
     }
}
