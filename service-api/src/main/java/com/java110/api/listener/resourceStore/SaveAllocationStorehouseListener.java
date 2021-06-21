package com.java110.api.listener.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.allocationStorehouseApply.AllocationStorehouseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.common.IAllocationStorehouseUserInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.po.allocationStorehouse.AllocationStorehousePo;
import com.java110.po.allocationStorehouseApply.AllocationStorehouseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodeAllocationStorehouseConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 保存商户侦听
 * add by wuxw 2019-06-30
 */
@Java110Listener("saveAllocationStorehouseListener")
public class SaveAllocationStorehouseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseUserInnerServiceSMO allocationStorehouseUserInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
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
        //校验 物品是否存在
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            if (!StringUtil.isEmpty(applyType) && applyType.equals("10000")) {  //调拨
                ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
                resourceStoreDto.setStoreId(reqJson.getString("storeId"));
                resourceStoreDto.setResId(resourceStores.getJSONObject(resIndex).getString("resId"));
                resourceStoreDto.setShId(resourceStores.getJSONObject(resIndex).getString("shId"));
                List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
                Assert.listOnlyOne(resourceStoreDtos, "未包含 物品信息");
                int stockA = Integer.parseInt(resourceStoreDtos.get(0).getStock());
                int stockB = Integer.parseInt(resourceStores.getJSONObject(resIndex).getString("curStock"));
                if (stockA < stockB) {
                    throw new IllegalArgumentException("库存不足");
                }
                resourceStores.getJSONObject(resIndex).put("resName", resourceStoreDtos.get(0).getResName());
                resourceStores.getJSONObject(resIndex).put("stockA", stockA);
            } else { //返还
                UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                userStorehouseDto.setResId(resourceStores.getJSONObject(resIndex).getString("resId"));
                userStorehouseDto.setUserId(reqJson.getString("userId"));
                List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
                Assert.listOnlyOne(userStorehouseDtos, "未包含 物品信息");
                int stockA = Integer.parseInt(userStorehouseDtos.get(0).getStock());
                int stockB = Integer.parseInt(resourceStores.getJSONObject(resIndex).getString("curStock"));
                if (stockA < stockB) {
                    throw new IllegalArgumentException("库存不足");
                }
                resourceStores.getJSONObject(resIndex).put("resName", userStorehouseDtos.get(0).getResName());
                resourceStores.getJSONObject(resIndex).put("stockA", stockA);
            }
        }
        reqJson.put("resourceStores", resourceStores);
//        reqJson.put("resName", resourceStoreDtos.get(0).getResName());
//        reqJson.put("stockA", stockA);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        //获取调拨返还状态标识
        String applyType = reqJson.getString("apply_type");
        AllocationStorehouseApplyPo allocationStorehouseApplyPo = new AllocationStorehouseApplyPo();
        allocationStorehouseApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
        allocationStorehouseApplyPo.setApplyCount("0");
        allocationStorehouseApplyPo.setRemark(reqJson.getString("remark"));
        allocationStorehouseApplyPo.setStartUserId(reqJson.getString("userId"));
        allocationStorehouseApplyPo.setStartUserName(reqJson.getString("userName"));
        allocationStorehouseApplyPo.setStoreId(reqJson.getString("storeId"));
        if (!StringUtil.isEmpty(applyType) && applyType.equals("10000")) { //调拨操作有状态，返还没有
            allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_APPLY);
        } else {
            allocationStorehouseApplyPo.setState(AllocationStorehouseDto.STATE_RETURN); //已退还状态
        }
        allocationStorehouseApplyPo.setApplyType(applyType); //调拨返还状态标识
        allocationStorehouseApplyPo.setCommunityId(reqJson.getString("communityId"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        allocationStorehouseApplyPo.setCreateTime(format.format(new Date()));
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        JSONObject resObj = null;
        for (int resIndex = 0; resIndex < resourceStores.size(); resIndex++) {
            resObj = resourceStores.getJSONObject(resIndex);
            AllocationStorehousePo allocationStorehousePo = new AllocationStorehousePo();
            allocationStorehousePo.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
            allocationStorehousePo.setApplyId(allocationStorehouseApplyPo.getApplyId());
            allocationStorehousePo.setResId(resObj.getString("resId"));
            allocationStorehousePo.setResName(resObj.getString("resName"));
            if (!StringUtil.isEmpty(applyType) && applyType.equals("10000")) { //调拨操作时保存前仓库id
                allocationStorehousePo.setShIda(resObj.getString("shId"));
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
            super.insert(context, allocationStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE);

            if (!StringUtil.isEmpty(applyType) && applyType.equals("10000")) { //调拨减去库存
                ResourceStorePo resourceStorePo = new ResourceStorePo();
                resourceStorePo.setResId(resObj.getString("resId"));
                resourceStorePo.setStoreId(reqJson.getString("storeId"));
                resourceStorePo.setShId(resObj.getString("shId"));
                int stockA = Integer.parseInt(resObj.getString("stock"));//现有库存
                int stockB = Integer.parseInt(resObj.getString("curStock"));//调拨数量
                resourceStorePo.setStock((stockA - stockB) + "");
                super.update(context, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
                int oldCurStore = Integer.parseInt(allocationStorehouseApplyPo.getApplyCount());
                oldCurStore += Integer.parseInt(resObj.getString("curStock"));
                allocationStorehouseApplyPo.setApplyCount(oldCurStore + "");
            } else {
                UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                userStorehouseDto.setResId(resObj.getString("resId"));
                userStorehouseDto.setUserId(reqJson.getString("userId"));
                //查询个人物品信息
                List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
                Assert.listOnlyOne(userStorehouseDtos, "查询当前用户个人物品信息错误！");
                int stockA = Integer.parseInt(resObj.getString("stock"));//现有库存
                int stockB = Integer.parseInt(resObj.getString("curStock"));//返还数量
                UserStorehousePo userStorehousePo = new UserStorehousePo();
                userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
                userStorehousePo.setStock((stockA - stockB) + "");
                super.update(context, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
                //退还数量
                allocationStorehouseApplyPo.setApplyCount(resObj.getString("curStock"));
                //返还的仓库物品加上库存
                ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
                resourceStoreDto.setResCode(resObj.getString("resCode"));
                resourceStoreDto.setShId(resObj.getString("shzId"));
                //查询目标仓库下该物品信息(根据目标仓库和物品编码查询)
                List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
                if (resourceStoreDtos != null && resourceStoreDtos.size() > 0) { //如果目标仓库下有这个物品信息，就更新该物品库存
                    for (ResourceStoreDto resourceStore : resourceStoreDtos) {
                        ResourceStorePo resourceStorePo = new ResourceStorePo();
                        //获取目标仓库下该物品的原有库存数
                        int stock = Integer.parseInt(resourceStore.getStock());
                        resourceStorePo.setResId(resourceStore.getResId());
                        resourceStorePo.setStock((stock + stockB) + "");
                        super.update(context, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
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
                    resourceStorePo.setStock(stockB + "");
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
                    resourceStorePo.setRssId(resourceStoreList.get(0).getRssId());
                    resourceStorePo.setCreateTime(format.format(new Date()));
                    super.insert(context, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE);
                }
            }
        }
        super.insert(context, allocationStorehouseApplyPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_STOREHOUSE_APPLY);
        commit(context);

        ResponseEntity<String> responseEntity = context.getResponseEntity();

        //开始流程
        if (HttpStatus.OK == responseEntity.getStatusCode() && !StringUtil.isEmpty(applyType) && applyType.equals("10000")) {
            AllocationStorehouseApplyDto allocationStorehouseDto = BeanConvertUtil.covertBean(allocationStorehouseApplyPo, AllocationStorehouseApplyDto.class);
            allocationStorehouseDto.setCurrentUserId(reqJson.getString("userId"));
            allocationStorehouseUserInnerServiceSMOImpl.startProcess(allocationStorehouseDto);
        }
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeAllocationStorehouseConstant.ADD_ALLOCATIONSTOREHOUSE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

}
