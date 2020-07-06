package com.unity.common.base.controller;

import com.unity.common.pojos.SystemResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * 控制器基类
 * <p>
 * create by Jung at 2018年02月09日18:23:12
 */
@Slf4j
public class BaseAppController {

    /**
     * 日志对象
     */
    protected static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 200的返回值
     *
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected <T> SystemResponse<T> success() {
        return success(null);
    }

    /**
     * 200的返回值
     *
     * @param obj 需要返回的对象
     * @return 返回的对象
     * @author Jung
     * @since 2018年02月09日18:24:08
     */
    protected <T> SystemResponse<T> success(T obj) {
        return new SystemResponse<T>().success(obj);
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
    protected SystemResponse<Object> error(SystemResponse.FormalErrorCode errorCode, String message) {
        return new SystemResponse<>().error(errorCode, message);
    }
}
