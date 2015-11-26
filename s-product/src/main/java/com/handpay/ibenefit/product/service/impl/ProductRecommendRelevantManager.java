/**
 * @Title: ProductRecommendRelevantManager.java
 * @Package com.handpay.ibenefit.product.service.impl
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * 
 * @author Mac.Yoon
 * @date 2015年5月19日 下午8:54:03
 * @version V1.0
 */
package com.handpay.ibenefit.product.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.category.entity.ProductRecommendRelevant;
import com.handpay.ibenefit.category.service.IProductRecommendRelevantManager;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.ProductRecommendRelevantDao;

/**
 * @Title: ProductRecommendRelevantManager.java
 * @Package com.handpay.ibenefit.product.service.impl
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * 
 * @author Mac.Yoon
 * @date 2015年5月19日 下午8:54:03
 * @version V1.0
 */
@Service(version = "1.0")
public class ProductRecommendRelevantManager extends BaseService<ProductRecommendRelevant> implements IProductRecommendRelevantManager{

	@Autowired
	private ProductRecommendRelevantDao  productRecommendRelevantDao;
	
	@Override
	public BaseDao<ProductRecommendRelevant> getDao() {
		return productRecommendRelevantDao;
	}

	@Override
	public void updatePriorityByObjectId(String id, String priority) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("objectId", id);
		param.put("priority", Double.parseDouble(priority));
		productRecommendRelevantDao.updatePriorityByObjectId(param);
	}
}
