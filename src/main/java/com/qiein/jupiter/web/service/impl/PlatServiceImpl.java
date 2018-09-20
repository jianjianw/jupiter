package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.vo.PlatPageVO;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import com.qiein.jupiter.web.entity.vo.SearchClientVO;
import com.qiein.jupiter.web.repository.ClientQueryByIdDao;
import com.qiein.jupiter.web.repository.ClientQueryDao;
import com.qiein.jupiter.web.repository.QueryClientByKeyDao;
import com.qiein.jupiter.web.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台接口
 *
 * @Author: shiTao
 */
@Service
public class PlatServiceImpl implements PlatService {

    @Autowired
    private QueryClientByKeyDao webClientInfoSearchDao;

    @Autowired
    private ClientQueryDao clientQueryDao;

    @Autowired
    private ClientQueryByIdDao clientQueryByIdDao;

    /**
     * 页面关键词搜索
     *
     * @param companyId
     * @param key
     * @return
     */
    @Override
    public List<SearchClientVO> pageSearch(int companyId, String key) {
        return webClientInfoSearchDao.search(companyId, key);
    }


    /**
     * 根据客资ID 获取
     *
     * @param queryVO
     * @return
     */
    @Override
    public JSONObject getClientInfoByKzid(QueryVO queryVO) {
        return clientQueryByIdDao.getClientByKzid(queryVO);
    }

    /**
     * 获取删除客资
     *
     * @param queryVO
     * @return
     */
    @Override
    public PlatPageVO getDelClient(QueryVO queryVO) {
        return clientQueryDao.queryDelClientInfo(queryVO);
    }

    /**
     * 查询重复客资
     *
     * @param queryVO
     * @return
     */
    @Override
    public PlatPageVO getRepeatClientHsWeb(QueryVO queryVO) {
        return clientQueryDao.checkRepeatInfoHs(queryVO);
    }


    /**
     * 页面客资搜索
     *
     * @param queryVO
     * @return
     */
    @Override
    public PlatPageVO queryPageClientInfo(QueryVO queryVO) {
        return clientQueryDao.clientSearchPage(queryVO);
    }


}
