package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.enums.StaffStatusEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.util.ding.DingAuthUtil;
import com.qiein.jupiter.util.wechat.WeChatAuthUtil;
import com.qiein.jupiter.util.wechat.WeChatPushUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.IpWhiteService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StaffDao staffDao;
    @Autowired
    private StaffStatusLogDao statusLogDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StaffRoleDao staffRoleDao;

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private ClientInfoDao clientInfoDao;

    @Autowired
    private StaffStatusLogDao staffStatusLogDao;

    @Autowired
    private IpWhiteService ipWhiteService;

    @Autowired
    private WeChatAuthUtil weChatAuthUtil;

    @Autowired
    private DingAuthUtil dingAuthUtil;

    @Autowired
    private OnLineTimeDao onLineTimeDao;


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
        // 1.给个默认token，钉钉需要
        String token = JwtUtil.generatorToken();
        staffVO.setToken(token);
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
        if (statusFlag == StaffStatusEnum.OffLine.getStatusId()
                || statusFlag == StaffStatusEnum.OffLine.getStatusId()) {
            if (staffNow.getStatusFlag() == StaffStatusEnum.STOP_ORDER.getStatusId()) {
                // 停单
                throw new RException(ExceptionEnum.STAFF_IS_STOP_RECEIPT);
            } else if (staffNow.getStatusFlag() == StaffStatusEnum.LIMIT.getStatusId()) {
                // 满限
                throw new RException(ExceptionEnum.STAFF_IS_LIMIT);
            }
        }
        staffPO.setStatusFlag(statusFlag);
        // 更新状态
        staffDao.updateStatusFlag(staffPO);
        // 如果是上线，修改最后上线时间
        if (StaffStatusEnum.OnLine.getStatusId() == statusFlag) {
            staffDao.updatLastLoginTime(id, companyId);
        }
        // 保存日志
        // 更新的员工
        StaffPO updateStaff = staffDao.getByIdAndCid(id, companyId);
        // 保存
        staffStatusLogDao
                .insert(new StaffStatusLog(updateStaff.getId(), statusFlag, operatorId, operatorName, companyId, ""));
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
        // TODO 清除缓存
        // FIXME  此处需要做删除校验
        List<StaffGroupVO> staffGroupVOS = groupStaffDao.getGroupStaffCountByStaffIdAndCompanyId(Arrays.asList(staffStateVO.getIds().split(CommonConstant.STR_SEPARATOR)),staffStateVO.getCompanyId());
        for (StaffGroupVO staffGroupVO :staffGroupVOS){
            if(staffGroupVO.getGroupCount() > 1){
                //FIXME 此处暂时无法更改
            }else{
                // 修改删除标识
                staffDao.batUpdateStaffState(staffStateVO, staffStateVO.getIds().split(","));
                // 硬删除员工角色
                staffRoleDao.batchDeleteByStaffIdArr(staffStateVO.getCompanyId(), staffStateVO.getIds().split(","));
                // 硬删除员工小组
                groupStaffDao.batchDeleteByStaffArr(staffStateVO.getCompanyId(), staffStateVO.getIds().split(","));
            }
        }

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
    // @Cacheable(value = "staff", key = "'staff'+':'+#id+':'+#companyId")
    @Override
    public StaffPO getById(int id, int companyId) {
        log.debug("未使用缓存");
        // TODO 获取详细的一些信息 还是 单一的信息？
        return staffDao.getByIdAndCid(id, companyId);
    }

    /**
     * 根据Id获取员工基础信息
     *
     * @param id
     * @param companyId
     * @return
     */
    @Override
    public StaffPO getByIdWithoutCache(int id, int companyId) {
        return staffDao.getByIdAndCid(id, companyId);
    }

    /**
     * 编辑员工消息设置
     *
     * @param staffMsg
     */
    @Override
    public void editMsgSet(StaffMsg staffMsg) {
        if (staffDao.editMsgSet(staffMsg) < 1) {
            throw new RException(ExceptionEnum.EDIT_FAIL);
        }
    }

    /**
     * 检查是否绑定成功，如果绑定成功返回微信公众号用户的所有信息
     *
     * @param companyId
     * @param staffId
     */
    @Override
    public WeChatUserDTO checkWXBind(Integer companyId, Integer staffId) {
        String resultJsonStr = HttpClient.get(WeChatPushUtil.APOLLO_URL + "/wechat/get_user_info")
                .queryString("companyId", companyId)
                .queryString("staffId", staffId)
                .asString();
        JSONObject resultJson = JSONObject.parseObject(resultJsonStr);
        log.info("微信绑定阿波罗返回： " + resultJsonStr);
        if (StringUtil.isEmpty(resultJson.getString("data")) && resultJson.getIntValue("code") == 100000)
            log.info(ExceptionEnum.WX_NOT_BIND.getMsg());
        if (resultJson.getIntValue("code") != 100000)
            throw new RException(ExceptionEnum.GET_WX_INFO_FAIL);

        WeChatUserDTO weChatUserDTO = JSONObject.parseObject(resultJson.getString("data"), WeChatUserDTO.class);
        return weChatUserDTO;
    }

    @Override
    public StaffTodayInfoDTO getWXStaffInfo(Integer companyId, Integer staffId) {
        //TODO
        return null;
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
     * 将更新的staff放入redis 并更新到数据库
     *
     * @param staffPO
     */
    @SuppressWarnings("unused")
    private void updateStaffToken(StaffPO staffPO) {
        // 生成token
        String token = JwtUtil.generatorToken();
        staffPO.setToken(token);
        redisTemplate.opsForValue().set(RedisConstant.getStaffKey(staffPO.getId(), staffPO.getCompanyId()), staffPO,
                CommonConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        // 并更新到数据库
        staffDao.updateToken(staffPO);
    }

    /**
     * 获取小组人员
     */
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
        return groupStaffDao.getGroupStaffsDetail(companyId, groupId);
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
     * 交接客资
     *
     * @param staffChangeVO 交接客服基本信息
     */
    @Override
    public void changeStaff(StaffChangeVO staffChangeVO) {
        // TODO 调用平台接口
        clientInfoDao.changeStaff(staffChangeVO);
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
        String newPassword = staffPasswordDTO.getNewPassword();
        // 空
        if (StringUtil.isEmpty(newPassword)) {
            throw new RException(ExceptionEnum.PASSWORD_NULL);
        }
        // 长度
        if (newPassword.length() < 6) {
            throw new RException(ExceptionEnum.SIMPLE_PASSWORD);
        }
        // 纯数字
        if (StringUtil.isNumber(newPassword)) {
            throw new RException(ExceptionEnum.SIMPLE_NUMBER_PASSWORD);
        }
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
    @Transactional
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
    @Transactional
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
    public List<StaffVO> getGroupStaffById(int companyId, int staffId, String groupId) {
        return staffDao.getGroupStaffById(companyId, staffId, groupId);
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
        // 获取昨天的时间戳
        Date yesterDay = TimeUtil.getYesterDay(new Date());
        int time = TimeUtil.dateToIntMillis(yesterDay);
        return staffStatusLogDao.listByStaffId(companyId, staffId, time);
    }

    /**
     * 退出登录
     *
     * @param companyId
     * @param staffId
     */
    @Override
    @Transactional
    public void logout(int companyId, int staffId, String staffName) {
        StaffPO staffNow = staffDao.getByIdAndCid(staffId, companyId);
        // 如果员工当前为在线状态，则让他下线
        // 验证公司属性
        CompanyVO company = companyDao.getVOById(companyId);
        if (!company.isUnableSelfLine()) {
            if (staffNow.getStatusFlag() == StaffStatusEnum.OnLine.getStatusId()) {
                // 更新为下线状态，新增日志
                staffNow.setStatusFlag(StaffStatusEnum.OffLine.getStatusId());
                staffDao.updateStatusFlag(staffNow);
            }
            staffStatusLogDao.insert(
                    new StaffStatusLog(staffId, StaffStatusEnum.OffLine.getStatusId(), staffId, staffName, companyId, ""));
        }
    }

    /**
     * 获取员工名片
     *
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public StaffDetailVO getStaffCard(int staffId, int companyId) {
        StaffDetailVO staffDetailVO = staffDao.getStaffCard(staffId, companyId);
        if (staffDetailVO == null) {
            throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
        }
        return staffDetailVO;
    }

    /**
     * 添加到白名单
     *
     * @param staffIds
     */
    @Override
    public void addIpWhite(String staffIds) {
        String[] split = staffIds.split(CommonConstant.STR_SEPARATOR);
        staffDao.addIpWhite(split);
    }

    /**
     * 从白名单删除
     *
     * @param staffId
     */
    @Override
    public void delIpWhite(int staffId) {
        staffDao.delIpWhite(staffId);
    }

    /**
     * 批量从白名单删除
     *
     * @param ids
     */
    @Override
    public void delListIpWhite(List<Integer> ids) {
        staffDao.delListIpWhite(ids);
    }

    /**
     * 检测
     */
    @Override
    public boolean heartBeat(StaffPO staffPO, String ip) {
        // 更新心跳时间
        staffDao.updateStaffHeartTime(staffPO.getId(), staffPO.getCompanyId());
        // 如果在线，
        if (staffPO.getStatusFlag() == StaffStatusEnum.OnLine.getStatusId()) {
            // 查是否有记录，没有则添加，
            OnLineTimePO onLineTimePO = onLineTimeDao.getLogByStaffAndDay(staffPO.getId(), staffPO.getCompanyId(),
                    DBSplitUtil.getOnLineTimeLogTabName(staffPO.getCompanyId()));
            if (onLineTimePO == null) {
                onLineTimeDao.addOnLineTimeLog(staffPO.getId(), staffPO.getCompanyId(), staffPO.getNickName(),
                        DBSplitUtil.getOnLineTimeLogTabName(staffPO.getCompanyId()));
            }
            // 判断距离上次时间间隔，超过120秒，当第一次上线，只修改上次心跳时间
            int interval = onLineTimeDao.getLastHeartTime(staffPO.getId(), staffPO.getCompanyId(),
                    DBSplitUtil.getOnLineTimeLogTabName(staffPO.getCompanyId()));
            if (interval > CommonConstant.DEFAULT_ONLINE_TIME) {
                onLineTimeDao.updateLastHeartTime(staffPO.getId(), staffPO.getCompanyId(),
                        DBSplitUtil.getOnLineTimeLogTabName(staffPO.getCompanyId()));
            } else {
                onLineTimeDao.updateOnLineTime(staffPO.getId(), staffPO.getCompanyId(),
                        DBSplitUtil.getOnLineTimeLogTabName(staffPO.getCompanyId()), interval);
            }
        }
        return ipWhiteService.checkIpLimit(staffPO.getId(), staffPO.getCompanyId(), ip);
    }

    /**
     * 计算员工今日客资数，并校验是否满限状态
     *
     * @param companyId
     * @param staffId
     */
    @Override
    public void resizeTodayNum(int companyId, int staffId) {
        // 计算客服今日领取客资数
        int num = staffDao.getTodayKzNum(companyId, staffId, DBSplitUtil.getInfoTabName(companyId));
        // 修改今日领取客资数
        int updateNum = staffDao.updateTodatKzNum(companyId, staffId, num);
        if (1 != updateNum) {
            throw new RException(ExceptionEnum.STAFF_EDIT_ERROR);
        }
        // 计算是否满限
        updateNum = staffDao.checkOverFlowToday(companyId, staffId);
        StaffPO byIdAndCid = staffDao.getByIdAndCid(staffId, companyId);
        //如果满限，并且当前状态不是满限，则记录日志
        if (1 == updateNum && byIdAndCid.getStatusFlag() != StaffStatusEnum.LIMIT.getStatusId()) {
            // 记录状态修改日志
            statusLogDao.insert(
                    new StaffStatusLog(staffId, StaffStatusEnum.LIMIT.getStatusId(), CommonConstant.SYSTEM_OPERA_ID,
                            CommonConstant.SYSTEM_OPERA_NAME, companyId, ClientLogConst.LIMITDAY_OVERFLOW));
            // 推送状态重载消息
            GoEasyUtil.pushStatusRefresh(companyId, staffId);
        }
    }

    /**
     * 绑定微信
     *
     * @param staffId
     * @param companyId
     * @param code
     * @return
     */
    @Override
    public boolean bindingWeChat(int staffId, int companyId, String code) {
        WeChatAuthDTO accessToken = weChatAuthUtil.getAccessToken(code);
        WeChatAuthDTO userInfo = weChatAuthUtil.getUserInfo(accessToken);
        String unionId = userInfo.getUnionId();
        // TODO 判断空
        StaffDetailVO staffDetailVO = new StaffDetailVO();
        staffDetailVO.setId(staffId);
        staffDetailVO.setCompanyId(companyId);
        staffDetailVO.setWeChatUnionId(unionId);
        staffDao.bindingWeChat(staffDetailVO);
        return true;
    }

    /**
     * 绑定钉钉
     *
     * @param staffId
     * @param companyId
     * @param code
     * @return
     */
    @Override
    public boolean bindingDing(int staffId, int companyId, String code) {
        DingAuthDTO persistentCode = dingAuthUtil.getPersistentCode(code);
        String unionId = persistentCode.getUnionId();
        // TODO 判断空
        StaffDetailVO staffDetailVO = new StaffDetailVO();
        staffDetailVO.setId(staffId);
        staffDetailVO.setCompanyId(companyId);
        staffDetailVO.setDingUnionId(unionId);
        staffDao.bindingDing(staffDetailVO);
        return true;
    }

    /**
     * 每天晚上初始化员工分客资信息
     */
    @Override
    public void taskClockInit() {
        staffDao.resetTodayNum();
        staffDao.resetOffLineWhenLimit();
    }

    /**
     * 功能描述:
     * 获取未绑定钉钉用户
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @Override
    public List<DingBindUserDTO> getNotBindDingUser(Integer companyId) {
        return staffDao.getNotBindDingUser(companyId);
    }

    /**
     * 员工全部下线
     */


    /**
     * 根据ids批量查找员工
     *
     * @param ids
     * @param companyId
     * @return
     */
    public List<StaffPO> getByIds(String ids, Integer companyId) {
        String[] idlist = ids.split(CommonConstant.STR_SEPARATOR);
        List<Integer> list = new ArrayList<>();
        for (String id : idlist) {
            list.add(Integer.parseInt(id));
        }
        return staffDao.getByIds(list, companyId);
    }

    /**
     * 根据ids批量查找员工小组
     *
     * @param ids
     * @param companyId
     * @return
     */
    public List<SearchStaffVO> getGroupById(String ids, Integer companyId) {
        String[] idlist = ids.split(CommonConstant.STR_SEPARATOR);
        List<Integer> list = new ArrayList<>();
        for (String id : idlist) {
            list.add(Integer.parseInt(id));
        }
        return staffDao.getGroupById(list, companyId);
    }

    /**
     * 员工切换公司
     *
     * @param companyId
     * @param staffId
     * @return
     */
    @Override
    public StaffPO getStaffByChangeCid(int changeCompanyId, int staffId, int companyId) {
        StaffPO byIdAndCid = staffDao.getByIdAndCid(staffId, companyId);
        StaffPO staffByPhoneMd5PwdAndCid = staffDao.getStaffByPhoneMd5PwdAndCid(byIdAndCid.getPhone(),
                byIdAndCid.getPassword(), changeCompanyId);
        if (staffByPhoneMd5PwdAndCid == null) {
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        }
        return staffByPhoneMd5PwdAndCid;
    }


}