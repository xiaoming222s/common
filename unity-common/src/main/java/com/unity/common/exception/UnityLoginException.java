package com.unity.common.exception;

import com.unity.common.pojos.SystemResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UnityLoginException extends RuntimeException {

//    @Builder.Default
    private SystemResponse.FormalErrorCode code;
    private String message;

    public UnityLoginException(){
        this.code = SystemResponse.FormalErrorCode.LOGIN_DATA_ERR;
        this.message = SystemResponse.FormalErrorCode.LOGIN_DATA_ERR.getName();
    }
}
