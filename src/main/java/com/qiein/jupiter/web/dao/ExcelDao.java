package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导入导出dao
 */
public interface ExcelDao {

    /**
     * 删除员工客资缓存记录
     **/
    void deleteTempByStaffId(@Param("tempName") String tempName, @Param("operaId") int operaId);

    /**
     * 批量写入客资临时缓存表
     */
    int insertExcelClientInfo(@Param("list") List<ClientExcelDTO> list, @Param("tempName") String tempName);

    /**
     * 清空缓存表
     */
    void truncateTempTable(@Param("tempName") String tempName);

    /**
     * 设置企业ID
     */
    void updateCompanyId(@Param("companyId") Integer companyId, @Param("tempName") String tempName,
                         @Param("staffId") Integer staffId);

    /**
     * 没有客资ID的，设置UUID
     */
    void updateKzid(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置咨询类型ID
     */
    void updateType(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置状态名称
     */
    void updateStatusName(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置客资当前分类ID
     **/
    void updateClassId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置来源ID,和来源类型
     */
    void updateSrcIdAndType(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置渠道ID
     *
     * @param tempName
     * @param staffId
     */
    void updateChannelId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新最后跟进时间为当前系统时间
     */
    void updateUpdateTime(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新提报人ID
     */
    void updateCollectorId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新提报人id是空的记录
     */
    void updateEmptyCollector(@Param("tempName") String tempName, @Param("operaId") Integer operaId);

    /**
     * 更新邀约员ID
     */
    void updateAppointId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新邀约客服id是空的记录
     */
    void updateEmptyAppoint(@Param("tempName") String tempName, @Param("operaId") Integer operaId);

    /**
     * 更新拍摄地ID
     */
    void updateShopId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新邀约小组ID
     **/
    void updateGroupId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 获取所有的记录
     **/
    List<ClientExcelDTO> getAllRecordByStaffId(@Param("tempName") String tempName, @Param("staffId") int staffId);

    /**
     * 获取与info表重复的记录
     **/
    List<ClientExcelDTO> getRepeatRecord(@Param("tempName") String tempName, @Param("tableName") String tableName,
                                         @Param("staffId") int staffId);

    /**
     * 获取Excel重复的记录
     **/
    public List<ClientExcelDTO> getExcelRepeatRecord(@Param("tempName") String tempName, @Param("staffId") int staffId);

    /**
     * 添加客资基础信息ByStaffId
     */
    public void insertBaseInfoByStaffId(@Param("tabName") String tabName, @Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 添加客资详细信息 ByStaffId
     */
    public void insertDetailInfoByStaffId(@Param("tabName") String tabName, @Param("tempName") String tempName, @Param("staffId") Integer staffId);

}
