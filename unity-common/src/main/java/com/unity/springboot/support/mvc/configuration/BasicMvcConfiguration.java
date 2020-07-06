package com.unity.springboot.support.mvc.configuration;

///**
// * 基础的Mvc配置类.
// * Created by Jung on 06/06/2017.
// */
//@Slf4j
//public abstract class BasicMvcConfiguration implements WebMvcConfigurer, EnvironmentAware {
//    private static Map<String, Object> DISPLAY_ERROR;
//    private static final Gson gson = new Gson();
//
//    static {
//        DISPLAY_ERROR = new HashMap<>();
//        DISPLAY_ERROR.put("code", 500);
//        DISPLAY_ERROR.put("message", "服务器开了个小差");
//    }
//
//    private Environment env;
//
//    public BasicMvcConfiguration(Environment env) {
//        this.env = env;
//    }
//
//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
//        exceptionResolvers.add((request, response, handler, ex) -> {
//            int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
//
//            String msg = gson.toJson(DISPLAY_ERROR);
//
//            if (ex instanceof ResourceAccessException) ex = (Exception) ex.getCause();
//
//            if (ex instanceof IllegalArgumentException) {
//                msg = ex.getMessage();
//            } else if (ex instanceof IllegalAccessException) {
//                msg = ex.getMessage();
//            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
//            } else if (ex instanceof RestClientException) {
//                msg = ex.getMessage();
//                msg = msg.startsWith("\"") ? msg.substring(1, msg.length() - 1) : msg;   //移除双引号
//            } else if (ex instanceof IllegalStateException) {
//                msg = ex.getMessage();
//            }
//
//            //Spring权限不足, 不打印日志
//            if (ex instanceof AccessDeniedException) {
//                status = HttpStatus.FORBIDDEN.value();
//                msg = "{\"code\":403,\"message\":\"您的权限不足\"}";
//            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
//                status = HttpStatus.METHOD_NOT_ALLOWED.value();
//                msg = "{\"code\":400,\"message\":\"您的请求方式错误，当前支持的请求方式为:" + ((HttpRequestMethodNotSupportedException) ex).getSupportedHttpMethods() + "\"}";
//            } else {
//                if (log.isInfoEnabled()) {
//                    log.info("[resolveException([request, response, handler, ex])] Illegal call from ip -> {}, headers -> {}, url -> {},query -> {}",
//                            XyHttps.getClientIp(request),
//                            gson.toJson(XyHttps.getHeaderMap(request)),
//                            request.getRequestURI(),
//                            request.getQueryString());
//                }
//                Exception finalEx = ex;
//                new Thread(() -> {
//                    try {
//                        String title = "新元认证系统异常提醒";
//                        MailUtil mailUtil = SpringUtils.getBean(MailUtil.class);
//                        //发送邮件
//                        mailUtil.sendMail2User("zhangwei@jingcaiwang.cn", title, "新元认证系统遇到异常，请及时处理：\n" + ExceptionUtils.getFullStackTrace(finalEx));
//                        mailUtil.sendMail2User("jialichao@jingcaiwang.cn", title, "新元认证系统遇到异常，请及时处理：\n" + ExceptionUtils.getFullStackTrace(finalEx));
//                    } catch (Exception e) {
//                        log.error("发送邮件过程中产生了异常");
//                    }
//                }).start();
//
//            }
//
//
//            try {
//                response.setStatus(status);
//                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//                response.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
//                response.getOutputStream().flush();
//            } catch (IOException e) {
//                //ignore
//            }
//            return null;
//        });
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new XyGsonHttpConverter());
//        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        env = environment;
//    }
//
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST")
//                .allowCredentials(false).maxAge(3600);
//    }
//}
