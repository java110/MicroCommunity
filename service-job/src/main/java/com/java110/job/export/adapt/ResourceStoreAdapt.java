package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 物品信息导出
 *
 * @author fqz
 * @date 203-11-18 10:37
 */
@Service("resourceStoreManage")
public class ResourceStoreAdapt implements IExportDataAdapt {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("物品信息");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("物品名称(编号)");
        row.createCell(1).setCellValue("仓库名称");
        row.createCell(2).setCellValue("物品类型");
        row.createCell(3).setCellValue("物品规格");
        row.createCell(4).setCellValue("固定物品");
        row.createCell(5).setCellValue("参考价格");
        row.createCell(6).setCellValue("收费标准");
        row.createCell(7).setCellValue("物品库存");
        row.createCell(8).setCellValue("最小计量");
        row.createCell(9).setCellValue("最小计量总数");
        row.createCell(10).setCellValue("物品均价");
        row.createCell(11).setCellValue("物品总价");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getResourceStoreManage(sheet, reqJson);
        return workbook;
    }

    private void getResourceStoreManage(Sheet sheet, JSONObject reqJson) {
        ResourceStoreDto resourceStoreDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreDto.class);
        BasePrivilegeDto basePrivilegeDto1 = new BasePrivilegeDto();
        basePrivilegeDto1.setResource("/viewHiddenWarehouse");
        basePrivilegeDto1.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto1);
        if (privileges.size() == 0) {
            resourceStoreDto.setIsShow("true");
        }
        resourceStoreDto.setPage(1);
        resourceStoreDto.setRow(MAX_ROW);
        List<ResourceStoreDto> resourceStores = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        appendData(resourceStores, sheet);
    }

    private void appendData(List<ResourceStoreDto> resourceStores, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < resourceStores.size(); index++) {
            row = sheet.createRow(index + 1);
            ResourceStoreDto resourceStoreDto = resourceStores.get(index);
            row.createCell(0).setCellValue(resourceStoreDto.getResName() + "(" + resourceStoreDto.getResCode() + ")");
            row.createCell(1).setCellValue(resourceStoreDto.getShName());
            if (!StringUtil.isEmpty(resourceStoreDto.getRstName())) {
                row.createCell(2).setCellValue(resourceStoreDto.getParentRstName() + ">" + resourceStoreDto.getRstName());
            } else {
                row.createCell(2).setCellValue(resourceStoreDto.getParentRstName());
            }
            if (!StringUtil.isEmpty(resourceStoreDto.getRssName())) {
                row.createCell(3).setCellValue(resourceStoreDto.getRssName());
            } else {
                row.createCell(3).setCellValue("--");
            }
            row.createCell(4).setCellValue(resourceStoreDto.getIsFixedName());
            row.createCell(5).setCellValue("￥" + resourceStoreDto.getPrice());
            if (resourceStoreDto.getOutHighPrice().equals(resourceStoreDto.getOutLowPrice())) {
                row.createCell(6).setCellValue("￥" + resourceStoreDto.getOutLowPrice());
            } else {
                row.createCell(6).setCellValue("￥" + resourceStoreDto.getOutLowPrice() + "-￥" + resourceStoreDto.getOutHighPrice());
            }
            row.createCell(7).setCellValue(resourceStoreDto.getStock() + resourceStoreDto.getUnitCodeName());
            row.createCell(8).setCellValue("1" + resourceStoreDto.getUnitCodeName() + "=" + resourceStoreDto.getMiniUnitStock() + resourceStoreDto.getMiniUnitCodeName());
            row.createCell(9).setCellValue(resourceStoreDto.getMiniStock() + resourceStoreDto.getMiniUnitCodeName());
            if (!StringUtil.isEmpty(resourceStoreDto.getAveragePrice())) {
                row.createCell(10).setCellValue("￥" + resourceStoreDto.getAveragePrice());
            } else {
                row.createCell(10).setCellValue("￥0");
            }
            //查询物品次数表resource_store_times，计算物品总价
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setResCode(resourceStoreDto.getResCode());
            resourceStoreTimesDto.setShId(resourceStoreDto.getShId());
            resourceStoreTimesDto.setStoreId(resourceStoreDto.getStoreId());
            List<ResourceStoreTimesDto> resourceStoreTimesList = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            BigDecimal totalPrice = new BigDecimal(0.00);
            if (resourceStoreTimesList != null && resourceStoreTimesList.size() > 0) {
                for (ResourceStoreTimesDto resourceStoreTimes : resourceStoreTimesList) {
                    BigDecimal stock = new BigDecimal(resourceStoreTimes.getStock()); //库存数
                    BigDecimal price = new BigDecimal(resourceStoreTimes.getPrice()); //资源单价
                    BigDecimal singlePrice = stock.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP); //计算单次总价
                    totalPrice = totalPrice.add(singlePrice);
                }
            }
            row.createCell(11).setCellValue("￥" + totalPrice);
        }
    }
}
