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
    public void deleteTempByStaffId(@Param("tempName") String tempName, @Param("operaId") int operaId);

    /**
     * 批量写入客资临时缓存表
     */
    public int insertExcelClientInfo(@Param("list") List<ClientExcelDTO> list, @Param("tempName") String tempName);

    /**
     * 清空缓存表
     */
    public void truncateTempTable(@Param("tempName") String tempName);

    /**
     * 设置企业ID
     */
    public void updateCompanyId(@Param("companyId") Integer companyId, @Param("tempName") String tempName,
                                @Param("staffId") Integer staffId);

    /**
     * 没有客资ID的，设置UUID
     */
    public void updateKzid(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置咨询类型ID
     */
    public void updateType(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置状态名称
     */
    public void updateStatusName(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置客资当前分类ID
     **/
    public void updateClassId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 设置渠道ID
     */
    public void updateSrcId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新最后跟进时间为当前系统时间
     */
    public void updateUpdateTime(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新提报人ID
     */
    public void updateCollectorId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新提报人id是空的记录
     */
    public void updateEmptyCollector(@Param("tempName") String tempName, @Param("operaId") Integer operaId);

    /**
     * 更新邀约员ID
     */
    public void updateAppointId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新邀约客服id是空的记录
     */
    public void updateEmptyAppoint(@Param("tempName") String tempName, @Param("operaId") Integer operaId);

    /**
     * 更新拍摄地ID
     */
    public void updateShopId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 更新邀约小组ID
     **/
    public void updateGroupId(@Param("tempName") String tempName, @Param("staffId") Integer staffId);


}
