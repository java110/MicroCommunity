package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.FloorDto;
import com.java110.dto.RoomDto;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.report.IReportFeeStatisticsInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.DateUtil;
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
 * 房屋费用明细导出
 */
@Service("reportFeeDetailRoom")
public class ReportFeeDetailRoomAdapt implements IExportDataAdapt {

    private static final int MAX_ROW = 200;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Autowired
    private IReportFeeStatisticsInnerServiceSMO reportFeeStatisticsInnerServiceSMOImpl;

    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        SXSSFWorkbook workbook = null;  //工作簿
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("房屋费用明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("房屋");
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
        doReportData(sheet, reqJson,dictDtos);

        for (int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
            row.createCell(4 + dictIndex * 2).setCellValue(dictDtos.get(dictIndex).getName());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4 + dictIndex * 2, 5 + dictIndex * 2));
        }
        return workbook;
    }

    private void doReportData(Sheet sheet, JSONObject reqJson,List<DictDto> dictDtos) {

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
        //todo 查询记录数
        long count = getRoomCount(queryStatisticsDto);

        List<RoomDto> rooms = null;
        for(int page = 1;page <= count; page++){
            queryStatisticsDto.setPage(page);
            queryStatisticsDto.setRow(MAX_ROW);
            rooms = getRoomInfo(queryStatisticsDto);
            // todo 计算 房屋欠费实收数据
            JSONArray datas = computeRoomOweReceivedFee(rooms,queryStatisticsDto);
            appendData(datas,sheet,(page-1)*MAX_ROW,dictDtos);
        }


    }


    private void appendData(JSONArray datas, Sheet sheet, int step,List<DictDto> dictDtos) {
        Row row = null;
        JSONObject dataObj = null;
        String oweFee = "";
        String receivedFee = "";
        for (int roomIndex = 0; roomIndex < datas.size(); roomIndex++) {
            row = sheet.createRow(roomIndex +step + 1);
            dataObj = datas.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("roomName"));
            row.createCell(1).setCellValue(dataObj.getString("ownerName")+"("+dataObj.getString("link")+")");
            row.createCell(2).setCellValue(dataObj.getString("oweFee"));
            row.createCell(3).setCellValue(dataObj.getString("receivedFee"));

            for(int dictIndex = 0; dictIndex < dictDtos.size(); dictIndex++) {
                oweFee = dataObj.getString("oweFee"+dictDtos.get(0).getStatusCd());
                if(StringUtil.isEmpty(oweFee)){
                    oweFee = "0";
                }
                receivedFee = dataObj.getString("receivedFee"+dictDtos.get(0).getStatusCd());
                if(StringUtil.isEmpty(receivedFee)){
                    receivedFee = "0";
                }
                row.createCell(4 + dictIndex*2).setCellValue(oweFee);
                row.createCell(4 + dictIndex*2+1).setCellValue(receivedFee);
            }
        }

    }


    public long getRoomCount(QueryStatisticsDto queryStatisticsDto) {

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());

        addRoomNumCondition(queryStatisticsDto, roomDto);
        return baseDataStatisticsInnerServiceSMOImpl.getRoomCount(roomDto);
    }

    public List<RoomDto> getRoomInfo(QueryStatisticsDto queryStatisticsDto) {
        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(queryStatisticsDto.getCommunityId());
        roomDto.setFloorId(queryStatisticsDto.getFloorId());
        roomDto.setPage(queryStatisticsDto.getPage());
        roomDto.setRow(queryStatisticsDto.getRow());
        addRoomNumCondition(queryStatisticsDto, roomDto);
        return baseDataStatisticsInnerServiceSMOImpl.getRoomInfo(roomDto);
    }

    /**
     * roomNum 拆分 查询房屋信息
     *
     * @param queryStatisticsDto
     * @param roomDto
     */
    private void addRoomNumCondition(QueryStatisticsDto queryStatisticsDto, RoomDto roomDto) {
        if (StringUtil.isEmpty(queryStatisticsDto.getObjName())) {
            return;
        }
        if (!queryStatisticsDto.getObjName().contains("-")) {
            roomDto.setRoomNumLike(queryStatisticsDto.getObjName());
            return;
        }
        String[] objNames = queryStatisticsDto.getObjName().split("-");
        if (objNames.length == 2) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum("0");
            roomDto.setRoomNum(objNames[1]);
            return;
        }
        objNames = queryStatisticsDto.getObjName().split("-", 3);
        if (objNames.length == 3) {
            roomDto.setFloorNum(objNames[0]);
            roomDto.setUnitNum(objNames[1]);
            roomDto.setRoomNum(objNames[2]);
        }

    }

    /**
     * 计算房屋欠费 实收费用
     *
     * @param rooms
     * @return
     */
    private JSONArray computeRoomOweReceivedFee(List<RoomDto> rooms,QueryStatisticsDto queryStatisticsDto) {
        if (rooms == null || rooms.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> objIds = new ArrayList<>();
        for (RoomDto roomDto : rooms) {
            objIds.add(roomDto.getRoomId());
            data = new JSONObject();
            data.put("roomId",roomDto.getRoomId());
            data.put("roomName",roomDto.getFloorNum()+"-"+roomDto.getUnitNum()+"-"+roomDto.getRoomNum());
            data.put("ownerName",roomDto.getOwnerName());
            data.put("ownerId",roomDto.getOwnerId());
            data.put("link",roomDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setObjIds(objIds.toArray(new String[objIds.size()]));
        List<Map> infos = reportFeeStatisticsInnerServiceSMOImpl.getObjFeeSummary(queryStatisticsDto);

        if(infos == null || infos.size() < 1){
            return datas;
        }

        BigDecimal oweFee = new BigDecimal(0.00);
        BigDecimal receivedFee = new BigDecimal(0.00);
        for(int dataIndex = 0; dataIndex < datas.size();dataIndex ++){
            data = datas.getJSONObject(dataIndex);
            for(Map info : infos){
                if(!data.get("roomId").toString().equals(info.get("objId"))){
                    continue;
                }

                oweFee = oweFee.add(new BigDecimal(info.get("oweFee").toString()));
                receivedFee = oweFee.add(new BigDecimal(info.get("receivedFee").toString()));
                data.put("oweFee"+info.get("feeTypeCd").toString(),info.get("oweFee"));
                data.put("receivedFee"+info.get("feeTypeCd").toString(),info.get("receivedFee"));
            }
            data.put("oweFee",oweFee.doubleValue());
            data.put("receivedFee",receivedFee.doubleValue());
        }

        return datas;
    }
}
