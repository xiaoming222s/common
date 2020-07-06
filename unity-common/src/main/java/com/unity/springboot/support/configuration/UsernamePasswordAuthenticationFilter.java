/*
package com.unity.springboot.support.configuration;

import com.google.common.collect.Lists;
import com.unity.common.base.SessionHolder;
import com.unity.common.pojos.AuthUser;
import com.unity.common.pojos.MyUserDetails;
import com.unity.common.util.RedisUtils;
import com.unity.springboot.support.holder.LoginContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

*/
/**
 * 用户验证类
 * <p>
 * create By Jung at 2018-02-20 16:39:10
 *//*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class UsernamePasswordAuthenticationFilter implements Filter {

    private static final String authorized = "_authorized";

    private static final String FILTER_APPLIED = "__spring_security_UsernamePasswordAuthenticationFilter_filterApplied";


    @Resource
    private RedisUtils redisUtils;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //去掉重复次数
        */
/*HttpServletRequest req = ((HttpServletRequest) request);
        if (req.getAttribute(FILTER_APPLIED) == null) {
            req.setAttribute(FILTER_APPLIED,true);
            chain.doFilter(request, response);
            return ;
        }*//*


        String authToken = SessionHolder.getToken();

        log.info("======《Filter》===METHOD {} ===URL {} ===authToken {}",SessionHolder.getRequest().getMethod(),SessionHolder.getRequest().getRequestURL(), authToken);
        //String authToken;
        //String userAgent = ((HttpServletRequest) request).getHeader(Headers.USER_AGENT);
        */
/*log.info("===Filter===《doFilter》===userAgent {}",userAgent);
        if(userAgent.contains(Headers.USER_AGENT_ANDROID)||userAgent.contains(Headers.USER_AGENT_IPAD)||userAgent.contains(Headers.USER_AGENT_IPHONE)){*//*


//        HttpServletRequest req = ((HttpServletRequest) request);
//        log.info("===Filter==={}===sessionId {}", req.getRequestURI(),req.getSession().getId());
//
//        String authToken = req.getHeader(Headers.AUTH_TOKEN);
//        if(StringUtils.isEmpty(authToken)){
//            authToken = req.getSession().getId();
//        }
//        log.info("===Filter==={}===authToken {}", req.getRequestURI(), authToken);

        SecurityContextHolder.getContext().setAuthentication(null);
        LoginContextHolder.setLoginAttributes(null);

        log.info("***********************************************************");
        AuthUser currentUser = redisUtils.getCurrentUserByToken(authToken);
        log.info("======redisUtils.getCurrentUserByToken(authToken)====== {}",currentUser==null);
        try {
            if (currentUser != null) {
                //log.info("======《Filter》======AuthUser {}", GsonUtils.format(currentUser));
                LoginContextHolder.setLoginAttributes(currentUser);
                Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
                if(currentUser.getAuth()!=null) {
                    List<String> authList = currentUser.getAuth();
                    authList.forEach((item) ->
                            authorities.add(new SimpleGrantedAuthority(item))
                    );
                }
                UserDetails userDetails = new MyUserDetails(currentUser.getId(), currentUser.getLoginName(), currentUser.getPwd(), true, authorities);
                PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
                //((HttpServletRequest) request).getSession().setAttribute(authorized, token);
            }
        } catch (Throwable t) {
            log.error("Authenticator throw an THROWABLE", t);
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}*/
