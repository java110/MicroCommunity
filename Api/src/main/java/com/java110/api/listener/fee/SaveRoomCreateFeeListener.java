package com.java110.api.listener.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.fee.IFeeConfigInnerServiceSMO;
import com.java110.core.smo.room.IRoomInnerServiceSMO;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @ClassName SaveRoomCreateFeeListener
 * @Description TODO
 * @Author wuxw
 * @Date 2020/1/31 15:57
 * @Version 1.0
 * add by wuxw 2020/1/31
 **/
@Java110Listener("saveRoomCreateFeeListener")
public class SaveRoomCreateFeeListener extends AbstractServiceApiListener {
    private static Logger logger = LoggerFactory.getLogger(SaveRoomCreateFeeListener.class);

    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_ROOM_CREATE_FEE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "locationTypeCd", "未包含收费范围");
        Assert.hasKeyAndValue(reqJson, "locationObjId", "未包含收费对象");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含收费项目");
        Assert.hasKeyAndValue(reqJson, "billType", "未包含出账类型");
        Assert.hasKeyAndValue(reqJson, "storeId", "未包含商户ID");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        logger.debug("ServiceDataFlowEvent : {}", event);
        List<RoomDto> roomDtos = null;
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(reqJson.getString("communityId"));
        feeConfigDto.setConfigId(reqJson.getString("configId"));
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "当前费用项ID不存在或存在多条" + reqJson.getString("configId"));
        reqJson.put("feeTypeCd", feeConfigDtos.get(0).getFeeTypeCd());
        reqJson.put("feeFlag", feeConfigDtos.get(0).getFeeFlag());
        //判断收费范围
        RoomDto roomDto = new RoomDto();
        if (reqJson.containsKey("roomState") && "2001".equals(reqJson.getString("roomState"))) {
            roomDto.setState("2001");
        }
        if ("1000".equals(reqJson.getString("locationTypeCd"))) {//小区

            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        } else if ("4000".equals(reqJson.getString("locationTypeCd"))) {//楼栋
            //RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setFloorId(reqJson.getString("locationObjId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        } else if ("2000".equals(reqJson.getString("locationTypeCd"))) {//单元
            //RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setUnitId(reqJson.getString("locationObjId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else if ("3000".equals(reqJson.getString("locationTypeCd"))) {//房屋
            //RoomDto roomDto = new RoomDto();
            roomDto.setCommunityId(reqJson.getString("communityId"));
            roomDto.setRoomId(reqJson.getString("locationObjId"));
            roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        } else {
            throw new IllegalArgumentException("收费范围错误");
        }

        if (roomDtos == null || roomDtos.size() < 1) {
            throw new IllegalArgumentException("未查到需要付费的房屋");
        }

        dealRoomFee(roomDtos, context, reqJson, event);
    }

    private void dealRoomFee(List<RoomDto> roomDtos, DataFlowContext context, JSONObject reqJson, ServiceDataFlowEvent event) {

        AppService service = event.getAppService();


        HttpHeaders header = new HttpHeaders();
        context.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();
        JSONObject paramInObj = null;
        ResponseEntity<String> responseEntity = null;
        int failRooms = 0;
        //添加单元信息
        for (int roomIndex = 0; roomIndex < roomDtos.size(); roomIndex++) {

            businesses.add(addFee(roomDtos.get(roomIndex), reqJson, context));

            if (roomIndex % DEFAULT_ADD_FEE_COUNT == 0 && roomIndex != 0) {
                paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());
                //将 rest header 信息传递到下层服务中去
                super.freshHttpHeader(header, context.getRequestCurrentHeaders());

                responseEntity = this.callService(context, service.getServiceCode(), paramInObj);

                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    failRooms += businesses.size();
                }

                businesses = new JSONArray();
            }
        }
        if (businesses != null && businesses.size() > 0) {

            paramInObj = super.restToCenterProtocol(businesses, context.getRequestCurrentHeaders());
            //将 rest header 信息传递到下层服务中去
            super.freshHttpHeader(header, context.getRequestCurrentHeaders());
            responseEntity = this.callService(context, service.getServiceCode(), paramInObj);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                failRooms += businesses.size();
            }
        }

        JSONObject paramOut = new JSONObject();
        paramOut.put("totalRoom", roomDtos.size());
        paramOut.put("successRoom", roomDtos.size() - failRooms);
        paramOut.put("errorRoom", failRooms);

        responseEntity = new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    private JSONObject addFee(RoomDto roomDto, JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FEE_INFO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ + 1);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", "-1");
        businessUnit.put("configId", paramInJson.getString("configId"));
        businessUnit.put("feeTypeCd", paramInJson.getString("feeTypeCd"));
        businessUnit.put("incomeObjId", paramInJson.getString("storeId"));
        businessUnit.put("amount", "-1.00");
        businessUnit.put("startTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("endTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        businessUnit.put("communityId", paramInJson.getString("communityId"));
        businessUnit.put("payerObjId", roomDto.getRoomId());
        businessUnit.put("feeFlag", paramInJson.getString("feeFlag"));
        businessUnit.put("state", "2008001");
        businessUnit.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessFee", businessUnit);

        return business;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IRoomInnerServiceSMO getRoomInnerServiceSMOImpl() {
        return roomInnerServiceSMOImpl;
    }

    public void setRoomInnerServiceSMOImpl(IRoomInnerServiceSMO roomInnerServiceSMOImpl) {
        this.roomInnerServiceSMOImpl = roomInnerServiceSMOImpl;
    }
}
