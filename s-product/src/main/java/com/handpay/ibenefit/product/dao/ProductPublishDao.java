package com.handpay.ibenefit.product.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.ProductPublish;
@Repository
public interface ProductPublishDao extends BaseDao<ProductPublish>{

    List<ProductPublish> findNoInSellingPublishProduct(PageSearch page);

    List<ProductPublish> getWelfarePlanProduct(PageSearch page);

    List<ProductPublish> getPublishProduct(PageSearch page);

    void uploadEnclosure(Map<String, Object> map);
    
    /**
     * getUserInsureProduct(获取用户在该年份中的投保商品)
     *
     * @Title: getUserInsureProduct
     * @Description: TODO
     * @param @param param
     * @param @return    设定文件
     * @return List<SkuPublish>    返回类型
     * @throws
    */
	List<ProductPublish> getUserInsureProduct(HashMap<String, Object> param);
}
