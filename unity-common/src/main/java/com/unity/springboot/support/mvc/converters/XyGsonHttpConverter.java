package com.unity.springboot.support.mvc.converters;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import com.unity.common.util.GsonUtils;
import com.unity.common.util.XyStrings;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 项目专用GSON消息处理器.
 */
public class XyGsonHttpConverter extends GsonHttpMessageConverter {
    private static final Logger LOG = LoggerFactory.getLogger(XyGsonHttpConverter.class);

    public XyGsonHttpConverter() {
        super();
        setGson(GsonUtils.inst());
    }


    @Override
    @NonNull
    protected Object readInternal(@NonNull Type resolvedType, Reader reader) throws Exception {
        TypeToken<?> token = TypeToken.get(resolvedType);
        return readTypeToken(token, reader);
    }

    private Object readTypeToken(TypeToken<?> token, Reader json) throws IOException {
        try {
            if (LOG.isTraceEnabled()) {
                String body = XyStrings.join(IOUtils.readLines(json), StringUtils.EMPTY);
                LOG.trace("[readTypeToken([token, inputMessage])] body -> {}", body);
                return getGson().fromJson(body, token.getType());
            }
            return getGson().fromJson(json, token.getType());
        } catch (JsonParseException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    private Charset getCharset(HttpHeaders headers) {
        if (headers == null || headers.getContentType() == null || headers.getContentType().getCharset() == null) {
            return DEFAULT_CHARSET;
        }
        return headers.getContentType().getCharset();
    }

}