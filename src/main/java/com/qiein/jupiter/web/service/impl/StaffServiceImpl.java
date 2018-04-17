package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.aop.annotation.LoginLog;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.NumberConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.JwtUtil;
import com.qiein.jupiter.util.ListUtil;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.GroupStaffDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.dao.StaffRoleDao;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.StaffRolePO;
import com.qiein.jupiter.web.entity.vo.StaffBaseInfoVO;
import com.qiein.jupiter.web.entity.vo.StaffPermissionVO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private GroupDao groupDao;//小组部门持久层

    @Autowired
    private StaffRoleDao staffRoleDao;//人员角色关联持久层

    @Autowired
    private GroupStaffDao groupStaffDao;

    /**
     * 员工新增
     *
     * @param staffVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StaffVO insert(StaffVO staffVO) {
        log.debug("未使用缓存");
        //加密码加密,密码为空则默认手机号
        staffVO.setPassword(MD5Util.getSaltMd5(StringUtil.isNullStr(staffVO.getPassword()) ? staffVO.getPhone() : staffVO.getPassword()));
        //1.根据手机号，全名，艺名查重，手机号全公司不重复，全名，艺名，在职员工中不能重复
        StaffPO phoneExist = staffDao.getStaffByPhone(staffVO.getCompanyId(), staffVO.getPhone());
        if (phoneExist != null && phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.STAFF_EXIST_DEL);
        }
        if (phoneExist != null && !phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.PHONE_EXIST);
        }
        //艺名查重
        if (staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getNickName()) != null) {
            throw new RException(ExceptionEnum.NICKNAME_EXIST);
        }
        //全名查重
        if (staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getUserName()) != null) {
            throw new RException(ExceptionEnum.USERNAME_EXIST);
        }
        //2.插入人员
        staffDao.addStaffVo(staffVO);
        //3.添加小组人员关联表
        groupStaffDao.insertGroupStaff(staffVO.getCompanyId(), staffVO.getGroupId(), staffVO.getId());
        //4.添加人员角色关联表
        if (StringUtil.isNotNullStr(staffVO.getRoleIds())) {
            String[] roleArr = staffVO.getRoleIds().split(CommonConstant.STR_SEPARATOR);
            staffRoleDao.batchInsertStaffRole(staffVO.getId(), staffVO.getCompanyId(), roleArr);
        }
        redisTemplate.opsForValue().set(RedisConstant.getStaffKey(staffVO.getId(), staffVO.getCompanyId()), staffVO);
        return staffVO;
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
        //TODO 删除前验证各种  如员工是否有客资等情况
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
        //TODO 删除前验证各种  如员工是否有客资等情况
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setDelFlag(true);
        return staffDao.update(staffPO);
    }

    /**
     * 更新
     *
     * @param staffVO
     * @return
     */
    @CachePut(value = "staff", key = "'staff'+':'+#staffVO.id+':'+#staffVO.companyId")
    @Transactional(rollbackFor = Exception.class)
    public StaffVO update(StaffVO staffVO) {
        log.debug("未使用缓存");
        //加密码加密,密码为空则默认手机号
        if (StringUtil.isNotNullStr(staffVO.getPassword())) {
            staffVO.setPassword(MD5Util.getSaltMd5(staffVO.getPassword()));
        }
        //1.根据手机号，全名，艺名查重，手机号全公司不重复，全名，艺名，在职员工中不能重复
        StaffPO phoneExist = staffDao.getStaffByPhone(staffVO.getCompanyId(), staffVO.getPhone());
        if (phoneExist != null && phoneExist.getId() != staffVO.getId() && phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.STAFF_EXIST_DEL);
        }
        if (phoneExist != null && phoneExist.getId() != staffVO.getId() && !phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.PHONE_EXIST);
        }
        //艺名查重
        StaffPO nickNameStaff = staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getNickName());
        if (nickNameStaff != null && nickNameStaff.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.NICKNAME_EXIST);
        }
        //全名查重
        StaffPO userNameStaff = staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getUserName());
        if (userNameStaff != null && userNameStaff.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.USERNAME_EXIST);
        }
        //2.修改员工信息
        staffDao.updateStaff(staffVO);
        //3.更新小组关联表
        groupStaffDao.deleteByStaffId(staffVO.getCompanyId(), staffVO.getId());
        groupStaffDao.insertGroupStaff(staffVO.getCompanyId(), staffVO.getGroupId(), staffVO.getId());
        //4.删除权限关联表
        staffRoleDao.deleteByStaffId(staffVO.getId(), staffVO.getCompanyId());
        //5.插入人员角色关联表
        if (StringUtil.isNotNullStr(staffVO.getRoleIds())) {
            String[] roleArr = staffVO.getRoleIds().split(CommonConstant.STR_SEPARATOR);
            staffRoleDao.batchInsertStaffRole(staffVO.getId(), staffVO.getCompanyId(), roleArr);
        }
        //6.清缓存
        // redisTemplate.opsForValue().getAndSet(RedisConstant.getStaffKey(staffVO.getId(), staffVO.getCompanyId()), staffVO);
        return staffVO;
    }

    /**
     * 根据Id获取员工基础信息
     *
     * @param id
     * @param companyId
     * @return
     */
    @Cacheable(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO getById(int id, int companyId) {
        log.debug("未使用缓存");
        //TODO  获取详细的一些信息 还是 单一的信息？
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
        List<CompanyPO> companyList = staffDao.getCompanyList(userName, MD5Util.getSaltMd5(password));
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
    //@Cacheable(value = "groupStaff", key = "'groupStaff'+':'+#companyId+':'+#groupId")
    public List<StaffVO> getGroupStaffs(int companyId, String groupId) {
        List<StaffVO> list = groupStaffDao.getGroupStaffs(companyId, groupId);
        if (ListUtil.isNotNullList(list)) {
            for (StaffVO vo : list) {
                vo.getStaffPwdFlag();
            }
        }
        return list;
    }

    /**
     * 批量编辑员工信息
     *
     * @param companyId
     * @param staffIds
     * @param roleIds
     * @param password
     * @param groupId
     */
    public void batchEditStaff(int companyId, String staffIds, String roleIds, String password, String groupId) {
        String[] staffIdArr = staffIds.split(CommonConstant.STR_SEPARATOR);
        //1.修改密码
        if (StringUtil.isNotNullStr(password)) {
            staffDao.batchEditStaffPwd(companyId, staffIdArr, MD5Util.getMD5(password));
        }
        //2.修改小组
        groupStaffDao.batchEditStaffGroup(companyId, staffIdArr, groupId);
        //3.修改角色
        if (StringUtil.isNotNullStr(roleIds)) {
            String[] roleIdArr = roleIds.split(CommonConstant.STR_SEPARATOR);
            staffRoleDao.batchDeleteByStaffIdArr(companyId, staffIdArr);
            List<StaffVO> list = new LinkedList<>();
            for (String staffId : staffIdArr) {
                for (String roleId : roleIdArr) {
                    StaffVO vo = new StaffVO(Integer.parseInt(staffId), companyId, Integer.parseInt(roleId));
                    list.add(vo);
                }
            }
            staffRoleDao.batchInsertStaffRoleByVO(list);
        }
        //4.TODO 清缓存

    }

    /**
     * 搜索员工
     *
     * @param companyId
     * @param searchKey
     * @return
     */
    public List<StaffVO> getStaffListBySearchKey(int companyId, String searchKey) {
        return staffDao.getStaffListBySearchKey(companyId, searchKey);
    }

    /**
     * 获取员工权限信息
     *
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public StaffPermissionVO getStaffPermissionById(int staffId, int companyId) {
        return staffRoleDao.getStaffPermission(staffId, companyId);
    }

    /**
     * 获取员工的基础信息
     *
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public StaffBaseInfoVO getStaffBaseInfo(int staffId, int companyId) {
        StaffBaseInfoVO staffBaseInfoVO = new StaffBaseInfoVO();
        //员工对象
        StaffPermissionVO staffPermission = staffRoleDao.getStaffPermission(staffId, companyId);
        staffBaseInfoVO.setStaffPermissionVO(staffPermission);
        //遍历权限集合生成Map
        Map<String, Integer> permissionMap = new HashMap<>();
        if (staffPermission != null && !ListUtil.isNullList(staffPermission.getPermissionList())) {
            for (PermissionPO permissionPO : staffPermission.getPermissionList()) {
                permissionMap.put(permissionPO.getAbbreviate(), permissionPO.getPermissionId());
            }
        }
        //放入权限对象
        staffBaseInfoVO.setPermissionMap(permissionMap);
        //放入公司对象
        staffBaseInfoVO.setCompanyPO(companyService.getById(companyId));
        return staffBaseInfoVO;
    }
}
