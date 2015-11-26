package com.handpay.ibenefit.category.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.category.dao.ProductCategoryMappingDao;
import com.handpay.ibenefit.category.entity.ProductCategoryMapping;
import com.handpay.ibenefit.category.service.IProductCategoryMappingManager;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;

@Service(version = "1.0")
public class ProductCategoryMappingManager extends BaseService<ProductCategoryMapping> implements IProductCategoryMappingManager {

	@Override
    public ProductCategoryMapping save(ProductCategoryMapping entity) {
		return super.save(entity);
	}

	@Autowired
	private ProductCategoryMappingDao productCategoryMappingDao;

	@Override
	public BaseDao<ProductCategoryMapping> getDao() {
		return productCategoryMappingDao;
	}

    @Override
    @Transactional
    public void save(Long mallCategoryId, String cetegoryId) {
        if(mallCategoryId!=null){
            ProductCategoryMapping sample = new ProductCategoryMapping();
            sample.setMallCategoryId(mallCategoryId);
            this.deleteBySample(sample);
            if(StringUtils.isNotBlank(cetegoryId)){
                String[] cetegoryIds = cetegoryId.split(",");
                // 保存三级分类映射关系
                for(String id : cetegoryIds){
                    ProductCategoryMapping mapping = new ProductCategoryMapping();
                    mapping.setMallCategoryId(mallCategoryId);
                    mapping.setProductCategoryId(Long.parseLong(id));
                    this.save(mapping);
                }
            }
        }
    }

}
