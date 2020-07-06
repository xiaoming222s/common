//package com.unity.springboot.support.configuration;
//
//import com.unity.springboot.support.annotation.Anonymous;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
//import org.springframework.security.web.access.channel.ChannelProcessingFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import javax.annotation.Resource;
//import javax.servlet.Filter;
//import java.util.Set;
//
///**
// * Spring security的配置
// * <p>
// * create by Jung at 2018-02-20 14:16:33
// */
//@Slf4j
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    @Resource
//    private UrlSecurityConfiguration urlSecurityConfiguration;
//    @Resource
//    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;
//    @Resource
//    private Environment env;
//    @Resource
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;
//
//    @Bean
//    public Filter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.addExposedHeader("x-auth-token");
//        config.addExposedHeader("x-total-count");
//        source.registerCorsConfiguration("", config);
//        return new CorsFilter(source);
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //解决静态资源被拦截的问题
//        web.ignoring().antMatchers("/lib/**","/lib/*","/resources/**","/static/**","*.woff2");
//
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        log.info("======《SecurityConfiguration》======初始化配置=====");
//        String contextPath = env.getProperty("server.servlet.context-path");
//        if (StringUtils.isEmpty(contextPath)) {
//            contextPath = "";
//        }
//
//        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry =
//                http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
//                        .authorizeRequests()
//                        .and().exceptionHandling()
//                        .and().headers().frameOptions().disable()
//                        .and().csrf().disable()
//                        .anonymous()
//                        .and().authorizeRequests()
//                        .antMatchers("/**").permitAll()
//                        .antMatchers(HttpMethod.OPTIONS).permitAll();
//
//        expressionInterceptUrlRegistry
//                .and().httpBasic();
//
//        requestMappingHandlerMapping
//                .getHandlerMethods()
//                .entrySet()
//                .stream()
////                .filter((entry) -> AnnotationUtils.findAnnotation(entry.getValue().getBeanType(), FeignClient.class) == null)
//                .forEach(entry -> {
//                    Set<String> patterns = entry.getKey()
//                            .getPatternsCondition()
//                            .getPatterns();
//                    if( AnnotationUtils.findAnnotation(entry.getValue().getMethod(),Anonymous.class) == null){
//                        patterns.forEach(pattern ->
//                                expressionInterceptUrlRegistry.antMatchers(pattern.replaceAll("\\{.+}", "*")).hasAuthority(pattern.replaceAll("\\{.+}", "*")));
//                    }else{
//                        //Set anonymous if contains Anonymous ANNOTATION
//                        patterns.forEach(pattern ->
//                                expressionInterceptUrlRegistry
//                                        .antMatchers(pattern.replaceAll("\\{.+}", "*"))
//                                        .permitAll());
//                    }
//
//
//
//                });
//
//        switch (urlSecurityConfiguration.getModel()) {
//            case permit:
//                if (urlSecurityConfiguration.getPermit() != null && urlSecurityConfiguration.getPermit().length > 0) {
//                    expressionInterceptUrlRegistry
//                            .antMatchers(urlSecurityConfiguration.getPermit()).permitAll();
//                }
//                expressionInterceptUrlRegistry.anyRequest().authenticated();
//
//                break;
//            case authenticated:
//                expressionInterceptUrlRegistry
//                        .anyRequest().permitAll();
//                if (urlSecurityConfiguration.getAuthenticated() != null && urlSecurityConfiguration.getAuthenticated().length > 0) {
//                    expressionInterceptUrlRegistry.antMatchers(urlSecurityConfiguration.getAuthenticated()).authenticated();
//                }
//
//                break;
//            default:
//                throw new IllegalArgumentException("Please configure new auth model!");
//        }
//
//        expressionInterceptUrlRegistry
//                .antMatchers("/**" + contextPath + "/**").authenticated()
//                .and().exceptionHandling()
//                .and().addFilterBefore(usernamePasswordAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
//        ;
//    }
//}
