package com.java110.api.bmo.allocationUserStorehouse.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.allocationUserStorehouse.IAllocationUserStorehouseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.entity.center.AppService;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.po.allocationUserStorehouse.AllocationUserStorehousePo;
import com.java110.po.resourceStoreUseRecord.ResourceStoreUseRecordPo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("allocationUserStorehouseBMOImpl")
public class AllocationUserStorehouseBMOImpl extends AbstractServiceApiPlusListener implements IAllocationUserStorehouseBMO {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void addAllocationUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        String resourceStores = paramInJson.getString("resourceStores");
        JSONArray json = JSONArray.parseArray(resourceStores);
        if (json.size() > 0) {
            Object[] objects = json.toArray();
            String flag = paramInJson.getString("flag");
            if (!StringUtil.isEmpty(flag) && flag.equals("1")) { //损耗
                for (int i = 0; i < objects.length; i++) {
                    Object object = objects[i];
                    JSONObject paramIn = JSONObject.parseObject(String.valueOf(object));
                    ResourceStoreUseRecordPo resourceStoreUseRecordPo = new ResourceStoreUseRecordPo();
                    resourceStoreUseRecordPo.setRsurId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rsurId));
                    resourceStoreUseRecordPo.setRepairId("-1"); //报修记录
                    resourceStoreUseRecordPo.setResId(paramIn.getString("resId")); //物品资源id
                    resourceStoreUseRecordPo.setCommunityId(paramInJson.getString("communityId")); //小区id
                    resourceStoreUseRecordPo.setStoreId(paramInJson.getString("storeId")); //商户id
                    resourceStoreUseRecordPo.setQuantity(paramIn.getString("giveQuantity")); //损耗数量
                    //根据物品资源id查询物品资源信息
                    ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
                    resourceStoreDto.setResId(paramIn.getString("resId"));
                    List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
                    Assert.listOnlyOne(resourceStoreDtos, "查询房屋信息错误！");
                    resourceStoreUseRecordPo.setUnitPrice(resourceStoreDtos.get(0).getPrice()); //物品资源单价
                    resourceStoreUseRecordPo.setCreateUserId(paramInJson.getString("userId")); //创建人id
                    resourceStoreUseRecordPo.setCreateUserName(paramInJson.getString("userName")); //创建人名称
                    resourceStoreUseRecordPo.setRemark(paramIn.getString("purchaseRemark")); //备注
                    resourceStoreUseRecordPo.setResourceStoreName(paramIn.getString("resName")); //物品名称
                    resourceStoreUseRecordPo.setState(paramIn.getString("state")); //1001 报废回收   2002 工单损耗   3003 公用损耗
                    super.insert(dataFlowContext, resourceStoreUseRecordPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_RESOURCE_STORE_USE_RECORD);
                    //个人物品处理
                    UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                    userStorehouseDto.setUserId(paramInJson.getString("userId"));
                    userStorehouseDto.setResId(paramIn.getString("resId"));
                    //查询个人物品信息
                    List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
                    Assert.listOnlyOne(userStorehouseDtos, "查询个人物品信息错误！");
                    //获取个人物品信息id
                    String usId = userStorehouseDtos.get(0).getUsId();
                    //获取物品单位
                    if (StringUtil.isEmpty(userStorehouseDtos.get(0).getUnitCode())) {
                        throw new IllegalArgumentException("物品单位不能为空");
                    }
                    String unitCode = userStorehouseDtos.get(0).getUnitCode(); //物品单位
                    //获取物品最小计量单位
                    if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniUnitCode())) {
                        throw new IllegalArgumentException("物品最小计量单位不能为空");
                    }
                    String miniUnitCode = userStorehouseDtos.get(0).getMiniUnitCode(); //物品最小计量单位
                    UserStorehousePo userStorehousePo = new UserStorehousePo();
                    userStorehousePo.setUsId(usId); //个人物品id
                    //获取最小单位计量总数
                    String miniStock = userStorehouseDtos.get(0).getMiniStock();
                    //获取最小计量单位数量
                    String miniUnitStock = paramIn.getString("miniUnitStock");
                    //获取报废数量
                    String giveQuantity = paramIn.getString("giveQuantity");
                    //除去报废个人物品剩余的最小单位计量总数
                    BigDecimal num1 = new BigDecimal(miniStock);
                    BigDecimal num2 = new BigDecimal(giveQuantity);
                    BigDecimal quantity = num1.subtract(num2);
                    if (quantity.doubleValue() == 0.0) { //如果减去报废后剩余0个，那么最小计量单位总数和物品数量都变为0
                        userStorehousePo.setMiniStock("0");
                        userStorehousePo.setStock("0");
                    } else {
                        userStorehousePo.setMiniStock(String.valueOf(quantity)); //减去报废后剩余的最小计量单位总数
                        BigDecimal reduceNum = num1.subtract(num2);
                        if (unitCode.equals(miniUnitCode)) { //如果物品单位与最小计量单位相同，就不向上取整
                            userStorehousePo.setStock(String.valueOf(reduceNum));
                        } else { //如果物品最小计量单位与物品单位不同，就向上取整
                            //用转赠后最小计量总数除以最小计量单位数量，并向上取整，获取转赠后的库存数
                            BigDecimal num3 = new BigDecimal(miniUnitStock);
                            BigDecimal unitStock = reduceNum.divide(num3, 2, BigDecimal.ROUND_HALF_UP);
                            Integer stockNumber = (int) Math.ceil(unitStock.doubleValue());
                            userStorehousePo.setStock(String.valueOf(stockNumber)); //减去报废后剩余的个人物品数量
                        }
                    }
                    //更新当前用户个人物品库存数
                    super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
                    commit(dataFlowContext);
                }
            } else { //退还
                for (int i = 0; i < objects.length; i++) {
                    Object object = objects[i];
                    JSONObject paramIn = JSONObject.parseObject(String.valueOf(object));
                    String stock = paramIn.getString("stock");
                    //获取最小计量总数
                    String miniStock = paramIn.getString("miniStock");
                    //获取最小计量单位数量
                    String miniUnitStock = paramIn.getString("miniUnitStock");
                    //获取转赠数量
                    String giveQuantity = paramIn.getString("giveQuantity");
                    //获取物品id
                    String resId = paramIn.getString("resId");
                    //获取物品id
                    String resCode = paramIn.getString("resCode");
                    //获取物品名称
                    String resName = paramIn.getString("resName");
                    //获取当前用户id
                    String userId = paramInJson.getString("userId");
                    //获取接受转赠用户id
                    String acceptUserId = paramInJson.getString("acceptUserId");
                    //获取接受转赠用户名称
                    String acceptUserName = paramInJson.getString("acceptUserName");
                    //获取商户id
                    String storeId = paramInJson.getString("storeId");
                    JSONObject allocationUserStorehouseJson = new JSONObject();
                    allocationUserStorehouseJson.put("ausId", "-1");
                    allocationUserStorehouseJson.put("resId", resId);
                    allocationUserStorehouseJson.put("resCode", resCode);
                    allocationUserStorehouseJson.put("resName", resName);
                    allocationUserStorehouseJson.put("storeId", storeId);
                    allocationUserStorehouseJson.put("stock", stock);
                    allocationUserStorehouseJson.put("giveQuantity", giveQuantity);
                    allocationUserStorehouseJson.put("startUserId", userId);
                    allocationUserStorehouseJson.put("startUserName", paramInJson.getString("userName"));
                    allocationUserStorehouseJson.put("acceptUserId", acceptUserId);
                    allocationUserStorehouseJson.put("acceptUserName", acceptUserName);
                    allocationUserStorehouseJson.put("createTime", new Date());
                    allocationUserStorehouseJson.put("remark", paramInJson.getString("description"));
                    AllocationUserStorehousePo allocationUserStorehousePo = BeanConvertUtil.covertBean(allocationUserStorehouseJson, AllocationUserStorehousePo.class);
                    super.insert(dataFlowContext, allocationUserStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_ALLOCATION_USER_STOREHOUSE);
                    UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                    userStorehouseDto.setUserId(userId);
                    userStorehouseDto.setResId(resId);
                    List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
                    Assert.listOnlyOne(userStorehouseDtos, "查询个人物品信息错误！");
                    //获取个人物品信息id
                    String usId = userStorehouseDtos.get(0).getUsId();
                    //获取物品单位
                    if (StringUtil.isEmpty(userStorehouseDtos.get(0).getUnitCode())) {
                        throw new IllegalArgumentException("物品单位不能为空");
                    }
                    String unitCode = userStorehouseDtos.get(0).getUnitCode();
                    //获取物品最小计量单位
                    if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniUnitCode())) {
                        throw new IllegalArgumentException("物品最小计量单位不能为空");
                    }
                    String miniUnitCode = userStorehouseDtos.get(0).getMiniUnitCode();
                    UserStorehousePo userStorehousePo = new UserStorehousePo();
                    userStorehousePo.setUsId(usId);
                    //转赠后个人物品最小计量总数
                    BigDecimal num1 = new BigDecimal(miniStock);
                    BigDecimal num2 = new BigDecimal(giveQuantity);
                    BigDecimal quantity = num1.subtract(num2);
                    if (quantity.doubleValue() == 0.0) {
                        userStorehousePo.setMiniStock("0");
                        userStorehousePo.setStock("0");
                    } else {
                        userStorehousePo.setMiniStock(String.valueOf(quantity));
                        BigDecimal reduceNum = num1.subtract(num2);
                        if (unitCode.equals(miniUnitCode)) { //如果物品单位与最小计量单位相同，就不向上取整
                            userStorehousePo.setStock(String.valueOf(reduceNum));
                        } else { //如果物品最小计量单位与物品单位不同，就向上取整
                            //用转赠后最小计量总数除以最小计量单位数量，并向上取整，获取转赠后的库存数
                            BigDecimal num3 = new BigDecimal(miniUnitStock);
                            BigDecimal unitStock = reduceNum.divide(num3, 2, BigDecimal.ROUND_HALF_UP);
                            Integer stockNumber = (int) Math.ceil(unitStock.doubleValue());
                            userStorehousePo.setStock(String.valueOf(stockNumber));
                        }
                    }
                    //更新当前用户库存数
                    super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
                    UserStorehouseDto userStorehouse = new UserStorehouseDto();
                    userStorehouse.setUserId(acceptUserId);
                    userStorehouse.setResCode(resCode);
                    //查询接受转赠人的个人物品信息
                    List<UserStorehouseDto> userStorehouses = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouse);
                    if (userStorehouses != null && userStorehouses.size() == 1) {
                        UserStorehousePo userStorePo = new UserStorehousePo();
                        //计算接受用户的最小计量总数
                        BigDecimal num4 = new BigDecimal(userStorehouses.get(0).getMiniStock());
                        BigDecimal num5 = new BigDecimal(giveQuantity);
                        BigDecimal addNum = num4.add(num5);
                        BigDecimal acceptMiniStock = num4.add(num5);
                        userStorePo.setMiniStock(String.valueOf(acceptMiniStock));
                        //获取物品单位
                        if (StringUtil.isEmpty(userStorehouses.get(0).getUnitCode())) {
                            throw new IllegalArgumentException("物品单位不能为空");
                        }
                        String unitCode1 = userStorehouses.get(0).getUnitCode();
                        //获取物品最小计量单位
                        if (StringUtil.isEmpty(userStorehouses.get(0).getMiniUnitCode())) {
                            throw new IllegalArgumentException("物品最小计量单位不能为空");
                        }
                        String miniUnitCode1 = userStorehouses.get(0).getMiniUnitCode();
                        //计算接受用户的库存数量
                        BigDecimal num6 = new BigDecimal(miniUnitStock);
                        BigDecimal unitStock = addNum.divide(num6, 2, BigDecimal.ROUND_HALF_UP);
                        if (unitCode1.equals(miniUnitCode1)) { //如果物品单位与物品最小计量单位相同，就不向上取整
                            //如果物品单位与最小计量单位相同，物品库存就等于最小计量总数
                            userStorePo.setStock(String.valueOf(acceptMiniStock));
                        } else { //如果物品单位与物品最小计量单位不同，就向上取整
                            Integer stockNumber = (int) Math.ceil(unitStock.doubleValue());
                            userStorePo.setStock(String.valueOf(stockNumber));
                        }
                        userStorePo.setUsId(userStorehouses.get(0).getUsId());
                        //更新当前用户的库存数量
                        super.update(dataFlowContext, userStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
                    } else if (userStorehouses != null && userStorehouses.size() > 1) {
                        throw new IllegalArgumentException("查询个人物品信息错误！");
                    } else {
                        //计算转赠后库存数量
                        BigDecimal num7 = new BigDecimal(giveQuantity);
                        BigDecimal num8 = new BigDecimal(miniUnitStock);
                        BigDecimal unitStock = num7.divide(num8, 2, BigDecimal.ROUND_HALF_UP);
                        UserStorehousePo userStorePo = new UserStorehousePo();
                        userStorePo.setUsId("-1");
                        userStorePo.setResId(resId);
                        userStorePo.setResName(resName);
                        userStorePo.setStoreId(storeId);
                        userStorePo.setResCode(resCode);
                        if (unitCode.equals(miniUnitCode)) { //如果物品单位与物品最小计量单位相同，就不向上取整
                            userStorePo.setStock(String.valueOf(num7));
                        } else { //如果物品单位与物品最小计量单位不同，就向上取整
                            Integer stockNumber = (int) Math.ceil(unitStock.doubleValue());
                            userStorePo.setStock(String.valueOf(stockNumber));
                        }
                        userStorePo.setMiniStock(giveQuantity);
                        userStorePo.setUserId(acceptUserId);
                        //保存接受转赠用户个人物品信息
                        super.insert(dataFlowContext, userStorePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_STOREHOUSE);
                    }
                }
            }
        }
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void updateAllocationUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        AllocationUserStorehousePo allocationUserStorehousePo = BeanConvertUtil.covertBean(paramInJson, AllocationUserStorehousePo.class);
        super.update(dataFlowContext, allocationUserStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ALLOCATION_USER_STOREHOUSE);
    }


    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    public void deleteAllocationUserStorehouse(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        AllocationUserStorehousePo allocationUserStorehousePo = BeanConvertUtil.covertBean(paramInJson, AllocationUserStorehousePo.class);
        super.update(dataFlowContext, allocationUserStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_ALLOCATION_USER_STOREHOUSE);
    }

    @Override
    public String getServiceCode() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public ResponseEntity<String> callService(ServiceDataFlowEvent event) {
        return null;
    }

    @Override
    public ResponseEntity<String> callService(DataFlowContext context, String serviceCode, JSONArray businesses) {
        return null;
    }

    @Override
    public ResponseEntity<String> callService(DataFlowContext context, String serviceCode, JSONObject businesses) {
        return null;
    }

    @Override
    public ResponseEntity<String> callService(DataFlowContext context, AppService appService, Map paramIn) {
        return null;
    }

    @Override
    public JSONObject restToCenterProtocol(JSONObject businesses, Map<String, String> headers) {
        return null;
    }

    @Override
    public void freshHttpHeader(HttpHeaders httpHeaders, Map<String, String> headers) {

    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) throws ParseException {

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {

    }
}
