package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.ClientExcelNewsDTO;
import com.qiein.jupiter.web.entity.dto.ClientSortCountDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导入导出dao
 */
public interface ExcelDao {

    /**
     * 删除员工客资缓存记录
     **/
    void deleteTempByStaffId(@Param("operaId") int operaId);

    /**
     * 批量写入客资临时缓存表
     */
    int insertExcelClientInfo(@Param("list") List<ClientExcelNewsDTO> list);

    /**
     * 清空缓存表
     */
    void truncateTempTable();

    /**
     * 设置企业ID
     */
    void updateCompanyId(@Param("companyId") Integer companyId, @Param("staffId") Integer staffId);

    /**
     * 没有客资ID的，设置UUID
     */
    void updateKzid(@Param("staffId") Integer staffId);

    /**
     * 设置咨询类型ID
     */
    void updateType(@Param("staffId") Integer staffId);

    /**
     * 设置状态名称
     */
    void updateStatusName(@Param("staffId") Integer staffId);

    /**
     * 设置客资当前分类ID
     **/
    void updateClassId(@Param("staffId") Integer staffId);

    /**
     * 设置来源ID,和来源类型
     */
    void updateSrcIdAndType(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);

    /**
     * 设置渠道和来源信息
     *
     * @param staffId
     */
    void updateSrcAndChannel(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);

    /**
     * 设置渠道ID
     *
     * @param staffId
     */
    void updateChannelId(@Param("staffId") Integer staffId);

    /**
     * 更新最后跟进时间为当前系统时间
     */
    void updateUpdateTime(@Param("staffId") Integer staffId);

    /**
     * 更新提报人ID
     */
    void updateCollectorId(@Param("staffId") Integer staffId);

    /**
     * 更新提报人id是空的记录
     */
    void updateEmptyCollector(@Param("operaId") Integer operaId);

    /**
     * 更新邀约员ID
     */
    void updateAppointId(@Param("staffId") Integer staffId);

    /**
     * 更新邀约客服id是空的记录
     */
    void updateEmptyAppoint(@Param("operaId") Integer operaId);

    /**
     * 更新拍摄地ID
     */
    void updateShopId(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);

    /**
     * 更新邀约小组ID
     **/
    void updateGroupId(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId);

    /**
     * 更新门市id
     *
     * @param staffId
     */
    void updateReceptorId(@Param(value = "staffId") int staffId, @Param("companyId") Integer companyId);

    /**
     * 获取所有的记录
     **/
    List<ClientExcelNewsDTO> getAllRecordByStaffId(@Param("staffId") int staffId);


    /**
     * 获取与info表重复的记录
     **/
    List<ClientExcelNewsDTO> getRepeatRecord(@Param("staffId") int staffId);

    /**
     * 获取Excel重复的记录
     **/
    List<ClientExcelNewsDTO> getExcelRepeatRecord(@Param("staffId") int staffId);

    /**
     * 添加客资基础信息ByStaffId
     */
    void insertBaseInfoByStaffId(@Param("staffId") Integer staffId);

    /**
     * 添加客资详细信息 ByStaffId
     */
    void insertDetailInfoByStaffId(@Param("staffId") Integer staffId);

    /**
     * 插入客资备注表
     *
     * @param staffId
     */
    void addExcelKzRemark(@Param("staffId") Integer staffId);

    /**
     * 批量删除员工客资缓存记录
     *
     * @param kzIdArr
     * @param operaId
     */
    void batchDeleteTemp(@Param("kzIdArr") String[] kzIdArr, @Param("operaId") int operaId);

    /**
     * 批量修改员工客资缓存记录
     *
     * @param kzIdArr
     * @param info
     */
    void batchEditTemp(@Param("kzIdArr") String[] kzIdArr, @Param("info") ClientExcelNewsDTO info);

    /**
     * 设置状态ID和classId
     *
     * @param operaId
     */
    void updateStatusIdAndClassId(@Param("operaId") int operaId, @Param("companyId") Integer companyId);

    /**
     * 获取错误客资
     *
     * @param staffId
     * @return
     */
    List<ClientExcelNewsDTO> getExcelErrorClient(@Param("staffId") int staffId);


    /**
     * 获取正常客资
     *
     * @param staffId
     * @return
     */
    List<ClientExcelNewsDTO> getExcelSuccessClient(@Param("staffId") int staffId);


    /**
     * 获取可知数量
     */
    ClientSortCountDTO getMultipleKzStatusCount(@Param("staffId") int staffId);

    /**
     * 更新咨询方式的字典编码
     *
     * @param staffId
     * @param dictionaryType
     */
    void updateZxStyleDictionaryCode(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param(value = "dictionaryType") String dictionaryType);


    /**
     * 更新意向等级的字典编码
     *
     * @param staffId
     * @param dictionaryType
     */
    void updateYxLevelDictionaryCode(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param(value = "dictionaryType") String dictionaryType);

    /**
     * 更新预算范围的字典编码
     *
     * @param staffId
     * @param dictionaryType
     */
    void updateYsRangeDictionaryCode(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param(value = "dictionaryType") String dictionaryType);


    /**
     * 更新预拍时间的字典编码
     *
     * @param staffId
     * @param dictionaryType
     */
    void updateYpTimeDictionaryCode(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param(value = "dictionaryType") String dictionaryType);


    /**
     * 更新结婚时间的字典编码
     *
     * @param staffId
     * @param dictionaryType
     */
    void updateMarryTimeDictionaryCode(@Param("staffId") Integer staffId, @Param("companyId") Integer companyId, @Param(value = "dictionaryType") String dictionaryType);

}
