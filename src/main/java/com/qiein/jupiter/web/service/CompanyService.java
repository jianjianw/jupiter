package com.qiein.jupiter.web.service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.web.entity.dto.CompanyZjsSetDTO;
import com.qiein.jupiter.web.entity.dto.DsinvalDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;

import java.util.List;
import java.util.Map;

/**
 * 公司实现接口
 */
public interface CompanyService {
    /**
     * 根据ID获取
     *
     * @param companyId
     * @return
     */
    CompanyPO getById(int companyId);

    /**
     * 根据条件获取
     *
     * @param map
     * @return
     */
    List<CompanyPO> findList(Map<String, Object> map);

    /**
     * 删除
     *
     * @param companyId
     * @return
     */
    int deleteFlag(int companyId);

    /**
     * 新增
     *
     * @param companyPO
     * @return
     */
    CompanyPO insert(CompanyPO companyPO);

    /**
     * 编辑
     *
     * @param companyPO
     * @return
     */
    int update(CompanyPO companyPO);

    /**
     * 编辑自定义设置
     *
     * @param column
     * @param flag
     * @return
     */
    int updateFlag(int companyId, String column, boolean flag);

    /**
     * 编辑自定义范围
     *
     * @param column
     * @param num
     * @return
     */
    int updateRange(int companyId, String column, int num);

    /**
     * 获取iplimt
     *
     * @param companyId
     * @return
     */
    int getIpLimit(int companyId);

    /**
     * 修改iplimt
     *
     * @param ipLimit
     * @param companyId
     * @return
     */
    void editIpLimit(Integer ipLimit, int companyId);

    /**
     * 获取公司PO 对象
     *
     * @param companyId
     * @return
     */
    CompanyVO getCompanyVO(int companyId);

    /**
     * 更改咨询类型(客资校验是否忽略咨询类型)
     *
     * @return
     */
    void editTypeRepeat(Boolean b, int companyId);

    /**
     * 更改渠道类型(客资校验是否忽略渠道类型)
     *
     * @return
     */
    void editTypeSrcRepeat(boolean parseBoolean, int companyId);

    /**
     * 更改客资录入时间和最后操作时间,客资状态是否可以重复录
     *
     * @return
     */
    void editKZday(String statusIgnore, String timeTypeIgnore, int dayIgnore, int companyId);

    /**
     * 查询哪些客资重复被拦截
     *
     * @return
     */
    CompanyPO selectAll(int companyId);

    /**
     * 修改无效状态以及待跟踪意向等级
     *
     * @param dsinvalDTO
     */
    void editDsInvalid(DsinvalDTO dsinvalDTO);

    /**
     * 根据员工手机号和加密密码查找 所在公司
     */
    List<CompanyVO> getCompanyListByPhoneAndPwd(String phone, String pwd);

    /**
     * 修改待定是否计入有效
     *
     * @param
     */
    void editDdIsValid(boolean ddIsValid, int companyId);

    /**
     * 搜索无效状态以及跟踪意向等级
     *
     * @param companyId
     * @return
     */
    DsinvalDTO findDsinvalId(Integer companyId);

    /**
     * 修改转介绍有效指标定义
     *
     * @param companyId
     * @param zjsValidStatus
     */
    void editZjsValidStatus(Integer companyId, String zjsValidStatus);


    /**
     * 功能描述:
     * 获取公司转介绍设置
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    CompanyZjsSetDTO getCompanyZjsSet(int companyId);

    /**
     * 公司钉钉转介绍提报自定义设置
     *
     * @param oldClientZjsSet
     * @param qyZjsSet
     */
    void editCompanyZJSSet(String oldClientZjsSet, String qyZjsSet, int companyId);

    /**
     * 客服编辑接待结果
     */
    void editKfEditJdRst(boolean kfEditJdRst, Integer companyId);

    /**
     * 获取转介绍提报必填项设置
     *
     * @param companyId
     * @return
     */
    String getZjsRequiredField(int companyId);

    /**
     * 修改电商待定自定义 状态
     *
     * @param dsddStatus
     */
    void editDsddStatus(String dsddStatus, Integer companyId);

    /**
     * 编辑公司报表设置
     *
     * @param companyId
     * @param reportsConfig
     */
    void editReportsConfig(int companyId, String reportsConfig);

    /**
     * 是否可以修改手机和微信的权限
     *
     * @param editPhoneAndWechat
     * @param companyId
     */
    void editPhoneAndWechat(boolean editPhoneAndWechat, int companyId);

    /**
     * 编辑公司公共设置
     */
    int editConfig(int companyId, String config);

    /**
     * 获取公司配置
     */
    JSONObject getCompanyConfig(int companyId);

    /**
     * 定时执行企业的配置任务
     */
    int timingExecuteConfigTask();
}