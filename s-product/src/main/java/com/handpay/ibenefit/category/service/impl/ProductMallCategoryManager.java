package com.handpay.ibenefit.category.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.IBSConstants;
import com.handpay.ibenefit.category.dao.ProductMallCategoryDao;
import com.handpay.ibenefit.category.entity.ProductCategory;
import com.handpay.ibenefit.category.entity.ProductMallCategory;
import com.handpay.ibenefit.category.service.IProductCategoryManager;
import com.handpay.ibenefit.category.service.IProductMallCategoryManager;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.service.ISequenceManager;
import com.handpay.ibenefit.product.entity.Product;
import com.handpay.ibenefit.product.service.IProductManager;

@Service(version = "1.0")
public class ProductMallCategoryManager extends BaseService<ProductMallCategory> implements IProductMallCategoryManager {

	private static final Logger LOGGER = Logger.getLogger(ProductMallCategoryManager.class);

	@Autowired
	private ProductMallCategoryDao productMallCategoryDao;

	@Autowired
	private IProductManager productManager;

	@Autowired
	private IProductCategoryManager productCategoryManager;

	@Reference(version = "1.0")
	private ISequenceManager sequenceManager;

	/**
	 * 保存时三级分类ID的序列化
	 */
	@Override
	@Transactional
    public ProductMallCategory save(ProductMallCategory entity) {
		if (entity.getLayer() == 1) {
			if (null == entity.getFirstId()) {
				Long firstId = sequenceManager.getNextId("SEQ_FIRST_ID");
				entity.setFirstId(firstId);
			}
		}
		else if (entity.getLayer() == 2) {
			if (null == entity.getSecondId()) {
				Long secondId = sequenceManager.getNextId("SEQ_SECOND_ID");
				entity.setSecondId(secondId);
			}
		}
		else if (entity.getLayer() == 3) {
			if (null == entity.getThirdId()) {
				Long thirdId = sequenceManager.getNextId("SEQ_THIRD_ID");
				entity.setThirdId(thirdId);
			}
		} else {

		}
		return super.save(entity);
	}

	@Override
	public BaseDao<ProductMallCategory> getDao() {
		return productMallCategoryDao;
	}

	/**
	 * 一级分类页面统计二级分类数量
	 */
	@Override
	@Transactional
	public int updateSecondCategoryCount() {
		return productMallCategoryDao.updateSecondCategoryCount();
	}

	/**
	 * 二级分类页面统计三级分类数量
	 */
	@Override
	@Transactional
	public int updateThirdCategoryCount() {
		return productMallCategoryDao.updateThirdCategoryCount();
	}

	/**
	 * 根据三级ID得到所有的三级分类
	 */
    @Override
    public List<ProductMallCategory> getAllThirdCategoryByProductThirdId(Map<String, Object> map) {
        return productMallCategoryDao.getAllThirdCategoryByProductThirdId(map);
    }

	@Override
	@Transactional
	public void updateStatus(Long objectId, int status) {
		ProductMallCategory category = new ProductMallCategory();
		category.setObjectId(objectId);
		category.setStatus(status);
		productMallCategoryDao.updateStatus(category);
	}

	@Override
	@Transactional
	public void updateSortNoByObjectId(String objectIds, String sortNos) {
		String[] ids = objectIds.split(",");
		String[] nos = sortNos.split(",");
		for(int i =0; i<ids.length;i++){
			try{
				Long objectId = Long.parseLong(ids[i]);
				Double sortNo = Double.parseDouble(nos[i]);
				ProductMallCategory category = new ProductMallCategory();
				category.setSortNo(sortNo);
				category.setObjectId(objectId);
				productMallCategoryDao.updateSortNo(category);
			}catch (Exception e){
				LOGGER.error("updateSortNoByObjectId",e);
			}
		}
	}

	@Override
	public ProductMallCategory getProductMallCategoryByFirstId(String firstId) {
		ProductMallCategory sample = new ProductMallCategory();
		sample.setFirstId(Long.parseLong(firstId));
		sample.setLayer(1);
		List<ProductMallCategory> list = getBySample(sample);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public ProductMallCategory getProductMallCategoryBySecondId(String secondId) {
		ProductMallCategory sample = new ProductMallCategory();
		sample.setSecondId(Long.parseLong(secondId));
		sample.setLayer(2);
		List<ProductMallCategory> list = getBySample(sample);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public ProductMallCategory getProductMallCategoryByThirdId(String thirdId) {
		ProductMallCategory sample = new ProductMallCategory();
		sample.setThirdId(Long.parseLong(thirdId));
		sample.setLayer(3);
		List<ProductMallCategory> list = getBySample(sample);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 判断一个分类是否能被删除
	 * 1.该分类下面有商品（无论商品是什么状态）都不能删除
	 * 2.二级分类下面有三级分类（无论状态是什么）不能删除该二级分类
	 * 3.一级分类下面有二级分类（无论状态是什么）不能删除该一级分类
	 * @param objectId
	 * @return
	 */
	@Override
    public boolean canDelete(Long objectId) {
		if(objectId==null){
			return false;
		}
		ProductMallCategory category = getByObjectId(objectId);
		if(category.getLayer()==1){
			ProductMallCategory sample = new ProductMallCategory();
			sample.setFirstId(category.getFirstId());
			sample.setLayer(2);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else if(category.getLayer()==2){
			ProductMallCategory sample = new ProductMallCategory();
			sample.setSecondId(category.getSecondId());
			sample.setLayer(3);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else{
			return hasRelatedProduct(objectId);
		}
		return true;
	}

	//根据映射关系找到相应的运营分类，再判断是否有商品关联到该运营分类
	private boolean hasRelatedProduct(Long objectId) {
		List<ProductCategory> categories = productCategoryManager.getProductCategoriesByMallCategoryId(objectId);
		for(ProductCategory productCategory : categories){
			Product sample = new Product();
			sample.setCategoryId(productCategory.getObjectId());
			sample.setDeleted(IBSConstants.STATUS_NO);
			long count = productManager.getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断一个分类是否置为无效
	 * 1.二级分类下面有有效分类一级分类不能为无效
	 * 2.三级分类下面全部为无效则二级可为无效
	 * @param objectId
	 * @return
	 */
	@Override
    public boolean canInvalid(Long objectId) {
		if(objectId==null){
			return true;
		}
		ProductMallCategory category = getByObjectId(objectId);
		if(category.getLayer()==1){
			ProductMallCategory sample = new ProductMallCategory();
			sample.setFirstId(category.getFirstId());
			sample.setStatus(IBSConstants.EFFECTIVE);
			sample.setLayer(2);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else if(category.getLayer()==2){
			ProductMallCategory sample = new ProductMallCategory();
			sample.setSecondId(category.getSecondId());
			sample.setStatus(IBSConstants.EFFECTIVE);
			sample.setLayer(3);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else{
			//return hasRelatedProduct(objectId);
		}
		return true;
	}

    @Override
    public List<ProductMallCategory> getAllFirstCategory(Integer paltform,int count) {
        return productMallCategoryDao.getAllFirstCategory(paltform,count);
    }

    @Override
    public List<ProductMallCategory> getSecondCategoryByParam(Map<String, Object> param) {
        return productMallCategoryDao.getSecondCategoryByParam(param);
    }

    @Override
    public List<ProductMallCategory> getThirdCategoryByParam(Map<String, Object> param) {
        return productMallCategoryDao.getThirdCategoryByParam(param);
    }
    @Override
    public List<ProductMallCategory> getThirdCategoryByParamWeixin(Map<String, Object> param) {
        return productMallCategoryDao.getThirdCategoryByParamWeixin(param);
    }
}
