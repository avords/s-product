package com.handpay.ibenefit.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.AttributeValueDao;
import com.handpay.ibenefit.product.entity.Attribute;
import com.handpay.ibenefit.product.entity.AttributeValue;
import com.handpay.ibenefit.product.service.IAttributeManager;
import com.handpay.ibenefit.product.service.IAttributeValueManager;
import com.handpay.ibenefit.product.service.IProductManager;
@Service(version="1.0")
public class AttributeValueManager  extends BaseService<AttributeValue> implements IAttributeValueManager{

    @Autowired
    private AttributeValueDao attributeValueDao;
    @Reference(version="1.0",check=false)
    private IAttributeManager attributeManager;
    @Reference(version="1.0",check=false)
    private IProductManager productManager;
    @Override
    public BaseDao<AttributeValue> getDao() {
        return attributeValueDao;
    }
    @Override
    public List<AttributeValue> getByAttributeId(Long attributeId) {
        return attributeValueDao.getByAttributeId(attributeId);
    }
    @Override
    @Transactional
    public void save(Map<String, Object> param) {
      //属性

        Long categoryId = (Long) param.get("categoryId");
        Long thirdId = (Long) param.get("thirdId");

        String attributeObjectId1 = (String) param.get("attributeObjectId1");
        String name1 = (String) param.get("name1");
        String attributeObjectId2 = (String) param.get("attributeObjectId2");
        String name2 = (String) param.get("name2");

        String[] objectId1s = (String[]) param.get("objectId1s");
        String[] attributeValue1s = (String[]) param.get("attributeValue1s");
        String[] sortNo1s = (String[]) param.get("sortNo1s");

        String[] objectId2s = (String[]) param.get("objectId2s");
        String[] attributeValue2s = (String[]) param.get("attributeValue2s");
        String[] sortNo2s = (String[]) param.get("sortNo2s");
        if(StringUtils.isNotBlank(name1)){
            Attribute att = new Attribute();
            if(StringUtils.isNotBlank(attributeObjectId1)){
                Long attributeObjectId = Long.parseLong(attributeObjectId1);
                att.setObjectId(attributeObjectId);
            }
            att.setCategoryId(categoryId);
            att.setName(name1);
            att = attributeManager.save(att);
            Long attributeId = att.getObjectId();
            //插入属性值1
            for(int i=0;i<objectId1s.length;i++){
                Long objectId = null;
                if(StringUtils.isNotBlank(objectId1s[i])){
                    objectId = Long.parseLong(objectId1s[i]);

                }
                String value = attributeValue1s[i];
                Double sortNo = Double.parseDouble(sortNo1s[i]);
                AttributeValue attVal = new AttributeValue();
                attVal.setObjectId(objectId);
                attVal.setAttributeValue(value);
                attVal.setAttributeId(attributeId);
                attVal.setSortNo(sortNo);
                //保存属性值1
                this.save(attVal);
            }
        }
        if(StringUtils.isNotBlank(name2)){
            Attribute att = new Attribute();
            if(StringUtils.isNotBlank(attributeObjectId2)){
                Long attributeObjectId = Long.parseLong(attributeObjectId2);
                att.setObjectId(attributeObjectId);
            }
            att.setCategoryId(categoryId);
            att.setName(name2);
            att = attributeManager.save(att);
            Long attributeId = att.getObjectId();
            //插入属性值2
            for(int i=0;i<objectId2s.length;i++){
                Long objectId = null;
                if(StringUtils.isNotBlank(objectId2s[i])){
                    objectId = Long.parseLong(objectId2s[i]);
                }
                String value = attributeValue2s[i];
                Double sortNo = Double.parseDouble(sortNo2s[i]);
                AttributeValue attVal = new AttributeValue();
                attVal.setObjectId(objectId);
                attVal.setAttributeValue(value);
                attVal.setAttributeId(attributeId);
                attVal.setSortNo(sortNo);
                //保存属性值2
                this.save(attVal);
            }
        }
    }

}
