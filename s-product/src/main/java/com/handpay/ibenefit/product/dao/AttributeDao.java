package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.product.entity.Attribute;
@Repository
public interface AttributeDao extends BaseDao<Attribute>{

    List<Attribute> getByCategoryId(Long categoryId);

    void savePM(Map<String, Object> map);

}
