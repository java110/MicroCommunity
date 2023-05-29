package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IInoutStatistics;
import com.java110.report.statistics.IOrderStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 数据统计出入统计
 *
 */
@Java110Cmd(serviceCode = "dataReport.queryInoutDataReport")
public class QueryInoutDataReportCmd extends Cmd {

    @Autowired
    private IInoutStatistics inoutStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束时间");
        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if (!startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("startDate", startDate);
        }
        if (!endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("endDate", endDate);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStoreId(storeId);
        JSONArray datas = new JSONArray();
        JSONObject data = null;
        // todo 查询 进场车辆数
        double carInCount = inoutStatisticsImpl.getCarInCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","进场车辆数");
        data.put("value", carInCount);
        datas.add(data);

        // todo 查询 出场车辆数
        double carOutCount = inoutStatisticsImpl.getCarOutCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","出场车辆数");
        data.put("value", carOutCount);
        datas.add(data);

        // todo 查询 进场人员数
        double personInCount = inoutStatisticsImpl.getPersonInCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","进场人员数");
        data.put("value", personInCount);
        datas.add(data);


        // todo 查询 人脸同步数
        double personFaceToMachine = inoutStatisticsImpl.getPersonFaceToMachineCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","人脸同步数");
        data.put("value", personFaceToMachine);
        datas.add(data);

        // todo 查询 采购入库数
        double purchaseInCount = inoutStatisticsImpl.purchaseInCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","采购入库数");
        data.put("value", purchaseInCount);
        datas.add(data);

        // todo 查询 领用出库数
        double purchaseOutCount = inoutStatisticsImpl.purchaseOutCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","领用出库数");
        data.put("value", purchaseOutCount);
        datas.add(data);

        // todo 查询 采购入库金额
        double purchaseInAmount = inoutStatisticsImpl.purchaseInAmount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","采购入库金额");
        data.put("value", purchaseInAmount);
        datas.add(data);

        // todo 查询 领用出库金额
        double purchaseOutAmount = inoutStatisticsImpl.purchaseOutAmount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","完成巡检");
        data.put("value", purchaseOutAmount);
        datas.add(data);

        // todo 查询 调拨数量
        double allocationCount = inoutStatisticsImpl.allocationCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","调拨数量");
        data.put("value", allocationCount);
        datas.add(data);

        // todo 查询 房屋装修数
        double roomRenovationCount = inoutStatisticsImpl.roomRenovationCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","房屋装修数");
        data.put("value", roomRenovationCount);
        datas.add(data);

        // todo 查询 物品放行
        double itemReleaseCount = inoutStatisticsImpl.itemReleaseCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","物品放行");
        data.put("value", itemReleaseCount);
        datas.add(data);

        // todo 查询 交房数量
        double roomInCount = inoutStatisticsImpl.roomInCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","交房数量");
        data.put("value", roomInCount);
        datas.add(data);

        // todo 查询 退房数量
        double roomOutCount = inoutStatisticsImpl.roomOutCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","退房数量");
        data.put("value", roomOutCount);
        datas.add(data);

        // todo 查询 业主绑定
        double ownerRegisterCount = inoutStatisticsImpl.ownerRegisterCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","业主绑定");
        data.put("value", ownerRegisterCount);
        datas.add(data);

        // todo 查询 未考勤数
        double noAttendanceCount = inoutStatisticsImpl.noAttendanceCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","未考勤数");
        data.put("value", noAttendanceCount);
        datas.add(data);

        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
