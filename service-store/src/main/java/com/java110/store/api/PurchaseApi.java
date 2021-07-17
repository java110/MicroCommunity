package com.java110.store.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.allocationStorehouse.AllocationStorehouseDto;
import com.java110.dto.allocationStorehouseApply.AllocationStorehouseApplyDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.storehouse.StorehouseDto;
import com.java110.intf.store.*;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.store.bmo.purchase.IPurchaseApplyBMO;
import com.java110.store.bmo.purchase.IResourceEnterBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/purchase")
public class PurchaseApi {

    @Autowired
    private IPurchaseApplyBMO purchaseApplyBMOImpl;

    @Autowired
    private IResourceEnterBMO resourceEnterBMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IStorehouseInnerServiceSMO storehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    //域
    public static final String DOMAIN_COMMON = "DOMAIN.COMMON";

    //键
    public static final String URGRNT_NUMBER = "URGRNT_NUMBER";

    /**
     * 采购申请
     * <p>
     * {"resourceStores":[{"resId":"852020061636590016","resName":"橡皮擦","resCode":"003","price":"100.00","stock":"0","description":"ada","quantity":"1"},
     * {"resId":"852020061729120031","resName":"文档柜","resCode":"002","price":"33.00","stock":"0","description":"蓝色","quantity":"1"}],
     * "description":"123123","endUserName":"1","endUserTel":"17797173942","file":"","resOrderType":"10000","staffId":"","staffName":""}
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/purchaseApply", method = RequestMethod.POST)
    public ResponseEntity<String> purchaseApply(@RequestBody JSONObject reqJson,
                                                @RequestHeader(value = "user-id") String userId,
                                                @RequestHeader(value = "user-name") String userName,
                                                @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写申请采购的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写申请类型");
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
        purchaseApplyPo.setDescription(reqJson.getString("description"));
        purchaseApplyPo.setUserId(userId);
        purchaseApplyPo.setUserName(userName);
        purchaseApplyPo.setEndUserName(reqJson.getString("endUserName"));
        purchaseApplyPo.setEndUserTel(reqJson.getString("endUserTel"));
        purchaseApplyPo.setStoreId(storeId);
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_ENTER);
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_WAIT_DEAL);
        purchaseApplyPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_APPLY);
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            resourceStore.remove("price");//采购价格默认空
            resourceStore.put("originalStock", resourceStore.getString("stock"));
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return purchaseApplyBMOImpl.apply(purchaseApplyPo);
    }

    @RequestMapping(value = "/resourceEnter", method = RequestMethod.POST)
    public ResponseEntity<String> resourceEnter(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单ID为空");
        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(purchaseApplyDetail, "purchaseQuantity", "采购数量未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "price", "采购单价未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "id", "明细ID为空");
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(purchaseApplyDetail, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(reqJson.getString("applyOrderId"));
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return resourceEnterBMOImpl.enter(purchaseApplyPo);
    }

    /**
     * 直接入库操作
     * <p>
     * {"resourceStores":[{"resId":"852020061636590016","resName":"橡皮擦","resCode":"003","price":"100.00","stock":"0","description":"ada","quantity":"1"},
     * {"resId":"852020061729120031","resName":"文档柜","resCode":"002","price":"33.00","stock":"0","description":"蓝色","quantity":"1"}],
     * "description":"123123","endUserName":"1","endUserTel":"17797173942","file":"","resOrderType":"10000","staffId":"","staffName":""}
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/purchaseStorage", method = RequestMethod.POST)
    public ResponseEntity<String> purchaseStorage(@RequestBody JSONObject reqJson,
                                                  @RequestHeader(value = "user-id") String userId,
                                                  @RequestHeader(value = "user-name") String userName,
                                                  @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写申请采购的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写申请类型");
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
        purchaseApplyPo.setDescription("直接采购入库");
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT);
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPo.setRemark("直接采购入库");
            purchaseApplyDetailPo.setOriginalStock(resourceStore.getString("stock"));
            purchaseApplyDetailPo.setQuantity(purchaseApplyDetailPo.getPurchaseQuantity());
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
            //增加库存
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setPurchasePrice(purchaseApplyDetailPo.getPrice());
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_ENTER);
            //获取采购数量
            double purchaseQuantity = Double.parseDouble(purchaseApplyDetailPo.getPurchaseQuantity());
            //获取原有最小计量总数
            double miniStock = Double.parseDouble(resourceStore.getString("miniStock"));
            //获取最小单位数量
            double newMiniStock = 0.0;
            if (StringUtil.isEmpty(resourceStore.getString("miniUnitStock"))) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            double miniUnitStock = Double.parseDouble(resourceStore.getString("miniUnitStock"));
            //计算最小计量总数
            if (StringUtil.isEmpty(resourceStore.getString("miniStock"))) {
                newMiniStock = purchaseQuantity * miniUnitStock;
            } else {
                newMiniStock = (purchaseQuantity * miniUnitStock) + miniStock;
            }
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return purchaseApplyBMOImpl.apply(purchaseApplyPo);
    }

    /**
     * 紧急采购入库
     *
     * @author fqz
     * @date 2021-07-07 15:46
     */
    @RequestMapping(value = "/urgentPurchaseApply", method = RequestMethod.POST)
    public ResponseEntity<String> urgentPurchaseApply(@RequestBody JSONObject reqJson,
                                                      @RequestHeader(value = "user-id") String userId,
                                                      @RequestHeader(value = "user-name") String userName,
                                                      @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写申请采购的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写申请类型");
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
        String value = MappingCache.getValue(DOMAIN_COMMON, URGRNT_NUMBER);
        if (StringUtil.isEmpty(value)) {
            throw new IllegalArgumentException("映射值为空！");
        }
        int number = Integer.parseInt(value);
        if (count > number) {
            throw new IllegalArgumentException("本月紧急采购次数已超过" + number + "次，请下月再使用！");
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
        //查询当前小区的小区仓库
        StorehouseDto storehouseDto = new StorehouseDto();
        storehouseDto.setShObjId(reqJson.getString("communityId"));
        storehouseDto.setShType(StorehouseDto.SH_TYPE_COMMUNITY);//小区仓库
        //获取当前小区的小区仓库
        List<StorehouseDto> storehouseDtos = storehouseInnerServiceSMOImpl.queryStorehouses(storehouseDto);
        //Assert.listOnlyOne(storehouseDtos, "没有查询到当前小区仓库或查询出多个小区仓库！");
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
            resourceStoreDto.setShId(storehouseDtos.get(0).getShId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            //调整集团仓库物品信息
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setPurchasePrice(resourceStore.getString("urgentPrice"));
            resourceStorePo.setStock(purchaseApplyDetailPo.getQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT);
            resourceStorePo.setOperationType(PurchaseApplyDto.WEIGHTED_MEAN_TRUE);
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            if (resourceStoreDtos != null && resourceStoreDtos.size() == 1) {
                //生成调拨记录
                AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
                allocationStorehouseDto.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
                allocationStorehouseDto.setbId("-1");
                allocationStorehouseDto.setShIda(resourceStore.getString("shId"));
                allocationStorehouseDto.setShIdz(storehouseDtos.get(0).getShId());
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
                allocationStorehouseApplyDto.setCreateTime(new Date());
                allocationStorehouseApplyDto.setState(AllocationStorehouseDto.STATE_SUCCESS);
                allocationStorehouseApplyDto.setCommunityId(reqJson.getString("communityId"));
                //调拨
                allocationStorehouseApplyDto.setApplyType("30000");
                allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto);

                //调整小区仓库物品均价、数量

                //集团仓库商品信息
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
                double purchaseQuantity = Double.parseDouble(purchaseApplyDetailPo.getQuantity());
                //获取原有最小计量总数
                if(StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())){
                    throw new IllegalArgumentException("最小计量总数不能为空！");
                }
                double miniStock = Double.parseDouble(resourceStoreDtos.get(0).getMiniStock());
                //获取最小计量单位数量
                if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                    throw new IllegalArgumentException("最小计量单位数量不能为空！");
                }
                double miniUnitStock = Double.parseDouble(resourceStoreDtos.get(0).getMiniUnitStock());
                //计算最小计量总数
                double newMiniStock = (purchaseQuantity * miniUnitStock) + miniStock;
                resourceStorePo1.setMiniStock(String.valueOf(newMiniStock));
                resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo1);
            } else if (resourceStoreDtos != null && resourceStoreDtos.size() > 1) {
                throw new IllegalArgumentException("查询商品错误！");
            } else {
                //生成调拨记录
                AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
                allocationStorehouseDto.setAsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_allocationStorehouseId));
                allocationStorehouseDto.setbId("-1");
                allocationStorehouseDto.setShIda(resourceStore.getString("shId"));
                allocationStorehouseDto.setShIdz(storehouseDtos.get(0).getShId());
                allocationStorehouseDto.setResId(resourceStore.getString("resId"));
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
                allocationStorehouseApplyDto.setCreateTime(new Date());
                allocationStorehouseApplyDto.setState(AllocationStorehouseDto.STATE_SUCCESS);
                allocationStorehouseApplyDto.setCommunityId(reqJson.getString("communityId"));
                //调拨
                allocationStorehouseApplyDto.setApplyType("30000");
                allocationStorehouseApplyInnerServiceSMOImpl.saveAllocationStorehouseApplys(allocationStorehouseApplyDto);
                //小区仓库进行入库插入
                //集团仓库商品信息
                ResourceStoreDto resourceStoreDto2 = new ResourceStoreDto();
                resourceStoreDto2.setResCode(resourceStore.getString("resCode"));
                resourceStoreDto2.setResId(purchaseApplyDetailPo.getResId());
                List<ResourceStoreDto> resourceStoreDtoList = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto2);
                ResourceStoreDto resourceStoreDto1 = BeanConvertUtil.covertBean(resourceStoreDtoList.get(0), ResourceStoreDto.class);
                resourceStoreDto1.setResId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_resId));
                resourceStoreDto1.setShId(storehouseDtos.get(0).getShId());
                resourceStoreDto1.setbId("-1");
                resourceStoreDto1.setStock(purchaseApplyDetailPo.getQuantity());
                resourceStoreDto1.setCreateTime(new Date());
                //获取紧急采购数量
                double purchaseQuantity = Double.parseDouble(purchaseApplyDetailPo.getQuantity());
                //获取最小计量单位数量
                if (StringUtil.isEmpty(resourceStore.getString("miniUnitStock"))) {
                    throw new IllegalArgumentException("最小计量单位数量不能为空！");
                }
                double miniUnitStock = Double.parseDouble(resourceStore.getString("miniUnitStock"));
                //计算最小计量总数
                double miniStock = purchaseQuantity * miniUnitStock;
                resourceStoreDto1.setMiniStock(String.valueOf(miniStock));
                resourceStoreInnerServiceSMOImpl.saveResourceStore(resourceStoreDto1);
            }
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return purchaseApplyBMOImpl.apply(purchaseApplyPo);
    }
}
