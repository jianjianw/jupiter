package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.vo.*;

import java.util.List;
import java.util.Map;

public interface ScreenService {
   
    
	/**
     * 电商推广月度ROI--HJF
     * @param reportParamDTO
     */
	List<ScreenVO> getDayKZTotal();

	List<ScreenVO> getMonthKZTotal();

	List<ScreenVO> getDdNum();

	List<ScreenVO> getWXFlag();

	List<ScreenVO> getWXKzNum();

	List<ScreenVO> getWXKzComeNum();

	List<ScreenVO> getWXGroupKzNumMonth();

	List<ScreenVO> WXGroupKzComeNumWeek();

	List<ScreenVO> WXGroupKzComeNumMonth();
}
