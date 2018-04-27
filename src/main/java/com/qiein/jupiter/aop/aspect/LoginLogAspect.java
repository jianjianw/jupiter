package com.qiein.jupiter.aop.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 系统日志切面
 */
@Aspect
@Component
public class LoginLogAspect {

	@Pointcut("@annotation(com.qiein.jupiter.aop.aspect.annotation.LoginLog)")
	public void loginLogPointCut() {

	}

	@Around("loginLogPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保存日志
		saveLog(point, time);
		return result;
	}

	private void saveLog(ProceedingJoinPoint point, long time) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		// 请求的方法名
		String className = point.getTarget().getClass().getName();
		String methodName = signature.getName();
		// 请求的参数
		Object[] args = point.getArgs();
	}
}
