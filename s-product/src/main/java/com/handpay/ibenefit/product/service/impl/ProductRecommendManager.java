/**
 * @Title: ProductRecommendManager.java
 * @Package com.handpay.ibenefit.product.service.impl
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 *
 * @author Mac.Yoon
 * @date 2015年5月21日 下午3:37:47
 * @version V1.0
 */
package com.handpay.ibenefit.product.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.category.entity.ProductRecommend;
import com.handpay.ibenefit.category.service.IProductRecommendManager;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.ProductRecommendDao;

/**
 * @Title: ProductRecommendManager.java
 * @Package com.handpay.ibenefit.product.service.impl
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 *
 * @author Mac.Yoon
 * @date 2015年5月21日 下午3:37:47
 * @version V1.0
 */
@Service(version="1.0")
public class ProductRecommendManager extends BaseService<ProductRecommend> implements IProductRecommendManager{

	@Autowired
	private ProductRecommendDao productRecommentDao;

	@Override
	public void updateSortNoByObjectId(String objectId, String sortNo) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("objectId", objectId);
		param.put("sortNo", Double.parseDouble(sortNo));
		productRecommentDao.updateSortNoByObjectId(param);
	}


	@Override
	public BaseDao<ProductRecommend> getDao() {
		return productRecommentDao;
	}

	@Override
	public PageSearch findList(PageSearch page) {
		List<ProductRecommend> productRecommends =  productRecommentDao.findList(page);
		page.setList(productRecommends);
		return page;
	}


    @Override
    public ProductRecommend getBasicInfoByPositionCode(String positionCode) {
        return productRecommentDao.getBasicInfoByPositionCode(positionCode);
    }

}
