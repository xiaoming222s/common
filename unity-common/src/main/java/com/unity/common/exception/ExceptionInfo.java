package com.unity.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newInstance")
public class ExceptionInfo {
    private Date timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
