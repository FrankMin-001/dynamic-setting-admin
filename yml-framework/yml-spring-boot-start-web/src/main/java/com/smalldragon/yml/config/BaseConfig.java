package com.smalldragon.yml.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "base-config")
public class BaseConfig {

    private String publicKey;
    public static String PUBLIC_KEY;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @PostConstruct
    public void init() {
        PUBLIC_KEY = this.publicKey;
        System.out.println("Static field PUBLIC_KEY has been initialized: " + PUBLIC_KEY);
    }

}
