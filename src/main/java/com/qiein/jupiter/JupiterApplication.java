package com.qiein.jupiter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//扫描DAO包
@MapperScan("com.qiein.jupiter.web.dao")
//扫描实体类包
@EntityScan("com.qiein.jupiter.web.entity.*")
//允许异步
@EnableAsync
//允许定时
@EnableScheduling
public class JupiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(JupiterApplication.class, args);
	}
}