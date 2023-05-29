package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IInoutStatistics;
import com.java110.report.statistics.IOthersStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 数据统计出入统计
 *
 */
@Java110Cmd(serviceCode = "dataReport.queryOthersDataReport")
public class QueryOthersDataReportCmd extends Cmd {

    @Autowired
    private IOthersStatistics othersStatisticsImpl;

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
        // todo 查询 查询场地预约数
        long venueReservationCount = othersStatisticsImpl.venueReservationCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","场地预约数");
        data.put("value", venueReservationCount);
        datas.add(data);

        // todo 查询 合同数
        long contractCount = othersStatisticsImpl.contractCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","合同数");
        data.put("value", contractCount);
        datas.add(data);

        // todo 查询 合同资产变更
        long contractChangeCount = othersStatisticsImpl.contractChangeCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","合同资产变更");
        data.put("value", contractChangeCount);
        datas.add(data);


        // todo 查询 租期变更
        long leaseChangeCount = othersStatisticsImpl.leaseChangeCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","租期变更");
        data.put("value", leaseChangeCount);
        datas.add(data);

        // todo 查询 主体变更
        long mainChange = othersStatisticsImpl.mainChange(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","主体变更");
        data.put("value", mainChange);
        datas.add(data);

        // todo 查询 到期合同
        long expirationContract = othersStatisticsImpl.expirationContract(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","到期合同");
        data.put("value", expirationContract);
        datas.add(data);

        // todo 查询 车辆数
        long carCount = othersStatisticsImpl.carCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","车辆数");
        data.put("value", carCount);
        datas.add(data);

        // todo 查询 车位申请
        long carApplyCount = othersStatisticsImpl.carApplyCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","车位申请");
        data.put("value", carApplyCount);
        datas.add(data);

        // todo 查询 停车券购买
        double buyParkingCouponCount = othersStatisticsImpl.buyParkingCouponCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","停车券购买");
        data.put("value", buyParkingCouponCount);
        datas.add(data);

        // todo 查询 停车券核销
        long writeOffParkingCouponCount = othersStatisticsImpl.writeOffParkingCouponCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","停车券核销");
        data.put("value", writeOffParkingCouponCount);
        datas.add(data);

        // todo 查询 赠送优惠券
        double sendCouponCount = othersStatisticsImpl.sendCouponCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","赠送优惠券");
        data.put("value", sendCouponCount);
        datas.add(data);

        // todo 查询 使用优惠券
        long writeOffCouponCount = othersStatisticsImpl.writeOffCouponCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","使用优惠券");
        data.put("value", writeOffCouponCount);
        datas.add(data);

        // todo 查询 赠送积分
        double sendIntegralCount = othersStatisticsImpl.sendIntegralCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","退房数量");
        data.put("value", sendIntegralCount);
        datas.add(data);

        // todo 查询 使用积分
        double writeOffIntegralCount = othersStatisticsImpl.writeOffIntegralCount(queryStatisticsDto);
        data = new JSONObject();
        data.put("name","使用积分");
        data.put("value", writeOffIntegralCount);
        datas.add(data);


        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
