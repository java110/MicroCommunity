package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.fee.IFeeBMO;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.RoomDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.entity.center.AppService;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.apache.http.annotation.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SaveContractCreateFeeListener
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/31 15:57
 * @Version 1.0
 * add by wuxw 2020/1/31
 **/
@Java110Listener("saveContractCreateFeeListener")
public class SaveContractCreateFeeListener extends AbstractServiceApiListener {

    private static Logger logger = LoggerFactory.getLogger(SaveContractCreateFeeListener.class);

    @Autowired
    private IFeeBMO feeBMOImpl;

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_CONTRACT_CREATE_FEE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        //Assert.hasKeyAndValue(reqJson, "startTime", "未包含收费其实时间");
        //Assert.hasKeyAndValue(reqJson, "billType", "未包含出账类型");
        //Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户ID");


    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        logger.debug("ServiceDataFlowEvent : {}", event);
        String storeId = context.getRequestCurrentHeaders().get("store-id");
        reqJson.put("storeId",storeId);
        List<ContractDto> contractDtos = null;
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        reqJson.put("configEndTime", feeConfigDtos.get(0).getEndTime());

        if (FeeDto.FEE_FLAG_ONCE.equals(feeConfigDtos.get(0).getFeeFlag()) && reqJson.containsKey("endTime")) {
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

            contractDto.setCommunityId(reqJson.getString("communityId"));
        contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        if (contractDtos == null || contractDtos.size() < 1) {
            throw new IllegalArgumentException("未查到需要付费的房屋");
        }
        dealContractFee(contractDtos, context, reqJson, event);
    }

    private void dealContractFee(List<ContractDto> contractDtos, DataFlowContext context, JSONObject reqJson, ServiceDataFlowEvent event) {
        AppService service = event.getAppService();
        List<String> roomIds = new ArrayList<>();

        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        JSONObject paramInObj = null;
        ResponseEntity<String> responseEntity = null;
        int failRooms = 0;
        //添加单元信息
        int curFailRoomCount = 0;
        for (int roomIndex = 0; roomIndex < contractDtos.size(); roomIndex++) {
            curFailRoomCount++;
            businesses.add(feeBMOImpl.addContractFee(contractDtos.get(roomIndex), reqJson, context));
            if (!StringUtil.isEmpty(contractDtos.get(roomIndex).getObjId())) {
                if (FeeDto.FEE_FLAG_ONCE.equals(reqJson.getString("feeFlag"))) {
                    businesses.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME,
                            reqJson.containsKey("endTime") ? reqJson.getString("endTime") : reqJson.getString("configEndTime")));
                }
                businesses.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_ID, contractDtos.get(roomIndex).getObjId()));
                businesses.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_LINK, contractDtos.get(roomIndex).getbLink()));
                businesses.add(feeBMOImpl.addFeeAttr(reqJson, context, FeeAttrDto.SPEC_CD_OWNER_NAME, contractDtos.get(roomIndex).getPartyB()));
            }
            if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {
                responseEntity = feeBMOImpl.callService(context, service.getServiceCode(), businesses);
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    failRooms += curFailRoomCount;
                } else {
                    curFailRoomCount = 0;
                }
                businesses = new JSONArray();
            }
        }
        if (businesses != null && businesses.size() > 0) {
            responseEntity = feeBMOImpl.callService(context, service.getServiceCode(), businesses);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                failRooms += businesses.size();
            }
        }
        JSONObject paramOut = new JSONObject();
        paramOut.put("totalRoom", contractDtos.size());
        paramOut.put("successRoom", contractDtos.size() - failRooms);
        paramOut.put("errorRoom", failRooms);
        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
