package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.Java110ThreadPoolFactory;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.MoneyUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 查询费用汇总表
 * <p>
 * add by  wuxw
 */
@Java110Cmd(serviceCode = "reportFeeMonthStatistics.queryReportFeeSummary")
public class QueryReportFeeSummaryCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    /**
     * 校验查询条件
     * <p>
     * 开始时间
     * 结束时间
     * 房屋
     * 业主
     * 楼栋
     * 费用项
     *
     * @param event   事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     */
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        /*queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));*/
        if (reqJson.containsKey("startDate") && !reqJson.getString("startDate").contains(":")) {
            queryStatisticsDto.setStartDate(reqJson.getString("startDate") + " 00:00:00");
        }
        if (reqJson.containsKey("endDate") && !reqJson.getString("endDate").contains(":")) {
            queryStatisticsDto.setEndDate(reqJson.getString("endDate") + " 23:59:59");
        }
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setFloorId(reqJson.getString("floorId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        if (reqJson.containsKey("configIds")) {
            queryStatisticsDto.setConfigIds(reqJson.getString("configIds").split(","));
        }


        JSONObject data = new JSONObject();

        Java110ThreadPoolFactory java110ThreadPoolFactory = null;
        try {
            java110ThreadPoolFactory = Java110ThreadPoolFactory.getInstance().createThreadPool(5);
            java110ThreadPoolFactory.submit(() -> {
                //todo 查询历史欠费
                double hisOweFee = feeStatisticsImpl.getHisMonthOweFee(queryStatisticsDto);
                data.put("hisOweFee", MoneyUtil.computePriceScale(hisOweFee));
                return hisOweFee;
            });

            java110ThreadPoolFactory.submit(() -> {
                //todo 查询 单月欠费
                double curOweFee = feeStatisticsImpl.getCurMonthOweFee(queryStatisticsDto);
                data.put("curOweFee", MoneyUtil.computePriceScale(curOweFee));
                return curOweFee;
            });

            java110ThreadPoolFactory.submit(() -> {
                //todo 查询当月应收
                double curReceivableFee = feeStatisticsImpl.getCurReceivableFee(queryStatisticsDto);
                data.put("curReceivableFee", MoneyUtil.computePriceScale(curReceivableFee));
                return curReceivableFee;
            });

            java110ThreadPoolFactory.submit(() -> {
                //todo 查询 欠费追回
                double hisReceivedFee = feeStatisticsImpl.getHisReceivedFee(queryStatisticsDto);
                data.put("hisReceivedFee", MoneyUtil.computePriceScale(hisReceivedFee));
                return hisReceivedFee;
            });

            java110ThreadPoolFactory.submit(() -> {
                //todo  查询 预交费用
                double preReceivedFee = feeStatisticsImpl.getPreReceivedFee(queryStatisticsDto);
                data.put("preReceivedFee", MoneyUtil.computePriceScale(preReceivedFee));
                return preReceivedFee;
            });

            java110ThreadPoolFactory.submit(() -> {
                //todo 查询实收
                double receivedFee = feeStatisticsImpl.getReceivedFee(queryStatisticsDto);
                data.put("receivedFee", MoneyUtil.computePriceScale(receivedFee));
                return receivedFee;
            });

            java110ThreadPoolFactory.submit(() -> {
                //todo 房屋数
                long roomCount = baseDataStatisticsImpl.getRoomCount(queryStatisticsDto);
                data.put("roomCount", roomCount);
                return roomCount;
            });
            java110ThreadPoolFactory.submit(() -> {
                //todo 收费房屋数
                long feeRoomCount = feeStatisticsImpl.getFeeRoomCount(queryStatisticsDto);
                data.put("feeRoomCount", feeRoomCount);
                return feeRoomCount;
            });
            java110ThreadPoolFactory.submit(() -> {
                //todo 欠费户数
                int oweRoomCount = feeStatisticsImpl.getOweRoomCount(queryStatisticsDto);
                data.put("oweRoomCount", oweRoomCount);
                return oweRoomCount;
            });

            java110ThreadPoolFactory.get();
        } finally {
            if (java110ThreadPoolFactory != null) {
                java110ThreadPoolFactory.stop();
            }
        }
        JSONArray datas = new JSONArray();
        datas.add(data);
        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
