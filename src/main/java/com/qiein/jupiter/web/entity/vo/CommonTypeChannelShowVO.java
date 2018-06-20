package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.CommonTypePO;

import java.util.List;
/**
 * 拍摄类型渠道
 * Author xiangliang
 */
public class CommonTypeChannelShowVO {
    //拍摄方式渠道分配
    private List<CommonTypeChannelVO> commonTypeChannelVOList;
    //拍摄方式
    private List<CommonTypeVO>  commonTypeVOList;

    public List<CommonTypeChannelVO> getCommonTypeChannelVOList() {
        return commonTypeChannelVOList;
    }

    public void setCommonTypeChannelVOList(List<CommonTypeChannelVO> commonTypeChannelVOList) {
        this.commonTypeChannelVOList = commonTypeChannelVOList;
    }

    public List<CommonTypeVO> getCommonTypeVOList() {
        return commonTypeVOList;
    }

    public void setCommonTypeVOList(List<CommonTypeVO> commonTypeVOList) {
        this.commonTypeVOList = commonTypeVOList;
    }
}
