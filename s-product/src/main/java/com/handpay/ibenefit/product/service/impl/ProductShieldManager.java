package com.handpay.ibenefit.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.ProductShieldDao;
import com.handpay.ibenefit.product.entity.ProductShield;
import com.handpay.ibenefit.product.service.IProductShieldManager;

@Service(version = "1.0")
public class ProductShieldManager extends BaseService<ProductShield> implements IProductShieldManager {

	@Override
    public ProductShield save(ProductShield entity) {
		entity = super.save(entity);
		return entity;
	}

	@Autowired
	private ProductShieldDao productShieldDao;

	@Override
	public BaseDao<ProductShield> getDao() {
		return productShieldDao;
	}

	/**
	 * 取得屏蔽商品信息
	 * @param	null
	 * @return	屏蔽商品List
	 */
	@Override
	public List<Map<String, Object>> getShieldDetailInfo() {
		return productShieldDao.getShieldDetailInfo();
	}

    @Override
    public PageSearch getShieldInfo(PageSearch page) {
        List<Map<String, Object>> list = productShieldDao.getShieldInfo(page);
        page.setList(list);
        return page;
    }

    @Override
    public List<Long> getAllCompanyId() {
        return productShieldDao.getAllCompanyId();
    }

}
