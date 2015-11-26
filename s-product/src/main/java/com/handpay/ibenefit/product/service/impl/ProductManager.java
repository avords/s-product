package com.handpay.ibenefit.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.IBSConstants;
import com.handpay.ibenefit.ProductConstants;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.entity.AuditEntity;
import com.handpay.ibenefit.framework.entity.ForeverEntity;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.service.IDictionaryManager;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.dao.ProductDao;
import com.handpay.ibenefit.product.entity.Product;
import com.handpay.ibenefit.product.entity.Sku;
import com.handpay.ibenefit.product.entity.SkuPriceHistory;
import com.handpay.ibenefit.product.service.IProductManager;
import com.handpay.ibenefit.product.service.ISkuManager;
import com.handpay.ibenefit.product.service.ISkuPriceHistoryManager;
import com.handpay.ibenefit.security.entity.User;
import com.handpay.ibenefit.security.service.IUserManager;

@Service(version="1.0")
public class ProductManager extends  BaseService<Product> implements IProductManager{

    @Autowired
    private ProductDao productDao;
    @Reference(version="1.0",check=false)
    private ISkuManager skuManager;
    @Reference(version="1.0",check=false)
    private ISkuPriceHistoryManager skuPriceHistoryManager;
    @Reference(version="1.0",check=false)
    private IUserManager userManager;
    @Reference(version="1.0",check=false)
    private IDictionaryManager dictionaryManager;

    @Override
    public BaseDao<Product> getDao() {
        return productDao;
    }
    @Override
    @Transactional
    public Product save(Product entity) {
        if(entity.getObjectId()==null){
        }
        return super.save(entity);
    }

    @Override
    public void saveWelfare(Map<String, Object> map) {
        productDao.saveWelfare(map);
    }
    @Override
    public void updateWelfare(Map<String, Object> map) {
        productDao.updateWelfare(map);
    }
    private void saveProductType(Map<String, Object> map) {
        productDao.saveProductType(map);
    }
    private void saveProductPicture(Map<String, Object> map) {
        productDao.saveProductPicture(map);
    }
    private void saveProductTogetherShow(Map<String, Object> map) {
        productDao.saveProductTogetherShow(map);
    }
    @Override
    public void deleteWelfare(Map<String, Object> m) {
        productDao.deleteWelfare(m);
    }
    private void deleteProductType(Map<String, Object> m) {
        productDao.deleteProductType(m);
    }
    private void deleteProductTogetherShow(Map<String, Object> m) {
        productDao.deleteProductTogetherShow(m);
    }
    private void deleteProductPicture(Map<String, Object> m) {
        productDao.deleteProductPicture(m);
    }
    private void saveSellArea(Map<String, Object> map) {
        productDao.saveSellArea(map);
    }
    private void deleteSellArea(Map<String, Object> m) {
        productDao.deleteSellArea(m);
    }
    @Override
    public List<Long> getWelfare(Map<String, Object> map) {
        return productDao.getWelfare(map);
    }
    @Override
    public List<Integer> getProductType(Map<String, Object> map) {
        return productDao.getProductType(map);
    }
    @Override
    public List<String> getProductPicture(Map<String, Object> map) {
        return productDao.getProductPicture(map);
    }
    @Override
    public List<Integer> getProductTogetherShow(Map<String, Object> map) {
        return productDao.getProductTogetherShow(map);
    }
    @Override
    public List<String> getSellArea(Map<String, Object> map) {
        return productDao.getSellArea(map);
    }
    @Override
    @Transactional
    public void updateProductProperties(Map<String, Object> m) {
        //清空
        productDao.deleteWelfare(m);
        productDao.deleteProductPicture(m);
        productDao.deleteProductTogetherShow(m);
        productDao.deleteProductType(m);
        productDao.deleteSellArea(m);
        productDao.deleteProductPublish(m);
        productDao.upProductPicture(m);
        productDao.upProductTogetherShow(m);
        productDao.upProductType(m);
        productDao.upSellArea(m);
        productDao.upWelfare(m);
        productDao.upProductPublish(m);
    }

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
	@Override
	public List<Product> getProductsBySubOrderId(Long subOrderId) {
		List<Product> products = new ArrayList<Product>();
		if(subOrderId != null){
			products = productDao.getProductsBySubOrderId(subOrderId);
		}
		return products;
	}
    @Override
    public Long queryCountByParam(Map<String, Object> map) {
        return productDao.queryCountByParam(map);
    }
	@Override
	public boolean cansubitemInvalid(Long objectId) {
		Long productcount = productDao.getProductbySubitemid(objectId);
		if(productcount > 0){
			return false;
		}
		return true;
	}
    private void saveWelfare(String welfareIdsStr, Long productId, Integer status) {
      //根据状态和商品id情况数据
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("productId", productId);
        m.put("status", status);
        this.deleteWelfare(m);
        if(StringUtils.isNotBlank(welfareIdsStr)){
            String[] welfareIds = welfareIdsStr.split(",");
            for(String welfareId:welfareIds){
                Long id = Long.parseLong(welfareId);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("productId", productId);
                map.put("welfareId", id);
                map.put("status", status);
                if(productId!=null&&id!=null&&status!=null){
                    this.saveWelfare(map);
                }
            }
        }
    }
    private void saveProductType(String[] productTypesStr, Long productId, Integer status) {
        //根据状态和商品id情况数据
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("productId", productId);
        m.put("status", status);
        this.deleteProductType(m);
        if(productTypesStr!=null){
            for(String type:productTypesStr){
                Integer id = Integer.parseInt(type);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("productId", productId);
                map.put("type", id);
                map.put("status", status);
                if(productId!=null&&id!=null&&status!=null){
                    this.saveProductType(map);
                }
            }
        }
    }
    private void saveProductPicture(String subPicture, Long productId, Integer status) {
      //根据状态和商品id情况数据
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("productId", productId);
        m.put("status", status);
        this.deleteProductPicture(m);
        if(StringUtils.isNotBlank(subPicture)){
            String[] productSubPic = subPicture.split(",");
            for(String filePath:productSubPic){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("productId", productId);
                map.put("url", filePath);
                map.put("status", status);
                if(productId!=null&&StringUtils.isNotBlank(filePath)&&status!=null){
                    this.saveProductPicture(map);
                }
            }
        }
    }
    private void saveSellArea(String sellAreas, Long productId, Integer status) {
      //根据状态和商品id情况数据
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("productId", productId);
        m.put("status", status);
        this.deleteSellArea(m);
        if(StringUtils.isNotBlank(sellAreas)){
            String[] sellAreaCodes = sellAreas.split(",");
            for(String areaCode:sellAreaCodes){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("productId", productId);
                map.put("areaCode", areaCode);
                map.put("status", status);
                if(productId!=null&&StringUtils.isNotBlank(areaCode)&&status!=null){
                    this.saveSellArea(map);
                }
            }
        }
    }
    private void saveProductTogetherShow(Map<String, String> param, Long productId, Integer status) {
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("productId", productId);
        m.put("status", status);
        this.deleteProductTogetherShow(m);

        String attribute1Show = param.get("attribute1Show");
        String attribute2Show = param.get("attribute2Show");
        String attributeId1 = param.get("attributeId1");
        String attributeId2 = param.get("attributeId2");

        Integer isTogether1 = null;
        if(StringUtils.isNotBlank(attribute1Show)){
            isTogether1 = Integer.parseInt(attribute1Show);
        }else{
            isTogether1 = 1;
        }

        Integer isTogether2 = null;
        if(StringUtils.isNotBlank(attribute2Show)){
            isTogether2 = Integer.parseInt(attribute2Show);
        }else{
            isTogether2 = 1;
        }

        if(StringUtils.isNotBlank(attributeId1)){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("productId", productId);
            map.put("attributeId", Long.parseLong(attributeId1));
            map.put("isTogether", isTogether1);
            map.put("status", status);
            if(productId!=null&&attributeId1!=null&&isTogether1!=null&&status!=null){
                this.saveProductTogetherShow(map);
            }
        }
        if(StringUtils.isNotBlank(attributeId2)){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("productId", productId);
            map.put("attributeId", Long.parseLong(attributeId2));
            map.put("isTogether", isTogether2);
            map.put("status", status);
            if(productId!=null&&attributeId2!=null&&isTogether2!=null&&status!=null){
                this.saveProductTogetherShow(map);
            }
        }
    }
    private void saveSku(Long userId,Map<String, String[]> param, Long productId, Integer checkStatus,Integer productType) {
        String[] skuName = param.get("skuName");
        String[] skuObjectId = param.get("skuObjectId");
        String[] skuReturnGoods = param.get("skuReturnGoods");
        String[] skuNo = param.get("skuNo");
        String[] skuProductType = param.get("skuProductType");
        String[] attributeValueId1 = param.get("attributeValueId1");
        String[] attributeValueId2 = param.get("attributeValueId2");
        String[] attributeValue1 = param.get("attributeValue1");
        String[] attributeValue2 = param.get("attributeValue2");
        String[] skuProductNo = param.get("skuProductNo");
        String[] skuProductModel = param.get("skuProductModel");
        String[] skuSupplyPrice = param.get("skuSupplyPrice");
        String[] skuMarketPrice = param.get("skuMarketPrice");
        String[] skuSellPrice = param.get("skuSellPrice");
        String[] skuStock = param.get("skuStock");
        String[] skuSafetyStock = param.get("skuSafetyStock");
        String[] skuIfInvoice = param.get("skuIfInvoice");

        if(skuName!=null){
            for(int i= 0;i<skuName.length;i++){
                Sku sku = new Sku();
                Long skuId = null;
                Integer type = Integer.parseInt(skuProductType[i]);
                if(!type.equals(productType)){
                    continue;
                }
                sku.setType(type);
                if(type.equals(ProductConstants.PRODUCT_TYPE_ELECTRONICS_CARD)){
                    sku.setReturnGoods(IBSConstants.STATUS_NO);
                }else{
                    sku.setReturnGoods(IBSConstants.STATUS_YES);
                }

                if(StringUtils.isNotBlank(skuObjectId[i])){
                    skuId = Long.parseLong(skuObjectId[i]);
                    sku.setObjectId(skuId);
                }
                if(StringUtils.isNotBlank(skuReturnGoods[i])){
                    Integer returnGoods = Integer.parseInt(skuReturnGoods[i]);
                    sku.setReturnGoods(returnGoods);
                }
              //生成sku编号
                if(StringUtils.isBlank(skuNo[i])){
                    //String no = productId+"-"+skuPublishManager.getNextId("HP_IBS_PROD_SKU_NO");
                    String no = productId+"-"+(i+1);
                    sku.setSkuNo(no);
                }else{
                    sku.setSkuNo(skuNo[i]);
                }
                sku.setProductId(productId);
                /* if(StringUtils.isBlank(skuObjectId[i])){
                    String name = "["+dictionaryManager.getDictionaryByDictionaryIdAndValue(1101, productType).getName()+"]"+skuName[i];
                    sku.setName(name);
                }else{
                    sku.setName(skuName[i]);
                }*/
                sku.setName(skuName[i]);
                sku.setCheckStatus(checkStatus);
                if(StringUtils.isNotBlank(attributeValueId1[i])){
                    sku.setAttributeId1(Long.parseLong(attributeValueId1[i]));
                    sku.setAttributeValue1(attributeValue1[i]);
                }
                if(StringUtils.isNotBlank(attributeValueId2[i])){
                    sku.setAttributeId2(Long.parseLong(attributeValueId2[i]));
                    sku.setAttributeValue2(attributeValue2[i]);
                }
                sku.setProductNo(skuProductNo[i]);
                sku.setProductModel(skuProductModel[i]);

                Double supplyPrice = Double.parseDouble(skuSupplyPrice[i]);
                sku.setSupplyPrice(supplyPrice);
                Double marketPrice = Double.parseDouble(skuMarketPrice[i]);
                sku.setMarketPrice(marketPrice);
                Double sellPrice = Double.parseDouble(skuSellPrice[i]);
                sku.setSellPrice(sellPrice);
                if(StringUtils.isNotBlank(skuStock[i])){
                    sku.setStock(Long.parseLong(skuStock[i]));
                }else{
                    sku.setStock(0L);
                }
                sku.setSafetyStock(Long.parseLong(skuSafetyStock[i]));
                sku.setIfInvoice(Integer.parseInt(skuIfInvoice[i]));
                Product product = this.getByObjectId(productId);
                sku.setImmediateRelease(product.getImmediateRelease());
                sku.setStartDate(product.getStartDate());
                sku.setEndDate(product.getEndDate());
                //监控sku价格是否有所改变
                if(skuId!=null){
                    Sku skuOld = skuManager.getByObjectId(skuId);
                    Double oldSupplyPrice = skuOld.getSupplyPrice();
                    Double oldMarketPrice = skuOld.getMarketPrice();
                    Double oldSellPrice = skuOld.getSellPrice();
                    if(Math.abs(oldSupplyPrice-supplyPrice)>=0.01||Math.abs(oldMarketPrice-marketPrice)>=0.01
                            ||Math.abs(oldSellPrice-sellPrice)>=0.01){
                        SkuPriceHistory sph = new SkuPriceHistory();
                        sph.setMarketPrice(oldMarketPrice);
                        sph.setSellPrice(oldSellPrice);
                        sph.setSupplyPrice(oldSupplyPrice);
                        sph.setSkuId(skuId);
                        sph.setUpdateDate(new Date());
                        sph.setUpdateUserId(userId);
                        skuPriceHistoryManager.save(sph);
                    }
                }
                Sku s = null;
                //保存sku
                if(skuId!=null){
                    s = skuManager.getByObjectId(skuId);
                    sku.setStockSource(s.getStockSource());
                }

                if(s!=null&&(s.getCheckStatus().equals(ProductConstants.PRODUCT_STATUS_WAIT_CHECK)
                                ||s.getCheckStatus().equals(ProductConstants.PRODUCT_STATUS_IN_SALE))){

                }else{
                    saveSku(sku,userId);
                }
            }
        }
    }

    private Sku saveSku(Sku t,Long userId){
        if(t instanceof AuditEntity){
            AuditEntity baseEntity = t;
            if(baseEntity.getObjectId()==null){
                baseEntity.setCreatedBy(userId);
                baseEntity.setCreatedOn(new Date());
            }else{
                baseEntity.setUpdatedBy(userId);
                baseEntity.setUpdatedOn(new Date());
            }
            if(t.getStock()==null){
                t.setStock(0L);
            }
            if(t instanceof ForeverEntity){
                ((ForeverEntity)baseEntity).setDeleted(ForeverEntity.DELETED_NO);
            }
        }
        return skuManager.save(t);
    }
    @Override
    @Transactional
    public void saveProduct(Map<String, Object> productParam) {
        //得到数据
        Product product = (Product) productParam.get("product");
        String welfareIdsStr = (String) productParam.get("welfareIds");
        String[] productTypesStr = (String[]) productParam.get("productTypes");
        String sellAreas = (String) productParam.get("sellAreas");
        String subPicture = (String) productParam.get("subPicture");
        Map<String,String> togetherShowParam = (Map<String, String>) productParam.get("togetherShowMap");
        Map<String,String[]> skuParam = (Map<String, String[]>) productParam.get("skuMap");
        Long userId = (Long) productParam.get("userId");
        Integer checkStatus = (Integer) productParam.get("checkStatus");
        Integer publishStatus = (Integer) productParam.get("publishStatus");
        Integer platform = (Integer) productParam.get("platform");
        //实际的保存
        Long objectId = product.getObjectId();
        String name = product.getName();
        for(String productType:productTypesStr){
            Integer type = Integer.parseInt(productType);
            /*if(objectId==null){
                product.setObjectId(null);
                String nameTemp = "["+dictionaryManager.getDictionaryByDictionaryIdAndValue(1101, type).getName()+"]"+name;
                product.setName(nameTemp);
            }else{
                product.setName(name);
            }*/
            product = save(product,userId,platform);
            Long productId = product.getObjectId();
          //保存福利主题
            this.saveWelfare(welfareIdsStr, productId, publishStatus);
            //保存商品类型
            this.saveProductType(new String[]{productType}, productId, publishStatus);
            //保存销售区域
            if(product.getIsCountrywide()!=null&&product.getIsCountrywide()==1){
                sellAreas = "-1";
            }
            this.saveSellArea(sellAreas, productId, publishStatus);
            //保存商品副图
            this.saveProductPicture(subPicture, productId, publishStatus);
            //保存聚合展示
            this.saveProductTogetherShow(togetherShowParam, productId, publishStatus);
           //保存sku
            //得到sku type为productType的sku集合
            this.saveSku(userId,skuParam, productId, checkStatus,type);
        }
    }
    private Product save(Product t,Long userId,Integer platform){
		if (platform.equals(ProductConstants.RELEASE_VENDOR)) {
			User user = userManager.getByObjectId(userId);
			if (t instanceof AuditEntity) {
				AuditEntity baseEntity = t;
				if (baseEntity.getObjectId() == null) {
					t.setSupplierId(user.getCompanyId());
					baseEntity.setCreatedBy(userId);
					baseEntity.setCreatedOn(new Date());
				} else {
					baseEntity.setUpdatedBy(userId);
					baseEntity.setUpdatedOn(new Date());
				}
				if (t instanceof ForeverEntity) {
					((ForeverEntity) baseEntity).setDeleted(ForeverEntity.DELETED_NO);
				}
			}
			return this.save(t);
		} else if (platform.equals(ProductConstants.RELEASE_PLATFORM_AGENT)) {
			if (t instanceof AuditEntity) {
				AuditEntity baseEntity = t;
				if (baseEntity.getObjectId() == null) {
					baseEntity.setCreatedBy(userId);
					baseEntity.setCreatedOn(new Date());
				} else {
					baseEntity.setUpdatedBy(userId);
					baseEntity.setUpdatedOn(new Date());
				}
				if (t instanceof ForeverEntity) {
					((ForeverEntity) baseEntity).setDeleted(ForeverEntity.DELETED_NO);
				}
			}
			return this.save(t);
		}
		return null;
    }
	@Override
	public PageSearch queryProductOnshelves(PageSearch page) {
		List<Product> shelves = productDao.queryOnshelves(page);
		page.setList(shelves);
		return page;
	}
    @Override
    public boolean isSelectCountrywide(Long productId) {
        Long count = productDao.isSelectCountrywide(productId);
        if(count.equals(0L)){
            return false;
        }else{
            return true;
        }
    }
    @Override
    public void saveSpecPic(Long productId, Long attributeValueId, String url) {
        //先删除，再插入
        deleteSpecPic(productId, attributeValueId);
        productDao.saveSpecPic(productId,attributeValueId,url);
    }
    @Override
    public void deleteSpecPic(Long productId, Long attributeValueId) {
        productDao.deleteSpecPic(productId,attributeValueId);
    }
    @Override
    public String getSpecPic(Long productId, Long attributeValueId) {
        return productDao.getSpecPic(productId,attributeValueId);
    }
    @Override
    public void uploadEnclosure(Long productId, String filePath, String fileName) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("productId", productId);
        map.put("enclosure", filePath);
        map.put("enclosureName", fileName);
        productDao.uploadEnclosure(map);
    }

}
