package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.EditClientPhonePO;
import com.qiein.jupiter.web.entity.po.RepateKzLogPO;
import com.qiein.jupiter.web.entity.po.WechatScanPO;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgGoldDataReportsVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamVO;
import com.qiein.jupiter.web.repository.CommonReportsDao;
import com.qiein.jupiter.web.repository.DstgGoldDataReportsDao;
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

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private DstgGoldDataReportsDao dstgGoldDataReportsDao;

    @Autowired
    private CommonReportsDao commonReportsDao;

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

        // 获取员工角色
        List<String> roleList = groupStaffDao.getStaffRoleList(clientLogDTO.getCompanyId(), clientLogDTO.getStaffId());

        for (String role : roleList) {
            // 1.如果是管理中心，全部开放
            if (RoleConstant.GLZX.equals(role)) {
                clientLogDTO.setStaffId(CommonConstant.SYSTEM_OPERA_ID);
                break;
            }
        }
        List<RepateKzLogPO> list = clientInfoDao.repateKzLog(clientLogDTO);
        return new PageInfo<>(list);
    }


    /**
     * 获取电商推广广告报表
     * @param start
     * @param end
     * @param companyId
     * */
    @Override
    public List<DstgGoldDataReportsVO> getDstgAdReports(Integer start, Integer end, Integer companyId) {
       //封装对应的参数
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取数据
        List<DstgGoldDataReportsVO> dstgGoldDataReprots = dstgGoldDataReportsDao.getDstgGoldDataReprots(reportsParamVO,invalidConfig);
        return dstgGoldDataReprots;
    }
}
