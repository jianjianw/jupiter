package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.vo.LazyWorkVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16 0016.
 */
@Service
public interface LazyWorkService {
    /**
     * 根据员工id获取怠工日志列表
     * @param staffId
     * @param companyId
     * @return
     */
    List<LazyWorkVO> getLazyWorkListByStaffId(int staffId,int companyId);


    PageInfo<LazyWorkVO> getLazyWorkList(LazyWorkVO lazyWorkVO);
}
