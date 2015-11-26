package com.handpay.ibenefit.product.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.SkuCheckDao;
import com.handpay.ibenefit.product.entity.SkuCheck;
import com.handpay.ibenefit.product.service.ISkuCheckManager;
@Service(version="1.0")
public class SkuCheckManager extends BaseService<SkuCheck> implements ISkuCheckManager {

    @Autowired
    private SkuCheckDao skuCheckDao;
    @Override
    public BaseDao<SkuCheck> getDao() {
        return skuCheckDao;
    }
    @Override
    public SkuCheck getLatelyCheckRecord(Map<String, Object> map) {
        return skuCheckDao.getLatelyCheckRecord(map);
    }

}
