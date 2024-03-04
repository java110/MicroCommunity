package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.AllocationStorehouseApplyPo;
import com.java110.po.purchase.AllocationStorehousePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 修改调拨申请
 */
@Java110Cmd(serviceCode = "resourceStore.updateAllocationStorehouse")
public class UpdateAllocationStorehouseCmd extends Cmd {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyV1InnerServiceSMO allocationStorehouseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseV1InnerServiceSMO allocationStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseV1InnerServiceSMO userStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStorehouseV1InnerServiceSMO storehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含申请信息");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        if (!reqJson.containsKey("resourceStores")) {
            throw new IllegalArgumentException("请求报文中未包含物品信息");
        }
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        if (resourceStores == null || resourceStores.size() < 1) {
            throw new IllegalArgumentException("请求报文中未包含物品信息");
        }
        //todo 校验 物品是否存在(用批次表的物品数量与前端传过来的数量比较，因为不同价格物品数量不同)
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            //主要用来校验库存够不够
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setStoreId(reqJson.getString("storeId"));
            resourceStoreDto.setResId(resourceStores.getJSONObject(resIndex).getString("resId"));
            resourceStoreDto.setShId(resourceStores.getJSONObject(resIndex).getString("shId"));
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            Assert.listOnlyOne(resourceStoreDtos, "未包含 物品信息");

            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(resourceStores.getJSONObject(resIndex).getString("timesId"));
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            Assert.listOnlyOne(resourceStoreTimesDtos, "查询物品批次表错误！");
            double stockA = Double.parseDouble(resourceStoreTimesDtos.get(0).getStock());
            double stockB = Double.parseDouble(resourceStores.getJSONObject(resIndex).getString("curStock"));
            if (stockA < stockB) {
                throw new IllegalArgumentException("该批次价格下库存数量不足！");
            }
            resourceStores.getJSONObject(resIndex).put("resName", resourceStoreDtos.get(0).getResName());
            resourceStores.getJSONObject(resIndex).put("stockA", stockA);
        }
    }

    /**
     * 调拨申请-调拨申请发起
     *
     * @param event              事件对象
     * @param cmdDataFlowContext
     * @param reqJson            请求报文
     * @throws CmdException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        int flag = 0;
        // 查询用户名称
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        reqJson.put("userName", userDtos.get(0).getName());
        //封装调拨对象
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = covertAllocationStorehouseApply(reqJson);
        //todo 默认写0 后面 相加
        allocationStorehouseApplyPo.setApplyCount("0.0");
        //todo 删除 老的 调拨物品
        AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
        allocationStorehousePo.setApplyId(allocationStorehouseApplyPo.getApplyId());
        allocationStorehouseV1InnerServiceSMOImpl.deleteAllocationStorehouse(allocationStorehousePo);
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        JSONObject resObj = null;
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            //处理 物品信息
            resObj = resourceStores.getJSONObject(resIndex);
            //todo 记录明细
            saveAllocationStorehouse(reqJson, allocationStorehouseApplyPo, resObj);
        }
        flag = allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);
        if (flag < 1) {
            throw new CmdException("保存修改物品失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void saveAllocationStorehouse(JSONObject reqJson, AllocationStorehouseApplyPo allocationStorehouseApplyPo, JSONObject resObj) {
        AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
        allocationStorehousePo.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
        allocationStorehousePo.setApplyId(allocationStorehouseApplyPo.getApplyId());
        allocationStorehousePo.setResId(resObj.getString("resId"));
        allocationStorehousePo.setResName(resObj.getString("resName"));
        allocationStorehousePo.setShIda(resObj.getString("shId"));
        allocationStorehouseApplyPo.setShId(resObj.getString("shId"));
        allocationStorehousePo.setShIdz(resObj.getString("shzId"));
        allocationStorehousePo.setStoreId(reqJson.getString("storeId"));
        //调拨(返还)数量
        allocationStorehousePo.setStock(resObj.getString("curStock"));
        //原有库存
        allocationStorehousePo.setOriginalStock(resObj.getString("stock"));
        allocationStorehousePo.setRemark(reqJson.getString("remark"));
        allocationStorehousePo.setStartUserId(reqJson.getString("userId"));
        allocationStorehousePo.setStartUserName(reqJson.getString("userName"));
        allocationStorehousePo.setTimesId(resObj.getString("timesId"));
        int flag = allocationStorehouseV1InnerServiceSMOImpl.saveAllocationStorehouse(allocationStorehousePo);
        if (flag < 1) {
            throw new CmdException("保存调拨物品失败");
        }
        double applyCount = Double.parseDouble(allocationStorehouseApplyPo.getApplyCount());
        applyCount += resObj.getDoubleValue("curStock");
        allocationStorehouseApplyPo.setApplyCount(applyCount + "");
    }

    /**
     * 封装对象
     *
     * @param reqJson
     * @return
     */
    private AllocationStorehouseApplyPo covertAllocationStorehouseApply(JSONObject reqJson) {
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        allocationStorehouseApplyPo.setApplyId(reqJson.getString("applyId"));
        allocationStorehouseApplyPo.setRemark(reqJson.getString("remark"));
        allocationStorehouseApplyPo.setStoreId(reqJson.getString("storeId"));
        allocationStorehouseApplyPo.setCommunityId(reqJson.getString("communityId"));
        return allocationStorehouseApplyPo;
    }
}
