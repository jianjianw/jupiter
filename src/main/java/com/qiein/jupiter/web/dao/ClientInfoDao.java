
package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
import com.qiein.jupiter.web.entity.dto.ClientSourceDTO;
import com.qiein.jupiter.web.entity.po.EditClientPhonePO;
import com.qiein.jupiter.web.entity.po.RepateKzLogPO;
import com.qiein.jupiter.web.entity.po.WechatScanPO;
import com.qiein.jupiter.web.entity.vo.StaffChangeVO;

import java.util.List;

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
    ClientPushDTO getClientPushDTOById(@Param("kzId") String kzId, @Param("infoTabName") String infoTabName,
                                       @Param("detailTabName") String detailTabName);

    /**
     * 客资分配给邀约客服后修改客资信息
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
    int updateClientDetailWhenAllot(@Param("companyId") int companyId, @Param("detailTabName") String detailTabName,
                                    @Param("kzId") String kzId, @Param("appointorName") String appointorName,
                                    @Param("groupName") String groupName);

    /**
     * 客资分配给门市后，修改客资信息
     *
     * @param companyId
     * @param infoTabName
     * @param kzId
     * @param classId
     * @param statusId
     * @param receptorId
     * @param allotType
     * @return
     */
    int updateClientInfoWhenAllotMsjd(@Param("companyId") int companyId, @Param("infoTabName") String infoTabName,
                                      @Param("kzId") String kzId, @Param("statusId") int statusId,
                                      @Param("shopId") int shopId, @Param("receptorId") int receptorId, @Param("allotType") int allotType);

    /**
     * 客资分配给门市后，修改客资信息详情信息
     *
     * @param companyId
     * @param detailTabName
     * @param kzId
     * @param receptorName
     * @return
     */
    int updateClientDetailWhenAllotMsjd(@Param("companyId") int companyId, @Param("detailTabName") String detailTabName,
                                        @Param("kzId") String kzId, @Param("shopName") String shopName, @Param("receptorName") String receptorName);

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

    /**
     * 将未交接的客资转移到指定员工
     *
     * @param staffChangeVO
     * @return
     */
    int changeStaff(StaffChangeVO staffChangeVO);

    /**
     * 获取当前企业要推送的客资
     *
     * @param infoTabName
     * @param companyId
     * @return
     */
    List<ClientPushDTO> getInfoListBeReadyPush(@Param("infoTabName") String infoTabName,
                                               @Param("companyId") int companyId, @Param("overTime") int overTime);

    /**
     * 获取企业需要分配的筛选中的客资列表
     *
     * @param infoTabName
     * @param companyId
     * @param overTime
     * @return
     */
    List<ClientPushDTO> getSkInfoList(@Param("infoTabName") String infoTabName,
                                      @Param("companyId") int companyId, @Param("overTime") int overTime);

    /**
     * 获取goeasy推送的客资信息
     *
     * @param kzId
     * @param infoTabName
     * @param detailTabName
     * @return
     */
    ClientGoEasyDTO getClientGoEasyDTOById(@Param("kzId") String kzId, @Param("infoTabName") String infoTabName,
                                           @Param("detailTabName") String detailTabName);

    /**
     * 根据拍摄地获取客资数量
     *
     * @param infoTabName
     * @param companyId
     * @param shopId
     * @return
     */
    int getKzNumByShopId(@Param("infoTabName") String infoTabName, @Param("companyId") int companyId,
                         @Param("shopId") int shopId);

    /**
     * 根据最终拍摄地获取客资数量
     *
     * @param detailName
     * @param companyId
     * @param filmingCode
     * @return
     */
    int getKzNumByFilmingCode(@Param("detailName") String detailName, @Param("companyId") int companyId,
                              @Param("filmingCode") int filmingCode);

    /**
     * 根据客资ID集合查询客资信息
     *
     * @param kzIds
     * @param companyId
     * @param infoTabName
     * @return
     */
    List<ClientPushDTO> listClientsInStrKzids(@Param("kzIds") String kzIds, @Param("companyId") int companyId,
                                              @Param("infoTabName") String infoTabName);

    /**
     * 根据客资ID集合查询可以分配给门市的客资集合
     *
     * @param kzIds
     * @param companyId
     * @param infoTabName
     * @return
     */
    List<ClientPushDTO> listClientsInStrKzids4Msjd(@Param("kzIds") String kzIds, @Param("companyId") int companyId,
                                                   @Param("infoTabName") String infoTabName);

    /**
     * 查询已有客服的客资数量，用于分配
     *
     * @param kzIds
     * @param companyId
     * @param infoTabName
     * @return
     */
    int listExistAppointClientsNum(@Param("kzIds") String kzIds, @Param("companyId") int companyId,
                                   @Param("infoTabName") String infoTabName, @Param("type") String type);

    /**
     * 修改联系方式日志
     *
     * @param clientLogDTO
     * @return
     */
    List<EditClientPhonePO> editClientPhoneLog(ClientLogDTO clientLogDTO);

    /**
     * pc端领取客资，修改客资基本信息
     *
     * @param companyId
     * @param infoTabName
     * @param kzId
     * @param classId
     * @param statusId
     * @param appointorId
     * @param groupId
     * @param allotType
     * @return
     */
    int updateClientInfoWhenReceive(@Param("companyId") int companyId, @Param("infoTabName") String infoTabName,
                                    @Param("kzId") String kzId, @Param("classId") int classId, @Param("statusId") int statusId,
                                    @Param("appointorId") int appointorId, @Param("groupId") String groupId, @Param("allotType") int allotType);

    /**
     * 微信扫码记录
     *
     * @param clientLogDTO
     * @return
     */
    List<WechatScanPO> wechatScanCodeLog(ClientLogDTO clientLogDTO);

    /**
     * 重复客资日志
     *
     * @param clientLogDTO
     * @return
     */
    List<RepateKzLogPO> repateKzLog(ClientLogDTO clientLogDTO);

    /**
     * 获取Kzid是否存在
     */
    List<ClientPushDTO> getKzIdExists(@Param(value = "kzIds") String kzId, @Param(value = "companyId") int companyId, @Param(value = "infoTabName") String infoTabName);

    /**
     * 修改已收金额
     *
     * @param detTabName
     * @param kzId
     * @param companyId
     */
    void editStayAmount(@Param("detTabName") String detTabName, @Param("cashTabName") String cashTabName, @Param("kzId") String kzId, @Param("companyId") int companyId);

    /**
     * 分配给筛客之后，修改筛选人ID，推送时间
     *
     * @param infoTabName
     * @param promotorId
     * @param kzId
     * @param companyId
     * @return
     */
    int updateSkInfoWhenAllot(@Param("infoTabName") String infoTabName, @Param("promotorId") int promotorId, @Param("kzId") String kzId, @Param("companyId") int companyId);

    /**
     * 分配给筛客之后，修改筛选人姓名
     *
     * @param detTabName
     * @param promoterName
     * @param kzId
     * @param companyId
     * @return
     */
    int updateSkDetailWhenAllot(@Param("detTabName") String detTabName, @Param("promoterName") String promoterName, @Param("kzId") String kzId, @Param("companyId") int companyId);

    /**
     * 修改手机是否已加状态
     *
     * @param kzId
     * @param kzphoneFlag
     * @param table
     */
    void editKzphoneFlag(@Param("kzId") String kzId, @Param("kzphoneFlag") Integer kzphoneFlag, @Param("table") String table);

    /**
     * 修改客资上次推送时间
     *
     * @param companyId
     * @param infoTabName
     * @param kzId
     */
    void updateLastPushTime(@Param("companyId") int companyId, @Param("infoTabName") String infoTabName, @Param("kzId") String kzId);
    /**
     * 渠道转移客资
     * @param clientSourceDTO
     */
    void changeClientSource(ClientSourceDTO clientSourceDTO);
}