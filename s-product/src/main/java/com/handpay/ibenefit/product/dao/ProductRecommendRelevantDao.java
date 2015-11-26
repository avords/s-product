/**
 * @Title: ProductRecommendRelevantDao.java
 * @Package com.handpay.ibenefit.product.dao
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * 
 * @author Mac.Yoon
 * @date 2015年5月19日 下午8:58:22
 * @version V1.0
 */
package com.handpay.ibenefit.product.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.category.entity.ProductRecommendRelevant;
import com.handpay.ibenefit.framework.dao.BaseDao;

/**
 * @Title: ProductRecommendRelevantDao.java
 * @Package com.handpay.ibenefit.product.dao
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * 
 * @author Mac.Yoon
 * @date 2015年5月19日 下午8:58:22
 * @version V1.0
 */
@Repository
public interface ProductRecommendRelevantDao extends BaseDao<ProductRecommendRelevant>{

	/**
	 * @param param 
	  * updatePriorityByObjectId(这里用一句话描述这个方法的作用)
	  *
	  * @Title: updatePriorityByObjectId
	  * @Description: TODO
	  * @param     设定文件
	  * @return void    返回类型
	  * @throws
	  */
	void updatePriorityByObjectId(HashMap<String, Object> param);

}
