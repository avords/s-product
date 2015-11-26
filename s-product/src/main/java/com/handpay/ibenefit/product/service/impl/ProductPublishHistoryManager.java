package com.handpay.ibenefit.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.ProductPublishHistoryDao;
import com.handpay.ibenefit.product.entity.ProductPublishHistory;
import com.handpay.ibenefit.product.service.IProductPublishHistoryManager;
@Service(version = "1.0")
public class ProductPublishHistoryManager extends  BaseService<ProductPublishHistory> implements IProductPublishHistoryManager{

    @Autowired
    private ProductPublishHistoryDao productPublishHistoryDao;
    @Override
    public BaseDao<ProductPublishHistory> getDao() {
        return productPublishHistoryDao;
    }

}
