package com.java110.common.cmd.area;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.area.AreaDto;
import com.java110.intf.common.IAreaInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.area.ApiAreaDataVo;
import com.java110.vo.api.area.ApiAreaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "area.listAreas")
public class ListAreas extends Cmd {

    @Autowired
    private IAreaInnerServiceSMO areaInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        AreaDto areaDto = BeanConvertUtil.covertBean(reqJson, AreaDto.class);

        List<ApiAreaDataVo> areas = BeanConvertUtil.covertBeanList(areaInnerServiceSMOImpl.getArea(areaDto),ApiAreaDataVo.class);


        ApiAreaVo apiAreaVo = new ApiAreaVo();

        apiAreaVo.setTotal(1);
        apiAreaVo.setRecords(1);
        apiAreaVo.setAreas(areas);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAreaVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
