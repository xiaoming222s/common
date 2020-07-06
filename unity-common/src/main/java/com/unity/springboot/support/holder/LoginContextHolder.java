package com.unity.springboot.support.holder;

import com.unity.common.exception.UnityLoginException;
import com.unity.common.exception.UnityRuntimeException;
import com.unity.common.pojos.AuthUser;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Optional;

/**
 * 登录的占位符
 * <p>
 * create by Jung at 2018年06月16日11:10:44
 */
public class LoginContextHolder {
    private static final ThreadLocal<AuthUser> requestAttributesHolder = new NamedThreadLocal<>("Login User");

    /**
     * Bind the given RequestAttributes to the current thread.
     *
     * @param attributes the RequestAttributes to expose,
     *                   or {@code null} to reset the thread-bound context
     */
    public static void setLoginAttributes(@Nullable AuthUser attributes) {
        requestAttributesHolder.set(attributes);
    }

    /**
     * Return the RequestAttributes currently bound to the thread.
     *
     * @return the RequestAttributes currently bound to the thread,
     * or {@code null} if none bound
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T getRequestAttributes() {
        return (T) Optional.ofNullable((T) requestAttributesHolder.get()).orElseThrow(UnityLoginException::new);
    }

    /**
     * clear the RequestAttributes currently bound to the thread.
     *
     */
    public static void clearRequestAttributes() {
        requestAttributesHolder.remove();
    }
}
