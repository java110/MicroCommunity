package com.java110.api.smo.assetExport.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetExport.IExportOwnerCarSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service("exportOwnerCarSMOImpl")
public class ExportOwnerCarSMOImpl extends DefaultAbstractComponentSMO implements IExportOwnerCarSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception {
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "communityId", "请求中未包含小区");
        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();
        //获取楼信息
        getOwnerCar(pd, result, workbook);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=ownerCarImport_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
        headers.add("Pargam", "no-cache");
        headers.add("Cache-Control", "no-cache");
        //headers.add("Content-Disposition", "attachment; filename=" + outParam.getString("fileName"));
        headers.add("Accept-Ranges", "bytes");
        byte[] context = null;
        try {
            workbook.write(os);
            context = os.toByteArray();
            os.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 保存数据
            return new ResponseEntity<Object>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 保存数据
        return new ResponseEntity<Object>(context, headers, HttpStatus.OK);
    }

    /**
     * 获取 房屋信息
     *
     * @param componentValidateResult
     * @param workbook
     */
    private void getOwnerCar(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("业主车辆信息");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("房屋号: 格式为xx-xx-xx(楼栋-单元-房屋)；\n车辆类型：9901表示家用小汽车，9902表示客车，9903表示货车；" +
                "\n停车场：停车场编号，系统中不存在自动创建" + "\n车位：停车位编号，系统中不存在自动创建" + "\n起租时间: 格式为YYYY-MM-DD；\n截止时间: 格式为YYYY-MM-DD；" +
                "\n停车场类型：1001表示地上停车场，2001表示地下停车场；\n车位状态：S表示出售，H表示出租；" +
                "\n注意：所有单元格式为文本");
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //关键
        cell0.setCellStyle(cs);
        row.setHeight((short) (200 * 15));
        row = sheet.createRow(1);
        row.createCell(0).setCellValue("车牌号");
        row.createCell(1).setCellValue("房屋号");
        row.createCell(2).setCellValue("车辆品牌");
        row.createCell(3).setCellValue("车辆类型");
        row.createCell(4).setCellValue("颜色");
        row.createCell(5).setCellValue("停车场");
        row.createCell(6).setCellValue("车位");
        row.createCell(7).setCellValue("起租时间");
        row.createCell(8).setCellValue("截止时间");
        row.createCell(9).setCellValue("停车场类型");
        row.createCell(10).setCellValue("车位状态");
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 8);
        sheet.addMergedRegion(region);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
