package com.soccerlocation.response;


import com.soccerlocation.config.validation.ValidationTuples;
import lombok.Builder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;


/**
 * {
 *      "code" : "400",
 *      "message" : "잘못된 요청입니다."
 *       "validation" : {
 *            "title" : " 값을 입력해주세요"
 *       }
 * }
 */

@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;

    // 개선이 필요하다. Map을 어떤걸로 바꿔야할까?
    // class로 하자니 아 ! 잠시만

    private final List<ValidationTuples> validation;

    @Builder
    public ErrorResponse(String code, String message, List<ValidationTuples> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String field, String errorMessage){
       this.validation.add(new ValidationTuples(field, errorMessage));
    }

}
