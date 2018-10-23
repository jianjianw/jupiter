package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.repository.*;
import com.qiein.jupiter.web.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 报表
 */
@Service
public class ReportsServiceImpl implements ReportService {
    @Autowired
    private CityReportsDao cityReportsDao;

    @Autowired
    private DstgProvinceReportsDao provinceReportsDao;

    @Autowired
    private ClientInfoDao clientInfoDao;

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private SourceDao sourceDao;

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
    private ZjsKzOfYearDao zjsKzOfYearDao;
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

    @Autowired
    private DstgYearsClientReportsDao dstgYearsClientReportsDao;

    @Autowired
    private CwMonthOrderCountReportsDao cwMonthOrderCountReportsDao;

    @Autowired
    private DstgYearsClientDetailReportsDao dstgYearsClientDetailReportsDao;

    @Autowired
    private DstgYearsClientReportsRowDao dstgYearsClientReportsRowDao;

    @Autowired
    private PersonalPresentationDao personalPresentationDao;

    @Autowired
    private DstgChannelReportsOrderBySrcDao dstgChannelReportsOrderBySrcDao;

    @Autowired
    private SourceAndStatusReportsDao sourceAndStatusReportsDao;

    @Autowired
    private ClientStatusDao clientStatusDao;

    @Autowired
    private DstgOrderCycleCountDao dstgOrderCycleCountDao;
    @Autowired
    private SalesCenterReportsDao salesCenterReportsDao;


    @Autowired
    private ProfessionalCenterDao professionalCenterDao;
    @Autowired
    private ZjsGroupReportDao zjsGroupReportDao;
    @Autowired
    private ZjsGroupDetailReportDao zjsGroupDetailReportDao;



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
    public PageInfo getDstgAdReports(Integer start, Integer end, Integer companyId, String type,Integer page) {
        //封装对应的参数
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setType(type);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取数据
        List<DstgGoldDataReportsVO> dstgGoldDataReprots = dstgGoldDataReportsDao.getDstgGoldDataReprots(reportsParamVO, invalidConfig);

        Integer pageSize = 20;
        //逻辑分页
        List<DstgGoldDataReportsVO> dstgGoldDataReportsVOS = new LinkedList<>();
        int index = 0;
        if( page * pageSize > dstgGoldDataReprots.size() ){
            for( index = pageSize*(page-1) ;index <dstgGoldDataReprots.size();index++ ){
                dstgGoldDataReportsVOS.add(dstgGoldDataReprots.get(index));
            }
        }else{
            for( index = pageSize*(page-1) ;index <page * pageSize;index++ ){
                dstgGoldDataReportsVOS.add(dstgGoldDataReprots.get(index));
            }
        }
        PageInfo<DstgGoldDataReportsVO> pageInfo = new PageInfo<>(dstgGoldDataReportsVOS);
        pageInfo.setTotal(dstgGoldDataReprots.size());
        return pageInfo;
    }

    @Override
    public List<DstgZxStyleReportsVO> getDstgZxStyleReports(Integer start, Integer end, int companyId, String type, String zxStyleCode,String sourceIds,String collectorId) {
        //封装对应的参数
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setType(type);
        reportsParamVO.setZxStyleCode(zxStyleCode);
        reportsParamVO.setSourceIds(sourceIds);
        reportsParamVO.setCollectorId(collectorId);
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
        List<DictionaryPO> DicList = dictionaryDao.getInvaildReasons(companyId,"invalid_reason");
        List<SourcePO> sourcePOS = sourceDao.findSourseByType1(companyId, CommonConstant.DsSrc,sourceIds,startTime,endTime);
        invalidReasonReportsVO.setInvalidReasonKz(invalidReasonReportsDao.getInvalidReasonReports(sourcePOS,DicList, DBSplitUtil.getTable(TableEnum.info, companyId), DBSplitUtil.getTable(TableEnum.detail, companyId), companyId, sourceIds, startTime, endTime, typeIds));
        list.add(dictionaryPO);
        list.addAll(DicList);
        invalidReasonReportsVO.setInvalidReasons(list);
        return invalidReasonReportsVO;
    }

    /**
     * 获取转介绍月底客资报表
     */
    public List<ZjsKzOfMonthShowVO> ZjskzOfMonth(Integer companyId, String month, String typeIds, String sourceIds, String type) {
        List<Map<String, Object>> newList = zjskzOfMonthDao.getDayOfMonth(Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[0]), Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[1]), DBSplitUtil.getTable(TableEnum.info, companyId));
        List<SourcePO> sourcePOS = sourceDao.findSourseByType(companyId, CommonConstant.ZjsSrc,sourceIds);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        return zjskzOfMonthDao.getzjskzOfMonth(sourcePOS, newList, month.replace(CommonConstant.ROD_SEPARATOR, CommonConstant.FILE_SEPARATOR), companyId, DBSplitUtil.getTable(TableEnum.info, companyId), sourceIds, typeIds, invalidConfig, type);
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
    public Map<String,Object> getProvinceReport(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        //获取公司自定义的无效设置 TODO 其实部分数据是不用调这个借口
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(provinceAnalysisParamDTO.getCompanyId());
//        List<ProvinceReportsVO2> provinceReport = provinceReportsDao.provinceReport(provinceAnalysisParamDTO, invalidConfig);
        Map<String,Object> provinceReport = provinceReportsDao.getDstgProvinceReports(provinceAnalysisParamDTO, invalidConfig);
        return provinceReport;
    }

    @Override
    public DsyyStatusReportsHeaderVO getDsyyStatusReports(Integer start, Integer end, int companyId, String type, String groupId) {
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
        return zjskzOfMonthDao.ZjskzOfMonthIn(newList, companyId, month, sourceId, invalidConfig, CommonConstant.ZjsSrc);
    }

    /**
     * 电商每月客资报表内表详情
     */
    public ZjskzOfMonthMapVO DskzOfMonthIn(Integer companyId, String sourceId, String month) {
        List<Map<String, Object>> newList = zjskzOfMonthDao.getDayOfMonth(Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[0]), Integer.parseInt(month.split(CommonConstant.ROD_SEPARATOR)[1]), DBSplitUtil.getTable(TableEnum.info, companyId));
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        month = month.replace(CommonConstant.ROD_SEPARATOR, CommonConstant.FILE_SEPARATOR);
        return dstgReportsSrcMonthDao.ZjskzOfMonthIn(newList, companyId, month, sourceId, invalidConfig, CommonConstant.DsSrc);
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
    public List<Map<String, Object>> getDSTGSrcMonthReportsSum(String month, String typeId,String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsSum(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询客资量--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> getDSTGSrcMonthReportsAll(String month, String typeId,String sourceId, int companyId) {
    	//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsAll(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询待定客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> getDSTGSrcMonthReportsDdNum(String month, String typeId,String sourceId, int companyId){
    		//封装参数
            ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
            
            reportsParamSrcMonthVO.setTypeId(typeId);
            reportsParamSrcMonthVO.setSourceId(sourceId);
            reportsParamSrcMonthVO.setCompanyId(companyId);
            //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
            DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
          //获取时间时间戳
            int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
            int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsDdNum(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询无效客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> getDSTGSrcMonthReportsInvalid(String month, String typeId,String sourceId, int companyId){
    	//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsInvalid(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    /**
     * 查询有效客资--电商月度客资汇总报表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> getDSTGSrcMonthReportsvalid(String month, String typeId,String sourceId, int companyId) {
        //封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsvalid(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
    }

    @Override
    public DsyyStatusReportsHeaderVO getDsyyStatusDetailReports(Integer start, Integer end, String groupId, int companyId, String type) {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setGroupId(groupId);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setType(type);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        DsyyStatusReportsHeaderVO dsyyStatusReports = dsyyStatusStaffReportsDao.getDsyyStatusReports(reportsParamVO, invalidConfig);
        return dsyyStatusReports;
    }

    @Override
    public List<DstgZxStyleReportsVO> getDstgZxStyleSourceRerports(Integer start, Integer end, String zxStyleCode, String type, int companyId,String collectorId) {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setStart(start);
        reportsParamVO.setEnd(end);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setZxStyleCode(zxStyleCode);
        reportsParamVO.setType(type);
        reportsParamVO.setCollectorId(collectorId);
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
     *
     * @param startTime
     * @param endTime
     * @param keyWord
     * @param companyId
     * @return
     */
    public List<KeyWordReportsVO> getKeyWordReports(String startTime, String endTime, String typeIds, String keyWord, Integer companyId) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        return keyWordReportsDao.getKeyWordReports(startTime, endTime, typeIds, keyWord, companyId, invalidConfig);
    }

    /**
     * 财务 月度订单数据统计
     *
     * @param reportsParamVO
     * @return
     */
    @Override
    public JSONObject getCwMonthOrderCountReports(ReportsParamVO reportsParamVO) {
        return cwMonthOrderCountReportsDao.getCwMonthOrderCountReports(reportsParamVO);
    }

    /**
     * 转介绍年度报表
     *
     * @param zjsClientYearReportDTO
     * @return
     */
    @Override
    public List<ZjsClientYearReportVO2> getZjsYearReport(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(zjsClientYearReportDTO.getCompanyId());
        List<ZjsClientYearReportVO2> list = zjsKzOfYearDao.getZjsKzYearReport(zjsClientYearReportDTO, invalidConfig);
        return list;
    }

    @Override
    public List<DstgYearDetailReportsProcessVO> getDstgYearDetailReports(String years, Integer companyId) {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setYears(years);
        reportsParamVO.setCompanyId(companyId);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        List<DstgYearDetailReportsVO> dstgYearsClietnDetailReports = null;


        //FIXME 此处代码已写死
        List<DstgYearDetailReportsProcessVO> dstgYearDetailReportsProcessVOS = new ArrayList<>();
        try {
            dstgYearsClietnDetailReports = dstgYearsClientDetailReportsDao.getDstgYearsClietnDetailReports(reportsParamVO, invalidConfig);
            //FIXME
            HashMap<String, Object> allClientCountMap = new HashMap();
            HashMap<String, Object> validClientCountMap = new HashMap();
            HashMap<String, Object> validRateMap = new HashMap();
            HashMap<String, Object> allCostMap = new HashMap();
            HashMap<String, Object> clientCostMap = new HashMap();
            HashMap<String, Object> validClientCostMap = new HashMap();

            DstgYearDetailReportsProcessVO dstgYearDetailReportsProcessVO = new DstgYearDetailReportsProcessVO();
            DstgYearDetailReportsProcessVO dstgYearDetailReportsProcessVO1 = new DstgYearDetailReportsProcessVO();
            DstgYearDetailReportsProcessVO dstgYearDetailReportsProcessVO2 = new DstgYearDetailReportsProcessVO();
            DstgYearDetailReportsProcessVO dstgYearDetailReportsProcessVO3 = new DstgYearDetailReportsProcessVO();
            DstgYearDetailReportsProcessVO dstgYearDetailReportsProcessVO4 = new DstgYearDetailReportsProcessVO();
            DstgYearDetailReportsProcessVO dstgYearDetailReportsProcessVO5 = new DstgYearDetailReportsProcessVO();
            dstgYearDetailReportsProcessVO.setName("客资量");
            dstgYearDetailReportsProcessVO1.setName("有效量");
            dstgYearDetailReportsProcessVO2.setName("有效率");
            dstgYearDetailReportsProcessVO3.setName("总花费");
            dstgYearDetailReportsProcessVO4.setName("毛客资成本");
            dstgYearDetailReportsProcessVO5.setName("有效成本");

            for (DstgYearDetailReportsVO dstgYearDetailReportsVO : dstgYearsClietnDetailReports) {
                allClientCountMap.put(dstgYearDetailReportsVO.getMonth(), dstgYearDetailReportsVO.getAllClientCount());
                validClientCountMap.put(dstgYearDetailReportsVO.getMonth(), dstgYearDetailReportsVO.getValidClientCount());
                validRateMap.put(dstgYearDetailReportsVO.getMonth(), dstgYearDetailReportsVO.getValidRate());
                allCostMap.put(dstgYearDetailReportsVO.getMonth(), dstgYearDetailReportsVO.getAllCost());
                clientCostMap.put(dstgYearDetailReportsVO.getMonth(), dstgYearDetailReportsVO.getClientCost());
                validClientCostMap.put(dstgYearDetailReportsVO.getMonth(), dstgYearDetailReportsVO.getValidClientCost());
            }

            dstgYearDetailReportsProcessVO.setProcessData(allClientCountMap);
            dstgYearDetailReportsProcessVO1.setProcessData(validClientCountMap);
            dstgYearDetailReportsProcessVO2.setProcessData(validRateMap);
            dstgYearDetailReportsProcessVO3.setProcessData(allCostMap);
            dstgYearDetailReportsProcessVO4.setProcessData(clientCostMap);
            dstgYearDetailReportsProcessVO5.setProcessData(validClientCostMap);

            dstgYearDetailReportsProcessVOS.add(dstgYearDetailReportsProcessVO);
            dstgYearDetailReportsProcessVOS.add(dstgYearDetailReportsProcessVO1);
            dstgYearDetailReportsProcessVOS.add(dstgYearDetailReportsProcessVO2);
            dstgYearDetailReportsProcessVOS.add(dstgYearDetailReportsProcessVO3);
            dstgYearDetailReportsProcessVOS.add(dstgYearDetailReportsProcessVO4);
            dstgYearDetailReportsProcessVOS.add(dstgYearDetailReportsProcessVO5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dstgYearDetailReportsProcessVOS;
    }

    @Override
    public List<Map<String, Object>> getZjsYearDetailReport(ZjsClientYearReportDTO zjsClientYearReportDTO) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(zjsClientYearReportDTO.getCompanyId());
//        if (zjsClientYearReportDTO.getSourceIds().equals("0"))
//            zjsClientYearReportDTO.setSourceIds(sourceDao.getAllZjsSourceIdStr(zjsClientYearReportDTO.getCompanyId()));
        List<Map<String, Object>> list = zjsKzOfYearDao.getZjsYearDetailReport(zjsClientYearReportDTO, invalidConfig);
        return list;
    }

    /**
     * 个人简报
     * @param reportParamDTO
     * @return
     */
    @Override
    public Map<String,RegionReportsVO> getPersonalPresentation(ReportParamDTO reportParamDTO) {
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(reportParamDTO.getCompanyId());
        return personalPresentationDao.getPersonalPresentation(reportParamDTO,invalidConfig);
    }

    @Override
    public List<DstgYearReportsVO> getDstgYearsReports(String type, String sourceIds, int companyId, String years, String conditionType) {
        ReportsParamVO reportsParamVO = new ReportsParamVO();
        reportsParamVO.setType(type);
        reportsParamVO.setSourceIds(sourceIds);
        reportsParamVO.setCompanyId(companyId);
        reportsParamVO.setYears(years);
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        List<DstgSourceYearReportsVO> dstgYearsClientReports = dstgYearsClientReportsRowDao.getDstgYearsClientReports(reportsParamVO, invalidConfig);
        List<DstgYearReportsVO> dstgYearReportsVOS = dstgYearsClientReportsDao.getDstgYearsClientReports(reportsParamVO, invalidConfig);

        getDataByConditionType(conditionType, dstgYearsClientReports, dstgYearReportsVOS);
        //计算总计
//        Map hashMap = new HashMap();
//        for (DstgYearReportsVO dstgYearReportsVO:dstgYearReportsVOS){
//            for (Map.Entry<String, Object> keys : dstgYearReportsVO.getMapList().entrySet()) {
//                    hashMap.get(keys.getKey()).toString()

//                    hashMap.put(Integer.parseInt(keys.getValue().toString()))
//            }
//        }
        return dstgYearReportsVOS;
    }

    private void computerClientCountTotal(List<DstgYearReportsVO> dstgYearReportsVOS) {
        for (DstgYearReportsVO dstgYearReports : dstgYearReportsVOS) {
            Map<String, Object> mapList = dstgYearReports.getMapList();
            Map<String,Object> newsMapList = new HashMap<>();
            for (Map.Entry<String, Object> keys : mapList.entrySet()) {
                Integer kzNum = (Integer)newsMapList.get("合计");
                if(kzNum == null){
                    kzNum = 0;
                }
                newsMapList.put(keys.getKey(),keys.getValue());
                Integer value = Integer.parseInt(keys.getValue().toString());
                newsMapList.put("合计",  (kzNum+value));
            }
            dstgYearReports.setMapList(newsMapList);
        }
    }

    private void computerClientRateTotal(List<DstgYearReportsVO> dstgYearReportsVOS) {
        for (DstgYearReportsVO dstgYearReports : dstgYearReportsVOS) {
            Map<String, Object> mapList = dstgYearReports.getMapList();
            Map<String,Object> newsMapList = new HashMap<>();
            for (Map.Entry<String, Object> keys : mapList.entrySet()) {
                Double kzNum = (Double)newsMapList.get("合计");
                if(kzNum == null){
                    kzNum = 0.00;
                }
                newsMapList.put(keys.getKey(),keys.getValue());
                Double value = Double.parseDouble(keys.getValue().toString());
                newsMapList.put("合计",  (kzNum+value));
            }
            dstgYearReports.setMapList(newsMapList);
        }
    }



    private void getDataByConditionType(String conditionType, List<DstgSourceYearReportsVO> dstgYearsClientReports, List<DstgYearReportsVO> dstgYearReportsVOS) {
        switch (conditionType) {
            case "sum":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getAllClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "all":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "valid":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getValidClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "come":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getComeShopClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "success":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getSuccessClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "invalid":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getInValidClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "ddnum":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getPendingClientCount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientCountTotal(dstgYearReportsVOS);
                break;
            case "validrate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Integer validClientCount = 0;
                    Integer clientCount = 0;

                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getValidRate());
                            clientCount += dstgSourceYearReportsVO.getClientCount();
                            validClientCount += dstgSourceYearReportsVO.getValidClientCount();
                        }
                    }
                    if(clientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(validClientCount/clientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "invalidrate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Integer clientCount = 0;
                    Integer inValidClientCount = 0;
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getInValidRate());
                            clientCount += dstgSourceYearReportsVO.getClientCount();
                            inValidClientCount += dstgSourceYearReportsVO.getInValidClientCount();
                        }
                    }
                    if(clientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(inValidClientCount/clientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "ddrate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    Integer clientCount = 0;
                    Integer pendingClientCount = 0;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getWaitRate());
                            clientCount += dstgSourceYearReportsVO.getClientCount();
                            pendingClientCount += dstgSourceYearReportsVO.getPendingClientCount();
                        }
                    }
                    if(clientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(pendingClientCount/clientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "allcomerate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    Integer clientCount = 0;
                    Integer comeShopClientCount = 0;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getClientComeShopRate());
                            clientCount += dstgSourceYearReportsVO.getClientCount();
                            comeShopClientCount += dstgSourceYearReportsVO.getComeShopClientCount();
                        }
                    }
                    if(clientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(comeShopClientCount/clientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "validcomerate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    Integer validClientCount = 0;
                    Integer comeShopClientCount = 0;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getValidClientComeShopRate());
                            validClientCount += dstgSourceYearReportsVO.getValidClientCount();
                            comeShopClientCount += dstgSourceYearReportsVO.getComeShopClientCount();
                        }
                    }
                    if(validClientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(comeShopClientCount/validClientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "rdsuccessrate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    Integer comeShopClientCount = 0;
                    Integer successClientCount = 0;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getComeShopSuccessRate());
                            comeShopClientCount += dstgSourceYearReportsVO.getComeShopClientCount();
                            successClientCount += dstgSourceYearReportsVO.getSuccessClientCount();
                        }
                    }
                    if(comeShopClientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(successClientCount/comeShopClientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "allsuccessrate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    Integer clientCount = 0;
                    Integer successClientCount = 0;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getClientSuccessRate());
                            clientCount += dstgSourceYearReportsVO.getClientCount();
                            successClientCount += dstgSourceYearReportsVO.getSuccessClientCount();
                        }
                    }
                    if(clientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(successClientCount/clientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "validsuccessrate":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    Integer validClientCount = 0;
                    Integer successClientCount = 0;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getValidClientSuccessRate());
                            validClientCount += dstgSourceYearReportsVO.getValidClientCount();
                            successClientCount += dstgSourceYearReportsVO.getSuccessClientCount();
                        }
                    }
                    if(validClientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(successClientCount/validClientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "amount":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getAllCost());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                //计算合计
                computerClientRateTotal(dstgYearReportsVOS);
                break;
            case "allcb":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Integer allClientCount = 0;
                    double allCost = 0.00;
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getClientCost());
                            allClientCount += dstgSourceYearReportsVO.getAllClientCount();
                            allCost += Double.parseDouble(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost())?"0.00":dstgSourceYearReportsVO.getAllCost());
                        }
                    }
                    newMap.put("合计",(allCost/allClientCount));
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "validcb":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Integer validClientCount = 0;
                    double allCost = 0.00;
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getValidClientCost());
                            validClientCount += dstgSourceYearReportsVO.getValidClientCount();
                            allCost += Double.parseDouble(StringUtil.isEmpty(dstgSourceYearReportsVO.getAllCost())?"0.00":dstgSourceYearReportsVO.getAllCost());
                        }
                    }
                    if(validClientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(allCost/validClientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "comecb":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Integer comeShopClientCount = 0;
                    double allCost = 0.00;
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getComeShopClientCost());
                            comeShopClientCount += dstgSourceYearReportsVO.getComeShopClientCount();
                            allCost += Double.parseDouble(dstgSourceYearReportsVO.getAllCost());
                        }
                    }
                    if(comeShopClientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(allCost/comeShopClientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "successcb":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Integer successClientCount = 0;
                    double allCost = 0.00;
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getSuccessClientCost());
                            successClientCount += dstgSourceYearReportsVO.getSuccessClientCount();
                            allCost += Double.parseDouble(dstgSourceYearReportsVO.getAllCost());
                        }
                    }
                    if(successClientCount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(allCost/successClientCount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            case "successavg":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getAvgAmount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                computerClientRateTotal(dstgYearReportsVOS);
                break;
            case "successamount":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getAmount());
                        }
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                computerClientRateTotal(dstgYearReportsVOS);
                break;
            case "roi":
                for (DstgYearReportsVO dstgYearReportsVO : dstgYearReportsVOS) {
                    double allCost = 0.00;
                    double amount = 0.00;
                    Map newMap = null;
                    if (CollectionUtils.isNotEmpty(dstgYearReportsVO.getMapList())) {
                        newMap = dstgYearReportsVO.getMapList();
                    } else {
                        newMap = new HashMap();
                    }
                    for (DstgSourceYearReportsVO dstgSourceYearReportsVO : dstgYearsClientReports) {
                        if (dstgYearReportsVO.getSourceId().equals(dstgSourceYearReportsVO.getSourceId())) {
                            newMap.put("month" + dstgSourceYearReportsVO.getMonth(), dstgSourceYearReportsVO.getROI());
                            allCost += Double.parseDouble(dstgSourceYearReportsVO.getAllCost());
                            amount += dstgSourceYearReportsVO.getAmount();
                        }
                    }
                    if(amount == 0){
                        newMap.put("合计",0);
                    }else{
                        newMap.put("合计",(allCost/amount)*100);
                    }
                    dstgYearReportsVO.setMapList(newMap);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 推广渠道报表根据 小组id的详情报表
     * @param groupId
     * @param companyId
     * @return
     */
    public List<DstgChannelReportsOrderBySrcVO> getDstgChannelReportsOrderBySrc(String groupId,Integer companyId,String start,String end,String sourceIds,String typeIds){
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        return dstgChannelReportsOrderBySrcDao.getDstgChannelReportsOrderBySrc(groupId,companyId,start,end,sourceIds,typeIds,invalidConfig);
    }
    /**
     * 客资各个渠道各个状态
     */
    public SourceAndStatusReportsShowVO getSourceAndStatusReports(String appointorIds,String collectorIds,String receptorIds,String start,String end,String groupIds,String typeIds,String sourceIds,Integer companyId){
            List<StatusPO> statusPOS=clientStatusDao.getCompanyStatusList(companyId);
            List<SourcePO> sourcePOS=sourceDao.findSourseByType(companyId, CommonConstant.DsSrc,"");
            SourceAndStatusReportsShowVO sourceAndStatusReportsShowVO=new SourceAndStatusReportsShowVO();
            sourceAndStatusReportsShowVO.setList(sourceAndStatusReportsDao.getSourceAndStatusReports(appointorIds,collectorIds,receptorIds,start,end,groupIds,typeIds,sourceIds,companyId,statusPOS,sourcePOS));
            StatusPO statusPO=new StatusPO();
            statusPO.setStatusId(-1);
            statusPO.setStatusName("合计");
            sourceAndStatusReportsShowVO.setStatusPO(statusPOS);
            return sourceAndStatusReportsShowVO;
    }
    
    /**
     * 电商推广月度报表入店量--HJF
     * @param 
     */


	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCome(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsCome(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表成交量量--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsSuccess(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsSuccess(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表有效率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsValidRate(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidRate(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表无效率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsInValidRate(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsInValidRate(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表待定率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsDdnumRate(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsDdnumRate(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表毛客资入店率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsComeRate(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsComeRate(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表有效客资入店率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsValidComeRate(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidComeRate(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表有效客资入店率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsSuccessRate(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidSuccessRate(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表毛客资成交率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsSuccessRate1(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidSuccessRate1(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表有效客资成交率--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsSuccessRate2(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidSuccessRate2(firstDay,lastDay,reportsParamSrcMonthVO, invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表花费--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCost(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        //DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidCost(firstDay,lastDay,reportsParamSrcMonthVO);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表毛客资成本--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCostKZ(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidCostKZ(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表有效客资成本--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCostValidKZ(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidCostValidKZ(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表入店成本--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCostComeKZ(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidCostComeKZ(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表成交成本--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCostSuccessKZ(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidCostSuccessKZ(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}
	/**
     * 电商推广月度报表成交均价--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsCostSuccessAvg(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsValidCostSuccessAvg(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表营业额--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsAmount(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsAmount(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}

	/**
     * 电商推广月度报表ROI--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsROI(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsROI(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}

    @Override
    public List<Object> getZjsGroupReport(ReportsParamVO reportsParamVO) {
        //List<ZjsClientDetailReportVO> reportVOS = zjsGroupReportDao.getZjsGroupReport(reportsParamVO);
        List<Object> reportVOS = zjsGroupReportDao.getZjsGroupReport(reportsParamVO);
        return reportVOS;
    }



    /**
     * 电商推广订单周期统计
     *
     * @param queryVO
     * @return
     */
    @Override
    public JSONObject getDstgOrderCycleCount(QueryVO queryVO) {
        return dstgOrderCycleCountDao.getCount(queryVO);
    }
    /**
     * 销售中心报表
     */
    public List<SalesCenterReportsVO> getSalesCenterReports(ReportsParamVO reportsParamVO){
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(reportsParamVO.getCompanyId());
        return salesCenterReportsDao.getSalesCenterReports(reportsParamVO,invalidConfig);
    }

    @Override
    public List<Object> getZjsGroupDetailReport(ReportsParamVO reportsParamVO) {
        //List<ZjsClientDetailReportVO> zjsGroupDetailReport = zjsGroupDetailReportDao.getZjsGroupDetailReport(reportsParamVO);
        List<Object> zjsGroupDetailReport = zjsGroupDetailReportDao.getZjsGroupDetailReport(reportsParamVO);
        return zjsGroupDetailReport;
    }
    /**
     * 专业中心报表
     */
    public List<ProfessionalCenterVO> getProfessionalCenterVO(ReportsParamVO reportsParamVO){
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(reportsParamVO.getCompanyId());
        return professionalCenterDao.getProfessionalCenterVO(reportsParamVO,invalidConfig);
    }

    /**
     * 电商推广月度报表预约量--HJF
     * @param 
     */
	@Override
	public List<Map<String, Object>> getDSTGSrcMonthReportsAppointment(String month, String typeId, String sourceId,
			int companyId) {
		//封装参数
        ReportsParamSrcMonthVO reportsParamSrcMonthVO = new ReportsParamSrcMonthVO();
        reportsParamSrcMonthVO.setTypeId(typeId);
        reportsParamSrcMonthVO.setSourceId(sourceId);
        reportsParamSrcMonthVO.setCompanyId(companyId);
        //获取无效状态指标，无效意向等级，待定是否为有效量，待定指标
        DsInvalidVO invalidConfig = commonReportsDao.getInvalidConfig(companyId);
        //获取时间时间戳
        int firstDay = TimeUtil.getMonthStartTimeStampByDate(month);
        int lastDay=TimeUtil.getMonthEndTimeStampByDate(month);	
        //获取客资数据
        List<Map<String, Object>> dstgSrcMonthReports = dstgReportsSrcMonthDao.getDSTGSrcMonthReportsAppointment(firstDay,lastDay,reportsParamSrcMonthVO,invalidConfig);
        return dstgSrcMonthReports;
	}
}
