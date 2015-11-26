package com.handpay.ibenefit.product.dao;

import java.util.List;
import java.util.Map;

import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.product.entity.ElectronicCard;

public interface ElectronicCardDao extends BaseDao<ElectronicCard>{

    Long getCountByParam(Map<String,Object> map);


	
	List<ElectronicCard> getCardByCount(Map<String, Object> map);
	
	/**
	 * 子订单关联卡密信息
	 * @author zhliu
	 * @date 2015年8月5日
	 * @parm
	 * @param map
	 * @return
	 */
	List<ElectronicCard> selectSubOrderCardInfo(Map<String, Object> map);
	
}
