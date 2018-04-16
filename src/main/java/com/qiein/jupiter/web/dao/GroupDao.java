package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.vo.GroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组/部门  Dao
 */
public interface GroupDao extends BaseDao<GroupPO> {

    /**
     * 根据部门名称获取
     *
     * @param groupName
     * @return
     */
    GroupPO getByName(@Param("groupName") String groupName,@Param("companyId") int companyId);

    List<GroupPO> findAllByCompanyId(int companyId);

    /*获取公司所有部门和小组*/
    public List<GroupVO> getCompanyAllDeptList(@Param("companyId") Integer companyId);

}
