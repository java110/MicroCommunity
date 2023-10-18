package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFee.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.payFee.PayFeeBatchPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 房屋批量创建费用
 */

@Java110Cmd(serviceCode = "fee.roomCreatePayFee")
public class RoomCreatePayFeeCmd extends Cmd {


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "startTime", "开始时间不存在");

        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        reqJson.put("configEndTime", feeConfigDtos.get(0).getEndTime());

        if (!reqJson.containsKey("roomIds")) {
            throw new IllegalArgumentException("未包含房屋");
        }

        JSONArray roomIds = reqJson.getJSONArray("roomIds");
        if (roomIds == null || roomIds.isEmpty()) {
            throw new IllegalArgumentException("未包含房屋");
        }
        //todo 不是周期性费用 endTime 必须存在
        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag())) {
            Assert.hasKeyAndValue(reqJson, "endTime", "结束时间不存在");
            Date endTime = null;
            Date configEndTime = null;
            endTime = DateUtil.getDateFromStringB(reqJson.getString("endTime"));
            configEndTime = DateUtil.getDateFromStringA(feeConfigDtos.get(0).getEndTime());
            if (endTime.getTime() > configEndTime.getTime()) {
                throw new IllegalArgumentException("结束时间不能超过费用项时间");
            }
        }

        //todo 动态费用
        if (FeeConfigDto.COMPUTING_FORMULA_DYNAMIC.equals(feeConfigDtos.get(0).getComputingFormula())) {
            Assert.hasKeyAndValue(reqJson, "amount", "未包含金额");
        }

        //todo 递增情况
        if (FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(feeConfigDtos.get(0).getComputingFormula())) {
            Assert.hasKeyAndValue(reqJson, "rateCycle", "未包含递增周期");
            Assert.hasKeyAndValue(reqJson, "rate", "未包含递增率");
            Assert.hasKeyAndValue(reqJson, "rateStartTime", "未包含递增开始时间");
            reqJson.put("configComputingFormula", feeConfigDtos.get(0).getComputingFormula());
        }

        String userId = CmdContextUtils.getUserId(context);
        reqJson.put("userId", userId);

        String storeId = CmdContextUtils.getStoreId(context);
        reqJson.put("storeId", storeId);

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        //todo 生成批次
        generatorBatch(reqJson);


        //todo 交给databus 异步方式处理，同步方式处理很容易超时
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_ROOM_CREATE_PAY_FEE, reqJson));

    }

    /**
     * 生成批次号
     *
     * @param reqJson
     */
    private void generatorBatch(JSONObject reqJson) {
        PayFeeBatchPo payFeeBatchPo = new PayFeeBatchPo();
        payFeeBatchPo.setBatchId(GenerateCodeFactory.getGeneratorId("12"));
        payFeeBatchPo.setCommunityId(reqJson.getString("communityId"));
        payFeeBatchPo.setCreateUserId(reqJson.getString("userId"));
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        payFeeBatchPo.setCreateUserName(userDtos.get(0).getUserName());
        payFeeBatchPo.setState(PayFeeBatchDto.STATE_NORMAL);
        payFeeBatchPo.setMsg("正常");
        int flag = payFeeBatchV1InnerServiceSMOImpl.savePayFeeBatch(payFeeBatchPo);

        if (flag < 1) {
            throw new IllegalArgumentException("生成批次失败");
        }

        reqJson.put("batchId", payFeeBatchPo.getBatchId());
    }
}
