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

import java.math.BigDecimal;
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
                double stockA = Double.parseDouble(resourceStoreDtos.get(0).getStock());
                double stockB = Double.parseDouble(resourceStores.getJSONObject(resIndex).getString("curStock"));
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
                double stockA = Double.parseDouble(resObj.getString("stock"));//现有库存
                double stockB = Double.parseDouble(resObj.getString("curStock"));//调拨数量
                resourceStorePo.setStock((stockA - stockB) + "");
                //计算当前调拨最小计量总数
                if (StringUtil.isEmpty(resObj.getString("miniStock"))) {
                    throw new IllegalArgumentException("最小计量总数不能为空！");
                }
                String miniStock = resObj.getString("miniStock"); //调拨前的最小计量总数
                if (StringUtil.isEmpty(resObj.getString("miniUnitStock"))) {
                    throw new IllegalArgumentException("最小计量单位数量不能为空！");
                }
                String miniUnitStock = resObj.getString("miniUnitStock"); //最小计量单位数量
                double curStock = Double.parseDouble(resObj.getString("curStock")) * Double.parseDouble(miniUnitStock); //当前调拨的最小计量总数
                double newMiniStock = Double.parseDouble(miniStock) - curStock; //调拨后剩余的最小计量总数
                resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
                super.update(context, resourceStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_RESOURCE_STORE);
                double oldCurStore = Double.parseDouble(allocationStorehouseApplyPo.getApplyCount());
                oldCurStore += Double.parseDouble(resObj.getString("curStock"));
                allocationStorehouseApplyPo.setApplyCount(oldCurStore + "");
            } else {
                UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                userStorehouseDto.setResId(resObj.getString("resId"));
                userStorehouseDto.setUserId(reqJson.getString("userId"));
                //查询个人物品信息
                List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
                Assert.listOnlyOne(userStorehouseDtos, "查询当前用户个人物品信息错误！");
//              double stockA = Double.parseDouble(resObj.getString("stock"));//现有库存
//              double stockB = Double.parseDouble(resObj.getString("curStock"));//返还数量
                UserStorehousePo userStorehousePo = new UserStorehousePo();
                //获取原最小计量单位数量
                if (StringUtil.isEmpty(resObj.getString("miniUnitStock"))) {
                    throw new IllegalArgumentException("最小计量总数不能为空！");
                }
                double miniUnitStock = Double.parseDouble(resObj.getString("miniUnitStock"));
                //获取原最小计量总数
                if (StringUtil.isEmpty(resObj.getString("miniStock"))) {
                    throw new IllegalArgumentException("最小计量总数不能为空！");
                }
                String miniStock = resObj.getString("miniStock");
                //获取物品单位
                if (StringUtil.isEmpty(userStorehouseDtos.get(0).getUnitCode())) {
                    throw new IllegalArgumentException("物品单位不能为空！");
                }
                String unitCode = userStorehouseDtos.get(0).getUnitCode();
                //获取物品最小计量单位
                if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniUnitCode())) {
                    throw new IllegalArgumentException("物品最小计量单位不能为空！");
                }
                String miniUnitCode = userStorehouseDtos.get(0).getMiniUnitCode();
                //计算个人物品剩余的最小计量总数
                double curStock = Double.parseDouble(miniStock) - Double.parseDouble(resObj.getString("curStock"));
                if (unitCode.equals(miniUnitCode)) { //物品单位与最小计量单位相同时，就不向上取整
                    //计算个人物品剩余的库存(向上取整)
                    double newMiniStock = curStock / miniUnitStock;
                    userStorehousePo.setStock(String.valueOf(newMiniStock));
                } else { //物品单位与最小计量单位不同时就向上取整
                    //计算个人物品剩余的库存(向上取整)
                    double newMiniStock = Math.ceil(curStock / miniUnitStock);
                    userStorehousePo.setStock(String.valueOf(newMiniStock));
                }
                userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
                userStorehousePo.setMiniStock(String.valueOf(curStock));
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
                        //获取目标仓库下该物品的原有最小计量总数
                        if (StringUtil.isEmpty(resourceStore.getMiniStock())) {
                            throw new IllegalArgumentException("最小计量总数不能为空！");
                        }
                        double oldMiniStock = Double.parseDouble(resourceStore.getMiniStock());
                        //计算返还后总的最小计量总数
                        BigDecimal num1 = new BigDecimal(oldMiniStock);
                        BigDecimal num2 = new BigDecimal(Double.parseDouble(resObj.getString("curStock")));
                        double allMiniStock = num1.add(num2).doubleValue();
                        //获取最小计量单位数量
                        if (StringUtil.isEmpty(resourceStore.getMiniUnitStock())) {
                            throw new IllegalArgumentException("最小计量单位数量不能为空！");
                        }
                        double miniUnitStock1 = Double.parseDouble(resourceStore.getMiniUnitStock());
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
                        if (unitCode1.equals(miniUnitCode1)) { //物品单位与物品最小计量单位相同时，就不向上取整
                            //计算返还后物品资源库存(向上取整)
                            double newStock = allMiniStock / miniUnitStock1;
                            resourceStorePo.setStock(String.valueOf(newStock));
                        } else { //物品单位与物品最小计量单位不同时，就向上取整
                            //计算返还后物品资源库存(向上取整)
                            double newStock = Math.ceil(allMiniStock / miniUnitStock1);
                            resourceStorePo.setStock(String.valueOf(newStock));
                        }
                        resourceStorePo.setResId(resourceStore.getResId());
                        resourceStorePo.setMiniStock(String.valueOf(allMiniStock));
//                      resourceStorePo.setStock((stock + stockB) + "");
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
                    resourceStorePo.setRssId(resourceStoreList.get(0).getRssId());
                    if (StringUtil.isEmpty(resourceStoreList.get(0).getMiniUnitStock())) {
                        throw new IllegalArgumentException("最小计量单位数量不能为空！");
                    }
                    resourceStorePo.setMiniUnitStock(resourceStoreList.get(0).getMiniUnitStock());
                    if (StringUtil.isEmpty(resourceStoreList.get(0).getMiniUnitCode())) {
                        throw new IllegalArgumentException("最小计量单位不能为空！");
                    }
                    resourceStorePo.setMiniUnitCode(resourceStoreList.get(0).getMiniUnitCode());
                    if (resourceStorePo.getUnitCode().equals(resourceStorePo.getMiniUnitCode())) { //如果物品单位与物品最小计量单位相同，就不向上取整
                        //计算物品库存
                        BigDecimal num1 = new BigDecimal(Double.parseDouble(resourceStorePo.getMiniStock()));
                        BigDecimal num2 = new BigDecimal(Double.parseDouble(resourceStorePo.getMiniUnitStock()));
                        double newStock = num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        resourceStorePo.setStock(String.valueOf(newStock));
                    } else { //如果物品单位与物品最小计量单位不相同，就向上取整
                        //计算物品库存
                        BigDecimal num1 = new BigDecimal(Double.parseDouble(resourceStorePo.getMiniStock()));
                        BigDecimal num2 = new BigDecimal(Double.parseDouble(resourceStorePo.getMiniUnitStock()));
                        double newStock = num1.divide(num2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double newStock1 = Math.ceil(newStock);
                        resourceStorePo.setStock(String.valueOf(newStock1));
                    }
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
