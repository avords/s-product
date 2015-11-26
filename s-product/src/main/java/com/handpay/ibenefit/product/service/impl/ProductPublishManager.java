package com.handpay.ibenefit.product.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.ProductPublishDao;
import com.handpay.ibenefit.product.entity.ProductPublish;
import com.handpay.ibenefit.product.service.IProductPublishManager;

@Service(version = "1.0")
public class ProductPublishManager extends  BaseService<ProductPublish> implements IProductPublishManager{

    @Autowired
    private ProductPublishDao productPublishDao;
    @Override
    public BaseDao<ProductPublish> getDao() {
        return productPublishDao;
    }

    @Override
    public ProductPublish getByObjectId(Long objectId){
    	return super.getByObjectId(objectId);
    }

    @Override
    public void update(Long objectId){
    }

    @Override
    public PageSearch findNoInSellingPublishProduct(PageSearch page) {
        List<ProductPublish> list = productPublishDao.findNoInSellingPublishProduct(page);
        page.setList(list);
        return page;
    }
    @Override
    public PageSearch getWelfarePlanProduct(PageSearch page) {
        List<ProductPublish> list = productPublishDao.getWelfarePlanProduct(page);
        page.setList(list);
        return page;
    }
    @Override
    public PageSearch getPublishProduct(PageSearch page) {
        List<ProductPublish> list = productPublishDao.getPublishProduct(page);
        page.setList(list);
        return page;
    }

    @Override
    public void uploadEnclosure(Long productId, String filePath, String fileName) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("productId", productId);
        map.put("enclosure", filePath);
        map.put("enclosureName", fileName);
        productPublishDao.uploadEnclosure(map);
    }
    
    @Override
	public List<ProductPublish> getUserInsureProduct(Long userId,
			Integer currentYear) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("currentYear", currentYear);
		return productPublishDao.getUserInsureProduct(param);
	}
}
