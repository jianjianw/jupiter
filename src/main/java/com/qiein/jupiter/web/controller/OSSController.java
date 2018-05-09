package com.qiein.jupiter.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.OSSUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;

/**
 * 阿里OSSController Created by Administrator on 2018/4/26 0026.
 */
@RestController
@RequestMapping("/oss")
public class OSSController extends BaseController {

	/**
	 * 获取Web直传Policy
	 *
	 * @return
	 */
	@GetMapping("/get_policy")
	public ResultInfo getPolicyAndCallback() {
		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, OSSUtil.getPolicy(300));
	}

	/**
	 * 根据图片路径删除
	 *
	 * @param imgUrl
	 * @return
	 */
	@GetMapping("/delete")
	public ResultInfo deleteImg(String imgUrl) {
		OSSUtil.deleteObject(imgUrl);
		System.out.println(1);
		return ResultInfoUtil.success(TigMsgEnum.SUCCESS);
	}

	/**
	 * 上传网络图片
	 *
	 * @param imgUrl
	 *            图片地址
	 * @return
	 */
	@GetMapping("/upload_net_img")
	public ResultInfo uploadNetImg(String imgUrl) {
		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, OSSUtil.uploadWebImage(imgUrl, null));
	}

	/**
	 * 上传图片
	 *
	 * @return
	 */
	@PostMapping("/upload_img")
	public ResultInfo uploadImg(MultipartFile file) {
		String path = null;
		System.out.println(1);
		// 图片上传
		if (file != null) {
			// 检查文件类型是否为图片
			// 正则：^正则表达式开始，.
			// 匹配任何单个字符；?0到1个字符串匹配多个字符，*0到多个子串匹配，()分组，|或者，$正则表达式结束
			if (!file.getOriginalFilename().matches(".+(.gif|.jpg|.jpeg|.png|.GIF|.JPG|.PNG)$")) {
				return ResultInfoUtil.error(ExceptionEnum.OSS_UPLOAD_TYPE_ERROR);
			}
			try {
				long imgSize = file.getSize();

				// 限定图片大小
				if (imgSize > 3 * 1024 * 1024) {
					return ResultInfoUtil.error(ExceptionEnum.OSS_UPLOAD_SIZE_ERROR);
				}
				// // 图片MD5摘要值
				// String md5 = MD5Util.md5file(file.getInputStream());
				//
				// String fileName = md5
				// +
				// file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				// 上传至OSS库
				// OSSUtil.putOssObject(fileName, file.getInputStream());
				path = OSSUtil.upload(file.getInputStream(), null,
						file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
			} catch (Exception e) {
				e.printStackTrace();
				ResultInfoUtil.error(ExceptionEnum.OSS_UPLOAD_FAIL);
			}

		} else {
			ResultInfoUtil.error(ExceptionEnum.OSS_NO_FILE);
		}

		return ResultInfoUtil.success(TigMsgEnum.SUCCESS, path);
	}

}
