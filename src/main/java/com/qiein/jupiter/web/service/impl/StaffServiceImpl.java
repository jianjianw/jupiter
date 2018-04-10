package com.qiein.jupiter.web.service.impl;

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

import java.util.List;
import java.util.Map;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffDao staffDao;

    /**
     * 员工新增
     *
     * @param staffPO
     * @return
     */
    @Override
    public StaffPO insert(StaffPO staffPO) {
        staffDao.insert(staffPO);
        return staffPO;
    }

    /**
     * 员工锁定与解锁
     *
     * @param id
     * @param companyId
     * @param lockFlag
     */
    @Override
    public int setLockState(int id, int companyId, boolean lockFlag) {
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setLockFlag(lockFlag);
        return staffDao.update(staffPO);
    }

    /**
     * 设置在线状态
     *
     * @param id
     * @param companyId
     * @param showFlag
     * @return
     */
    @Override
    public int setOnlineState(int id, int companyId, int showFlag) {
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setShowFlag(showFlag);
        return staffDao.update(staffPO);
    }

    /**
     * 物理删除
     *
     * @param id
     * @param companyId
     */
    @Override
    public int delete(int id, int companyId) {
        return staffDao.deleteByIdAndCid(id, companyId);
    }

    /**
     * 逻辑删除，即设置isdel 1
     *
     * @param id
     * @param companyId
     */
    @Override
    public int logicDelete(int id, int companyId) {
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setDelFlag(true);
        return staffDao.update(staffPO);
    }

    /**
     * 更新
     *
     * @param staffPO
     * @return
     */
    @Override
    public int update(StaffPO staffPO) {
        return staffDao.update(staffPO);
    }

    /**
     * 根据Id获取
     *
     * @param id
     * @param companyId
     * @return
     */
    @Override
    public StaffPO getById(int id, int companyId) {
        return staffDao.getByIdAndCid(id, companyId);
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
            //锁定
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        } else if (staffPO.isDelFlag()) {
            //删除
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
    public int updateToken(StaffPO staffPO) {
        StaffPO tokenStaff = new StaffPO();
        tokenStaff.setId(staffPO.getId());
        tokenStaff.setToken(staffPO.getToken());
        tokenStaff.setCompanyId(staffPO.getCompanyId());
        return staffDao.update(tokenStaff);
    }


}
