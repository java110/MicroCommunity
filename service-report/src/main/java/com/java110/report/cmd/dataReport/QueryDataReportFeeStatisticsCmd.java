package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.intf.community.IFloorV1InnerServiceSMO;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * 查询收费情况分析
 * 根据楼栋查询
 */
@Java110Cmd(serviceCode = "dataReport.queryDataReportFeeStatistics")
public class QueryDataReportFeeStatisticsCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "floorIds", "未包含楼栋信息");

        String startDate = reqJson.getString("startDate");
        String endDate = reqJson.getString("endDate");
        if (!StringUtil.isEmpty(startDate) && !startDate.contains(":")) {
            startDate += " 00:00:00";
            reqJson.put("startDate", startDate);
        }
        if (!StringUtil.isEmpty(endDate) && !endDate.contains(":")) {
            endDate += " 23:59:59";
            reqJson.put("endDate", endDate);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String[] floorIds = reqJson.getString("floorIds").split(",");
        //todo 如果没有包含楼栋信息 直接返回空
        if (floorIds == null || floorIds.length < 1) {
            return;
        }
        JSONArray data = new JSONArray();
        //todo 根据楼栋ID循环查询
        for (String floorId : floorIds) {
            //todo 获取到数据
            doGetData(floorId, data, reqJson);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(data));
    }

    /**
     * 查询数据
     *
     * @param floorId
     * @param datas
     */
    private void doGetData(String floorId, JSONArray datas, JSONObject reqJson) {
        JSONObject data = new JSONObject();
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setFloorId(floorId);
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));

        String monthFastDate = DateUtil.getFormatTimeStringB(DateUtil.getFirstDate(reqJson.getString("startDate")));
        String monthLastDate = DateUtil.getFormatTimeStringB(DateUtil.getNextMonthFirstDate(reqJson.getString("startDate")));
        String startDate = reqJson.getString("startDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDateFromStringB(startDate));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String endDate = DateUtil.getFormatTimeStringB(calendar.getTime());

        // todo 查询楼栋
        FloorDto floorDto = new FloorDto();
        floorDto.setFloorId(floorId);
        floorDto.setCommunityId(reqJson.getString("communityId"));
        List<FloorDto> floorDtos = floorV1InnerServiceSMOImpl.queryFloors(floorDto);
        Assert.listOnlyOne(floorDtos, "楼栋不存在");
        data.put("floorNum", floorDtos.get(0).getFloorNum());

        // todo 查询户数
        long roomCount = baseDataStatisticsImpl.getRoomCount(queryStatisticsDto);
        data.put("roomCount", roomCount);

        // todo 查询空置户数
        long freeCount = baseDataStatisticsImpl.getFreeRoomCount(queryStatisticsDto);
        data.put("freeCount", freeCount);

        // todo 查询 历史欠费
        //这里设置查询月1日 不然历史和总欠费一样有点奇怪
        queryStatisticsDto.setStartDate(monthFastDate);
        double hisMonthOweFee = feeStatisticsImpl.getHisMonthOweFee(queryStatisticsDto);
        data.put("hisMonthOweFee", hisMonthOweFee);

        // todo 查询总欠费
        queryStatisticsDto.setEndDate(monthLastDate);
        double oweFee = feeStatisticsImpl.getOweFee(queryStatisticsDto);
        data.put("oweFee", oweFee);

        // todo 本日已交户数
        queryStatisticsDto.setStartDate(startDate);
        queryStatisticsDto.setEndDate(endDate);
        double todayReceivedRoomCount = feeStatisticsImpl.getReceivedRoomCount(queryStatisticsDto);
        data.put("todayReceivedRoomCount", todayReceivedRoomCount);

        // todo 本日已交金额
        double todayReceivedRoomAmount = feeStatisticsImpl.getReceivedRoomAmount(queryStatisticsDto);
        data.put("todayReceivedRoomAmount", todayReceivedRoomAmount);

        // todo 历史欠费清缴户
        queryStatisticsDto.setStartDate(startDate);
        queryStatisticsDto.setEndDate(endDate);
        queryStatisticsDto.setHisDate(monthFastDate);
        double hisOweReceivedRoomCount = feeStatisticsImpl.getHisOweReceivedRoomCount(queryStatisticsDto);
        data.put("hisOweReceivedRoomCount", hisOweReceivedRoomCount);
        // todo 历史欠费清缴金额
        double hisOweReceivedRoomAmount = feeStatisticsImpl.getHisOweReceivedRoomAmount(queryStatisticsDto);
        data.put("hisOweReceivedRoomAmount", hisOweReceivedRoomAmount);

        // todo 本月已收户
        queryStatisticsDto.setStartDate(monthFastDate);
        queryStatisticsDto.setEndDate(monthLastDate);
        double monthReceivedRoomCount = feeStatisticsImpl.getReceivedRoomCount(queryStatisticsDto);
        data.put("monthReceivedRoomCount", monthReceivedRoomCount);

        // todo 已收金额
        double monthReceivedRoomAmount = feeStatisticsImpl.getReceivedRoomAmount(queryStatisticsDto);
        data.put("monthReceivedRoomAmount", monthReceivedRoomAmount);
        // todo 剩余未收
        double curMonthOweFee = feeStatisticsImpl.getCurMonthOweFee(queryStatisticsDto);
        data.put("curMonthOweFee", curMonthOweFee);

        datas.add(data);


    }


}
