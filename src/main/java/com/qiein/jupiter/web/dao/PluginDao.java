package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.po.CompanyPluginPO;
import com.qiein.jupiter.web.entity.po.PluginPO;
import com.qiein.jupiter.web.entity.vo.PluginVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 插件Dao
 * Created by Tt
 * on 2018/5/26 0026.
 */
public interface PluginDao {
    /**
     * 获取公司插件列表
     * @paramcompanyId
     * @return
     */
    List<PluginVO> getListByCompanyId(@Param("companyId") int companyId);

    /**
     * 获取所有插件列表
     * @return
     */
    List<PluginPO> getListAll();

    /**
     * 添加插件
     * @param companyPluginPO
     * @return
     */
    Integer addPlugin(CompanyPluginPO companyPluginPO);

    /**
     * 删除插件
     * @param pluginIds
     * @param companyId
     * @return
     */
    Integer delPlugin(@Param("pluginIds") String[] pluginIds,@Param("companyId") int companyId);
}
