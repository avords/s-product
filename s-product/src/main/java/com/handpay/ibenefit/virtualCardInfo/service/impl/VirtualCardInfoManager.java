package com.handpay.ibenefit.virtualCardInfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.virtualCardInfo.VirtualCardInfo;
import com.handpay.ibenefit.virtualCardInfo.dao.VirtualCardInfoDao;
import com.handpay.ibenefit.virtualCardInfo.service.IVirtualCardInfoManager;


/**
 * 
 * @author zhliu
 * @date 2015年6月11日
 * @parm
 */
@Service(version = "1.0")
public class VirtualCardInfoManager extends BaseService<VirtualCardInfo>  implements IVirtualCardInfoManager {

	@Autowired
    private VirtualCardInfoDao virtualCardInfoDao;
	@Override
	public BaseDao<VirtualCardInfo> getDao() {
		return virtualCardInfoDao;
	}
	
	
	
	
	
}
