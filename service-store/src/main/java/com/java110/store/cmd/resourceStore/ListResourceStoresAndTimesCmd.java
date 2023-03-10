package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.PageDto;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.resourceStore.ApiResourceStoreDataVo;
import com.java110.vo.api.resourceStore.ApiResourceStoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询价格物品
 * add by wuxw 2023-01-19
 */
@Java110Cmd(serviceCode = "resourceStore.listResourceStoresAndTimes")
public class ListResourceStoresAndTimesCmd extends Cmd {

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户ID");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResourceStoreTimesDto resourceStoreTimesDto = BeanConvertUtil.covertBean(reqJson, ResourceStoreTimesDto.class);

        int count = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimessCount(resourceStoreTimesDto);
        List<ResourceStoreTimesDto> resourceStores = null;
        if (count > 0) {
            resourceStores = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
        } else {
            resourceStores = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, resourceStores);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
