package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.aop.annotation.LoginLog;
import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.JwtUtil;
import com.qiein.jupiter.util.ListUtil;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.StaffVO;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 员工新增
     *
     * @param staffPO
     * @return
     */
    @CachePut(value = "staff", key = "'staff'+':'+#staffPO.id+':'+#staffPO.companyId")
    @Override
    public StaffPO insert(StaffPO staffPO) {
        log.debug("未使用缓存");
        //加密码加密
        staffPO.setPassword(MD5Util.getSaltMd5(staffPO.getPassword()));
        //TODO 数据库重复性校验
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
    @CachePut(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO setLockState(int id, int companyId, boolean lockFlag) {
        log.debug("未使用缓存");
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setLockFlag(lockFlag);
        staffDao.update(staffPO);
        return staffPO;
    }

    /**
     * 设置在线状态
     *
     * @param id
     * @param companyId
     * @param showFlag
     * @return
     */
    @CachePut(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO setOnlineState(int id, int companyId, int showFlag) {
        log.debug("未使用缓存");
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setShowFlag(showFlag);
        staffDao.update(staffPO);
        return staffPO;
    }

    /**
     * 物理删除
     *
     * @param id
     * @param companyId
     */
    @CacheEvict(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
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
    @CachePut(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
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
    @CachePut(value = "staff", key = "'staff'+':'+#staffPO.id+':'+#staffPO.companyId")
    @Override
    public StaffPO update(StaffPO staffPO) {
        log.debug("未使用缓存");
        staffDao.update(staffPO);
        return staffPO;
    }

    /**
     * 根据Id获取
     *
     * @param id
     * @param companyId
     * @return
     */
    @Cacheable(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO getById(int id, int companyId) {
        log.debug("未使用缓存");
        return staffDao.getByIdAndCid(id, companyId);
    }

    /**
     * 根据条件获取集合
     *
     * @param queryMapDTO 查询条件
     * @return
     */
    @Override
    public PageInfo findList(QueryMapDTO queryMapDTO) {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<StaffPO> list = staffDao.findList(queryMapDTO.getCondition());
        return new PageInfo<>(list);
    }

    /**
     * 登录时获取员工公司集合
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
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        }
        //移除错误次数
        removeUserErrorNumber(userName);
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
    @LoginLog
    public StaffPO loginWithCompanyId(String userName, String password, int companyId) {
        //加密码加密
        password = MD5Util.getSaltMd5(password);
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
        //验证公司属性
        CompanyPO companyPO = companyService.getById(staffPO.getCompanyId());
        //如果公司开启单地点登录,则不生成新token
        if (companyPO.isSsoLimit()) {
            //如果员工没有token，重新生成
            if (StringUtil.isNullStr(staffPO.getToken())) {
                updateStaffToken(staffPO);
            }
        } else {
            updateStaffToken(staffPO);
        }
        //移除错误次数
        removeUserErrorNumber(userName);
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
     * 删除缓存中的用户错误次数和验证码
     *
     * @param phone
     */
    private void removeUserErrorNumber(String phone) {
        //错误次数
        String userLoginErrNum = RedisConstant.getUserLoginErrNumKey(phone);
        redisTemplate.delete(userLoginErrNum);
        //验证码
        redisTemplate.delete(RedisConstant.getVerifyCodeKey(phone));
    }


    /**
     * 将更新的staff放入redis 并更新到数据库
     *
     * @param staffPO
     */
    private void updateStaffToken(StaffPO staffPO) {
        //生成token
        String token = JwtUtil.generatorToken();
        staffPO.setToken(token);
        redisTemplate.opsForValue().set(
                RedisConstant.getStaffKey(staffPO.getId(), staffPO.getCompanyId()),
                staffPO, NumberConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        //并更新到数据库
        staffDao.update(staffPO);
    }

    /**
     * 生成Jwt
     *
     * @param staffPO
     * @return
     */
    private String executeJwtToken(StaffPO staffPO) {
        StaffPO jwtStaff = new StaffPO();
        jwtStaff.setId(staffPO.getId());
        jwtStaff.setCompanyId(staffPO.getCompanyId());
        jwtStaff.setPhone(staffPO.getPhone());
        return JwtUtil.encrypt(JSONObject.toJSONString(jwtStaff));
    }

    /**
     * 获取小组人员
     */
    @Cacheable(value = "groupStaff", key = "'groupStaff'+':'+#companyId+':'+#groupId")
    public List<StaffVO> getGroupStaffs(int companyId, String groupId) {
        List<StaffVO> list = staffDao.getGroupStaffs(companyId, groupId);
        if (ListUtil.isNotNullList(list)) {
            for (StaffVO vo : list) {
                vo.getStaffPwdFlag();
            }
        }
        return list;
    }

}
