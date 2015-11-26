package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.Cart;
@Repository
public interface CartDao extends BaseDao<Cart>{

    void updateCountByParam(Map<String, Object> param);

    Long queryCountByUserId(Map<String, Object> map);

    List<Cart> getCarts(PageSearch page);

}
