package com.handpay.ibenefit.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.SkuPublishViewDao;
import com.handpay.ibenefit.product.entity.SkuPublishView;
import com.handpay.ibenefit.product.service.ISkuPublishViewManager;

@Service(version = "1.0")
public class SkuPublishViewManager extends BaseService<SkuPublishView> implements ISkuPublishViewManager{

	@Autowired
	private SkuPublishViewDao skuPublishViewDao;
	@Override
	public BaseDao<SkuPublishView> getDao() {
		return skuPublishViewDao;
	}

}
