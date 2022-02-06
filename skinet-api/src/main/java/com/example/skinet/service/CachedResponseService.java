package com.example.skinet.service;

import com.example.skinet.aop.CacheResponse;
import com.example.skinet.core.entity.CachedResponse;
import com.example.skinet.repo.CachedResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CachedResponseService {
    private final CachedResponseRepository cachedResponseRepository;

    public boolean isCacheEnabled(Method method) {
        return method.isAnnotationPresent(CacheResponse.class);
    }

    public void cacheResponse(Method method,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              String body,
                              HttpHeaders headers) {

        int status = response.getStatus();
        if (status != HttpStatus.OK.value()) {
            // for now cache only OK responses
            return;
        }

        String key = createKeyFromRequest(request);

        cachedResponseRepository.save(new CachedResponse(key, status,
                headers.toSingleValueMap(), body));
    }

    public Optional<CachedResponse> getCachedResponse(HttpServletRequest request) {

        String key = createKeyFromRequest(request);
        return cachedResponseRepository.findById(key);
    }

    private String createKeyFromRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURI());

        request.getParameterMap().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    sb.append('|');
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(Arrays.toString(e.getValue()));
                });
//        for (Object arg : args) {
//            sb.append('|');
//            if (arg != null) {
//                sb.append(arg);
//            }
//        }
        return sb.toString();
    }
}
