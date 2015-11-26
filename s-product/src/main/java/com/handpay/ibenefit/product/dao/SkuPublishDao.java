package com.handpay.ibenefit.product.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.Sku;
import com.handpay.ibenefit.product.entity.SkuHomeView;
import com.handpay.ibenefit.product.entity.SkuPublish;
@Repository
public interface SkuPublishDao extends BaseDao<SkuPublish>{

    void modifySkuPublish(Map<String,Object> map);

    List<SkuPublish> getImmediatelyPublish();

    List<SkuPublish> getSpecificDatePublish();

    List<SkuPublish> findSkuByGroup(PageSearch page);

    List<SkuPublish> findSkuDirect(PageSearch page);

    Long getStock(Map<String, Object> param);

    List<Sku> findWelfarePackageSku(PageSearch page);

    /**
     * 根据套餐ID 查询 对应的商品
     * @author zhliu
     * @date 2015年6月2日
     * @parm
     * @param map
     * @return
     */
    List<SkuPublish> findWelfarePackageSkuForPackageId(Map map);

    /**
     * 查询 选中商品 信息
     * @author zhliu
     * @date 2015年6月3日
     * @parm
     * @param map
     * @return
     */
    List<SkuPublish> findWelfarePackageSkuForProdIds(Map map);


    /**
     * 根据 选中商品Id 获取不同的供应商信息
     * @author zhliu
     * @date 2015年6月3日
     * @parm
     * @param map
     * @return
     */
    List<Map> findSupplierProdIds(Map map);


    /**
     * 根据子订单号查询 订单中相关商品信息
     * @author zhliu
     * @date 2015年6月4日
     * @parm
     * @param map
     * @return
     */
    List<SkuPublish>  selectSkuPublishForSubOrderNo(Map map);

    List<SkuHomeView> getRecommendProductSkuByParam(PageSearch page);

    List<SkuPublish> getByParam(Map<String, Object> param);

    List<SkuHomeView> getPublishProductSkuByParam(PageSearch page);
    
    List<SkuHomeView> getPublishProductSkuByParamWeixin(PageSearch page);
    

    SkuPublish getSkuPublishPrice(Map<String, Object> map);

    /**
     * 查询供应商内卖商品
     * @param supplierId
     * @return
     */
    public List<SkuPublish> findSkuInner(Map<String,Object> map);

    Long isHavePermission(Map<String, Object> map);
}
