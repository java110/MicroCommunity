package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStore.auditAllocationStoreOrder")
public class AuditAllocationStoreOrderCmd extends Cmd {

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyV1InnerServiceSMO allocationStorehouseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyId", "订单号不能为空");
        Assert.hasKeyAndValue(reqJson, "taskId", "必填，请填写任务ID");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写批注");
    }

    /**
     * 调拨申请-物品调拨审核
     * @param event              事件对象
     * @param context 数据上文对象
     * @param reqJson            请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        int flag = 0;
        AllocationStorehouseApplyDto allocationStorehouseDto = new AllocationStorehouseApplyDto();
        allocationStorehouseDto.setTaskId(reqJson.getString("taskId"));
        allocationStorehouseDto.setApplyId(reqJson.getString("applyId"));
        allocationStorehouseDto.setStoreId(reqJson.getString("storeId"));
        allocationStorehouseDto.setAuditCode(reqJson.getString("state"));
        allocationStorehouseDto.setAuditMessage(reqJson.getString("remark"));
        allocationStorehouseDto.setCurrentUserId(reqJson.getString("userId"));
        allocationStorehouseDto.setNoticeState(reqJson.getString("noticeState"));
        AllocationStorehouseApplyDto tmpAllocationStorehouseDto = new AllocationStorehouseApplyDto();
        tmpAllocationStorehouseDto.setApplyId(reqJson.getString("applyId"));
        tmpAllocationStorehouseDto.setStoreId(reqJson.getString("storeId"));
        List<AllocationStorehouseApplyDto> allocationStorehouseDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(tmpAllocationStorehouseDto);
        Assert.listOnlyOne(allocationStorehouseDtos, "调拨申请单存在多条");
        allocationStorehouseDto.setStartUserId(allocationStorehouseDtos.get(0).getStartUserId());

        boolean isLastTask = allocationStorehouseUserInnerServiceSMOImpl.completeTask(allocationStorehouseDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        if (isLastTask) {
            updateAllocationStorehouse(reqJson, context);
        } else if (reqJson.getString("state").equals("1100")) {  //审核同意时，状态变为调拨审核  1100同意状态
            String procure = reqJson.getString("procure");
            AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
            allocationStorehouseApplyPo.setApplyId(allocationStorehouseDtos.get(0).getApplyId());
            allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_AUDIT);
            if (!StringUtil.isEmpty(procure) && procure.equals("true")) {
                allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_REVIEWED);
            }
            flag = allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
        } else if (reqJson.getString("state").equals("1200")) {  //审核拒绝时，状态变为调拨失败  1200拒绝状态
            revokeAllocationStorehouse(reqJson);
        }
    }

    /**
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private void updateAllocationStorehouse(JSONObject paramInJson, ICmdDataFlowContext context) {

        AllocationStorehouseApplyDto tmpAllocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        tmpAllocationStorehouseApplyDto.setApplyId(paramInJson.getString("applyId"));
        tmpAllocationStorehouseApplyDto.setStoreId(paramInJson.getString("storeId"));
        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(tmpAllocationStorehouseApplyDto);

        Assert.listOnlyOne(allocationStorehouseApplyDtos, "存在多条记录，或不存在数据" + tmpAllocationStorehouseApplyDto.getApplyId());

        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(BeanConvertUtil.beanCovertMap(allocationStorehouseApplyDtos.get(0)));
        businessComplaint.put("state", AllocationStorehouseDto.STATE_SUCCESS);
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(businessComplaint, AllocationStorehouseApplyPo.class);
        int flag = 0;
        if (allocationStorehouseApplyDtos.get(0).getState().equals("1201") || allocationStorehouseApplyDtos.get(0).getState().equals("1204")) {
            flag = allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
            //调拨详情
            AllocationStorehouseDto tmpAllocationStorehouseDto = new AllocationStorehouseDto();
            tmpAllocationStorehouseDto.setApplyId(paramInJson.getString("applyId"));
            tmpAllocationStorehouseDto.setStoreId(paramInJson.getString("storeId"));
            List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(tmpAllocationStorehouseDto);

            for (AllocationStorehouseDto allocationStorehouseDto : allocationStorehouseDtos) {
                //查询是否新仓库有此物品
                ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
                resourceStoreDto.setShId(allocationStorehouseDto.getShIdz());
                resourceStoreDto.setResCode(allocationStorehouseDto.getResCode());
                List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
                ResourceStorePo resourceStorePo = new ResourceStorePo();

                if (resourceStoreDtos != null && resourceStoreDtos.size() == 1) {
                    //查询原仓库该物品记录
                    ResourceStoreDto originalResourceStoreDto = new ResourceStoreDto();
                    originalResourceStoreDto.setShId(allocationStorehouseDto.getShIda());
                    originalResourceStoreDto.setResId(allocationStorehouseDto.getResId());
                    List<ResourceStoreDto> originalResourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(originalResourceStoreDto);
                    Assert.listOnlyOne(originalResourceStoreDtos, "原仓库记录不存在");
                    ResourceStoreDto resourceStoreDto1 = resourceStoreDtos.get(0);
                    //计算库存
                    BigDecimal stock1 = new BigDecimal(resourceStoreDtos.get(0).getStock());
                    BigDecimal stock2 = new BigDecimal(allocationStorehouseDto.getStock());
                    resourceStoreDto1.setStock(stock1.add(stock2).toString());
                    //计算最小计量总数
                    if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())) {
                        throw new IllegalArgumentException("最小计量总数不能为空！");
                    }
                    BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock()); //获取原最小计量总数
                    if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                        throw new IllegalArgumentException("最小计量单位数量不能为空！");
                    }
                    BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock()); //获取最小计量单位数量
                    BigDecimal nowMiniStock = stock2.multiply(miniUnitStock); //计算调拨的最小计量总数
                    BigDecimal newMiniStock = miniStock.add(nowMiniStock);
                    resourceStoreDto1.setMiniStock(String.valueOf(newMiniStock));
                    resourceStorePo = BeanConvertUtil.covertBean(resourceStoreDto1, ResourceStorePo.class);
                    resourceStorePo.setAveragePrice(originalResourceStoreDtos.get(0).getAveragePrice());
                    resourceStorePo.setOutLowPrice(originalResourceStoreDtos.get(0).getOutLowPrice());
                    resourceStorePo.setOutHighPrice(originalResourceStoreDtos.get(0).getOutHighPrice());
                    resourceStorePo.setRstId(originalResourceStoreDtos.get(0).getRstId());
                    resourceStorePo.setParentRstId(originalResourceStoreDtos.get(0).getParentRstId());
                    resourceStorePo.setRssId(originalResourceStoreDtos.get(0).getRssId());
                    if (!StringUtil.isEmpty(originalResourceStoreDtos.get(0).getMiniUnitCode()) && !StringUtil.isEmpty(originalResourceStoreDtos.get(0).getMiniUnitStock())) {
                        resourceStorePo.setMiniUnitCode(originalResourceStoreDtos.get(0).getMiniUnitCode());
                        resourceStorePo.setMiniUnitStock(originalResourceStoreDtos.get(0).getMiniUnitStock());
                    }
                    flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);
                    if (flag < 1) {
                        throw new CmdException("修改失败");
                    }
                } else if (resourceStoreDtos != null && resourceStoreDtos.size() > 1) {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "物品信息查询到多条，请核实后再处理！");
                    context.setResponseEntity(responseEntity);
                    return;
                } else {
                    //查询是否原仓库有此物品
                    resourceStoreDto = new ResourceStoreDto();
                    resourceStoreDto.setShId(allocationStorehouseDto.getShIda());
                    resourceStoreDto.setResId(allocationStorehouseDto.getResId());
                    resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
                    Assert.listOnlyOne(resourceStoreDtos, "原仓库记录不存在");
                    resourceStorePo = BeanConvertUtil.covertBean(allocationStorehouseDto, resourceStorePo);
                    resourceStorePo.setResId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_resId));
                    resourceStorePo.setStock(allocationStorehouseDto.getStock());
                    //获取最小计量单位数量
                    if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                        throw new IllegalArgumentException("最小计量单位数量不能为空！");
                    }
                    BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock());
                    BigDecimal stock = new BigDecimal(allocationStorehouseDto.getStock());
                    //计算最小计量单位总数
                    BigDecimal miniStock = stock.multiply(miniUnitStock);
                    resourceStorePo.setMiniStock(String.valueOf(miniStock));
                    resourceStorePo.setShId(allocationStorehouseDto.getShIdz());
                    resourceStorePo.setPrice(resourceStoreDtos.get(0).getPrice());
                    resourceStorePo.setDescription("");
                    resourceStorePo.setUnitCode(resourceStoreDtos.get(0).getUnitCode());
                    resourceStorePo.setOutLowPrice(resourceStoreDtos.get(0).getOutLowPrice());
                    resourceStorePo.setOutHighPrice(resourceStoreDtos.get(0).getOutHighPrice());
                    resourceStorePo.setShowMobile(resourceStoreDtos.get(0).getShowMobile());
                    resourceStorePo.setWarningStock(resourceStoreDtos.get(0).getWarningStock());
                    resourceStorePo.setAveragePrice(resourceStoreDtos.get(0).getAveragePrice());
                    resourceStorePo.setRstId(resourceStoreDtos.get(0).getRstId());
                    resourceStorePo.setParentRstId(resourceStoreDtos.get(0).getParentRstId());
                    resourceStorePo.setRssId(resourceStoreDtos.get(0).getRssId());
                    resourceStorePo.setMiniUnitCode(resourceStoreDtos.get(0).getMiniUnitCode());
                    resourceStorePo.setMiniUnitStock(resourceStoreDtos.get(0).getMiniUnitStock());
                    flag = resourceStoreV1InnerServiceSMOImpl.saveResourceStore(resourceStorePo);
                    if (flag < 1) {
                        throw new CmdException("添加失败");
                    }
                }
                // 保存至 物品 times表
                //查询调拨批次价格
                ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
                resourceStoreTimesDto.setTimesId(allocationStorehouseDto.getTimesId());
                List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);

                ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
                resourceStoreTimesPo.setApplyOrderId(tmpAllocationStorehouseDto.getApplyId());
                resourceStoreTimesPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
                resourceStoreTimesPo.setStock(allocationStorehouseDto.getStock());
                resourceStoreTimesPo.setResCode(allocationStorehouseDto.getResCode());
                resourceStoreTimesPo.setStoreId(allocationStorehouseDto.getStoreId());
                resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
                resourceStoreTimesPo.setShId(allocationStorehouseDto.getShIdz());
                resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);

            }
        } else if (allocationStorehouseApplyDtos.get(0).getState().equals("1203")) {
            allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_FAIL);
            flag = allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
        }
    }


    /**
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private void revokeAllocationStorehouse(JSONObject paramInJson) {

        AllocationStorehouseApplyDto tmpAllocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        tmpAllocationStorehouseApplyDto.setApplyId(paramInJson.getString("applyId"));
        tmpAllocationStorehouseApplyDto.setStoreId(paramInJson.getString("storeId"));
        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtos = allocationStorehouseApplyInnerServiceSMOImpl.queryAllocationStorehouseApplys(tmpAllocationStorehouseApplyDto);
        Assert.listOnlyOne(allocationStorehouseApplyDtos, "存在多条记录，或不存在数据" + tmpAllocationStorehouseApplyDto.getApplyId());

        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(BeanConvertUtil.beanCovertMap(allocationStorehouseApplyDtos.get(0)));
        businessComplaint.put("state", AllocationStorehouseDto.STATE_FAIL);
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = BeanConvertUtil.covertBean(businessComplaint, AllocationStorehouseApplyPo.class);
        int flag = allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(allocationStorehouseApplyPo);
        if (flag < 1) {
            throw new CmdException("修改失败");
        }
        //调拨详情数据
        AllocationStorehouseDto tmpAllocationStorehouseDto = new AllocationStorehouseDto();
        tmpAllocationStorehouseDto.setApplyId(paramInJson.getString("applyId"));
        tmpAllocationStorehouseDto.setStoreId(paramInJson.getString("storeId"));
        List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(tmpAllocationStorehouseDto);

        for (AllocationStorehouseDto allocationStorehouseDto : allocationStorehouseDtos) {
            //查询是否原仓库有此物品
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(allocationStorehouseDto.getResId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            Assert.listOnlyOne(resourceStoreDtos, "资源物品信息错误");
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(allocationStorehouseDto.getResId());
            //获取库存数量
            BigDecimal resourceStoreStock = new BigDecimal(resourceStoreDtos.get(0).getStock());
            //获取调拨的数量
            BigDecimal storehouseStock = new BigDecimal(allocationStorehouseDto.getStock());
            //库存数量
            BigDecimal stock = resourceStoreStock.add(storehouseStock);
            resourceStorePo.setStock(String.valueOf(stock));
            //计算最小计量总数
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock()); //获取物品表的最小计量总数
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock()); //获取最小计量单位数量
            BigDecimal stock1 = new BigDecimal(allocationStorehouseDto.getStock()); //获取最小计量单位数量
            BigDecimal nowMiniStock = stock1.multiply(miniUnitStock); //计算当前的最小计量总数
            BigDecimal newMiniStock = miniStock.add(nowMiniStock);
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);

            // 保存至 物品 times表
            //查询调拨批次价格
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(allocationStorehouseDto.getTimesId());
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);

            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(tmpAllocationStorehouseDto.getApplyId());
            resourceStoreTimesPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
            resourceStoreTimesPo.setStock(allocationStorehouseDto.getStock());
            resourceStoreTimesPo.setResCode(allocationStorehouseDto.getResCode());
            resourceStoreTimesPo.setStoreId(allocationStorehouseDto.getStoreId());
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setShId(allocationStorehouseDto.getShIda());
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);

            if (flag < 1) {
                throw new CmdException("修改失败");
            }
        }
    }
}
