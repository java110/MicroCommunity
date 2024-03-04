package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.dto.purchase.AllocationStorehouseDto;
import com.java110.dto.purchase.AllocationStorehouseApplyDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.dto.store.StorehouseDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.AllocationStorehousePo;
import com.java110.po.purchase.AllocationStorehouseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.po.resource.ResourceStoreUseRecordPo;
import com.java110.po.user.UserStorehousePo;
import com.java110.store.bmo.allocation.IAllocationBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调拨申请
 */
@Java110Cmd(serviceCode = "resourceStore.saveAllocationStorehouse")
public class SaveAllocationStorehouseCmd extends Cmd {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

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
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IStorehouseV1InnerServiceSMO storehouseV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;

    @Autowired
    private IAllocationBMO allocationBMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreUseRecordV1InnerServiceSMO resourceStoreUseRecordV1InnerServiceSMOImpl;

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;


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
        //获取调拨返还状态标识
        String applyType = reqJson.getString("apply_type");
        List<StorehouseDto> storehouseDtos = new ArrayList<>();
        //校验 物品是否存在
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            //主要用来校验库存够不够
            validateResoureStore(reqJson, resourceStores, applyType, resIndex);
            JSONObject param = resourceStores.getJSONObject(resIndex);
            //todo 查询仓库是否存在
            StorehouseDto storehouseDto = new StorehouseDto();
            storehouseDto.setShId(param.getString("shzId"));
            storehouseDtos = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
        }
        Assert.listOnlyOne(storehouseDtos, "仓库不存在");
        reqJson.put("resourceStores", resourceStores);
        if (!StorehouseDto.SWITCH_ON.equals(storehouseDtos.get(0).getAllocationSwitch())) {
            return;
        }
        String storeId = CmdContextUtils.getStoreId(cmdDataFlowContext);
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(storeId);
        oaWorkflowDto.setFlowId(storehouseDtos.get(0).getAllocationFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        Assert.listOnlyOne(oaWorkflowDtos, "流程不存在");
        if (!OaWorkflowDto.STATE_COMPLAINT.equals(oaWorkflowDtos.get(0).getState())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }
        if (StringUtil.isEmpty(oaWorkflowDtos.get(0).getProcessDefinitionKey())) {
            throw new IllegalArgumentException(oaWorkflowDtos.get(0).getFlowName() + "流程未部署");
        }
    }

    /**
     * 校验 调拨物品数据合法性
     * <p>
     * 主要用来校验库存 够不够
     *
     * @param reqJson
     * @param resourceStores
     * @param applyType
     * @param resIndex
     */
    private void validateResoureStore(JSONObject reqJson, JSONArray resourceStores, String applyType, int resIndex) {
        if (AllocationStorehouseApplyDto.STORE_TYPE_ALLOCATION.equals(applyType)) {  //调拨
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
        } else if (AllocationStorehouseApplyDto.STORE_TYPE_RETURN.equals(applyType)) { //返还
            UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
            userStorehouseDto.setResId(resourceStores.getJSONObject(resIndex).getString("resId"));
            userStorehouseDto.setUserId(reqJson.getString("userId"));
            List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
            Assert.listOnlyOne(userStorehouseDtos, "未包含 物品信息");
            double stockA = Double.parseDouble(userStorehouseDtos.get(0).getStock());
//                double stockB = Double.parseDouble(resourceStores.getJSONObject(resIndex).getString("curStock"));
            if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            //获取个人物品原最小计量总数
            double miniStock = Double.parseDouble(userStorehouseDtos.get(0).getMiniStock());
            //获取当前返还的数量
            double curStock = Double.parseDouble(resourceStores.getJSONObject(resIndex).getString("curStock"));
            if (miniStock < curStock) {
                throw new IllegalArgumentException("库存不足");
            }
            resourceStores.getJSONObject(resIndex).put("resName", userStorehouseDtos.get(0).getResName());
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
        //获取调拨返还状态标识
        String applyType = reqJson.getString("apply_type");
        // 查询用户名称
        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("userId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        reqJson.put("userName", userDtos.get(0).getName());
        //封装调拨对象
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = covertAllocationStorehouseApply(reqJson);
        //todo 默认写0 后面 相加
        allocationStorehouseApplyPo.setApplyCount("0");
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        JSONObject resObj = null;
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            //处理 物品信息
            resObj = resourceStores.getJSONObject(resIndex);
            //todo 记录明细
            saveAllocationStorehouse(reqJson, applyType, allocationStorehouseApplyPo, resObj);
            //todo 退还 时 源仓库加库存
            if (!AllocationStorehouseApplyDto.STORE_TYPE_ALLOCATION.equals(applyType)) {
                addAResourceStore(reqJson, allocationStorehouseApplyPo, resObj);
            }
        }
        flag = allocationStorehouseApplyV1InnerServiceSMOImpl.saveAllocationStorehouseApply(allocationStorehouseApplyPo);
        if (flag < 1) {
            throw new CmdException("保存修改物品失败");
        }
        if (!AllocationStorehouseApplyDto.STORE_TYPE_ALLOCATION.equals(applyType)) {
            return;
        }
        // todo 开启流程
        toStartWorkflow(allocationStorehouseApplyPo, reqJson);
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 启动审批流程
     *
     * @param allocationStorehouseApplyPo
     */
    private void toStartWorkflow(AllocationStorehouseApplyPo allocationStorehouseApplyPo, JSONObject reqJson) {
        //todo 查询仓库是否存在
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShId(reqJson.getString("shId"));
        List<StorehouseDto> storehouseDtos = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
        Assert.listOnlyOne(storehouseDtos, "仓库不存在");
        if (!StorehouseDto.SWITCH_ON.equals(storehouseDtos.get(0).getAllocationSwitch())) {
            //todo 直接调拨
            toAllocationStorehouse(allocationStorehouseApplyPo, storehouseDto, reqJson);
            return;
        }
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(allocationStorehouseApplyPo.getStoreId());
        oaWorkflowDto.setFlowId(storehouseDtos.get(0).getAllocationFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        //todo 提交审核
        JSONObject flowJson = new JSONObject();
        flowJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        flowJson.put("createUserId", allocationStorehouseApplyPo.getStartUserId());
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        flowJson.put("id", allocationStorehouseApplyPo.getApplyId());
        flowJson.put("auditMessage", "提交审核");
        flowJson.put("storeId", allocationStorehouseApplyPo.getStoreId());
        reqJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        JSONObject result = oaWorkflowActivitiInnerServiceSMOImpl.startProcess(flowJson);
        JSONObject audit = reqJson.getJSONObject("audit");
        String nextUserId = "-1";
        if (audit != null) {
            String staffId = reqJson.getJSONObject("audit").getString("staffId");
            if (!StringUtil.isEmpty(staffId)) {
                nextUserId = reqJson.getJSONObject("audit").getString("staffId");
            } else {
                nextUserId = reqJson.getJSONObject("audit").getString("assignee");
            }
        }
        //提交者提交
        flowJson = new JSONObject();
        flowJson.put("processInstanceId", result.getString("processInstanceId"));
        flowJson.put("createUserId", allocationStorehouseApplyPo.getStartUserId());
        flowJson.put("nextUserId", nextUserId);
        flowJson.put("storeId", allocationStorehouseApplyPo.getStoreId());
        flowJson.put("id", allocationStorehouseApplyPo.getApplyId());
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowActivitiInnerServiceSMOImpl.autoFinishFirstTask(flowJson);
    }

    /**
     * 直接调拨
     *
     * @param allocationStorehouseApplyPo
     * @param storehouseDto
     * @param reqJson
     */
    private void toAllocationStorehouse(AllocationStorehouseApplyPo allocationStorehouseApplyPo, StorehouseDto storehouseDto, JSONObject reqJson) {
        //查询调拨记录
        AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
        allocationStorehouseDto.setApplyId(allocationStorehouseApplyPo.getApplyId());
        List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseV1InnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        if (allocationStorehouseDtos == null || allocationStorehouseDtos.size() < 1) {
            return;
        }
        //被调拨ApplyId
        String newApplyId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId);
        for (AllocationStorehouseDto tmpAllocationStorehouseDto : allocationStorehouseDtos) {
            double allocationStock = new Double(tmpAllocationStorehouseDto.getStock());
            //todo 每条记录调拨
            tmpAllocationStorehouseDto.setApplyIda(newApplyId);
            allocationBMOImpl.doToAllocationStorehouse(tmpAllocationStorehouseDto, allocationStock);
        }
        String applyId = allocationStorehouseApplyPo.getApplyId();
        AllocationStorehouseApplyPo tmpAllocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        tmpAllocationStorehouseApplyPo.setApplyId(applyId);
        //todo 如果包含taskId 流程提交下去
        if (reqJson.containsKey("taskId")) {
            reqJson.put("auditCode", "1100");
            reqJson.put("auditMessage", "调拨入库成功");
            reqJson.put("id", reqJson.getString("applyId"));
            reqJson.put("storeId", reqJson.getString("storeId"));
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                tmpAllocationStorehouseApplyPo.setState(AllocationStorehouseApplyDto.STATE_END);
            } else {
                tmpAllocationStorehouseApplyPo.setState(AllocationStorehouseApplyDto.STATE_DEALING);
            }
        } else {
            tmpAllocationStorehouseApplyPo.setState(AllocationStorehouseApplyDto.STATE_DEALING);
        }
        tmpAllocationStorehouseApplyPo.setStatusCd("0");
        allocationStorehouseApplyV1InnerServiceSMOImpl.updateAllocationStorehouseApply(tmpAllocationStorehouseApplyPo);

        //加入被调拨申请记录
        AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
        allocationStorehouseApplyDto.setApplyId(applyId);
        List<AllocationStorehouseApplyDto> allocationStorehouseApplyDtoList = allocationStorehouseApplyV1InnerServiceSMOImpl.queryAllocationStorehouseApplys(allocationStorehouseApplyDto);
        AllocationStorehouseApplyDto allocationStorehouseApplyDto1 = allocationStorehouseApplyDtoList.get(0);
        allocationStorehouseApplyDto1.setApplyId(newApplyId);
        allocationStorehouseApplyDto1.setApplyType("40000");//调拨记录
        allocationStorehouseApplyDto1.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        //查询被调拨仓库归属小区
        String shId = storehouseDto.getShId();
        StorehouseDto storehouseDto1 = new StorehouseDto();
        storehouseDto1.setShId(shId);
        List<StorehouseDto> storehouseList = storehouseInnerServiceSMOImpl.queryStorehouses(storehouseDto);
        allocationStorehouseApplyDto1.setCommunityId(storehouseList.get(0).getCommunityId());
        allocationStorehouseApplyDto1.setbId("-1");
        allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto1);
    }

    /**
     * 源仓库物品加库存
     *
     * @param reqJson
     * @param allocationStorehouseApplyPo
     * @param resObj
     */
    private void addAResourceStore(JSONObject reqJson, AllocationStorehouseApplyPo allocationStorehouseApplyPo, JSONObject resObj) {
        int flag;
        UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
        userStorehouseDto.setResId(resObj.getString("resId"));
        userStorehouseDto.setUserId(reqJson.getString("userId"));
        //查询个人物品信息
        List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
        Assert.listOnlyOne(userStorehouseDtos, "查询当前用户个人物品信息错误！");
        UserStorehousePo userStorehousePo = new UserStorehousePo();
        //获取原最小计量单位数量
        if (StringUtil.isEmpty(resObj.getString("miniUnitStock"))) {
            throw new IllegalArgumentException("最小计量单位数量不能为空！");
        }
        BigDecimal miniUnitStock = new BigDecimal(resObj.getString("miniUnitStock")); //获取最小计量单位数量
        //获取原最小计量总数
        if (StringUtil.isEmpty(resObj.getString("miniStock"))) {
            throw new IllegalArgumentException("最小计量总数不能为空！");
        }
        BigDecimal miniStock = new BigDecimal(resObj.getString("miniStock")); //获取最小计量总数
        //获取物品单位
        if (StringUtil.isEmpty(userStorehouseDtos.get(0).getUnitCode())) {
            throw new IllegalArgumentException("物品单位不能为空！");
        }
        String unitCode = userStorehouseDtos.get(0).getUnitCode(); //获取物品单位
        //获取物品最小计量单位
        if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniUnitCode())) {
            throw new IllegalArgumentException("物品最小计量单位不能为空！");
        }
        String miniUnitCode = userStorehouseDtos.get(0).getMiniUnitCode(); //获取最小计量单位
        //计算个人物品剩余的最小计量总数
        BigDecimal curStockNew = new BigDecimal(resObj.getString("curStock")); //退还的数量
        BigDecimal curStock = miniStock.subtract(curStockNew).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (unitCode.equals(miniUnitCode)) { //物品单位与最小计量单位相同时
            userStorehousePo.setStock(String.valueOf(curStock));
        } else { //物品单位与最小计量单位不同时,四舍五入保留两位小数
            //计算个人物品剩余的库存
            BigDecimal newMiniStock = curStock.divide(miniUnitStock, 2, BigDecimal.ROUND_HALF_UP);
            userStorehousePo.setStock(String.valueOf(newMiniStock));
        }
        userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
        userStorehousePo.setMiniStock(String.valueOf(curStock));
        flag = userStorehouseV1InnerServiceSMOImpl.updateUserStorehouse(userStorehousePo);
        if (flag < 1) {
            throw new CmdException("保存修改物品失败");
        }
        //todo 退还数量
        allocationStorehouseApplyPo.setApplyCount(resObj.getString("curStock"));
        //todo 返还的仓库物品加上库存
        ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
        resourceStoreDto.setResCode(resObj.getString("resCode"));
        resourceStoreDto.setShId(resObj.getString("shzId"));
        //todo 查询目标仓库下该物品信息(根据目标仓库和物品编码查询)
        List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
        if (resourceStoreDtos != null && resourceStoreDtos.size() > 0) { //如果目标仓库下有这个物品信息，就更新该物品库存
            for (ResourceStoreDto resourceStore : resourceStoreDtos) {
                ResourceStorePo resourceStorePo = new ResourceStorePo();
                //获取目标仓库下该物品的原有最小计量总数
                if (StringUtil.isEmpty(resourceStore.getMiniStock())) {
                    throw new IllegalArgumentException("最小计量总数不能为空！");
                }
                //double oldMiniStock = Double.parseDouble(resourceStore.getMiniStock());
                //计算返还后总的最小计量总数
                BigDecimal num1 = new BigDecimal(resourceStore.getMiniStock());
                BigDecimal num2 = new BigDecimal(resObj.getString("curStock"));
                BigDecimal allMiniStock = num1.add(num2).setScale(2, BigDecimal.ROUND_HALF_UP);
                //获取最小计量单位数量
                if (StringUtil.isEmpty(resourceStore.getMiniUnitStock())) {
                    throw new IllegalArgumentException("最小计量单位数量不能为空！");
                }
                BigDecimal miniUnitStock1 = new BigDecimal(resourceStore.getMiniUnitStock());
                //获取物品单位
                if (StringUtil.isEmpty(resourceStore.getUnitCode())) {
                    throw new IllegalArgumentException("物品单位不能为空！");
                }
                String unitCode1 = resourceStore.getUnitCode();
                //获取物品最小计量单位
                if (StringUtil.isEmpty(resourceStore.getMiniUnitCode())) {
                    throw new IllegalArgumentException("物品最小计量单位不能为空！");
                }
                String miniUnitCode1 = resourceStore.getMiniUnitCode();
                if (unitCode1.equals(miniUnitCode1)) { //物品单位与物品最小计量单位相同时
                    resourceStorePo.setStock(String.valueOf(allMiniStock));
                } else { //物品单位与物品最小计量单位不同时,四舍五入保留两位小数
                    //计算返还后物品资源库存
                    BigDecimal newStock = allMiniStock.divide(miniUnitStock1, 2, BigDecimal.ROUND_HALF_UP);
                    resourceStorePo.setStock(String.valueOf(newStock));
                }
                resourceStorePo.setResId(resourceStore.getResId());
                resourceStorePo.setMiniStock(String.valueOf(allMiniStock));
                flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);
                if (flag < 1) {
                    throw new CmdException("保存修改物品失败");
                }
                // 保存至 物品 times表
                //查询调拨批次价格
                ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
                resourceStoreTimesDto.setTimesId(resObj.getString("timesId"));
                List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
                ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
                resourceStoreTimesPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId("10"));
                resourceStoreTimesPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
                if (unitCode1.equals(miniUnitCode1)) { //物品单位与物品最小计量单位相同时
                    resourceStoreTimesPo.setStock(resObj.getString("curStock"));
                } else { //物品单位与物品最小计量单位不同时,四舍五入保留两位小数
                    //计算返还后物品资源库存
                    BigDecimal curStock1 = new BigDecimal(resObj.getString("curStock"));
                    BigDecimal divide = curStock1.divide(miniUnitStock1);
                    resourceStoreTimesPo.setStock(String.valueOf(divide));
                }
                resourceStoreTimesPo.setResCode(resObj.getString("resCode"));
                resourceStoreTimesPo.setStoreId(reqJson.getString("storeId"));
                resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
                resourceStoreTimesPo.setShId(resObj.getString("shzId"));
                resourceStoreTimesPo.setCommunityId(reqJson.getString("communityId"));
                resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
                ResourceStoreUseRecordPo resourceStoreUseRecordPo = new ResourceStoreUseRecordPo();
                resourceStoreUseRecordPo.setRsurId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rsurId));
                resourceStoreUseRecordPo.setRepairId("-1");
                resourceStoreUseRecordPo.setResId(resourceStore.getResId());
                resourceStoreUseRecordPo.setCommunityId(reqJson.getString("communityId"));
                resourceStoreUseRecordPo.setStoreId(reqJson.getString("storeId"));
                resourceStoreUseRecordPo.setQuantity(resObj.getString("curStock"));
                resourceStoreUseRecordPo.setUnitPrice(resourceStoreTimesDtos.get(0).getPrice());
                resourceStoreUseRecordPo.setCreateUserId(reqJson.getString("userId"));
                resourceStoreUseRecordPo.setCreateUserName(reqJson.getString("userName"));
                resourceStoreUseRecordPo.setRemark("个人物品退还仓库");
                resourceStoreUseRecordPo.setResourceStoreName(resourceStore.getResName());
                resourceStoreUseRecordPo.setState("4004"); //1001 报废回收   2002 工单损耗   3003 公用损耗  4004 退还仓库
                resourceStoreUseRecordV1InnerServiceSMOImpl.saveResourceStoreUseRecord(resourceStoreUseRecordPo);
            }
        } else { //如果目标仓库下没有这个物品信息，就插入一条物品信息
            ResourceStoreDto resourceStore = new ResourceStoreDto();
            resourceStore.setResId(resObj.getString("resId"));
            //根据资源id查询物品信息
            List<ResourceStoreDto> resourceStoreList = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStore);
            Assert.listOnlyOne(resourceStoreList, "资源表没有该物品信息！");
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_resId));
            resourceStorePo.setResName(resourceStoreList.get(0).getResName());
            resourceStorePo.setStoreId(reqJson.getString("storeId"));
            resourceStorePo.setResCode(resourceStoreList.get(0).getResCode());
            resourceStorePo.setMiniStock(resObj.getString("curStock"));
            resourceStorePo.setShId(resObj.getString("shzId"));
            resourceStorePo.setPrice(resourceStoreList.get(0).getPrice());
            resourceStorePo.setDescription("");
            resourceStorePo.setUnitCode(resourceStoreList.get(0).getUnitCode());
            resourceStorePo.setOutLowPrice(resourceStoreList.get(0).getOutLowPrice());
            resourceStorePo.setOutHighPrice(resourceStoreList.get(0).getOutHighPrice());
            resourceStorePo.setShowMobile(resourceStoreList.get(0).getShowMobile());
            resourceStorePo.setWarningStock(resourceStoreList.get(0).getWarningStock());
            resourceStorePo.setAveragePrice(resourceStoreList.get(0).getAveragePrice());
            resourceStorePo.setRstId(resourceStoreList.get(0).getRstId());
            resourceStorePo.setParentRstId(resourceStoreList.get(0).getParentRstId());
            resourceStorePo.setRssId(resourceStoreList.get(0).getRssId());
            resourceStorePo.setIsFixed(resourceStoreList.get(0).getIsFixed());
            if (StringUtil.isEmpty(resourceStoreList.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            resourceStorePo.setMiniUnitStock(resourceStoreList.get(0).getMiniUnitStock());
            if (StringUtil.isEmpty(resourceStoreList.get(0).getMiniUnitCode())) {
                throw new IllegalArgumentException("最小计量单位不能为空！");
            }
            resourceStorePo.setMiniUnitCode(resourceStoreList.get(0).getMiniUnitCode());
            if (resourceStorePo.getUnitCode().equals(resourceStorePo.getMiniUnitCode())) { //如果物品单位与物品最小计量单位相同，就不向上取整
                //单位相同，物品库存就等于物品最小计量总数
                resourceStorePo.setStock(resourceStorePo.getMiniStock());
            } else { //如果物品单位与物品最小计量单位不相同，就向上取整
                //计算物品库存
                BigDecimal num1 = new BigDecimal(resourceStorePo.getMiniStock());
                BigDecimal num2 = new BigDecimal(resourceStorePo.getMiniUnitStock());
                BigDecimal newStock = num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP);
                resourceStorePo.setStock(String.valueOf(newStock));
            }
            resourceStorePo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            StorehouseDto storehouseDto = new StorehouseDto();
            storehouseDto.setShId(resourceStoreList.get(0).getShId());
            List<StorehouseDto> storehouseDtoList = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
            String storehouseCommunityId = "";
            if (storehouseDtoList.size() > 0) {
                storehouseCommunityId = storehouseDtoList.get(0).getCommunityId();
            }
            resourceStorePo.setCommunityId(storehouseCommunityId);
            //退还仓库中保存一条物品记录
            flag = resourceStoreV1InnerServiceSMOImpl.saveResourceStore(resourceStorePo);
            if (flag < 1) {
                throw new CmdException("保存修改物品失败");
            }
            //todo 保存至 物品 times表
            //查询调拨批次价格
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(resObj.getString("timesId"));
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
            if (resourceStorePo.getUnitCode().equals(resourceStorePo.getMiniUnitCode())) { //如果物品单位与物品最小计量单位相同，就不向上取整
                //单位相同，物品库存就等于物品最小计量总数
                resourceStoreTimesPo.setStock(resObj.getString("curStock"));
            } else { //如果物品单位与物品最小计量单位不相同，就向上取整
                //计算物品库存
                BigDecimal curStock1 = new BigDecimal(resObj.getString("curStock"));
                BigDecimal miniUnitStock1 = new BigDecimal(resourceStorePo.getMiniUnitStock());
                BigDecimal divide = curStock1.divide(miniUnitStock1);
                resourceStoreTimesPo.setStock(String.valueOf(divide));
            }
            resourceStoreTimesPo.setResCode(resourceStoreList.get(0).getResCode());
            resourceStoreTimesPo.setStoreId(reqJson.getString("storeId"));
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setShId(resObj.getString("shzId"));
            resourceStoreTimesPo.setCommunityId(storehouseCommunityId);
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
            ResourceStoreUseRecordPo resourceStoreUseRecordPo = new ResourceStoreUseRecordPo();
            resourceStoreUseRecordPo.setRsurId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rsurId));
            resourceStoreUseRecordPo.setRepairId("-1");
            resourceStoreUseRecordPo.setResId(resourceStoreList.get(0).getResId());
            resourceStoreUseRecordPo.setCommunityId(reqJson.getString("communityId"));
            resourceStoreUseRecordPo.setStoreId(reqJson.getString("storeId"));
            resourceStoreUseRecordPo.setQuantity(resObj.getString("curStock"));
            resourceStoreUseRecordPo.setUnitPrice(resourceStoreTimesDtos.get(0).getPrice());
            resourceStoreUseRecordPo.setCreateUserId(reqJson.getString("userId"));
            resourceStoreUseRecordPo.setCreateUserName(reqJson.getString("userName"));
            resourceStoreUseRecordPo.setRemark("个人物品退还仓库");
            resourceStoreUseRecordPo.setResourceStoreName(resourceStoreList.get(0).getResName());
            resourceStoreUseRecordPo.setState("4004"); //1001 报废回收   2002 工单损耗   3003 公用损耗  4004 退还仓库
            resourceStoreUseRecordV1InnerServiceSMOImpl.saveResourceStoreUseRecord(resourceStoreUseRecordPo);
        }
    }

    /**
     * 原仓库 物品减去库存
     *
     * @param reqJson
     * @param allocationStorehouseApplyPo
     * @param resObj
     */
    private void subAResourceStore(JSONObject reqJson, AllocationStorehouseApplyPo allocationStorehouseApplyPo, JSONObject resObj) {
        int flag;
        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + resObj.getString("resId");
        try {
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(resObj.getString("resId"));
            resourceStorePo.setStoreId(reqJson.getString("storeId"));
            resourceStorePo.setShId(resObj.getString("shId"));
            BigDecimal stockA = new BigDecimal(resObj.getString("stock"));//现有库存
            BigDecimal stockB = new BigDecimal(resObj.getString("curStock"));//调拨数量
            if (stockA.compareTo(stockB) < 0) {
                throw new IllegalArgumentException("库存不足！");
            }
            resourceStorePo.setStock((stockA.subtract(stockB)).toString());
            //计算当前调拨最小计量总数
            if (StringUtil.isEmpty(resObj.getString("miniStock"))) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            BigDecimal miniStock = new BigDecimal(resObj.getString("miniStock")); //调拨前的最小计量总数
            if (StringUtil.isEmpty(resObj.getString("miniUnitStock"))) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            BigDecimal miniUnitStock = new BigDecimal(resObj.getString("miniUnitStock")); //最小计量单位数量
            BigDecimal curStockNew = new BigDecimal(resObj.getString("curStock"));
            BigDecimal curStock = miniUnitStock.multiply(curStockNew); //当前调拨的最小计量总数
            BigDecimal newMiniStock = miniStock.subtract(curStock); //调拨后剩余的最小计量总数
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            flag = resourceStoreV1InnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            if (flag < 1) {
                throw new CmdException("保存修改物品失败");
            }
            BigDecimal oldCurStore = new BigDecimal(allocationStorehouseApplyPo.getApplyCount());
            oldCurStore = oldCurStore.add(new BigDecimal(resObj.getString("curStock")));
            allocationStorehouseApplyPo.setApplyCount(oldCurStore.toString());
            //加入 从库存中扣减
            subResourceStoreTimesStock(resObj);
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }
    }

    private void saveAllocationStorehouse(JSONObject reqJson, String applyType, AllocationStorehouseApplyPo allocationStorehouseApplyPo, JSONObject resObj) {
        AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
        allocationStorehousePo.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
        allocationStorehousePo.setApplyId(allocationStorehouseApplyPo.getApplyId());
        allocationStorehousePo.setResId(resObj.getString("resId"));
        allocationStorehousePo.setResName(resObj.getString("resName"));
        if (AllocationStorehouseApplyDto.STORE_TYPE_ALLOCATION.equals(applyType)) { //调拨操作时保存前仓库id
            allocationStorehousePo.setShIda(resObj.getString("shId"));
            allocationStorehouseApplyPo.setShId(resObj.getString("shId"));
        } else {  //返还操作时保存返还申请人id
            allocationStorehousePo.setShIda(allocationStorehouseApplyPo.getStartUserId());
        }
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
        applyCount += Double.parseDouble(resObj.getString("curStock"));
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
        allocationStorehouseApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
        allocationStorehouseApplyPo.setApplyCount("0");
        allocationStorehouseApplyPo.setRemark(reqJson.getString("remark"));
        allocationStorehouseApplyPo.setStartUserId(reqJson.getString("userId"));
        allocationStorehouseApplyPo.setStartUserName(reqJson.getString("userName"));
        allocationStorehouseApplyPo.setStoreId(reqJson.getString("storeId"));
        if (AllocationStorehouseApplyDto.STORE_TYPE_ALLOCATION.equals(reqJson.getString("apply_type"))) { //调拨操作有状态，返还没有
            allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_APPLY);
        } else {
            allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_RETURN); //已退还状态
        }
        allocationStorehouseApplyPo.setApplyType(reqJson.getString("apply_type")); //调拨返还状态标识
        allocationStorehouseApplyPo.setCommunityId(reqJson.getString("communityId"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        allocationStorehouseApplyPo.setCreateTime(format.format(new Date()));
        return allocationStorehouseApplyPo;
    }

    /**
     * 从times中扣减
     *
     * @param resObj
     */
    private void subResourceStoreTimesStock(JSONObject resObj) {
        String applyQuantity = resObj.getString("curStock");
        ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
        resourceStoreTimesDto.setResCode(resObj.getString("resCode"));
        resourceStoreTimesDto.setTimesId(resObj.getString("timesId"));
        List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
        if (resourceStoreTimesDtos == null || resourceStoreTimesDtos.size() < 1) {
            return;
        }
        int stock = 0;
        int quantity = Integer.parseInt(applyQuantity);
        ResourceStoreTimesPo resourceStoreTimesPo = null;
        stock = Integer.parseInt(resourceStoreTimesDtos.get(0).getStock());
        if (stock < quantity) {
            throw new CmdException(resourceStoreTimesDtos.get(0).getResCode() + "价格为：" + resourceStoreTimesDtos.get(0).getPrice() + "的库存" + resourceStoreTimesDtos.get(0).getStock() + ",库存不足");
        }
        stock = stock - quantity;
        resourceStoreTimesPo = new ResourceStoreTimesPo();
        resourceStoreTimesPo.setTimesId(resourceStoreTimesDtos.get(0).getTimesId());
        resourceStoreTimesPo.setStock(stock + "");
        resourceStoreTimesV1InnerServiceSMOImpl.updateResourceStoreTimes(resourceStoreTimesPo);
    }
}
