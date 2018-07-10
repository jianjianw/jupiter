package com.qiein.jupiter.web.controller;

import java.util.List;

import javax.annotation.Resource;

import com.qiein.jupiter.web.entity.vo.PluginVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CompanyPluginPO;
import com.qiein.jupiter.web.entity.po.PluginPO;
import com.qiein.jupiter.web.service.PluginService;

/**
 * 插件
 * Created by Tt(叶华葳) on 2018/5/26 0026.
 */
@RestController
@RequestMapping("/plugin")
public class PluginController extends BaseController {

    @Resource
    private PluginService pluginService;

    /**
     * 获取所有插件列表
     *
     * @return
     */
    @GetMapping("/all_list")
    public ResultInfo getAllPlugin() {
        List<PluginPO> list = pluginService.getListAll();
        return ResultInfoUtil.success(list);
    }

    /**
     * 获取公司插件列表
     *
     * @return
     */
    @GetMapping("/list")
    public ResultInfo getCompanyPlugin() {
        List<PluginVO> list = pluginService.getListByCompanyId(getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(list);
    }

    /**
     * 新增插件
     *
     * @param companyPluginPO
     * @return
     */
    @PostMapping("/add")
    public ResultInfo addPlugin(@RequestBody CompanyPluginPO companyPluginPO) {
        companyPluginPO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        pluginService.addPlugin(companyPluginPO);
        return ResultInfoUtil.success();
    }

    /**
     * 删除插件
     *
     * @param pluginIds
     * @return
     */
    @GetMapping("/del")
    public ResultInfo delPlugin(@NotEmptyStr String pluginIds) {
        pluginService.delPlugin(pluginIds, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success();
    }

}
