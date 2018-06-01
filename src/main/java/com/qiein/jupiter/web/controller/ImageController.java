package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.service.ImageService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tt(叶华葳)
 * on 2018/4/21 0021.
 */
@RestController
@RequestMapping("/image")
public class ImageController extends BaseController {

    @Autowired
    private ImageService imageService;

    /**
     * 根据图片类型获取图片信息
     *
     * @param type
     * @return
     */
    @GetMapping("/get_by_type")
    public ResultInfo getSrcImgList(@NotEmpty String type) {
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS, imageService.getSrcImgList(type));
    }
}
