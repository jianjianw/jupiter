package com.qiein.jupiter.web.entity.vo;
import java.util.Map;
public class ZjsKzOfMonthShowVO {
    private String srcName;
    private Integer srcId;
    private String srcImg;
    private Map<String,String> map;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public Integer getSrcId() {
        return srcId;
    }

    public void setSrcId(Integer srcId) {
        this.srcId = srcId;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
