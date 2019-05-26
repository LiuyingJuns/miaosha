package com.miaosha.redis;

import com.miaosha.redis.impl.RedisServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.miaosha"})
@RestController
public class SpringRedisApplication {

    public static void main(String[] args) {
        RedisService redisService = new RedisServiceImpl();
        redisService.set("1","hehe");
        SpringApplication.run(SpringRedisApplication.class, args);

    }
}
