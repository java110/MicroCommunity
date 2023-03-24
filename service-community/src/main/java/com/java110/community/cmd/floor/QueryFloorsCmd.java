package com.java110.community.cmd.floor;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.FloorDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiFloorDataVo;
import com.java110.vo.api.ApiFloorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Java110CmdDoc(title = "查询楼栋",
        description = "用于外系统查询楼栋信息功能",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/floor.queryFloors",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "floor.queryFloors",
        seq = 8
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "page", type = "int",length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "apiFloorDataVoList", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "apiFloorDataVoList",name = "floorId", type = "String", remark = "楼栋ID"),
                @Java110ParamDoc(parentNodeName = "apiFloorDataVoList",name = "floorNum", type = "String", remark = "楼栋编号"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/floor.queryFloors?page=1&row=10&communityId=123123",
        resBody="{'code':0,'msg':'成功','apiFloorDataVoList':[{'floorId':'123123','floorNum':'123213'}]}"
)

@Java110Cmd(serviceCode = "floor.queryFloors")
public class QueryFloorsCmd extends Cmd {

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.isInteger(reqJson.getString("page"), "page不是有效数字");
        Assert.isInteger(reqJson.getString("row"), "row不是有效数字");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //int page = reqJson.getInteger("page");
        int row = reqJson.getInteger("row");
        //String communityId = reqJson.getString("communityId");

        ApiFloorVo apiFloorVo = new ApiFloorVo();

        //查询总记录数
        int total = floorInnerServiceSMOImpl.queryFloorsCount(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
        apiFloorVo.setTotal(total);
        if (total > 0) {
            List<FloorDto> floorDtoList = floorInnerServiceSMOImpl.queryFloors(BeanConvertUtil.covertBean(reqJson, FloorDto.class));
            apiFloorVo.setApiFloorDataVoList(BeanConvertUtil.covertBeanList(floorDtoList, ApiFloorDataVo.class));
        }

        apiFloorVo.setRecords((int) Math.ceil((double) total / (double) row));

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFloorVo), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
