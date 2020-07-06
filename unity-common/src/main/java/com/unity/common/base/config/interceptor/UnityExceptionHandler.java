package com.unity.common.base.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.unity.common.base.SessionHolder;
import com.unity.common.exception.FeignError;
import com.unity.common.exception.UnityLoginException;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.SystemResponse;
import com.unity.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Slf4j
@ControllerAdvice
public class UnityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e){
        log.error("Exception",e);
        return handleException(UnityRuntimeException.newInstance().message("系统正忙,请稍后在试......").build());
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleException(RuntimeException  e){
        log.error("RuntimeException",e);
        return handleException(UnityRuntimeException.newInstance().message("系统正忙,请稍后在试......").build());
    }



    @ExceptionHandler(UnityRuntimeException.class)
    public Object handleException(UnityRuntimeException e){
        //log.error("UnityRuntimeException",e);
        switch (getViewFlag()){
            case VIEW:{
                ModelAndView view = new ModelAndView();
                view.addObject("message",e.getMessage());
                view.addObject("code",e.getCode().getValue());
                view.setViewName("comm/errPage");
                log.info("====== UnityRuntimeException === view === {}",e.getMessage());
                return view;
            }
            case JSON:{
                log.info("====== UnityRuntimeException === json === {}",e.getMessage());
                MappingJackson2JsonView view = new MappingJackson2JsonView();
                view.setAttributesMap(JsonUtil.ObjectToMap(
                        new SystemResponse<>().error(e.getCode(), e.getMessage())
                ));
                return view;
            }
            case FEIGN:{
                log.info("====== UnityRuntimeException === feign === {}",e.getMessage());
                String msg = JSON.toJSONString(FeignError.newInstance()
                    .code(e.getCode().getValue())
                    .message(e.getMessage()).build());
                e.setMessage(msg);
                return e;
            }
            default:
                return null;
        }

    }


    @ExceptionHandler(UnityLoginException.class)
    public Object handleException(UnityLoginException e){
        //log.error("UnityRuntimeException",e);
        switch (getViewFlag()){
            case VIEW:{
                ModelAndView view = new ModelAndView();
//                view.addObject("message",e.getMessage());
//                view.addObject("code",e.getCode().getValue());
//                view.setViewName("login");
                view.setViewName("comm/autoLoginPage");
                log.info("====== UnityLoginException === view === {}",e.getMessage());
                return view;
            }
            case JSON:{
                log.info("===== UnityLoginException === json === {}",e.getMessage());
                MappingJackson2JsonView view = new MappingJackson2JsonView();
                view.setAttributesMap(JsonUtil.ObjectToMap(
                        new SystemResponse<>().error(e.getCode(), e.getMessage())
                ));
                return view;
            }
            case FEIGN:{
                log.info("====== UnityLoginException === feign === {}",e.getMessage());
                String msg = JSON.toJSONString(FeignError.newInstance()
                        .code(e.getCode().getValue())
                        .message(e.getMessage()).build());
                e.setMessage(msg);
                return e;
            }
            default:
                return null;
        }

    }

    enum  Flag{
        VIEW,FEIGN,JSON
    }

    private Flag getViewFlag(){
        String url = SessionHolder.getRequest().getRequestURL().toString();
        if(url.indexOf("/moduleEntrance/")>-1 || url.indexOf("/editEntrance/")>-1|| url.indexOf("/view/")>-1|| url.indexOf("/home/")>-1) {
            return Flag.VIEW;
        }
        else if(url.indexOf("/feign/")>-1) {
            return Flag.FEIGN;
        }
        else{
            return Flag.JSON;
        }
    }
}
