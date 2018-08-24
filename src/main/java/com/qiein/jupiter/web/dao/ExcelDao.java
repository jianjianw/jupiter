package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
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
    void deleteTempByStaffId(@Param("tempName") String tempName, @Param("operaId") int operaId);

    /**
     * 批量写入客资临时缓存表
     */
    int insertExcelClientInfo(@Param("list") List<ClientExcelNewsDTO> list, @Param("tempName") String tempName);

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
    void updateSrcIdAndType(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId);

    /**
     * 设置渠道和来源信息
     *
     * @param tempName
     * @param staffId
     */
    void updateSrcAndChannel(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId);

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
    void updateShopId(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId);

    /**
     * 更新邀约小组ID
     **/
    void updateGroupId(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId);

    /**
     * 更新门市id
     *
     * @param tempName
     * @param staffId
     */
    void updateReceptorId(@Param(value = "tempName") String tempName,
                          @Param(value = "staffId") int staffId,
                            @Param("companyId")Integer companyId);

    /**
     * 获取所有的记录
     **/
    List<ClientExcelNewsDTO> getAllRecordByStaffId(@Param("tempName") String tempName, @Param("staffId") int staffId);


    /**
     * 获取与info表重复的记录
     **/
    List<ClientExcelNewsDTO> getRepeatRecord(@Param("tempName") String tempName, @Param("tableName") String tableName,
                                             @Param("staffId") int staffId);

    /**
     * 获取Excel重复的记录
     **/
    List<ClientExcelNewsDTO> getExcelRepeatRecord(@Param("tempName") String tempName, @Param("staffId") int staffId);

    /**
     * 添加客资基础信息ByStaffId
     */
    void insertBaseInfoByStaffId(@Param("tabName") String tabName, @Param("tempName") String tempName, @Param("tableName") String tableName, @Param("staffId") Integer staffId);

    /**
     * 添加客资详细信息 ByStaffId
     */
    void insertDetailInfoByStaffId(@Param("tabName") String tabName, @Param("tempName") String tempName, @Param("tableName") String tableName, @Param("staffId") Integer staffId);

    /**
     * 插入客资备注表
     *
     * @param tabName
     * @param tempName
     * @param staffId
     */
    void addExcelKzRemark(@Param("tabName") String tabName, @Param("tempName") String tempName, @Param("staffId") Integer staffId);

    /**
     * 批量删除员工客资缓存记录
     *
     * @param tempName
     * @param kzIdArr
     * @param operaId
     */
    void batchDeleteTemp(@Param("tempName") String tempName, @Param("kzIdArr") String[] kzIdArr, @Param("operaId") int operaId);

    /**
     * 批量修改员工客资缓存记录
     *
     * @param tempName
     * @param kzIdArr
     * @param info
     */
    void batchEditTemp(@Param("tempName") String tempName, @Param("kzIdArr") String[] kzIdArr, @Param("info") ClientExcelNewsDTO info);

    /**
     * 设置状态ID和classId
     *
     * @param tempName
     * @param operaId
     */
    void updateStatusIdAndClassId(@Param("tempName") String tempName, @Param("operaId") int operaId,@Param("companyId")Integer companyId);

    /**
     * 获取错误客资
     *
     * @param tempName
     * @param staffId
     * @return
     */
    List<ClientExcelNewsDTO> getExcelErrorClient(@Param("tempName") String tempName, @Param("staffId") int staffId);


    /**
     * 获取正常客资
     *
     * @param tempName
     * @param staffId
     * @return
     */
    List<ClientExcelNewsDTO> getExcelSuccessClient(@Param("tempName") String tempName, @Param("tableName") String tableName,
                                                   @Param("staffId") int staffId);


    /**
     * 获取可知数量
     */
    ClientSortCountDTO getMultipleKzStatusCount(@Param("tempName") String tempName, @Param("tableName") String tableName,
                                                @Param("staffId") int staffId);

    /**
     * 更新咨询方式的字典编码
     *
     * @param tempName
     * @param staffId
     * @param dictionaryType
     */
    void updateZxStyleDictionaryCode(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId, @Param(value = "dictionaryType") String dictionaryType);


    /**
     * 更新意向等级的字典编码
     *
     * @param tempName
     * @param staffId
     * @param dictionaryType
     */
    void updateYxLevelDictionaryCode(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId, @Param(value = "dictionaryType") String dictionaryType);

    /**
     * 更新预算范围的字典编码
     *
     * @param tempName
     * @param staffId
     * @param dictionaryType
     */
    void updateYsRangeDictionaryCode(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId, @Param(value = "dictionaryType") String dictionaryType);


    /**
     * 更新预拍时间的字典编码
     *
     * @param tempName
     * @param staffId
     * @param dictionaryType
     */
    void updateYpTimeDictionaryCode(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId, @Param(value = "dictionaryType") String dictionaryType);


    /**
     * 更新结婚时间的字典编码
     * @param tempName
     * @param staffId
     * @param dictionaryType
     * */
    void updateMarryTimeDictionaryCode(@Param("tempName") String tempName, @Param("staffId") Integer staffId,@Param("companyId")Integer companyId, @Param(value = "dictionaryType") String dictionaryType);

    /**
     * 批量添加客资日志
     * @param infoLogTabName
     * @param staffId
     * @param nickName
     * @param logType
     * */
    void batchAddInfoLog(@Param("logTabName") String infoLogTabName,@Param(value="tempName") String tempName,@Param(value="tabName")String tabName,@Param("staffId") int staffId,@Param(value="nickName")String nickName,@Param(value="logType") Integer logType,@Param(value="memo")String memo);
}
