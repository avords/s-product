package com.handpay.ibenefit.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.category.entity.ProductCategory;
import com.handpay.ibenefit.category.service.IProductCategoryManager;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.AttributeDao;
import com.handpay.ibenefit.product.entity.Attribute;
import com.handpay.ibenefit.product.entity.AttributeValue;
import com.handpay.ibenefit.product.service.IAttributeManager;
import com.handpay.ibenefit.product.service.IAttributeValueManager;
import com.handpay.ibenefit.security.entity.User;
import com.handpay.ibenefit.security.service.IUserManager;

@Service(version = "1.0")
public class AttributeManager extends BaseService<Attribute> implements IAttributeManager{

    @Autowired
    private AttributeDao attributeDao;
    @Reference(version="1.0",check=false)
    private IUserManager userManager;
    @Reference(version="1.0",check=false)
    private IProductCategoryManager productCategoryManager;
    @Autowired
    private IAttributeValueManager attributeValueManager;

    @Override
    public Attribute getByObjectId(Long objectId){
    	return super.getByObjectId(objectId);
    }

    @Override
    public void update(Long objectId){
    }

    @Override
    public Attribute save(Attribute attribute){
    	attribute = super.save(attribute);
    	update(attribute.getObjectId());
    	return attribute;
    }


    @Override
    public BaseDao<Attribute> getDao() {
        return attributeDao;
    }

    @Override
    public List<Attribute> getByCategoryId(Long categoryId) {
        return attributeDao.getByCategoryId(categoryId);
    }
    @Transactional
    private void savePM(Map<String, Object> map) {
        attributeDao.savePM(map);
    }

    @Override
    @Transactional
    public void savePM(String[] categoryIds, Long userId) {
        for(String thirdId:categoryIds){
            ProductCategory pc = productCategoryManager.getThirdCategoryByThirdId(thirdId);
            Long categoryId = pc.getObjectId();
            Map<String,Object> map = new HashMap<String,Object>();
            User user = userManager.getByObjectId(userId);
            map.put("auditorId", userId);
            map.put("auditorName", user.getUserName());
            map.put("categoryId", categoryId);
            this.savePM(map);
        }
    }

    @Override
    public List<Long> getOtherAttributeValueIdsByAttributeId(Long attributeId) {
        List<Long> attributeValueIds =  new ArrayList<Long>();
        Attribute attribute = getByObjectId(attributeId);
        Long categoryId = attribute.getCategoryId();
        Attribute otherAttribute = new Attribute();
        otherAttribute.setCategoryId(categoryId);
        List<Attribute> attributes = getBySample(otherAttribute);
        Long attributeId2 = null;
        for(Attribute a:attributes){
            if(!a.getObjectId().equals(attributeId)){
                attributeId2 = a.getObjectId();
            }
        }
        if(attributeId2 == null){
            return attributeValueIds;
        }
        List<AttributeValue> attributeValues = attributeValueManager.getByAttributeId(attributeId2);
        for(AttributeValue a : attributeValues){
            attributeValueIds.add(a.getObjectId());
        }
        return attributeValueIds;
    }
}
