package com.qiein.jupiter.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.qiein.jupiter.enums.StaffStatusEnum;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.PageDictDTO;
import com.qiein.jupiter.web.entity.dto.StaffMarsDTO;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.SourceService;
import com.qiein.jupiter.web.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.aop.aspect.annotation.LoginLog;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.dto.StaffPasswordDTO;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private ClientInfoDao clientInfoDao;

    @Autowired
    private StaffStatusLogDao staffStatusLogDao;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private StatusService statusService;

    /**
     * 员工新增
     *
     * @param staffVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public StaffVO insert(StaffVO staffVO) {
        log.debug("未使用缓存");
        // 加密码加密,密码为空则默认手机号
        staffVO.setPassword(MD5Util
                .getSaltMd5(StringUtil.isEmpty(staffVO.getPassword()) ? staffVO.getPhone() : staffVO.getPassword()));
        // 1.根据手机号，全名，艺名查重，手机号全公司不重复，全名，艺名，在职员工中不能重复
        StaffPO phoneExist = staffDao.getStaffByPhone(staffVO.getCompanyId(), staffVO.getPhone());
        if (phoneExist != null && phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.STAFF_EXIST_DEL);
        }
        if (phoneExist != null && !phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.PHONE_EXIST);
        }
        // 艺名查重
        if (staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getNickName()) != null) {
            throw new RException(ExceptionEnum.NICKNAME_EXIST);
        }
        // 全名查重
        if (staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getUserName()) != null) {
            throw new RException(ExceptionEnum.USERNAME_EXIST);
        }
        CompanyPO companyPO = companyDao.getById(staffVO.getCompanyId());
        staffVO.setLimitDay(companyPO.getLimitDefault());
        // 2.插入人员
        staffDao.addStaffVo(staffVO);
        // 3.添加小组人员关联表
        groupStaffDao.insertGroupStaff(staffVO.getCompanyId(), staffVO.getGroupId(), staffVO.getId());
        // 4.添加人员角色关联表
        if (StringUtil.isNotEmpty(staffVO.getRoleIds())) {
            String[] roleArr = staffVO.getRoleIds().split(CommonConstant.STR_SEPARATOR);
            staffRoleDao.batchInsertStaffRole(staffVO.getId(), staffVO.getCompanyId(), roleArr);
        }
        // 5.新增员工详细信息
        StaffDetailPO staffDetailPO = new StaffDetailPO();
        staffDetailPO.setId(staffVO.getId());
        staffDetailPO.setCompanyId(staffVO.getCompanyId());
        staffDao.insertStaffDetail(staffDetailPO);
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
    @CacheEvict(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO setLockState(int id, int companyId, boolean lockFlag) {
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setLockFlag(lockFlag);
        staffDao.updateLockFlag(staffPO);
        return staffPO;
    }

    /**
     * 设置状态
     *
     * @param id
     * @param companyId
     * @param statusFlag
     * @return
     */
    @CacheEvict(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO updateStatusFlag(int id, int companyId, int statusFlag, Integer operatorId, String operatorName) {
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        StaffPO staffNow = staffDao.getByIdAndCid(id, companyId);
        // 当要上下线时，判断状态
        if (statusFlag == 0 || statusFlag == 1) {
            if (staffNow.getStatusFlag() == 8) {
                // 停单
                throw new RException(ExceptionEnum.STAFF_IS_STOP_RECEIPT);
            } else if (staffNow.getStatusFlag() == 9) {
                // 满限
                throw new RException(ExceptionEnum.STAFF_IS_LIMIT);
            }
        }
        staffPO.setStatusFlag(statusFlag);
        //更新状态
        staffDao.updateStatusFlag(staffPO);
        //保存日志
        //更新的员工
        StaffPO updateStaff = staffDao.getByIdAndCid(id, companyId);
        //保存
        staffStatusLogDao.insert(new StaffStatusLog(updateStaff.getId(), updateStaff.getNickName(),
                statusFlag, operatorId, operatorName, companyId));
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
        // TODO 删除前验证各种 如员工是否有客资等情况 同时删除员工的详细信息
        return staffDao.deleteByIdAndCid(id, companyId);
    }

    /**
     * 批量物理删除员工
     *
     * @param ids
     * @param companyId
     * @return
     */
    @Override
    public void batDelete(String[] ids, int companyId) {
        // TODO 删除前验证各种 如员工是否有客资等情况
        staffDao.batDelByIdsAndCid(ids, companyId);
        // TODO 删除相关连接表
    }

    /**
     * 批量删除员工，将isShow字段改为true，然后硬删除员工对应的角色和员工所属小组
     *
     * @param staffStateVO
     */
    @Override
    @Transactional
    public void batDelStaff(StaffStateVO staffStateVO) {
        //TODO 缓存
        // 修改删除标识
        staffDao.batUpdateStaffState(staffStateVO, staffStateVO.getIds().split(","));
        // 硬删除员工角色
        staffRoleDao.batchDeleteByStaffIdArr(staffStateVO.getCompanyId(), staffStateVO.getIds().split(","));
        // 硬删除员工小组
        groupStaffDao.batchDeleteByStaffArr(staffStateVO.getCompanyId(), staffStateVO.getIds().split(","));
    }

    /**
     * 批量编辑员工状态 显示、锁定、删除
     *
     * @param staffStateVO
     */
    @Override
    public void batUpdateStaffState(StaffStateVO staffStateVO) {
        staffDao.batUpdateStaffState(staffStateVO, staffStateVO.getIds().split(","));
    }

    /**
     * 批量检查员工是否可删
     *
     * @param ids
     * @param companyId
     * @return
     */
    @Override
    public String checkBatDelete(String[] ids, int companyId) {
        // TODO 检查员工是否可删除

        return "可删除";
    }

    /**
     * 逻辑删除，即设置isdel 1
     *
     * @param id
     * @param companyId
     */
    @CacheEvict(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public int logicDelete(int id, int companyId) {
        // TODO 删除前验证各种 如员工是否有客资等情况
        StaffPO staffPO = new StaffPO();
        staffPO.setId(id);
        staffPO.setCompanyId(companyId);
        staffPO.setDelFlag(true);
        return staffDao.updateDelFlag(staffPO);
    }

    /**
     * 更新
     *
     * @param staffVO
     * @return
     */
    @CacheEvict(value = "staff", key = "'staff'+':'+#staffVO.id+':'+#staffVO.companyId")
    @Transactional(rollbackFor = Exception.class)
    public StaffVO update(StaffVO staffVO) {
        log.debug("未使用缓存");
        // 加密码加密
        if (StringUtil.isNotEmpty(staffVO.getPassword())) {
            staffVO.setPassword(MD5Util.getSaltMd5(staffVO.getPassword()));
        }
        // 1.根据手机号，全名，艺名查重，手机号全公司不重复，全名，艺名，在职员工中不能重复
        StaffPO phoneExist = staffDao.getStaffByPhone(staffVO.getCompanyId(), staffVO.getPhone());
        if (phoneExist != null && phoneExist.getId() != staffVO.getId() && phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.STAFF_EXIST_DEL);
        }
        if (phoneExist != null && phoneExist.getId() != staffVO.getId() && !phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.PHONE_EXIST);
        }
        // 艺名查重
        StaffPO nickNameStaff = staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getNickName());
        if (nickNameStaff != null && nickNameStaff.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.NICKNAME_EXIST);
        }
        // 全名查重
        StaffPO userNameStaff = staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getUserName());
        if (userNameStaff != null && userNameStaff.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.USERNAME_EXIST);
        }
        // 2.修改员工信息
        staffDao.updateStaff(staffVO);
        // 3.更新小组关联表
        groupStaffDao.deleteByStaffId(staffVO.getCompanyId(), staffVO.getId());
        groupStaffDao.insertGroupStaff(staffVO.getCompanyId(), staffVO.getGroupId(), staffVO.getId());
        // 4.删除权限关联表
        staffRoleDao.deleteByStaffId(staffVO.getId(), staffVO.getCompanyId());
        // 5.插入人员角色关联表
        if (StringUtil.isNotEmpty(staffVO.getRoleIds())) {
            String[] roleArr = staffVO.getRoleIds().split(CommonConstant.STR_SEPARATOR);
            staffRoleDao.batchInsertStaffRole(staffVO.getId(), staffVO.getCompanyId(), roleArr);
        }
        // 6.清缓存
        // redisTemplate.opsForValue().getAndSet(RedisConstant.getStaffKey(staffVO.getId(),
        // staffVO.getCompanyId()), staffVO);
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
        // TODO 获取详细的一些信息 还是 单一的信息？
        return staffDao.getByIdAndCid(id, companyId);
    }

    /**
     * 根据条件获取集合
     *
     * @param queryMapDTO 查询条件
     * @return
     */
    @Override
    public PageInfo<StaffPO> findList(QueryMapDTO queryMapDTO) {
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
        if (CollectionUtils.isEmpty(companyList)) {
            // 用户不存在
            throw new RException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        // 移除错误次数
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
    public StaffPO loginWithCompanyId(String userName, String password, int companyId, String ip) {
        // 加密码加密
        password = MD5Util.getSaltMd5(password);
        StaffPO staffPO = staffDao.loginWithCompanyId(userName, password, companyId);
        if (staffPO == null) {
            // 用户不存在
            throw new RException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        } else if (staffPO.isLockFlag()) {
            // 锁定
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        } else if (staffPO.isDelFlag()) {
            // 删除
            throw new RException(ExceptionEnum.USER_IS_DEL);
        }
        // 验证公司属性
        CompanyPO companyPO = companyService.getById(staffPO.getCompanyId());
        // 如果员工没有token，重新生成
        if (StringUtil.isEmpty(staffPO.getToken()) || companyPO.isSsoLimit()) {
            updateStaffToken(staffPO);
        }
        // todo 上下线日志
        // 移除错误次数
        removeUserErrorNumber(userName);
        // 更新登录时间和IP
        StaffDetailPO staffDetailPO = new StaffDetailPO();
        staffDetailPO.setId(staffPO.getId());
        staffDetailPO.setCompanyId(staffPO.getCompanyId());
        staffDetailPO.setLastLoginIp(ip);
        staffDao.updateStaffLoginInfo(staffDetailPO);
        // 如果当前员工为下线状态，则更新他为上线状态
        if (staffPO.getStatusFlag() == StaffStatusEnum.OffLine.getStatusId()) {
            StaffPO staffPO1 = new StaffPO();
            staffPO1.setId(staffPO.getId());
            staffPO1.setCompanyId(staffPO.getCompanyId());
            staffPO1.setStatusFlag(StaffStatusEnum.InLine.getStatusId());
            staffDao.updateStatusFlag(staffPO1);
            //新增上线日志
            staffStatusLogDao.insert(new StaffStatusLog(
                    staffPO.getId(), staffPO.getNickName(), StaffStatusEnum.InLine.getStatusId(),
                    staffPO.getId(), staffPO.getNickName(), staffPO.getCompanyId()
            ));
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
     * 删除缓存中的用户错误次数和验证码
     *
     * @param phone
     */
    private void removeUserErrorNumber(String phone) {
        // 错误次数
        String userLoginErrNum = RedisConstant.getUserLoginErrNumKey(phone);
        redisTemplate.delete(userLoginErrNum);
        // 验证码
        redisTemplate.delete(RedisConstant.getVerifyCodeKey(phone));
    }

    /**
     * 将更新的staff放入redis 并更新到数据库
     *
     * @param staffPO
     */
    private void updateStaffToken(StaffPO staffPO) {
        // 生成token
        String token = JwtUtil.generatorToken();
        staffPO.setToken(token);
        redisTemplate.opsForValue().set(RedisConstant.getStaffKey(staffPO.getId(), staffPO.getCompanyId()), staffPO,
                CommonConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        // 并更新到数据库
        staffDao.updateToken(staffPO);
    }

    // /**
    // * 生成Jwt
    // *
    // * @param staffPO
    // * @return
    // */
    // private String executeJwtToken(StaffPO staffPO) {
    // StaffPO jwtStaff = new StaffPO();
    // jwtStaff.setId(staffPO.getId());
    // jwtStaff.setCompanyId(staffPO.getCompanyId());
    // jwtStaff.setPhone(staffPO.getPhone());
    // return JwtUtil.encrypt(JSONObject.toJSONString(jwtStaff));
    // }

    /**
     * 获取小组人员
     */
    // @Cacheable(value = "groupStaff", key =
    // "'groupStaff'+':'+#companyId+':'+#groupId")
    public List<StaffVO> getGroupStaffs(int companyId, String groupId) {
        List<StaffVO> list = groupStaffDao.getGroupStaffs(companyId, groupId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (StaffVO vo : list) {
                vo.getStaffPwdFlag();
            }
        }
        return list;
    }

    /**
     * 获取小组人员详情
     *
     * @param companyId
     * @param groupId
     * @return
     */
    public List<StaffMarsDTO> getGroupStaffsDetail(int companyId, String groupId) {

        List<StaffMarsDTO> list = groupStaffDao.getGroupStaffsDetail(companyId, groupId);
        return list;
    }

    /**
     * 获取各小组内人员的接单数和在线人数
     *
     * @param companyId
     * @return
     */
    public List<GroupsInfoVO> getStaffMarsInfo(int companyId) {
        return groupStaffDao.getStaffMarsInfo(companyId);
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
        // 1.修改密码
        if (StringUtil.isNotEmpty(password)) {
            staffDao.batchEditStaffPwd(companyId, staffIdArr, MD5Util.getSaltMd5(password));
        }
        // 2.修改小组
        groupStaffDao.batchEditStaffGroup(companyId, staffIdArr, groupId);

        // 4.修改角色
        if (StringUtil.isNotEmpty(roleIds)) {
            String[] roleIdArr = roleIds.split(CommonConstant.STR_SEPARATOR);
            // 删除角色关联
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
        // 4.TODO 清缓存

    }

    /**
     * 搜索员工
     *
     * @param companyId
     * @param searchKey
     * @return
     */
    public List<SearchStaffVO> getStaffListBySearchKey(int companyId, String searchKey) {
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
    public List<PermissionPO> getStaffPermissionById(int staffId, int companyId) {
        return permissionDao.getStaffPermission(staffId, companyId);
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
        // 员工对象
        List<PermissionPO> permissionPOList = permissionDao.getStaffPermission(staffId, companyId);
        staffBaseInfoVO.setPermission(permissionPOList);
        // 放入公司对象
        CompanyVO companyVO = companyDao.getVOById(companyId);
        companyVO.setMenuList(getCompanyMenuList(companyId, staffId));
        staffBaseInfoVO.setCompany(companyVO);
        staffBaseInfoVO.setStaffDetail(staffDao.getStaffDetail(staffId, companyId));
        //设置页面字典
        PageDictDTO pageDictDTO = new PageDictDTO();
        //来源字典
        pageDictDTO.setSourceMap(sourceService.getSourcePageMap(companyId));
        //状态字典
        pageDictDTO.setStatusMap(statusService.getStatusDictMap(companyId));
        staffBaseInfoVO.setPageDict(pageDictDTO);
        return staffBaseInfoVO;
    }

    /**
     * 交接客资
     *
     * @param staffChangeVO 交接客服基本信息
     */
    @Override
    public void changeStaff(StaffChangeVO staffChangeVO) {
        //TODO 调用平台接口
//        clientInfoDao.changeStaff(staffChangeVO);
    }

    /**
     * 根据公司ID和个人获取菜单列表
     *
     * @param companyId
     * @param staffId
     * @return
     */
    private List<MenuVO> getCompanyMenuList(int companyId, int staffId) {
        // 企业左上角菜单栏
        List<MenuVO> menuList = dictionaryDao.getCompanyMemu(companyId, DictionaryConstant.MENU_TYPE);
        if (CollectionUtils.isEmpty(menuList)) {
            menuList = dictionaryDao.getCompanyMemu(DictionaryConstant.COMMON_COMPANYID, DictionaryConstant.MENU_TYPE);
        }
        // 获取员工角色
        List<String> roleList = groupStaffDao.getStaffRoleList(companyId, staffId);
        if (CollectionUtils.isEmpty(menuList)) {
            throw new RException(ExceptionEnum.MENU_NULL);
        }
        if (CollectionUtils.isEmpty(roleList)) {
            return menuList;
        }

        for (String role : roleList) {
            // 1.如果是管理中心，全部开放
            if (RoleConstant.GLZX.equals(role)) {
                for (MenuVO menu : menuList) {
                    menu.setSelectFlag(true);
                }
                return menuList;
            }
            // 2.如果是财务中心，除管理中心，全部开放
            if (RoleConstant.CWZX.equals(role)) {
                for (MenuVO menu : menuList) {
                    if (RoleConstant.GLZX.equals(menu.getType())) {
                        menu.setSelectFlag(false);
                    } else {
                        menu.setSelectFlag(true);
                    }
                }
                return menuList;
            }
            // 都不是，则一一对应
            for (MenuVO menu : menuList) {
                if (role.equals(menu.getType())) {
                    menu.setSelectFlag(true);
                }
            }
        }
        return menuList;
    }

    /**
     * 获取电商邀约小组人员列表
     *
     * @param companyId
     * @return
     */
    @Override
    public List<GroupStaffVO> getChangeList(int companyId) {
        return groupStaffDao.getListByGroupType("dsyy", companyId);
    }

    /**
     * 修改密码时的验证
     *
     * @param id
     * @param password
     * @param companyId
     * @return
     */
    @Override
    public boolean isRightPassword(int id, String password, int companyId) {
        StaffPO staff = staffDao.getByIdAndCid(id, companyId);
        return StringUtil.ignoreCaseEqual(staff.getPassword(), MD5Util.getSaltMd5(password));
    }

    /**
     * 更新详细的信息
     *
     * @param staffDetailVO
     * @return
     */
    @Override
    @Transactional
    public StaffDetailVO update(StaffDetailVO staffDetailVO) {
        // 1.根据手机号，全名，艺名查重，手机号全公司不重复，全名，艺名，在职员工中不能重复
        StaffPO phoneExist = staffDao.getStaffByPhone(staffDetailVO.getCompanyId(), staffDetailVO.getPhone());
        if (phoneExist != null && phoneExist.getId() != staffDetailVO.getId() && phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.STAFF_EXIST_DEL);
        }
        if (phoneExist != null && phoneExist.getId() != staffDetailVO.getId() && !phoneExist.isDelFlag()) {
            throw new RException(ExceptionEnum.PHONE_EXIST);
        }
        // 艺名查重
        StaffPO nickNameStaff = staffDao.getStaffByNames(staffDetailVO.getCompanyId(), staffDetailVO.getNickName());
        if (nickNameStaff != null && nickNameStaff.getId() != staffDetailVO.getId()) {
            throw new RException(ExceptionEnum.NICKNAME_EXIST);
        }
        // 全名查重
        StaffPO userNameStaff = staffDao.getStaffByNames(staffDetailVO.getCompanyId(), staffDetailVO.getUserName());
        if (userNameStaff != null && userNameStaff.getId() != staffDetailVO.getId()) {
            throw new RException(ExceptionEnum.USERNAME_EXIST);
        }
        // 更新基础信息
        StaffPO staffPO = new StaffPO(staffDetailVO);
        staffDao.update(staffPO);
        // 更新详细信息
        StaffDetailPO staffDetailPO = new StaffDetailPO(staffDetailVO);
        staffDao.updateStaffDetail(staffDetailPO);
        return staffDetailVO;
    }

    /**
     * 更新密码
     *
     * @return
     */
    @Override
    public int updatePassword(StaffPasswordDTO staffPasswordDTO) {
        // 如果原始密码不对
        if (!isRightPassword(staffPasswordDTO.getId(), staffPasswordDTO.getOldPassword(),
                staffPasswordDTO.getCompanyId())) {
            throw new RException(ExceptionEnum.OLD_PASSWORD_ERROR);
        }
        StaffPO staffPO = new StaffPO();
        staffPO.setCompanyId(staffPasswordDTO.getCompanyId());
        staffPO.setId(staffPasswordDTO.getId());
        staffPO.setPassword(MD5Util.getSaltMd5(staffPasswordDTO.getNewPassword()));
        return staffDao.update(staffPO);
    }

    /**
     * 根据小组类型获取小组及组内人员信息
     *
     * @param companyId
     * @param type
     * @return
     */
    @Override
    public List<GroupStaffVO> getGroupStaffByType(int companyId, String type) {
        return groupStaffDao.getListByGroupType(type, companyId);
    }

    /**
     * 获取离职员工列表
     *
     * @param queryMapDTO
     * @return
     */
    @Override
    public PageInfo<StaffPO> getDelStaffList(QueryMapDTO queryMapDTO) {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<StaffPO> list = staffDao.findList(queryMapDTO.getCondition());
        return new PageInfo<>(list);
    }

    /**
     * 恢复离职员工
     *
     * @param staffVO
     */
    @Override
    public void restoreDelStaff(StaffVO staffVO) {
        // 1.根据手机号，全名，艺名查重，手机号全公司不重复，全名，艺名，在职员工中不能重复
        StaffPO phoneExist = staffDao.getStaffByPhone(staffVO.getCompanyId(), staffVO.getPhone());
        if (phoneExist != null && phoneExist.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.PHONE_EXIST);
        }
        // 艺名查重
        StaffPO nickNameStaff = staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getNickName());
        if (nickNameStaff != null && nickNameStaff.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.NICKNAME_EXIST);
        }
        // 全名查重
        StaffPO userNameStaff = staffDao.getStaffByNames(staffVO.getCompanyId(), staffVO.getUserName());
        if (userNameStaff != null && userNameStaff.getId() != staffVO.getId()) {
            throw new RException(ExceptionEnum.USERNAME_EXIST);
        }
        // 2.修改员工基础信息
        if (StringUtil.isNotEmpty(staffVO.getPassword())) {
            staffVO.setPassword(MD5Util.getSaltMd5(staffVO.getPassword()));
        }
        staffVO.setDelFlag(false);
        staffDao.updateStaff(staffVO);
        // 3.添加小组关联
        groupStaffDao.insertGroupStaff(staffVO.getCompanyId(), staffVO.getGroupId(), staffVO.getId());
        // 4.添加权限关联
        if (StringUtil.isNotEmpty(staffVO.getRoleIds())) {
            String[] roleArr = staffVO.getRoleIds().split(CommonConstant.STR_SEPARATOR);
            staffRoleDao.batchInsertStaffRole(staffVO.getId(), staffVO.getCompanyId(), roleArr);
        }

    }

    /**
     * 批量恢复员工
     *
     * @param companyId
     * @param staffIds
     * @param roleIds
     * @param password
     * @param groupId
     */
    @Override
    public void batchRestoreStaff(int companyId, String staffIds, String roleIds, String password, String groupId) {
        String[] staffIdArr = staffIds.split(CommonConstant.STR_SEPARATOR);
        // 1.恢复员工
        staffDao.batchRestoreStaff(companyId, staffIdArr,
                StringUtil.isNotEmpty(password) ? MD5Util.getSaltMd5(password) : null);
        // 2.修改小组
        groupStaffDao.batchInsertGroupStaff(companyId, groupId, staffIdArr);
        // 4.修改角色
        if (StringUtil.isNotEmpty(roleIds)) {
            String[] roleIdArr = roleIds.split(CommonConstant.STR_SEPARATOR);
            List<StaffVO> list = new LinkedList<>();
            for (String staffId : staffIdArr) {
                for (String roleId : roleIdArr) {
                    StaffVO vo = new StaffVO(Integer.parseInt(staffId), companyId, Integer.parseInt(roleId));
                    list.add(vo);
                }
            }
            staffRoleDao.batchInsertStaffRoleByVO(list);
        }
        // 4.TODO 清缓存
    }

    /**
     * 搜索离职员工
     *
     * @param companyId
     * @param searchKey
     * @return
     */
    @Override
    public List<StaffPO> getDelStaffListBySearchKey(int companyId, String searchKey) {
        return staffDao.getDelStaffListBySearchKey(companyId, searchKey);
    }


    /**
     * 根据员工ID，获取小组员工信息
     *
     * @param companyId
     * @param staffId
     * @return
     */
    public List<StaffVO> getGroupStaffById(int companyId, int staffId) {
        return staffDao.getGroupStaffById(companyId, staffId);
    }

    /**
     * 获取员工上下线日志
     *
     * @param companyId
     * @param staffId
     * @return
     */
    @Override
    public List<StaffStatusLog> getStaffStatusLogById(int companyId, int staffId) {
        //获取昨天的时间戳
        Date yesterDay = TimeUtil.getYesterDay(new Date());
        int time = TimeUtil.dateToIntMillis(yesterDay);
        return staffStatusLogDao.listByStaffId(companyId, staffId, time);
    }


}
