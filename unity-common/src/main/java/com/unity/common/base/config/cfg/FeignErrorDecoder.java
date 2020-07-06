package com.unity.common.base.config.cfg;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unity.common.exception.ExceptionInfo;
import com.unity.common.exception.FeignError;
import com.unity.common.exception.UnityLoginException;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {

        try {
//            log.info("FeignErrorDecoder=======response======="+JSON.toJSONString(response.toString()));
            if (response.body() != null){// && response.status() == 500) {
                String targetMsg = null;
                String body = Util.toString(response.body().asReader());
//                log.info("FeignErrorDecoder=====s=="+s+"======="+body);
                //return new UnityRuntimeException(body);
                ExceptionInfo ei = JSON.parseObject(body, ExceptionInfo.class);
                try{
                    FeignError err = JSON.parseObject(ei.getMessage(),FeignError.class);
                    if(err.getCode().equals(SystemResponse.FormalErrorCode.LOGIN_DATA_ERR.getValue())){
                        return new UnityLoginException();
                    }
                    else{
                        return UnityRuntimeException.newInstance()
                                .message(err.getMessage())
                                .code(SystemResponse.FormalErrorCode.of(err.getCode())).build();
                    }
                }
                catch (Exception e){
                    return new UnityRuntimeException(ei.getMessage());
                }

            }
        } catch (Exception var4) {
            log.error(var4.getMessage());
            return new UnityRuntimeException(var4.getMessage());
        }
        return new UnityRuntimeException("系统异常,请联系管理员");
    }

}
