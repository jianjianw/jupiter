package com.qiein.jupiter;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}
