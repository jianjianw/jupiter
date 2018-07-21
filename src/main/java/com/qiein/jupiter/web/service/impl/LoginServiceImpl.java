package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.RedisConstant;
import com.qiein.jupiter.constant.RoleConstant;
import com.qiein.jupiter.enums.StaffStatusEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.util.ding.DingAuthUtil;
import com.qiein.jupiter.util.wechat.WeChatAuthUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.DingAuthDTO;
import com.qiein.jupiter.web.entity.dto.PageDictDTO;
import com.qiein.jupiter.web.entity.dto.WeChatAuthDTO;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.*;
import com.qiein.jupiter.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务
 *
 * @Author: shiTao
 */
@Service
public class LoginServiceImpl implements LoginService {

    // private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GroupStaffDao groupStaffDao;

    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private StaffStatusLogDao staffStatusLogDao;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private IpWhiteService ipWhiteService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private WeChatAuthUtil weChatAuthUtil;

    @Autowired
    private DingAuthUtil dingAuthUtil;

    /**
     * 微信获取公司列表
     *
     * @param authCode
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyListByWeChat(String authCode) {
        WeChatAuthDTO accessToken = weChatAuthUtil.getAccessToken(authCode);
        WeChatAuthDTO userInfo = weChatAuthUtil.getUserInfo(accessToken);
        String unionId = userInfo.getUnionId();
        List<CompanyPO> companyList = loginDao.getCompanyListByWeChatUnionId(unionId);
        if (CollectionUtils.isEmpty(companyList)) {
            // 用户不存在
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        }
        // 根据CODE 把unionid 放入redis
        redisTemplate.opsForValue().set(RedisConstant.getWeChatKey(authCode), unionId,
                CommonConstant.DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
        return companyList;
    }

    /**
     * 微信根据公司ID登录
     *
     * @param authCode
     * @param companyId
     * @param ip
     * @return
     */
    @Override
    public StaffPO loginWithCompanyIdByWeChat(String authCode, int companyId, String ip) {
        String weChatUnionID = (String) redisTemplate.opsForValue().get(RedisConstant.getWeChatKey(authCode));
        if (StringUtil.isEmpty(weChatUnionID)) {
            // 过期
        }
        StaffPO staff = loginDao.loginWithCidByWeChatUnionId(weChatUnionID, companyId);
        return checkUserInfo(staff, ip, false);
    }

    /**
     * 手机号码 获取公司列表
     *
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyListByPhone(LoginUserVO loginUserVO) {
        String phone = loginUserVO.getUserName();
        String password = loginUserVO.getPassword();
        List<CompanyPO> companyList = loginDao.getCompanyListByPhone(phone, MD5Util.getSaltMd5(password));
        if (CollectionUtils.isEmpty(companyList)) {
            // 用户不存在
            throw new RException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        // 移除错误次数
        removeUserErrorNumber(phone);
        return companyList;
    }

    /**
     * 手机号码 登录
     */
    @Override
    public StaffPO loginWithCompanyIdByPhone(LoginUserVO loginUserVO) {
        StaffPO staff = loginDao.loginWithCidByPhone(loginUserVO.getUserName(),
                MD5Util.getSaltMd5(loginUserVO.getPassword()),
                loginUserVO.getCompanyId());
        StaffPO staffPO = checkUserInfo(staff, loginUserVO.getIp(), loginUserVO.isClientFlag());
        // 移除错误次数
        removeUserErrorNumber(loginUserVO.getUserName());
        return staffPO;
    }

    /**
     * 钉钉获取公司列表
     *
     * @param authCode
     * @return
     */
    @Override
    public List<CompanyPO> getCompanyListByDing(String authCode) {
        DingAuthDTO persistentCode = dingAuthUtil.getPersistentCode(authCode);
        List<CompanyPO> companyList = loginDao.getCompanyListByDingUnionId(persistentCode.getUnionId());
        if (CollectionUtils.isEmpty(companyList)) {
            // 用户不存在
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        }
        // 根据CODE 把unionid 放入redis
        redisTemplate.opsForValue().set(RedisConstant.getDingKey(authCode), persistentCode.getUnionId(),
                CommonConstant.DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
        return companyList;
    }

    /**
     * 钉钉登录
     *
     * @param authCode
     * @param companyId
     * @param ip
     * @return
     */
    @Override
    public StaffPO loginWithCompanyIdByDing(String authCode, int companyId, String ip) {
        String unionId = (String) redisTemplate.opsForValue().get(RedisConstant.getDingKey(authCode));
        if (StringUtil.isEmpty(unionId)) {
            // 过期
        }
        StaffPO staff = loginDao.loginWithCidByDingUnionId(unionId, companyId);
        return checkUserInfo(staff, ip, false);
    }

    /**
     * 根据公司ID 登录
     */
    private StaffPO checkUserInfo(StaffPO staff, String ip, boolean clientFlag) {
        if (staff == null) {
            // 用户不存在
            throw new RException(ExceptionEnum.USER_NOT_FOUND);
        } else if (staff.isLockFlag()) {
            // 锁定
            throw new RException(ExceptionEnum.USER_IS_LOCK);
        } else if (staff.isDelFlag()) {
            // 删除
            throw new RException(ExceptionEnum.USER_IS_DEL);
        }
        // IP限制
        if (!ipWhiteService.checkIpLimit(staff.getId(), staff.getCompanyId(), ip)) {
            throw new RException(ExceptionEnum.IP_NOT_IN_SAFETY);
        }
        // 验证公司属性
        CompanyPO companyPO = companyService.getById(staff.getCompanyId());
        // 被锁定
        if (companyPO.isLockFlag()) {
            throw new RException(ExceptionEnum.COMPANY_IS_LOCK);
        }
        //只要客户端登录
        if (companyPO.isOnlyApp()) {
            if (!clientFlag) {
                throw new RException(ExceptionEnum.ONLY_APP_LOGIN);
            }
        }
        // 如果员工没有token，或者重新生成
        if (StringUtil.isEmpty(staff.getToken()) || companyPO.isSsoLimit()) {
            updateToken(staff);
        }
        // 更新登录时间和IP
        StaffDetailPO staffDetailPO = new StaffDetailPO();
        staffDetailPO.setId(staff.getId());
        staffDetailPO.setCompanyId(staff.getCompanyId());
        staffDetailPO.setLastLoginIp(ip);
        staffDao.updateStaffLoginInfo(staffDetailPO);
        // 如果当前员工为下线状态，则更新他为上线状态
        if (staff.getStatusFlag() == StaffStatusEnum.OffLine.getStatusId()) {
            StaffPO staffPO1 = new StaffPO();
            staffPO1.setId(staff.getId());
            staffPO1.setCompanyId(staff.getCompanyId());
            staffPO1.setStatusFlag(StaffStatusEnum.OnLine.getStatusId());
            staffDao.updateStatusFlag(staffPO1);
        }
        // 新增上线日志
        staffStatusLogDao.insert(new StaffStatusLog(staff.getId(), StaffStatusEnum.OnLine.getStatusId(), staff.getId(),
                staff.getNickName(), staff.getCompanyId(), ""));
        // 给特定用户推送上线
        GoEasyUtil.pushStaffRefresh(staff.getCompanyId(), staff.getId(), ip,
                HttpUtil.getIpLocation(ip).replace(CommonConstant.STR_SEPARATOR, ""));
        return staff;
    }

    /**
     * 更新token
     */
    @Override
    public String updateToken(StaffPO staff) {
        // 生成token
        String token = JwtUtil.generatorToken();
        staff.setToken(token);
        redisTemplate.opsForValue().set(RedisConstant.getStaffKey(staff.getId(), staff.getCompanyId()), staff,
                CommonConstant.DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
        // 并更新到数据库
        staffDao.updateToken(staff);
        return token;
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
     * 获取员工的基础信息
     *
     * @param staffId
     * @param companyId
     * @return
     */
    @Override
    public BaseInfoVO getBaseInfo(int staffId, int companyId) {
        BaseInfoVO staffBaseInfoVO = new BaseInfoVO();
        // 权限列表 和 map
        List<PermissionPO> permissionPOList = permissionDao.getStaffPermission(staffId, companyId);
        Map<String, String> permissionMap = getPermissionMap(permissionPOList);
        staffBaseInfoVO.setPermission(permissionPOList);
        staffBaseInfoVO.setPermissionMap(permissionMap);
        // 放入公司对象
        CompanyVO companyVO = companyService.getCompanyVO(companyId);
        companyVO.setMenuList(getCompanyMenuList(companyId, staffId));
        staffBaseInfoVO.setCompany(companyVO);
        // 员工
        StaffDetailVO staffDetailVO = staffDao.getStaffDetailVO(staffId, companyId);
        // 是否原始密码
        if (StringUtil.ignoreCaseEqual(staffDetailVO.getPassword(), MD5Util.getSaltMd5(staffDetailVO.getPhone()))) {
            staffDetailVO.setSimplePasswordFlag(true);
        }
        staffBaseInfoVO.setStaffDetail(staffDetailVO);
        // 设置页面字典
        PageDictDTO pageDictDTO = new PageDictDTO();
        // 来源字典
        pageDictDTO.setSourceMap(sourceService.getSourcePageMap(companyId));
        // 状态字典
        pageDictDTO.setStatusMap(statusService.getStatusDictMap(companyId));
        // 公共字典
        pageDictDTO.setCommonMap(dictionaryService.getDictMapByCid(companyId));
        // 渠道字典
        pageDictDTO.setChannelMap(channelService.getChannelDict(companyId));
        // 拍摄地字典
        pageDictDTO.setShopMap(shopService.getShopDictByCid(companyId));
        staffBaseInfoVO.setPageDict(pageDictDTO);
        // 消息
        staffBaseInfoVO.setNews(newsService.getNewsTotalAmountAndFlag(staffId, companyId));
        return staffBaseInfoVO;
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
        List<MenuVO> menuList = dictionaryDao.getCompanyMenu(companyId, DictionaryConstant.MENU_TYPE);
        if (CollectionUtils.isEmpty(menuList)) {
            menuList = dictionaryDao.getCompanyMenu(DictionaryConstant.COMMON_COMPANYID, DictionaryConstant.MENU_TYPE);
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
     * 获取权限Map
     *
     * @param permissionPOList
     * @return
     */
    private Map<String, String> getPermissionMap(List<PermissionPO> permissionPOList) {
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(permissionPOList)) {
            for (PermissionPO permissionPO : permissionPOList) {
                map.put("key" + String.valueOf(permissionPO.getPermissionId()), permissionPO.getPermissionName());
            }
        }
        return map;
    }

}
