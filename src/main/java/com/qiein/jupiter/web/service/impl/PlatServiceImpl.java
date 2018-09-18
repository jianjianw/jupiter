package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.entity.vo.SearchClientVO;
import com.qiein.jupiter.web.repository.WebClientInfoSearchDao;
import com.qiein.jupiter.web.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 平台接口
 *
 * @Author: shiTao
 */
@Service
public class PlatServiceImpl implements PlatService {

    @Autowired
    private WebClientInfoSearchDao webClientInfoSearchDao;

    @Override
    public List<SearchClientVO> pageSearch(int companyId, String key) {
        return webClientInfoSearchDao.search(companyId, key);
    }



}
