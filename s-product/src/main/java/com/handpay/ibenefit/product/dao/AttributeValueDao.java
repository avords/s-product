package com.handpay.ibenefit.product.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.product.entity.AttributeValue;
@Repository
public interface AttributeValueDao extends BaseDao<AttributeValue>{

    List<AttributeValue> getByAttributeId(Long attributeId);

}
