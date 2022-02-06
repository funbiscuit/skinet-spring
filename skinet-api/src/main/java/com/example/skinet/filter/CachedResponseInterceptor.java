package com.example.skinet.filter;

import com.example.skinet.core.entity.CachedResponse;
import com.example.skinet.service.CachedResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class CachedResponseInterceptor implements HandlerInterceptor {
    private final CachedResponseService cachedResponseService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (!cachedResponseService.isCacheEnabled(((HandlerMethod) handler).getMethod())) {
            return true;
        }

        Optional<CachedResponse> cachedResponse = cachedResponseService.getCachedResponse(request);
        if (cachedResponse.isEmpty()) {
            return true;
        }
        log.info("Using cached response for request: {}", request.getRequestURI());

        CachedResponse r = cachedResponse.get();

        response.setStatus(r.getStatus());
        r.getHeaders().forEach(response::setHeader);
        response.getOutputStream().write(r.getBody().getBytes());

        return false;
    }
}
