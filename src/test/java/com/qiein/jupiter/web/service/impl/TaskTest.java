package com.qiein.jupiter.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qiein.jupiter.web.service.task.ClientPushTask;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {

	@Autowired
	private ClientPushTask task;

	@Test
	public void taskPush() {
		task.taskPushLp();
	}
}