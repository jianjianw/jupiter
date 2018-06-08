package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.IpWhiteDao;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.IpWhiteStaffVO;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.IpWhiteService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ip白名单
 *
 * @author XiangLiang 2018/05/16
 **/
@Service
public class IpWhiteServiceImpl implements IpWhiteService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IpWhiteDao ipwhitedao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private StaffService staffService;

    /**
     * 新增
     *
     * @param ipWhitePo
     * @return
     */
    @Override
    public void insert(IpWhitePO ipWhitePo) {
        ipwhitedao.insert(ipWhitePo);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public void delete(int id) {
        ipwhitedao.delete(id);
    }

    /**
     * 修改
     *
     * @param ipWhitePo
     * @return
     */
    @Override
    public void update(IpWhitePO ipWhitePo) {
        ipwhitedao.update(ipWhitePo);
    }

    /**
     * 显示页面
     *
     * @param companyId
     * @return List<IpWhitePO>
     */
    @Override
    public List<IpWhitePO> getAllIpByCompanyId(int companyId) {
        return ipwhitedao.getAllIpByCompanyId(companyId);
    }

    /**
     * 显示ip页面
     *
     * @param companyId
     * @return List<IpWhiteStaffVO>
     */
    @Override
    public PageInfo<IpWhiteStaffVO> findIpWhite(QueryMapDTO queryMapDTO, int companyId) {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<IpWhiteStaffVO> list = ipwhitedao.findIpWhite(companyId);
        return new PageInfo<>(list);
    }

    /**
     * 根据公司id 寻找白名单ip
     *
     * @param companyId
     */
    @Override
    public List<String> findIp(int companyId) {
        return ipwhitedao.findIp(companyId);
    }

    /**
     * 校验IP限制
     */
    @Override
    public boolean checkIpLimit(int staffId, int companyId, String ip) {
        if (StringUtil.isEmpty(ip)) {
            log.error("未获取到IP！！");
            return true;
        }
        //校验IP,当企业开启了IP校验功能
        if (companyService.getById(companyId).isIpLimit()) {
            StaffPO StaffPOById = staffService.getById(staffId, companyId);
            //是IP白名单用户
            if (StaffPOById.isWhiteFlag()) {
                return true;
            }
            //判断是否在安全IP内
            List<String> ips = findIp(companyId);
            for (String sip : ips) {
                if (sip.endsWith(".0")) {
                    //去掉最后.0
                    sip = sip.substring(0, sip.length() - 2);
                }
                if (ip.startsWith(sip)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }


}
