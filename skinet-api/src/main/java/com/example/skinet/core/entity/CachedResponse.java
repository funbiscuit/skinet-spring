package com.example.skinet.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

@RedisHash(timeToLive = 600)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedResponse {

    private String id;

    private int status;

    private Map<String, String> headers;

    private String body;
}
