package com.handpay.ibenefit.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.Brand;

@Repository
public interface BrandDao extends BaseDao<Brand>{

    List<Brand> getBrandsByCount(Long brandCount);

    List<Brand> getPublishBrands(PageSearch pageSearch);

    List<Brand> getRecommendBrands(PageSearch pageSearch);
}
