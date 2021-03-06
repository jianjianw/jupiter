package com.qiein.jupiter.web.dao;

import com.qiein.jupiter.web.entity.dto.CompanyZjsSetDTO;
import com.qiein.jupiter.web.entity.dto.DsinvalDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.Datav;
import com.qiein.jupiter.web.entity.po.DatavPermissionPo;
import com.qiein.jupiter.web.entity.vo.CompanyVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 公司Dao
 */
public interface CompanyDao extends BaseDao<CompanyPO> {

    CompanyVO getVOById(@Param("companyId") int companyId);

    /**
     * 编辑自定义设置
     *
     * @param column
     * @param flag
     * @return
     */
    int updateFlag(@Param("companyId") int companyId, @Param("column") String column,
                   @Param("flag") boolean flag);

    /**
     * 编辑自定义范围
     *
     * @param column
     * @param num
     * @return
     */
    int updateRange(@Param("companyId") int companyId, @Param("column") String column, @Param("num") int num);

    /**
     * 获取iplimt
     *
     * @param companyId
     * @return
     */
    int getIpLimit(int companyId);

    /**
     * 修改iplimt
     *
     * @param ipLimit
     * @param companyId
     * @return
     */
    void editIpLimit(@Param("ipLimit") Integer ipLimit, @Param("companyId") int companyId);

    /**
     * 获取系统中的企业信息列表
     *
     * @return
     */
    List<CompanyPO> listComp(int type);

    /**
     * 获取筛客平均的企业列表
     *
     * @return
     */
    List<CompanyPO> listSkAvgComp();

    /**
     * 更改咨询类型(客资校验是否忽略咨询类型)
     *
     * @return
     */
    void editTypeRepeat(@Param("typeRepeat") Boolean srcRepeat, @Param("companyId") Integer companyId);

    /**
     * 更改渠道类型(客资校验是否忽略渠道类型)
     *
     * @return
     */
    void editTypeSrcRepeat(@Param("srcRepeat") boolean srcRepeat, @Param("companyId") int companyId);

    /**
     * 更改客资录入时间和最后操作时间,客资状态是否可以重复录
     *
     * @return
     */
    void editKZday(@Param("statusIgnore") String statusIgnore, @Param("timeTypeIgnore") String timeTypeIgnore, @Param("dayIgnore") int dayIgnore, @Param("companyId") int companyId);

    /**
     * 查询哪些客资重复被拦截
     *
     * @return
     */
    CompanyPO selectAll(@Param("companyId") int companyId);

    /**
     * 修改待定是否计入有效
     *
     * @param ddIsValid
     * @param companyId
     */
    void editDdIsValid(@Param("ddIsValid") boolean ddIsValid, @Param("companyId") int companyId);

    /**
     * 修改无效状态以及待跟踪意向等级
     *
     * @param dsinvalDTO
     */
    void editDsInvalid(DsinvalDTO dsinvalDTO);

    /**
     * 搜索无效状态以及跟踪意向等级
     *
     * @param companyId
     * @return
     */
    DsinvalDTO findDsinvalId(@Param("companyId") Integer companyId);

    /**
     * 修改转介绍有效指标定义
     *
     * @param companyId
     * @param zjsValidStatus
     */
    void editZjsValidStatus(@Param("companyId") Integer companyId, @Param("zjsValidStatus") String zjsValidStatus);


    /**
     * 功能描述:
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    int editCompanyZJSSet(@Param("oldClient") String oldClientZjsSet, @Param("qy") String qyZjsSet, @Param("companyId") int companyId);

    /**
     * 功能描述:
     * 获取企业转介绍自定义设置
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    CompanyZjsSetDTO getCompanyZjsSet(@Param("companyId") int companyId);

    /**
     * 获取平均分配默认时间范围
     *
     * @param companyId
     * @return
     */
    int getAvgDefaultTime(@Param("companyId") int companyId);

    /**
     * 客服编辑接待结果
     */
    void editKfEditJdRst(@Param("kfEditJdRst") boolean kfEditJdRst, @Param("companyId") Integer companyId);

    /**
     * 获取转介绍提报必填项设置
     *
     * @param companyId
     * @return
     */
    String getZjsRequiredField(@Param("companyId") int companyId);


    /**
     * 根据手机号码和加密码  获取对应的公司
     *
     * @param phone
     * @param password
     * @return
     */
    List<CompanyVO> getCompanyListByPhoneAndPwd(@Param("phone") String phone, @Param("password") String password);

    /**
     * 修改电商待定自定义 状态
     *
     * @param dsddStatus
     */
    void editDsddStatus(@Param("dsddStatus") String dsddStatus, @Param("companyId") Integer companyId);

    /**
     * 编辑公司报表设置
     *
     * @return
     */
    int editReportsConfig(@Param("companyId") int companyId, @Param("reportsConfig") String reportsConfig);

    /**
     * 是否可以修改手机和微信的权限
     *
     * @param editPhoneAndWechat
     * @param companyId
     */
    void editPhoneAndWechat(@Param("editPhoneAndWechat") boolean editPhoneAndWechat, @Param("companyId") int companyId);

    /**
     * 修改配置
     */
    int editConfig(@Param("companyId") int companyId, @Param("config") String config);
    /**
     * 获取权限
     */
	List<DatavPermissionPo> getPermission(@Param("phone") String phone, @Param("companyId") int companyId);
	/**
     * 获取datav数据
     */
	List<Datav> getDatav(@Param("companyId") int companyId);

    /**
     * 获取公司报表配置
     * @param companyId
     * @return
     */
    CompanyVO getReportConfigById(@Param("companyId") int companyId);
}
