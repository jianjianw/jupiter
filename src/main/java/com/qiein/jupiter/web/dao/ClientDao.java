package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.msg.goeasy.ClientDTO;
import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import com.qiein.jupiter.web.entity.vo.ClientStatusVoteVO;
import com.qiein.jupiter.web.entity.vo.StaffNumVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by Tt on 2018/5/15 0015.
 */
public interface ClientDao {
    /**
     * 编辑客资基本信息，如性别，微信添加状况
     *
     * @param clientStatusVO
     */
    void editClientBaseInfo(@Param("clientStatusVO") ClientStatusVO clientStatusVO, @Param("tabName") String tabName);

    /**
     * 快速添加备注标签
     *
     * @param tabName
     * @param companyId
     * @param kzId
     * @param label
     * @return
     */
    int editClientMemoLabel(@Param("tabName") String tabName, @Param("companyId") int companyId,
                            @Param("kzId") String kzId, @Param("label") String label);

    /**
     * 根据状态筛选客资数量
     *
     * @param statusId
     * @param companyId
     * @param tableName
     * @return
     */
    public int getKzNumByStatusId(@Param("statusId") int statusId, @Param("companyId") Integer companyId,
                                  @Param("tableName") String tableName);

    /**
     * 根据客资Id修改客资状态
     *
     * @param tabName
     * @param clientStatusVoteVO
     */
    void updateKzValidStatusByKzId(@Param("tabName") String tabName, @Param(value = "clientStatusVoteVO") ClientStatusVoteVO clientStatusVoteVO);

    /**
     * 客资被转移时统计被转移人的客资数
     *
     * @param tabName
     * @param kzIdArr
     * @param type
     * @param companyId
     * @return
     */
    List<StaffNumVO> getOnwerInfoNumByIds(@Param("tabName") String tabName, @Param("kzIdArr") String[] kzIdArr,
                                          @Param("type") String type, @Param("companyId") Integer companyId);

    /**
     * 寻找 kz的主id
     *
     * @param kzId
     * @return
     */
    Integer findId(@Param("kzId") String kzId, @Param("companyId") Integer companyId, @Param("tableName") String tableName);


    /**
     * 根据来源id获取客资数量
     */
    Integer getKzNumBySourceId(@Param("tableName") String tableName, @Param(value = "sourceId") int sourceId, @Param(value = "companyId") Integer companyId);


    /**
     * 修改客资渠道Id
     *
     * @param infoTabName
     * @param channelId
     * @param sourceId
     */
    void updateKzChannelId(@Param(value = "infoTabName") String infoTabName, @Param(value = "channelId") int channelId, @Param(value = "sourceId") int sourceId, @Param(value = "companyId") Integer companyId);

    /**
     * 更新客资详情表中的数据
     *
     * @param detailTabName
     * @param kzId
     * @param companyId
     * @param content
     */
    void updateDetailMemo(@Param(value = "detailTabName") String detailTabName, @Param(value = "kzId") String kzId, @Param(value = "companyId") Integer companyId, @Param(value = "content") String content,
                          @Param(value = "label") String label, @Param(value = "reason") String reason);

    /**
     * 修改客资的客服小组名称
     *
     * @param infoTabName
     * @param detailTabName
     * @param companyId
     * @param groupName
     * @param groupId
     */
    void updateGroupName(@Param(value = "infoTabName") String infoTabName, @Param(value = "detailTabName") String detailTabName,
                         @Param(value = "companyId") int companyId, @Param("groupName") String groupName, @Param("groupId") String groupId);

    /**
     * 修改客资录入人名称
     *
     * @param infoTabName
     * @param detailTabName
     * @param companyId
     * @param nickName
     * @param staffId
     */
    void updateCollectorName(@Param(value = "infoTabName") String infoTabName, @Param(value = "detailTabName") String detailTabName,
                             @Param(value = "companyId") int companyId, @Param("nickName") String nickName, @Param("staffId") int staffId);

    /**
     * 修改客资筛选人名称
     *
     * @param infoTabName
     * @param detailTabName
     * @param companyId
     * @param nickName
     * @param staffId
     */
    void updatePromoterName(@Param(value = "infoTabName") String infoTabName, @Param(value = "detailTabName") String detailTabName,
                             @Param(value = "companyId") int companyId, @Param("nickName") String nickName, @Param("staffId") int staffId);

    /**
     * 修改客资邀约人名称
     *
     * @param infoTabName
     * @param detailTabName
     * @param companyId
     * @param nickName
     * @param staffId
     */
    void updateAppointorName(@Param(value = "infoTabName") String infoTabName, @Param(value = "detailTabName") String detailTabName,
                             @Param(value = "companyId") int companyId, @Param("nickName") String nickName, @Param("staffId") int staffId);

    /**
     * 修改客资门市接待名称
     *
     * @param infoTabName
     * @param detailTabName
     * @param companyId
     * @param nickName
     * @param staffId
     */
    void updateReceptorName(@Param(value = "infoTabName") String infoTabName, @Param(value = "detailTabName") String detailTabName,
                             @Param(value = "companyId") int companyId, @Param("nickName") String nickName, @Param("staffId") int staffId);
}
