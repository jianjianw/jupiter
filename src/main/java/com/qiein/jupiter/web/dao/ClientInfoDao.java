package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.ClientLogDTO;
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
                                      @Param("kzId") String kzId, @Param("classId") int classId, @Param("statusId") int statusId,
                                      @Param("receptorId") int receptorId, @Param("allotType") int allotType);

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
                                        @Param("kzId") String kzId, @Param("receptorName") String receptorName);

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
                                               @Param("companyId") int companyId, @Param("interval") int interval);

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

    List<WechatScanPO> wechatScanCodeLog(ClientLogDTO clientLogDTO);
    List<RepateKzLogPO> repateKzLog(ClientLogDTO clientLogDTO);

    /**
     * 获取Kzid是否存在
     * */
    List<ClientPushDTO> getKzIdExists(@Param(value="kzIds") String kzId,@Param(value="companyId") int companyId,@Param(value="infoTabName") String infoTabName);
}