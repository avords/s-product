package com.handpay.ibenefit.product.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.product.entity.SkuCheck;
@Repository
public interface SkuCheckDao extends BaseDao<SkuCheck>{

    SkuCheck getLatelyCheckRecord(Map<String,Object> map);
}
