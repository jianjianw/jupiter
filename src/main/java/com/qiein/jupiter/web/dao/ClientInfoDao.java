package com.qiein.jupiter.web.dao;

import org.apache.ibatis.annotations.Param;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;

/**
 * 客资
 *
 * @author JingChenglong 2018/05/08 16:00
 */
public interface ClientInfoDao extends BaseDao<ClientInfoDao> {

    /**
     * 根据名字类型，获取来源信息
     *
     * @param companyId
     * @param srcName
     * @param typeId
     * @return
     */
    ClientPushDTO getClientPushDTOById(@Param("kzId") String kzId, @Param("infoTabName") String infoTabName);

    /**
     * 客资分配给客服后修改客资信息
     *
     * @param companyId
     * @param infoTabName
     * @param kzId
     * @param classId
     * @param statusId
     * @param appointorId
     * @param groupId
     * @return
     */
    int updateClientInfoWhenAllot(@Param("companyId") int companyId, @Param("infoTabName") String infoTabName,
                                  @Param("kzId") String kzId, @Param("classId") int classId, @Param("statusId") int statusId,
                                  @Param("appointorId") int appointorId, @Param("groupId") String groupId, @Param("allotType") int allotType);

    /**
     * 客资分配客服后修改客资详情
     *
     * @param companyId
     * @param detailTabName
     * @param kzId
     * @param appointorName
     * @param groupName
     * @return
     */
    int updateClientDetailWhenAllot(@Param("companyId") int companyId,
                                    @Param("detailTabName") String detailTabName, @Param("kzId") String kzId,
                                    @Param("appointorName") String appointorName, @Param("groupName") String groupName);

    /**
     * 客资分配后修改客资的领取时间和最后操作时间
     *
     * @param companyId
     * @param infoTabName
     * @param kzId
     * @return
     */
    int updateClientInfoAfterAllot(@Param("companyId") int companyId, @Param("infoTabName") String infoTabName,
                                   @Param("kzId") String kzId);

    /**
     * 修改客资状态
     *
     * @param companyId
     * @param infoTabName
     * @param kzId
     * @param classId
     * @param statusId
     * @return
     */
    int updateClientInfoStatus(@Param("companyId") int companyId, @Param("infoTabName") String infoTabName,
                               @Param("kzId") String kzId, @Param("classId") int classId, @Param("statusId") int statusId);
}