package com.handpay.ibenefit.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.ProductCompanyExclusiveDao;
import com.handpay.ibenefit.product.entity.ProductCompanyExclusive;
import com.handpay.ibenefit.product.service.IProductCompanyExclusiveManager;

@Service(version = "1.0")
public class ProductCompanyExclusiveManager extends BaseService<ProductCompanyExclusive> implements IProductCompanyExclusiveManager {

	@Override
    public ProductCompanyExclusive save(ProductCompanyExclusive entity) {
		super.save(entity);
		return entity;
	}

	@Autowired
	private ProductCompanyExclusiveDao productCompanyExclusiveDao;

	@Override
	public BaseDao<ProductCompanyExclusive> getDao() {
		return productCompanyExclusiveDao;
	}

	/**
	 * 取得专属商品信息
	 * @param	null
	 * @return	专属商品List
	 */
	@Override
	public PageSearch getExclusiveProductInfo(PageSearch page) {
	    List<Map<String, Object>> list = productCompanyExclusiveDao.getExclusiveProductInfo(page);
	    page.setList(list);
	    return page;
	}

	@Override
	public List<Long> getProductIdsByCompanyId(long companyId){
		return productCompanyExclusiveDao.getProductIdsByCompanyId(companyId);
	}

	@Override
	public List<Long> getAllExclusiveProduct() {
		return productCompanyExclusiveDao.getAllExclusiveProduct();
	}

	@Override
	public List<Long> getExclusiveCompanyId(Long productId) {
		return productCompanyExclusiveDao.getExclusiveCompanyId(productId);
	}
}
