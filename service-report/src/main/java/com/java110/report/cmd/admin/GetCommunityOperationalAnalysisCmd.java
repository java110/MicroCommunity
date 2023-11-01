package com.java110.report.cmd.admin;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreDto;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.*;

/**
 * 查询小区运营数据
 */
@Java110Cmd(serviceCode = "admin.getCommunityOperationalAnalysis")
public class GetCommunityOperationalAnalysisCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_ADMIN);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统管理员账户操作");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");
        String storeId = CmdContextUtils.getStoreId(context);
        if (StringUtil.isEmpty(startTime)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            startTime = DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_B);
        }
        if (StringUtil.isEmpty(endTime)) {
            endTime = DateUtil.getAddDayStringB(new Date(), 1);
        }

        reqJson.put("startTime", startTime);
        reqJson.put("endTime", endTime);

        //todo 查询小区七天内缴费订单数
        JSONObject data = new JSONObject();

        //todo 查询小区缴费订单数
        List<Map> feeDetailData = baseDataStatisticsInnerServiceSMOImpl.getCommunityFeeDetailCountAnalysis(reqJson);
        feeDetailData = fillDate(feeDetailData, startTime, endTime);
        data.put("feeDetailData", feeDetailData);

        //todo 查询小区报修单订单数
        List<Map> repairData = baseDataStatisticsInnerServiceSMOImpl.getCommunityRepairCountAnalysis(reqJson);
        repairData = fillDate(repairData, startTime, endTime);
        data.put("repairData", repairData);

        //todo 查询巡检数据数
        List<Map> inspectionData = baseDataStatisticsInnerServiceSMOImpl.getCommunityInspectionAnalysis(reqJson);
        inspectionData = fillDate(inspectionData, startTime, endTime);
        data.put("inspectionData", inspectionData);

        //todo 查询保养数据数
        List<Map> maintainanceData = baseDataStatisticsInnerServiceSMOImpl.getCommunityMaintainanceAnalysis(reqJson);
        maintainanceData = fillDate(maintainanceData, startTime, endTime);
        data.put("maintainanceData", maintainanceData);

        //todo 查询采购订单数
        List<Map> itemInData = baseDataStatisticsInnerServiceSMOImpl.getCommunityItemInAnalysis(reqJson);
        itemInData = fillDate(itemInData, startTime, endTime);
        data.put("itemInData", itemInData);

        //todo 查询领用订单数
        List<Map> itemOutData = baseDataStatisticsInnerServiceSMOImpl.getCommunityItemOutAnalysis(reqJson);
        itemOutData = fillDate(itemOutData, startTime, endTime);
        data.put("itemOutData", itemOutData);

        //todo 查询车辆进场数
        List<Map> carInData = baseDataStatisticsInnerServiceSMOImpl.getCommunityCarInAnalysis(reqJson);
        carInData = fillDate(carInData, startTime, endTime);
        data.put("carInData", carInData);

        //todo 查询人员进场数
        List<Map> personInData = baseDataStatisticsInnerServiceSMOImpl.getCommunityPersonInAnalysis(reqJson);
        personInData = fillDate(personInData, startTime, endTime);
        data.put("personInData", personInData);

        //todo 查询起草合同数
        reqJson.put("storeId",storeId);
        List<Map> contractData = baseDataStatisticsInnerServiceSMOImpl.getCommunityContractAnalysis(reqJson);
        contractData = fillDate(contractData, startTime, endTime);
        data.put("contractData", contractData);
        context.setResponseEntity(ResultVo.createResponseEntity(data));

    }

    private List<Map> fillDate(List<Map> datas, String startTime, String endTime) {
        Date sTime = DateUtil.getDateFromStringB(startTime);
        Date eTime = DateUtil.getDateFromStringB(endTime);
        List<Map> tempDatas = new ArrayList<>();
        while (sTime.getTime() <= eTime.getTime()) {

            Map data = hasInDatas(sTime, datas);
            if (data == null) {
                data = new HashMap();
                data.put("createTime", DateUtil.getFormatTimeStringB(sTime));
                data.put("countValue", "0");
            }

            tempDatas.add(data);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sTime);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            sTime = calendar.getTime();
        }

        return tempDatas;
    }

    private Map hasInDatas(Date sTime, List<Map> datas) {

        String curTime = DateUtil.getFormatTimeStringB(sTime);
        for (Map data : datas) {
            if (curTime.equals(data.get("createTime"))) {
                return data;
            }
        }

        return null;
    }
}
