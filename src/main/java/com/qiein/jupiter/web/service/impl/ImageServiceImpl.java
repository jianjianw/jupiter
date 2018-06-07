package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.web.dao.ImageDao;
import com.qiein.jupiter.web.service.ImageService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tt(叶华葳)
 * on 2018/4/21 0021.
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Value("${apollo.getImgUrl}")
    private String getImgUrl;


    /**
     * 根据图片类型后去图片信息
     *
     * @param type
     * @return
     */
    @Override
    public List<Map<String, Object>> getSrcImgList(String type) {
        return imageDao.getSrcImgList(type);
    }

    @Override
    public JSONArray getSrcImgListRPC(String type, int companyId) {
        JSONObject params = new JSONObject();
        params.put("companyId", companyId);
        params.put("typeCode", type);
        String res = HttpClient.get(getImgUrl)
                .queryString("sign", MD5Util.getApolloMd5(params.toString()))
                .queryString("params", params.toString())
                .queryString("time", new Date().getTime())
                .asString();
        JSONObject json = JSON.parseObject(res);
        if (json.getIntValue("code") == CommonConstant.DEFAULT_SUCCESS_CODE) {
            return json.getJSONArray("data");
        }
        return new JSONArray();
    }

}
