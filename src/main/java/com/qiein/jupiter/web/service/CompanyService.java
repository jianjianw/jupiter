package com.qiein.jupiter.web.service;

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

	void editTypeRepeat(Boolean b, int companyId);

	void editTypeSrcRepeat(boolean parseBoolean, int companyId);

	void editKZStutas(String statusIgnore,Integer companyId);

	void editKZday(String timeTypeIgnore, String dayIgnore, int companyId);
}