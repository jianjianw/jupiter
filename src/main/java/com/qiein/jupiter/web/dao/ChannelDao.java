package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.ChannelPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道dao
 * Created by Administrator on 2018/4/19 0019.
 */
public interface ChannelDao  extends BaseDao<ChannelPO> {
    /**
     * 检查所属公司下是否有重复渠道组，
     * 返回值为同名的渠道组数，
     * 大于0则说明存在多个同名渠道，
     * 小于0则说明不存在同名渠道
     *
     * @param grpName   需要检查的渠道组名
     * @param companyId 所属公司编号
     * @return
     */
    Integer checkChannel(@Param("grpName") String grpName, @Param("companyId") Integer companyId);

    /**
     * 获取渠道列表
     * @param companyId
     * @param typeIds
     * @return
     */
    List<ChannelPO> getChannelList(@Param("companyId") Integer companyId,@Param("typeIds") List<Integer> typeIds);

    List<ChannelPO> getListByType(@Param("companyId") Integer companyId,@Param("typeId") Integer typeId);
}
