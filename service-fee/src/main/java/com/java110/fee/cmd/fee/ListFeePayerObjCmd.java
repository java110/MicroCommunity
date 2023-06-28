package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.fee.PayFeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.room.RoomDto;
import com.java110.intf.community.IRoomV1InnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IBuildingOwnerV1InnerServiceSMO;
import com.java110.intf.user.IOwnerCarV1InnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询费用 对象信息
 */

@Java110CmdDoc(title = "查询费用作用对象",
        description = "供三方系统查询的费用对象 比如 房屋 车辆 合同等",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/fee.listFeePayerObj",
        resource = "feeDoc",
        author = "吴学文",
        serviceCode = "fee.listFeePayerObj",
        seq = 1
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "payerObjId", length = 30, remark = "对象ID"),
        @Java110ParamDoc(name = "payerObjType", length = 12, remark = "对象类型"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", length = -1, defaultValue = "成功", remark = "数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "payerObjId", length = 30, remark = "费用对象ID"),
                @Java110ParamDoc(parentNodeName = "data", name = "payerObjName", length = 64, remark = "费用对象名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "paNum", length = 11, remark = "停车场"),
                @Java110ParamDoc(parentNodeName = "data", name = "psNum", length = 30, remark = "车位"),
                @Java110ParamDoc(parentNodeName = "data", name = "ownerId", length = 30, remark = "业主Id"),
                @Java110ParamDoc(parentNodeName = "data", name = "ownerName", length = 64, remark = "业主名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "tel", length = 11, remark = "手机号"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://ip:port/app/fee.listFeePayerObj?payerObjId=1001&payerObjType=3333&communityId=2022121921870161",
        resBody = "{\n" +
                "\t\"data\": {\n" +
                "\t\t\"payerObjId\": \"张三\",\n" +
                "\t\t\"payerObjName\": \"\",\n" +
                "\t\t\"paNum\": \"18909718888\",\n" +
                "\t\t\"psNum\": \"772023012589770046\",\n" +
                "\t\t\"ownerId\": \"王王\",\n" +
                "\t\t\"ownerName\": \"1001\",\n" +
                "\t\t\"tel\": \"\",\n" +
                "\t},\n" +
                "\t\"page\": 0,\n" +
                "\t\"records\": 1,\n" +
                "\t\"rows\": 0,\n" +
                "\t\"total\": 1\n" +
                "}"
)
@Java110Cmd(serviceCode = "fee.listFeePayerObj")
public class ListFeePayerObjCmd extends Cmd {

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");

        //todo 如果包含feeId 则根据feeId 刷入 payerObjId 和payerObjType
        ifContainFeeId(reqJson);
        Assert.hasKeyAndValue(reqJson, "payerObjId", "未包含payerObjId");
        Assert.hasKeyAndValue(reqJson, "payerObjType", "未包含payerObjType");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONObject data = new JSONObject();
        String payerObjType = reqJson.getString("payerObjType");
        String payerObjId = reqJson.getString("payerObjId");
        // todo 如果是房屋
        ifRoom(payerObjId, payerObjType, reqJson, data);

        // todo 如果是车辆
        ifCar(payerObjId, payerObjType, reqJson, data);

        // todo 如果是合同
        ifContract(payerObjId, payerObjType, reqJson, data);

        context.setResponseEntity(ResultVo.createResponseEntity(data));

    }


    /**
     * 如果是房屋
     *
     * @param reqJson
     * @param data
     */
    private void ifRoom(String payerObjId, String payerObjType, JSONObject reqJson, JSONObject data) {

        //todo 如果不是房屋 直接返回
        if (!FeeDto.PAYER_OBJ_TYPE_ROOM.equals(payerObjType)) {
            return;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(payerObjId);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");

        data.putAll(BeanConvertUtil.beanCovertJson(roomDtos.get(0)));
        data.put("payerObjName", roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomDtos.get(0).getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return;
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerRoomRelDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }
        data.putAll(BeanConvertUtil.beanCovertJson(ownerDtos.get(0)));

    }

    /**
     * 如果是车辆
     *
     * @param reqJson
     * @param data
     */
    private void ifCar(String payerObjId, String payerObjType, JSONObject reqJson, JSONObject data) {

        //todo 如果不是车辆 直接返回
        if (!FeeDto.PAYER_OBJ_TYPE_CAR.equals(payerObjType)) {
            return;
        }

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setMemberId(payerObjId);
        ownerCarDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "车辆不存在");

        data.putAll(BeanConvertUtil.beanCovertJson(ownerCarDtos.get(0)));
        data.put("payerObjName", ownerCarDtos.get(0).getCarNum());


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }
        data.putAll(BeanConvertUtil.beanCovertJson(ownerDtos.get(0)));

    }

    /**
     * 如果 合同
     *
     * @param reqJson
     * @param data
     */
    private void ifContract(String payerObjId, String payerObjType, JSONObject reqJson, JSONObject data) {
        //todo 如果不是合同 直接返回
        if (!FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(payerObjType)) {
            return;
        }
        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(payerObjId);
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
        Assert.listOnlyOne(contractDtos, "合同不存在");

        data.putAll(BeanConvertUtil.beanCovertJson(contractDtos.get(0)));
        data.put("payerObjName", contractDtos.get(0).getContractName());

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(contractDtos.get(0).getbLink());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }
        data.putAll(BeanConvertUtil.beanCovertJson(ownerDtos.get(0)));
    }


    /**
     * 如果包含了feeId 则查询 费用作用对象信息
     *
     * @param reqJson
     */
    private void ifContainFeeId(JSONObject reqJson) {


        if (!reqJson.containsKey("feeId")) {
            return;
        }

        String feeId = reqJson.getString("feeId");

        if (StringUtil.isEmpty(feeId)) {
            return;
        }

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeDto> payFeeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        Assert.listOnlyOne(payFeeDtos, "费用不存在");

        reqJson.put("payerObjId", payFeeDtos.get(0).getPayerObjId());
        reqJson.put("payerObjType", payFeeDtos.get(0).getPayerObjType());
    }

}
