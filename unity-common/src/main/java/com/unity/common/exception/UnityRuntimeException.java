package com.unity.common.exception;

import com.unity.common.pojos.SystemResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
public class UnityRuntimeException extends RuntimeException {

    @Builder.Default
    private SystemResponse.FormalErrorCode code = SystemResponse.FormalErrorCode.SERVER_ERROR;
    private String message;

    public UnityRuntimeException(Exception ex){
        this.code = SystemResponse.FormalErrorCode.SERVER_ERROR;
        this.message = ex.getMessage();
    }

    public UnityRuntimeException(String message){
        this.code = SystemResponse.FormalErrorCode.SERVER_ERROR;
        this.message = message;
    }

    public UnityRuntimeException(SystemResponse.FormalErrorCode code){
        this.code = code;
    }
}
