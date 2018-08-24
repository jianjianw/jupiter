package com.qiein.jupiter.web.entity.vo;
import java.util.Map;
/**
 * 无效原因报表数据
 * author xiangliang
 */
public class InvalidReasonReportsShowVO {
    private String srcName;
    private String srcImg;
    private Integer srcId;
    private Map<String,Integer> map;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
