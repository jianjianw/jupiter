package com.qiein.jupiter.util;

/**
 * 通用工具类
 * 
 * @author JingChenglong 2017/12/18 11:34
 *
 */
public class CommonUtils {

	public static String getTraceInfo() {

		StringBuffer sb = new StringBuffer();

		StackTraceElement stackTraceElement = new java.lang.Throwable().getStackTrace()[1];
		sb.append("(");
		sb.append("className:").append(stackTraceElement.getClassName());
		sb.append(";fieldName:").append(stackTraceElement.getFileName());
		sb.append(";methodName:").append(stackTraceElement.getMethodName());
		sb.append(";lineNumber:").append(stackTraceElement.getLineNumber());
		sb.append(")");
		return sb.toString();
	}
}