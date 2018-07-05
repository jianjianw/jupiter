package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.EditClientPhonePO;
import com.qiein.jupiter.web.entity.po.RepateKzLogPO;
import com.qiein.jupiter.web.entity.po.WechatScanPO;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsServiceImpl implements ReportService {
    @Autowired
    private ClientInfoDao clientInfoDao;

    /**
     * 修改联系方式日志
     *
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    public PageInfo editClientPhoneLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO) {
        List<Integer> sourseIds = new ArrayList<>();
        if (!StringUtil.isEmpty(clientLogDTO.getSourceIds())) {
            String[] ids = clientLogDTO.getSourceIds().split(CommonConstant.STR_SEPARATOR);

            for (String id : ids) {
                sourseIds.add(Integer.parseInt(id));
            }
        }
        clientLogDTO.setList(sourseIds);
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        clientLogDTO.setTableEditLog(DBSplitUtil.getEditLogTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setTableInfo(DBSplitUtil.getInfoTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setTableDetail(DBSplitUtil.getDetailTabName(clientLogDTO.getCompanyId()));
        List<EditClientPhonePO> list = clientInfoDao.editClientPhoneLog(clientLogDTO);
        return new PageInfo<>(list);

    }


    /**
     * 微信扫码日志
     *
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    public PageInfo wechatScanCodeLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO) {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        clientLogDTO.setTableInfo(DBSplitUtil.getInfoTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setTableDetail(DBSplitUtil.getDetailTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setTableLog(DBSplitUtil.getInfoLogTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setLogType(ClientLogConst.INFO_LOGTYPE_SCAN_WECAHT);
        List<WechatScanPO> list = clientInfoDao.wechatScanCodeLog(clientLogDTO);
        return new PageInfo<>(list);
    }

    /**
     * 重复客资记录
     *
     * @param queryMapDTO
     * @param clientLogDTO
     * @return
     */
    public PageInfo repateKzLog(QueryMapDTO queryMapDTO, ClientLogDTO clientLogDTO) {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        clientLogDTO.setTableInfo(DBSplitUtil.getInfoTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setTableDetail(DBSplitUtil.getDetailTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setTableLog(DBSplitUtil.getInfoLogTabName(clientLogDTO.getCompanyId()));
        clientLogDTO.setLogType(ClientLogConst.INFO_LOGTYPE_REPEAT);
        List<RepateKzLogPO> list = clientInfoDao.repateKzLog(clientLogDTO);
        return new PageInfo<>(list);
    }
}