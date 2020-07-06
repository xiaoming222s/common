package com.unity.springboot.support.configuration;

import com.alibaba.fastjson.JSON;
import com.unity.common.base.SessionHolder;
import com.unity.common.exception.UnityLoginException;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.AuthUser;
import com.unity.common.pojos.MyUserDetails;
import com.unity.common.pojos.SystemResponse;
import com.unity.common.util.RedisUtils;
import com.unity.springboot.support.holder.LoginContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final RedisUtils redisUtils;
    @Resource
    private UrlSecurityConfiguration urlSecurityConfiguration;

    public JwtAuthorizationTokenFilter(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String token = SessionHolder.getTokenByRequest(request);
        String requestURI = request.getRequestURI();
        log.info("======《Filter》===URL {} ===authToken {}", request.getRequestURL(), token);
        SecurityContextHolder.getContext().setAuthentication(null);
        LoginContextHolder.setLoginAttributes(null);
        try {
            AuthUser currentUser = redisUtils.getCurrentUserByToken(token);
            if (currentUser != null) {
                LoginContextHolder.setLoginAttributes(currentUser);
                Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(requestURI);
                authorities.add(simpleGrantedAuthority);
//                if (currentUser.getAuth() != null) {
//                    authorities = currentUser.getAuth().parallelStream()
//                            .map(SimpleGrantedAuthority::new)
//                            .collect(Collectors.toList());
//                }
                UserDetails userDetails = new MyUserDetails(currentUser.getId(), currentUser.getLoginName(),
                        currentUser.getPwd(), true, authorities);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } else {
                log.error("===== 《Filter》 获取用户信息为空 token {}", token);
                writeExceptionInfo(request, response, requestURI, chain);
            }
        } catch (Exception e) {
            log.error("====== 《Filter》 异常 Authenticator throw an Exception {}", e.toString());
//                throw new InsufficientAuthenticationException("token已失效，请重新登录");
            writeExceptionInfo(request, response, requestURI, chain);
            SecurityContextHolder.clearContext();
        } catch (Throwable t) {
            log.error("====== 《Filter》 异常 Authenticator throw an THROWABLE {}", t.toString());
            writeExceptionInfo(request, response, requestURI, chain);
            SecurityContextHolder.clearContext();
        }

    }

    public void writeExceptionInfo(HttpServletRequest request, HttpServletResponse response, String requestURI, FilterChain chain) {
        try {
            List<String> list = Arrays.asList(urlSecurityConfiguration.getPermit());
            if (!list.contains(requestURI)) {
                response.setStatus(HttpStatus.OK.value());
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                SystemResponse systemResponse = new SystemResponse();
                systemResponse.setCode(SystemResponse.FormalErrorCode.LOGIN_DATA_ERR.getValue());
                systemResponse.setMessage("token已失效，请重新登录");
                writer.write(JSON.toJSONString(systemResponse));
            } else {
                chain.doFilter(request, response);
            }

        } catch (Exception e) {
            log.info("token已失效，请重新登录=======", e.toString());
        }

    }


}
