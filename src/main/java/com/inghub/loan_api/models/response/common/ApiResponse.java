package com.inghub.loan_api.models.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ApiResponse {
    private Object data = new Object();
    private String message ="";
    private String errorCode = "";

    public static ApiResponse success(Object data){
        return ApiResponse.builder().data(data).build();
    }

    public static ApiResponse success(){
        return ApiResponse.builder().build();
    }

    public static ApiResponse fail(String message, String errorCode){
        return ApiResponse.builder().message(message).errorCode(errorCode).build();
    }
}
