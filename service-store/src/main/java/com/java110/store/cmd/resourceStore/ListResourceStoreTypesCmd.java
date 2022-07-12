package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.resourceStoreType.ResourceStoreTypeDto;
import com.java110.intf.store.IResourceStoreTypeInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStoreType.listResourceStoreTypes")
public class ListResourceStoreTypesCmd extends Cmd {

    @Autowired
    private IResourceStoreTypeInnerServiceSMO resourceStoreTypeInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (reqJson.containsKey("flag") && reqJson.getString("flag").equals("0")) {
            reqJson.put("parentId", reqJson.getString("rstId"));
            reqJson.put("rstId", null);
            reqJson.put("name", null);
        }
        if (reqJson.containsKey("rsId") && !StringUtil.isEmpty(reqJson.getString("rsId"))) {
            reqJson.put("rstId", reqJson.getString("rsId"));
        }
        if (reqJson.containsKey("rstName") && !StringUtil.isEmpty(reqJson.getString("rstName"))) {
            reqJson.put("name", reqJson.getString("rstName"));
        }
        ResourceStoreTypeDto resourceStoreTypeDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreTypeDto.class);

        int count = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypesCount(resourceStoreTypeDto);

        List<ResourceStoreTypeDto> resourceStoreTypeDtos = null;

        if (count > 0) {
            resourceStoreTypeDtos = resourceStoreTypeInnerServiceSMOImpl.queryResourceStoreTypes(resourceStoreTypeDto);
        } else {
            resourceStoreTypeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStoreTypeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
