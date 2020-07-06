package com.unity.common.base;

import com.unity.common.constants.Headers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SessionHolder {

    private final static String TOKEN = "token";

    private static Map<String,String> excelCatch = new HashMap<>();

    public static Map<String,String> getCatch() {
        return excelCatch;
    }

    public static HttpSession getSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getSession();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static HttpServletRequest getRequest() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request;
        } catch (NullPointerException e) {
            return null;
        }
    }


    public static String getToken() {
        HttpServletRequest req = SessionHolder.getRequest();
        return getTokenByRequest(req);
    }

    public static synchronized String getTokenByRequest(HttpServletRequest req) {
        if (req == null) {
            return null;
        }
        String authToken = req.getHeader(Headers.AUTH_TOKEN);
        if (StringUtils.isEmpty(authToken)) {
            authToken = req.getParameter(TOKEN);
            if (StringUtils.isEmpty(authToken)) {
                authToken = ContextHolder.get(Headers.AUTH_TOKEN);
            }
        }
        return authToken;
    }

    public static HttpServletResponse getResponse() {

        HttpServletResponse response;
        try {
            response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            return response;
        } catch (Exception ex) {
            return null;
        }

    }
}
