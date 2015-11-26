package com.handpay.ibenefit.category.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.ProdSellAreaDao;
import com.handpay.ibenefit.product.entity.ProdSellArea;
import com.handpay.ibenefit.product.service.IProdSellAreaManager;

/**
 * 商品销售区域
 * @author zhliu
 * @date 2015年7月1日
 * @parm
 */
@Service(version = "1.0")
public class ProdSellAreaManager extends BaseService<ProdSellArea> implements IProdSellAreaManager {

	@Autowired
	private ProdSellAreaDao prodSellAreaDao;
	@Override
	public BaseDao<ProdSellArea> getDao() {
		return prodSellAreaDao;
	}
	
	
	
	
}
