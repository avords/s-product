package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.AttributeValue;
import com.handpay.ibenefit.product.entity.Sku;
import com.handpay.ibenefit.product.entity.SkuHomeView;
@Repository
public interface SkuDao extends BaseDao<Sku>{

    List<Sku> findSkuByGroup(PageSearch page);

    List<Sku> findSkuDirect(PageSearch page);

    void modifySku(Map<String,Object> map);

    void upSku(Map<String, Object> map);

    void updateSkuCheckStatus(Map<String, Object> map);

    List<Sku> getNotPass(Map<String, Object> p);

    List<Sku> querySkuList(Map<String, Object> map);

    List<AttributeValue> getAllAttribute1(Long productId);

    List<AttributeValue> getAllAttribute2(Long productId);

	/**
	  * getRecommendProductSku(查询推荐商品SKU)
	  *
	  * @Title: getRecommendProductSku
	  * @Description: TODO
	  * @param @return    设定文件
	  * @return List<SkuHomeView>    返回类型
	  * @throws
	  */
	List<SkuHomeView> getRecommendProductSku(Map<String, Object> map);
	List<SkuHomeView>  getRecommendProductSkuPagination(PageSearch page);
	/**
	  * getSkuDetail(根据SKU ID 获取商品详情)
	  *
	  * @Title: getSkuDetail
	  * @Description: TODO
	  * @param @param param
	  * @param @return    设定文件
	  * @return Sku    返回类型
	  * @throws
	  */
	Sku getSkuDetail(Map<String, Object> param);

    Long searchSkuStatus(Map<String, Object> par);

    Long getStock(Map<String, Object> param);
    /**
     * @author 闫冬全
     * 得到需要自动上架的skuId的集合
     * @param currentPage 当前页
     * @param pageSize 大小
     * @return List<Long>
     */
    List<Long> getNeedUpShelves(@Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);

    /**
     * @author 闫冬全
     * 得到需要自动下架的skuId的集合
     * @param currentPage 当前页
     * @param pageSize 大小
     * @return List<Long>
     */
    List<Long> getNeedDownShelves(@Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize);
}
