package com.handpay.ibenefit.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.ProductCompanyPriceDao;
import com.handpay.ibenefit.product.entity.ProductCompanyPrice;
import com.handpay.ibenefit.product.service.IProductCompanyPriceManager;

@Service(version = "1.0")
public class ProductCompanyPriceManager extends BaseService<ProductCompanyPrice> implements IProductCompanyPriceManager {

	@Override
	@Transactional
    public ProductCompanyPrice save(ProductCompanyPrice entity) {
		return super.save(entity);
	}

	@Autowired
	private ProductCompanyPriceDao productCompanyPriceDao;

	@Override
	public BaseDao<ProductCompanyPrice> getDao() {
		return productCompanyPriceDao;
	}

	/**
	 * 取得面向企业价格信息
	 * @param	null
	 * @return	面向企业价List
	 */
	@Override
	public PageSearch getCompanyPriceInfo(PageSearch page) {
	    List<Map<String, Object>> list = productCompanyPriceDao.getCompanyPriceInfo(page);
	    page.setList(list);
	    return page;
	}
}
