package com.qiein.jupiter.web.service;


import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by Tt(叶华葳)
 * on 2018/4/21 0021.
 */
public interface ImageService {
    /**
     * 根据图片类型获取图片信息
     *
     * @param type
     * @return
     */
    List<Map<String, Object>> getSrcImgList(String type);

    /**
     * 根据类型和公司  远程获取图片集合
     */
    JSONArray getSrcImgListRPC(String type, int companyId);
}
