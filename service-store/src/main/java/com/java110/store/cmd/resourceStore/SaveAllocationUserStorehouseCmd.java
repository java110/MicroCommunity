package com.java110.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.DataFlowContext;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.allocationUserStorehouse.AllocationUserStorehousePo;
import com.java110.po.resourceStoreUseRecord.ResourceStoreUseRecordPo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Java110Cmd(serviceCode = "resourceStore.saveAllocationUserStorehouse")
public class SaveAllocationUserStorehouseCmd extends Cmd {

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseV1InnerServiceSMO userStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreUseRecordV1InnerServiceSMO resourceStoreUseRecordV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationUserStorehouseV1InnerServiceSMO allocationUserStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;



    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        //Assert.hasKeyAndValue(reqJson, "acceptUserId", "请求报文中未包含acceptUserId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = reqJson.getString("userId");
        if(StringUtil.isEmpty(userId)){
            userId = context.getReqHeaders().get("user-id");
        }
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setPage(1);
        userDto.setRow(1);
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos,"用户不存在");

        reqJson.put("userName",userDtos.get(0).getName());

        String acceptUserId = reqJson.getString("acceptUserId");
        if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(acceptUserId) && acceptUserId.equals(userId)){
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "物品接受人不能是本人，所以无法进行转赠操作！");
            context.setResponseEntity(responseEntity);
            return;
        }

        addAllocationUserStorehouse(reqJson,context);
    }

    public void addAllocationUserStorehouse(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) {
        String resourceStores = paramInJson.getString("resourceStores");
        JSONArray json = JSONArray.parseArray(resourceStores);
        int flag1 = 0;
        if (json.size() > 0) {
            Object[] objects = json.toArray();
            String flag = paramInJson.getString("flag");
            if (!StringUtil.isEmpty(flag) && flag.equals("1")) { //损耗
                for (int i = 0; i < objects.length; i++) {
                    Object object = objects[i];
                    JSONObject paramIn = JSONObject.parseObject(String.valueOf(object));
                    ResourceStoreUseRecordPo resourceStoreUseRecordPo = new ResourceStoreUseRecordPo();
                    resourceStoreUseRecordPo.setRsurId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rsurId));
                    resourceStoreUseRecordPo.setRepairId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_repairId)); //报修记录
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
                    flag1 = resourceStoreUseRecordV1InnerServiceSMOImpl.saveResourceStoreUseRecord(resourceStoreUseRecordPo);
                    if(flag1 <1){
                        throw new CmdException("保存失败");
                    }
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
                    flag1 = userStorehouseV1InnerServiceSMOImpl.updateUserStorehouse(userStorehousePo);
                    if(flag1 <1){
                        throw new CmdException("保存失败");
                    }
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
                    //批次
                    String timesId = paramIn.getString("timesId");
                    //获取商户id
                    String storeId = paramInJson.getString("storeId");
                    JSONObject allocationUserStorehouseJson = new JSONObject();
                    allocationUserStorehouseJson.put("ausId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ausId));
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
                    flag1 = allocationUserStorehouseV1InnerServiceSMOImpl.saveAllocationUserStorehouse(allocationUserStorehousePo);
                    if(flag1 <1){
                        throw new CmdException("保存失败");
                    }
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
                    flag1 = userStorehouseV1InnerServiceSMOImpl.updateUserStorehouse(userStorehousePo);
                    if(flag1 <1){
                        throw new CmdException("保存失败");
                    }
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
                        userStorePo.setTimesId(timesId);
                        //更新当前用户的库存数量
                        flag1 = userStorehouseV1InnerServiceSMOImpl.updateUserStorehouse(userStorePo);
                        if(flag1 <1){
                            throw new CmdException("保存失败");
                        }
                    } else if (userStorehouses != null && userStorehouses.size() > 1) {
                        throw new IllegalArgumentException("查询个人物品信息错误！");
                    } else {
                        //计算转赠后库存数量
                        BigDecimal num7 = new BigDecimal(giveQuantity);
                        BigDecimal num8 = new BigDecimal(miniUnitStock);
                        BigDecimal unitStock = num7.divide(num8, 2, BigDecimal.ROUND_HALF_UP);
                        UserStorehousePo userStorePo = new UserStorehousePo();
                        userStorePo.setUsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
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
                        userStorePo.setTimesId(timesId);
                        //保存接受转赠用户个人物品信息
                        flag1 = userStorehouseV1InnerServiceSMOImpl.saveUserStorehouse(userStorePo);
                        if(flag1 <1){
                            throw new CmdException("保存失败");
                        }                    }
                }
            }
        }
    }
}
