package com.handpay.ibenefit.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.handpay.ibenefit.product.dao.ProductDao;
import com.handpay.ibenefit.product.dao.SkuPublishDao;
import com.handpay.ibenefit.product.entity.Sku;
import com.handpay.ibenefit.product.entity.SkuHomeView;
import com.handpay.ibenefit.product.entity.SkuPublish;
import com.handpay.ibenefit.product.service.ISkuPublishManager;
import com.handpay.ibenefit.welfare.entity.WelfarePackage;
import com.handpay.ibenefit.welfare.entity.WelfarePackageCategory;
import com.handpay.ibenefit.welfare.service.IWelfarePackageCategoryManager;
import com.handpay.ibenefit.welfare.service.IWelfarePackageManager;
@Service(version="1.0")
public class SkuPublishManager extends BaseService<SkuPublish> implements ISkuPublishManager{

	Logger logger = Logger.getLogger(SkuPublishManager.class);


    @Autowired
    private SkuPublishDao skuPublishDao;
    @Reference(version="1.0")
	private IWelfarePackageManager welfarePackageManager;
    @Autowired
    private ProductDao productDao;
    @Reference(version="1.0")
  	private IWelfarePackageCategoryManager welfarePackageCategoryManager;

    @Override
    public BaseDao<SkuPublish> getDao() {
        return skuPublishDao;
    }
    
    public SkuPublish getByObjectId(Long objectId){
    	return super.getByObjectId(objectId);
    }
    
    public void update(Long objectId){
    }

    @Override
    @Transactional
    public SkuPublish save(SkuPublish entity) {
        if(entity.getObjectId()==null){

        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void modifySkuPublish(Map<String, Object> map) {
        skuPublishDao.modifySkuPublish(map);
    }

    @Override
    public List<SkuPublish> getAllPublish() {
        List<SkuPublish> list1 = getImmediatelyPublish();
        List<SkuPublish> list2 = getSpecificDatePublish();
        list1.addAll(list2);
        return list1;
    }

    public List<SkuPublish> getImmediatelyPublish() {
        return skuPublishDao.getImmediatelyPublish();
    }

    public List<SkuPublish> getSpecificDatePublish() {
        return skuPublishDao.getSpecificDatePublish();
    }

    @Override
    public PageSearch findSkuByGroup(PageSearch page) {
        List<SkuPublish> list = skuPublishDao.findSkuByGroup(page);
        page.setList(list);
        return page;
    }

    @Override
    public PageSearch findSkuDirect(PageSearch page) {
        List<SkuPublish> list = skuPublishDao.findSkuDirect(page);
        page.setList(list);
        return page;
    }

    @Override
    public Long getStock(Map<String, Object> param) {
        return skuPublishDao.getStock(param);
    }

    @Override
    public  PageSearch findWelfarePackageSku(PageSearch page){
    	 List<Sku> list = skuPublishDao.findWelfarePackageSku(page);
    	 page.setList(list);
    	 return page;
    }

    /**
     * 根据套餐ID 查询 对应的商品 若 推荐商品售罄 备用商品 补充
     * @author zhliu
     * @date 2015年6月2日
     * @parm
     * @param map
     * @return
     */
    @Override
    public List<SkuPublish> findWelfarePackageSkuForPackageId(Map map){
    	List<SkuPublish> resultList = new ArrayList<SkuPublish>();

    	try {
    		//根据套餐属性 进行判断是否补位 备选商品
    		Long packageId = Long.valueOf(map.get("packageId").toString());
    		WelfarePackage welfarePackage = welfarePackageManager.getByObjectId(packageId);


    		//套餐属性 ，根据套餐属性 查询相关的 商品
    		WelfarePackageCategory welfarePackageCategory = welfarePackageCategoryManager.getByObjectId(welfarePackage.getWpCategoryId());
    		int firstParameter = welfarePackageCategory.getFirstParameter();


    		//固定套餐 （不许补位）
    		if(welfarePackage.getWpCategoryType() == IBSConstants.WELFARE_PACKAGE_WP_CATEGORY_TYPE_FIXED){
    			map.put("productType", ProductConstants.WP_RELATION_PRODUCT_TYPE_TJ);//查询推荐商品 数量
    			map.put("orderBy", "REL.PRIORITY ASC");
    			List<SkuPublish> tjSkuList = skuPublishDao.findWelfarePackageSkuForPackageId(map);//推荐商品
    			resultList.addAll(tjSkuList);
    			resultList = tjSkuList;

    			logger.info("固定套餐");
    		}else{//弹性套餐 （根据补位规则进行补位）
    			/**
    			 * 补位规则：
    			 * 1、推荐商品 库存或下架  备用商品补位
    			 * 2、推荐商品 和 备用商品 都不足 时，展示推荐商品 但是不能选择该商品
    			 */
    			logger.info("弹性套餐");

    			map.put("productType", ProductConstants.WP_RELATION_PRODUCT_TYPE_TJ);//查询推荐商品 数量
    			List<SkuPublish> tjSkuListAll = skuPublishDao.findWelfarePackageSkuForPackageId(map);//推荐商品（所有的包括有效和无效的）


    			map.put("deleted", IBSConstants.NOT_DELETE);//库存大于0
    			map.put("stock", 0);//库存大于0
        		map.put("productStatus", ProductConstants.PRODUCT_STATUS_IN_SALE);//查询上架中的商品 数量
    	    	map.put("productType", ProductConstants.WP_RELATION_PRODUCT_TYPE_TJ);//查询推荐商品 数量
    	    	map.put("orderBy", "REL.PRIORITY ASC");
    			List<SkuPublish> tjSkuList = skuPublishDao.findWelfarePackageSkuForPackageId(map);//推荐商品（有效的）



    			tjSkuListAll.removeAll(tjSkuList);//剔除有效的商品信息

    			logger.info("剔除后的list"+tjSkuListAll);

    	    	if(tjSkuList.size() < firstParameter){//推荐商品 小于 商品属性 进行备选商品补位
    	    		int lackNo = firstParameter-tjSkuList.size();//缺少商品数量

    	    		map.put("productType", ProductConstants.WP_RELATION_PRODUCT_TYPE_BX);//有效的 备用商品
    	    		map.put("orderBy", "REL.PRIORITY ASC");
    		    	List<SkuPublish> bySkuList =  skuPublishDao.findWelfarePackageSkuForPackageId(map);//备用商品

    	    		if(bySkuList.size() >= lackNo){//如果 备用商品 大于 缺少商品 补充相应的数量即可
    	    			logger.info("备用商品 大于 缺少商品");
    	    			for (int i = 0; i < lackNo; i++) {
    	    				tjSkuList.add(bySkuList.get(i));
    					}
    	    		}else{//如果 备用商品 小于 缺少商品  补充所有备用商品，再补充 库存不足的 推荐商品
    	    			logger.info("备用商品 小于 缺少商品");
    	    			for (int i = 0; i < bySkuList.size(); i++) {
    	    				tjSkuList.add(bySkuList.get(i));
    					}
    	    			//补充推荐商品信息
    	    			int addTJProdCount = lackNo-bySkuList.size();
    	    			if(addTJProdCount <= tjSkuListAll.size()){//缺少 商品 小于 推荐商品数量
    	    				for (int i = 0; i < addTJProdCount; i++) {
        	    				tjSkuList.add(tjSkuListAll.get(i));
    						}
    	    			}else{//缺少 商品 大于 推荐商品数量
    	    				for (int i = 0; i < tjSkuListAll.size(); i++) {
        	    				tjSkuList.add(tjSkuListAll.get(i));
    						}
    	    			}
    				}
    	    	}else{
    	    		logger.info("推荐商品 大于 商品属性");
    	    		for (int i = 0; i < firstParameter; i++) {
    	    			resultList.add(tjSkuList.get(i));
    				}
    	    	}
    	    	resultList = tjSkuList;
    		}


    	} catch (Exception e) {
			e.printStackTrace();
		}

    	return resultList;
    }





    /**
     * 根据商品ID 查询 商品信息
     * @author zhliu
     * @date 2015年6月2日
     * @parm
     * @param map
     * @return
     */
    @Override
    public List<SkuPublish> findWelfarePackageSkuForPordIds(Map map){
    	return skuPublishDao.findWelfarePackageSkuForProdIds(map);
    }

    /**
     *
     * @author zhliu
     * @date 2015年6月3日
     * @parm
     * @param map
     * @return
     */
    @Override
    public List<Map> findSupplierProdIds(Map map){
    	return skuPublishDao.findSupplierProdIds(map);
    }


    /**
     * 根据子订单号查询 订单中相关商品信息
     * @author zhliu
     * @date 2015年6月4日
     * @parm
     * @param map
     * @return
     */
    @Override
    public List<SkuPublish>  selectSkuPublishForSubOrderNo(Map map){
    	return skuPublishDao.selectSkuPublishForSubOrderNo(map);
    }

    @Override
    public PageSearch getRecommendProductSkuByParam(PageSearch page) {
        List<SkuHomeView> list = skuPublishDao.getRecommendProductSkuByParam(page);
        page.setList(list);
        return page;
    }


    @Override
    public List<SkuPublish> getByParam(Map<String, Object> param) {
        return skuPublishDao.getByParam(param);
    }


    @Override
    public PageSearch getPublishProductSkuByParam(PageSearch page) {
        List<SkuHomeView> list = skuPublishDao.getPublishProductSkuByParam(page);
        page.setList(list);
        return page;
    }
    @Override
    public PageSearch getPublishProductSkuByParamWeixin(PageSearch page) {
        List<SkuHomeView> list = skuPublishDao.getPublishProductSkuByParamWeixin(page);
        page.setList(list);
        return page;
    }

    @Override
    public SkuPublish getSkuPublishPrice(Long companyId, Long skuId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("companyId", companyId);
        map.put("skuId", skuId);
        SkuPublish skuPublish = skuPublishDao.getSkuPublishPrice(map);
        return skuPublish;
    }

    /**
     *
     * @Title: queryDetailPic
     * @Description: 查询出产品的明细图并放入SkuPublist
     * @param  skups
     * @return List<SkuPublish>
     * @throws
     * @author 陈传洞
     */
	@Override
	public List<SkuPublish> putDetailPic(List<SkuPublish> skups) {
		List<Long> skupublishIds = new ArrayList<Long>();
		if(skups != null && skups.size()>0){
			//取出 产品Id
			for (SkuPublish skuPublish : skups) {
				skupublishIds.add(skuPublish.getProductId());
			}
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("skupublishIds", skupublishIds);
			map.put("status", ProductConstants.PRODUCT_YES_PUBLISH);
			Map<String,List<String>> productPics = new HashMap<String, List<String>>();
			//根据产品Id 查询图片List
			List<Map> picturesMap = productDao.queryPicsByProductIds(map);
			List<String> pics = new ArrayList<String>();
			if(picturesMap != null){
				for (Map picture : picturesMap) {
					String pId = picture.get("PRODUCT_ID").toString();
					String picUrl = picture.get("URL").toString();
					if(productPics.get(pId) != null){
						pics = productPics.get(pId);
					}else{
						pics = new ArrayList<String>();
					}
					pics.add(picUrl);
					productPics.put(pId, pics);
				}
			}
			//将查询出的产品详情图片放入产品
			for (SkuPublish skuPublish : skups) {
				if(skuPublish.getProductId() != null ){
					List<String> detailPictures = productPics.get(skuPublish.getProductId().toString());
					if(detailPictures == null){
						detailPictures = new ArrayList<String>();
					}
					skuPublish.setDetailPictures(detailPictures);
				}
			}

		}
		return skups;
	}

	/**
     *
     * @Title: getSkuPublishByObjectId
     * @Description: 根据 主键Id 查询包含产品信息的 SkuPublish
     * @param  objectId 主键Id
     * @return SkuPublish
     * @throws
     * @author 陈传洞
     */
	@Override
	public SkuPublish getSkuPublishByObjectId(Long objectId) {
		SkuPublish skuPublish = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objectId", objectId);
		List<SkuPublish> skuPublishs = this.skuPublishDao.getByParam(param );
		if(skuPublishs != null && skuPublishs.size()>0){
			skuPublish = skuPublishs.get(0);
		}
		return skuPublish;
	}

	/**
     * 查询供应商内卖商品
     * @param supplierId
     * @return
     */
	@Override
    public List<SkuPublish> findSkuInner(Map<String,Object> map) {
		return skuPublishDao.findSkuInner(map);
    }


    @Override
    public boolean isHavePermission(Long companyId, Long skuId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("companyId", companyId);
        map.put("skuId", skuId);
        Long count = skuPublishDao.isHavePermission(map);
        if(count>0){
            return true;
        }
        return false;
    }
}
