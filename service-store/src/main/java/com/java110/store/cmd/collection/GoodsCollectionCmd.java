package com.java110.store.cmd.collection;

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
import com.java110.dto.purchase.PurchaseApplyDetailDto;
import com.java110.dto.purchase.PurchaseApplyDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.dto.store.StorehouseDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.oa.IOaWorkflowInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.po.user.UserStorehousePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品领用申请
 */
@Java110Cmd(serviceCode = "/collection/goodsCollection")
public class GoodsCollectionCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IStorehouseV1InnerServiceSMO storehouseV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowActivitiInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写物品领用的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "shId", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "endUserName", "必填，请填写采购联系人");
        Assert.hasKeyAndValue(reqJson, "endUserTel", "必填，请填写采购联系电话");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        String storeId = CmdContextUtils.getStoreId(context);
        if (resourceStores == null || resourceStores.size() < 1) {
            throw new IllegalArgumentException("未包含领用物品");
        }
        double quanitity = 0;
        double stock = 0;
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            Assert.hasKeyAndValue(resourceStore, "timesId", "必填，未选择价格");
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(resourceStore.getString("timesId"));
            resourceStoreTimesDto.setStoreId(storeId);
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            Assert.listOnlyOne(resourceStoreTimesDtos, "价格不存在");
            if(StringUtil.isEmpty(resourceStore.getString("quantity"))){
                throw new IllegalArgumentException("申请数量不能为空");
            }
            quanitity = Double.parseDouble(resourceStore.getString("quantity"));
            if (quanitity <= 0) {
                throw new IllegalArgumentException("申请数量不正确");
            }
            stock = Double.parseDouble(resourceStoreTimesDtos.get(0).getStock());
            if (quanitity > stock) {
                throw new IllegalArgumentException("该批次价格下库存数量不足！");
            }
            resourceStore.put("resourceStoreTimesDtos", resourceStoreTimesDtos);
        }
        //todo 查询仓库是否存在
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShId(reqJson.getString("shId"));
        List<StorehouseDto> storehouseDtos = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
        Assert.listOnlyOne(storehouseDtos, "仓库不存在");
        if (!StorehouseDto.SWITCH_ON.equals(storehouseDtos.get(0).getUseSwitch())) {
            return;
        }
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(storeId);
        oaWorkflowDto.setFlowId(storehouseDtos.get(0).getUseFlowId());
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
     * 物品领用申请-发起
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = context.getReqHeaders().get("store-id");
        String userId = context.getReqHeaders().get("user-id");
        String acceptStaffId = reqJson.getString("acceptStaffId");
        if (!StringUtil.isEmpty(acceptStaffId)) {
            userId = acceptStaffId;
        }
        //todo 查询仓库是否存在
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShId(reqJson.getString("shId"));
        List<StorehouseDto> storehouseDtos = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
        Assert.listOnlyOne(storehouseDtos, "仓库不存在");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "未包含用户");
        String userName = userDtos.get(0).getName();
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
        purchaseApplyPo.setDescription(reqJson.getString("description"));
        purchaseApplyPo.setUserId(userId);
        purchaseApplyPo.setUserName(userName);
        purchaseApplyPo.setEndUserName(reqJson.getString("endUserName"));
        purchaseApplyPo.setEndUserTel(reqJson.getString("endUserTel"));
        purchaseApplyPo.setStoreId(storeId);
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_WAIT_DEAL);
        purchaseApplyPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_APPLY);
        //直接出库不走OA流程
        if (!StringUtil.isEmpty(reqJson.getString("useSwitch")) && StorehouseDto.SWITCH_OFF.equals(reqJson.getString("useSwitch"))) {
            purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT);
        }
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        //todo 封装物品
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        List<ResourceStoreTimesDto> resourceStoreTimesDtos = null;
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            resourceStoreTimesDtos = (List<ResourceStoreTimesDto>) resourceStore.get("resourceStoreTimesDtos");
            resourceStore.put("originalStock", resourceStore.get("stock"));
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
            purchaseApplyDetailPo.setTimesId(resourceStoreTimesDtos.get(0).getTimesId());
            purchaseApplyDetailPo.setOriginalStock(resourceStoreTimesDtos.get(0).getStock());
            purchaseApplyDetailPo.setPurchaseQuantity(resourceStore.getString("quantity"));
            //todo 获取批次采购参考价格
            String consultPrice = null;
            JSONArray timeList = resourceStore.getJSONArray("times");
            if (resourceStore.containsKey("timesId") && !StringUtil.isEmpty(resourceStore.getString("timesId"))) {
                for (int timesIndex = 0; timesIndex < timeList.size(); timesIndex++) {
                    JSONObject times = timeList.getJSONObject(timesIndex);
                    if (times.getString("timesId").toString().equals(resourceStore.getString("timesId").toString())) {
                        consultPrice = times.getString("price");
                    }
                }
            }
            purchaseApplyDetailPo.setConsultPrice(consultPrice);
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);
        if (saveFlag < 1) {
            throw new CmdException("物品领用申请失败");
        }
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
        //直接出库不走OA流程
        if (!StringUtil.isEmpty(reqJson.getString("useSwitch")) && StorehouseDto.SWITCH_OFF.equals(reqJson.getString("useSwitch"))) {
            storehouseDtos.get(0).setUseSwitch(StorehouseDto.SWITCH_OFF);
        }
        //todo 启动审核流程
        toStartWorkflow(purchaseApplyDto, storehouseDtos.get(0), reqJson);
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "物品领用成功"));
    }

    /**
     * 启动审批流程
     *
     * @param purchaseApplyDto
     */
    private void toStartWorkflow(PurchaseApplyDto purchaseApplyDto, StorehouseDto storehouseDto, JSONObject reqJson) {
        if (!StorehouseDto.SWITCH_ON.equals(storehouseDto.getUseSwitch())) {
            //todo 直接入库
            toPurchaseOutStorehouse(purchaseApplyDto, storehouseDto, reqJson);
            return;
        }
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setStoreId(purchaseApplyDto.getStoreId());
        oaWorkflowDto.setFlowId(storehouseDto.getUseFlowId());
        List<OaWorkflowDto> oaWorkflowDtos = oaWorkflowInnerServiceSMOImpl.queryOaWorkflows(oaWorkflowDto);
        //todo 提交审核
        JSONObject flowJson = new JSONObject();
        flowJson.put("processDefinitionKey", oaWorkflowDtos.get(0).getProcessDefinitionKey());
        flowJson.put("createUserId", purchaseApplyDto.getCurrentUserId());
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        flowJson.put("id", purchaseApplyDto.getApplyOrderId());
        flowJson.put("auditMessage", "提交审核");
        flowJson.put("storeId", purchaseApplyDto.getStoreId());
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
        flowJson.put("createUserId", purchaseApplyDto.getCurrentUserId());
        flowJson.put("nextUserId", nextUserId);
        flowJson.put("storeId", purchaseApplyDto.getStoreId());
        flowJson.put("id", purchaseApplyDto.getApplyOrderId());
        flowJson.put("flowId", oaWorkflowDtos.get(0).getFlowId());
        oaWorkflowActivitiInnerServiceSMOImpl.autoFinishFirstTask(flowJson);
    }

    /**
     * 入库
     *
     * @param purchaseApplyDto
     * @param storehouseDto
     * @param reqJson
     */
    private void toPurchaseOutStorehouse(PurchaseApplyDto purchaseApplyDto, StorehouseDto storehouseDto, JSONObject reqJson) {
        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        purchaseApplyDetailDto.setApplyOrderId(purchaseApplyDto.getApplyOrderId());
        purchaseApplyDetailDto.setStoreId(purchaseApplyDto.getStoreId());
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        if (purchaseApplyDetailDtos == null || purchaseApplyDetailDtos.size() < 1) {
            return;
        }
        double stock = 0;
        for (PurchaseApplyDetailDto tmpPurchaseApplyDetailDto : purchaseApplyDetailDtos) {
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(tmpPurchaseApplyDetailDto.getResId());
            resourceStorePo.setPurchasePrice(tmpPurchaseApplyDetailDto.getPrice());
            resourceStorePo.setStock("-" + tmpPurchaseApplyDetailDto.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
            //查询物品资源信息
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(tmpPurchaseApplyDetailDto.getResId());
            resourceStoreDto.setShId(tmpPurchaseApplyDetailDto.getShId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            Assert.listOnlyOne(resourceStoreDtos, "查询物品资源信息错误！");
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            //获取最小计量单位数量
            BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock());
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            //获取采购前物品最小计量总数
//            BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock());
//            //计算采购的物品最小计量总数
//            BigDecimal purchaseQuantity = new BigDecimal(tmpPurchaseApplyDetailDto.getPurchaseQuantity());
//            BigDecimal purchaseMiniStock = purchaseQuantity.multiply(miniUnitStock);
//            //计算采购后物品最小计量总数
//            BigDecimal nowMiniStock = miniStock.subtract(purchaseMiniStock);
//            if (nowMiniStock.compareTo(BigDecimal.ZERO) == -1) {
//                throw new IllegalArgumentException("物品库存已经不足，请确认物品库存！");
//            }
//            resourceStorePo.setMiniStock(String.valueOf(nowMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            // 保存至 物品 times表
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(tmpPurchaseApplyDetailDto.getApplyOrderId());
            resourceStoreTimesPo.setPrice(tmpPurchaseApplyDetailDto.getPrice());
            resourceStoreTimesPo.setStock("-" + tmpPurchaseApplyDetailDto.getPurchaseQuantity());
            resourceStoreTimesPo.setResCode(resourceStoreDtos.get(0).getResCode());
            resourceStoreTimesPo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setShId(tmpPurchaseApplyDetailDto.getShId());
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
            //todo 个人仓库中添加
            addPersonStorehouse(purchaseApplyDto, resourceStoreDtos, tmpPurchaseApplyDetailDto);
        }
        //获取订单号
        PurchaseApplyPo purchaseApplyPo = null;
        purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(purchaseApplyDto.getApplyOrderId());
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_END);
        purchaseApplyPo.setStatusCd("0");
        purchaseApplyInnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);
    }

    /**
     * 向个人仓库中添加数据
     *
     * @param resourceStoreDtos
     */
    private void addPersonStorehouse(PurchaseApplyDto purchaseApplyDto, List<ResourceStoreDto> resourceStoreDtos, PurchaseApplyDetailDto purchaseApplyDetailDto) {
        //获取物品单位
        String unitCode = resourceStoreDtos.get(0).getUnitCode();
        //获取物品最小计量单位
        String miniUnitCode = resourceStoreDtos.get(0).getMiniUnitCode();
        //获取物品最小计量单位数量
        String miniUnitStock = resourceStoreDtos.get(0).getMiniUnitStock();
        //入库到个人仓库中
        UserStorehousePo userStorehousePo = new UserStorehousePo();
        userStorehousePo.setUsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
        userStorehousePo.setResId(resourceStoreDtos.get(0).getResId());
        userStorehousePo.setResCode(resourceStoreDtos.get(0).getResCode());
        userStorehousePo.setResName(resourceStoreDtos.get(0).getResName());
        userStorehousePo.setStoreId(resourceStoreDtos.get(0).getStoreId());
        userStorehousePo.setUserId(purchaseApplyDto.getUserId());
        userStorehousePo.setTimesId(purchaseApplyDetailDto.getTimesId());
        //查询物品 是否已经存在
        UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
        userStorehouseDto.setResCode(resourceStoreDtos.get(0).getResCode());
        userStorehouseDto.setUserId(purchaseApplyDto.getUserId());
        userStorehouseDto.setStoreId(resourceStoreDtos.get(0).getStoreId());
        List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
        if (userStorehouseDtos == null || userStorehouseDtos.size() < 1) {
            userStorehousePo.setStock(purchaseApplyDetailDto.getPurchaseQuantity());
            if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                //获取领取数量
                BigDecimal purchaseQuantity2 = new BigDecimal(purchaseApplyDetailDto.getPurchaseQuantity());
                BigDecimal miniUnitStock2 = new BigDecimal(miniUnitStock);
                //计算个人物品最小计量总数
                BigDecimal quantity = purchaseQuantity2.multiply(miniUnitStock2);
                userStorehousePo.setMiniStock(String.valueOf(quantity));
            } else {
                userStorehousePo.setMiniStock(purchaseApplyDetailDto.getPurchaseQuantity());
            }
            userStorehouseInnerServiceSMOImpl.saveUserStorehouses(userStorehousePo);
        } else {
            //获取个人物品领用后的库存
            BigDecimal purchaseQuantity3 = new BigDecimal(purchaseApplyDetailDto.getPurchaseQuantity());
            BigDecimal stock3 = new BigDecimal(userStorehouseDtos.get(0).getStock());
            BigDecimal total = purchaseQuantity3.add(stock3);
            userStorehousePo.setStock(total.toString());
            userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
            if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                //获取本次领取数量
                BigDecimal miniUnitStock3 = new BigDecimal(miniUnitStock);
                //计算本次领取的个人物品最小计量总数
                BigDecimal quantity = purchaseQuantity3.multiply(miniUnitStock3);
                BigDecimal miniStock = new BigDecimal(0);
                //获取个人物品原先的最小计量总数
                if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniStock())) {
                    throw new IllegalArgumentException("信息错误，个人物品最小计量总数不能为空！");
                } else {
                    miniStock = new BigDecimal(userStorehouseDtos.get(0).getMiniStock());
                }
                //计算领用后个人物品总的最小计量总数
                BigDecimal miniQuantity = quantity.add(miniStock);
                userStorehousePo.setMiniStock(String.valueOf(miniQuantity));
            } else {
                userStorehousePo.setMiniStock(String.valueOf(total));
            }
            userStorehouseInnerServiceSMOImpl.updateUserStorehouses(userStorehousePo);
        }
    }
}
