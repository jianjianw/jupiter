package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.web.dao.SendMsgDao;
import com.qiein.jupiter.web.entity.dto.SendMsgDTO;
import com.qiein.jupiter.web.service.SendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMsgServiceImpl implements SendMsgService{
    @Autowired
    private SendMsgDao sendMsgDao;
    /**
     * 获取模板id
     * @param type
     * @param companyId
     * @return
     */
   public String getTemplateId(String type,Integer companyId){
       SendMsgDTO sendMsgDTO=sendMsgDao.getTemplateId(type,companyId);
       return sendMsgDTO.getTemplateId();
   }
}
