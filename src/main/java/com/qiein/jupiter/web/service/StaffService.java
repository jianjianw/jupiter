package com.qiein.jupiter.web.service;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工
 */
public interface StaffService {

    /**
     * 员工新增
     *
     * @param
     * @return
     */
    StaffVO insert(StaffVO staffVO);

    /**
     * 设置员工锁定状态
     *
     * @param id
     * @param lockFlag
     */
    StaffPO setLockState(int id, int companyId, boolean lockFlag);


    /**
     * 设置在线状态
     *
     * @param id
     * @param showFlag
     */
    StaffPO setOnlineState(int id, int companyId, int showFlag);

    /**
     * 员工删除（物理删除）
     *
     * @param
     * @return
     */
    int delete(int id, int companyId);

    /**
     * 员工删除(逻辑删除)
     *
     * @param id
     */
    int logicDelete(int id, int companyId);

    /**
     * 修改
     *
     * @param staffPO
     * @return
     */
    StaffPO update(StaffPO staffPO);

    /**
     * 根据ID获取员工
     *
     * @return
     */
    StaffPO getById(int id, int companyId);

    /**
     * 根据条件查询
     *
     * @param queryMapDTO 查询条件
     * @return
     */
    PageInfo findList(QueryMapDTO queryMapDTO);

    /**
     * 获取员工所在的所有的公司集合
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户所在公司集合
     */
    List<CompanyPO> getCompanyList(String userName, String password);

    /**
     * 根据用户名及密码和公司Id进行登录
     *
     * @param userName
     * @param password
     * @param companyId
     * @return
     */
    StaffPO loginWithCompanyId(String userName, String password, int companyId);

    /**
     * 心跳更新
     */
    void heartBeatUpdate(int id, int companyId);

    /**
     * 获取小组人员
     */
    public List<StaffVO> getGroupStaffs(int companyId, String groupId);
}
