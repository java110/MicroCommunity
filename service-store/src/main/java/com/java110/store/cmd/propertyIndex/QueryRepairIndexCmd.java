package com.java110.store.cmd.propertyIndex;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.repair.RepairDto;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@Java110Cmd(serviceCode = "propertyIndex.queryRepairIndex")
public class QueryRepairIndexCmd extends Cmd {

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject paramOut = new JSONObject();

        // 全部报修
        RepairDto repairDto = new RepairDto();
        repairDto.setCommunityId(reqJson.getString("communityId"));
        int allCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);
        paramOut.put("allCount",allCount);

        // 待派单
        repairDto = new RepairDto();
        repairDto.setCommunityId(reqJson.getString("communityId"));
        repairDto.setState(RepairDto.STATE_WAIT);
        int waitCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);
        paramOut.put("waitCount",waitCount);

        //处理中
        repairDto = new RepairDto();
        repairDto.setCommunityId(reqJson.getString("communityId"));
        repairDto.setStatess(new String[]{RepairDto.STATE_TAKING,RepairDto.STATE_BACK,RepairDto.STATE_TRANSFER,RepairDto.STATE_PAY,RepairDto.STATE_PAY_ERROR});
        int doingCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);
        paramOut.put("doingCount",doingCount);

        //已完成
        repairDto = new RepairDto();
        repairDto.setCommunityId(reqJson.getString("communityId"));
        repairDto.setStatess(new String[]{RepairDto.STATE_APPRAISE,RepairDto.STATE_RETURN_VISIT,RepairDto.STATE_COMPLATE,RepairDto.STATE_UNPROCESSED,RepairDto.STATE_STOP});
        int finishCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);
        paramOut.put("finishCount",finishCount);
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
