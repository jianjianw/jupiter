package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.dao.StaffMarsDao;
import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.vo.StaffDetailVO;
import com.qiein.jupiter.web.service.StaffMarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */
@Service
public class StaffMarsServiceImpl implements StaffMarsService {

    @Autowired
    private StaffMarsDao staffMarsDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private ChannelDao channelDao;

    /**
     * 编辑网销排班员工
     * @param staffMarsDTO
     */
    @Override
    public void editStaffMars(StaffMarsDTO staffMarsDTO) {
        if (staffMarsDTO.getLimitShopIds()!=null){   //改不接单的拍摄地则同时修改拍摄地名称
            List<String> list = shopDao.getLimitShopNamesByIds(staffMarsDTO.getLimitShopIds().split(","),staffMarsDTO.getCompanyId());
            String limitShopNames ="";
            for (String s:list)limitShopNames += s+",";
            staffMarsDTO.setLimitShopNames(limitShopNames.substring(0,limitShopNames.length()-1));
        }

        if(staffMarsDTO.getLimitChannelIds()!=null){    //  修改不接单的渠道同时修改渠道名称
            List<String> list = channelDao.getChannelNamesByIds(staffMarsDTO.getCompanyId(),staffMarsDTO.getLimitChannelIds().split(","));
            String limitChannelNames = "";
            for (String s:list)limitChannelNames+= s+",";
            staffMarsDTO.setLimitChannelNames(limitChannelNames.substring(0,limitChannelNames.length()-1));
        }

        //员工详情
        StaffDetailVO staff=staffDao.getStaffDetail(staffMarsDTO.getId(),staffMarsDTO.getCompanyId());
        if (staffMarsDTO.getLimitDay()!=null){ //  改了日接单限额
            if (staff.getTodayNum()>= staffMarsDTO.getLimitDay())
                staffMarsDTO.setStatusFlag(9);
            else {
                staffMarsDTO.setStatusFlag(0);
            }
        }

        staffMarsDao.update(staffMarsDTO);
    }
}
