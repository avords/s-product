package com.handpay.ibenefit.category.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.category.entity.ProductCategory;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.util.PageSearch;

@Repository
public interface ProductCategoryDao extends BaseDao<ProductCategory> {

	// 统计二级分类数量
	public int updateSecondCategoryCount();

	// 统计三级分类数量
	public int updateThirdCategoryCount();

	/**
	 * 根据一级分类得到二级分类
	 * @param	一级分类Map
	 * @return	二级分类List
	 */
	public List<ProductCategory> getSecondCategoryByParam(Map<String, Object> param);

	/**
	 * 根据二级分类得到三级分类
	 * @param	二级分类Map
	 * @return	三级分类List
	 */
	public List<ProductCategory> getThirdCategoryByParam(Map<String, Object> param);

	/**
	 * 根据二级分类ID得到二级分类
	 * @param	二级分类ID
	 * @return	二级分类
	 */
	public ProductCategory getProductCategoryBySecondId(String secondId);

	/**
	 * 根据三级分类ID得到三级分类
	 * @param	三级分类ID
	 * @return	三级分类
	 */
	public ProductCategory getProductCategoryByThirdId(String thirdId);

	// 取得所有的三级分类
	public List<ProductCategory> getAllThirdCategory(PageSearch pageSearch);

	// 取得所有的一级分类
    public List<ProductCategory> getAllFirstCategory();

	/**
	 * 根据三级分类ID得到三级分类
	 * @param	三级分类ID
	 * @return	三级分类
	 */
    public ProductCategory getThirdCategoryByThirdId(String thirdId);

	/**
	 * 根据福利商城三级分类ID得到商品运营三级分类
	 * @param	福利商城三级分类ID
	 * @return	商品运营三级分类List
	 */
    public List<ProductCategory> getProductCategoriesByMallCategoryId(Long mallCategoryId);

    /**
     * 更新状态
     * @param category
     */

    public void updateStatus(ProductCategory category);

    /**
     * 更新排列顺序
     * @param category
     */
    public void updateSortNo(ProductCategory category);

    public ProductCategory getThirdCategoryByObjectId(Long objectId);

    public List<ProductCategory> getThirdCategory(@Param("categoryIds") String categoryIds);

}
