package com.unity.common.base.controller;



import com.unity.common.base.BaseEntity;
import com.unity.common.constants.ConstString;
import com.unity.common.pojos.SystemResponse;
import com.unity.common.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

/**
 * 控制器基类
 * <p>
 * create by Jung at 2018年02月09日18:23:12
 */
public class BaseWebController {

    protected void adapterField(Map<String, Object> m, BaseEntity entity){
        if(!org.apache.commons.lang3.StringUtils.isEmpty(entity.getCreator())) {
            if(entity.getCreator().indexOf(ConstString.SEPARATOR_POINT)>-1)
                m.put("creator", entity.getCreator().split(ConstString.SPLIT_POINT)[1]);
            else
                m.put("creator", entity.getCreator());
        }
        if(!org.apache.commons.lang3.StringUtils.isEmpty(entity.getEditor())) {
            if(entity.getEditor().indexOf(ConstString.SEPARATOR_POINT)>-1)
                m.put("editor", entity.getEditor().split(ConstString.SPLIT_POINT)[1]);
            else
                m.put("editor", entity.getEditor());
        }

        m.put("gmtCreate", DateUtils.timeStamp2Date(entity.getGmtCreate()));
        m.put("gmtModified", DateUtils.timeStamp2Date(entity.getGmtModified()));

    }

    //    @ExceptionHandler({ Exception.class })
//    @ResponseBody
//    public <T> ResponseEntity<SystemResponse<T>> exception(HttpServletRequest request,
//                                                HttpServletResponse response, Exception e) {
//        logger.error("error intercept___________:", e);
//        // 需要过滤相关异常，做精准返回，比如传入数据的格式问题
//        return exceptionResult(e);
//    }
//
//    public static <T> ResponseEntity<SystemResponse<T>> exceptionResult(Exception e) {
//        if (e instanceof UnityRuntimeException) {
//            UnityRuntimeException ex = (UnityRuntimeException)e;
//            return new ResponseEntity(new SystemResponse<>()
//                    .error(ex.getCode(), ex.getMessage()), HttpStatus.OK);
//        } else if (e instanceof MissingServletRequestParameterException) {
//            return new ResponseEntity(new SystemResponse<>()
//                    .error(SystemResponse.FormalErrorCode.LACK_REQUIRED_PARAM, "缺少参数！"), HttpStatus.OK);
//        } else if (e instanceof HttpMessageNotReadableException) {
//            return new ResponseEntity(new SystemResponse<>()
//                    .error(SystemResponse.FormalErrorCode.LACK_REQUIRED_PARAM, "缺少参数！"), HttpStatus.OK);
//        } else {
//            return new ResponseEntity(new SystemResponse<>()
//                    .error(SystemResponse.FormalErrorCode.SERVER_ERROR, "系统正忙,请稍后在试......"), HttpStatus.OK);
//        }
//    }

    /**
     * 日志对象
     */
    protected static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected void login() {
    }


    /**
     * 200的返回值
     *
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected Mono<ResponseEntity<SystemResponse<Object>>> success() {
        return success(null);
    }

    /**
     * 200的返回值
     *
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected Mono<ResponseEntity<SystemResponse<Object>>> success(String message) {
        return success(message, null);
    }

    /**
     * 200的返回值
     *
     * @param obj 需要返回的对象
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected <T> Mono<ResponseEntity<SystemResponse<T>>> success(T obj) {
        return success(null, obj);
    }

    /**
     * 200的返回值
     *
     * @param obj 需要返回的对象
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected <T> Mono<ResponseEntity<SystemResponse<T>>> success(String message, T obj) {
        return Mono.just(new ResponseEntity(new SystemResponse<T>().formalSuccess(StringUtils.isEmpty(message) ? "success" : message, obj), HttpStatus.OK));
    }

    /**
     * 错误的返回值
     *
     * @param errorCode 需要返回的错误对象
     * @param message   需要返回的错误值
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected Mono<ResponseEntity<SystemResponse<Object>>> error(SystemResponse.FormalErrorCode errorCode, String message) {
        return Mono.just(new ResponseEntity<>(new SystemResponse<>().error(errorCode, message), HttpStatus.OK));
    }


//    public class BaseLayUIWebController extends BaseWebController {
//        /**
//         * LAYUI-200的返回值
//         *
//         * @param data 需要返回的对象
//         * @param cnt  需要返回的总条数
//         * @return 返回的对象
//         * @author Jung
//         * @since 2018年07月28日11:47:08
//         */
//        public <T> Mono<ResponseEntity<LayUIResponse<T>>> success(List<T> data, int cnt) {
//            return Mono.just(new ResponseEntity<>(new LayUIResponse<T>().success(data, cnt), HttpStatus.OK));
//        }
//
//        /**
//         * LAYUI-200的返回值
//         *
//         * @param response 需要返回的对象
//         * @return 返回的对象
//         * @author Jung
//         * @since 2018年07月28日11:47:11
//         */
//        public <T> Mono<ResponseEntity<LayUIResponse<T>>> success(LayUIResponse<T> response) {
//            return Mono.just(new ResponseEntity<>(response, HttpStatus.OK));
//        }
//    }

}
