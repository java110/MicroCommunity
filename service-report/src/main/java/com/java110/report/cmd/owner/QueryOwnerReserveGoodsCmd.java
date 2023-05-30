package com.java110.report.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.report.IReportOrderStatisticsInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询业主就餐情况
 */
@Java110Cmd(serviceCode = "owner.queryOwnerReserveGoods")
public class QueryOwnerReserveGoodsCmd extends Cmd {

    @Autowired
    private IReportOrderStatisticsInnerServiceSMO reportOrderStatisticsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");
        Assert.hasKeyAndValue(reqJson,"startDate","未包含开始时间");
        Assert.hasKeyAndValue(reqJson,"endDate","未包含结束时间");
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
        int row = reqJson.getInteger("row");
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);

        int total = reportOrderStatisticsInnerServiceSMOImpl.getOwnerReserveGoodsCount(ownerDto);
//        int count = 0;
        List<Map> infos = null;
        if (total > 0) {
            infos = reportOrderStatisticsInnerServiceSMOImpl.getOwnerReserveGoods(ownerDto);
        } else {
            infos = new ArrayList<>();
        }

        freshStartDateAndEndDate(infos,ownerDto);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, infos);
        context.setResponseEntity(responseEntity);

    }

    private void freshStartDateAndEndDate(List<Map> infos,OwnerDto ownerDto) {
        if(infos == null || infos.size() < 1){
            return ;
        }

        for(Map info :infos){
            info.put("startDate",ownerDto.getStartDate());
            info.put("endDate",ownerDto.getEndDate());
        }
    }
}
