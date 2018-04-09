package com.qiein.jupiter;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    @Test
    public void testValueOperation(){
        redisTemplate.opsForValue().set("hello","123" );
    }

    @Test
    public void test(){
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);

    }
}
