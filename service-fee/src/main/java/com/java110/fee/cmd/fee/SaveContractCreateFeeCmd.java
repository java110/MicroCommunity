package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.payFeeBatch.PayFeeBatchDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.bmo.fee.IFeeBMO;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.fee.IPayFeeBatchV1InnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.payFeeBatch.PayFeeBatchPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "fee.saveContractCreateFee")
public class SaveContractCreateFeeCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveContractCreateFeeCmd.class);

    @Autowired
    private IFeeBMO feeBMOImpl;

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IPayFeeBatchV1InnerServiceSMO payFeeBatchV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        //Assert.hasKeyAndValue(reqJson, "startTime", "未包含收费其实时间");
        //Assert.hasKeyAndValue(reqJson, "billType", "未包含出账类型");
        //Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        logger.debug("ServiceDataFlowEvent : {}", event);
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
//        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
//        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
//        reqJson.put("storeId", storeId);
//        reqJson.put("userId", userId);
        List<ContractDto> contractDtos = null;
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        reqJson.put("configEndTime", feeConfigDtos.get(0).getEndTime());

        if (!FeeDto.FEE_FLAG_CYCLE.equals(feeConfigDtos.get(0).getFeeFlag()) && reqJson.containsKey("endTime")) {
            Date endTime = null;
            Date configEndTime = null;
            try {
                endTime = DateUtil.getDateFromString(reqJson.getString("endTime"), DateUtil.DATE_FORMATE_STRING_B);
                configEndTime = DateUtil.getDateFromString(feeConfigDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
                if (endTime.getTime() > configEndTime.getTime()) {
                    throw new IllegalArgumentException("结束时间不能超过费用项时间");
                }
            } catch (ParseException e) {
                throw new IllegalArgumentException("结束时间错误" + reqJson.getString("endTime"));
            }
        }
        //生成批次号
        generatorBatch(reqJson);
        //判断收费范围
        ContractDto contractDto = new ContractDto();
        /*if (reqJson.containsKey("roomState") && RoomDto.STATE_SELL.equals(reqJson.getString("roomState"))) {
            roomDto.setState(RoomDto.STATE_SELL);
        }*/
        if (reqJson.containsKey("contractState") && reqJson.getString("contractState").contains(",")) {
            String states = reqJson.getString("contractState");
            contractDto.setStates(states.split(","));
        }

        contractDto.setContractId(reqJson.getString("contractId"));
        contractDto.setStoreId(storeId);
        //contractDto.setCommunityId(reqJson.getString("communityId"));
        contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        if (contractDtos == null || contractDtos.size() < 1) {
            throw new IllegalArgumentException("未查到需要付费的房屋");
        }
        dealContractFee(contractDtos, cmdDataFlowContext, reqJson, event);
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

    private void dealContractFee(List<ContractDto> contractDtos, ICmdDataFlowContext context, JSONObject reqJson, CmdEvent event) {


        List<PayFeePo> feePos = new ArrayList<>();
        List<FeeAttrPo> feeAttrsPos = new ArrayList<>();
        JSONObject paramInObj = null;
        ResponseEntity<String> responseEntity = null;
        int failRooms = 0;
        //添加单元信息
        int curFailRoomCount = 0;
        int saveFlag = 0;
        for (int roomIndex = 0; roomIndex < contractDtos.size(); roomIndex++) {
            curFailRoomCount++;
            feePos.add(BeanConvertUtil.covertBean(feeBMOImpl.addContractFee(contractDtos.get(roomIndex), reqJson, context), PayFeePo.class));
            if (!StringUtil.isEmpty(contractDtos.get(roomIndex).getObjId())) {
                if (!FeeDto.FEE_FLAG_CYCLE.equals(reqJson.getString("feeFlag"))) {
                    feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                            reqJson.containsKey("endTime") ? reqJson.getString("endTime") : reqJson.getString("configEndTime")));
                }
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_ID, contractDtos.get(roomIndex).getObjId()));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_LINK, contractDtos.get(roomIndex).getbLink()));
                feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_NAME, contractDtos.get(roomIndex).getPartyB()));
            }

            //付费对象名称
            feeAttrsPos.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME,
                    contractDtos.get(roomIndex).getContractName()));

            if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {
                saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
                feePos = new ArrayList<>();
                feeAttrsPos = new ArrayList<>();
                if (saveFlag < 1) {
                    failRooms += curFailRoomCount;
                } else {
                    curFailRoomCount = 0;
                }
            }
        }
        if (feePos != null && feePos.size() > 0) {
            saveFlag = saveFeeAndAttrs(feePos, feeAttrsPos);
            if (saveFlag < 1) {
                failRooms += curFailRoomCount;
            }
        }
        JSONObject paramOut = new JSONObject();
        paramOut.put("totalRoom", contractDtos.size());
        paramOut.put("successRoom", contractDtos.size() - failRooms);
        paramOut.put("errorRoom", failRooms);

        context.setResponseEntity( ResultVo.createResponseEntity(paramOut));
    }


    private int saveFeeAndAttrs(List<PayFeePo> feePos, List<FeeAttrPo> feeAttrsPos) {
        int flag = feeInnerServiceSMOImpl.saveFee(feePos);
        if (flag < 1) {
            return flag;
        }

        flag = feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrsPos);

        return flag;
    }
}
