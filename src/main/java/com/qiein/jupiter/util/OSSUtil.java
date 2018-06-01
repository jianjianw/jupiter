package com.qiein.jupiter.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里OSS对象存储工具类，饿汉式单例模式
 * Created by Tt(叶华葳)
 * on 2018/4/28 0028.
 */
public class OSSUtil {
	// 参考https://blog.csdn.net/wus_shang/article/details/79286756
	// https://blog.csdn.net/github_36086968/article/details/53113274
	// =====================================永久固定的OSS常量
	/**
	 * END_POINT是访问OSS的域名
	 */
	private static final String END_POINT = "oss-cn-hangzhou.aliyuncs.com";
	/**
	 * 访问密钥的id
	 */
	private static final String ACCESS_KEY_ID = "LTAIH4J10mrJgons";
	/**
	 * 访问密钥
	 */
	private static final String ACCESS_KEY_SECRET = "9wSrBw0DKHHY0YKBA1FEGkch6rNEVb";

	// ========================================存储文件相对的OSS地址
	/**
	 * 存储空间名称
	 */
	private static final String BUCKET_NAME = "crm-jupiter";
	/**
	 * 上传到存储空间的那个目录下
	 */
	private static String imgDir = "image/";

	// ==========================================会变的参数

	/**
	 * 请求地址
	 */
	private static String host = "http://" + BUCKET_NAME + "." + END_POINT;

	/**
	 * 缓冲时间是三十秒,即还有三十秒要过期授权就直接废弃重新申请
	 */
	private static long expireTime = 60 * 1000;

	/**
	 * 临时授权过期时间
	 */
	private static long expireEndTime;

	// ==================================================私有对象
	/**
	 * 阿里云OSS云存储对象
	 */
	@SuppressWarnings("deprecation")
	private static OSSClient client = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

	/**
	 * OSS工具类对象
	 */
	private static OSSUtil ossUtil = new OSSUtil();

	/**
	 * 时间格式类
	 */
	private static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy/MM/dd/");
	private static final SimpleDateFormat SDF_yyMMddHHmmss_ = new SimpleDateFormat("yyMMddHHmmss_");

	/**
	 * 私有构造函数，无法直接new，只能通过getInstance方法获取唯一实例
	 */
	private OSSUtil() {

	}

	/**
	 * 获取单例
	 *
	 * @return
	 */
	public static OSSUtil getInstance() {
		return ossUtil;
	}

	// =============================================================前端web直传阿里OSS对象存储

	/**
	 * 获取上传策略Policy，默认授权有效时间30s,默认获取上传图片策略
	 *
	 * @return
	 */
	public static JSONObject getPolicy() {
		return getPolicy(null, 30);
	}

	/**
	 * 获取上传策略Policy,默认授权有效时间30s
	 *
	 * @param seconds
	 * @return
	 */
	public static JSONObject getPolicy(int seconds) {
		return getPolicy(null, seconds);
	}

	/**
	 * 获取Web上传策略Policy
	 *
	 * @param callbackUrl
	 *            回调地址
	 * @param seconds
	 *            授权有效持续时间
	 * @return
	 */
	public static JSONObject getPolicy(String callbackUrl, int seconds) {
		JSONObject reply = new JSONObject();
		// //回调内容 目前并没有用到
		// Map<String, String> callback = new HashMap<>();
		// callback.put("callbackUrl", callbackUrl);
		// callback.put("callbackBody",
		// "filename=${object}&size=${size}&mimeType=${mimeType}&height" +
		// "=${imageInfo.height}&width=${imageInfo.width}");
		// callback.put("callbackBodyType",
		// "application/x-www-form-urlencoded");

		// 过期授权时间
		Date expiration = getExpiration(seconds);
		// oss策略对象
		PolicyConditions policyConds = new PolicyConditions();
		// 设置上传文件的大小限制
		policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
		// 设置此次上传的文件名必须是dir变量的值开头
		policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, imgDir);
		String postPolicy = client.generatePostPolicy(expiration, policyConds);

		// policy
		try {
			String policy = BinaryUtil.toBase64String(postPolicy.getBytes("utf-8"));
//			System.out.println("policy :" + policy);
			// 签名
			String signature = client.calculatePostSignature(postPolicy);
//			System.out.println("signature :" + signature);
			// 回调
			// String callbackData =
			// BinaryUtil.toBase64String(callback.toString().getBytes("utf-8"));

			reply.put("accessid", ACCESS_KEY_ID);
			reply.put("policy", policy);
			reply.put("signature", signature);
			reply.put("dir", imgDir + getDirectory());
			reply.put("host", host);
			reply.put("expire", expiration);
			// reply.put("callback", callbackData);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return reply;
	}

	/**
	 * 获取过期授权时间
	 *
	 * @return
	 */
	static Date getExpiration(int expire) {
		if (expireEndTime == 0 || System.currentTimeMillis() > expireEndTime) { // 如果为0或者当前时间大于授权过期时间
																				// ，则去获取
			if (expire == 0)
				expireEndTime = System.currentTimeMillis() + expireTime;
			else
				expireEndTime = System.currentTimeMillis() + expire;
		}
		Date expiration = new Date(expireEndTime);
		return expiration;
	}

	/**
	 * 根据时间获取目录名
	 *
	 * @return 如：2018/05/02/
	 */
	public static String getDirectory() {
		return SDF_YYYYMMDD.format(Calendar.getInstance().getTime());
	}

	/**
	 * 根据Key或者url获取文件名称
	 * 如key:image/2018/05/03/180503164504_dfd59c47-a91e-4b7e-8697-59dab618db16.
	 * png
	 *
	 * @return
	 */
	public static String getObjectName(String keyOrUrl) {
		int endIndex = keyOrUrl.lastIndexOf("?");
		String temp = endIndex == -1 ? keyOrUrl : keyOrUrl.substring(0, endIndex);
		int beginIndex = temp.lastIndexOf("/") + 1;
		return endIndex == -1 ? temp.substring(beginIndex) : temp.substring(beginIndex, endIndex);
	}

	/**
	 * 根据Key或者url获取文件全名
	 *
	 * @return
	 */
	public static String getObjectLongName(String keyOrUrl) {
		int beginIndex = keyOrUrl.indexOf(imgDir) != -1 ? keyOrUrl.indexOf(imgDir) : 0;
		int endIndex = keyOrUrl.lastIndexOf("?") != -1 ? keyOrUrl.indexOf("?") : keyOrUrl.length();
		return keyOrUrl.substring(beginIndex, endIndex);
	}

	/**
	 * 生成文件名
	 *
	 * @return 如：180503143800_7424be58-4ce3-4ee7-8ea8-768644c7c9de
	 */
	public static String getFileName() {
		return SDF_yyMMddHHmmss_.format(System.currentTimeMillis()) + UUID.randomUUID();
	}

	/**
	 * 是用户对该上传对象object的描述，
	 * 由一系列name-value对组成；其中ContentLength是必须设置的，以便SDK可以正确识别上传Object的大小
	 *
	 * @param length
	 * @return
	 */
	private static ObjectMetadata getObjectMetadata(long length) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(length);
		// 被下载时网页的缓存行为,允许被缓存
		// objectMetadata.setCacheControl("no-cache");
		// objectMetadata.setHeader("Pragma", "no-cache");
		return objectMetadata;
	}
	// ============================================================================上传至服务器然后再处理

	/**
	 * 上传自定义格式
	 *
	 * @param is
	 *            输入流
	 * @param directory
	 *            目录
	 * @param fileType
	 *            文件类型
	 * @return
	 */
	public static String upload(InputStream is, String directory, String fileType) {
		String fileName = fileType + "/" + getFileName() + "." + fileType;
		return uploadFile(is, directory, fileName);
	}

	/**
	 * 上传视频指定mp4格式
	 *
	 * @param is
	 *            输入流
	 * @param directory
	 *            目录名
	 * @return
	 */
	public static String uploadVideo(InputStream is, String directory) {
		String fileName = "mp4/" + getFileName() + ".mp4";
		return uploadFile(is, directory, fileName);
	}

	/**
	 * 上传图片指定png格式
	 *
	 * @param is
	 *            输入流
	 * @param directory
	 *            目录名
	 * @return
	 */
	public static String uploadImage(InputStream is, String directory) {
		String fileName = "png/" + getFileName() + ".png";
		return uploadFile(is, directory, fileName);
	}

	/**
	 * 上传网络图片指定png格式
	 *
	 * @param fileUrl
	 * @param directory
	 * @return
	 */
	public static String uploadWebImage(String fileUrl, String directory) {
		String fileName = "png" + getFileName() + ".png";
		return uploadWebFile(fileUrl, directory, fileName);
	}

	/**
	 * 上传本地文件（文件流上传）
	 *
	 * @param is
	 * @param directory
	 * @param fileName
	 * @return
	 */
	public static String uploadFile(InputStream is, String directory, String fileName) {
		String key = getDirectory() + fileName;
		if (StringUtil.isNotEmpty(directory)) {
			key = directory + "/" + fileName;
		}
		try {
			ObjectMetadata objectMetadata = getObjectMetadata(is.available());
			// 存储空间名，存储的地址加名称，输入流，文件描述
			client.putObject(BUCKET_NAME, key, is, objectMetadata);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return host + "/" + key;
	}

	/**
	 * 上传网络图片
	 *
	 * @param fileUrl
	 * @param directory
	 * @param fileName
	 * @return
	 */
	public static String uploadWebFile(String fileUrl, String directory, String fileName) {
		String key = getDirectory() + fileName;
		if (StringUtil.isNotEmpty(directory))
			key = directory + "/" + fileName;
		InputStream is = null;
		try {
			Integer length = new URL(fileUrl).openConnection().getContentLength();
			is = new URL(fileUrl).openStream();
			ObjectMetadata objectMetadata = getObjectMetadata(length);
			client.putObject(BUCKET_NAME, key, is, objectMetadata);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return host + "/" + key;
	}

	/**
	 * 单个下载文件
	 *
	 * @param key
	 *            文件key值
	 * @param localFileUrl
	 *            目标文件路径名称
	 * @return boolean
	 */
	public static boolean getObject(String key, String localFileUrl) {
		String fileName = getObjectName(key);
		File file = new File(localFileUrl);
		if (!file.exists()) {
			file.mkdirs();
		}
		ObjectMetadata object = client.getObject(new GetObjectRequest(BUCKET_NAME, key),
				new File(localFileUrl + fileName));
		if (object != null) {
			return true;
		}
		return false;
	}

	/**
	 * 根据图片url或者key删除图片
	 *
	 * @param fileUrl
	 * @return
	 */
	public static boolean deleteObject(String fileUrl) {
		// String fileName = fileUrl.substring(fileUrl.indexOf(dir));
		// System.out.println("fileName:"+fileName);
		// System.out.println(getObjectLongName(fileUrl));
		try {
			client.deleteObject(BUCKET_NAME, getObjectLongName(fileUrl));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws FileNotFoundException {

		// String string = "abcdefghijklmn";
		// System.out.println(string.indexOf("ojbk"));
		// string.substring(string.indexOf("ojkb"));

		// System.out.println(deleteObject("image/2018/05/03/180503171525_a16db271-0266-4115-85e4-797bfb77088b.png"));
	}
}
