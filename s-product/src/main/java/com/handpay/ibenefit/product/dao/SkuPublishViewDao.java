package com.handpay.ibenefit.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.product.entity.SkuPublishView;

@Repository
public interface SkuPublishViewDao extends BaseDao<SkuPublishView>{
	
	/**
	 * 查找企业专属商品
	 * @param companyId
	 * @return SKU ID
	 */
	public List<Long> getCompanyExclusiveSku(Long companyId);
	
	/**
	 * 根据供应商查找所属商品
	 * @param supplierId
	 * @return
	 */
	public List<SkuPublishView> getPublishedSkuBySupplierId(Long supplierId); 
	
	/**
	 * 查找所有仅供内卖供应商的商品
	 * @return
	 */
	public List<SkuPublishView> getInSellingSku();
	
}
