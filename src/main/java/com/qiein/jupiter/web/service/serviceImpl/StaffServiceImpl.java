package com.qiein.jupiter.web.service.serviceImpl;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffDao staffDao;


    @Override
    public StaffPO insert(StaffPO staffPO) {
        return null;
    }

    @Override
    public void setLockState(int id, int companyId, boolean lockFlag) {

    }

    @Override
    public void setOnlineState(int id, int companyId, boolean showFlag) {

    }

    @Override
    public void delete(int id, int companyId) {

    }

    @Override
    public void logicDelete(int id, int companyId) {

    }

    @Override
    public StaffPO update(StaffPO staffPO) {
        return null;
    }

    @Override
    public StaffPO getById(int id, int companyId) {
        return null;
    }

    /**
     * 根据条件获取集合
     *
     * @param map 查询条件
     * @return
     */
    @Override
    public List<StaffPO> findList(final Map map) {
        try {
            return PageHelper.startPage(1, 5).doSelectPage(new ISelect() {
                @Override
                public void doSelect() {
                    staffDao.findList(map);
                }
            });
        } catch (Exception e) {
            throw new RException(ExceptionEnum.USER_NOT_FIND);
        }
    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyList(String userName, String password) {
        List<CompanyPO> companyList = staffDao.getCompanyList(userName, password);
        if (companyList == null || companyList.isEmpty()) {
            //用户不存在
            throw new RException(ExceptionEnum.USER_NOT_FIND);
        }
        return companyList;
    }

    /**
     * 根据公司id登录
     *
     * @param userName
     * @param password
     * @param companyId
     * @return
     */
    @Override
    public StaffPO loginWithCompanyId(String userName, String password, int companyId) {
        StaffPO staffPO = staffDao.loginWithCompanyId(userName, password, companyId);
        if (staffPO == null) {
            //用户不存在
            throw new RException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        } else if (staffPO.isLockFlag()) {
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        } else if (staffPO.isDelFlag()) {
            throw new RException(ExceptionEnum.USER_IS_DEL);
        }
        return staffPO;
    }

    /**
     * 更新心跳
     *
     * @param id
     * @param companyId
     */
    @Override
    public void heartBeatUpdate(int id, int companyId) {

    }

    /**
     * 更新token
     *
     * @param staffPO
     */
    @Override
    public void updateToken(StaffPO staffPO) {
        StaffPO tokenStaff = new StaffPO();
        tokenStaff.setId(staffPO.getId());
        tokenStaff.setToken(staffPO.getToken());
        tokenStaff.setCompanyId(staffPO.getCompanyId());
        staffDao.update(tokenStaff);
    }


}
