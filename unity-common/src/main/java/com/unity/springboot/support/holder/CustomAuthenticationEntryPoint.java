package com.unity.springboot.support.holder;

import com.alibaba.fastjson.JSON;
import com.unity.common.pojos.SystemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义无效token异常 401
 * <p>
 * create by gengjiajia at 2019/08/05 15:58
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //设置返回状态码401
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        //writer 输出
        if(authException != null){
            Lock lock = new ReentrantLock();
            lock.lock();
            PrintWriter writer = response.getWriter();
            try {
                SystemResponse systemResponse = new SystemResponse();
                if(authException instanceof InsufficientAuthenticationException){
                    systemResponse.setCode(SystemResponse.FormalErrorCode.LOGIN_DATA_ERR.getValue());
                    systemResponse.setMessage("token已失效，请重新登录");
                } else {
                    systemResponse.setCode(SystemResponse.FormalErrorCode.OPERATION_NO_AUTHORITY.getValue());
                    systemResponse.setMessage("操作过快，请重试一次");
                }
                writer.write(JSON.toJSONString(systemResponse));
            } finally {
                log.error("====== 《CustomAuthenticationEntryPoint》 无效token 401 异常 {}",authException.toString());
                //别忘了close
                lock.unlock();
                if(writer != null){
                    writer.close();
                }
            }
        } else {
            log.error("====== 《CustomAuthenticationEntryPoint》 无效token 401 但异常信息为空");
        }
    }
}
