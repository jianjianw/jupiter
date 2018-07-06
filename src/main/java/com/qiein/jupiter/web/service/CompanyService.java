package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.DsinvalDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import org.apache.ibatis.annotations.Param;

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
     * 修改待定是否计入有效
     *
     * @param dsinvalDTO
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
     * @param companyId
     * @param zjsValidStatus
     */
    void editZjsValidStatus(Integer companyId,String zjsValidStatus);

    /**
     * 转介绍有效指标定义
     * @param companyId
     * @return
     */
    List<String> findZjsValidStatus(Integer companyId);
}