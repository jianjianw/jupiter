package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CashLogPO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.vo.CashLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收款日志
 *
 * @author gaoxiaoli 2018/05/09 17:35
 */
public interface CashLogDao extends BaseDao<CashLogDao> {

    /**
     * 新增客资日志
     *
     * @param logTabName
     * @param cashLogPO
     * @return
     */
    int addCahsLog(@Param("logTabName") String logTabName, @Param("log") CashLogPO cashLogPO);

    /**
     * 修改付款记录的状态
     */
    void editAmount(@Param("table") String table, @Param("amount") int amount, @Param("id") int id, @Param("companyId") int companyId, @Param("staffId") int staffId,
                    @Param("staffName") String staffName, @Param("payStyle") Integer payStyle, @Param("paymentTime") Integer paymentTime, @Param("typeId") int typeId);

    /**
     * 付款记录查询页面
     *
     * @param kzId
     * @param table
     * @return
     */
    List<CashLogVO> findCashLog(@Param("kzId") String kzId, @Param("table") String table);

    /**
     * 根据ID查询收款记录
     *
     * @param table
     * @param id
     * @param companyId
     * @return
     */
    CashLogPO getCashLogById(@Param("table") String table, @Param("id") int id, @Param("companyId") int companyId);

    /**
     * 获取某个客资的所有收款记录总和
     *
     * @return
     */
    int getClientReceivedAmount(@Param("companyId") int companyId, @Param("table") String table, @Param("kzId") String kzId);

    /**
     * 删除付款记录
     *
     * @param id
     */
    void deleteCashLog(@Param("id") Integer id);

}