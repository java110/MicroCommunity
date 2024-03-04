package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.patrolBuilding.PatrolBuildingDto;
import com.java110.intf.community.IPatrolBuildingV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 员工巡楼导出
 *
 * @author fqz
 * @date 2024-01-03
 */
@Service("patrolBuildingManage")
public class PatrolBuildingManageAdapt implements IExportDataAdapt {

    @Autowired
    private IPatrolBuildingV1InnerServiceSMO patrolBuildingV1InnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("员工巡楼");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("巡楼编号");
        row.createCell(1).setCellValue("标题");
        row.createCell(2).setCellValue("员工ID");
        row.createCell(3).setCellValue("员工姓名");
        row.createCell(4).setCellValue("楼栋编号");
        row.createCell(5).setCellValue("有无异常");
        row.createCell(6).setCellValue("巡楼内容");
        row.createCell(7).setCellValue("登记时间");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getPatrolBuildingManage(sheet, reqJson);
        return workbook;
    }

    private void getPatrolBuildingManage(Sheet sheet, JSONObject reqJson) {
        PatrolBuildingDto patrolBuildingDto = BeanConvertUtil.covertBean(reqJson, PatrolBuildingDto.class);
        patrolBuildingDto.setPage(1);
        patrolBuildingDto.setRow(MAX_ROW);
        List<PatrolBuildingDto> patrolBuildingDtoList = patrolBuildingV1InnerServiceSMOImpl.queryPatrolBuildings(patrolBuildingDto);
        appendData(patrolBuildingDtoList, sheet);
    }

    private void appendData(List<PatrolBuildingDto> patrolBuildingList, Sheet sheet) {
        Row row = null;
        for (int index = 0; index < patrolBuildingList.size(); index++) {
            row = sheet.createRow(index + 1);
            PatrolBuildingDto patrolBuildingDto = patrolBuildingList.get(index);
            row.createCell(0).setCellValue(patrolBuildingDto.getPbId());
            row.createCell(1).setCellValue(patrolBuildingDto.getTitle());
            row.createCell(2).setCellValue(patrolBuildingDto.getStaffId());
            row.createCell(3).setCellValue(patrolBuildingDto.getStaffName());
            if (StringUtil.isEmpty(patrolBuildingDto.getFloorId()) || patrolBuildingDto.getFloorId().equals("-1")) {
                row.createCell(4).setCellValue("--");
            } else {
                row.createCell(4).setCellValue(patrolBuildingDto.getFloorNum() + "号楼");
            }
            if (patrolBuildingDto.getState().equals("0")) {
                row.createCell(5).setCellValue("无");
            } else {
                row.createCell(5).setCellValue("有");
            }
            row.createCell(6).setCellValue(patrolBuildingDto.getRemark());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell(7).setCellValue(format.format(patrolBuildingDto.getCreateTime()));
        }
    }
}
