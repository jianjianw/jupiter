package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.vo.LazyWorkVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16 0016.
 */
@Service
public interface LazyWorkService {
    List<LazyWorkVO> getLazyWorkListByStaffId(int staffId,int companyId);
}
