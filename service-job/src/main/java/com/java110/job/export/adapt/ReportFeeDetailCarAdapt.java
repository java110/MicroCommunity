package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
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
@Service("reportFeeDetailCar")
public class ReportFeeDetailCarAdapt implements IExportDataAdapt {

    private static final int MAX_ROW = 200;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;


    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("车辆费用明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("车辆");
        row.createCell(1).setCellValue("业主");
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
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        long count = getCarCount(queryStatisticsDto);
        List<OwnerCarDto> carDtos = null;
        for (int page = 1; page <= count; page++) {
            queryStatisticsDto.setPage(page);
            queryStatisticsDto.setRow(MAX_ROW);
            carDtos = getCar(queryStatisticsDto);
            // todo 计算 合同欠费实收数据
            JSONArray datas = computeCarOweReceivedFee(carDtos, queryStatisticsDto);
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
            row.createCell(0).setCellValue(dataObj.getString("carNum"));
            row.createCell(1).setCellValue(dataObj.getString("ownerName") + "(" + dataObj.getString("link") + ")");
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


    public long getCarCount(QueryStatisticsDto queryStatisticsDto) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(queryStatisticsDto.getCommunityId());
        ownerCarDto.setCarNumLike(queryStatisticsDto.getObjName());
        ownerCarDto.setOwnerName(queryStatisticsDto.getOwnerName());
        ownerCarDto.setLink(queryStatisticsDto.getLink());
        ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        return ownerCarInnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
    }

    public List<OwnerCarDto> getCar(QueryStatisticsDto queryStatisticsDto) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(queryStatisticsDto.getCommunityId());
        ownerCarDto.setCarNumLike(queryStatisticsDto.getObjName());
        ownerCarDto.setOwnerName(queryStatisticsDto.getOwnerName());
        ownerCarDto.setLink(queryStatisticsDto.getLink());
        ownerCarDto.setPage(queryStatisticsDto.getPage());
        ownerCarDto.setRow(queryStatisticsDto.getRow());
        ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_PRIMARY);
        return ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
    }

    /**
     * 计算合同欠费 实收费用
     *
     * @param ownerCarDtos
     * @return
     */
    private JSONArray computeCarOweReceivedFee(List<OwnerCarDto> ownerCarDtos, QueryStatisticsDto queryStatisticsDto) {
        if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> objIds = new ArrayList<>();
        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            objIds.add(ownerCarDto.getCarId());
            data = new JSONObject();
            data.put("carId", ownerCarDto.getCarId());
            data.put("carNum", ownerCarDto.getCarNum() );
            data.put("ownerName", ownerCarDto.getOwnerName());
            data.put("ownerId", ownerCarDto.getOwnerId());
            data.put("link", ownerCarDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setObjIds(objIds.toArray(new String[objIds.size()]));
        List<Map> infos = reportFeeStatisticsInnerServiceSMOImpl.getOwnerFeeSummary(queryStatisticsDto);

        if (infos == null || infos.size() < 1) {
            return datas;
        }

        BigDecimal oweFee = new BigDecimal(0.00);
        BigDecimal receivedFee = new BigDecimal(0.00);
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            data = datas.getJSONObject(dataIndex);
            for (Map info : infos) {
                if (!data.get("carId").toString().equals(info.get("objId"))) {
                    continue;
                }

                oweFee = oweFee.add(new BigDecimal(info.get("oweFee").toString()));
                receivedFee = oweFee.add(new BigDecimal(info.get("receivedFee").toString()));
                data.put("oweFee" + info.get("feeTypeCd").toString(), info.get("oweFee"));
                data.put("receivedFee" + info.get("feeTypeCd").toString(), info.get("receivedFee"));
            }
            data.put("oweFee", oweFee.doubleValue());
            data.put("receivedFee", receivedFee.doubleValue());
        }

        return datas;
    }
}
