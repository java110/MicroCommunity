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
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.repair.RepairUserDto;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.*;
import com.java110.intf.fee.IFeeAttrInnerServiceSMO;
import com.java110.intf.fee.IFeeConfigInnerServiceSMO;
import com.java110.intf.fee.IPayFeeV1InnerServiceSMO;
import com.java110.intf.store.IResourceStoreUseRecordV1InnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.intf.store.IUserStorehouseV1InnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.po.fee.FeeAttrPo;
import com.java110.po.fee.PayFeePo;
import com.java110.po.file.FileRelPo;
import com.java110.po.owner.RepairPoolPo;
import com.java110.po.owner.RepairUserPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreUseRecordPo;
import com.java110.po.user.UserStorehousePo;
import com.java110.utils.constant.FeeTypeConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.*;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "ownerRepair.repairFinish")
public class RepairFinishCmd extends Cmd {

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMO;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IResourceStoreServiceSMO resourceStoreServiceSMO;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMO;

    @Autowired
    private IUserStorehouseV1InnerServiceSMO userStorehouseV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreUseRecordV1InnerServiceSMO resourceStoreUseRecordV1InnerServiceSMOImpl;

    @Autowired
    private IRepairUserV1InnerServiceSMO repairUserV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IRepairPoolV1InnerServiceSMO repairPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "repairId", "未包含报修单信息");
        Assert.hasKeyAndValue(reqJson, "context", "未包含派单内容");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
//        Assert.hasKeyAndValue(reqJson, "amount", "未包含金额");
        Assert.hasKeyAndValue(reqJson, "feeFlag", "未包含费用标识");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        String userName = context.getReqHeaders().get("user-name");
        String publicArea = reqJson.getString("publicArea");
        int flag = 0;
        //获取报修渠道
        String repairChannel = reqJson.getString("repairChannel");
        //获取维修类型
        String maintenanceType = reqJson.getString("maintenanceType");
        //获取商品集合
        String choosedGoodsList = reqJson.getString("choosedGoodsList");
        //判断当前用户是否有需要处理的订单
        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(reqJson.getString("repairId"));
        repairUserDto.setCommunityId(reqJson.getString("communityId"));
        repairUserDto.setState(RepairUserDto.STATE_DOING);
        repairUserDto.setStaffId(userId);
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        if (repairUserDtos == null || repairUserDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "当前用户没有需要处理订单！");
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONArray json = JSONArray.parseArray(choosedGoodsList);
        //用料
        String repairMaterial = "";
        //费用明细(单价 * 数量 = 总价)
        String repairFee = "";
        if (!ListUtil.isNull(json) && ("1001".equals(maintenanceType) || "1003".equals(maintenanceType))) {
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
                    userStorehouseDto.setUserId(userId);
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
                    userStorehouseDto.setUserId(userId);
                    //查询个人物品信息
                    userStorehouseDtoList = userStorehouseInnerServiceSMO.queryUserStorehouses(userStorehouseDto);
                    Assert.listOnlyOne(userStorehouseDtoList, "查询不到个人物品信息或查询到多条信息！");
                    if (userStorehouseDtoList.size() == 1) {
                        //最小计量总数
                        nowStock = userStorehouseDtoList.get(0).getMiniStock();
                    }
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
                    userStorehousePo.setUserId(userId);
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
                resourceStoreUseRecordPo.setCreateUserId(reqJson.getString("userId"));
                resourceStoreUseRecordPo.setCreateUserName(reqJson.getString("userName"));
                resourceStoreUseRecordPo.setRemark(reqJson.getString("context"));
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
                if (!StringUtil.isEmpty(repairMaterials)) {
                    repairMaterial += repairMaterials + "；";
                }
                if (!StringUtil.isEmpty(repair)) {
                    repairFee += repair + "；";
                }
            }
        }
        // 1.0 关闭自己订单
        RepairUserPo repairUserPo = new RepairUserPo();
        repairUserPo.setRuId(repairUserDtos.get(0).getRuId());
        repairUserPo.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        repairUserPo.setState(RepairUserDto.STATE_CLOSE);
        repairUserPo.setContext(reqJson.getString("context"));
        repairUserPo.setCommunityId(reqJson.getString("communityId"));
        flag = repairUserV1InnerServiceSMOImpl.updateRepairUserNew(repairUserPo);
        if (flag < 1) {
            throw new CmdException("修改用户失败");
        }
        if ((!StringUtil.isEmpty(repairChannel) && "Z".equals(repairChannel))
                || (!StringUtil.isEmpty(maintenanceType) && "1001".equals(maintenanceType))) {  //如果是业主报修或者是有偿的就生成一条新状态，否则不变
            //2.0 给开始节点派支付单
            repairUserDto = new RepairUserDto();
            repairUserDto.setRepairId(reqJson.getString("repairId"));
            repairUserDto.setCommunityId(reqJson.getString("communityId"));
            repairUserDto.setRepairEvent(RepairUserDto.REPAIR_EVENT_START_USER);
            List<RepairUserDto> startRepairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
            if (startRepairUserDtos.size() != 1) {
                ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "数据错误 该订单没有发起人！");
                context.setResponseEntity(responseEntity);
                return;
            }
            repairUserPo = new RepairUserPo();
            repairUserPo.setRuId(GenerateCodeFactory.getGeneratorId("83"));
            repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            if ("1001".equals(maintenanceType)) { //如果是有偿的就走下面(业主报修有偿或者电话申请有偿或者员工报修有偿)
                repairUserPo.setState(RepairUserDto.STATE_PAY_FEE);
                repairUserPo.setContext("待支付" + reqJson.getString("totalPrice") + "元");
            } else {
                repairUserPo.setState(RepairUserDto.STATE_EVALUATE);
                repairUserPo.setContext("待评价");
            }
            repairUserPo.setRepairId(reqJson.getString("repairId"));
            if ("Z".equals(repairChannel)) {  //如果是业主端报修的走下面的方法
                repairUserPo.setStaffId(startRepairUserDtos.get(0).getStaffId());
                repairUserPo.setStaffName(startRepairUserDtos.get(0).getStaffName());
            } else { //如果不是业主报修，并且有偿
                RepairDto repairDto = new RepairDto();
                repairDto.setRepairId(reqJson.getString("repairId"));
                List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
                if (repairDtos.size() != 1) {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "数据错误，该用户没有报修单！");
                    context.setResponseEntity(responseEntity);
                    return;
                }
                //此时的报修对象ID即房屋ID
                String roomId = repairDtos.get(0).getRepairObjId();
                OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
                ownerRoomRelDto.setRoomId(roomId);
                //查询房屋业主关系表
                List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMO.queryOwnerRoomRels(ownerRoomRelDto);
                if (ownerRoomRelDtos.size() != 1) {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "信息错误，未找到房屋的业主！");
                    context.setResponseEntity(responseEntity);
                    return;
                }
                //获取业主id
                String ownerId = ownerRoomRelDtos.get(0).getOwnerId();
                OwnerDto ownerDto = new OwnerDto();
                ownerDto.setOwnerId(ownerId);
                //根据业主id查询业主信息
                List<OwnerDto> ownerDtos = ownerInnerServiceSMO.queryOwners(ownerDto);
                if (ownerDtos.size() != 1) {
                    ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "未查询到业主信息！");
                    context.setResponseEntity(responseEntity);
                    return;
                }
                //获取业主姓名
                String ownerName = ownerDtos.get(0).getName();
                repairUserPo.setStaffId(ownerId);
                repairUserPo.setStaffName(ownerName);
            }
            repairUserPo.setPreStaffId(repairUserDtos.get(0).getStaffId());
            repairUserPo.setPreStaffName(repairUserDtos.get(0).getStaffName());
            repairUserPo.setPreRuId(repairUserDtos.get(0).getRuId());
            repairUserPo.setRepairEvent(RepairUserDto.REPAIR_EVENT_PAY_USER);
            repairUserPo.setCommunityId(reqJson.getString("communityId"));
            flag = repairUserV1InnerServiceSMOImpl.saveRepairUserNew(repairUserPo);
            if (flag < 1) {
                throw new CmdException("修改用户失败");
            }
        }
        //维修前图片处理
        if (reqJson.containsKey("beforeRepairPhotos") && !StringUtils.isEmpty(reqJson.getString("beforeRepairPhotos"))) {
            JSONArray beforeRepairPhotos = reqJson.getJSONArray("beforeRepairPhotos");
            for (int _photoIndex = 0; _photoIndex < beforeRepairPhotos.size(); _photoIndex++) {
                String photo = beforeRepairPhotos.getJSONObject(_photoIndex).getString("photo");
                if (photo.length() > 512) {
                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    fileDto.setContext(photo);
                    fileDto.setSuffix("jpeg");
                    fileDto.setCommunityId(reqJson.getString("communityId"));
                    photo = fileInnerServiceSMOImpl.saveFile(fileDto);
                }
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
                businessUnit.put("relTypeCd", FileRelDto.BEFORE_REPAIR_PHOTOS);
                businessUnit.put("saveWay", "ftp");
                businessUnit.put("objId", reqJson.getString("repairId"));
                businessUnit.put("fileRealName", photo);
                businessUnit.put("fileSaveName", photo);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存图片失败");
                }
            }
        }
        //维修后图片处理
        if (reqJson.containsKey("afterRepairPhotos") && !StringUtils.isEmpty(reqJson.getString("afterRepairPhotos"))) {
            JSONArray afterRepairPhotos = reqJson.getJSONArray("afterRepairPhotos");
            for (int _photoIndex = 0; _photoIndex < afterRepairPhotos.size(); _photoIndex++) {
                String photo = afterRepairPhotos.getJSONObject(_photoIndex).getString("photo");
                if (photo.length() > 512) {
                    FileDto fileDto = new FileDto();
                    fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                    fileDto.setFileName(fileDto.getFileId());
                    fileDto.setContext(photo);
                    fileDto.setSuffix("jpeg");
                    fileDto.setCommunityId(reqJson.getString("communityId"));
                    photo = fileInnerServiceSMOImpl.saveFile(fileDto);
                }
                JSONObject businessUnit = new JSONObject();
                businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
                businessUnit.put("relTypeCd", FileRelDto.AFTER_REPAIR_PHOTOS);
                businessUnit.put("saveWay", "ftp");
                businessUnit.put("objId", reqJson.getString("repairId"));
                businessUnit.put("fileRealName", photo);
                businessUnit.put("fileSaveName", photo);
                FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
                flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
                if (flag < 1) {
                    throw new CmdException("保存图片失败");
                }
            }
        }
        if ("F".equals(publicArea) && "1002".equals(reqJson.getString("maintenanceType"))) { //如果不是公共区域且是无偿的走下面
            //改变r_repair_pool表maintenance_type维修类型
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(reqJson.getString("repairId"));
            repairPoolPo.setMaintenanceType(reqJson.getString("maintenanceType"));
            flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
            if ("Z".equals(repairChannel)) { //如果是电话报修和员工代客报修结单后状态变为待回访
                modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_APPRAISE);
            } else { //如果是业主自主报修结单后状态变为待评价
                modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_RETURN_VISIT);
            }
        } else if ("F".equals(publicArea) && "1001".equals(reqJson.getString("maintenanceType"))) { //如果不是公共区域且是有偿的走下面
            //3.0 生成支付费用
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
            RepairDto repairDto = new RepairDto();
            repairDto.setCommunityId(reqJson.getString("communityId"));
            repairDto.setRepairId(reqJson.getString("repairId"));
            List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
            if (repairDtos.size() != 1) {
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
            //改变r_repair_pool表maintenance_type维修类型
            RepairPoolPo repairPoolPo = new RepairPoolPo();
            repairPoolPo.setRepairId(reqJson.getString("repairId"));
            //维修类型
            repairPoolPo.setMaintenanceType(reqJson.getString("maintenanceType"));
            //用料
            repairPoolPo.setRepairMaterials(repairMaterial.substring(0, repairMaterial.length() - 1));
            //费用明细
            repairPoolPo.setRepairFee(repairFee.substring(0, repairFee.length() - 1));
            //支付方式
            repairPoolPo.setPayType(reqJson.getString("payType"));
            flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
            if (flag < 1) {
                throw new CmdException("修改失败");
            }
            modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_PAY);
        } else if ("T".equals(publicArea)) {  //公共区域走这里
            //公共区域用料时修改维修类型和用料
            if ("1003".equals(maintenanceType)) {
                //改变r_repair_pool表maintenance_type维修类型
                RepairPoolPo repairPoolPo = new RepairPoolPo();
                repairPoolPo.setRepairId(reqJson.getString("repairId"));
                //维修类型
                repairPoolPo.setMaintenanceType(reqJson.getString("maintenanceType"));
                //用料
                repairPoolPo.setRepairMaterials(repairMaterial.substring(0, repairMaterial.length() - 1));
                flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
                if (flag < 1) {
                    throw new CmdException("修改失败");
                }
            }

            if ("Z".equals(repairChannel)) { //如果是电话报修和员工代客报修结单后状态变为待回访
                modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_APPRAISE);
            } else { //如果是业主自主报修结单后状态变为待评价
                modifyBusinessRepairDispatch(reqJson, RepairDto.STATE_RETURN_VISIT);
            }
        }
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
        //计算 应收金额
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        int flag = repairPoolV1InnerServiceSMOImpl.updateRepairPoolNew(repairPoolPo);
        if (flag < 1) {
            throw new CmdException("修改工单失败");
        }
    }
}
