package com.java110.report.cmd.dataReport;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询欠费统计
 */
@Java110Cmd(serviceCode = "dataReport.queryOweStatistics")
public class QueryOweStatisticsCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
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
        QueryStatisticsDto queryStatisticsDto = new QueryStatisticsDto();
        queryStatisticsDto.setCommunityId(reqJson.getString("communityId"));
        queryStatisticsDto.setStartDate(reqJson.getString("startDate"));
        queryStatisticsDto.setEndDate(reqJson.getString("endDate"));
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));

        List<Map> datas = null;
        // todo 按楼栋计算实收情况
        datas = feeStatisticsImpl.getOweFeeByFloor(queryStatisticsDto);

        datas = computeOweReceivedFee(datas);

        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }

    private List<Map> computeOweReceivedFee(List<Map> datas) {
        if (datas == null || datas.size() < 1) {
            return new ArrayList<>();
        }

        List<Map> tmpDatas = new ArrayList<>();
        for (Map data : datas) {
            if (!hasInTmp(tmpDatas, data)) {
                tmpDatas.add(data);
            }
        }

        if (tmpDatas == null || tmpDatas.size() < 1) {
            return new ArrayList<>();
        }

        BigDecimal receivedFee = new BigDecimal(0.00);
        for (Map tmpData : tmpDatas) {
            for (Map data : datas) {
                if (!data.get("floorId").toString().equals(tmpData.get("floorId"))) {
                    continue;
                }

                receivedFee = receivedFee.add(new BigDecimal(data.get("oweFee").toString()));
                tmpData.put("oweFee" + data.get("feeTypeCd").toString(), data.get("oweFee"));
            }
            tmpData.put("oweFee", receivedFee.doubleValue());
        }

        return tmpDatas;
    }

    private boolean hasInTmp(List<Map> tmpDatas, Map data) {
        for (Map tmpData : tmpDatas) {
            if (tmpData.get("floorId").equals(data.get("floorId"))) {
                return true;
            }
        }
        return false;
    }

}
