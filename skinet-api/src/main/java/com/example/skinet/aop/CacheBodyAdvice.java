package com.example.skinet.aop;

import com.example.skinet.service.CachedResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class CacheBodyAdvice implements ResponseBodyAdvice<Object> {

    private final CachedResponseService cachedResponseService;

    @Override
    public boolean supports(MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        return method != null && cachedResponseService.isCacheEnabled(method);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HttpMessageConverter httpMessageConverter = selectedConverterType
                    .getDeclaredConstructor().newInstance();
            httpMessageConverter.write(body, selectedContentType, new HttpOutputMessage() {
                @Override
                @NonNull
                public OutputStream getBody() {
                    return outputStream;
                }

                @Override
                @NonNull
                public HttpHeaders getHeaders() {
                    return httpHeaders;
                }
            });

            cachedResponseService.cacheResponse(returnType.getMethod(),
                    ((ServletServerHttpRequest) request).getServletRequest(),
                    ((ServletServerHttpResponse) response).getServletResponse(),
                    outputStream.toString(), httpHeaders);

        } catch (Exception e) {
            log.warn("Error occurred during caching response:");
            e.printStackTrace();
        }

        return body;
    }

}
