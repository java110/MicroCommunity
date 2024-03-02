package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.Java110ThreadPoolFactory;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.dto.report.ReportFloorFeeStatisticsDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.report.statistics.IFloorFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼栋费用统计
 */
@Java110Cmd(serviceCode = "reportFeeMonthStatistics.queryReportFloorFeeSummary")
public class QueryReportFloorFeeSummaryCmd extends Cmd {

    @Autowired
    private IFloorFeeStatistics floorFeeStatisticsImpl;

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "startDate", "未包含开始日期");
        Assert.hasKeyAndValue(reqJson, "endDate", "未包含结束日期");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        if (reqJson.containsKey("endDate") && !reqJson.getString("endDate").contains(":")) {
            queryStatisticsDto.setEndDate(reqJson.getString("endDate") + " 23:59:59");
        }
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        if (reqJson.containsKey("configIds")) {
            queryStatisticsDto.setConfigIds(reqJson.getString("configIds").split(","));
        }

        //todo 查询楼栋
        FloorDto floorDto = new FloorDto();
        floorDto.setCommunityId(reqJson.getString("communityId"));
        if(reqJson.containsKey("page")) {
            floorDto.setPage(reqJson.getIntValue("page"));
            floorDto.setRow(reqJson.getIntValue("row"));
        }
        List<FloorDto> floorDtos = floorInnerServiceSMOImpl.queryFloors(floorDto);

        if (ListUtil.isNull(floorDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(new ArrayList<>()));
            return;
        }

        List<Map> datas = new ArrayList<>();
        Java110ThreadPoolFactory java110ThreadPoolFactory = null;
        try {
            java110ThreadPoolFactory = Java110ThreadPoolFactory.getInstance().createThreadPool(5);
            for (FloorDto floorDto1 : floorDtos) {
                queryStatisticsDto.setFloorId(floorDto1.getFloorId());
                QueryStatisticsDto tmpQueryStatisticsDto = BeanConvertUtil.covertBean(queryStatisticsDto,QueryStatisticsDto.class);
                java110ThreadPoolFactory.submit(() -> {
                    //todo 欠费户数
                    List<Map> floorDatas = feeStatisticsImpl.getFloorFeeSummary(tmpQueryStatisticsDto);
                    if (!ListUtil.isNull(floorDatas)) {
                        datas.add(floorDatas.get(0));
                    }
                    return datas;
                });
            }
            java110ThreadPoolFactory.get();
        } finally {
            if (java110ThreadPoolFactory != null) {
                java110ThreadPoolFactory.stop();
            }
        }

        //todo 拼接数据

        //List<Map> datas = feeStatisticsImpl.getFloorFeeSummary(queryStatisticsDto);

        if (ListUtil.isNull(datas)) {
            context.setResponseEntity(ResultVo.createResponseEntity(datas));
            return;
        }
        BigDecimal feeRoomCountDec = null;
        BigDecimal oweRoomCountDec = null;
        BigDecimal feeRoomRate = null;
        BigDecimal curReceivedFee = null;
        BigDecimal curReceivableFee = null;
        for (Map data : datas) {
            //todo 计算 户收费率
            if (Double.parseDouble(data.get("feeRoomCount").toString()) > 0) {
                feeRoomCountDec = new BigDecimal(Double.parseDouble(data.get("feeRoomCount").toString()));
                oweRoomCountDec = new BigDecimal(Double.parseDouble(data.get("oweRoomCount").toString()));
                feeRoomRate = feeRoomCountDec.subtract(oweRoomCountDec).divide(feeRoomCountDec, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                data.put("feeRoomRate", feeRoomRate.doubleValue());
            } else {
                data.put("feeRoomRate", 0.0);
            }

            //todo 计算 收费率
            curReceivedFee = new BigDecimal(Double.parseDouble(data.get("curReceivedFee").toString()));
            curReceivableFee = new BigDecimal(Double.parseDouble(data.get("curReceivableFee").toString()));

            if (curReceivableFee.doubleValue() > 0) {
                feeRoomRate = curReceivedFee.divide(curReceivableFee, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                data.put("feeRate", feeRoomRate.doubleValue());
            } else {
                data.put("feeRate", 0.0);
            }
        }

        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }

    private void computeData(List<Map> datas, List<ReportFloorFeeStatisticsDto> oweRoomCounts,
                             List<ReportFloorFeeStatisticsDto> feeRoomCounts,
                             List<ReportFloorFeeStatisticsDto> receivedFees,
                             List<ReportFloorFeeStatisticsDto> preReceivedFees,
                             List<ReportFloorFeeStatisticsDto> hisOweFees,
                             List<ReportFloorFeeStatisticsDto> curReceivableFees,
                             List<ReportFloorFeeStatisticsDto> curReceivedFees,
                             List<ReportFloorFeeStatisticsDto> hisReceivedFees) {
        //todo 欠费房屋数 oweRoomCount
        for (Map data : datas) {
            data.put("oweRoomCount", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : oweRoomCounts) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("oweRoomCount", reportFloorFeeStatisticsDto.getOweRoomCount());
                }
            }
        }

        //todo 收费房屋数 feeRoomCount
        for (Map data : datas) {
            data.put("feeRoomCount", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : feeRoomCounts) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("feeRoomCount", reportFloorFeeStatisticsDto.getFeeRoomCount());
                }
            }
        }

        //todo 实收金额 receivedFee
        for (Map data : datas) {
            data.put("receivedFee", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : receivedFees) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("receivedFee", reportFloorFeeStatisticsDto.getReceivedFee());
                }
            }
        }

        //todo 预收金额 preReceivedFee
        for (Map data : datas) {
            data.put("preReceivedFee", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : preReceivedFees) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("preReceivedFee", reportFloorFeeStatisticsDto.getPreReceivedFee());
                }
            }
        }


        //todo 历史欠费金额 hisOweFee
        for (Map data : datas) {
            data.put("hisOweFee", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : hisOweFees) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("hisOweFee", reportFloorFeeStatisticsDto.getHisOweFee());
                }
            }
        }

        //todo 当期应收金额 curReceivableFee
        for (Map data : datas) {
            data.put("curReceivableFee", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : curReceivableFees) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("curReceivableFee", reportFloorFeeStatisticsDto.getCurReceivableFee());
                }
            }
        }

        //todo 当期实收金额 curReceivedFee

        for (Map data : datas) {
            data.put("curReceivedFee", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : curReceivedFees) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("curReceivedFee", reportFloorFeeStatisticsDto.getCurReceivedFee());
                }
            }
        }
        //todo 欠费追回 hisReceivedFee
        for (Map data : datas) {
            data.put("hisReceivedFee", "0");
            for (ReportFloorFeeStatisticsDto reportFloorFeeStatisticsDto : hisReceivedFees) {
                if (reportFloorFeeStatisticsDto.getFloorId().equals(data.get("floorId"))) {
                    data.put("hisReceivedFee", reportFloorFeeStatisticsDto.getHisReceivedFee());
                }
            }
        }
    }
}
