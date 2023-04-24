package com.java110.community.cmd.inspection;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.inspection.InspectionStaffDto;
import com.java110.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 员工巡检情况统计
 *
 *
 */

@Java110CmdDoc(title = "员工巡检情况统计",
        description = "物业手机端员工巡检情况统计，方便老板查看",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/inspection.queryReportStaffInspection",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "inspection.queryReportStaffInspection"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区信息"),
        @Java110ParamDoc(name = "queryTime", length = 30, remark = "查询日期 YYYY-MM-DD"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "staffName", type = "String", remark = "员工名称"),
                @Java110ParamDoc(parentNodeName = "data",name = "finishCount", type = "String", remark = "已巡检"),
                @Java110ParamDoc(parentNodeName = "data",name = "waitCount", type = "String", remark = "未巡检"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/inspection.queryReportStaffInspection?communityId=12323&queryTime=2022-11-11",
        resBody="{'code':0,'msg':'成功','data':{'staffName':'123123','finishCount':'123213','waitCount':123123}}"
)

@Java110Cmd(serviceCode = "inspection.queryReportStaffInspection")
public class QueryReportStaffInspectionCmd extends Cmd{

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区信息");
        Assert.hasKeyAndValue(reqJson,"queryTime","未包含查询日期");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {


        InspectionStaffDto inspectionStaffDto = BeanConvertUtil.covertBean(reqJson, InspectionStaffDto.class);

        List<InspectionStaffDto> inspectionStaffDtos = inspectionPlanStaffV1InnerServiceSMOImpl.queryStaffInspectionReport(inspectionStaffDto);

        context.setResponseEntity(ResultVo.createResponseEntity(inspectionStaffDtos));
    }
}
