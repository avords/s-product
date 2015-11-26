package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.ProductCompanyPrice;

@Repository
public interface ProductCompanyPriceDao extends BaseDao<ProductCompanyPrice> {

    public List<Map<String, Object>> getCompanyPriceInfo(PageSearch page);

}
