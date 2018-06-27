package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.SendMsgDTO;
import org.apache.ibatis.annotations.Param;

public interface SendMsgDao {

    /**
     * 获取模板id
     * @param type
     * @param companyId
     * @return
     */
    SendMsgDTO getTemplateId(@Param("type") String type,@Param("companyId") Integer companyId);
}
