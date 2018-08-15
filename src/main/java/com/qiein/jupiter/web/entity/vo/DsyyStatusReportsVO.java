package com.qiein.jupiter.web.entity.vo;

import com.qiein.jupiter.web.entity.po.GroupReportsVO;

import java.util.List;
import java.util.Map;

/**
 * @author: yyx
 * @Date: 2018-8-11
 */
public class DsyyStatusReportsVO {
    /**
     * 小组id
     * */
    private String groupId;

    /**
     * 小组名称
     * */
    private String grouoName;

    /**
     * Map
     * */
    private List<Map<Integer,Integer>> mapList;
    /**
     * StatusReports
     * */
    private List<ClientStatusReportsVO> clientStatusReportsVOS;


    public List<Map<Integer, Integer>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<Integer, Integer>> mapList) {
        this.mapList = mapList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGrouoName() {
        return grouoName;
    }

    public void setGrouoName(String grouoName) {
        this.grouoName = grouoName;
    }

    public List<ClientStatusReportsVO> getClientStatusReportsVOS() {
        return clientStatusReportsVOS;
    }

    public void setClientStatusReportsVOS(List<ClientStatusReportsVO> clientStatusReportsVOS) {
        this.clientStatusReportsVOS = clientStatusReportsVOS;
    }
}

