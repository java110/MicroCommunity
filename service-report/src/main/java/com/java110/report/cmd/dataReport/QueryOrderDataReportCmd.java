package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IOrderStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 数据统计工单类统计
 *
 */
@Java110Cmd(serviceCode = "dataReport.queryOrderDataReport")
public class QueryOrderDataReportCmd extends Cmd {

    @Autowired
    private IOrderStatistics orderStatisticsImpl;

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
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        JSONArray datas = new JSONArray();
        JSONObject data = null;
        // todo 查询 投诉单
        double complaintOrderCount = orderStatisticsImpl.getComplaintOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","投诉单");
        data.put("value", complaintOrderCount);
        datas.add(data);

        // todo 查询 投诉单
        double undoComplaintOrderCount = orderStatisticsImpl.getUndoComplaintOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","未完成投诉单");
        data.put("value", undoComplaintOrderCount);
        datas.add(data);

        // todo 查询 投诉单
        double finishComplaintOrderCount = orderStatisticsImpl.getFinishComplaintOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","完成投诉单");
        data.put("value", finishComplaintOrderCount);
        datas.add(data);

        // todo 查询 报修单
        double repairOrderCount = orderStatisticsImpl.getRepairOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","报修单");
        data.put("value", repairOrderCount);
        datas.add(data);

        // todo 查询 报修单
        double undoRepairOrderCount = orderStatisticsImpl.getUndoRepairOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","未完成报修单");
        data.put("value", undoRepairOrderCount);
        datas.add(data);

        // todo 查询 报修单
        double finishRepairOrderCount = orderStatisticsImpl.getFinishRepairOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","完成报修单");
        data.put("value", finishRepairOrderCount);
        datas.add(data);

        // todo 查询 巡检
        double inspectionOrderCount = orderStatisticsImpl.getInspectionOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","巡检");
        data.put("value", inspectionOrderCount);
        datas.add(data);

        // todo 查询 未完成巡检
        double undoInspectionOrderCount = orderStatisticsImpl.getUndoInspectionOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","未完成巡检");
        data.put("value", undoInspectionOrderCount);
        datas.add(data);

        // todo 查询 完成巡检
        double finishInspectionOrderCount = orderStatisticsImpl.getFinishInspectionOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","完成巡检");
        data.put("value", finishInspectionOrderCount);
        datas.add(data);

        // todo 查询 保养
        double maintainanceOrderCount = orderStatisticsImpl.getMaintainanceOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","保养");
        data.put("value", maintainanceOrderCount);
        datas.add(data);

        // todo 查询 未完成 保养
        double undoMaintainanceOrderCount = orderStatisticsImpl.getUndoMaintainanceOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","未完成保养");
        data.put("value", undoMaintainanceOrderCount);
        datas.add(data);

        // todo 查询 已完成 保养
        double finishMaintainanceOrderCount = orderStatisticsImpl.getFinishMaintainanceOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","完成保养");
        data.put("value", finishMaintainanceOrderCount);
        datas.add(data);

        // todo 查询 业主反馈
        double notepadOrderCount = orderStatisticsImpl.getNotepadOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","业主反馈");
        data.put("value", notepadOrderCount);
        datas.add(data);

        // todo 查询 充电订单
        double chargeMachineOrderCount = orderStatisticsImpl.getChargeMachineOrderCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","充电订单");
        data.put("value", chargeMachineOrderCount);
        datas.add(data);




        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
