package com.java110.store.cmd.purchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchase.AllocationStorehouseDto;
import com.java110.dto.purchase.AllocationStorehouseApplyDto;
import com.java110.dto.purchase.PurchaseApplyDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.store.StorehouseDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.store.*;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.store.bmo.purchase.IPurchaseApplyBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 紧急采购申请
 */
@Java110Cmd(serviceCode = "/purchase/urgentPurchaseApply")
public class UrgentPurchaseApplyCmd extends Cmd {

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String URGRNT_NUMBER = "URGRNT_NUMBER";

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyBMO purchaseApplyBMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IStorehouseV1InnerServiceSMO storehouseV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写申请采购的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写申请类型");
        Assert.hasKeyAndValue(reqJson, "shId", "必填，请填写仓库");
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        if (resourceStores == null || resourceStores.size() < 1) {
            throw new CmdException("未包含采购物品");
        }
        //todo 查询仓库是否存在
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShId(reqJson.getString("shId"));
        List<StorehouseDto> storehouseDtos = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
        Assert.listOnlyOne(storehouseDtos, "仓库不存在");
        //todo 不允许采购
        if (!"ON".equals(storehouseDtos.get(0).getAllowPurchase())) {
            throw new CmdException(storehouseDtos.get(0).getShName() + "不允许采购");
        }
    }

    /**
     * 紧急采购-仓库物品入库
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     * @throws CmdException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        String userName = context.getReqHeaders().get("user-name");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setRow(1);
        userDto.setPage(1);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        userName = userDtos.get(0).getName();
        String storeId = context.getReqHeaders().get("store-id");
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setResOrderType(reqJson.getString("resOrderType"));
        //紧急采购入库
        purchaseApplyDto.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT);
        purchaseApplyDto.setCommunityId(reqJson.getString("communityId"));
        purchaseApplyDto.setUserId(userId);
        //查询当月数据标识，当值为1时查询当月数据
        purchaseApplyDto.setUrgentFlag("1");
        //查询当月紧急采购的次数
        int count = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplysCount(purchaseApplyDto);
        //取出开关映射的值
        String value = CommunitySettingFactory.getValue(purchaseApplyDto.getCommunityId(), URGRNT_NUMBER);
        if (StringUtil.isEmpty(value)) {
            value = MappingCache.getValue(DOMAIN_COMMON, URGRNT_NUMBER);
        }
        if (StringUtil.isEmpty(value)) {
            throw new IllegalArgumentException("映射值为空！");
        }
        int number = Integer.parseInt(value);
        if (count >= number) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "本月紧急采购次数已超过" + number + "次，请下月再使用！");
            context.setResponseEntity(responseEntity);
            return;
        }
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
        purchaseApplyPo.setDescription(reqJson.getString("description"));
        purchaseApplyPo.setUserId(userId);
        purchaseApplyPo.setUserName(userName);
        purchaseApplyPo.setEndUserName(reqJson.getString("endUserName"));
        purchaseApplyPo.setEndUserTel(reqJson.getString("endUserTel"));
        purchaseApplyPo.setStoreId(storeId);
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_ENTER);
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_END);
        purchaseApplyPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        purchaseApplyPo.setDescription(reqJson.getString("description"));
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT);
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        //获取采购物品信息
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPo.setPurchaseQuantity(resourceStore.get("quantity").toString());
            purchaseApplyDetailPo.setPurchaseRemark(resourceStore.get("remark").toString());
            purchaseApplyDetailPo.setOriginalStock(resourceStore.getString("stock"));
            purchaseApplyDetailPo.setPrice(resourceStore.getString("urgentPrice"));
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
            //查询当前小区仓库下该物品信息
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResCode(resourceStore.getString("resCode"));
            if (StringUtil.isEmpty(resourceStore.getString("shzId"))) {
                resourceStore.put("shzId", resourceStore.getString("shId"));
            }
            resourceStoreDto.setShId(resourceStore.getString("shzId")); //小区目标仓库
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            //调整集团仓库物品信息
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setPurchasePrice(resourceStore.getString("urgentPrice"));
            resourceStorePo.setStock(purchaseApplyDetailPo.getQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT);
            resourceStorePo.setOperationType(PurchaseApplyDto.WEIGHTED_MEAN_TRUE);
            resourceStorePo.setAdjustmentType("1001");
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            // 保存至 物品 times表  (调整原仓库 批次)
            ResourceStoreTimesPo resourceStoreTimesPo1 = new ResourceStoreTimesPo();
            resourceStoreTimesPo1.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
            resourceStoreTimesPo1.setPrice(purchaseApplyDetailPo.getPrice());//采购价
            resourceStoreTimesPo1.setResCode(resourceStore.getString("resCode"));
            resourceStoreTimesPo1.setStock("0");
            resourceStoreTimesPo1.setStoreId(storeId);
            resourceStoreTimesPo1.setShId(resourceStore.getString("shId"));
            resourceStoreTimesPo1.setCommunityId(reqJson.getString("communityId"));
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo1);
            AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
            if (resourceStoreDtos != null && resourceStoreDtos.size() == 1) {//目标仓库有此物品
                //生成调拨详情记录
                allocationStorehouseDto.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
                allocationStorehouseDto.setbId("-1");
                allocationStorehouseDto.setShIda(resourceStore.getString("shId"));//原仓库
                allocationStorehouseDto.setShIdz(resourceStore.getString("shzId"));//目标仓库
                allocationStorehouseDto.setResId(resourceStoreDtos.get(0).getResId());
                allocationStorehouseDto.setResName(resourceStoreDtos.get(0).getResName());
                allocationStorehouseDto.setStoreId(storeId);
                allocationStorehouseDto.setStock(purchaseApplyDetailPo.getQuantity());
                allocationStorehouseDto.setStartUserId(userId);
                allocationStorehouseDto.setStartUserName(userName);
                allocationStorehouseDto.setCreateTime(new Date());
                allocationStorehouseDto.setRemark("紧急采购入库并调拨");
                allocationStorehouseDto.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
                allocationStorehouseDto.setOriginalStock(resourceStoreDtos.get(0).getStock());
                allocationStorehouseInnerServiceSMOImpl.saveAllocationStorehouses(allocationStorehouseDto);
                //生成调拨申请记录
                AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
                allocationStorehouseApplyDto.setApplyId(allocationStorehouseDto.getApplyId());
                allocationStorehouseApplyDto.setbId("-1");
                allocationStorehouseApplyDto.setStartUserId(allocationStorehouseDto.getStartUserId());
                allocationStorehouseApplyDto.setStartUserName(allocationStorehouseDto.getStartUserName());
                allocationStorehouseApplyDto.setRemark("紧急采购入库并调拨");
                allocationStorehouseApplyDto.setApplyCount(purchaseApplyDetailPo.getQuantity());
                allocationStorehouseApplyDto.setStoreId(storeId);
                allocationStorehouseApplyDto.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                allocationStorehouseApplyDto.setState(AllocationStorehouseDto.STATE_SUCCESS);
                allocationStorehouseApplyDto.setCommunityId(reqJson.getString("communityId"));
                //调拨
                allocationStorehouseApplyDto.setApplyType("30000");
                allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto);


                //被调拨详情明细
                AllocationStorehouseDto allocationStorehouseDto1 = allocationStorehouseDto;
                allocationStorehouseDto1.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
                allocationStorehouseDto1.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
                allocationStorehouseDto1.setResId(resourceStore.getString("resId"));
                allocationStorehouseDto1.setCreateTime(new Date());
                allocationStorehouseInnerServiceSMOImpl.saveAllocationStorehouses(allocationStorehouseDto1);

                //加入被调拨申请记录
                AllocationStorehouseApplyDto allocationStorehouseApplyDto1 = allocationStorehouseApplyDto;
                allocationStorehouseApplyDto1.setApplyId(allocationStorehouseDto1.getApplyId());
                allocationStorehouseApplyDto1.setApplyType("40000");//被调拨记录
                allocationStorehouseApplyDto1.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                allocationStorehouseApplyDto1.setCommunityId(resourceStore.getString("communityId"));
                allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto1);

                //调整小区仓库物品均价、数量
                ResourceStoreDto resourceStoreDto2 = new ResourceStoreDto();
                resourceStoreDto2.setResCode(resourceStore.getString("resCode"));
                resourceStoreDto2.setResId(purchaseApplyDetailPo.getResId());
                List<ResourceStoreDto> resourceStoreDtoList = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto2);
                ResourceStorePo resourceStorePo1 = new ResourceStorePo();
                resourceStorePo1.setPurchasePrice(resourceStore.getString("urgentPrice"));
                resourceStorePo1.setResId(resourceStoreDtos.get(0).getResId());
                resourceStorePo1.setStock(purchaseApplyDetailPo.getQuantity());
                resourceStorePo1.setAveragePrice(resourceStoreDtoList.get(0).getAveragePrice());
                resourceStorePo1.setPrice(resourceStoreDtoList.get(0).getPrice());
                resourceStorePo1.setResOrderType(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT);
                resourceStorePo1.setOperationType(PurchaseApplyDto.WEIGHTED_MEAN_FALSE);
                //获取紧急采购数量
                BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getQuantity());
                //获取原有最小计量总数
                if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())) {
                    throw new IllegalArgumentException("最小计量总数不能为空！");
                }
                BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock());
                //获取最小计量单位数量
                if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                    throw new IllegalArgumentException("最小计量单位数量不能为空！");
                }
                BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock());
                //计算最小计量总数
                BigDecimal newMiniStock = purchaseQuantity.multiply(miniUnitStock).add(miniStock);
                resourceStorePo1.setMiniStock(String.valueOf(newMiniStock));
                resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo1);
            } else if (resourceStoreDtos != null && resourceStoreDtos.size() > 1) {
                throw new IllegalArgumentException("查询商品错误！");
            } else {
                //获取小区新增物品信息ResId
                String newResId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_resId);
                //生成调拨详情记录
                allocationStorehouseDto.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
                allocationStorehouseDto.setbId("-1");
                allocationStorehouseDto.setShIda(resourceStore.getString("shId"));
                allocationStorehouseDto.setShIdz(resourceStore.getString("shzId"));//小区目标仓库
                allocationStorehouseDto.setResId(newResId);
                allocationStorehouseDto.setResName(resourceStore.getString("resName"));
                allocationStorehouseDto.setStoreId(storeId);
                allocationStorehouseDto.setStock(purchaseApplyDetailPo.getQuantity());
                allocationStorehouseDto.setStartUserId(userId);
                allocationStorehouseDto.setStartUserName(userName);
                allocationStorehouseDto.setCreateTime(new Date());
                allocationStorehouseDto.setRemark("紧急采购入库并调拨");
                allocationStorehouseDto.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
                allocationStorehouseDto.setOriginalStock("0");
                allocationStorehouseInnerServiceSMOImpl.saveAllocationStorehouses(allocationStorehouseDto);
                //生成调拨申请记录
                AllocationStorehouseApplyDto allocationStorehouseApplyDto = new AllocationStorehouseApplyDto();
                allocationStorehouseApplyDto.setApplyId(allocationStorehouseDto.getApplyId());
                allocationStorehouseApplyDto.setbId("-1");
                allocationStorehouseApplyDto.setStartUserId(allocationStorehouseDto.getStartUserId());
                allocationStorehouseApplyDto.setStartUserName(allocationStorehouseDto.getStartUserName());
                allocationStorehouseApplyDto.setRemark("紧急采购入库并调拨");
                allocationStorehouseApplyDto.setApplyCount(purchaseApplyDetailPo.getQuantity());
                allocationStorehouseApplyDto.setStoreId(storeId);
                allocationStorehouseApplyDto.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                allocationStorehouseApplyDto.setState(AllocationStorehouseDto.STATE_SUCCESS);
                allocationStorehouseApplyDto.setCommunityId(reqJson.getString("communityId"));
                //调拨
                allocationStorehouseApplyDto.setApplyType("30000");
                allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto);

                //加入被调拨详情记录
                //String newApplyId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId);
                AllocationStorehouseDto allocationStorehouseDto1 = allocationStorehouseDto;
                allocationStorehouseDto1.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
                allocationStorehouseDto1.setApplyId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyId));
                allocationStorehouseDto1.setResId(resourceStore.getString("resId"));
                allocationStorehouseDto1.setCreateTime(new Date());
                allocationStorehouseInnerServiceSMOImpl.saveAllocationStorehouses(allocationStorehouseDto1);

                //加入被调拨申请记录
                AllocationStorehouseApplyDto allocationStorehouseApplyDto1 = allocationStorehouseApplyDto;
                allocationStorehouseApplyDto1.setApplyId(allocationStorehouseDto1.getApplyId());
                allocationStorehouseApplyDto1.setApplyType("40000");//被调拨记录
                allocationStorehouseApplyDto1.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                allocationStorehouseApplyDto1.setCommunityId(resourceStore.getString("communityId"));
                allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto1);

                //小区仓库进行入库插入
                ResourceStoreDto resourceStoreDto2 = new ResourceStoreDto();
                resourceStoreDto2.setResCode(resourceStore.getString("resCode"));
                resourceStoreDto2.setResId(purchaseApplyDetailPo.getResId());
                List<ResourceStoreDto> resourceStoreDtoList = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto2);
                ResourceStoreDto resourceStoreDto1 = BeanConvertUtil.covertBean(resourceStoreDtoList.get(0), ResourceStoreDto.class);
                resourceStoreDto1.setResId(newResId);
                resourceStoreDto1.setShId(resourceStore.getString("shzId"));
                resourceStoreDto1.setbId("-1");
                resourceStoreDto1.setStock(purchaseApplyDetailPo.getQuantity());
                resourceStoreDto1.setCreateTime(new Date());
                resourceStoreDto1.setCommunityId(resourceStoreTimesPo1.getCommunityId());
                //获取紧急采购数量
                BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getQuantity());
                //获取最小计量单位数量
                if (StringUtil.isEmpty(resourceStore.getString("miniUnitStock"))) {
                    throw new IllegalArgumentException("最小计量单位数量不能为空！");
                }
                BigDecimal miniUnitStock = new BigDecimal(resourceStore.getString("miniUnitStock"));
                //计算最小计量总数
                BigDecimal miniStock = purchaseQuantity.multiply(miniUnitStock);
                resourceStoreDto1.setMiniStock(String.valueOf(miniStock));
                resourceStoreInnerServiceSMOImpl.saveResourceStore(resourceStoreDto1);
            }
            //查询模板仓库所属CommunityId
            // 保存至 物品 times表  (调整目标仓库 批次) resourceStore.getString("shzId")
            StorehouseDto storehouseDto = new StorehouseDto();
            storehouseDto.setShId(resourceStore.getString("shzId"));
            List<StorehouseDto> storehouseDtoList = storehouseV1InnerServiceSMOImpl.queryStorehouses(storehouseDto);
            String storehouseCommunityId = "";
            if (storehouseDtoList.size() > 0) {
                storehouseCommunityId = storehouseDtoList.get(0).getCommunityId();
            }
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(allocationStorehouseDto.getApplyId());
            resourceStoreTimesPo.setPrice(purchaseApplyDetailPo.getPrice());
            resourceStoreTimesPo.setResCode(resourceStore.getString("resCode"));
            resourceStoreTimesPo.setStock(purchaseApplyDetailPo.getQuantity());
            resourceStoreTimesPo.setStoreId(storeId);
            resourceStoreTimesPo.setShId(resourceStore.getString("shzId"));
            resourceStoreTimesPo.setCommunityId(storehouseCommunityId);
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        ResponseEntity responseEntity = purchaseApplyBMOImpl.apply(purchaseApplyPo, reqJson);
        context.setResponseEntity(responseEntity);
    }
}
