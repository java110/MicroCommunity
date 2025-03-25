package com.java110.api.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetExport.IExportRoomSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.room.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.parking.ParkingSpaceDto;
import com.java110.dto.system.ComponentValidateResult;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeConfigV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("exportRoomSMOImpl")
public class ExportRoomSMOImpl extends DefaultAbstractComponentSMO implements IExportRoomSMO {
    private final static Logger logger = LoggerFactory.getLogger(ExportRoomSMOImpl.class);

    public static final String TYPE_ROOM = "1001";
    public static final String TYPE_PARKSPACE = "2002";
    public static final String TYPE_CONTRACT = "3003"; //合同

    public static final int DEFAULT_ROW = 500;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getExistsRoom(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        apiUrl = "room.queryRooms?page=1&row=10000&communityId=" + result.getCommunityId();
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }

        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody());


        if (!savedRoomInfoResults.containsKey("rooms")) {
            return null;
        }


        return savedRoomInfoResults.getJSONArray("rooms");

    }



    /**
     * 导出报表
     *
     * @param pd 前台数据封装
     * @return
     * @throws Exception
     */
    @Override
    public ResponseEntity<Object> exportCustomReportTableData(IPageData pd) throws Exception {
        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");
        //Assert.hasKeyAndValue(paramIn, "floorIds", "请求中未包含楼栋");

        Workbook workbook = null;  //工作簿
        //工作表
        workbook = new XSSFWorkbook();

        //查询资产和费用项
        getCustomReportTableData(paramIn, workbook, pd);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=customReportTableImport_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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


    private void getCustomReportTableData(JSONObject paramIn, Workbook workbook, IPageData pd) {
        Sheet sheet = workbook.createSheet("报表数据");
        String apiUrl = "reportCustomComponent.listReportCustomComponentData" + super.mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return;
        }
        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());
        if (paramOut.getIntValue("code") != 0) {
            return;
        }
        JSONArray th = paramOut.getJSONObject("data").getJSONArray("th");

        if (th == null || th.size() < 1) {
            return;
        }

        Row row = sheet.createRow(0);
        for (int thIndex = 0; thIndex < th.size(); thIndex++) {
            row.createCell(thIndex).setCellValue(th.getString(thIndex));
        }

        JSONArray td = paramOut.getJSONObject("data").getJSONArray("td");

        if (td == null || td.size() < 1) {
            return;
        }
        JSONObject tdObj = null;
        for (int tdIndex = 0; tdIndex < td.size(); tdIndex++) {
            row = sheet.createRow(tdIndex + 1);
            tdObj = td.getJSONObject(tdIndex);
            for (int thIndex = 0; thIndex < th.size(); thIndex++) {
                row.createCell(thIndex).setCellValue(tdObj.getString(th.getString(thIndex)));
            }
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
