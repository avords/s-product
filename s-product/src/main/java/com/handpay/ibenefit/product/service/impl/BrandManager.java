package com.handpay.ibenefit.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.service.ISequenceManager;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.BrandDao;
import com.handpay.ibenefit.product.entity.Brand;
import com.handpay.ibenefit.product.entity.ProductPublish;
import com.handpay.ibenefit.product.service.IBrandManager;
@Service(version = "1.0")
public class BrandManager extends BaseService<Brand> implements IBrandManager{

    @Autowired
    private BrandDao brandDao;

	@Reference(version = "1.0")
	private ISequenceManager sequenceManager;

    @Override
    public BaseDao<Brand> getDao() {
        return brandDao;
    }
    
    public Brand getByObjectId(Long objectId){
    	return super.getByObjectId(objectId);
    }
    
    public void update(Long objectId){
    }
    

    @Override
    @Transactional
    public Brand save(Brand entity) {
        if(entity.getBrandNo()==null){
            Long brandNo = sequenceManager.getNextId("PRODUCT_BRAND_NO");
            entity.setBrandNo(brandNo);
        }
        Brand brand = super.save(entity);
        update(brand.getObjectId());
        return brand;
    }

    @Override
    public List<Brand> getBrandsByCount(Long count) {
        return brandDao.getBrandsByCount(count);
    }

    @Override
    public List<Brand> getPublishBrands(PageSearch pageSearch) {
        return brandDao.getPublishBrands(pageSearch);
    }

    @Override
    public List<Brand> getRecommendBrands(PageSearch pageSearch) {
        return brandDao.getRecommendBrands(pageSearch);
    }
}
