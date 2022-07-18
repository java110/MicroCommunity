package com.java110.common.cmd.corders;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.corder.CorderDto;
import com.java110.intf.order.ICordersInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@Java110Cmd(serviceCode = "corders.listCorders")
public class ListCordersCmd extends Cmd {

    @Autowired
    private ICordersInnerServiceSMO cordersInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        CorderDto corderDto = BeanConvertUtil.covertBean(reqJson, CorderDto.class);

        int count = cordersInnerServiceSMOImpl.queryCordersCount(corderDto);

        List<CorderDto> corderVos = null;

        if (count > 0) {
            corderVos = cordersInnerServiceSMOImpl.queryCorders(corderDto);
        } else {
            corderVos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")),count,corderVos);

        context.setResponseEntity(responseEntity);
    }
}
