package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.OSSUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阿里OSSController
 * Created by Administrator on 2018/4/26 0026.
 */
@RestController
@RequestMapping("/oss")
public class OSSController extends BaseController{

    /**
     * 获取Web直传Policy
     * @return
     */
    @GetMapping("/get_policy")
    public ResultInfo getPolicyAndCallback(){
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS,OSSUtil.getPolicy(60));
    }

    /**
     * 根据图片路径删除
     * @param imgUrl
     * @return
     */
    @GetMapping("/delete")
    public ResultInfo deleteImg(String imgUrl){
        OSSUtil.deleteObject(imgUrl);
        System.out.println(1);
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS);
    }

    /**
     * 上传网络图片
     * @param imgUrl    图片地址
     * @return
     */
    @GetMapping("/upload_net_img")
    public ResultInfo uploadNetImg(String imgUrl){
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS,OSSUtil.uploadWebImage(imgUrl,null));
    }

//    /**
//     * 上传图片
//     * @return
//     */
//    @PostMapping("/upload_img")
//    public ResultInfo uploadImg(MultipartFile file){
//
//
//
//        return ResultInfoUtil.success(TigMsgEnum.SUCCESS);
//    }

}
