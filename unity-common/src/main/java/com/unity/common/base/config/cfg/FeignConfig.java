package com.unity.common.base.config.cfg;

import com.unity.common.base.SessionHolder;
import com.unity.common.constants.Headers;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FeignConfig {

    @Bean
    public RequestInterceptor headerInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {

                String token = SessionHolder.getToken();

                if(!StringUtils.isEmpty(token)){
                    //System.out.println("Feign==========token=========="+token);
                    requestTemplate.header(Headers.AUTH_TOKEN,token);
                }

//                Enumeration<String> headerNames = SessionHolder.getRequest().getHeaderNames();
//                if (headerNames != null) {
//                    while (headerNames.hasMoreElements()) {
//                        String name = headerNames.nextElement();
//                        String value = SessionHolder.getRequest().getHeader(name);
//                        System.out.println("Feign=========="+ name+"========"+value);
//                        /**
//                         * 遍历请求头里面的属性字段，将logId和token添加到新的请求头中转发到下游服务
//                         * */
//                        if ("uniqueId".equalsIgnoreCase(name) || "token".equalsIgnoreCase(name)) {
//                            //logger.debug("添加自定义请求头key:" + name + ",value:" + value);
//                            requestTemplate.header(name, value);
//                        } else {
//                           // logger.debug("FeignHeadConfiguration", "非自定义请求头key:" + name + ",value:" + value + "不需要添加!");
//                        }
//                    }
//                } else {
//
//                }

//                if (requestTemplate.headers().get("form") != null) {
//                    if (requestTemplate.headers().get("form").iterator().next().equals("1")) {
//
//                        Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
//                        String parms = new String(requestTemplate.body());
//                        System.out.println("===========parms=====================" + parms);
//                        Map<String, Object> pa = JSONObject.parseObject(parms);
//
//                        StringBuilder ab = new StringBuilder();
//                        pa.forEach((k, v) -> {
//                            loadParms(ab, k, v);
//                            System.out.println("===========ab=============================" + ab);
//                            //System.out.println("===========pa========" + k + "=====================" + v);
//                        });
//
//                        requestTemplate.header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//                        requestTemplate.body(ab.toString().substring(1));
//
//                    }
//                }
            }
        };
    }

    void loadParms(StringBuilder sb, String Prefix, Object obj) {
        if (obj!=null) {
            if (obj instanceof List) {
                List<Object> ll = (List<Object>) obj;
                for (int i = 0; i < ll.size(); i++) {
                    loadParms(sb, Prefix + "[" + i + "]", ll.get(i));
                }
            } else if (obj instanceof Map) {
                Map<String, Object> mm = (Map<String, Object>) obj;
                mm.forEach((k, v) -> {
                    loadParms(sb, Prefix + "." + k, v);
                });
            } else {
                try {
                    sb.append("&" + Prefix + "=" + URLEncoder.encode(obj.toString(), "utf-8"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // @Bean
    // public Logger.Level level() {
    // return Logger.Level.FULL;
    // }

}