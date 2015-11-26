package com.handpay.ibenefit.product.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.handpay.ibenefit.IBSConstants;
import com.handpay.ibenefit.framework.dao.BaseDao;
import com.handpay.ibenefit.framework.service.BaseService;
import com.handpay.ibenefit.product.dao.ElectronicCardDao;
import com.handpay.ibenefit.product.entity.ElectronicCard;
import com.handpay.ibenefit.product.entity.Sku;
import com.handpay.ibenefit.product.service.IElectronicCardManager;
import com.handpay.ibenefit.product.service.ISkuManager;
import com.handpay.ibenefit.product.service.ISkuPublishManager;

@Service(version = "1.0")
public class ElectronicCardManager extends BaseService<ElectronicCard> implements IElectronicCardManager {

    @Autowired
    private ElectronicCardDao electronicCardDao;
    @Autowired
    private ISkuManager skuManager;
    @Autowired
    private ISkuPublishManager skuPublishManager;

    @Override
    public BaseDao<ElectronicCard> getDao() {
        return electronicCardDao;
    }

    @Transactional
    public int saveOrUpdateEntity(ElectronicCard entity) {
        //判断状态为4，如果不为4则更新
        int flag = 0;
        ElectronicCard electronicCard = new ElectronicCard();
        electronicCard.setSkuId(entity.getSkuId());
        electronicCard.setCardNo(entity.getCardNo());
        entity.setImportDate(new Date());
        List<ElectronicCard> electronicCards = getBySample(electronicCard);
        if(electronicCards.size()>0){
            ElectronicCard t = electronicCards.get(0);
            if(t.getStatus()!=IBSConstants.CARD_USED){
                //更新
                t.setCardPassword(entity.getCardPassword());
                t.setStatus(entity.getStatus());
                t.setImportDate(new Date());
                entity = super.save(t);
                //加库存
                if(entity.getStatus()!=null&&entity.getStatus().equals(IBSConstants.CARD_ACTIVATION)&&!t.getStatus().equals(IBSConstants.CARD_ACTIVATION)){
                    flag = 1;
                }
                //减库存
                if(entity.getStatus()!=null&&!entity.getStatus().equals(IBSConstants.CARD_ACTIVATION)&&t.getStatus().equals(IBSConstants.CARD_ACTIVATION)){
                    flag = -1;
                }
            }
        }else{
            entity = super.save(entity);
            if(entity.getStatus()!=null&&entity.getStatus().equals(IBSConstants.CARD_ACTIVATION)){
                flag = 1;
            }
        }
        return flag;
    }
    @Override
    public String importStock(byte[] fileData, Long skuId, Integer source) {
        StringBuilder message = new StringBuilder();
        int sucessed = 0;
        int empty = 0;
        int addStock = 0;
        HSSFWorkbook workbook;
        try {
            workbook = new HSSFWorkbook(new ByteArrayInputStream(fileData));
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int rowNum = 4; rowNum <= sheet.getLastRowNum(); rowNum++) {
                 HSSFRow row = sheet.getRow(rowNum);
                 if (row == null) {
                     empty++;
                     continue;
                 }else {
                     boolean flag = true;
                     for(int i=0;i<3;i++){
                         HSSFCell cell = row.getCell(i);
                         flag = flag&&((cell==null)||(cell!=null&&"".equals(getStringCellValue(cell))));
                     }
                     if(flag){
                         empty++;
                         continue;
                     }
                 }
                 ElectronicCard electronicCard = new ElectronicCard();
                 electronicCard.setSkuId(skuId);
                 electronicCard.setCardNo(getStringCellValue(row.getCell(0)));
                 electronicCard.setCardPassword(getStringCellValue(row.getCell(1)));
                 electronicCard.setStatus(Integer.parseInt(getStringCellValue(row.getCell(2))));
                 int flag = this.saveOrUpdateEntity(electronicCard);
                 addStock = addStock+flag;
                 sucessed++;
                }
            //获取卡券库存，通过skuId
            //Long stock = getElectronicCardStock(skuId);
            //更新库存和库存类型,如果库存类型不为2，则改变库存类型，并且设置库存为0
            Sku sku = skuManager.getByObjectId(skuId);
            if(sku.getStockSource()==null||sku.getStockSource().equals(1)){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("stockSource", source);
                map.put("stock", getElectronicCardStock(skuId));
                map.put("objectId", skuId);
                skuManager.updateSku(map);
            }else if(addStock!=0){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("addStock", addStock);
                map.put("objectId", skuId);
                //更改临时表库存和更改正式表库存
                skuManager.updateSku(map);
            }
            message.append("成功导入").append(sucessed).append("条记录,");
            message.append("共").append(sheet.getLastRowNum()-empty-sucessed-3).append("行数据异常，请检查!");
        } catch (IOException e) {
        }
        return message.toString();
    }

    private String getStringCellValue(HSSFCell cell) {
        String result = new String();
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date date = cell.getDateCellValue();
                result = sdf.format(date);
            } else if (cell.getCellStyle().getDataFormat() == 58) {
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                double value = cell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil
                        .getJavaDate(value);
                result = sdf.format(date);
            } else {
                double value = cell.getNumericCellValue();
                CellStyle style = cell.getCellStyle();
                DecimalFormat format = new DecimalFormat();
                String temp = style.getDataFormatString();
                // 单元格设置成常规
                if (temp.equals("General")) {
                    format.applyPattern("#");
                }
                result = format.format(value);
            }
            break;
        case HSSFCell.CELL_TYPE_STRING:// String类型
            result = cell.getRichStringCellValue().toString();
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            result = "";
            break;
        default:
            result = "";
            break;
        }
        return result;
    }

    private Long getElectronicCardStock(Long skuId){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("skuId", skuId);
        map.put("status", IBSConstants.CARD_ACTIVATION);
        return electronicCardDao.getCountByParam(map);
    }



	@Override
	public List<ElectronicCard> getCardByCount(Long productCount,Long productId) {
		 Map<String,Object> map = new HashMap<String,Object>();
	        map.put("count", productCount);
	        map.put("status", IBSConstants.CARD_ACTIVATION);
	        map.put("productId", productId);

		return electronicCardDao.getCardByCount(map);
	}





	/**
	 * 子订单关联卡密信息
	 * @author zhliu
	 * @date 2015年8月5日
	 * @parm
	 * @param map
	 * @return
	 */
	@Override
    public List<ElectronicCard> selectSubOrderCardInfo(Map<String, Object> map){
		return electronicCardDao.selectSubOrderCardInfo(map);
	}

}
