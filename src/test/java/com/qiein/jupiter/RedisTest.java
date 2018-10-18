package com.qiein.jupiter;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.entity.po.StaffPO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void testValueOperation() {
        StaffPO staffPO = new StaffPO();
        staffPO.setCompanyId(1);
        staffPO.setPhone("12345678");
        redisTemplate.opsForValue().set("hello", staffPO);
    }

    @Test
    public void test() {
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);

    }

    @Test
    public void test123() {
//		Map<String, Object> map = new HashMap<>();
//		map.put("123", 1);
//		map.put("321", 2);
//		redisTemplate.opsForHash().putAll("test",map );
        Long test = redisTemplate.opsForHash().increment("test", "123", -1);
        System.out.println(test);
    }


    @Test
    public void testWheel() {
        ListOperations<String, Object> stringObjectListOperations = redisTemplate.opsForList();
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        stringObjectListOperations.leftPushAll("list3", list);

        Map<Integer, Object> map = new HashMap<>();
        map.put(1, 1);
        map.put(2, 1);
        map.put(3, 1);
        redisTemplate.opsForHash().putAll("map3", map);
    }

    @Test
    public void testwhe1() {
        ListOperations<String, Object> stringObjectListOperations = redisTemplate.opsForList();
        Object list1 = stringObjectListOperations.rightPop("list3");
        if (list1 == null) {
            //重新获取新的 list
        }
        Long map3 = redisTemplate.opsForHash().increment("map3", list1, -1);
        if (map3 > 0) {
            redisTemplate.opsForList().leftPush("list3", list1);
        }
        System.out.println(list1);
    }
}
