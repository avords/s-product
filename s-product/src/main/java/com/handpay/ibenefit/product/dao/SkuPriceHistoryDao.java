package com.handpay.ibenefit.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.SkuPriceHistory;
@Repository
public interface SkuPriceHistoryDao extends BaseDao<SkuPriceHistory>{

    List<SkuPriceHistory> findSkuPriceHistory(PageSearch pageSearch);
}
