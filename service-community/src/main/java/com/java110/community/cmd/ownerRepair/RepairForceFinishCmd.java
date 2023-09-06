package com.java110.community.cmd.ownerRepair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeConfigDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.intf.community.*;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.store.IResourceStoreUseRecordV1InnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseV1InnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreUseRecordPo;
import com.java110.po.user.UserStorehousePo;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairForceFinish")
public class RepairForceFinishCmd extends Cmd {

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMO;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMO;

    @Autowired
    private IUserStorehouseV1InnerServiceSMO userStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreUseRecordV1InnerServiceSMO resourceStoreUseRecordV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        //获取维修类型
        String maintenanceType = reqJson.getString("maintenanceType");
        //获取商品集合
        String choosedGoodsList = reqJson.getString("choosedGoodsList");
        String userId = context.getReqHeaders().get("user-id");
        String userName = "";
        if (!StringUtil.isEmpty(context.getReqHeaders().get("user-name"))) {
            userName = context.getReqHeaders().get("user-name");
        } else {
            UserDto userDto = new UserDto();
            userDto.setUserId(userId);
            List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
            Assert.listOnlyOne(users, "查询用户错误！");
            userName = users.get(0).getName();
        }
        int flag = 0;
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(reqJson.getString("repairId"));
        repairDto.setCommunityId(reqJson.getString("communityId"));
        repairDto.setState(RepairDto.STATE_TAKING);
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.listOnlyOne(repairDtos, "当前没有需要处理订单");
        //查询正在处理 工单的师傅
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        if (repairUserDtos == null || repairUserDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "当前用户没有需要处理订单！");
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONArray json = JSONArray.parseArray(choosedGoodsList); //用到的物品列表
        if (json != null && json.size() > 0 && ("1001".equals(maintenanceType) || "1003".equals(maintenanceType))) { //有偿或需要用料
            Object[] objects = json.toArray();
            //数据前期校验
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                JSONObject paramIn = JSONObject.parseObject(String.valueOf(object));
                String isCustom = paramIn.getString("isCustom");//是否自定义
                String resId = paramIn.getString("resId");//获取商品资源id
                String useNumber = paramIn.getString("useNumber");//获取商品数量
                String outLowPrice = "0";//最低价
                String outHighPrice = "0"; //最高价
                List<ResourceStorePo> resourceStorePoList = new ArrayList<>();
                List<UserStorehouseDto> userStorehouseDtoList = new ArrayList<>();
                if (!StringUtil.isEmpty(resId)) {
                    //查询库存并校验库存
                    ResourceStorePo resourceStorePo = new ResourceStorePo();
                    resourceStorePo.setResId(resId);
                    resourceStorePoList = resourceStoreServiceSMO.getResourceStores(resourceStorePo);//查询物品资源信息
                    Assert.listOnlyOne(resourceStorePoList, "查询不到物品信息或查询到多个物品信息！");
                    outLowPrice = resourceStorePoList.get(0).getOutLowPrice(); //最低价
                    outHighPrice = resourceStorePoList.get(0).getOutHighPrice();//最高价
                }
                if (!StringUtil.isEmpty(useNumber) && !"0".equals(useNumber)
                        && (!StringUtil.isEmpty(isCustom) && isCustom.equals("false"))) {
                    String nowStock = "0";
                    //（从我的物料中获取商品库存）
                    UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                    userStorehouseDto.setResId(resId);
                    userStorehouseDto.setUserId(repairUserDtos.get(0).getStaffId());
                    //查询个人物品信息
                    userStorehouseDtoList = userStorehouseInnerServiceSMO.queryUserStorehouses(userStorehouseDto);
                    if (userStorehouseDtoList == null || userStorehouseDtoList.size() < 1) {
                        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "维修物料" + userStorehouseDtoList.get(0).getResName() + "库存不足，请您先申领物品！");
                        context.setResponseEntity(responseEntity);
                        return;
                    }
                    if (userStorehouseDtoList.size() == 1) {
                        //获取最小计量总数
                        nowStock = userStorehouseDtoList.get(0).getMiniStock();
                    }
                    if (Double.parseDouble(nowStock) < Double.parseDouble(useNumber)) {
                        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "维修物料" + userStorehouseDtoList.get(0).getResName() + "库存不足，请您先申领物品！");
                        context.setResponseEntity(responseEntity);
                        return;
                    }
                }
                if (maintenanceType.equals("1001") && (!StringUtil.isEmpty(isCustom) && isCustom.equals("false"))) {
                    Double price = Double.parseDouble(paramIn.getString("price")); //获取价格
                    Double outLowPrices = Double.parseDouble(outLowPrice);//最低价
                    Double outHighPrices = Double.parseDouble(outHighPrice);//最高价
                    //物品价格应该在最低价和最高价之间
                    if (price < outLowPrices || price > outHighPrices) {
                        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "输入的维修物料" + userStorehouseDtoList.get(0).getResName() + "单价不正确，请重新输入！");
                        context.setResponseEntity(responseEntity);
                        return;
                    }
                }
            }
            //数据操作入库以及更新数据库
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                JSONObject paramIn = JSONObject.parseObject(String.valueOf(object));
                String isCustom = paramIn.getString("isCustom");//是否自定义
                //获取商品资源id
                String resId = paramIn.getString("resId");
                //获取商品数量
                String useNumber = paramIn.getString("useNumber");
                //总价
                String totalPrice = "";
                //用料
                String repairMaterials = "";
                //单价
                Double unitPrice = 0.0;
                //数量
                Double useNumber_s = 0.0;
                //费用明细
                String repair = "";
                DecimalFormat df = new DecimalFormat("0.00");
                List<ResourceStorePo> resourceStorePoList = new ArrayList<>();
                List<UserStorehouseDto> userStorehouseDtoList = new ArrayList<>();
                if (!StringUtil.isEmpty(paramIn.getString("price")) && !StringUtil.isEmpty(useNumber)) {
                    //支付费用 数量乘以单价
                    unitPrice = Double.parseDouble(paramIn.getString("price"));
                    //物品数量
                    useNumber_s = Double.parseDouble(useNumber);
                    //计算金额
                    totalPrice = df.format(unitPrice * useNumber_s);
                    //费用明细
                    repair = df.format(unitPrice) + " * " + useNumber_s + " = " + totalPrice;
                }
                if (!StringUtil.isEmpty(resId)) {
                    ResourceStorePo resourceStorePo = new ResourceStorePo();
                    resourceStorePo.setResId(resId);
                    //查询物品资源信息
                    resourceStorePoList = resourceStoreServiceSMO.getResourceStores(resourceStorePo);
                    Assert.listOnlyOne(resourceStorePoList, "查询不到物品信息或查询到多个物品信息！");
                    //用料
                    repairMaterials = resourceStorePoList.get(0).getResName() + "*" + useNumber;
                } else {
                    //用料
                    repairMaterials = paramIn.getString("customGoodsName") + "*" + useNumber;
                }
                if (!StringUtil.isEmpty(useNumber) && !"0".equals(useNumber)
                        && (!StringUtil.isEmpty(isCustom) && isCustom.equals("false"))) {
                    String nowStock = "0";
                    //（从我的物料中获取商品库存）
                    UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
                    userStorehouseDto.setResId(resId);
                    userStorehouseDto.setUserId(repairUserDtos.get(0).getStaffId());
                    //查询个人物品信息
                    userStorehouseDtoList = userStorehouseInnerServiceSMO.queryUserStorehouses(userStorehouseDto);
                    Assert.listOnlyOne(userStorehouseDtoList, "查询不到个人物品信息或查询到多条信息！");
                    //最小计量总数
                    nowStock = userStorehouseDtoList.get(0).getMiniStock();
                    //库存减少
                    UserStorehousePo userStorehousePo = new UserStorehousePo();
                    //计算个人物品剩余最小计量总数
                    BigDecimal num1 = new BigDecimal(Double.parseDouble(nowStock));
                    BigDecimal num2 = new BigDecimal(Double.parseDouble(useNumber));
                    BigDecimal surplusStock = num1.subtract(num2);
                    //最小计量单位数量
                    double miniUnitStock = Double.parseDouble(userStorehouseDtoList.get(0).getMiniUnitStock());
                    //获取物品单位
                    if (StringUtil.isEmpty(userStorehouseDtoList.get(0).getUnitCode())) {
                        throw new IllegalArgumentException("物品单位不能为空！");
                    }
                    String unitCode = userStorehouseDtoList.get(0).getUnitCode();
                    //获取物品最小计量单位
                    if (StringUtil.isEmpty(userStorehouseDtoList.get(0).getMiniUnitCode())) {
                        throw new IllegalArgumentException("物品最小计量单位不能为空！");
                    }
                    String miniUnitCode = userStorehouseDtoList.get(0).getMiniUnitCode();
                    if (unitCode.equals(miniUnitCode)) { //如果最小计量单位与物品单位相同，就不向上取整
                        BigDecimal num3 = new BigDecimal(miniUnitStock);
                        double newStock = surplusStock.divide(num3, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        int remainingInventory = new Double(newStock).intValue();
                        userStorehousePo.setStock(String.valueOf(remainingInventory));
                    } else { //如果不相同就向上取整
                        BigDecimal num3 = new BigDecimal(miniUnitStock);
                        double newStock = surplusStock.divide(num3, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double ceil = Math.ceil(newStock);
                        int remainingInventory = new Double(ceil).intValue();
                        userStorehousePo.setStock(String.valueOf(remainingInventory));
                    }
                    if (useNumber.contains(".") || nowStock.contains(".")) { //如果传过来的使用数量为小数，或原有库存数量有小数，就保留小数
                        userStorehousePo.setMiniStock(String.valueOf(surplusStock.doubleValue()));
                    } else { //如果传来的使用数量为整数，且原有库存数量为整数，就取整
                        userStorehousePo.setMiniStock(String.valueOf(surplusStock.intValue()));
                    }
                    userStorehousePo.setUsId(userStorehouseDtoList.get(0).getUsId());
                    userStorehousePo.setResId(resId);
                    userStorehousePo.setUserId(repairUserDtos.get(0).getStaffId());
                    //更新库存
                    flag = userStorehouseV1InnerServiceSMOImpl.updateUserStorehouse(userStorehousePo);
                    if (flag < 1) {
                        throw new CmdException("修改工单失败");
                    }
                }
                //往物品使用记录表插入数据
                ResourceStoreUseRecordPo resourceStoreUseRecordPo = new ResourceStoreUseRecordPo();
                resourceStoreUseRecordPo.setRsurId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rsurId));
                resourceStoreUseRecordPo.setRepairId(reqJson.getString("repairId"));
                resourceStoreUseRecordPo.setResourceStoreName(paramIn.getString("resName"));
                if (!StringUtil.isEmpty(isCustom) && isCustom.equals("true")) {
                    resId = "666666";
                    resourceStoreUseRecordPo.setResourceStoreName(paramIn.getString("customGoodsName"));
                }
                resourceStoreUseRecordPo.setResId(resId);
                resourceStoreUseRecordPo.setCommunityId(reqJson.getString("communityId"));
                resourceStoreUseRecordPo.setStoreId(reqJson.getString("storeId"));
                resourceStoreUseRecordPo.setCreateUserId(repairUserDtos.get(0).getStaffId());
                resourceStoreUseRecordPo.setCreateUserName(repairUserDtos.get(0).getStaffName());
                resourceStoreUseRecordPo.setRemark(reqJson.getString("userName") + "强制回单");
                resourceStoreUseRecordPo.setQuantity(useNumber);
                resourceStoreUseRecordPo.setState("2002"); //1001 报废回收   2002 工单损耗   3003 公用损耗
                //有偿服务
                if (maintenanceType.equals("1001")) {
                    resourceStoreUseRecordPo.setUnitPrice(paramIn.getString("price"));
                    flag = resourceStoreUseRecordV1InnerServiceSMOImpl.saveResourceStoreUseRecord(resourceStoreUseRecordPo);
                    if (flag < 1) {
                        throw new CmdException("添加失败");
                    }
                } else if (maintenanceType.equals("1003")) {  //需要用料
                    flag = resourceStoreUseRecordV1InnerServiceSMOImpl.saveResourceStoreUseRecord(resourceStoreUseRecordPo);
                    if (flag < 1) {
                        throw new CmdException("添加失败");
                    }
                }
            }
            //有偿服务生成费用
            if (maintenanceType.equals("1001") && StringUtil.isEmpty(reqJson.getString("sign"))) {
                //查询默认费用项
                FeeConfigDto feeConfigDto = new FeeConfigDto();
                feeConfigDto.setCommunityId(reqJson.getString("communityId"));
                feeConfigDto.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
                feeConfigDto.setIsDefault(FeeConfigDto.DEFAULT_FEE_CONFIG);
                List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
                if (feeConfigDtos.size() != 1) {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "默认维修费用有多条或不存在！");
                    context.setResponseEntity(responseEntity);
                    return;
                }
                PayFeePo feePo = new PayFeePo();
                feePo.setAmount(String.valueOf(reqJson.getString("")));
                feePo.setCommunityId(reqJson.getString("communityId"));
                feePo.setConfigId(feeConfigDtos.get(0).getConfigId());
                feePo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                feePo.setFeeFlag(feeConfigDtos.get(0).getFeeFlag());
                feePo.setFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
                feePo.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_REPAIR);
                feePo.setIncomeObjId(reqJson.getString("storeId"));
                feePo.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
                feePo.setAmount(reqJson.getString("totalPrice"));
                feePo.setBatchId("-1");
                RepairDto repair = new RepairDto();
                repair.setCommunityId(reqJson.getString("communityId"));
                repair.setRepairId(reqJson.getString("repairId"));
                List<RepairDto> repairs = repairInnerServiceSMOImpl.queryRepairs(repair);
                if (repairs.size() != 1) {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "维修单有多条或不存在！");
                    context.setResponseEntity(responseEntity);
                    return;
                }
                feePo.setPayerObjId(repairDtos.get(0).getRepairObjId());
                feePo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                feePo.setState(FeeDto.STATE_DOING);
                feePo.setUserId(userId);
                feePo.setbId("-1");
                flag = payFeeV1InnerServiceSMOImpl.savePayFee(feePo);
                if (flag < 1) {
                    throw new CmdException("添加费用失败");
                }
                FeeAttrPo feeAttrPo = new FeeAttrPo();
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrPo.setFeeId(feePo.getFeeId());
                feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_REPAIR);
                feeAttrPo.setCommunityId(reqJson.getString("communityId"));
                feeAttrPo.setValue(reqJson.getString("repairId"));
                flag = feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
                if (flag < 1) {
                    throw new CmdException("添加费用失败");
                }
            }
        }
        if(maintenanceType.equals("1001") && !StringUtil.isEmpty(reqJson.getString("sign")) && reqJson.getString("sign").equals("1")){ //有偿服务
            RepairUserPo repairUserPo = new RepairUserPo();
            repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
            repairUserPo.setRepairId(reqJson.getString("repairId"));
            repairUserPo.setbId("-1");
            repairUserPo.setCommunityId(reqJson.getString("communityId"));
            repairUserPo.setState(RepairUserDto.STATE_FINISH_PAY_FEE);
            repairUserPo.setContext(userName + " 强制回单有偿物品支付");
            repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
            repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
            repairUserPo.setStaffId(repairUserDtos.get(0).getStaffId());
            repairUserPo.setStaffName(repairUserDtos.get(0).getStaffName());
            repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
            repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
            flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("添加维修工单流程信息失败");
            }
        }
        // 1.0 关闭自己订单
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_CLOSE);
        repairUserPo.setContext(userName + " 强制回单");
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改维修工单流程信息失败");
        }
        repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruId));
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_CLOSE);
        repairUserPo.setRepairId(reqJson.getString("repairId"));
        repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
        repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
        repairUserPo.setStaffId(userId);
        repairUserPo.setStaffName(userName);
        repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_AUDIT_USER);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("添加维修工单流程信息失败");
        }
        reqJson.put("state", RepairDto.STATE_RETURN_VISIT);
        modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_RETURN_VISIT);
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
        context.setResponseEntity(responseEntity);
    }

    public void modifyBusinessRepairDispatch(JSONObject paramInJson, String state) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", state);
        businessOwnerRepair.put("maintenanceType", paramInJson.getString("maintenanceType"));
        //计算 应收金额
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改工单失败");
        }
    }
}
