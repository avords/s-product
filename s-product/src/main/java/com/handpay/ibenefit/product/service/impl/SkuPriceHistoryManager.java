package com.handpay.ibenefit.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.SkuPriceHistoryDao;
import com.handpay.ibenefit.product.entity.SkuPriceHistory;
import com.handpay.ibenefit.product.service.ISkuPriceHistoryManager;
@Service(version="1.0")
public class SkuPriceHistoryManager extends BaseService<SkuPriceHistory> implements ISkuPriceHistoryManager{

    @Autowired
    private SkuPriceHistoryDao skuPriceHistoryDao;
    @Override
    public BaseDao<SkuPriceHistory> getDao() {
        return skuPriceHistoryDao;
    }
    @Override
    public PageSearch findSkuPriceHistory(PageSearch pageSearch) {
        List<SkuPriceHistory> list = skuPriceHistoryDao.findSkuPriceHistory(pageSearch);
        pageSearch.setList(list);
        return pageSearch;
    }

}
