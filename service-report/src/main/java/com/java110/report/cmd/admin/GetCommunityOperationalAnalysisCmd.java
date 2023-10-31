package com.java110.report.cmd.admin;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreDto;
import com.java110.intf.report.IBaseDataStatisticsInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.*;

/**
 * 查询小区运营数据
 */
@Java110Cmd(serviceCode = "admin.getCommunityOperationalAnalysis")
public class GetCommunityOperationalAnalysisCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatisticsInnerServiceSMO baseDataStatisticsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_ADMIN);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统管理员账户操作");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");
        if (StringUtil.isEmpty(startTime)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            startTime = DateUtil.getFormatTimeString(calendar.getTime(), DateUtil.DATE_FORMATE_STRING_B);
        }
        if (StringUtil.isEmpty(endTime)) {
            endTime = DateUtil.getAddDayStringB(new Date(), 1);
        }

        reqJson.put("startTime", startTime);
        reqJson.put("endTime", endTime);

        //todo 查询小区七天内缴费订单数
        JSONObject data = new JSONObject();

        //todo 查询小区缴费订单数
        List<Map> feeDetailData = baseDataStatisticsInnerServiceSMOImpl.getCommunityFeeDetailCountAnalysis(reqJson);
        data.put("feeDetailData", feeDetailData);

        //todo 查询小区报修单订单数
        List<Map> repairData = baseDataStatisticsInnerServiceSMOImpl.getCommunityRepairCountAnalysis(reqJson);
        data.put("repairData", repairData);

        context.setResponseEntity(ResultVo.createResponseEntity(data));

    }
}
