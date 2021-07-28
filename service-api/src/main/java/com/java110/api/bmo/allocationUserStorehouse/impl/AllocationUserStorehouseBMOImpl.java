package com.java110.api.bmo.allocationUserStorehouse.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.allocationUserStorehouse.IAllocationUserStorehouseBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.po.allocationUserStorehouse.AllocationUserStorehousePo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("allocationUserStorehouseBMOImpl")
public class AllocationUserStorehouseBMOImpl extends ApiBaseBMO implements IAllocationUserStorehouseBMO {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

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
                userStorehouse.setResId(resId);
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

}
