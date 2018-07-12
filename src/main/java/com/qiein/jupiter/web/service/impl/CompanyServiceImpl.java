package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.CompanyZjsSetDTO;
import com.qiein.jupiter.web.entity.dto.DsinvalDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import com.qiein.jupiter.web.service.CompanyService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公司实现类
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private StaffDao staffDao;

    /**
     * 根据Id获取
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyPO getById(int companyId) {
        return companyDao.getById(companyId);
    }

    /**
     * 根据查询条件获取集合
     *
     * @param map
     * @return
     */
    @Override
    public List<CompanyPO> findList(Map<String, Object> map) {
        return null;
    }

    /**
     * 删除公司 逻辑删除
     *
     * @param companyId
     * @return
     */
    @Override
    public int deleteFlag(int companyId) {
        return 0;
    }

    /**
     * 插入
     *
     * @param companyPO
     * @return
     */
    @Override
    public CompanyPO insert(CompanyPO companyPO) {
        return null;
    }

    /**
     * 更新
     *
     * @param companyPO
     * @return
     */
    @Override
    @Transactional
    public int update(CompanyPO companyPO) {
        if (companyPO.getId() == 0)
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        if (StringUtil.isNotEmpty(companyPO.getCorpId()))
            //修改corpId时，同时修改所属公司员工的corpId
            staffDao.editStaffCorpId(companyPO.getCorpId(), companyPO.getId());

        return companyDao.update(companyPO);
    }

    /**
     * 编辑自定义设置
     *
     * @param column
     * @param flag
     * @return
     */
    @Override
    public int updateFlag(int companyId, String column, boolean flag) {
        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        }
        return companyDao.updateFlag(companyId, column, flag);
    }

    /**
     * 编辑自定义范围
     *
     * @param column
     * @param num
     * @return
     */
    @Override
    public int updateRange(int companyId, String column, int num) {
        if (NumUtil.isInValid(companyId)) {
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        }
        return companyDao.updateRange(companyId, column, num);
    }

    /**
     * 获取iplimt
     *
     * @param companyId
     * @return
     */
    @Override
    public int getIpLimit(int companyId) {
        return companyDao.getIpLimit(companyId);
    }

    /**
     * 修改iplimt
     *
     * @param ipLimit * @param companyId
     * @return
     */
    @Override
    public void editIpLimit(Integer ipLimit, int companyId) {
        companyDao.editIpLimit(ipLimit, companyId);
    }

    /**
     * 获取VO
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyVO getCompanyVO(int companyId) {
        return companyDao.getVOById(companyId);
    }

    /**
     * 更改咨询类型(客资校验是否忽略咨询类型)
     *
     * @return
     */
    @Override
    public void editTypeRepeat(Boolean typeRepeat, int companyId) {
        companyDao.editTypeRepeat(typeRepeat, companyId);
    }

    /**
     * 更改渠道类型(客资校验是否忽略渠道类型)
     *
     * @return
     */
    @Override
    public void editTypeSrcRepeat(boolean srcRepeat, int companyId) {
        companyDao.editTypeSrcRepeat(srcRepeat, companyId);
    }

    /**
     * 更改客资录入时间和最后操作时间,客资状态是否可以重复录
     *
     * @return
     */
    @Override
    public void editKZday(String statusIgnore, String timeTypeIgnore, int dayIgnore, int companyId) {
        companyDao.editKZday(statusIgnore, timeTypeIgnore, dayIgnore, companyId);
    }

    /**
     * 查询哪些客资重复被拦截
     *
     * @return
     */
    @Override
    public CompanyPO selectAll(int companyId) {
        return companyDao.selectAll(companyId);
    }

    /**
     * 修改无效状态以及待跟踪意向等级
     *
     * @param dsinvalDTO
     */
    public void editDsInvalid(DsinvalDTO dsinvalDTO) {
        companyDao.editDsInvalid(dsinvalDTO);
    }

    /**
     * 修改待定是否计入有效
     *
     * @param ddIsValid
     * @param companyId
     */
    public void editDdIsValid(boolean ddIsValid, int companyId) {
        companyDao.editDdIsValid(ddIsValid, companyId);
    }

    /**
     * 搜索无效状态以及跟踪意向等级
     *
     * @param companyId
     * @return
     */
    public DsinvalDTO findDsinvalId(@Param("companyId") Integer companyId) {
        return companyDao.findDsinvalId(companyId);
    }

    /**
     * 修改转介绍有效指标定义
     *
     * @param companyId
     * @param zjsValidStatus
     */
    public void editZjsValidStatus(Integer companyId, String zjsValidStatus) {
        companyDao.editZjsValidStatus(companyId, zjsValidStatus);
    }

    /**
     * 转介绍有效指标定义
     *
     * @param companyId
     * @return
     */
    public List<String> findZjsValidStatus(Integer companyId) {
        String zjsValidStatus = companyDao.findZjsValidStatus(companyId);
        String[] zjsValidStatuss = zjsValidStatus.substring(1, zjsValidStatus.length() - 1).split(CommonConstant.FILE_SEPARATOR);
        List<String> list = new ArrayList<>();
        for (String status : zjsValidStatuss) {
            list.add(status);
        }
        return list;
    }

    /**
     *
     * 功能描述: 
     *  获取企业转介绍自定义设置
     * @auther: Tt(yehuawei)
     * @date:
     * @param: 
     * @return: 
     */
    @Override
    public CompanyZjsSetDTO getCompanyZjsSet(int companyId) {
        return companyDao.getCompanyZjsSet(companyId);
    }

    /**
     * 功能描述:
     * 公司钉钉转介绍提报自定义设置
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @Override
    public void editCompanyZJSSet(String oldClientZjsSet, String qyZjsSet, int companyId) {
        if (companyDao.editCompanyZJSSet(oldClientZjsSet, qyZjsSet, companyId) == 0)
            throw new RException(ExceptionEnum.COMPANY_EDIT_ZJS_SET);
    }

    /**
     * 客服编辑接待结果
     */
    public void editKfEditJdRst(boolean kfEditJdRst,Integer companyId){
        companyDao.editKfEditJdRst(kfEditJdRst,companyId);
    }


}