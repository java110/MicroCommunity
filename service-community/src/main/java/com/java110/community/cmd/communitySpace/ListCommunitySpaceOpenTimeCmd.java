package com.java110.community.cmd.communitySpace;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.community.CommunitySpaceOpenTimeDto;
import com.java110.intf.community.ICommunitySpaceOpenTimeV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110CmdDoc(title = "查询可预约时间场地",
        description = "查询系统中的查询可预约时间场地",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/communitySpace.listCommunitySpaceOpenTime",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "communitySpace.listCommunitySpaceOpenTime"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "spaceId", length = 30, remark = "场地ID"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "appointmentTime", length = 30, remark = "预约日期 YYYY-MM-DD"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "hours", type = "String", remark = "小时"),
        }
)

@Java110ExampleDoc(
        reqBody = "http://{ip}:{port}/app/communitySpace.listCommunitySpaceOpenTime?spaceId=123&appointmentTime=2022-01-01&communityId=2022081539020475",
        resBody = "{\"code\":0,\"data\":[{\"hours\":1,\"hours\":2}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":2}"
)
@Java110Cmd(serviceCode = "communitySpace.listCommunitySpaceOpenTime")
public class ListCommunitySpaceOpenTimeCmd extends Cmd{

    private static Logger logger = LoggerFactory.getLogger(ListCommunitySpacePersonCmd.class);


    @Autowired
    private ICommunitySpaceOpenTimeV1InnerServiceSMO communitySpaceOpenTimeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"appointmentTime","预约时间");
        Assert.hasKeyAndValue(reqJson,"communityId","小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        CommunitySpaceOpenTimeDto communitySpaceOpenTimeDto = new CommunitySpaceOpenTimeDto();
        communitySpaceOpenTimeDto.setSpaceId(reqJson.getString("spaceId"));
        communitySpaceOpenTimeDto.setVenueId(reqJson.getString("venueId"));
        communitySpaceOpenTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
        List<CommunitySpaceOpenTimeDto> communitySpaceOpenTimeDtos = communitySpaceOpenTimeV1InnerServiceSMOImpl.queryCommunitySpaceOpenTimes(communitySpaceOpenTimeDto);

        ResultVo resultVo = new ResultVo(1, communitySpaceOpenTimeDtos.size(), communitySpaceOpenTimeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
