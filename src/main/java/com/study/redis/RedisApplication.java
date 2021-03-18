package com.study.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Bean
    public Redisson redisson() {
        // 此为单机模式
        Config config = new Config();
//        config.useSingleServer().setAddress("redis://192.168.0.60:6379").setDatabase(0);
        config.useClusterServers()
                .addNodeAddress("redis://192.168.35.101:6379").setPassword("123456")
                .addNodeAddress("redis://192.168.35.102:6379").setPassword("123456")
                .addNodeAddress("redis://192.168.35.103:6379").setPassword("123456")
                .addNodeAddress("redis://192.168.35.101:6380").setPassword("123456")
                .addNodeAddress("redis://192.168.35.102:6380").setPassword("123456")
                .addNodeAddress("redis://192.168.35.103:6380").setPassword("123456");
        return (Redisson) Redisson.create(config);
    }
}
