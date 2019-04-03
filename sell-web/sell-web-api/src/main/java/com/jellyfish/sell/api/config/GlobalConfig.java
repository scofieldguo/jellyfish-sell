package com.jellyfish.sell.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class GlobalConfig {

    @Value("${aes.key}")
    private String key;
    @Value("${aes.iv}")
    private String iv;
}