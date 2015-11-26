package com.handpay.ibenefit.category.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.handpay.ibenefit.category.entity.ProductMallCategory;
import com.handpay.ibenefit.framework.dao.BaseDao;

@Repository
public interface ProductMallCategoryDao extends BaseDao<ProductMallCategory> {

	// 统计二级分类数量
	public int updateSecondCategoryCount();

	// 统计三级分类数量
	public int updateThirdCategoryCount();

	/**
	 * 根据三级分类ID得到三级分类
	 * @param	三级分类ID
	 * @return	三级分类List
	 */
	List<ProductMallCategory> getAllThirdCategoryByProductThirdId(Map<String,Object> map);

    /**
     * 更新状态
     * @param category
     */

    public void updateStatus(ProductMallCategory category);

    /**
     * 更新排列顺序
     * @param category
     */
    public void updateSortNo(ProductMallCategory category);

    public List<ProductMallCategory> getAllFirstCategory(@Param("platform")Integer platform,@Param("count") Integer count);

    public List<ProductMallCategory> getSecondCategoryByParam(Map<String, Object> param);

    public List<ProductMallCategory> getThirdCategoryByParam(Map<String, Object> param);
    
    public List<ProductMallCategory> getThirdCategoryByParamWeixin(Map<String, Object> param);
    /**
     * 根据运营分类ID查询关联的有效的福利商城分类
     * @param categoryId
     * @return
     */
    public Long getRelatedProductMallCategoryByCategoryId(Long categoryId);
}
