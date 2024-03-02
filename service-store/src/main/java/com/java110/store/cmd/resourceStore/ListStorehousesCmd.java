package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.store.StorehouseDto;
import com.java110.intf.store.IStorehouseInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110CmdDoc(title = "查询仓库",
        description = "外部系统通过此接口查询仓库",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/resourceStore.listStorehouses",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "resourceStore.listStorehouses"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page", length = 11, type = "int", remark = "页数"),
        @Java110ParamDoc(name = "row", length = 11, type = "int", remark = "行业数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data", name = "shName", type = "String", remark = "仓库"),
                @Java110ParamDoc(parentNodeName = "data", name = "shId", type = "String", remark = "仓库ID"),
        }
)

@Java110ExampleDoc(
        reqBody = "\n" +
                "http://localhost:3000/app/resourceStore.listStorehouses?shName=&shType=&isShow=&shId=&communityId=2023052267100146&page=1&row=10",
        resBody = "{\n" +
                "    \"code\": 0,\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"allocationFlowName\": \"调拨审核\",\n" +
                "            \"allocationRafId\": \"102023082479330020\",\n" +
                "            \"allocationSwitch\": \"ON\",\n" +
                "            \"createTime\": \"2023-08-24 21:56:01\",\n" +
                "            \"isShow\": \"true\",\n" +
                "            \"page\": -1,\n" +
                "            \"purchaseFlowName\": \"采购审核流程\",\n" +
                "            \"purchaseRafId\": \"102023082406130006\",\n" +
                "            \"purchaseSwitch\": \"ON\",\n" +
                "            \"records\": 0,\n" +
                "            \"row\": 0,\n" +
                "            \"shDesc\": \"\",\n" +
                "            \"shId\": \"102023082412640003\",\n" +
                "            \"shName\": \"A仓库\",\n" +
                "            \"statusCd\": \"0\",\n" +
                "            \"storeId\": \"10202305221014329108000168\",\n" +
                "            \"total\": 0,\n" +
                "            \"useFlowName\": \"领用审核\",\n" +
                "            \"useRafId\": \"102023082414750013\",\n" +
                "            \"useSwitch\": \"ON\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"page\": 0,\n" +
                "    \"records\": 1,\n" +
                "    \"rows\": 0,\n" +
                "    \"total\": 1\n" +
                "}"
)

@Java110Cmd(serviceCode = "resourceStore.listStorehouses")
public class ListStorehousesCmd extends Cmd {

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = CmdContextUtils.getStoreId(context);
        StorehouseDto storehouseDto = BeanConvertUtil.covertBean(reqJson, StorehouseDto.class);
        if (reqJson.containsKey("flag") && !StringUtil.isEmpty(reqJson.getString("flag")) && reqJson.getString("flag").equals("1")) {
            storehouseDto.setCommunityId("");
        }
        storehouseDto.setStoreId(storeId);
        int count = storehouseInnerServiceSMOImpl.queryStorehousesCount(storehouseDto);
        List<StorehouseDto> storehouseDtos = new ArrayList<>();
        if (count > 0) {
            List<StorehouseDto> storehouseList = storehouseInnerServiceSMOImpl.queryStorehouses(storehouseDto);
            if (reqJson.containsKey("flag") && !StringUtil.isEmpty(reqJson.getString("flag")) && reqJson.getString("flag").equals("1")) {
                for (StorehouseDto storehouse : storehouseList) {
                    //获取仓库所属小区id
                    String communityId = storehouse.getCommunityId();
                    //获取仓库是否对外开放
                    String isShow = storehouse.getIsShow();
                    if (communityId.equals(reqJson.getString("communityId"))) { //本小区仓库
                        storehouseDtos.add(storehouse);
                    } else if (isShow.equals("true")) { //对外开放的仓库
                        storehouseDtos.add(storehouse);
                    }
                }
            } else {
                storehouseDtos.addAll(storehouseList);
            }
        } else {
            storehouseDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) storehouseDtos.size() / (double) reqJson.getInteger("row")), storehouseDtos.size(), storehouseDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
