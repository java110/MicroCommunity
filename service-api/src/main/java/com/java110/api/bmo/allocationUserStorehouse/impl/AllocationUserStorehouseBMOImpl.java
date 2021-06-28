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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                //获取库存数量
                String stock = paramIn.getString("stock");
                //获取转增数量
                String giveQuantity = paramIn.getString("giveQuantity");
                //获取物品id
                String resId = paramIn.getString("resId");
                String resName = paramIn.getString("resName");
                //获取当前用户id
                String userId = paramInJson.getString("userId");
                //获取接受转增用户id
                String acceptUserId = paramInJson.getString("acceptUserId");
                String acceptUserName = paramInJson.getString("acceptUserName");
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
                UserStorehousePo userStorehousePo = new UserStorehousePo();
                userStorehousePo.setUsId(usId);
                //获取转增后的库存数量
                int newStock = Integer.parseInt(stock) - Integer.parseInt(giveQuantity);
                userStorehousePo.setStock(String.valueOf(newStock));
                //更新当前用户库存数
                super.update(dataFlowContext, userStorehousePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
                UserStorehouseDto userStorehouse = new UserStorehouseDto();
                userStorehouse.setUserId(acceptUserId);
                userStorehouse.setResId(resId);
                List<UserStorehouseDto> userStorehouses = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouse);
                if (userStorehouses != null && userStorehouses.size() == 1) {
                    //获取接受用户库存数
                    String userStock = userStorehouses.get(0).getStock();
                    //获取转增后的库存数量
                    int myStock = Integer.parseInt(userStock) + Integer.parseInt(giveQuantity);
                    UserStorehousePo userStorePo = new UserStorehousePo();
                    userStorePo.setUsId(userStorehouses.get(0).getUsId());
                    userStorePo.setStock(String.valueOf(myStock));
                    //更新当前用户的库存数量
                    super.update(dataFlowContext, userStorePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_USER_STOREHOUSE);
                } else if (userStorehouses != null && userStorehouses.size() > 1) {
                    throw new IllegalArgumentException("查询个人物品信息错误！");
                } else {
                    UserStorehousePo userStorePo = new UserStorehousePo();
                    userStorePo.setUsId("-1");
                    userStorePo.setResId(resId);
                    userStorePo.setResName(resName);
                    userStorePo.setStoreId(storeId);
                    userStorePo.setStock(giveQuantity);
                    userStorePo.setUserId(acceptUserId);
                    //保存接受转增用户个人物品信息
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
