package com.example.skinet.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.io.Reader;

@RequiredArgsConstructor
@Service
public class ResourceReader {
    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public String readString(String resourcePath) {
        Resource resource = resourceLoader.getResource(resourcePath);

        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
