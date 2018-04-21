package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公共图片Dao
 * Created by Administrator on 2018/4/21 0021.
 */
public interface ImageDao {
    List<Map<String,Object>> getSrcImgList(@Param("type") String type);
}
