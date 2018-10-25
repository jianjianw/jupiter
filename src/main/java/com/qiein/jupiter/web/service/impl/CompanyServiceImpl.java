package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.CompanyConfigDTO;
import com.qiein.jupiter.web.entity.dto.CompanyReportConfigDTO;
import com.qiein.jupiter.web.entity.dto.CompanyZjsSetDTO;
import com.qiein.jupiter.web.entity.dto.DsinvalDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.Datav;
import com.qiein.jupiter.web.entity.po.DatavPermissionPo;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.StaffService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公司实现类
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private StaffService staffService;


    /**
     * 根据Id获取
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyPO getById(int companyId) {
        return companyDao.getById(companyId);
    }

    /**
     * 根据查询条件获取集合
     *
     * @param map
     * @return
     */
    @Override
    public List<CompanyPO> findList(Map<String, Object> map) {
        return null;
    }

    /**
     * 删除公司 逻辑删除
     *
     * @param companyId
     * @return
     */
    @Override
    public int deleteFlag(int companyId) {
        return 0;
    }

    /**
     * 插入
     *
     * @param companyPO
     * @return
     */
    @Override
    public CompanyPO insert(CompanyPO companyPO) {
        return null;
    }

    /**
     * 更新
     *
     * @param companyPO
     * @return
     */
    @Override
    @Transactional
    public int update(CompanyPO companyPO) {
        if (companyPO.getId() == 0)
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        if (StringUtil.isNotEmpty(companyPO.getCorpId()))
            //修改corpId时，同时修改所属公司员工的corpId
            staffDao.editStaffCorpId(companyPO.getCorpId(), companyPO.getId());

        return companyDao.update(companyPO);
    }

    /**
     * 编辑自定义设置
     *
     * @param column
     * @param flag
     * @return
     */
    @Override
    public int updateFlag(int companyId, String column, boolean flag) {
        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        }
        return companyDao.updateFlag(companyId, column, flag);
    }

    /**
     * 编辑自定义范围
     *
     * @param column
     * @param num
     * @return
     */
    @Override
    public int updateRange(int companyId, String column, int num) {
        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        }
        return companyDao.updateRange(companyId, column, num);
    }

    /**
     * 获取iplimt
     *
     * @param companyId
     * @return
     */
    @Override
    public int getIpLimit(int companyId) {
        return companyDao.getIpLimit(companyId);
    }

    /**
     * 修改iplimt
     *
     * @param ipLimit * @param companyId
     * @return
     */
    @Override
    public void editIpLimit(Integer ipLimit, int companyId) {
        companyDao.editIpLimit(ipLimit, companyId);
    }

    /**
     * 获取VO
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyVO getCompanyVO(int companyId) {
        return companyDao.getVOById(companyId);
    }

    /**
     * 更改咨询类型(客资校验是否忽略咨询类型)
     *
     * @return
     */
    @Override
    public void editTypeRepeat(Boolean typeRepeat, int companyId) {
        companyDao.editTypeRepeat(typeRepeat, companyId);
    }

    /**
     * 更改渠道类型(客资校验是否忽略渠道类型)
     *
     * @return
     */
    @Override
    public void editTypeSrcRepeat(boolean srcRepeat, int companyId) {
        companyDao.editTypeSrcRepeat(srcRepeat, companyId);
    }

    /**
     * 更改客资录入时间和最后操作时间,客资状态是否可以重复录
     *
     * @return
     */
    @Override
    public void editKZday(String statusIgnore, String timeTypeIgnore, int dayIgnore, int companyId) {
        companyDao.editKZday(statusIgnore, timeTypeIgnore, dayIgnore, companyId);
    }

    /**
     * 查询哪些客资重复被拦截
     *
     * @return
     */
    @Override
    public CompanyPO selectAll(int companyId) {
        return companyDao.selectAll(companyId);
    }

    /**
     * 修改无效状态以及待跟踪意向等级
     *
     * @param dsinvalDTO
     */
    public void editDsInvalid(DsinvalDTO dsinvalDTO) {
        companyDao.editDsInvalid(dsinvalDTO);
    }

    /**
     * 修改待定是否计入有效
     *
     * @param ddIsValid
     * @param companyId
     */
    public void editDdIsValid(boolean ddIsValid, int companyId) {
        companyDao.editDdIsValid(ddIsValid, companyId);
    }

    /**
     * 搜索无效状态以及跟踪意向等级
     *
     * @param companyId
     * @return
     */
    public DsinvalDTO findDsinvalId(Integer companyId) {
        DsinvalDTO dsinvalDTO = companyDao.findDsinvalId(companyId);
        //拼接转介绍自定义list
        String zjsValidStatus = dsinvalDTO.getZjsValidStatus();
        if (StringUtil.isEmpty(zjsValidStatus)) {
            zjsValidStatus = ",,/,/,/,/,/,/,";
        }
        String[] zjsValidStatuss = zjsValidStatus.split(CommonConstant.FILE_SEPARATOR);
        List<String> list = new ArrayList<>();
        for (String status : zjsValidStatuss) {
            list.add(status);
        }
        dsinvalDTO.setList(list);
        //拼接电商待定自定义
        String dsddStatuss = dsinvalDTO.getDsddStatus();
        if (StringUtil.isEmpty(dsddStatuss)) {
            dsddStatuss = ",,/,/,/,/,/,/,";
        }
        String[] dsddStatus = dsddStatuss.split(CommonConstant.FILE_SEPARATOR);
        List<String> dsddList = new ArrayList<>();
        for (String status : dsddStatus) {
            dsddList.add(status);
        }
        dsinvalDTO.setDsddList(dsddList);
        return dsinvalDTO;
    }

    /**
     * 修改转介绍有效指标定义
     *
     * @param companyId
     * @param zjsValidStatus
     */
    public void editZjsValidStatus(Integer companyId, String zjsValidStatus) {
        companyDao.editZjsValidStatus(companyId, zjsValidStatus);
    }

    /**
     * 功能描述:
     * 获取企业转介绍自定义设置
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @Override
    public CompanyZjsSetDTO getCompanyZjsSet(int companyId) {
        return companyDao.getCompanyZjsSet(companyId);
    }

    /**
     * 功能描述:
     * 公司钉钉转介绍提报自定义设置
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @Override
    public void editCompanyZJSSet(String oldClientZjsSet, String qyZjsSet, int companyId) {
        if (companyDao.editCompanyZJSSet(oldClientZjsSet, qyZjsSet, companyId) == 0)
            throw new RException(ExceptionEnum.COMPANY_EDIT_ZJS_SET);
    }

    /**
     * 客服编辑接待结果
     */
    public void editKfEditJdRst(boolean kfEditJdRst, Integer companyId) {
        companyDao.editKfEditJdRst(kfEditJdRst, companyId);
    }

    /**
     * 获取转介绍提报必填项设置
     *
     * @param companyId
     * @return
     */
    public String getZjsRequiredField(int companyId) {
        return companyDao.getZjsRequiredField(companyId);
    }

    /**
     * 修改电商待定自定义状态
     *
     * @param dsddStatus
     * @return
     */
    public void editDsddStatus(String dsddStatus, Integer companyId) {
        companyDao.editDsddStatus(dsddStatus, companyId);
    }

    /**
     * 编辑公司报表设置
     *
     * @param companyId
     * @param reportsConfig
     */
    @Override
    public void editReportsConfig(int companyId, String reportsConfig) {
        companyDao.editReportsConfig(companyId, reportsConfig);
    }

    /**
     * 根据手机号码和加密码  获取对应的公司
     *
     * @param phone
     * @param pwd
     * @return
     */
    @Override
    public List<CompanyVO> getCompanyListByPhoneAndPwd(String phone, String pwd) {
        return companyDao.getCompanyListByPhoneAndPwd(phone, pwd);
    }

    /**
     * 是否可以修改手机和微信的权限
     *
     * @param editPhoneAndWechat
     * @param companyId
     */
    public void editPhoneAndWechat(boolean editPhoneAndWechat, int companyId) {
        companyDao.editPhoneAndWechat(editPhoneAndWechat, companyId);
    }

    /**
     * 编辑公司公共设置
     *
     * @param companyId
     * @param config
     * @return
     */
    @Override
    public int editConfig(int companyId, String config) {
        return companyDao.editConfig(companyId, config);
    }


    /**
     * 获取公司配置
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyConfigDTO getCompanyConfig(int companyId) {
        CompanyVO companyVO = companyDao.getVOById(companyId);
        CompanyConfigDTO companyConfigDTO = new CompanyConfigDTO();
        String config = companyVO.getConfig();
        if (StringUtil.isNotEmpty(config)) {
            try {
                companyConfigDTO = JSONObject.parseObject(config, CompanyConfigDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                companyDao.editConfig(companyId, JSONObject.toJSONString(companyConfigDTO));
                return companyConfigDTO;
            }

        }
        return companyConfigDTO;
    }

    /**
     * 定时执行企业的配置任务
     *
     * @return
     */
    @Override
    public int timingExecuteConfigTask() {
        int i = 0;
        List<CompanyPO> companyPOS = companyDao.listComp(1);
        for (CompanyPO companyPO : companyPOS) {
            String config = companyPO.getConfig();
            int companyId = companyPO.getId();
            if (StringUtil.isNotEmpty(config)) {
                CompanyConfigDTO companyConfigDTO = JSONObject.parseObject(config, CompanyConfigDTO.class);
                //如果企业 启用了自动关闭分配
                if (companyConfigDTO.isAutoCloseAllot()) {
                    companyConfigDTO.setAutoAllotDs(false);
                    companyConfigDTO.setAutoAllotZjs(false);
                }
                //如果企业开启自动下线
                if (companyConfigDTO.isAutoOffline()) {
                    staffService.companyStaffOffLine(companyId);
                }
                companyDao.editConfig(companyId, JSONObject.toJSONString(companyConfigDTO));
            }
            i++;
        }
        return i;
    }

    /**
     * 获取权限
     *
     * @param companyId
     */
    @Override
    public List<DatavPermissionPo> getPermission(String phone, int companyId) {
        List<DatavPermissionPo> permission = companyDao.getPermission(phone, companyId);
        return permission;
    }

    /**
     * 获取大屏数据
     *
     * @param companyId
     */
    @Override
    public List<Datav> getDatav(int companyId) {
        List<Datav> datav = companyDao.getDatav(companyId);
        return datav;
    }

    /**
     * 获取公司报表设置
     *
     * @param companyId
     * @return
     */
    @Override
    public JSONObject getCompanyReportConfig(int companyId) {

        CompanyVO companyVO = companyDao.getReportConfigById(companyId);
//        CompanyReportConfigDTO companyReportConfigDTO = new CompanyReportConfigDTO();
        JSONObject json = new JSONObject();
        String reportsConfig = companyVO.getReportsConfig();
        if (StringUtil.isNotEmpty(reportsConfig)) {
            try {
                json = JSONObject.parseObject(reportsConfig);
            } catch (Exception e) {
//                companyDao.editConfig(companyId, JSONObject.toJSONString(companyReportConfigDTO));
//                return companyReportConfigDTO;
                e.printStackTrace();
            }

        }
        return json;
    }

}