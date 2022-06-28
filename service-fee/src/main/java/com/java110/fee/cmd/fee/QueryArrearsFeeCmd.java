package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.community.IUnitInnerServiceSMO;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.ApiArrearsFeeDataVo;
import com.java110.vo.api.ApiArrearsFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "fee.queryArrearsFee")
public class QueryArrearsFeeCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "feeTypeCd", "请求中未包含feeTypeCd信息");
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");


        Assert.isInteger(reqJson.getString("row"), "row必须为数字");
        Assert.isInteger(reqJson.getString("page"), "page必须为数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        FeeDto feeDtoParamIn = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        feeDtoParamIn.setArrearsEndTime(DateUtil.getCurrentDate());

        //车位时处理为 查询多个
        if (FeeTypeConstant.FEE_TYPE_HIRE_PARKING_SPACE.equals(feeDtoParamIn.getFeeTypeCd())) {
            feeDtoParamIn.setFeeTypeCd("");
            feeDtoParamIn.setFeeTypeCds(new String[]{FeeTypeConstant.FEE_TYPE_HIRE_DOWN_PARKING_SPACE,
                    FeeTypeConstant.FEE_TYPE_HIRE_UP_PARKING_SPACE});
        }

        int page = reqJson.getInteger("page");
        int row = reqJson.getInteger("row");

        int feeCount = feeInnerServiceSMOImpl.queryFeesCount(feeDtoParamIn);
        ApiArrearsFeeVo apiArrearsFeeVo = new ApiArrearsFeeVo();
        apiArrearsFeeVo.setTotal(feeCount);
        apiArrearsFeeVo.setRecords((int) Math.ceil((double) feeCount / (double) row));
        ResponseEntity<String> responseEntity = null;
        if (feeCount == 0) {
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiArrearsFeeVo), HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return;
        }

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDtoParamIn);

        List<ApiArrearsFeeDataVo> apiFeeVo = BeanConvertUtil.covertBeanList(feeDtos, ApiArrearsFeeDataVo.class);

        String[] objIds = this.getObjIds(feeDtos);

        if (FeeTypeConstant.FEE_TYPE_PROPERTY.equals(feeDtoParamIn.getFeeTypeCd())) {

            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setRoomIds(objIds);
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByRoom(ownerDto);
            freshRoomAndOwnerData(apiFeeVo, ownerDtos);
        } else {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setRoomIds(objIds);
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnersByParkingSpace(ownerDto);
            freshParkingSpaceAndOwnerData(apiFeeVo, ownerDtos);
        }

        apiArrearsFeeVo.setArrears(apiFeeVo);

        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiArrearsFeeVo), HttpStatus.OK);


        context.setResponseEntity(responseEntity);
    }

    /**
     * 刷新 房间号
     *
     * @param apiFeeVos 费用出参对象
     * @param ownerDtos 房屋信息
     */
    private void freshRoomAndOwnerData(List<ApiArrearsFeeDataVo> apiFeeVos, List<OwnerDto> ownerDtos) {

        for (ApiArrearsFeeDataVo apiFeeVo : apiFeeVos) {
            for (OwnerDto ownerDto : ownerDtos) {
                if (apiFeeVo.getPayerObjId().equals(ownerDto.getRoomId())) {
                    apiFeeVo.setNum(ownerDto.getRoomNum());
                    apiFeeVo.setOwnerName(ownerDto.getName());
                    apiFeeVo.setTel(ownerDto.getLink());
                }
            }
        }
    }

    /**
     * 刷新 车位编号
     *
     * @param apiFeeVos 费用出参对象
     * @param ownerDtos 房屋信息
     */
    private void freshParkingSpaceAndOwnerData(List<ApiArrearsFeeDataVo> apiFeeVos, List<OwnerDto> ownerDtos) {

        for (ApiArrearsFeeDataVo apiFeeVo : apiFeeVos) {
            for (OwnerDto ownerDto : ownerDtos) {
                if (apiFeeVo.getPayerObjId().equals(ownerDto.getPsId())) {
                    apiFeeVo.setNum(ownerDto.getNum());
                    apiFeeVo.setOwnerName(ownerDto.getName());
                    apiFeeVo.setTel(ownerDto.getLink());
                }
            }
        }
    }

    /**
     * 查询 objIds
     *
     * @param feeDtos 费用信息
     * @return objIds信息
     */
    private String[] getObjIds(List<FeeDto> feeDtos) {
        List<String> objIds = new ArrayList<String>();
        for (FeeDto feeDto : feeDtos) {
            objIds.add(feeDto.getPayerObjId());
        }

        return objIds.toArray(new String[objIds.size()]);
    }
}
