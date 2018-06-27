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
     * @param tabName
     * @param clientStatusVoteVO
     * */
    void updateKzValidStatusByKzId(@Param("tabName") String tabName,@Param(value="clientStatusVoteVO") ClientStatusVoteVO clientStatusVoteVO);

    /**
     * 客资被转移时统计被转移人的客资数
     *
     * @param tabName
     * @param kzIds
     * @param type
     * @param companyId
     * @return
     */
    List<StaffNumVO> getOnwerInfoNumByIds(@Param("tabName") String tabName, @Param("kzIds") String kzIds,
                                          @Param("type") String type, @Param("companyId") Integer companyId);
    /**
     * 寻找 kz的主id
     * @param kzId
     * @return
     */
    Integer findId(@Param("kzId") String kzId,@Param("companyId") Integer companyId);


}
