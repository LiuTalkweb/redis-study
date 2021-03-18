package com.study.redis.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: redis
 * @description:
 * @author: liuB
 * @create: 2021-03-18 14:00
 **/
@Slf4j
@RestController
@RequestMapping("/sell")
public class SellController {

    @Autowired
    private Redisson redisson;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/test")
    public String test(@RequestParam("name") String name){
        return "hello:"+name;
    }

    @GetMapping("/deduct_stock")
    public String deductStock() throws InterruptedException {
        String lockKey = "product_001";
        //String clientId = UUID.randomUUID().toString();
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            //Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "zhuge"); //jedis.setnx(key,value)
            //stringRedisTemplate.expire(lockKey,30, TimeUnit.SECONDS);
            /*Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 30, TimeUnit.SECONDS);

            if (!result) {
                return "1001";
            }*/

            // 加锁，实现锁续命功能
            redissonLock.lock();
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock")); // jedis.get("stock")
            if (stock > 0) {
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + ""); // jedis.set(key,value)
                System.out.println("扣减成功，剩余库存:" + realStock + "");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        }finally {
            redissonLock.unlock();
            /*if (clientId.equals(stringRedisTemplate.opsForValue().get(lockKey))){
                    stringRedisTemplate.delete(lockKey);
            }*/
        }
        return "end";
    }
}