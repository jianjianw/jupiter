package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GoldTempPO;
import org.apache.ibatis.annotations.Param;

/**
 * FileName: GoldTempDao
 *
 * @author: yyx
 * @Date: 2018-6-20 18:36
 */
public interface GoldTempDao extends BaseDao<GoldTempPO> {
    /**
     * 根据客资手机号或者名称或者微信查询记录
     *
     * @param kzPhone
     * @param formId
     * @return
     */
    GoldTempPO getByKzNameOrKzPhoneOrKzWechat(@Param(value = "formId") String formId,
                                              @Param(value = "kzPhone") String kzPhone);
}
