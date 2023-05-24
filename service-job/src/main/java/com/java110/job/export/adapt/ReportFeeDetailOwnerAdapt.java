package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.RoomDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业主费用明细导出
 */
@Service("reportFeeDetailOwner")
public class ReportFeeDetailOwnerAdapt implements IExportDataAdapt {

    private static final int MAX_ROW = 200;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;


    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("业主费用明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("业主");
        row.createCell(1).setCellValue("房屋");
        row.createCell(2).setCellValue("欠费");
        row.createCell(3).setCellValue("实收");

        DictDto dictDto = new DictDto();
        dictDto.setTableName("pay_fee_config");
        dictDto.setTableColumns("fee_type_cd_show");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
            row.createCell(4 + dictIndex * 2).setCellValue(dictDtos.get(dictIndex).getName());
        }


        JSONObject reqJson = exportDataDto.getReqJson();

        //todo 查询数据
        doReportData(sheet, reqJson, dictDtos);

        for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
            row.createCell(4 + dictIndex * 2).setCellValue(dictDtos.get(dictIndex).getName());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4 + dictIndex * 2, 5 + dictIndex * 2));
        }
        return workbook;
    }

    private void doReportData(Sheet sheet, JSONObject reqJson, List<DictDto> dictDtos) {

        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        long count = getOwnerCount(queryStatisticsDto);
        List<OwnerDto> ownerDtos = null;
        for (int page = 1; page <= count; page++) {
            queryStatisticsDto.setPage(page);
            queryStatisticsDto.setRow(MAX_ROW);
            ownerDtos = getOwnerInfo(queryStatisticsDto);
            // todo 计算 房屋欠费实收数据
            JSONArray datas = computeOwnerOweReceivedFee(ownerDtos, queryStatisticsDto);
            appendData(datas, sheet, (page - 1) * MAX_ROW, dictDtos);
        }


    }


    private void appendData(JSONArray datas, Sheet sheet, int step, List<DictDto> dictDtos) {
        Row row = null;
        JSONObject dataObj = null;
        String oweFee = "";
        String receivedFee = "";
        for (int roomIndex = 0; roomIndex < datas.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = datas.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("ownerName") + "(" + dataObj.getString("link") + ")");
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("oweFee"));
            row.createCell(3).setCellValue(dataObj.getString("receivedFee"));

            for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
                oweFee = dataObj.getString("oweFee" + dictDtos.get(0).getStatusCd());
                if (StringUtil.isEmpty(oweFee)) {
                    oweFee = "0";
                }
                receivedFee = dataObj.getString("receivedFee" + dictDtos.get(0).getStatusCd());
                if (StringUtil.isEmpty(receivedFee)) {
                    receivedFee = "0";
                }
                row.createCell(4 + dictIndex * 2).setCellValue(oweFee);
                row.createCell(4 + dictIndex * 2 + 1).setCellValue(receivedFee);
            }
        }

    }


    public long getOwnerCount(QueryStatisticsDto queryStatisticsDto) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setNameLike(queryStatisticsDto.getOwnerName());
        ownerDto.setLink(queryStatisticsDto.getLink());
        ownerDto.setCommunityId(queryStatisticsDto.getCommunityId());
        return ownerV1InnerServiceSMOImpl.queryOwnersCount(ownerDto);
    }

    public List<OwnerDto> getOwnerInfo(QueryStatisticsDto queryStatisticsDto) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerTypeCd(OwnerDto.OWNER_TYPE_CD_OWNER);
        ownerDto.setNameLike(queryStatisticsDto.getOwnerName());
        ownerDto.setCommunityId(queryStatisticsDto.getCommunityId());
        ownerDto.setLink(queryStatisticsDto.getLink());
        ownerDto.setPage(queryStatisticsDto.getPage());
        ownerDto.setRow(queryStatisticsDto.getRow());
        return ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
    }


    /**
     * 计算业主欠费 实收费用
     *
     * @param owners
     * @return
     */
    private JSONArray computeOwnerOweReceivedFee(List<OwnerDto> owners, QueryStatisticsDto queryStatisticsDto) {
        if (owners == null || owners.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> ownerIds = new ArrayList<>();
        for (OwnerDto ownerDto : owners) {
            ownerIds.add(ownerDto.getOwnerId());
            data = new JSONObject();
            data.put("ownerName", ownerDto.getName());
            data.put("ownerId", ownerDto.getOwnerId());
            data.put("link", ownerDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> infos = reportFeeStatisticsInnerServiceSMOImpl.getOwnerFeeSummary(queryStatisticsDto);

        if (infos == null || infos.size() < 1) {
            return datas;
        }

        BigDecimal oweFee = new BigDecimal(0.00);
        BigDecimal receivedFee = new BigDecimal(0.00);
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            data = datas.getJSONObject(dataIndex);
            for (Map info : infos) {
                if (!data.get("ownerId").toString().equals(info.get("ownerId"))) {
                    continue;
                }

                oweFee = oweFee.add(new BigDecimal(info.get("oweFee").toString()));
                receivedFee = oweFee.add(new BigDecimal(info.get("receivedFee").toString()));
                data.put("oweFee" + info.get("feeTypeCd").toString(), info.get("oweFee"));
                data.put("receivedFee" + info.get("feeTypeCd").toString(), info.get("receivedFee"));
                data.put("objName", info.get("objName"));
            }
            data.put("oweFee", oweFee.doubleValue());
            data.put("receivedFee", receivedFee.doubleValue());
            // todo 处理 收费对象重复问题
            delRepeatObjName(data);
        }


        return datas;
    }

    /**
     * 去除 重复的objName
     * @param data
     */
    private void delRepeatObjName(JSONObject data) {

        String objName = data.getString("objName");
        if (StringUtil.isEmpty(objName)) {
            return;
        }

        String[] objNames = objName.split(",");
        List<String> oNames = new ArrayList<>();
        for (String oName : objNames) {
            if (!oNames.contains(oName)) {
                oNames.add(oName);
            }
        }
        objName = "";
        for (String oName : oNames) {
            objName += (oName + ",");
        }
        if (objName.endsWith(",")) {
            objName = objName.substring(0, objName.length() - 1);
        }

        data.put("objName", objName);
    }
}
