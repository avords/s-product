package com.handpay.ibenefit.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.IBSConstants;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.member.entity.CompanyFunction;
import com.handpay.ibenefit.member.entity.Supplier;
import com.handpay.ibenefit.member.entity.SupplierInnerCompany;
import com.handpay.ibenefit.member.service.ICompanyFunctionManager;
import com.handpay.ibenefit.member.service.ICompanyManager;
import com.handpay.ibenefit.member.service.ICompanyPublishedManager;
import com.handpay.ibenefit.member.service.ISupplierInnerCompanyManager;
import com.handpay.ibenefit.member.service.ISupplierManager;
import com.handpay.ibenefit.product.dao.CompanyGoodsDao;
import com.handpay.ibenefit.product.dao.SkuPublishViewDao;
import com.handpay.ibenefit.product.entity.CompanyGoods;
import com.handpay.ibenefit.product.entity.ProductPublish;
import com.handpay.ibenefit.product.entity.ProductShield;
import com.handpay.ibenefit.product.entity.SkuPublishView;
import com.handpay.ibenefit.product.service.ICompanyGoodsManager;
import com.handpay.ibenefit.product.service.IProductCompanyExclusiveManager;
import com.handpay.ibenefit.product.service.IProductPublishManager;
import com.handpay.ibenefit.product.service.ISkuPublishViewManager;
import com.handpay.ibenefit.welfare.entity.WelfarePackage;
import com.handpay.ibenefit.welfare.service.IWelfarePackageManager;

@Service(version = "1.0",timeout=60000)
public class CompanyGoodsManager extends BaseService<CompanyGoods> implements ICompanyGoodsManager {
	
	private static final Logger LOGGER = Logger.getLogger(CompanyGoodsManager.class);

	@Autowired
	private CompanyGoodsDao companyGoodsDao;
	
	@Autowired
	private SkuPublishViewDao skuPublishViewDao;

	@Reference(version = "1.0")
	private ISupplierManager supplierManager;

	@Autowired
	private ProductShieldManager productShieldManager;

	@Reference(version = "1.0")
	private ICompanyManager companyManager;

	@Autowired
	private ISkuPublishViewManager skuPublishViewManager;
	
	@Autowired
	private IProductCompanyExclusiveManager productCompanyExclusiveManager;
	
	@Autowired
	private IProductPublishManager productPublishManager;

	@Reference(version = "1.0")
	private IWelfarePackageManager welfarePackageManager;
	
	@Reference(version = "1.0")
	private ICompanyPublishedManager companyPublishedManager;
	
	@Reference(version = "1.0")
	private ICompanyFunctionManager companyFunctionManager;
	
	@Reference(version = "1.0")
	private ISupplierInnerCompanyManager supplierInnerCompanyManager;
	
	@Override
	public BaseDao<CompanyGoods> getDao() {
		return companyGoodsDao;
	}

	@Override
	@Transactional
	public void updateByCompanyId(Long companyId) {
		if(companyId!=null){
			List<SkuPublishView> skuPublishViews = skuPublishViewManager.getAll();
			WelfarePackage welfarePackage = new WelfarePackage();
			welfarePackage.setStatus(IBSConstants.SHELVES_ING);
			List<WelfarePackage> welfarePackages = welfarePackageManager.getBySample(welfarePackage);
			List<Long> allExclusive = productCompanyExclusiveManager.getAllExclusiveProduct();
			runOnCompany(companyId,skuPublishViews,welfarePackages,allExclusive);
			List<Long> companyIds = new ArrayList<Long>(1);
			companyIds.add(companyId);
			updateCompanySkuPrice(companyIds);
			updateCompanyPackagePrice(companyIds);
		}
	}
	
	/**
	 * 计算企业的权限清单
	 * @param companyId 企业ID
	 * @param skuPublishViews 所有上架中的商品SKU
	 * @param welfarePackages 所有上架中的套餐
	 * @param allExclusive 所有专属的商品
	 */
	private void runOnCompany(Long companyId, List<SkuPublishView> skuPublishViews,
			List<WelfarePackage> welfarePackages, List<Long> allExclusive) {
		deleteCompanyGoodsByCompanyId(companyId);
		ProductShield sample = new ProductShield();
		sample.setCompanyId(companyId);
		//所选企业是仅供内卖
		if(isExclusiveCompany(companyId)){
			skuPublishViews = getInSellingSku(companyId);
		}else{
			//企业专属商品
			List<SkuPublishView> exclusiveSkuPublishViews = getExclusiveSkuPublishView(companyId, skuPublishViews);
			// 企业的所有屏蔽信息
			List<ProductShield> list = productShieldManager.getBySample(sample);
			
			//商品屏蔽
			for (ProductShield productShield : list) {
				doProductShield(skuPublishViews, welfarePackages, productShield);
			}
			//供应商仅供内卖屏蔽
			List<SkuPublishView> inSellings = skuPublishViewDao.getInSellingSku();
			for(SkuPublishView skuPublishView : inSellings){
				shieldProduct(skuPublishViews, skuPublishView.getProductId());
			}
			//专属商品屏蔽
			for(long productId : allExclusive){
				shieldProduct(skuPublishViews, productId);
			}
			//添加内卖供应商的商品
			skuPublishViews.addAll(getInSellingSku(companyId));
			//添加专属商品
			skuPublishViews.addAll(exclusiveSkuPublishViews);
		}
		//去掉重复的数据
		Set<SkuPublishView> finalPublishViews = new HashSet<SkuPublishView>(skuPublishViews);
		//插入产品数据
		for (SkuPublishView skuPublishView : finalPublishViews) {
			saveSkuGoods(companyId, skuPublishView);
		}
		
		//去掉重复的数据
		Set<WelfarePackage> finalWelfarePackages = new HashSet<WelfarePackage>(welfarePackages);
		//插入套餐数据
		for (WelfarePackage tWelfarePackage : finalWelfarePackages) {
			saveWelfateGoods(companyId, tWelfarePackage);
		}
		LOGGER.info("完成企业[" + companyId + "]的商品权限数据更新。");
	}

	private void doProductShield(List<SkuPublishView> skuPublishViews, List<WelfarePackage> welfarePackages, ProductShield productShield) {
		if (productShield.getType() == IBSConstants.SUPPLIER) {
			ProductPublish product = new ProductPublish();
			product.setSupplierId(productShield.getItemId());
			List<ProductPublish> products = productPublishManager.getBySample(product);
			for(ProductPublish temp : products){
				shieldProduct(skuPublishViews, temp.getObjectId());
			}
		}else if(productShield.getType() == IBSConstants.BRAND){
			ProductPublish product = new ProductPublish();
			product.setBrandId(productShield.getItemId());
			List<ProductPublish> products = productPublishManager.getBySample(product);
			for(ProductPublish temp : products){
				shieldProduct(skuPublishViews, temp.getObjectId());
			}
		}else if(productShield.getType() == IBSConstants.PRODUCT_CATEGORY){
			ProductPublish product = new ProductPublish();
			product.setCategoryId(productShield.getItemId());
			List<ProductPublish> products = productPublishManager.getBySample(product);
			for(ProductPublish temp : products){
				shieldProduct(skuPublishViews, temp.getObjectId());
			}
		}else if(productShield.getType() == IBSConstants.PRODUCT){
			shieldProduct(skuPublishViews, productShield.getItemId());
		}else if(productShield.getType() == IBSConstants.WELFARE_PACKAGE){
			shieldPackage(welfarePackages, productShield.getItemId());
		}
	}

	/**
	 * 是否仅供企业内卖
	 * @param companyId
	 */
	private boolean isExclusiveCompany(Long companyId) {
		CompanyFunction companyFunction = new CompanyFunction();
		companyFunction.setCompanyId(companyId);
		companyFunction.setFunctionId(6);
		companyFunction.setIsPublished(IBSConstants.STATUS_YES);
		List<CompanyFunction> list = companyFunctionManager.getBySample(companyFunction);
		if(list.size()==1){
			return true;
		}
		return false;
	}
	
	//查找内卖供应商的产品
	private List<SkuPublishView> getInSellingSku(Long companyId){
		//查找内卖的供应商
		List<SupplierInnerCompany> list = getInsellingSuppliersByCompanyId(companyId);
		List<SkuPublishView> result = new ArrayList<SkuPublishView>();
		for(SupplierInnerCompany supplierInnerCompany : list){
			result.addAll(skuPublishViewDao.getPublishedSkuBySupplierId(supplierInnerCompany.getSupplierId()));
		}
		return result;
	}

	private List<SupplierInnerCompany> getInsellingSuppliersByCompanyId(Long companyId) {
		SupplierInnerCompany sample = new SupplierInnerCompany();
		sample.setCompanyId(companyId);
		return supplierInnerCompanyManager.getBySample(sample);
	}
	
	
	private List<SupplierInnerCompany> getInsellingCompanysBySupplierId(Long supplierId) {
		SupplierInnerCompany sample = new SupplierInnerCompany();
		sample.setSupplierId(supplierId);
		return supplierInnerCompanyManager.getBySample(sample);
	}
	 

	private List<SkuPublishView> getExclusiveSkuPublishView(Long companyId, List<SkuPublishView> skuPublishViews) {
		List<Long> exclusiveProductIds = productCompanyExclusiveManager.getProductIdsByCompanyId(companyId);
		List<SkuPublishView> exclusiveSkuPublishView = new ArrayList<SkuPublishView>();
		for(SkuPublishView temp : skuPublishViews){
			if(exclusiveProductIds.contains(temp.getProductId())){
				exclusiveSkuPublishView.add(temp);
			}
		}
		return exclusiveSkuPublishView;
	}

	private void saveSkuGoods(Long companyId, SkuPublishView skuPublishView) {
		CompanyGoods  temp= new CompanyGoods();
		temp.setCompanyId(companyId);
		temp.setGoodsId(skuPublishView.getSkuId());
		temp.setPrice(skuPublishView.getPrice());
		temp.setType(IBSConstants.ORDER_PRODUCT_TYPE_SKU);
		try{
			save(temp);
		}catch (Exception e){
			LOGGER.error(temp.toString(),e);
		}
	}

	private void saveWelfateGoods(Long companyId, WelfarePackage welfarePackage) {
		CompanyGoods  temp= new CompanyGoods();
		temp.setCompanyId(companyId);
		temp.setGoodsId(welfarePackage.getObjectId());
		temp.setPrice(welfarePackage.getPackagePrice());
		temp.setType(IBSConstants.ORDER_PRODUCT_TYPE_WELFARE);
		try{
			save(temp);
		}catch (Exception e){
			LOGGER.error(temp.toString(),e);
		}
	}
	
	private void shieldProduct(List<SkuPublishView> skuPublishViews, long productId){
		for(int i=skuPublishViews.size()-1;i>=0;i--){
			if(skuPublishViews.get(i).getProductId() == productId){
				skuPublishViews.remove(i);
			}
		}
	}
	
	private void shieldPackage(List<WelfarePackage> welfarePackages, long packageId){
		for(int i=welfarePackages.size()-1;i>=0;i--){
			if(welfarePackages.get(i).getObjectId() == packageId){
				welfarePackages.remove(i);
			}
		}
	}

	/**
	 * 商品发布 按如下规则进行处理：
	 *  1.是否仅供内卖商品 
	 *  2.查找所属供应商被屏蔽的企业 
	 *  3.查找所属品牌被屏蔽的企业 
	 *  4.查找所属分类被屏蔽的企业
	 *  5.查找所属商品被屏蔽的企业
	 * 
	 * @param product
	 */
	@Override
	@Transactional
	public void updateByProductId(Long productId) {
		ProductPublish product = productPublishManager.getByObjectId(productId);
		if(product!=null){
			doUpdateByProductId(product);
			LOGGER.info("更新商品[" + product.getName() + "," + product.getObjectId() +"]权限数据完成");
		}
	}

	private void doUpdateByProductId( ProductPublish product) {
		Long productId = product.getObjectId();
		companyGoodsDao.deleteCompanyGoodsByProductId(productId);
		Supplier supplier = supplierManager.getByObjectId(product.getSupplierId());
		// 供应商是仅供内卖,不处理面向企业价格
		if (supplier.getIsInSelling() != null && supplier.getIsInSelling() == IBSConstants.STATUS_YES) {
			List<SupplierInnerCompany> list = getInsellingCompanysBySupplierId(supplier.getObjectId());
			if (list != null && list.size()>0 ) {
				SkuPublishView publishView = new SkuPublishView();
				publishView.setProductId(productId);
				List<SkuPublishView> skuPublishViews = skuPublishViewManager.getBySample(publishView);
				for(SkuPublishView skuPublishView : skuPublishViews){
					for(SupplierInnerCompany innerCompany : list){
						saveSkuGoods(innerCompany.getCompanyId(), skuPublishView);
					}
				}
			}
		} else {
			List<Long> exclusiveCompanyIds = productCompanyExclusiveManager.getExclusiveCompanyId(productId);
			//不是专属商品，按屏蔽规则进行
			if(exclusiveCompanyIds==null || exclusiveCompanyIds.size()==0){
				//所有企业
				List<Long> allCompany = companyPublishedManager.getAllCompanyId();
				//去掉全部仅供内卖企业
				List<Long> innerCompany = companyPublishedManager.getAllInnerCompanyId();
				allCompany.removeAll(innerCompany);
				//屏蔽
				doShield(product, supplier, allCompany);
				//添加供应商自己的内卖企业
				List<SupplierInnerCompany> innerCompanies = getInsellingCompanysBySupplierId(product.getSupplierId());
				for(SupplierInnerCompany company : innerCompanies){
					allCompany.add(company.getCompanyId());
				}
				exclusiveCompanyIds = allCompany;
			}
			SkuPublishView publishView = new SkuPublishView();
			publishView.setProductId(productId);
			List<SkuPublishView> skuPublishViews = skuPublishViewManager.getBySample(publishView);
			//去除重复
			Set<Long> finalCompanyIds = new HashSet<Long>(exclusiveCompanyIds);
			// 插入
			for (Long companyId : finalCompanyIds) {
				for(SkuPublishView skuPublishView : skuPublishViews){
					saveSkuGoods(companyId, skuPublishView);
				}
			}
			//更新所有
			companyGoodsDao.updateAllCompanySkuPrice();
		}
	}

	private void doShield(ProductPublish product, Supplier supplier, List<Long> allCompany) {
		// 供应商屏蔽
		ProductShield sample = new ProductShield();
		sample.setItemId(supplier.getObjectId());
		sample.setType(IBSConstants.SUPPLIER);
		List<ProductShield> list = productShieldManager.getBySample(sample);
		for (ProductShield shield : list) {
			allCompany.remove(shield.getCompanyId());
		}
		// 品牌屏蔽
		sample = new ProductShield();
		sample.setItemId(product.getBrandId());
		sample.setType(IBSConstants.BRAND);
		list = productShieldManager.getBySample(sample);
		for (ProductShield shield : list) {
			allCompany.remove(shield.getCompanyId());
		}
		// 分类屏蔽
		sample = new ProductShield();
		sample.setItemId(product.getCategoryId());
		sample.setType(IBSConstants.PRODUCT_CATEGORY);
		list = productShieldManager.getBySample(sample);
		for (ProductShield shield : list) {
			allCompany.remove(shield.getCompanyId());
		}
		// 商品屏蔽
		sample = new ProductShield();
		sample.setItemId(product.getObjectId());
		sample.setType(IBSConstants.PRODUCT);
		list = productShieldManager.getBySample(sample);
		for (ProductShield shield : list) {
			allCompany.remove(shield.getCompanyId());
		}
	}

	@Override
	@Transactional
	public void upPackage(WelfarePackage welfarePackage) {
		if(welfarePackage!=null){
			deleteCompanyGoodsByPackageId(welfarePackage.getObjectId());
			//所有企业
			List<Long> allCompany = companyPublishedManager.getAllCompanyId();
			//查找屏蔽此套餐的所有企业
			ProductShield sample = new ProductShield();
			sample.setItemId(welfarePackage.getObjectId());
			sample.setType(IBSConstants.WELFARE_PACKAGE);
			List<ProductShield> list = productShieldManager.getBySample(sample);
			for (ProductShield shield : list) {
				allCompany.remove(shield.getCompanyId());
			}
			// 插入
			for (Long companyId : allCompany) {
				saveWelfateGoods(companyId, welfarePackage);
			}
			updateCompanyPackagePrice(allCompany);
		}
	}
	
	@Override
	@Transactional
	public void downPackage(Long packageId) {
		deleteCompanyGoodsByPackageId(packageId);
	}

	private void deleteCompanyGoodsByPackageId(Long packageId){
		CompanyGoods companyGoods = new CompanyGoods();
		companyGoods.setGoodsId(packageId);
		companyGoods.setType(IBSConstants.ORDER_PRODUCT_TYPE_WELFARE);
		deleteBySample(companyGoods);
	}
	
	private void deleteCompanyGoodsByCompanyId(Long companyId){
		CompanyGoods companyGoods = new CompanyGoods();
		companyGoods.setCompanyId(companyId);
		deleteBySample(companyGoods);
	}
	
	@Override
	public void updateAll() {
		long begin = System.currentTimeMillis();
		LOGGER.info("开始全量更新商品权限数据");
		List<Long> companies = companyPublishedManager.getAllCompanyId();
		List<SkuPublishView> skuPublishViews = skuPublishViewManager.getAll();
		WelfarePackage welfarePackage = new WelfarePackage();
		welfarePackage.setStatus(IBSConstants.SHELVES_ING);
		List<WelfarePackage> welfarePackages = welfarePackageManager.getBySample(welfarePackage);
		for(Long companyId : companies){
			List<SkuPublishView> tempSkuPublishViews = new ArrayList<SkuPublishView>(skuPublishViews);
			List<WelfarePackage> tempWelfarePackages = new ArrayList<WelfarePackage>(welfarePackages);
			List<Long> allExclusive = productCompanyExclusiveManager.getAllExclusiveProduct();
			runOnCompany(companyId , tempSkuPublishViews, tempWelfarePackages,allExclusive);
		}
		long end = System.currentTimeMillis();
		LOGGER.info("完成全量更新商品权限数据，用时" + (end - begin)/1000 + "秒");
		//更新全部商品的面向企业价格
		companyGoodsDao.updateAllCompanySkuPrice();
		//更新全部套餐的面向企业价格
		companyGoodsDao.updateCompanyPackagePrice(new HashMap<String, Object>());
		LOGGER.info("完成全量更新商品权限数据的企业价格，用时" + (System.currentTimeMillis()- end)/1000 + "秒");
	}

	@Override
	@Transactional
	public void updateCompanySkuPrice(List<Long> companyIds) {
		if(companyIds!=null && companyIds.size()>0){
			StringBuilder ss = new StringBuilder();
			for(Long companyId : companyIds){
				ss.append(companyId).append(",");
			}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("companyIds", ss.subSequence(0, ss.length()-1));
			companyGoodsDao.updateCompanySkuPrice(param);
		}
	}

	@Override
	public CompanyGoods getSkuPriceByCompanyIdAndSkuId(Long companyId, Long skuId) {
		CompanyGoods companyGoods = new CompanyGoods();
		companyGoods.setCompanyId(companyId);
		companyGoods.setGoodsId(skuId);
		companyGoods.setType(IBSConstants.ORDER_PRODUCT_TYPE_WELFARE);
		List<CompanyGoods> list=getBySample(companyGoods);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public void updateCompanyPackagePrice(List<Long> companyIds) {
		if(companyIds!=null && companyIds.size()>0){
			StringBuilder ss = new StringBuilder();
			for(Long companyId : companyIds){
				ss.append(companyId).append(",");
			}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("companyIds", ss.subSequence(0, ss.length()-1));
			companyGoodsDao.updateCompanyPackagePrice(param);
		}
	}

	@Override
	@Transactional
	public void updateBySupplierId(Long supplierId) {
		ProductPublish sample = new ProductPublish();
		sample.setSupplierId(supplierId);
		List<ProductPublish> productPublishs = productPublishManager.getBySample(sample);
		for(ProductPublish product : productPublishs){
			doUpdateByProductId(product);
		}
		LOGGER.info("更新供疫商[" + supplierId +"]权限数据完成");
	}
}
