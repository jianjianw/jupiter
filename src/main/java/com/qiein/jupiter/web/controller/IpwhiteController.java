package com.qiein.jupiter.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.IpwhiteService;

/**
 * ip白名单
 * 
 * @author XiangLiang 2018/05/16 
 **/
@RestController
@RequestMapping("/ipwhite")
@Validated
public class IpwhiteController extends BaseController{
	
	@Autowired
	private IpwhiteService ipwhiteService;
	/*
	 * 新增
	 */
	@PostMapping("/insert")
	public ResultInfo insert(@Validated @RequestBody IpWhitePO ipWhitePo){
		//获取当前登录用户
		StaffPO staff=getCurrentLoginStaff();
		
		//判断ip 输入是否正确
		String[] ips=ipWhitePo.getIp().split(".");
		if(ips.length!=4){
			return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
		}
		for(int i=0;i<ips.length-1;i++){
			int number=Integer.parseInt(ips[i]);
			if(!(number>=0&&number<=255)){
				return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
				
			}
		}
		if(!ips[3].equals("*")){
			return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
		}
		ipWhitePo.setCreateTime(new Date());
		ipWhitePo.setCompanyId(staff.getCompanyId());
		ipWhitePo.setCreatoerId(staff.getId());
		ipWhitePo.setCreatoerName(staff.getUserName());
		ipwhiteService.insert(ipWhitePo);
		return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
		
	}
	
	@GetMapping("/delete")
	public void delete(){
		
	}
	
	@GetMapping("update")
	public ResultInfo update(@Validated @RequestBody IpWhitePO ipWhitePo){
		return null;
	}

}
