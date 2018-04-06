package com.qiein.jupiter.web.service.serviceImpl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RRException;
import com.qiein.jupiter.web.dao.StaffDao;
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
     * 保存员工
     *
     * @param staffPO
     * @return
     */
    @Override
    public int insert(StaffPO staffPO) {
        return 0;
    }

    /**
     * 删除员工
     *
     * @param staffPO
     * @return
     */
    @Override
    public int delete(StaffPO staffPO) {
        return 0;
    }

    /**
     * 根据id获取
     *
     * @return
     */
    @Override
    public StaffPO getById() {
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
            throw new RRException(ExceptionEnum.USER_NOT_FIND);
        }
    }

    /**
     * 登录
     *
     * @param userName  用户名
     * @param password  密码
     * @param companyId 公司id
     * @return
     */
    @Override
    public StaffPO Login(String userName, String password, int companyId) {
        //TODO 未完成
        StaffPO staff = staffDao.login(userName, password, companyId);
        if (null == staff) {
            //用户不存在
            throw new RRException(ExceptionEnum.USER_NOT_FIND);
        } else if (staff.isLockFlag()) {
            //用户已锁定
            throw new RRException(ExceptionEnum.USER_IS_LOCK);
        }
        return staff;
    }
}
