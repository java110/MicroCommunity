package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 我的物品导出
 *
 * @author fqz
 * @date 2023-11-26
 */
@Service(value = "myResourceStoreManage")
public class MyResourceStoreManage implements IExportDataAdapt {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("我的物品");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("用户ID");
        row.createCell(1).setCellValue("用户名");
        row.createCell(2).setCellValue("物品ID");
        row.createCell(3).setCellValue("物品类型");
        row.createCell(4).setCellValue("物品名称");
        row.createCell(5).setCellValue("物品规格");
        row.createCell(6).setCellValue("物品编码");
        row.createCell(7).setCellValue("是否是固定物品");
        row.createCell(8).setCellValue("物品库存");
        row.createCell(9).setCellValue("最小计量总数");
        row.createCell(10).setCellValue("最小计量");
        row.createCell(11).setCellValue("收费标准");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getMyResourceStoreManage(sheet, reqJson);
        return workbook;
    }

    private void getMyResourceStoreManage(Sheet sheet, JSONObject reqJson) {
        UserStorehouseDto userStorehouseDto = BeanConvertUtil.covertBean(reqJson, UserStorehouseDto.class);
        //调拨记录（调拨记录所有权限查看所有数据）
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/everythingGoods");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        userStorehouseDto.setPage(1);
        userStorehouseDto.setRow(MAX_ROW);
        if (privileges.size() > 0) {
            userStorehouseDto.setUserId("");
            userStorehouseDto.setUserName("");
        }
        userStorehouseDto.setLagerStockZero("1"); //这一步是筛选个人物品数量大于0的数据
        List<UserStorehouseDto> userStorehouses = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
        appendData(userStorehouses, sheet);
    }

    private void appendData(List<UserStorehouseDto> userStorehouses, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < userStorehouses.size(); index++) {
            row = sheet.createRow(index + 1);
            UserStorehouseDto userStorehouseDto = userStorehouses.get(index);
            row.createCell(0).setCellValue(userStorehouseDto.getUserId());
            row.createCell(1).setCellValue(userStorehouseDto.getUserName());
            row.createCell(2).setCellValue(userStorehouseDto.getResId());
            row.createCell(3).setCellValue(userStorehouseDto.getParentRstName() + ">" + userStorehouseDto.getRstName());
            row.createCell(4).setCellValue(userStorehouseDto.getResName());
            if (!StringUtil.isEmpty(userStorehouseDto.getSpecName())) {
                row.createCell(5).setCellValue(userStorehouseDto.getSpecName());
            } else {
                row.createCell(5).setCellValue("--");
            }
            row.createCell(6).setCellValue(userStorehouseDto.getResCode());
            row.createCell(7).setCellValue(userStorehouseDto.getIsFixedName());
            row.createCell(8).setCellValue(userStorehouseDto.getStock() + userStorehouseDto.getUnitCodeName());
            row.createCell(9).setCellValue(userStorehouseDto.getMiniStock() + userStorehouseDto.getMiniUnitCodeName());
            row.createCell(10).setCellValue("1" + userStorehouseDto.getUnitCodeName() + "=" + userStorehouseDto.getMiniUnitStock() + userStorehouseDto.getMiniUnitCodeName());
            if (userStorehouseDto.getOutHighPrice().equals(userStorehouseDto.getOutLowPrice())) {
                row.createCell(11).setCellValue("￥" + userStorehouseDto.getOutLowPrice());
            } else {
                row.createCell(11).setCellValue("￥" + userStorehouseDto.getOutLowPrice() + "-￥" + userStorehouseDto.getOutHighPrice());
            }
        }
    }
}
