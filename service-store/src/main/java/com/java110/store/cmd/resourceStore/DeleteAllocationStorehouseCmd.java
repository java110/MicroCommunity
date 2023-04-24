package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.intf.community.IResourceStoreServiceSMO;
import com.java110.intf.store.*;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStore.deleteAllocationStorehouse")
public class DeleteAllocationStorehouseCmd extends Cmd {

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseV1InnerServiceSMO allocationStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyV1InnerServiceSMO allocationStorehouseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "applyId", "调拨编号不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //获取申请id
        String applyId = reqJson.getString("applyId");
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        allocationStorehouseApplyDto.setApplyId(applyId);
        //查询调拨申请信息
        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        Assert.listOnlyOne(allocationStorehouseApplyDtos, "查询调拨申请表错误！");
        //获取状态
        String state = allocationStorehouseApplyDtos.get(0).getState();
        if (!StringUtil.isEmpty(state) && state.equals("1200")) { //1200表示调拨申请状态
            deleteAllocationStorehouse(reqJson);
        } else {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您的调拨订单状态已改变，无法进行取消操作！！");
            context.setResponseEntity(responseEntity);
            return;
        }
    }

    public void deleteAllocationStorehouse(JSONObject paramInJson) {

        AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
        allocationStorehouseDto.setApplyId(paramInJson.getString("applyId"));
        allocationStorehouseDto.setStoreId(paramInJson.getString("storeId"));

        List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        int flag = 0;
        for (AllocationStorehouseDto tmpAllocationStorehouseDto : allocationStorehouseDtos) {
            AllocationStorehousePo allocationStorehousePo = BeanConvertUtil.covertBean(tmpAllocationStorehouseDto, AllocationStorehousePo.class);
            allocationStorehousePo.setStatusCd("1");
            flag = allocationStorehouseV1InnerServiceSMOImpl.deleteAllocationStorehouse(allocationStorehousePo);

            if (flag < 1) {
                throw new CmdException("删除失败");
            }
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(allocationStorehousePo.getResId());
            //查询资源物品表
            List<ResourceStorePo> resourceStores = resourceStoreServiceSMOImpl.getResourceStores(resourceStorePo);
            Assert.listOnlyOne(resourceStores, "资源物品信息错误");
            //获取库存数量
            BigDecimal resourceStoreStock = new BigDecimal(resourceStores.get(0).getStock());
            //获取调拨的数量
            BigDecimal storehouseStock = new BigDecimal(allocationStorehousePo.getStock());
            //库存数量
            BigDecimal stock = resourceStoreStock.add(storehouseStock);
            resourceStorePo.setStock(String.valueOf(stock));
            //计算最小计量总数
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            BigDecimal miniStock = new BigDecimal(resourceStores.get(0).getMiniStock()); //获取物品表的最小计量总数
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            BigDecimal miniUnitStock = new BigDecimal(resourceStores.get(0).getMiniUnitStock()); //获取最小计量单位数量
            BigDecimal stock2 = new BigDecimal(allocationStorehousePo.getStock()); //获取最小计量单位数量
            BigDecimal nowMiniStock = stock2.multiply(miniUnitStock); //计算当前的最小计量总数
            BigDecimal newMiniStock = miniStock.add(nowMiniStock);
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
            //取消流程审批
            //查询任务
            PurchaseApplyDto purchaseDto = new PurchaseApplyDto();
            purchaseDto.setBusinessKey(tmpAllocationStorehouseDto.getApplyId());
            List<PurchaseApplyDto> purchaseApplyDtoList = purchaseApplyInnerServiceSMOImpl.getActRuTaskId(purchaseDto);
            if (purchaseApplyDtoList != null && purchaseApplyDtoList.size() > 0) {
                PurchaseApplyDto purchaseDto1 = new PurchaseApplyDto();
                purchaseDto1.setActRuTaskId(purchaseApplyDtoList.get(0).getActRuTaskId());
                purchaseDto1.setAssigneeUser("999999");
                purchaseApplyInnerServiceSMOImpl.updateActRuTaskById(purchaseDto1);
            }
            // 保存至 物品 times表
            //查询调拨批次价格
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(tmpAllocationStorehouseDto.getTimesId());
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);

            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(tmpAllocationStorehouseDto.getApplyId());
            resourceStoreTimesPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
            resourceStoreTimesPo.setStock(tmpAllocationStorehouseDto.getStock());
            resourceStoreTimesPo.setResCode(tmpAllocationStorehouseDto.getResCode());
            resourceStoreTimesPo.setStoreId(tmpAllocationStorehouseDto.getStoreId());
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setShId(tmpAllocationStorehouseDto.getShIda());
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
        }

        AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        allocationStorehouseApplyPo.setApplyId(allocationStorehouseDto.getApplyId());
        allocationStorehouseApplyPo.setStoreId(allocationStorehouseDto.getStoreId());
        allocationStorehouseApplyPo.setStatusCd("1");
        flag = allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }

    }
}
