package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.dao.WxmpClientDao;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.WxmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 小程序
 *
 * @Author: shiTao
 */
@Service
public class WxmpServiceImpl implements WxmpService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private WxmpClientDao wxmpClientDao;

    @Override
    public PageInfo<ClientVO> searchClient(int companyId, String searchKey, int page, int size) {
        PageHelper.startPage(page, size);
        List<ClientVO> searchList = wxmpClientDao.searchClient(  companyId, searchKey);
        return new PageInfo<> (searchList);
    }



    private ClientVO getClientInfoFromResult(ResultSet resultSet) throws SQLException {
        ClientVO clientVO = new ClientVO();
        clientVO.setId(resultSet.getInt("id"));
        clientVO.setKzId(resultSet.getString(""));
        clientVO.setTypeId(resultSet.getInt(""));
        clientVO.setStatusId(resultSet.getInt(""));
        clientVO.setKzName(resultSet.getString(""));
        clientVO.setKzPhone(resultSet.getString(""));
        clientVO.setKzQq(resultSet.getString(""));
        clientVO.setKzWechat(resultSet.getString(""));
        clientVO.setKzWw(resultSet.getString(""));
        clientVO.setAppointName(resultSet.getString(""));
        clientVO.setCreateTime(resultSet.getString(""));
        clientVO.setStatusId(resultSet.getInt(""));
        clientVO.setSourceId(resultSet.getInt(""));
        clientVO.setChannelId(resultSet.getInt(""));
        clientVO.setCollectorName(resultSet.getString(""));
        clientVO.setShopName(resultSet.getString(""));
        clientVO.setYxLevel(resultSet.getInt(""));
        return clientVO;
    }
}
