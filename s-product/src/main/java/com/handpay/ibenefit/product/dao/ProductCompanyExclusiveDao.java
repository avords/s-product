package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.ProductCompanyExclusive;

@Repository
public interface ProductCompanyExclusiveDao extends BaseDao<ProductCompanyExclusive> {

	// 获取企业专属商品信息
	public List<Map<String, Object>> getExclusiveProductInfo(PageSearch page);

	/**
	 * 根据企业查询所有专属的商品ID
	 * @param companyId
	 * @return
	 */
	public List<Long> getProductIdsByCompanyId(long companyId);
	
	/**
	 * 查询所有专属商品
	 * @return
	 */
	public List<Long> getAllExclusiveProduct();
	
	/**
	 * 根据产品查找专属的企业ID
	 * @param productId
	 * @return
	 */
	public List<Long> getExclusiveCompanyId(Long productId);

}
