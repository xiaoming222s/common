package com.unity.springboot.support.holder;

import com.alibaba.fastjson.JSON;
import com.unity.common.pojos.SystemResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义权限不足异常 403
 * <p>
 * create by gengjiajia at 2019/08/05 16:13
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        if(accessDeniedException != null){
            final PrintWriter writer = response.getWriter();
            try {
                SystemResponse systemResponse = new SystemResponse();
                systemResponse.setCode(SystemResponse.FormalErrorCode.OPERATION_NO_AUTHORITY.getValue());
                systemResponse.setMessage("未授权或无此权限");
                writer.write(JSON.toJSONString(systemResponse));
            } finally {
                log.error("====== 《CustomAccessDeniedHandler》 权限认证 403 异常 {}",accessDeniedException.toString());
                if(writer != null){
                    writer.close();
                }
            }
        } else {
            log.error("====== 《CustomAccessDeniedHandler》 权限认证 403 但异常信息为空");
        }
    }
}
