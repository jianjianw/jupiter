package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ImageDao;
import com.qiein.jupiter.web.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Tt(叶华葳)
 * on 2018/4/21 0021.
 */
@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private ImageDao imageDao;

    /**
     * 根据图片类型后去图片信息
     * @param type
     * @return
     */
    @Override
    public List<Map<String,Object>> getSrcImgList(String type) {
        return imageDao.getSrcImgList(type);
    }
}
