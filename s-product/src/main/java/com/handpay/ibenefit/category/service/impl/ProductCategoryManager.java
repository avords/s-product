package com.handpay.ibenefit.category.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.IBSConstants;
import com.handpay.ibenefit.category.dao.ProductCategoryDao;
import com.handpay.ibenefit.category.dao.ProductMallCategoryDao;
import com.handpay.ibenefit.category.entity.ProductCategory;
import com.handpay.ibenefit.category.service.IProductCategoryManager;
import com.handpay.ibenefit.category.service.IProductMallCategoryManager;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.framework.service.ISequenceManager;
import com.handpay.ibenefit.framework.util.PageSearch;
import com.handpay.ibenefit.product.entity.Product;
import com.handpay.ibenefit.product.service.IProductManager;

@Service(version = "1.0")
public class ProductCategoryManager extends BaseService<ProductCategory> implements IProductCategoryManager {

	private static final Logger LOGGER = Logger.getLogger(ProductCategoryManager.class);

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductMallCategoryDao productMallCategoryDao;

	@Autowired
	private IProductManager productManager;

	@Autowired
	private IProductMallCategoryManager productMallCategoryManager;

	@Reference(version = "1.0")
	private ISequenceManager sequenceManager;

	/**
	 * 保存时三级分类ID的序列化
	 */
	@Override
	@Transactional
    public ProductCategory save(ProductCategory entity) {
		if (entity.getLayer() == 1) {
			if (StringUtils.isBlank(entity.getFirstId())) {
				String firstId = sequenceManager.getNextId("SEQ_FIRST_ID").toString();
				entity.setFirstId(firstId);
			}
		}
		else if (entity.getLayer() == 2) {
			if (StringUtils.isBlank(entity.getSecondId())) {
				String secondId = sequenceManager.getNextId("SEQ_SECOND_ID").toString();
				entity.setSecondId(secondId);
			}
		}
		else if (entity.getLayer() == 3) {
			if (StringUtils.isBlank(entity.getThirdId())) {
				String thirdId = sequenceManager.getNextId("SEQ_THIRD_ID").toString();
				entity.setThirdId(thirdId);
			}
		} else {

		}
		return super.save(entity);
	}



	@Override
	public BaseDao<ProductCategory> getDao() {
		return productCategoryDao;
	}

	/**
	 * 一级分类页面统计二级分类数量
	 */
	@Override
	@Transactional
	public int updateSecondCategoryCount() {
		return productCategoryDao.updateSecondCategoryCount();
	}

	/**
	 * 二级分类页面统计三级分类数量
	 */
	@Override
	@Transactional
	public int updateThirdCategoryCount() {
		return productCategoryDao.updateThirdCategoryCount();
	}

	/**
	 * 根据一级分类得到二级分类
	 * @param	一级分类Map
	 * @return	二级分类List
	 */
	@Override
	public List<ProductCategory> getSecondCategoryByParam(
			Map<String, Object> param) {
		return productCategoryDao.getSecondCategoryByParam(param);
	}

	/**
	 * 根据二级分类得到三级分类
	 * @param	二级分类Map
	 * @return	三级分类List
	 */
	@Override
	public List<ProductCategory> getThirdCategoryByParam(
			Map<String, Object> param) {
		return productCategoryDao.getThirdCategoryByParam(param);
	}

	/**
	 * 根据二级分类ID得到二级分类
	 * @param	二级分类ID
	 * @return	二级分类
	 */
	@Override
	public ProductCategory getProductCategoryBySecondId(String secondId) {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setSecondId(secondId);
		productCategory.setLayer(2);
		List<ProductCategory> list = getBySample(productCategory);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据三级分类ID得到三级分类
	 * @param	三级分类ID
	 * @return	三级分类
	 */
	@Override
	public ProductCategory getProductCategoryByThirdId(String thirdId) {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setThirdId(thirdId);
		productCategory.setLayer(3);
		List<ProductCategory> list = getBySample(productCategory);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查找所有的三级分类
	 */
    @Override
    public PageSearch findAllThirdCategory(PageSearch pageSearch) {
        List<ProductCategory> list = productCategoryDao.getAllThirdCategory(pageSearch);
        pageSearch.setList(list);
        return pageSearch;
    }

	/**
	 * 得到所有的一级分类
	 */
    @Override
    public List<ProductCategory> getAllFirstCategory(){
        return productCategoryDao.getAllFirstCategory();
    }

	/**
	 * 根据三级分类ID得到三级分类
	 */
    @Override
    public ProductCategory getThirdCategoryByThirdId(String thirdId) {
        return productCategoryDao.getThirdCategoryByThirdId(thirdId);
    }

	/**
	 * 根据福利商城三级分类ID得到商品运营三级分类
	 */
	@Override
	public List<ProductCategory> getProductCategoriesByMallCategoryId(Long mallCategoryId) {
		return productCategoryDao.getProductCategoriesByMallCategoryId(mallCategoryId);
	}

	/**
	 * 构造商品运营分类Left tree的结构
	 * @param	商品运营分类List
	 * @return	Left tree的HTML
	 */
	@Override
	public ProductCategory getProductCategoryByFirstId(String firstId) {
		ProductCategory sample = new ProductCategory();
		sample.setFirstId(firstId);
		sample.setLayer(1);
		List<ProductCategory> list = getBySample(sample);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public void updateStatus(Long objectId, int status) {
		ProductCategory category = new ProductCategory();
		category.setObjectId(objectId);
		category.setStatus(status);
		productCategoryDao.updateStatus(category);
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
				ProductCategory category = new ProductCategory();
				category.setSortNo(sortNo);
				category.setObjectId(objectId);
				productCategoryDao.updateSortNo(category);
			}catch (Exception e){
				LOGGER.error("updateSortNoByObjectId",e);
			}
		}
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
		ProductCategory category = getByObjectId(objectId);
		if(category.getLayer()==1){
			ProductCategory sample = new ProductCategory();
			sample.setFirstId(category.getFirstId());
			sample.setLayer(2);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else if(category.getLayer()==2){
			ProductCategory sample = new ProductCategory();
			sample.setSecondId(category.getSecondId());
			sample.setLayer(3);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else{
			Product sample = new Product();
			sample.setCategoryId(objectId);
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
	 * 3.关联的福利商城分类有效则三级分类不能无效
	 * 4.该分类下面有商品（无论商品是什么状态）都不能置为无效
	 * @param objectId
	 * @return
	 */
	@Override
    public boolean canInvalid(Long objectId) {
		if(objectId==null){
			return true;
		}
		ProductCategory category = getByObjectId(objectId);
		if(category.getLayer()==1){
			ProductCategory sample = new ProductCategory();
			sample.setFirstId(category.getFirstId());
			sample.setStatus(IBSConstants.EFFECTIVE);
			sample.setLayer(2);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else if(category.getLayer()==2){
			ProductCategory sample = new ProductCategory();
			sample.setSecondId(category.getSecondId());
			sample.setStatus(IBSConstants.EFFECTIVE);
			sample.setLayer(3);
			long count = getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}else{
			//是否有关联的福利商城的分类
			long mallCategoryCount = productMallCategoryDao.getRelatedProductMallCategoryByCategoryId(objectId);
			if(mallCategoryCount!=0){
				return false;
			}
			Product sample = new Product();
			sample.setCategoryId(objectId);
			sample.setDeleted(IBSConstants.STATUS_NO);
			long count = productManager.getObjectCount(sample);
			if(count!=0){
				return false;
			}
		}
		return true;
	}



    @Override
    public ProductCategory getThirdCategoryByObjectId(Long objectId) {
        return productCategoryDao.getThirdCategoryByObjectId(objectId);
    }



    @Override
    public List<ProductCategory> getThirdCategory(List<Long> categoryIds) {
        if(categoryIds==null||categoryIds.size()==0){
            return new ArrayList<ProductCategory>();
        }
        String ids = "";
        for(Long id:categoryIds){
            ids +=id+",";
        }
        if(categoryIds.size()>0){
            ids = ids.substring(0, ids.length()-1);
        }
        return productCategoryDao.getThirdCategory(ids);
    }
}
