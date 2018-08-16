package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.entity.dto.CitiesAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.EditClientPhonePO;
import com.qiein.jupiter.web.entity.po.RepateKzLogPO;
import com.qiein.jupiter.web.entity.po.WechatScanPO;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.repository.*;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表
 */
@Service
public class ReportsServiceImpl implements ReportService {
    @Autowired
    private CityReportsDao cityReportsDao;

    @Autowired
    private ProvinceReportsDao provinceReportsDao;

    @Autowired
    private ClientInfoDao clientInfoDao;

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private DstgGoldDataReportsDao dstgGoldDataReportsDao;

    @Autowired
    private DstgZxStyleReportsDao zxStyleReportsDao;

    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private CommonReportsDao commonReportsDao;
    @Autowired
    private InvalidReasonReportsDao invalidReasonReportsDao;
    @Autowired
    private ZjskzOfMonthDao zjskzOfMonthDao;
    @Autowired
    private DsyyStatusReportsDao dsyyStatusReportsDao;
    @Autowired
    private OldKzReportsDao oldKzReportsDao;
    @Autowired
    private DstgReportsSrcMonthDao dstgReportsSrcMonthDao;
    @Autowired
    private DsyyStatusStaffReportsDao dsyyStatusStaffReportsDao;
    @Autowired
    private DstgZxStyleSourceReportsDao dstgZxStyleSourceReportsDao;

    @Autowired
    private SourceOrderDataReportsDao sourceOrderDataReportsDao;

    @Autowired
    private ClientStatusTranslateReportsDao clientStatusTranslateReportsDao;

    @Autowired
    private KeyWordReportsDao keyWordReportsDao;

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
     *
     * @param start
     * @param end
     * @param companyId
     */
    @Override
    public List<DstgGoldDataReportsVO> getDstgAdReports(Integer start, Integer end, Integer companyId,Integer type) {
        //封装对应的参数
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setType(type);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取数据
        List<DstgGoldDataReportsVO> dstgGoldDataReprots = dstgGoldDataReportsDao.getDstgGoldDataReprots(reportsParamVO, invalidConfig);
        return dstgGoldDataReprots;
    }

    @Override
    public List<DstgZxStyleReportsVO> getDstgZxStyleReports(Integer start, Integer end, int companyId,Integer type,String zxStyleCode) {
        //封装对应的参数
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setType(type);
        reportsParamVO.setZxStyleCode(zxStyleCode);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取数据
        List<DstgZxStyleReportsVO> dstgZxStyleReportsVOS = zxStyleReportsDao.getDstgGoldDataReprots(reportsParamVO, invalidConfig);
        return dstgZxStyleReportsVOS;
    }

    /**
     * 获取无效原因客资报表
     *
     * @param companyId
     * @return
     */
    public InvalidReasonReportsVO invalidReasonReports(Integer companyId, String sourceIds, String startTime, String endTime, String typeIds) {
        InvalidReasonReportsVO invalidReasonReportsVO = new InvalidReasonReportsVO();
        List<DictionaryPO> list = new ArrayList<>();
        DictionaryPO dictionaryPO = new DictionaryPO();
        dictionaryPO.setDicType("hj");
        dictionaryPO.setDicName("合计");
        List<DictionaryPO> DicList = dictionaryDao.getInvaildReasons(companyId, DictionaryConstant.INVALID_REASON);
        invalidReasonReportsVO.setInvalidReasonKz(invalidReasonReportsDao.getInvalidReasonReports(DicList, DBSplitUtil.getTable(TableEnum.info, companyId), DBSplitUtil.getTable(TableEnum.detail, companyId), companyId, sourceIds, startTime, endTime, typeIds));
        list.add(dictionaryPO);
        list.addAll(DicList);
        invalidReasonReportsVO.setInvalidReasons(list);
        return invalidReasonReportsVO;
    }

    /**
     * 获取转介绍月底客资报表
     */
    public ZjskzOfMonthVO ZjskzOfMonth(Integer companyId, String month, String type, String sourceIds) {
        ZjskzOfMonthVO zjskzOfMonthVO = new ZjskzOfMonthVO();
        List<Map<String, Object>> newList = zjskzOfMonthDao.getDayOfMonth(Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[0]), Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[1]), DBSplitUtil.getTable(TableEnum.info, companyId));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dayName", "合计");
        map.put("dayKey", "hj");
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        list.addAll(newList);
        zjskzOfMonthVO.setHeadList(list);
        zjskzOfMonthVO.setList(zjskzOfMonthDao.getzjskzOfMonth(newList, month.replace(CommonConstant.ROD_SEPARATOR, CommonConstant.FILE_SEPARATOR), companyId, DBSplitUtil.getTable(TableEnum.info, companyId), sourceIds, type));
        return zjskzOfMonthVO;
    }

    @Override
    public List<RegionReportsVO> getCityReport(CitiesAnalysisParamDTO citiesAnalysisParamDTO) {
        //获取公司自定义的无效设置
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(citiesAnalysisParamDTO.getCompanyId());
        //获取市域分析报表
        List<RegionReportsVO> cityReport = cityReportsDao.getCityReport(citiesAnalysisParamDTO, invalidConfig);
        return cityReport;
    }

    @Override
    public List<ProvinceReportsVO2> getProvinceReport(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        //获取公司自定义的无效设置 TODO 其实部分数据是不用调这个借口
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(provinceAnalysisParamDTO.getCompanyId());
        List<ProvinceReportsVO2> provinceReport = provinceReportsDao.provinceReport(provinceAnalysisParamDTO,invalidConfig);
        return provinceReport;
    }

    @Override
    public DsyyStatusReportsHeaderVO getDsyyStatusReports(Integer start, Integer end, int companyId,Integer type,String groupId) {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setType(type);
        reportsParamVO.setGroupId(groupId);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        DsyyStatusReportsHeaderVO dsyyStatusReports = dsyyStatusReportsDao.getDsyyStatusReports(reportsParamVO, invalidConfig);
        return dsyyStatusReports;
    }

    /**
     * 转介绍每月客资报表内表详情
     */
    public ZjskzOfMonthMapVO ZjskzOfMonthIn(Integer companyId, String sourceId, String month) {
        List<Map<String, Object>> newList = zjskzOfMonthDao.getDayOfMonth(Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[0]), Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[1]), DBSplitUtil.getTable(TableEnum.info, companyId));
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        month = month.replace(CommonConstant.ROD_SEPARATOR, CommonConstant.FILE_SEPARATOR);
        return zjskzOfMonthDao.ZjskzOfMonthIn(newList, companyId, month, sourceId, invalidConfig);
    }

    /**
     * 老客信息汇总报表
     */
    public List<OldKzReportsVO> getOldKzReports(Integer companyId, String startTime, String endTime, String kzNameOrPhone) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        return oldKzReportsDao.getReports(companyId, startTime, endTime, kzNameOrPhone, invalidConfig);
    }

    /**
     * 查询总客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsSum(Integer start, Integer end, String typeId,
                                                                 String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setStart(start);
        reportsParamSrcMonthVO.setEnd(end);
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取客资数据
        List<DstgReportsSrcMonthVO> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsSum(reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsAll(Integer start, Integer end, String typeId,
                                                                 String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setStart(start);
        reportsParamSrcMonthVO.setEnd(end);
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取客资数据
        List<DstgReportsSrcMonthVO> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsAll(reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询待定客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsDdNum(Integer start, Integer end, String typeId,
                                                                   String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setStart(start);
        reportsParamSrcMonthVO.setEnd(end);
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取客资数据
        List<DstgReportsSrcMonthVO> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsDdNum(reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询无效客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsInvalid(Integer start, Integer end, String typeId,
                                                                     String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setStart(start);
        reportsParamSrcMonthVO.setEnd(end);
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取客资数据
        List<DstgReportsSrcMonthVO> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsInvalid(reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }


    @Override
    public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsvalid(Integer start, Integer end, String typeId,
                                                                   String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setStart(start);
        reportsParamSrcMonthVO.setEnd(end);
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取客资数据
        List<DstgReportsSrcMonthVO> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsvalid(reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    @Override
    public DsyyStatusReportsHeaderVO getDsyyStatusDetailReports(Integer start, Integer end, String groupId, int companyId) {
        //FIXME 此处有问题
        if (StringUtil.isEmpty(groupId)) {
            throw new RException(ExceptionEnum.GROUP_IS_NULL);
        }
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setGroupId(groupId);
        reportsParamVO.setCompanyId(companyId);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        DsyyStatusReportsHeaderVO dsyyStatusReports = dsyyStatusStaffReportsDao.getDsyyStatusReports(reportsParamVO, invalidConfig);
        return dsyyStatusReports;
    }

    @Override
    public List<DstgZxStyleReportsVO> getDstgZxStyleSourceRerports(Integer start, Integer end, String zxStyleCode,Integer type, int companyId) {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setZxStyleCode(zxStyleCode);
        reportsParamVO.setType(type);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        List<DstgZxStyleReportsVO> dstgGoldDataReprots = dstgZxStyleSourceReportsDao.getDstgGoldDataReprots(reportsParamVO, invalidConfig);
        return dstgGoldDataReprots;
    }


    /**
     * 渠道订单数据统计
     *
     * @param reportsParamVO
     * @return
     */
    @Override
    public List<SourceOrderDataReportsVO> getSourceOrderDataReports(ReportsParamVO reportsParamVO) {
        return sourceOrderDataReportsDao.getSourceOrderDataReports(reportsParamVO);
    }

    /**
     * 客资各个状态转化统计
     *
     * @param reportsParamVO
     * @return
     */
    @Override
    public List<JSONObject> getClientStatusTranslateForGroup(ReportsParamVO reportsParamVO) {
        if (StringUtil.isNotEmpty(reportsParamVO.getGroupId())) {
            //查询个人
            return clientStatusTranslateReportsDao.getClientStatusTranslateForGroupStaff(reportsParamVO);
        } else {
            //查询小组
            return clientStatusTranslateReportsDao.getClientStatusTranslateForGroup(reportsParamVO);
        }
    }
    /**
     * 关键词报表
     * @param startTime
     * @param endTime
     * @param typeIds
     * @param keyWord
     * @param companyId
     * @return
     */
    public List<KeyWordReportsVO> getKeyWordReports(String startTime,String endTime,String typeIds,String keyWord,Integer companyId){
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        return keyWordReportsDao.getKeyWordReports( startTime, endTime, typeIds, keyWord, companyId,invalidConfig);
    }
}
