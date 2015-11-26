package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.ProductShield;

@Repository
public interface ProductShieldDao extends BaseDao<ProductShield> {

	// 获取企业商品屏蔽信息(sku)
	public List<Map<String, Object>> getShieldDetailInfo();

    public List<Map<String, Object>> getShieldDetailInfo(PageSearch page);

    public List<Map<String, Object>> getShieldInfo(PageSearch page);

    public List<Long> getAllCompanyId();
}
