package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CommonTypePO;

import java.util.List;
/**
 * 拍摄类型渠道
 * Author xiangliang
 */
public class CommonTypeChannelVO {
    //拍摄方式渠道分配
    private List<CommonTypePO> commonTypePOList;
    //拍摄方式
    private List<CommonTypeVO>  commonTypeVOList;

    public List<CommonTypePO> getCommonTypePOList() {
        return commonTypePOList;
    }

    public void setCommonTypePOList(List<CommonTypePO> commonTypePOList) {
        this.commonTypePOList = commonTypePOList;
    }

    public List<CommonTypeVO> getCommonTypeVOList() {
        return commonTypeVOList;
    }

    public void setCommonTypeVOList(List<CommonTypeVO> commonTypeVOList) {
        this.commonTypeVOList = commonTypeVOList;
    }
}
