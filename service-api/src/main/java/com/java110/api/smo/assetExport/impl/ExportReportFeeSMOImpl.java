package com.java110.api.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.assetExport.IExportReportFeeSMO;
import com.java110.core.context.IPageData;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AssetImportSmoImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2019/9/23 23:14
 * @Version 1.0
 * add by wuxw 2019/9/23
 **/
@Service("exportReportFeeSMOImpl")
public class ExportReportFeeSMOImpl extends DefaultAbstractComponentSMO implements IExportReportFeeSMO {

    private final static Logger logger = LoggerFactory.getLogger(ExportReportFeeSMOImpl.class);

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键(导出最大限制条数)
    public static final String EXPORT_NUMBER = "EXPORT_NUMBER";

    public static final String REPORT_FEE_SUMMARY = "reportFeeSummary";
    public static final String REPORT_FLOOR_UNIT_FEE_SUMMARY = "reportFloorUnitFeeSummary";
    public static final String REPORT_FEE_BREAKDOWN = "reportFeeBreakdown";
    public static final String REPORT_FEE_DETAIL = "reportFeeDetail";
    public static final String REPORT_RETURN_PAY_FEE_MANAGE = "returnPayFeeManage";
    public static final String REPORT_OWE_FEE_DETAIL = "reportOweFeeDetail";
    public static final String REPORT_PAY_FEE_DETAIL = "reportPayFeeDetail";
    public static final String REPORT_YEAR_COLLECTION = "reportYearCollection";
    public static final String REPORT_LIST_OWE_FEE = "listOweFee";
    public static final String REPORT_REPAIR_DETAIL = "reportRepairDetail";
    public static final String REPORT_PAY_FEE_MANAGE = "reportPayFeeManage";
    public static final String REPORT_QUESTION_ANSWER_DETAIL = "reportQuestionAnswerDetail";
    public static final String REPORT_PRE_PAYMENT_FEE = "reportPrePaymentFee";
    public static final String REPORT_DEAD_LINE_FEE = "reportDeadlineFee";
    public static final String REPORT_NO_FEE_ROOM = "reportNoFeeRoom";
    public static final String REPORT_PURCHASE_APPLY_DETAIL = "purchaseApplyDetail";
    public static final String REPORT_ALLOCATION_STORE_HOUSE = "allocationStorehouse";
    public static final String REPORT_ALLOCATION_STORE_HOUSE_DETAIL = "allocationStorehouseDetail";
    public static final String REPORT_RESOURCE_STORE_MANAGE = "resourceStoreManage";
    public static final String REPORT_PURCHASE_APPLY_MANAGE = "purchaseApplyManage";
    public static final String REPORT_ITEM_OUT_MANAGE = "itemOutManage";
    public static final String ALLOCATION_USER_STORE_HOUSE_MANAGE = "allocationUserStorehouseManage";
    public static final String RESOURCE_STORE_USE_RECORD_MANAGE = "resourceStoreUseRecordManage";
    public static final String RESOURCE_STAFF_FEE_MANAGE = "staffFeeManage";
    public static final String REPORT_PAY_FEE_DEPOSIT = "reportPayFeeDeposit";
    public static final String INSPECTION_TASK_DETAILS = "inspectionTaskDetails";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) throws Exception {

        ComponentValidateResult result = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        Map pdHeaders = pd.getHeaders();
        if (!StringUtil.isEmpty(result.getStoreId())) {
            pdHeaders.remove("store-id");
            pdHeaders.put("store-id", result.getStoreId());
        }
        if (!StringUtil.isEmpty(result.getLoginUserId())) {
            pdHeaders.remove("user-id");
            pdHeaders.remove("user_id");
            pdHeaders.put("user-id", result.getUserId());
            pdHeaders.put("user_id", result.getUserId());
            pdHeaders.put("login-user-id",result.getLoginUserId());
        }

        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "communityId", "请求中未包含小区");
        Assert.hasKeyAndValue(JSONObject.parseObject(pd.getReqData()), "pagePath", "请求中未包含页面");

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        String pagePath = reqJson.getString("pagePath");

        switch (pagePath) {
            case REPORT_FEE_SUMMARY:
                reportFeeSummary(pd, result, workbook);
                break;
            case REPORT_FLOOR_UNIT_FEE_SUMMARY:
                reportFloorUnitFeeSummary(pd, result, workbook);
                break;
            case REPORT_FEE_BREAKDOWN:
                reportFeeBreakdown(pd, result, workbook);
                break;
            case REPORT_RETURN_PAY_FEE_MANAGE:
                returnPayFeeManage(pd, result, workbook);
                break;
            case REPORT_FEE_DETAIL:
                reportFeeDetail(pd, result, workbook);
                break;
            case REPORT_OWE_FEE_DETAIL:
                reportOweFeeDetail(pd, result, workbook);
                break;
            case REPORT_PAY_FEE_DETAIL:
                reportPayFeeDetail(pd, result, workbook);
                break;
            case REPORT_YEAR_COLLECTION:
                reportYearCollection(pd, result, workbook);
                break;
            case REPORT_LIST_OWE_FEE:
                reportListOweFee(pd, result, workbook);
                break;
            case REPORT_REPAIR_DETAIL:
                reportRepairDetail(pd, result, workbook);
                break;
            case REPORT_PAY_FEE_MANAGE:
                reportPayFeeManage(pd, result, workbook);
                break;
            case REPORT_QUESTION_ANSWER_DETAIL:
                reportQuestionAnswerDetail(pd, result, workbook);
                break;
            case REPORT_PRE_PAYMENT_FEE:
                reportPrePaymentFee(pd, result, workbook);
                break;
            case REPORT_DEAD_LINE_FEE:
                reportDeadlineFee(pd, result, workbook);
                break;
            case REPORT_NO_FEE_ROOM:
                reportNoFeeRoom(pd, result, workbook);
                break;
            case REPORT_PURCHASE_APPLY_DETAIL:
                purchaseApplyDetailManage(pd, result, workbook);
                break;
            case REPORT_ALLOCATION_STORE_HOUSE:
                allocationStorehouse(pd, result, workbook);
                break;
            case REPORT_ALLOCATION_STORE_HOUSE_DETAIL:
                allocationStorehouseDetail(pd, result, workbook);
                break;
            case REPORT_RESOURCE_STORE_MANAGE:
                resourceStoreManage(pd, result, workbook);
                break;
            case REPORT_PURCHASE_APPLY_MANAGE:
                purchaseApplyManage(pd, result, workbook);
                break;
            case REPORT_ITEM_OUT_MANAGE:
                itemOutManage(pd, result, workbook);
                break;
            case ALLOCATION_USER_STORE_HOUSE_MANAGE:
                allocationUserStorehouseManage(pd, result, workbook);
                break;
            case RESOURCE_STORE_USE_RECORD_MANAGE:
                resourceStoreUseRecordManage(pd, result, workbook);
                break;
            case RESOURCE_STAFF_FEE_MANAGE:
                staffFeeManage(pd, result, workbook);
                break;
            case REPORT_PAY_FEE_DEPOSIT:
                reportPayFeeDeposit(pd, result, workbook);
                break;
            case INSPECTION_TASK_DETAILS:
                inspectionTaskDetails(pd, result, workbook);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=" + pagePath + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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

    private void reportListOweFee(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        String configIds = reqJson.getString("configIds");
        //查询楼栋信息
        JSONArray oweFees = this.getReportListOweFees(pd, result);
        if (oweFees == null) {
            return;
        }
        //获取费用项
        List<FeeConfigDto> feeConfigDtos = getFeeConfigs(oweFees);
        Sheet sheet = workbook.createSheet("欠费清单");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("收费对象");
        row.createCell(1).setCellValue("业主名称");
        row.createCell(2).setCellValue("手机号");
        row.createCell(3).setCellValue("开始时间");
        row.createCell(4).setCellValue("结束时间");
        if (!StringUtil.isEmpty(configIds)) {
            for (int feeConfigIndex = 0; feeConfigIndex < feeConfigDtos.size(); feeConfigIndex++) {
                row.createCell(5 + feeConfigIndex).setCellValue(feeConfigDtos.get(feeConfigIndex).getFeeName());
            }
            row.createCell(5 + feeConfigDtos.size()).setCellValue("合计");
        } else {
            row.createCell(5).setCellValue("合计");
        }


        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < oweFees.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = oweFees.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("payerObjName"));
            row.createCell(1).setCellValue(dataObj.getString("ownerName"));
            row.createCell(2).setCellValue(dataObj.getString("ownerTel"));
            row.createCell(3).setCellValue(dataObj.getString("endTime"));
            row.createCell(4).setCellValue(dataObj.getString("deadlineTime"));
            if (!StringUtil.isEmpty(configIds)) {
                for (int feeConfigIndex = 0; feeConfigIndex < feeConfigDtos.size(); feeConfigIndex++) {
                    row.createCell(5 + feeConfigIndex).setCellValue(getFeeConfigAmount(feeConfigDtos.get(feeConfigIndex), dataObj));
                }
                row.createCell(5 + feeConfigDtos.size()).setCellValue(getAllFeeOweAmount(feeConfigDtos, dataObj));
            } else {
                row.createCell(5).setCellValue(getAllFeeOweAmount(feeConfigDtos, dataObj));
            }

        }
    }

    /**
     * _getAllFeeOweAmount: function (_fee) {
     * let _feeConfigNames = $that.listOweFeeInfo.feeConfigNames;
     * if (_feeConfigNames.length < 1) {
     * return _fee.amountOwed;
     * }
     * <p>
     * let _amountOwed = 0.0;
     * let _items = _fee.items;
     * _feeConfigNames.forEach(_feeItem =>{
     * _items.forEach(_item=>{
     * if(_feeItem.configId == _item.configId){
     * _amountOwed += parseFloat(_item.amountOwed);
     * }
     * })
     * })
     * return _amountOwed;
     * }
     *
     * @param dataObj
     * @return
     */
    private double getAllFeeOweAmount(List<FeeConfigDto> feeConfigDtos, JSONObject dataObj) {
        if (feeConfigDtos == null || feeConfigDtos.size() < 1) {
            return dataObj.getDouble("amountOwed");
        }
        JSONArray items = dataObj.getJSONArray("items");
        if (items == null || items.size() < 1) {
            return dataObj.getDouble("amountOwed");
        }

        BigDecimal oweAmount = new BigDecimal(0);
        for (FeeConfigDto feeConfigDto : feeConfigDtos) {
            for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
                if (feeConfigDto.getConfigId().equals(items.getJSONObject(itemIndex).getString("configId"))) {
                    oweAmount = oweAmount.add(new BigDecimal(items.getJSONObject(itemIndex).getDouble("amountOwed")));
                }
            }
        }

        return oweAmount.doubleValue();
    }

    private double getFeeConfigAmount(FeeConfigDto feeConfigDto, JSONObject dataObj) {
        JSONArray items = dataObj.getJSONArray("items");
        double oweAmount = 0;

        if (items == null || items.size() < 1) {
            return oweAmount;
        }

        for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
            if (feeConfigDto.getConfigId().equals(items.getJSONObject(itemIndex).getString("configId"))) {
                oweAmount = items.getJSONObject(itemIndex).getDouble("amountOwed");
                break;
            }
        }
        return oweAmount;
    }


    private List<FeeConfigDto> getFeeConfigs(JSONArray oweFees) {
        List<FeeConfigDto> feeConfigDtos = new ArrayList<>();
        FeeConfigDto feeConfigDto = null;
        for (int oweFeeIndex = 0; oweFeeIndex < oweFees.size(); oweFeeIndex++) {
            JSONArray items = oweFees.getJSONObject(oweFeeIndex).getJSONArray("items");
            for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
                if (existsFeeConfig(feeConfigDtos, items.getJSONObject(itemIndex))) {
                    continue;
                }
                feeConfigDto = new FeeConfigDto();
                feeConfigDto.setConfigId(items.getJSONObject(itemIndex).getString("configId"));
                feeConfigDto.setFeeName(items.getJSONObject(itemIndex).getString("configName"));
                feeConfigDtos.add(feeConfigDto);
            }
        }

        return feeConfigDtos;
    }

    private boolean existsFeeConfig(List<FeeConfigDto> feeConfigDtos, JSONObject oweFee) {
        if (feeConfigDtos.size() < 1) {
            return false;
        }
        for (FeeConfigDto feeConfigDto : feeConfigDtos) {
            if (feeConfigDto.getConfigId().equals(oweFee.getString("configId"))) {
                return true;
            }
        }

        return false;
    }


    private void reportPayFeeDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("缴费明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("订单号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("费用项");
        row.createCell(4).setCellValue("费用类型");
        row.createCell(5).setCellValue("费用状态");
        row.createCell(6).setCellValue("支付方式");
        row.createCell(7).setCellValue("费用开始时间");
        row.createCell(8).setCellValue("费用结束时间");
        row.createCell(9).setCellValue("缴费时间");
        row.createCell(10).setCellValue("应收金额");
        row.createCell(11).setCellValue("实收金额");
        row.createCell(12).setCellValue("优惠金额");
        row.createCell(13).setCellValue("减免金额");
        row.createCell(14).setCellValue("赠送金额");
        row.createCell(15).setCellValue("滞纳金");
        row.createCell(16).setCellValue("空置房打折金额");
        row.createCell(17).setCellValue("空置房减免金额");
        row.createCell(18).setCellValue("面积");
        row.createCell(19).setCellValue("车位");
        //查询楼栋信息
        JSONArray rooms = this.getReportPayFeeDetail(pd, result);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("oId"));
            if (!StringUtil.isEmpty(dataObj.getString("payerObjType")) && dataObj.getString("payerObjType").equals("3333")) { //房屋
                row.createCell(1).setCellValue(dataObj.getString("floorNum") + "-" + dataObj.getString("unitNum") + "-" + dataObj.getString("roomNum"));
            } else {
                row.createCell(1).setCellValue(dataObj.getString("objName"));
            }
            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("feeName"));
            row.createCell(4).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(5).setCellValue(dataObj.getString("stateName"));
            row.createCell(6).setCellValue(dataObj.getString("primeRate"));
            row.createCell(7).setCellValue(dataObj.getString("startTime"));
            row.createCell(8).setCellValue(dataObj.getString("endTime"));
            row.createCell(9).setCellValue(dataObj.getString("createTime"));
            row.createCell(10).setCellValue(dataObj.getDouble("receivableAmount"));
            row.createCell(11).setCellValue(dataObj.getDouble("receivedAmount"));
            row.createCell(12).setCellValue(dataObj.getDouble("preferentialAmount"));
            row.createCell(13).setCellValue(dataObj.getDouble("deductionAmount"));
            row.createCell(14).setCellValue(dataObj.getDouble("giftAmount"));
            row.createCell(15).setCellValue(dataObj.getDouble("lateFee"));
            row.createCell(16).setCellValue(dataObj.getDouble("vacantHousingDiscount"));
            row.createCell(17).setCellValue(dataObj.getDouble("vacantHousingReduction"));
            row.createCell(18).setCellValue(dataObj.getString("builtUpArea"));
            row.createCell(19).setCellValue(dataObj.getString("psName"));
        }
    }

    private void reportRepairDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("报修汇总表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("员工ID");
        row.createCell(1).setCellValue("员工姓名");
        row.createCell(2).setCellValue("处理中条数");
        row.createCell(3).setCellValue("派单条数");
        row.createCell(4).setCellValue("转单条数");
        row.createCell(5).setCellValue("退单条数");
        row.createCell(6).setCellValue("已回访条数");
        row.createCell(7).setCellValue("已完结条数");
        row.createCell(8).setCellValue("员工评分");

        //查询报修信息
        JSONArray repairs = this.getReportRepairDetail(pd, result);
        if (repairs == null || repairs.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < repairs.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = repairs.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("staffId"));
            row.createCell(1).setCellValue(dataObj.getString("staffName"));
            row.createCell(2).setCellValue(dataObj.getString("dealAmount"));
            row.createCell(3).setCellValue(dataObj.getString("dispatchAmount"));
            row.createCell(4).setCellValue(dataObj.getString("transferOrderAmount"));
            row.createCell(5).setCellValue(dataObj.getString("chargebackAmount"));
            row.createCell(6).setCellValue(dataObj.getString("returnAmount"));
            row.createCell(7).setCellValue(dataObj.getString("statementAmount"));
            row.createCell(8).setCellValue(dataObj.getString("score"));
        }
    }

    private void reportPayFeeManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("缴费清单");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用类型");
        row.createCell(1).setCellValue("费用项目");
        row.createCell(2).setCellValue("付费方");
        row.createCell(3).setCellValue("缴费ID");
        row.createCell(4).setCellValue("支付方式");
        row.createCell(5).setCellValue("付费周期(月)");
        row.createCell(6).setCellValue("应付金额(元)");
        row.createCell(7).setCellValue("实付金额(元)");
        row.createCell(8).setCellValue("操作员工");
        row.createCell(9).setCellValue("缴费时间");

        //查询缴费信息
        JSONArray payFees = this.getReportPayFeeManage(pd, result);
        if (payFees == null || payFees.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < payFees.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = payFees.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(1).setCellValue(dataObj.getString("feeName"));
            row.createCell(2).setCellValue(dataObj.getString("payObjName"));
            row.createCell(3).setCellValue(dataObj.getString("detailId"));
            row.createCell(4).setCellValue(dataObj.getString("primeRate"));
            row.createCell(5).setCellValue(dataObj.getString("cycles"));
            row.createCell(6).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(7).setCellValue(dataObj.getString("receivedAmount"));
            row.createCell(8).setCellValue(dataObj.getString("userName"));
            row.createCell(9).setCellValue(dataObj.getString("createTime"));
        }
    }

    private void reportQuestionAnswerDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("问卷明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("问卷人");
        row.createCell(1).setCellValue("问卷类型");
        row.createCell(2).setCellValue("问卷名称");
        row.createCell(3).setCellValue("问卷题目");
        row.createCell(4).setCellValue("答案");
        row.createCell(5).setCellValue("时间");

        JSONArray questionAnswers = this.getReportQuestionAnswerDetail(pd, result);
        if (questionAnswers == null || questionAnswers.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < questionAnswers.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = questionAnswers.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("userName"));
            row.createCell(1).setCellValue(dataObj.getString("qaTypeName"));
            row.createCell(2).setCellValue(dataObj.getString("qaName"));
            row.createCell(3).setCellValue(dataObj.getString("qaTitle"));
            row.createCell(4).setCellValue(dataObj.getString("qaValue"));
            row.createCell(5).setCellValue(dataObj.getString("createTime"));
        }
    }

    private void reportPrePaymentFee(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("预交费提醒表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("房号");
        row.createCell(1).setCellValue("费用项");
        row.createCell(2).setCellValue("费用开始时间");
        row.createCell(3).setCellValue("距离费用开始时间（天）");

        JSONArray prePayFees = this.getReportPrePaymentFee(pd, result);
        if (prePayFees == null || prePayFees.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < prePayFees.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = prePayFees.getJSONObject(roomIndex);
            if (!StringUtil.isEmpty(dataObj.getString("payerObjType")) && dataObj.getString("payerObjType").equals("3333")) { //房屋
                row.createCell(0).setCellValue(dataObj.getString("floorNum") + "-" + dataObj.getString("unitNum") + "-" + dataObj.getString("roomNum"));
            } else {
                row.createCell(0).setCellValue(dataObj.getString("objName"));
            }
            row.createCell(1).setCellValue(dataObj.getString("feeName"));
            row.createCell(2).setCellValue(dataObj.getString("endTime"));
            row.createCell(3).setCellValue(dataObj.getString("oweDay"));
        }
    }

    private void reportDeadlineFee(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用到期提醒表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("房号");
        row.createCell(1).setCellValue("费用项");
        row.createCell(2).setCellValue("费用结束时间");
        row.createCell(3).setCellValue("距离费用结束时间（天）");

        JSONArray deadlineFees = this.getReportDeadlineFee(pd, result);
        if (deadlineFees == null || deadlineFees.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < deadlineFees.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = deadlineFees.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("objNameNum"));
            row.createCell(1).setCellValue(dataObj.getString("feeName"));
            row.createCell(2).setCellValue(dataObj.getString("deadlineTime"));
            row.createCell(3).setCellValue(dataObj.getString("oweDay"));
        }
    }

    private void reportNoFeeRoom(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("未收费房屋表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("楼栋");
        row.createCell(1).setCellValue("单元");
        row.createCell(2).setCellValue("房屋");
        row.createCell(3).setCellValue("业主名称");
        row.createCell(4).setCellValue("业主电话");

        JSONArray noFeeRooms = this.getReportNoFeeRoom(pd, result);
        if (noFeeRooms == null || noFeeRooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < noFeeRooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = noFeeRooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("floorNum"));
            row.createCell(1).setCellValue(dataObj.getString("unitNum"));
            row.createCell(2).setCellValue(dataObj.getString("roomNum"));
            row.createCell(3).setCellValue(dataObj.getString("ownerName"));
            row.createCell(4).setCellValue(dataObj.getString("link"));
        }
    }

    private void purchaseApplyDetailManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("出入库明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("申请单号");
        row.createCell(1).setCellValue("申请人");
        row.createCell(2).setCellValue("使用人");
        row.createCell(3).setCellValue("出入库类型");
        row.createCell(4).setCellValue("物品类型");
        row.createCell(5).setCellValue("物品名称");
        row.createCell(6).setCellValue("物品规格");
        row.createCell(7).setCellValue("是否是固定物品");
        row.createCell(8).setCellValue("物品供应商");
        row.createCell(9).setCellValue("物品仓库");
        row.createCell(10).setCellValue("采购/出库方式");
        row.createCell(11).setCellValue("申请数量");
        row.createCell(12).setCellValue("采购/出库数量");
        row.createCell(13).setCellValue("采购价格");
        row.createCell(14).setCellValue("总价");
        row.createCell(15).setCellValue("申请备注");
        row.createCell(16).setCellValue("状态");
        row.createCell(17).setCellValue("创建时间");

        JSONArray purchaseApplyDetails = this.getPurchaseApplyDetail(pd, result);
        if (purchaseApplyDetails == null || purchaseApplyDetails.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < purchaseApplyDetails.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = purchaseApplyDetails.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("applyOrderId"));
            row.createCell(1).setCellValue(dataObj.getString("userName"));
            row.createCell(2).setCellValue(dataObj.getString("endUserName"));
            row.createCell(3).setCellValue(dataObj.getString("resOrderTypeName"));
            row.createCell(4).setCellValue(dataObj.getString("parentRstName") + ">" + dataObj.getString("rstName"));
            row.createCell(5).setCellValue(dataObj.getString("resName"));
            row.createCell(6).setCellValue(dataObj.getString("specName"));
            row.createCell(7).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(8).setCellValue(dataObj.getString("supplierName"));
            row.createCell(9).setCellValue(dataObj.getString("shName"));
            row.createCell(10).setCellValue(dataObj.getString("warehousingWayName") + dataObj.getString("resOrderTypeName"));
            row.createCell(11).setCellValue(dataObj.getString("quantity") + dataObj.getString("unitCodeName"));
            row.createCell(12).setCellValue(dataObj.getString("purchaseQuantity") + dataObj.getString("unitCodeName"));
            row.createCell(13).setCellValue(dataObj.getString("price"));
            //!StringUtil.isEmpty(dataObj.getString("resOrderType")) && dataObj.getString("resOrderType").equals("10000") &&
            if (!StringUtil.isEmpty(dataObj.getString("purchaseQuantity")) && !StringUtil.isEmpty(dataObj.getString("price"))) { //状态是入库
                //获取采购数量
                double purchaseQuantity = Double.parseDouble(dataObj.getString("purchaseQuantity"));
                //获取采购单价
                double price = Double.parseDouble(dataObj.getString("price"));
                //获取采购总价
                double totalPrice = purchaseQuantity * price;
                row.createCell(14).setCellValue(String.valueOf(totalPrice));
            } else {
                row.createCell(14).setCellValue("--");
            }
            row.createCell(15).setCellValue(dataObj.getString("remark"));
            row.createCell(16).setCellValue(dataObj.getString("stateName"));
            row.createCell(17).setCellValue(dataObj.getString("createTime"));
        }
    }

    private void allocationStorehouse(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("调拨记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("申请单号");
        row.createCell(1).setCellValue("调拨/退还总数量");
        row.createCell(2).setCellValue("申请人ID");
        row.createCell(3).setCellValue("申请人");
        row.createCell(4).setCellValue("状态");
        row.createCell(5).setCellValue("类型");
        row.createCell(6).setCellValue("时间");

        JSONArray allocationStorehouses = this.getAllocationStorehouse(pd, result);
        if (allocationStorehouses == null || allocationStorehouses.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < allocationStorehouses.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = allocationStorehouses.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("applyId"));
            row.createCell(1).setCellValue(dataObj.getString("applyCount"));
            row.createCell(2).setCellValue(dataObj.getString("startUserId"));
            row.createCell(3).setCellValue(dataObj.getString("startUserName"));
            row.createCell(4).setCellValue(dataObj.getString("stateName"));
            row.createCell(5).setCellValue(dataObj.getString("applyTypeName"));
            row.createCell(6).setCellValue(dataObj.getString("createTime"));
        }
    }

    private void allocationStorehouseDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("调拨明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("调拨申请ID");
        row.createCell(1).setCellValue("物品ID");
        row.createCell(2).setCellValue("物品类型");
        row.createCell(3).setCellValue("物品名称");
        row.createCell(4).setCellValue("物品规格");
        row.createCell(5).setCellValue("是否是固定物品");
        row.createCell(6).setCellValue("被调仓库原库存");
        row.createCell(7).setCellValue("调拨数量");
        row.createCell(8).setCellValue("被调仓库");
        row.createCell(9).setCellValue("目标仓库");
        row.createCell(10).setCellValue("申请人ID");
        row.createCell(11).setCellValue("申请人");
        row.createCell(12).setCellValue("调拨说明");
        row.createCell(13).setCellValue("状态");
        row.createCell(14).setCellValue("时间");

        JSONArray allocationStorehouses = this.getAllocationStorehouseDetail(pd, result);
        if (allocationStorehouses == null || allocationStorehouses.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < allocationStorehouses.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = allocationStorehouses.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("applyId"));
            row.createCell(1).setCellValue(dataObj.getString("resId"));
            row.createCell(2).setCellValue(dataObj.getString("parentRstName") + ">" + dataObj.getString("rstName"));
            row.createCell(3).setCellValue(dataObj.getString("resName"));
            row.createCell(4).setCellValue(dataObj.getString("specName"));
            row.createCell(5).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(6).setCellValue(dataObj.getString("originalStock") + dataObj.getString("unitCodeName"));
            row.createCell(7).setCellValue(dataObj.getString("stock") + dataObj.getString("unitCodeName"));
            if (!StringUtil.isEmpty(dataObj.getString("applyType")) && dataObj.getString("applyType").equals("20000")) {  //返还
                row.createCell(8).setCellValue(dataObj.getString("startUserName"));
            } else {
                row.createCell(8).setCellValue(dataObj.getString("shaName"));
            }
            row.createCell(9).setCellValue(dataObj.getString("shzName"));
            row.createCell(10).setCellValue(dataObj.getString("startUserId"));
            row.createCell(11).setCellValue(dataObj.getString("startUserName"));
            row.createCell(12).setCellValue(dataObj.getString("remark"));
            row.createCell(13).setCellValue(dataObj.getString("stateName"));
            row.createCell(14).setCellValue(dataObj.getString("createTime"));
        }
    }

    private void resourceStoreManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("物品信息");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("物品ID");
        row.createCell(1).setCellValue("仓库名称");
        row.createCell(2).setCellValue("物品类型");
        row.createCell(3).setCellValue("物品名称");
        row.createCell(4).setCellValue("物品规格");
        row.createCell(5).setCellValue("物品编码");
        row.createCell(6).setCellValue("是否是固定物品");
        row.createCell(7).setCellValue("采购参考价格");
        row.createCell(8).setCellValue("收费标准");
        row.createCell(9).setCellValue("物品库存");
        row.createCell(10).setCellValue("最小计量");
        row.createCell(11).setCellValue("最小计量总数");
        row.createCell(12).setCellValue("物品均价");
        row.createCell(13).setCellValue("物品总价");
        JSONArray resourceStores = this.getResourceStore(pd, result);
        if (resourceStores == null || resourceStores.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < resourceStores.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = resourceStores.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("resId"));
            row.createCell(1).setCellValue(dataObj.getString("shName"));
//            row.createCell(2).setCellValue(dataObj.getString("rstName"));
            if (StringUtil.isEmpty(dataObj.getString("rstName"))) {
                row.createCell(2).setCellValue(dataObj.getString("parentRstName"));
            } else {
                row.createCell(2).setCellValue(dataObj.getString("parentRstName") + ">" + dataObj.getString("rstName"));
            }
            row.createCell(3).setCellValue(dataObj.getString("resName"));
            row.createCell(4).setCellValue(dataObj.getString("rssName"));
            row.createCell(5).setCellValue(dataObj.getString("resCode"));
            row.createCell(6).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(7).setCellValue(dataObj.getString("price"));
            //获取最低价
            String outLowPrice = dataObj.getString("outLowPrice");
            //获取最高价
            String outHighPrice = dataObj.getString("outHighPrice");
            if (!StringUtil.isEmpty(outLowPrice) && !StringUtil.isEmpty(outHighPrice) && outLowPrice.equals(outHighPrice)) {
                row.createCell(8).setCellValue(outLowPrice);
            } else if (!StringUtil.isEmpty(outLowPrice) && !StringUtil.isEmpty(outHighPrice) && !outLowPrice.equals(outHighPrice)) {
                row.createCell(8).setCellValue(dataObj.getString("outLowPrice") + "-" + dataObj.getString("outHighPrice"));
            } else {
                row.createCell(8).setCellValue("--");
            }
            row.createCell(9).setCellValue(dataObj.getString("stock") + dataObj.getString("unitCodeName"));
            row.createCell(10).setCellValue("1" + dataObj.getString("unitCodeName") + "=" + dataObj.getString("miniUnitStock") + dataObj.getString("miniUnitCodeName"));
            row.createCell(11).setCellValue(dataObj.getString("miniStock") + dataObj.getString("miniUnitCodeName"));
            row.createCell(12).setCellValue(dataObj.getString("averagePrice"));
            if (!StringUtil.isEmpty(dataObj.getString("stock")) && !StringUtil.isEmpty(dataObj.getString("averagePrice"))) {
                //获取物品库存数量
                double stock = Double.parseDouble(dataObj.getString("stock"));
                BigDecimal x1 = new BigDecimal(stock);
                //获取物品均价
                double averagePrice = Double.parseDouble(dataObj.getString("averagePrice"));
                BigDecimal y1 = new BigDecimal(averagePrice);
                //计算物品总价
                BigDecimal price = x1.multiply(y1);
                double totalPrice = price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                row.createCell(13).setCellValue(String.valueOf(totalPrice));
            } else {
                row.createCell(13).setCellValue("0.0");
            }
        }
    }

    private void purchaseApplyManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("采购申请");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("申请单号");
        row.createCell(1).setCellValue("申请人");
        row.createCell(2).setCellValue("使用人");
        row.createCell(3).setCellValue("操作人");
        row.createCell(4).setCellValue("物品");
        row.createCell(5).setCellValue("是否是固定物品");
        row.createCell(6).setCellValue("申请时间");
        row.createCell(7).setCellValue("采购方式");
        row.createCell(8).setCellValue("审批状态");
        JSONArray purchaseApplys = this.getPurchaseApply(pd, result);
        if (purchaseApplys == null || purchaseApplys.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < purchaseApplys.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = purchaseApplys.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("applyOrderId"));
            row.createCell(1).setCellValue(dataObj.getString("userName"));
            row.createCell(2).setCellValue(dataObj.getString("endUserName"));
            row.createCell(3).setCellValue(dataObj.getString("createUserName"));
            row.createCell(4).setCellValue(dataObj.getString("resourceNames"));
            row.createCell(5).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(6).setCellValue(dataObj.getString("createTime"));
            //获取入库方式
            String warehousingWay = dataObj.getString("warehousingWay");
            if (!StringUtil.isEmpty(warehousingWay) && warehousingWay.equals("10000")) {
                row.createCell(7).setCellValue("直接入库");
            } else {
                row.createCell(7).setCellValue("采购申请入库");
            }
            row.createCell(8).setCellValue(dataObj.getString("stateName"));
        }
    }

    private void itemOutManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("物品领用");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("单号");
        row.createCell(1).setCellValue("物品");
        row.createCell(2).setCellValue("是否是固定物品");
        row.createCell(3).setCellValue("申请人");
        row.createCell(4).setCellValue("操作人");
        row.createCell(5).setCellValue("申请时间");
        row.createCell(6).setCellValue("状态");
        row.createCell(7).setCellValue("领用方式");
        JSONArray itemOutManages = this.getItemOutManage(pd, result);
        if (itemOutManages == null || itemOutManages.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < itemOutManages.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = itemOutManages.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("applyOrderId"));
            row.createCell(1).setCellValue(dataObj.getString("resourceNames"));
            row.createCell(2).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(3).setCellValue(dataObj.getString("userName"));
            row.createCell(4).setCellValue(dataObj.getString("createUserName"));
            row.createCell(5).setCellValue(dataObj.getString("createTime"));
            row.createCell(6).setCellValue(dataObj.getString("stateName"));
            //获取出库方式
            String warehousingWay = dataObj.getString("warehousingWay");
            if (!StringUtil.isEmpty(warehousingWay) && warehousingWay.equals("10000")) {
                row.createCell(7).setCellValue("直接出库");
            } else if (!StringUtil.isEmpty(warehousingWay) && warehousingWay.equals("20000")) {
                row.createCell(7).setCellValue("审核出库");
            } else {
                row.createCell(7).setCellValue("--");
            }
        }
    }

    private void allocationUserStorehouseManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("转赠记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("转赠记录编号");
        row.createCell(1).setCellValue("物品资源ID");
        row.createCell(2).setCellValue("物品类型");
        row.createCell(3).setCellValue("物品名称");
        row.createCell(4).setCellValue("物品规格");
        row.createCell(5).setCellValue("是否是固定物品");
        row.createCell(6).setCellValue("转赠对象ID");
        row.createCell(7).setCellValue("转赠对象");
        row.createCell(8).setCellValue("原有库存");
        row.createCell(9).setCellValue("转赠数量");
        row.createCell(10).setCellValue("创建时间");
        row.createCell(11).setCellValue("备注");
        JSONArray allocationUserStorehouses = this.getAllocationUserStorehouseManage(pd, result);
        if (allocationUserStorehouses == null || allocationUserStorehouses.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < allocationUserStorehouses.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = allocationUserStorehouses.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("ausId"));
            row.createCell(1).setCellValue(dataObj.getString("resId"));
            row.createCell(2).setCellValue(dataObj.getString("parentRstName") + ">" + dataObj.getString("rstName"));
            row.createCell(3).setCellValue(dataObj.getString("resName"));
            row.createCell(4).setCellValue(dataObj.getString("specName"));
            row.createCell(5).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(6).setCellValue(dataObj.getString("acceptUserId"));
            row.createCell(7).setCellValue(dataObj.getString("acceptUserName"));
            row.createCell(8).setCellValue(dataObj.getString("stock") + dataObj.getString("unitCodeName"));
            row.createCell(9).setCellValue(dataObj.getString("giveQuantity") + dataObj.getString("miniUnitCodeName"));
            row.createCell(10).setCellValue(dataObj.getString("createTime"));
            row.createCell(11).setCellValue(dataObj.getString("remark"));
        }
    }

    private void inspectionTaskDetails(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("巡检明细");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("任务详情ID");
        row.createCell(1).setCellValue("巡检点名称");
        row.createCell(2).setCellValue("巡检计划名称");
        row.createCell(3).setCellValue("巡检路线名称");
        row.createCell(4).setCellValue("巡检人开始时间");
        row.createCell(5).setCellValue("巡检人结束时间");
        row.createCell(6).setCellValue("巡检点开始时间");
        row.createCell(7).setCellValue("巡检点结束时间");
        row.createCell(8).setCellValue("实际巡检时间");
        row.createCell(9).setCellValue("实际签到状态");
        row.createCell(10).setCellValue("计划巡检人");
        row.createCell(11).setCellValue("实际巡检人");
        row.createCell(12).setCellValue("巡检方式");
        row.createCell(13).setCellValue("任务状态");
        row.createCell(14).setCellValue("巡检点状态");
        row.createCell(15).setCellValue("巡检情况");
        JSONArray inspectionTaskDetails = this.getInspectionTaskDetails(pd, result);
        if (inspectionTaskDetails == null || inspectionTaskDetails.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < inspectionTaskDetails.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = inspectionTaskDetails.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("taskDetailId"));
            row.createCell(1).setCellValue(dataObj.getString("inspectionName"));
            row.createCell(2).setCellValue(dataObj.getString("inspectionPlanName"));
            row.createCell(3).setCellValue(dataObj.getString("routeName"));
            row.createCell(4).setCellValue(dataObj.getString("planInsTime"));
            row.createCell(5).setCellValue(dataObj.getString("planEndTime"));
            if (!StringUtil.isEmpty(dataObj.getString("pointStartTime"))) {
                row.createCell(6).setCellValue(dataObj.getString("pointStartTime"));
            } else {
                row.createCell(6).setCellValue("--");
            }
            if (!StringUtil.isEmpty(dataObj.getString("pointEndTime"))) {
                row.createCell(7).setCellValue(dataObj.getString("pointEndTime"));
            } else {
                row.createCell(7).setCellValue("--");
            }
            if (!StringUtil.isEmpty(dataObj.getString("inspectionTime"))) {
                row.createCell(8).setCellValue(dataObj.getString("inspectionTime"));
            } else {
                row.createCell(8).setCellValue("--");
            }
            row.createCell(9).setCellValue(dataObj.getString("inspectionStateName"));
            row.createCell(10).setCellValue(dataObj.getString("planUserName"));
            if (!StringUtil.isEmpty(dataObj.getString("actUserName"))) {
                row.createCell(11).setCellValue(dataObj.getString("actUserName"));
            } else {
                row.createCell(11).setCellValue("--");
            }
            row.createCell(12).setCellValue(dataObj.getString("signTypeName"));
            row.createCell(13).setCellValue(dataObj.getString("taskStateName"));
            row.createCell(14).setCellValue(dataObj.getString("stateName"));
            if (!StringUtil.isEmpty(dataObj.getString("description"))) {
                row.createCell(15).setCellValue(dataObj.getString("description"));
            } else {
                row.createCell(15).setCellValue("--");
            }
        }
    }

    private void resourceStoreUseRecordManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("物品使用记录");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("物品使用记录编号");
        row.createCell(1).setCellValue("维修工单编号");
        row.createCell(2).setCellValue("物品资源编号");
        row.createCell(3).setCellValue("物品类型");
        row.createCell(4).setCellValue("物品名称");
        row.createCell(5).setCellValue("物品规格");
        row.createCell(6).setCellValue("是否是固定物品");
        row.createCell(7).setCellValue("物品使用类型");
        row.createCell(8).setCellValue("物品使用数量");
        row.createCell(9).setCellValue("物品价格");
        row.createCell(10).setCellValue("使用人ID");
        row.createCell(11).setCellValue("使用人");
        row.createCell(12).setCellValue("创建时间");
        row.createCell(13).setCellValue("备注");
        JSONArray resourceStoreUseRecords = this.getResourceStoreUseRecordManage(pd, result);
        if (resourceStoreUseRecords == null || resourceStoreUseRecords.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < resourceStoreUseRecords.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = resourceStoreUseRecords.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("rsurId"));
            row.createCell(1).setCellValue(dataObj.getString("repairId"));
            row.createCell(2).setCellValue(dataObj.getString("resId"));
            row.createCell(3).setCellValue(dataObj.getString("parentRstName") + ">" + dataObj.getString("rstName"));
            row.createCell(4).setCellValue(dataObj.getString("resourceStoreName"));
            row.createCell(5).setCellValue(dataObj.getString("specName"));
            row.createCell(6).setCellValue(dataObj.getString("isFixedName"));
            row.createCell(7).setCellValue(dataObj.getString("stateName"));
            row.createCell(8).setCellValue(dataObj.getString("quantity") + dataObj.getString("miniUnitCodeName"));
            row.createCell(9).setCellValue(dataObj.getString("unitPrice"));
            row.createCell(10).setCellValue(dataObj.getString("createUserId"));
            row.createCell(11).setCellValue(dataObj.getString("createUserName"));
            row.createCell(12).setCellValue(dataObj.getString("createTime"));
            row.createCell(13).setCellValue(dataObj.getString("remark"));
        }
    }

    private void staffFeeManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("员工收费");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("员工编码");
        row.createCell(1).setCellValue("员工名称");
        row.createCell(2).setCellValue("应收金额");
        row.createCell(3).setCellValue("实收金额");
        JSONArray staffFeeManages = this.getStaffFeeManage(pd, result);
        if (staffFeeManages == null || staffFeeManages.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < staffFeeManages.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = staffFeeManages.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("userId"));
            row.createCell(1).setCellValue(dataObj.getString("userName"));
            row.createCell(2).setCellValue(dataObj.getString("receivableAmount"));
            row.createCell(3).setCellValue(dataObj.getString("receivedAmount"));
        }
    }

    //押金报表导出
    private void reportPayFeeDeposit(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("押金报表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用ID");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("费用类型");
        row.createCell(4).setCellValue("费用项");
        row.createCell(5).setCellValue("费用开始时间");
        row.createCell(6).setCellValue("费用结束时间");
        row.createCell(7).setCellValue("创建时间");
        row.createCell(8).setCellValue("付费对象类型");
        row.createCell(9).setCellValue("付款方ID");
        row.createCell(10).setCellValue("应收金额");
        row.createCell(11).setCellValue("状态");
        row.createCell(12).setCellValue("退费状态");
        JSONArray reportPayFeeDeposits = this.getReportPayFeeDeposit(pd, result);
        if (reportPayFeeDeposits == null || reportPayFeeDeposits.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < reportPayFeeDeposits.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = reportPayFeeDeposits.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("feeId"));
            if (!StringUtil.isEmpty(dataObj.getString("payerObjType")) && dataObj.getString("payerObjType").equals("3333")) { //房屋
                row.createCell(1).setCellValue(dataObj.getString("floorNum") + "-" + dataObj.getString("unitNum") + "-" + dataObj.getString("roomNum"));
            } else {
                row.createCell(1).setCellValue(dataObj.getString("objName"));
            }
            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(4).setCellValue(dataObj.getString("feeName"));
            row.createCell(5).setCellValue(dataObj.getString("startTime"));
            row.createCell(6).setCellValue(dataObj.getString("deadlineTime"));
            row.createCell(7).setCellValue(dataObj.getString("createTime"));
            row.createCell(8).setCellValue(dataObj.getString("payerObjTypeName"));
            row.createCell(9).setCellValue(dataObj.getString("payerObjId"));
            row.createCell(10).setCellValue(dataObj.getString("additionalAmount"));
            row.createCell(11).setCellValue(dataObj.getString("stateName"));
            if (dataObj.getString("state").equals("2009001")) {
                row.createCell(12).setCellValue(dataObj.getString("detailStateName"));
            } else {
                row.createCell(12).setCellValue("未缴费");
            }
        }
    }


    private void reportYearCollection(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用台账");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("联系电话");
        row.createCell(3).setCellValue("面积");
        row.createCell(4).setCellValue("费用名称");
        row.createCell(5).setCellValue("应收金额");

        //查询楼栋信息
        JSONArray rooms = this.getReportYearCollection(pd, result);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONArray reportFeeYearCollectionDetailDtos = rooms.getJSONObject(0).getJSONArray("reportFeeYearCollectionDetailDtos");

        for (int detailIndex = 0; detailIndex < reportFeeYearCollectionDetailDtos.size(); detailIndex++) {
            row.createCell(6 + detailIndex).setCellValue(reportFeeYearCollectionDetailDtos.getJSONObject(detailIndex).getString("collectionYear") + "年");
        }

        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("ownerName"));
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("ownerLink"));
            row.createCell(3).setCellValue(dataObj.getString("builtUpArea"));
            row.createCell(4).setCellValue(dataObj.getString("feeName"));
            row.createCell(5).setCellValue(dataObj.getString("receivableAmount"));

            reportFeeYearCollectionDetailDtos = dataObj.getJSONArray("reportFeeYearCollectionDetailDtos");
            for (int detailIndex = 0; detailIndex < reportFeeYearCollectionDetailDtos.size(); detailIndex++) {
                row.createCell(6 + detailIndex).setCellValue(reportFeeYearCollectionDetailDtos.getJSONObject(detailIndex).getString("receivedAmount"));
            }
        }
    }

    private JSONArray getReportListOweFees(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        reqJson.put("hasOweFee","Y");
        apiUrl = "/reportOweFee/queryReportAllOweFee" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    private JSONArray getReportPayFeeDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryPayFeeDetail" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    private JSONArray getReportRepairDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryRepair" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRepairInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRepairInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRepairInfoResults.containsKey("data")) {
            return null;
        }
        return savedRepairInfoResults.getJSONArray("data");
    }

    private JSONArray getReportPayFeeManage(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 0);
        reqJson.put("row", 10000);
        //获取费用类型
        String payObjType = reqJson.getString("payObjType");
        if (!StringUtil.isEmpty(payObjType) && payObjType.equals("3333")) {
            apiUrl = "api.getPropertyPayFee" + mapToUrlParam(reqJson);
        } else if (!StringUtil.isEmpty(payObjType) && payObjType.equals("6666")) {
            apiUrl = "api.getParkingSpacePayFee" + mapToUrlParam(reqJson);
        } else {
            apiUrl = "api.getListPayFee" + mapToUrlParam(reqJson);
        }
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedPayFeeManageInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedPayFeeManageInfoResults.getJSONArray("payFees").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedPayFeeManageInfoResults.containsKey("payFees")) {
            return null;
        }
        return savedPayFeeManageInfoResults.getJSONArray("payFees");
    }

    private JSONArray getReportQuestionAnswerDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("store-id", result.getStoreId());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportQuestionAnswer/queryUserQuestionAnswerValue" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedQuestionAnswers = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedQuestionAnswers.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedQuestionAnswers.containsKey("data")) {
            return null;
        }
        return savedQuestionAnswers.getJSONArray("data");
    }

    private JSONArray getReportPrePaymentFee(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics.queryPrePayment" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedPrePayments = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedPrePayments.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedPrePayments.containsKey("data")) {
            return null;
        }
        return savedPrePayments.getJSONArray("data");
    }

    private JSONArray getReportDeadlineFee(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics.queryDeadlineFee" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedDeadlineFees = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedDeadlineFees.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedDeadlineFees.containsKey("data")) {
            return null;
        }
        return savedDeadlineFees.getJSONArray("data");
    }

    private JSONArray getReportNoFeeRoom(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryNoFeeRooms" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedNoFeeRooms = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedNoFeeRooms.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedNoFeeRooms.containsKey("data")) {
            return null;
        }
        return savedNoFeeRooms.getJSONArray("data");
    }

    private JSONArray getPurchaseApplyDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        reqJson.put("storeId", result.getStoreId());
        apiUrl = "purchaseApplyDetail.listPurchaseApplyDetails" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedPurchaseApplyDetails = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedPurchaseApplyDetails.getJSONArray("purchaseApplyDetails").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedPurchaseApplyDetails.containsKey("purchaseApplyDetails")) {
            return null;
        }
        return savedPurchaseApplyDetails.getJSONArray("purchaseApplyDetails");
    }

    private JSONArray getAllocationStorehouse(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("userId", pd.getUserId());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "resourceStore.listAllocationStorehouseApplys" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedAllocationStorehouses = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedAllocationStorehouses.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedAllocationStorehouses.containsKey("data")) {
            return null;
        }
        return savedAllocationStorehouses.getJSONArray("data");
    }

    private JSONArray getAllocationStorehouseDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "resourceStore.listAllocationStorehouses" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedAllocationStorehouses = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedAllocationStorehouses.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedAllocationStorehouses.containsKey("data")) {
            return null;
        }
        return savedAllocationStorehouses.getJSONArray("data");
    }

    private JSONArray getResourceStore(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        //获取当前用户id
        String userId = pd.getUserId();
        //获取商户id
        String storeId = result.getStoreId();
        reqJson.put("userId", userId);
        reqJson.put("storeId", storeId);
        apiUrl = "resourceStore.listResourceStores" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedResourceStores = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        String exportNumber = MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER);
        if (!StringUtil.isNumber(exportNumber)) {
            exportNumber = "10000";
        }
        int number = Integer.parseInt(exportNumber);
        if (savedResourceStores.getJSONArray("resourceStores").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedResourceStores.containsKey("resourceStores")) {
            return null;
        }
        return savedResourceStores.getJSONArray("resourceStores");
    }

    private JSONArray getPurchaseApply(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "purchaseApply.listPurchaseApplys" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedPurchaseApplys = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedPurchaseApplys.getJSONArray("purchaseApplys").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedPurchaseApplys.containsKey("purchaseApplys")) {
            return null;
        }
        return savedPurchaseApplys.getJSONArray("purchaseApplys");
    }

    private JSONArray getItemOutManage(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "purchaseApply.listPurchaseApplys" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedItemOutManages = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedItemOutManages.getJSONArray("purchaseApplys").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedItemOutManages.containsKey("purchaseApplys")) {
            return null;
        }
        return savedItemOutManages.getJSONArray("purchaseApplys");
    }

    private JSONArray getAllocationUserStorehouseManage(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "resourceStore.listAllocationUserStorehouses" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedAllocationUserStorehouses = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedAllocationUserStorehouses.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedAllocationUserStorehouses.containsKey("data")) {
            return null;
        }
        return savedAllocationUserStorehouses.getJSONArray("data");
    }

    private JSONArray getInspectionTaskDetails(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "inspectionTaskDetail.listInspectionTaskDetails" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedInspectionTaskDetails = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedInspectionTaskDetails.getJSONArray("inspectionTaskDetails").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedInspectionTaskDetails.containsKey("inspectionTaskDetails")) {
            return null;
        }
        return savedInspectionTaskDetails.getJSONArray("inspectionTaskDetails");
    }

    private JSONArray getResourceStoreUseRecordManage(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "resourceStore.listResourceStoreUseRecords" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedResourceStoreUseRecords = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedResourceStoreUseRecords.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedResourceStoreUseRecords.containsKey("data")) {
            return null;
        }
        return savedResourceStoreUseRecords.getJSONArray("data");
    }

    private JSONArray getStaffFeeManage(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 0);
        reqJson.put("row", 10000);
        apiUrl = "api.getStaffFee" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedStaffFeeManages = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedStaffFeeManages.getJSONArray("staffFees").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedStaffFeeManages.containsKey("staffFees")) {
            return null;
        }
        return savedStaffFeeManages.getJSONArray("staffFees");
    }

    private JSONArray getReportPayFeeDeposit(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryPayFeeDeposit" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedReportPayFeeDeposits = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedReportPayFeeDeposits.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedReportPayFeeDeposits.containsKey("data")) {
            return null;
        }
        return savedReportPayFeeDeposits.getJSONArray("data");
    }

    private JSONArray getReportYearCollection(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10001);
        apiUrl = "/reportFeeYearCollection/queryReportFeeYear" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取导出限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    private void reportOweFeeDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("欠费明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("面积");
        row.createCell(4).setCellValue("费用项");
        row.createCell(5).setCellValue("费用开始时间");
        row.createCell(6).setCellValue("更新时间");
        row.createCell(7).setCellValue("欠费时长（天）");
        row.createCell(8).setCellValue("欠费金额");
        //查询楼栋信息
        JSONArray rooms = this.getReportOweFeeDetail(pd, result);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("objName"));
            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("builtUpArea"));
            row.createCell(4).setCellValue(dataObj.getString("feeName"));
            row.createCell(5).setCellValue(dataObj.getString("feeCreateTime"));
            row.createCell(6).setCellValue(dataObj.getString("updateTime"));
            row.createCell(7).setCellValue(dataObj.getString("oweDay"));
            row.createCell(8).setCellValue(dataObj.getString("oweAmount"));
        }
    }

    private void returnPayFeeManage(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("退费审核表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("退费审核编号");
        row.createCell(1).setCellValue("退款单号");
        row.createCell(2).setCellValue("缴费单号");
        row.createCell(3).setCellValue("费用类型");
        row.createCell(4).setCellValue("付费对象");
        row.createCell(5).setCellValue("付费周期(单位:月)");
        row.createCell(6).setCellValue("应付金额(单位:元)");
        row.createCell(7).setCellValue("实付金额(单位:元)");
        row.createCell(8).setCellValue("申请时间");
        row.createCell(9).setCellValue("退费原因");
        row.createCell(10).setCellValue("审核状态");
        //查询楼栋信息
        JSONArray returnPayFees = this.getReturnPayFeeManage(pd, result);
        if (returnPayFees == null || returnPayFees.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < returnPayFees.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = returnPayFees.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("returnFeeId"));
            row.createCell(2).setCellValue(dataObj.getString("detailId"));
            row.createCell(3).setCellValue(dataObj.getString("feeTypeCdName"));
            row.createCell(4).setCellValue(dataObj.getString("payerObjName"));
            String cycle = dataObj.getString("cycles");
            if (!StringUtil.isEmpty(cycle) && cycle.contains("-")) {
                String[] split = cycle.split("-");
                cycle = split[1];
            }
            row.createCell(5).setCellValue(cycle);
            String receivableAmount = dataObj.getString("receivableAmount");
            if (!StringUtil.isEmpty(receivableAmount) && receivableAmount.contains("-")) {
                String[] split = receivableAmount.split("-");
                receivableAmount = split[1];
            }
            row.createCell(6).setCellValue(receivableAmount);
            String feeAccountDetailDtoList = dataObj.getString("feeAccountDetailDtoList");
            JSONArray feeAccountDetails = JSONArray.parseArray(feeAccountDetailDtoList);
            String str = "";
            if (feeAccountDetails != null && feeAccountDetails.size() > 0) {
                for (int paramIndex = 0; paramIndex < feeAccountDetails.size(); paramIndex++) {
                    JSONObject param = feeAccountDetails.getJSONObject(paramIndex);
                    //获取抵扣类型
                    String state = param.getString("state");
                    if (!StringUtil.isEmpty(state) && !state.equals("1001")) {
                        str = param.getString("stateName") + ":" + param.getString("amount") + ";  " + str;
                    }
                }
            }
            String payFeeDetailDiscountDtoList = dataObj.getString("payFeeDetailDiscountDtoList");
            JSONArray payFeeDetailDiscounts = JSONArray.parseArray(payFeeDetailDiscountDtoList);
            String discount = "";
            if (payFeeDetailDiscounts != null && payFeeDetailDiscounts.size() > 0) {
                for (int index = 0; index < payFeeDetailDiscounts.size(); index++) {
                    JSONObject param = payFeeDetailDiscounts.getJSONObject(index);
                    String discountPrice = param.getString("discountPrice");
                    if (!StringUtil.isEmpty(discountPrice) && discountPrice.contains("-")) {
                        String[] split = discountPrice.split("-");
                        discountPrice = split[1];
                    }
                    discount = param.getString("discountName") + ":" + discountPrice + ";  " + discount;
                }
            }
            String receivedAmount = dataObj.getString("receivedAmount");
            if (!StringUtil.isEmpty(receivedAmount) && receivedAmount.contains("-")) {
                String[] split = receivedAmount.split("-");
                receivedAmount = split[1];
            }
            if(!StringUtil.isEmpty(str) || !StringUtil.isEmpty(discount)) {
                row.createCell(7).setCellValue(receivedAmount + "(" + str + discount + ")");
            } else {
                row.createCell(7).setCellValue(receivedAmount);
            }
            row.createCell(8).setCellValue(dataObj.getString("createTime"));
            row.createCell(9).setCellValue(dataObj.getString("reason"));
            row.createCell(10).setCellValue(dataObj.getString("stateName"));
        }
    }

    private void reportFeeDetail(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("房号");
        row.createCell(2).setCellValue("业主");
        row.createCell(3).setCellValue("面积");
        row.createCell(4).setCellValue("费用项");
        row.createCell(5).setCellValue("费用开始时间");
        row.createCell(6).setCellValue("费用结束时间");
        row.createCell(7).setCellValue("历史欠费(单位:元)");
        row.createCell(8).setCellValue("当月应收(单位:元)");
        row.createCell(9).setCellValue("应收合计(单位:元)");
        row.createCell(10).setCellValue("当月实收(单位:元)");
        row.createCell(11).setCellValue("欠费追回(单位:元)");
        row.createCell(12).setCellValue("预交费用(单位:元)");
        row.createCell(13).setCellValue("实收合计(单位:元)");
        row.createCell(14).setCellValue("欠费金额(单位:元)");
        row.createCell(15).setCellValue("更新时间");
        //查询楼栋信息
        JSONArray rooms = this.getReportFeeDetail(pd, result);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("objNameNum"));
            row.createCell(2).setCellValue(dataObj.getString("ownerName"));
            row.createCell(3).setCellValue(dataObj.getString("builtUpArea"));
            row.createCell(4).setCellValue(dataObj.getString("feeName"));
            row.createCell(5).setCellValue(dataObj.getString("feeCreateTime"));
            row.createCell(6).setCellValue(dataObj.getString("deadlineTime"));
            row.createCell(7).setCellValue(dataObj.getString("hisOweAmount"));
            row.createCell(8).setCellValue(dataObj.getString("curReceivableAmount"));
            BigDecimal hisOweAmount = new BigDecimal(dataObj.getString("hisOweAmount"));
            BigDecimal curReceivableAmount = new BigDecimal(dataObj.getString("curReceivableAmount"));
            row.createCell(9).setCellValue(hisOweAmount.add(curReceivableAmount).toString());
            row.createCell(10).setCellValue(dataObj.getString("curReceivedAmount"));
            row.createCell(11).setCellValue(dataObj.getString("hisOweReceivedAmount"));
            row.createCell(12).setCellValue(dataObj.getString("preReceivedAmount"));
            BigDecimal curReceivedAmount = new BigDecimal(dataObj.getString("curReceivedAmount"));
            BigDecimal hisOweReceivedAmount = new BigDecimal(dataObj.getString("hisOweReceivedAmount"));
            BigDecimal preReceivedAmount = new BigDecimal(dataObj.getString("preReceivedAmount"));
            row.createCell(13).setCellValue(hisOweReceivedAmount.add(preReceivedAmount).add(curReceivedAmount).toString());
            row.createCell(14).setCellValue(hisOweAmount.add(curReceivableAmount).subtract(curReceivedAmount).subtract(hisOweReceivedAmount).toString());
            row.createCell(15).setCellValue(dataObj.getString("updateTime"));
        }
    }

    private JSONArray getReportOweFeeDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryOweFeeDetail" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    private JSONArray getReturnPayFeeManage(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "returnPayFee.listReturnPayFees" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedReturnPayFeeManages = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedReturnPayFeeManages.getJSONArray("returnPayFees").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedReturnPayFeeManages.containsKey("returnPayFees")) {
            return null;
        }
        return savedReturnPayFeeManages.getJSONArray("returnPayFees");
    }

    private JSONArray getReportFeeDetail(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryFeeDetail" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    private void reportFeeBreakdown(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用分项表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("费用编号");
        row.createCell(1).setCellValue("费用类型");
        row.createCell(2).setCellValue("费用项");
        row.createCell(3).setCellValue("历史欠费(单位:元)");
        row.createCell(4).setCellValue("当月应收(单位:元)");
        row.createCell(5).setCellValue("应收合计(单位:元)");
        row.createCell(6).setCellValue("当月实收(单位:元)");
        row.createCell(7).setCellValue("欠费追回(单位:元)");
        row.createCell(8).setCellValue("预交费用(单位:元)");
        row.createCell(9).setCellValue("实收合计(单位:元)");
        row.createCell(10).setCellValue("欠费金额(单位:元)");
        row.createCell(11).setCellValue("更新时间");
        //查询楼栋信息
        JSONArray rooms = this.getReportFeeBreakdown(pd, result);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        BigDecimal oweFeeDec = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(roomIndex + 1);
            row.createCell(1).setCellValue(dataObj.getString("feeTypeCd"));
            row.createCell(2).setCellValue(dataObj.getString("feeName"));
            row.createCell(3).setCellValue(dataObj.getString("hisOweAmount"));
            row.createCell(4).setCellValue(dataObj.getString("curReceivableAmount"));
            BigDecimal hisOweAmount = new BigDecimal(dataObj.getString("hisOweAmount"));
            BigDecimal curReceivableAmount = new BigDecimal(dataObj.getString("curReceivableAmount"));
            row.createCell(5).setCellValue(hisOweAmount.add(curReceivableAmount).toString());
            row.createCell(6).setCellValue(dataObj.getString("curReceivedAmount"));
            row.createCell(7).setCellValue(dataObj.getString("hisOweReceivedAmount"));
            row.createCell(8).setCellValue(dataObj.getString("preReceivedAmount"));
            BigDecimal curReceivedAmount = new BigDecimal(dataObj.getString("curReceivedAmount"));
            BigDecimal hisOweReceivedAmount = new BigDecimal(dataObj.getString("hisOweReceivedAmount"));
            BigDecimal preReceivedAmount = new BigDecimal(dataObj.getString("preReceivedAmount"));
            row.createCell(9).setCellValue(hisOweReceivedAmount.add(preReceivedAmount).add(curReceivedAmount).toString());
            oweFeeDec = new BigDecimal(Double.parseDouble(dataObj.getString("hisOweAmount")))
                    .add(new BigDecimal(Double.parseDouble(dataObj.getString("curReceivableAmount"))))
                    .subtract(new BigDecimal(Double.parseDouble(dataObj.getString("curReceivedAmount"))))
                    .subtract(new BigDecimal(Double.parseDouble(dataObj.getString("hisOweReceivedAmount")))).setScale(2, BigDecimal.ROUND_HALF_UP);
            row.createCell(10).setCellValue(oweFeeDec.doubleValue() < 0 ? "0" : oweFeeDec.doubleValue() + "");
            row.createCell(11).setCellValue(dataObj.getString("updateTime"));
        }
    }

    private JSONArray getReportFeeBreakdown(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryFeeBreakdown" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    private void reportFloorUnitFeeSummary(IPageData pd, ComponentValidateResult result, Workbook workbook) {
        Sheet sheet = workbook.createSheet("楼栋费用表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("日期");
        row.createCell(1).setCellValue("楼栋");
        row.createCell(2).setCellValue("单元");
        row.createCell(3).setCellValue("历史欠费(单位:元)");
        row.createCell(4).setCellValue("当月应收(单位:元)");
        row.createCell(5).setCellValue("应收合计(单位:元)");
        row.createCell(6).setCellValue("当月实收(单位:元)");
        row.createCell(7).setCellValue("欠费追回(单位:元)");
        row.createCell(8).setCellValue("预交费用(单位:元)");
        row.createCell(9).setCellValue("实收合计(单位:元)");
        row.createCell(10).setCellValue("欠费金额(单位:元)");
        row.createCell(11).setCellValue("更新时间");
        //查询楼栋信息
        JSONArray rooms = this.getReportFloorUnitFeeSummary(pd, result);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        BigDecimal oweFeeDec = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("feeYear") + "年" + dataObj.getString("feeMonth") + "月");
            row.createCell(1).setCellValue(dataObj.getString("floorNum") + "栋");
            row.createCell(2).setCellValue(dataObj.getString("unitNum") + "单元");
            row.createCell(3).setCellValue(dataObj.getString("hisOweAmount"));
            row.createCell(4).setCellValue(dataObj.getString("curReceivableAmount"));
            BigDecimal hisOweAmount = new BigDecimal(dataObj.getString("hisOweAmount"));
            BigDecimal curReceivableAmount = new BigDecimal(dataObj.getString("curReceivableAmount"));
            row.createCell(5).setCellValue(hisOweAmount.add(curReceivableAmount).toString());
            row.createCell(6).setCellValue(dataObj.getString("curReceivedAmount"));
            row.createCell(7).setCellValue(dataObj.getString("hisOweReceivedAmount"));
            row.createCell(8).setCellValue(dataObj.getString("preReceivedAmount"));
            BigDecimal hisOweReceivedAmount = new BigDecimal(dataObj.getString("hisOweReceivedAmount"));
            BigDecimal preReceivedAmount = new BigDecimal(dataObj.getString("preReceivedAmount"));
            BigDecimal curReceivedAmount = new BigDecimal(dataObj.getString("curReceivedAmount"));
            row.createCell(9).setCellValue(hisOweReceivedAmount.add(preReceivedAmount).add(curReceivedAmount).toString());
            oweFeeDec = new BigDecimal(Double.parseDouble(dataObj.getString("hisOweAmount")))
                    .add(new BigDecimal(Double.parseDouble(dataObj.getString("curReceivableAmount"))))
                    .subtract(new BigDecimal(Double.parseDouble(dataObj.getString("curReceivedAmount"))))
                    .subtract(new BigDecimal(Double.parseDouble(dataObj.getString("hisOweReceivedAmount")))).setScale(2, BigDecimal.ROUND_HALF_UP);
            row.createCell(10).setCellValue(oweFeeDec.doubleValue() < 0 ? "0" : oweFeeDec.doubleValue() + "");
            row.createCell(11).setCellValue(dataObj.getString("updateTime"));
        }
    }

    private JSONArray getReportFloorUnitFeeSummary(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics/queryFloorUnitFeeSummary" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    /**
     * 查询存在的房屋信息
     * room.queryRooms
     *
     * @param pd
     * @param result
     * @return
     */
    private JSONArray getReportFeeSummaryFee(IPageData pd, ComponentValidateResult result) {
        String apiUrl = "";
        ResponseEntity<String> responseEntity = null;
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        reqJson.put("page", 1);
        reqJson.put("row", 10000);
        apiUrl = "/reportFeeMonthStatistics.queryReportFeeSummary" + mapToUrlParam(reqJson);
        responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);
        if (responseEntity.getStatusCode() != HttpStatus.OK) { //跳过 保存单元信息
            return null;
        }
        JSONObject savedRoomInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        //获取限制条数的值
        int number = Integer.parseInt(MappingCache.getValue(DOMAIN_COMMON, EXPORT_NUMBER));
        if (savedRoomInfoResults.getJSONArray("data").size() > number) {
            throw new IllegalArgumentException("导出数据超过限制条数" + number + "条，无法继续导出操作！");
        }
        if (!savedRoomInfoResults.containsKey("data")) {
            return null;
        }
        return savedRoomInfoResults.getJSONArray("data");
    }

    /**
     * 获取 房屋信息
     *
     * @param componentValidateResult
     * @param workbook
     */
    private void reportFeeSummary(IPageData pd, ComponentValidateResult componentValidateResult, Workbook workbook) {
        Sheet sheet = workbook.createSheet("费用汇总表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("日期");
        row.createCell(1).setCellValue("历史欠费(单位:元)");
        row.createCell(2).setCellValue("当月应收(单位:元)");
        row.createCell(3).setCellValue("应收合计(单位:元)");
        row.createCell(4).setCellValue("当月实收(单位:元)");
        row.createCell(5).setCellValue("欠费追回(单位:元)");
        row.createCell(6).setCellValue("预交费用(单位:元)");
        row.createCell(7).setCellValue("实收合计(单位:元)");
        row.createCell(8).setCellValue("欠费金额(单位:元)");
        row.createCell(9).setCellValue("收费率");
        row.createCell(10).setCellValue("更新时间");
        //查询楼栋信息
        JSONArray rooms = this.getReportFeeSummaryFee(pd, componentValidateResult);
        if (rooms == null || rooms.size() == 0) {
            return;
        }
        JSONObject dataObj = null;
        BigDecimal oweFeeDec = null;
        for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = rooms.getJSONObject(roomIndex);
            row.createCell(0).setCellValue(dataObj.getString("feeYear") + "年" + dataObj.getString("feeMonth") + "月");
            row.createCell(1).setCellValue(dataObj.getString("hisOweAmount"));
            row.createCell(2).setCellValue(dataObj.getString("curReceivableAmount"));
            BigDecimal hisOweAmount = new BigDecimal(dataObj.getString("hisOweAmount"));
            BigDecimal curReceivableAmount = new BigDecimal(dataObj.getString("curReceivableAmount"));
            row.createCell(3).setCellValue(hisOweAmount.add(curReceivableAmount).toString());
            row.createCell(4).setCellValue(dataObj.getString("curReceivedAmount"));
            row.createCell(5).setCellValue(dataObj.getString("hisOweReceivedAmount"));
            row.createCell(6).setCellValue(dataObj.getString("preReceivedAmount"));
            BigDecimal curReceivedAmount = new BigDecimal(dataObj.getString("curReceivedAmount"));
            BigDecimal hisOweReceivedAmount = new BigDecimal(dataObj.getString("hisOweReceivedAmount"));
            BigDecimal preReceivedAmount = new BigDecimal(dataObj.getString("preReceivedAmount"));
            row.createCell(7).setCellValue(curReceivedAmount.add(hisOweReceivedAmount).add(preReceivedAmount).toString());
            oweFeeDec = new BigDecimal(Double.parseDouble(dataObj.getString("hisOweAmount")))
                    .add(new BigDecimal(Double.parseDouble(dataObj.getString("curReceivableAmount"))))
                    .subtract(new BigDecimal(Double.parseDouble(dataObj.getString("curReceivedAmount"))))
                    .subtract(new BigDecimal(Double.parseDouble(dataObj.getString("hisOweReceivedAmount")))).setScale(2, BigDecimal.ROUND_HALF_UP);
            row.createCell(8).setCellValue(oweFeeDec.doubleValue() < 0 ? "0" : oweFeeDec.doubleValue() + "");
            row.createCell(9).setCellValue(dataObj.getString("chargeRate"));
            row.createCell(10).setCellValue(dataObj.getString("updateTime"));
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
