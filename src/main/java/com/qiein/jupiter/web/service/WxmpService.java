package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.vo.ClientVO;

/**
 * 小程序
 *
 * @Author: shiTao
 */
public interface WxmpService {
    /**
     * 搜索客资
     *
     * @return
     */
    PageInfo<ClientVO> searchClient(int companyId, String searchKey, int page, int size);

}
