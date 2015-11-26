/**
 * @Title: ProductRecommendDao.java
 * @Package com.handpay.ibenefit.product.dao
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 *
 * @author Mac.Yoon
 * @date 2015年5月21日 下午3:39:02
 * @version V1.0
 */
package com.handpay.ibenefit.product.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.handpay.ibenefit.category.entity.ProductRecommend;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;

/**
 * @Title: ProductRecommendDao.java
 * @Package com.handpay.ibenefit.product.dao
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 *
 * @author Mac.Yoon
 * @date 2015年5月21日 下午3:39:02
 * @version V1.0
 */
public interface ProductRecommendDao extends BaseDao<ProductRecommend>{

	/**
	  * updateSortNoByObjectId(这里用一句话描述这个方法的作用)
	  *
	  * @Title: updateSortNoByObjectId
	  * @Description: TODO
	  * @param @param param    设定文件
	  * @return void    返回类型
	  * @throws
	  */
	void updateSortNoByObjectId(HashMap<String, Object> param);

	/**
	  * findList(这里用一句话描述这个方法的作用)
	  *
	  * @Title: findList
	  * @Description: TODO
	  * @param @param page
	  * @param @return    设定文件
	  * @return PageSearch    返回类型
	  * @throws
	  */
	List<ProductRecommend> findList(PageSearch page);

    ProductRecommend getBasicInfoByPositionCode(@Param("positionCode")String positionCode);

}
