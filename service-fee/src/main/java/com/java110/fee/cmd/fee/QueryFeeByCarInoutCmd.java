package com.java110.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.fee.FeeDto;
import com.java110.intf.fee.IFeeInnerServiceSMO;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiMainFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "fee.queryFeeByCarInout")
public class QueryFeeByCarInoutCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "inoutId", "请求中未包含inoutId信息");
        Assert.jsonObjectHaveKey(reqJson, "state", "请求中未包含state信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        FeeDto feeDtoParamIn = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        feeDtoParamIn.setPayerObjId(reqJson.getString("inoutId"));
        feeDtoParamIn.setState(reqJson.getString("state"));
        feeDtoParamIn.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        feeDtoParamIn.setFeeFlag("2006012");

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDtoParamIn);
        ResponseEntity<String> responseEntity = null;
        if (feeDtos == null || feeDtos.size() == 0) {
            responseEntity = new ResponseEntity<String>("{}", HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return ;
        }

        FeeDto feeDto = feeDtos.get(0);

        ApiMainFeeVo apiFeeVo = BeanConvertUtil.covertBean(feeDto, ApiMainFeeVo.class);

        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);


        context.setResponseEntity(responseEntity);
    }
}
