package com.qiein.jupiter.web.service;


import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/21 0021.
 */
public interface ImageService {
    /**
     * 根据图片类型获取图片信息
     *
     * @param type
     * @return
     */
    List<Map<String, Object>> getSrcImgList(String type);
}
