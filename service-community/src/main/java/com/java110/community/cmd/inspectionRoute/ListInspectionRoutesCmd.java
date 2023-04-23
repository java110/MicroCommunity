package com.java110.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.inspection.InspectionRouteDto;
import com.java110.intf.community.IInspectionRouteInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.inspectionRoute.ApiInspectionRouteDataVo;
import com.java110.vo.api.inspectionRoute.ApiInspectionRouteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "inspectionRoute.listInspectionRoutes")
public class ListInspectionRoutesCmd extends Cmd {

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRouteDto inspectionRouteDto = BeanConvertUtil.covertBean(reqJson, InspectionRouteDto.class);

        int count = inspectionRouteInnerServiceSMOImpl.queryInspectionRoutesCount(inspectionRouteDto);

        List<ApiInspectionRouteDataVo> inspectionRoutes = null;

        if (count > 0) {
            inspectionRoutes = BeanConvertUtil.covertBeanList(inspectionRouteInnerServiceSMOImpl.queryInspectionRoutes(inspectionRouteDto), ApiInspectionRouteDataVo.class);
        } else {
            inspectionRoutes = new ArrayList<>();
        }

        ApiInspectionRouteVo apiInspectionRouteVo = new ApiInspectionRouteVo();

        apiInspectionRouteVo.setTotal(count);
        apiInspectionRouteVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionRouteVo.setInspectionRoutes(inspectionRoutes);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionRouteVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
