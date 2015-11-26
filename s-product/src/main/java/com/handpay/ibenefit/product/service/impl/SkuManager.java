package com.handpay.ibenefit.product.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.IBSConstants;
import com.handpay.ibenefit.ProductConstants;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.SkuDao;
import com.handpay.ibenefit.product.dao.SkuPublishDao;
import com.handpay.ibenefit.product.entity.AttributeValue;
import com.handpay.ibenefit.product.entity.Product;
import com.handpay.ibenefit.product.entity.ProductPublishHistory;
import com.handpay.ibenefit.product.entity.Sku;
import com.handpay.ibenefit.product.entity.SkuCheck;
import com.handpay.ibenefit.product.entity.SkuHomeView;
import com.handpay.ibenefit.product.entity.SkuPublish;
import com.handpay.ibenefit.product.service.ICompanyGoodsManager;
import com.handpay.ibenefit.product.service.IProductManager;
import com.handpay.ibenefit.product.service.IProductPublishManager;
import com.handpay.ibenefit.product.service.ISkuCheckManager;
import com.handpay.ibenefit.product.service.ISkuManager;
import com.handpay.ibenefit.search.service.ISolrIndexUpdateManager;
@Service(version="1.0")
public class SkuManager extends BaseService<Sku> implements ISkuManager{

    private static final Logger LOGGER = Logger.getLogger(SkuManager.class);

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private SkuPublishDao skuPublishDao;
    @Autowired
    private IProductManager productManager;
    @Autowired
    private ProductPublishHistoryManager productPublishHistoryManager;
    @Autowired
    private SkuPublishManager skuPublishManager;
    @Autowired
    private ICompanyGoodsManager companyGoodsManager;
    @Reference(version="1.0",check=false)
    private ISolrIndexUpdateManager solrIndexUpdateManager;
    @Autowired
    private ISkuCheckManager skuCheckManager;
    @Autowired
    private IProductPublishManager productPublishManager;


    @Override
    public BaseDao<Sku> getDao() {
        return skuDao;
    }

    @Override
    public PageSearch findSkuByGroup(PageSearch page) {
        page.setList(skuDao.findSkuByGroup(page));
        return page;
    }

    @Override
    public PageSearch findSkuDirect(PageSearch page) {
        page.setList(skuDao.findSkuDirect(page));
        return page;
    }

    private void modifySku(Map<String, Object> map) {
        skuDao.modifySku(map);
    }

    private void upSku(Long objectId){
        Sku sku = getByObjectId(objectId);
        if(sku!=null){
            SkuPublish skuDelete = new SkuPublish();
            skuDelete.setObjectId(objectId);
            skuPublishDao.deleteByObject(skuDelete);

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("skuId", objectId);
            skuDao.upSku(map);
            skuPublishManager.update(objectId);
        }
    }

    private void updateSkuCheckStatus(Map<String, Object> map) {
        skuDao.updateSkuCheckStatus(map);
    }

    @Transactional
    public void executeUpProducts(List<Long> skuIds){
        Set<Long> productIds = new HashSet<Long>();
        for(Long skuId : skuIds){
            Sku sku = getByObjectId(skuId);
            sku.setCheckStatus(ProductConstants.PRODUCT_STATUS_IN_SALE);
            sku.setStartDate(new Date());
            sku = save(sku);
            //上架商品属性
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("productId", sku.getProductId());
            param.put("status", new Integer(1));
            productManager.updateProductProperties(param);
            //上架单个sku
            upSku(skuId);
            //记录商品发布时间
            //插入商品发布表历史表
            ProductPublishHistory entity = new ProductPublishHistory();
            entity.setProductId(sku.getProductId());
            entity.setPublishDate(new Date());
            entity.setSkuId(skuId);
            entity.setEndDate(sku.getEndDate());
            productPublishHistoryManager.save(entity);
            productIds.add(sku.getProductId());
        }
        //更新权限数据
        for(Long productId : productIds){
        	productPublishManager.update(productId);
            companyGoodsManager.updateByProductId(productId);

        }
    }

    private void renewSearch(List<Long> skuIds){
        for(Long skuId : skuIds){
            solrIndexUpdateManager.addSku(skuId);
        }
    }

    @Override
    public void upProducts(List<Long> skuIds) {
        executeUpProducts(skuIds);
        try{
            renewSearch(skuIds);
        }catch(Exception e){
            LOGGER.error("solrIndexUpdateManager error");
        }
    }

    @Override
    @Transactional
    public void executeDownProducts(List<Long> skuIds) {
    	Set<Long> productIds = new HashSet<Long>();
		for (Long skuId : skuIds) {
			Sku sku = getByObjectId(skuId);
			sku.setCheckStatus(ProductConstants.PRODUCT_STATUS_NOT_SALE);
			sku.setEndDate(new Date());
			sku = save(sku);
			// 更新正式表中的状态为已下架
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("objectId", skuId);
			map.put("checkStatus", ProductConstants.PRODUCT_STATUS_NOT_SALE);
			skuPublishManager.modifySkuPublish(map);
			skuPublishManager.update(skuId);
			productIds.add(sku.getProductId());
			//更新索引文档
			try{
	            solrIndexUpdateManager.deleteProdDocBySkuId(skuId.toString());
			}catch(Exception e){
			    LOGGER.error("delete solr index by skuId have a error!",e);
			}
		}
		//更新权限数据
		for(Long productId : productIds){
			productPublishManager.update(productId);
    		companyGoodsManager.updateByProductId(productId);
    	}
    }

	@Override
	public List<Sku> getSkusByProductId(Long productId) {
		Sku sku = new Sku();
		sku.setDeleted(IBSConstants.STATUS_NO);
		sku.setProductId(productId);
		return getBySample(sku);
	}

	@Override
	public List<Sku> querySkuList(Map<String, Object> map) {
		List<Sku> skuList = skuDao.querySkuList(map);
		return skuList;
	}
    @Override
    public List<AttributeValue> getAllAttribute1(Long productId) {
        return skuDao.getAllAttribute1(productId);
    }

    @Override
    public List<AttributeValue> getAllAttribute2(Long productId) {
        return skuDao.getAllAttribute2(productId);
    }

	@Override
	public List<SkuHomeView> getRecommendProductSku(Integer count,String areaId,String priceZone) {
		Map<String,Object> param = new HashMap<String,Object>();
        param.put("count", count);
        if(areaId!=null&&!"".equals(areaId)&&areaId.startsWith("all")){
			param.put("areaId", null);
		}else{
			param.put("areaId", areaId);
		}

		if(priceZone !=null && !"".equals(priceZone)){
			String[] priceZones = priceZone.split("-");
			if(priceZones.length>1){
				param.put("priceZoneAfter", priceZones[0]);
				param.put("priceZoneBefore", priceZones[1]);
			}else if(!priceZone.startsWith("all")){
				param.put("priceZoneAfter", priceZones[0]);
			}
		}
		return skuDao.getRecommendProductSku(param);
	}

	@Override
	public PageSearch getRecommendProductSkuPagination(PageSearch page) {
		page.setList(skuDao.getRecommendProductSkuPagination(page));
		return page;
	}


	@Override
	public Sku getSkuDetail(Long skuId) {
		Sku sku = getByObjectId(skuId);
		Product product = productManager.getByObjectId(sku.getProductId());
		sku.setProduct(product);
		return sku;
	}

    @Override
    public boolean searchSkuStatus(Map<String, Object> par) {
        Long count = skuDao.searchSkuStatus(par);
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public Long getStock(Map<String, Object> param) {
        return skuDao.getStock(param);
    }

    @Override
    @Transactional
    public void executeCheck(Long skuId, Integer checkStatus, SkuCheck sc) {
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("objectId", skuId);
        param.put("checkStatus", checkStatus);
        this.updateSkuCheckStatus(param);
        skuCheckManager.save(sc);
    }

    @Override
    @Transactional
    public void updateSku(Map<String, Object> map) {
        this.modifySku(map);
        skuPublishManager.modifySkuPublish(map);
    }

    @Override
    public void exeAutoUpShelves() {
        Integer currentPage = 1;
        Integer pageSize = 100;
        while(true){
            List<Long> skuIds = skuDao.getNeedUpShelves(currentPage, pageSize);
            if(skuIds.size()==0){
                return;
            }else{
                 upProducts(skuIds);
            }
            currentPage++;
        }
    }

    @Override
    public void exeAutoDownShelves() {
        Integer currentPage = 1;
        Integer pageSize = 100;
        while(true){
            List<Long> skuIds = skuDao.getNeedDownShelves(currentPage, pageSize);
            if(skuIds.size()==0){
                return;
            }else{
                 executeDownProducts(skuIds);
            }
            currentPage++;
        }
    }

}
