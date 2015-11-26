package com.handpay.ibenefit.product.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.CartDao;
import com.handpay.ibenefit.product.entity.Cart;
import com.handpay.ibenefit.product.service.ICartManager;
@Service(version = "1.0")
public class CartManager extends  BaseService<Cart> implements ICartManager {
    @Autowired
    private CartDao cartDao;
    @Override
    public BaseDao<Cart> getDao() {
        return cartDao;
    }

    @Override
    @Transactional
    public void updateCountByParam(Map<String, Object> param) {
        cartDao.updateCountByParam(param);
    }

    @Override
    @Transactional
    public Cart save(Cart entity) {
        if(entity.getObjectId()==null){
            entity.setAddDate(new Date());
        }
        return super.save(entity);
    }

    @Override
    public List<Cart> getByUserId(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        List<Cart> carts = getBySample(cart);
        return carts;
    }

    @Override
    public Long queryCountByUserId(Long currentUserId,Long companyId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", currentUserId);
        map.put("companyId", companyId);
        return cartDao.queryCountByUserId(map);
    }

    @Override
    public PageSearch getCarts(PageSearch page) {
        List<Cart> carts = cartDao.getCarts(page);
        page.setList(carts);
        return page;
    }
}
