package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchase.AllocationStorehouseApplyDto;
import com.java110.dto.purchase.AllocationStorehouseDto;
import com.java110.dto.purchase.PurchaseApplyDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.dto.store.StorehouseDto;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.po.purchase.AllocationStorehouseApplyPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.store.bmo.allocation.IAllocationBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 调拨入库
 */
@Java110Cmd(serviceCode = "resourceStore.allocationStoreEnter")
public class AllocationStoreEnterCmd extends Cmd {

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;


    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyV1InnerServiceSMO allocationStorehouseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseV1InnerServiceSMO allocationStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationBMO allocationBMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "applyId", "订单ID为空");

        AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        allocationStorehouseApplyDto.setApplyId(reqJson.getString("applyId"));
        List<AllocationStorehouseApplyDto> allocationStorehouseDtos
                = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        Assert.listOnlyOne(allocationStorehouseDtos, "调拨申请单存在多条");

        if (!reqJson.containsKey("resourceStores")) {
            throw new CmdException("未包含物品");
        }
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");

        if (resourceStores == null || resourceStores.size() < 1) {
            throw new CmdException("未包含物品");
        }
        JSONObject resourceStore = null;
        ResourceStoreTimesDto resourceStoreTimesDto = null;
        List<ResourceStoreTimesDto> resourceStoreTimesDtos = null;
        int originStock = 0;
        for (int detailIndex = 0; detailIndex < resourceStores.size(); detailIndex++) {
            resourceStore = resourceStores.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(resourceStore, "quantity", "调拨数量未包含");
            Assert.hasKeyAndValue(resourceStore, "shIda", "原仓库不存在");
            Assert.hasKeyAndValue(resourceStore, "shIdz", "目标仓库不存在");
            Assert.hasKeyAndValue(resourceStore, "timesId", "单价未填写");
            Assert.hasKeyAndValue(resourceStore, "resCode", "物品编码未填写");
            Assert.hasKeyAndValue(resourceStore, "asId", "调拨明细未填写");


            resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(resourceStore.getString("timesId"));
            resourceStoreTimesDto.setShId(resourceStore.getString("shIda"));
            resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);

            Assert.listOnlyOne(resourceStoreTimesDtos, "物品不存在");
            originStock = Integer.parseInt(resourceStoreTimesDtos.get(0).getStock());
            if(originStock < resourceStore.getIntValue("quantity")){
                throw new CmdException(reqJson.getString("resCode")+"库存不足");
            }
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        JSONObject resourceStore = null;
        int quantity = 0;
        for (int detailIndex = 0; detailIndex < resourceStores.size(); detailIndex++) {
            resourceStore = resourceStores.getJSONObject(detailIndex);
            //todo 查询调拨记录
            AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
            allocationStorehouseDto.setApplyId(reqJson.getString("applyId"));
            allocationStorehouseDto.setAsId(resourceStore.getString("asId"));
            List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseV1InnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
            quantity = resourceStore.getIntValue("quantity");
            //todo 每条记录调拨
            allocationBMOImpl.doToAllocationStorehouse(allocationStorehouseDtos.get(0), quantity);
        }

        //
        String applyId = reqJson.getString("applyId");
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        allocationStorehouseApplyPo.setApplyId(applyId);
        //todo 如果包含taskId 流程提交下去
        if (reqJson.containsKey("taskId")) {
            reqJson.put("auditCode", "1100");
            reqJson.put("auditMessage", "入库成功");
            reqJson.put("id", reqJson.getString("applyId"));
            reqJson.put("storeId", CmdContextUtils.getStoreId(context));
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                allocationStorehouseApplyPo.setState(AllocationStorehouseApplyDto.STATE_END);
            } else {
                allocationStorehouseApplyPo.setState(AllocationStorehouseApplyDto.STATE_DEALING);
            }
        } else {
            allocationStorehouseApplyPo.setState(AllocationStorehouseApplyDto.STATE_DEALING);
        }
        allocationStorehouseApplyPo.setStatusCd("0");
        allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);



        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "采购申请成功"));
    }

}
