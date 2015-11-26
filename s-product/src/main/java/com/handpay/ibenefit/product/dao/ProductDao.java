package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.Product;
@Repository
public interface ProductDao extends BaseDao<Product>{

    void saveWelfare(Map<String, Object> map);

    void updateWelfare(Map<String, Object> map);

    void saveProductType(Map<String, Object> map);

    void saveProductPicture(Map<String, Object> map);

    void saveProductTogetherShow(Map<String, Object> map);

    void deleteWelfare(Map<String, Object> m);

    void deleteProductType(Map<String, Object> m);

    void deleteProductTogetherShow(Map<String, Object> m);

    void deleteProductPicture(Map<String, Object> m);

    void saveSellArea(Map<String, Object> map);

    void deleteSellArea(Map<String, Object> m);

    List<Long> getWelfare(Map<String, Object> map);

    List<Integer> getProductType(Map<String, Object> map);

    List<String> getProductPicture(Map<String, Object> map);

    List<Integer> getProductTogetherShow(Map<String, Object> map);

    List<String> getSellArea(Map<String, Object> map);

    void upWelfare(Map<String,Object> map);
    void upProductType(Map<String,Object> map);
    void upProductPicture(Map<String,Object> map);
    void upProductTogetherShow(Map<String,Object> map);
    void upSellArea(Map<String,Object> map);
    /**
     *
     * @Title: getProductsBySubOrderId
     * @Description: 根据子订单Id 获取产品List
     * @param @param subOrderId
     * @param @return
     * @return List<Product>
     * @throws
     * @author 陈传洞
     */
    public List<Product> getProductsBySubOrderId(Long subOrderId);

    void deleteProductPublish(Map<String, Object> m);

    void upProductPublish(Map<String, Object> m);

    Long queryCountByParam(Map<String, Object> map);

	Long getProductbySubitemid(Long objectId);

	List<Product> queryOnshelves(PageSearch page);

	List<Map>queryPicsByProductIds(Map<String,Object> map);

    Long isSelectCountrywide(Long productId);

    void saveSpecPic(@Param("productId") Long productId, @Param("attributeValueId") Long attributeValueId, @Param("url") String url);

    void deleteSpecPic(@Param("productId") Long productId, @Param("attributeValueId") Long attributeValueId);

    String getSpecPic(@Param("productId") Long productId, @Param("attributeValueId") Long attributeValueId);

    void uploadEnclosure(Map<String, Object> map);
}
