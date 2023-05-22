package com.java110.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.report.statistics.IFeeStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据业主查询 费用明细
 */
@Java110Cmd(serviceCode = "reportFeeMonthStatistics.queryReportFeeDetailOwner")
public class QueryReportFeeDetailOwnerCmd extends Cmd {

    @Autowired
    private IFeeStatistics feeStatisticsImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
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
        queryStatisticsDto.setConfigId(reqJson.getString("configId"));
        queryStatisticsDto.setObjName(reqJson.getString("objName"));
        queryStatisticsDto.setFeeTypeCd(reqJson.getString("feeTypeCd"));
        queryStatisticsDto.setOwnerName(reqJson.getString("ownerName"));
        queryStatisticsDto.setLink(reqJson.getString("link"));
        queryStatisticsDto.setPage(reqJson.getInteger("page"));
        queryStatisticsDto.setRow(reqJson.getInteger("row"));
        long count = baseDataStatisticsImpl.getOwnerCount(queryStatisticsDto);
        List<OwnerDto> owners = null;
        if (count > 0) {
            owners = baseDataStatisticsImpl.getOwnerInfo(queryStatisticsDto);
        } else {
            owners = new ArrayList<>();
        }

        // todo 计算 业主欠费实收数据
        JSONArray datas = computeOwnerOweReceivedFee(owners, queryStatisticsDto);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) queryStatisticsDto.getRow()), count, datas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    /**
     * 计算业主欠费 实收费用
     *
     * @param owners
     * @return
     */
    private JSONArray computeOwnerOweReceivedFee(List<OwnerDto> owners, QueryStatisticsDto queryStatisticsDto) {
        if (owners == null || owners.size() < 1) {
            return new JSONArray();
        }

        JSONArray datas = new JSONArray();
        JSONObject data = null;

        List<String> ownerIds = new ArrayList<>();
        for (OwnerDto ownerDto : owners) {
            ownerIds.add(ownerDto.getOwnerId());
            data = new JSONObject();
            data.put("ownerName", ownerDto.getName());
            data.put("ownerId", ownerDto.getOwnerId());
            data.put("link", ownerDto.getLink());
            datas.add(data);
        }

        queryStatisticsDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
        List<Map> infos = feeStatisticsImpl.getOwnerFeeSummary(queryStatisticsDto);

        if (infos == null || infos.size() < 1) {
            return datas;
        }

        BigDecimal oweFee = new BigDecimal(0.00);
        BigDecimal receivedFee = new BigDecimal(0.00);
        for (int dataIndex = 0; dataIndex < datas.size(); dataIndex++) {
            data = datas.getJSONObject(dataIndex);
            for (Map info : infos) {
                if (!data.get("ownerId").toString().equals(info.get("ownerId"))) {
                    continue;
                }

                oweFee = oweFee.add(new BigDecimal(info.get("oweFee").toString()));
                receivedFee = oweFee.add(new BigDecimal(info.get("receivedFee").toString()));
                data.put("oweFee" + info.get("feeTypeCd").toString(), info.get("oweFee"));
                data.put("receivedFee" + info.get("feeTypeCd").toString(), info.get("receivedFee"));
                data.put("objName", info.get("objName"));
            }
            data.put("oweFee", oweFee.doubleValue());
            data.put("receivedFee", receivedFee.doubleValue());
            // todo 处理 收费对象重复问题
            delRepeatObjName(data);
        }


        return datas;
    }

    /**
     * 去除 重复的objName
     * @param data
     */
    private void delRepeatObjName(JSONObject data) {

        String objName = data.getString("objName");
        if (StringUtil.isEmpty(objName)) {
            return;
        }

        String[] objNames = objName.split(",");
        List<String> oNames = new ArrayList<>();
        for (String oName : objNames) {
            if (!oNames.contains(oName)) {
                oNames.add(oName);
            }
        }
        objName = "";
        for (String oName : oNames) {
            objName += (oName + ",");
        }
        if (objName.endsWith(",")) {
            objName = objName.substring(0, objName.length() - 1);
        }

        data.put("objName", objName);
    }


}
