package com.handpay.ibenefit.product.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.product.entity.CompanyGoods;

@Repository
public interface CompanyGoodsDao extends BaseDao<CompanyGoods>{
	
	/**
	 * 更新企业权限商品的"面向企业价格"
	 * 根据ID排倒序，取最新的价格
	 * @param companyIds
	 */
	public void updateCompanySkuPrice(HashMap<String, Object> params);
	
	/**
	 * 更新所有企业的"面向企业价格"
	 * 根据ID排倒序，取最新的价格
	 */
	public void updateAllCompanySkuPrice();
	
	/**
	 * 根据产品ID删除所属SKU权限信息
	 * @param productId
	 */
	public void deleteCompanyGoodsByProductId(Long productId);
	
	/**
	 * 更新企业权限套餐的"面向企业价格"
	 * 根据ID排倒序，取最新的价格
	 * @param companyIds
	 */
	public void updateCompanyPackagePrice(HashMap<String, Object> params);
	
}
