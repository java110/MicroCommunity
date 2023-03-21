package com.java110.store.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.intf.store.IUserStorehouseInnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.store.bmo.collection.IGetCollectionAuditOrderBMO;
import com.java110.store.bmo.collection.IGoodsCollectionBMO;
import com.java110.store.bmo.collection.IResourceOutBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品领用 接口类
 */

@RestController
@RequestMapping(value = "/collection")
public class CollectionApi {

    @Autowired
    private IGoodsCollectionBMO goodsCollectionBMOImpl;

    @Autowired
    private IGetCollectionAuditOrderBMO getCollectionAuditOrderBMOImpl;

    @Autowired
    private IResourceOutBMO resourceOutBMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;


    /**
     * 物品领用 接口类
     *
     * @param reqJson
     * @param userId
     * @param storeId
     * @return {"resourceStores":[{"resId":"852020070239060001","resName":"水性笔","resCode":"002","price":"2.00","stock":"2",
     * "description":"黑色","quantity":"1"}],"description":"123123","endUserName":"1","endUserTel":"17797173942","file":"",
     * "resOrderType":"20000","staffId":"","staffName":""}
     */
    @RequestMapping(value = "/goodsCollection", method = RequestMethod.POST)
    public ResponseEntity<String> goodsCollection(@RequestBody JSONObject reqJson,
                                                  @RequestHeader(value = "user-id") String userId,
                                                  @RequestHeader(value = "store-id") String storeId) {

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos,"未包含用户");


        String userName  = userDtos.get(0).getName();

        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写物品领用的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
        purchaseApplyPo.setDescription(reqJson.getString("description"));
        purchaseApplyPo.setUserId(userId);
        purchaseApplyPo.setUserName(userName);
        purchaseApplyPo.setEndUserName(reqJson.getString("endUserName"));
        purchaseApplyPo.setEndUserTel(reqJson.getString("endUserTel"));
        purchaseApplyPo.setStoreId(storeId);
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
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
            resourceStore.put("originalStock", resourceStore.get("stock"));
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return goodsCollectionBMOImpl.collection(purchaseApplyPo,reqJson);
    }

    /**
     * 查询审核单
     *
     * @param page
     * @param row
     * @param userId
     * @param storeId
     * @return
     */
    @RequestMapping(value = "/getCollectionAuditOrder", method = RequestMethod.GET)
    public ResponseEntity<String> getCollectionAuditOrder(@RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row,
                                                          @RequestParam(value = "communityId") String communityId,
                                                          @RequestHeader(value = "user-id") String userId,
                                                          @RequestHeader(value = "store-id") String storeId) {
        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(userId);
        auditUser.setPage(page);
        auditUser.setRow(row);
        auditUser.setStoreId(storeId);
        auditUser.setCommunityId(communityId);
        return getCollectionAuditOrderBMOImpl.auditOrder(auditUser);
    }

    /**
     * 物品发放(物品发放之后直接到个人手中)
     * {"resourceOuts":[],"applyOrderId":"152020071665420001","taskId":"237506","resOrderType":"20000",
     * "purchaseApplyDetailVo":[{"applyOrderId":"152020071665420001","id":"152020071690120002","price":"","quantity":"1",
     * "resCode":"002","resId":"852020070239060001","resName":"水性笔","stock":"2","purchaseQuantity":"2","purchaseRemark":""}]}
     */

    @RequestMapping(value = "/resourceOut", method = RequestMethod.POST)
    public ResponseEntity<String> resourceOut(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单ID为空");
        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(purchaseApplyDetail, "purchaseQuantity", "采购数量未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "id", "明细ID为空");
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(purchaseApplyDetail, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(reqJson.getString("applyOrderId"));
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return resourceOutBMOImpl.out(purchaseApplyPo);
    }

    /**
     * 物品领用-物品直接出库
     */
    @RequestMapping(value = "/goodsDelivery", method = RequestMethod.POST)
    public ResponseEntity<String> goodsDelivery(@RequestBody JSONObject reqJson,
                                                @RequestHeader(value = "user-id") String userId,
                                                @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写物品领用的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos,"未包含用户");


        String userName  = userDtos.get(0).getName();

        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
        purchaseApplyPo.setDescription(reqJson.getString("description"));
        purchaseApplyPo.setUserId(reqJson.getString("receiverUserId"));
        purchaseApplyPo.setUserName(reqJson.getString("receiverUserName"));
        purchaseApplyPo.setEndUserName(reqJson.getString("endUserName"));
        purchaseApplyPo.setEndUserTel(reqJson.getString("endUserTel"));
        purchaseApplyPo.setStoreId(storeId);
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_END);
        purchaseApplyPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT);
        purchaseApplyPo.setDescription("直接出库操作");
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setPrice(resourceStore.getString("contrastPrice"));
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPo.setQuantity(purchaseApplyDetailPo.getPurchaseQuantity());
            purchaseApplyDetailPo.setRemark("直接出库");
            purchaseApplyDetailPo.setOriginalStock(resourceStore.getString("stock"));
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
            //调整总库存
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setStock("-" + purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
            //计算出库后的最小计量总数
            BigDecimal oldMiniStock = new BigDecimal(resourceStore.getString("miniStock")); //获取原先的最小计量总数
            BigDecimal oldMiniUnitStock = new BigDecimal(resourceStore.getString("miniUnitStock")); //获取最小计量单位数量
            BigDecimal nowQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity()); //获取出库数量
            BigDecimal nowMiniStock = nowQuantity.multiply(oldMiniUnitStock); //计算当前出库的最小计量总数
            BigDecimal surplusMiniStock = oldMiniStock.subtract(nowMiniStock);
            if (surplusMiniStock.compareTo(BigDecimal.ZERO) == -1) {
                throw new IllegalArgumentException("物品库存已经不足，请确认物品库存！");
            }
            resourceStorePo.setMiniStock(String.valueOf(surplusMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);

            //加入 从库存中扣减
            subResourceStoreTimesStock(resourceStore.getString("resCode"), purchaseApplyDetailPo);

            //查询资源
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(purchaseApplyDetailPo.getResId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            if (resourceStoreDtos == null || resourceStoreDtos.size() < 1) {
                throw new IllegalArgumentException("未查询到物品资源信息！");
            }
            //获取物品单位
            String unitCode = resourceStoreDtos.get(0).getUnitCode();
            //获取物品最小计量单位
            String miniUnitCode = resourceStoreDtos.get(0).getMiniUnitCode();
            //获取物品最小计量单位数量
            String miniUnitStock = resourceStoreDtos.get(0).getMiniUnitStock();
            //入库到个人仓库中
            UserStorehousePo userStorehousePo = new UserStorehousePo();
            userStorehousePo.setUsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));
            userStorehousePo.setResId(resourceStoreDtos.get(0).getResId());
            userStorehousePo.setResName(resourceStoreDtos.get(0).getResName());
            userStorehousePo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            userStorehousePo.setUserId(purchaseApplyPo.getUserId());
            userStorehousePo.setTimesId(purchaseApplyDetailPo.getTimesId());

            //查询个人物品仓库中 是否已经存在商品
            UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
            userStorehouseDto.setResCode(resourceStoreDtos.get(0).getResCode());
            userStorehouseDto.setUserId(purchaseApplyPo.getUserId());
            userStorehouseDto.setStoreId(resourceStoreDtos.get(0).getStoreId());
            List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
            userStorehousePo.setResCode(resourceStoreDtos.get(0).getResCode());
            if (userStorehouseDtos == null || userStorehouseDtos.size() < 1) {
                userStorehousePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
                if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                    //获取领取数量
                    BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
                    //计算个人物品最小计量总数
                    BigDecimal miniUnitStock1 = new BigDecimal(miniUnitStock);
                    BigDecimal quantity = purchaseQuantity.multiply(miniUnitStock1);
                    userStorehousePo.setMiniStock(String.valueOf(quantity));
                } else {
                    userStorehousePo.setMiniStock(purchaseApplyDetailPo.getPurchaseQuantity());
                }
                userStorehouseInnerServiceSMOImpl.saveUserStorehouses(userStorehousePo);
            } else {
                //获取个人物品领用后的库存
                BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
                BigDecimal stock1 = new BigDecimal(userStorehouseDtos.get(0).getStock());
                BigDecimal total = purchaseQuantity.add(stock1);
                userStorehousePo.setStock(total + "");
                userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
                if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                    //获取本次领取数量
                    BigDecimal miniUnitStock1 = new BigDecimal(miniUnitStock);
                    //计算本次领取的个人物品最小计量总数
                    BigDecimal quantity = purchaseQuantity.multiply(miniUnitStock1);
                    BigDecimal miniStock = new BigDecimal(0);
                    //获取个人物品原先的最小计量总数
                    if (StringUtil.isEmpty(userStorehouseDtos.get(0).getMiniStock())) {
                        throw new IllegalArgumentException("信息错误，个人物品最小计量总数不能为空！");
                    } else {
                        miniStock = new BigDecimal(userStorehouseDtos.get(0).getMiniStock());
                    }
                    //计算领用后个人物品总的最小计量总数
                    BigDecimal miniQuantity = quantity.add(miniStock);
                    userStorehousePo.setMiniStock(String.valueOf(miniQuantity));
                } else {
                    userStorehousePo.setMiniStock(String.valueOf(total));
                }
                userStorehouseInnerServiceSMOImpl.updateUserStorehouses(userStorehousePo);
            }
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        return goodsCollectionBMOImpl.collection(purchaseApplyPo,reqJson);
    }

    /**
     * 从times中扣减
     *
     * @param resCode
     * @param purchaseApplyDetailPo
     */
    private void subResourceStoreTimesStock(String resCode, PurchaseApplyDetailPo purchaseApplyDetailPo) {
        String applyQuantity = purchaseApplyDetailPo.getPurchaseQuantity();
        ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
        resourceStoreTimesDto.setResCode(resCode);
        resourceStoreTimesDto.setTimesId(purchaseApplyDetailPo.getTimesId());
        List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);

        if (resourceStoreTimesDtos == null || resourceStoreTimesDtos.size() < 1) {
            return;
        }
        int stock = 0;
        int quantity = Integer.parseInt(applyQuantity);
        ResourceStoreTimesPo resourceStoreTimesPo = null;

        stock = Integer.parseInt(resourceStoreTimesDtos.get(0).getStock());
        if (stock < quantity) {
            throw new CmdException(resourceStoreTimesDtos.get(0).getResCode() + "价格为：" + resourceStoreTimesDtos.get(0).getPrice() + "的库存" + resourceStoreTimesDtos.get(0).getStock() + ",库存不足");
        }

        stock = stock - quantity;
        resourceStoreTimesPo = new ResourceStoreTimesPo();
        resourceStoreTimesPo.setTimesId(resourceStoreTimesDtos.get(0).getTimesId());
        resourceStoreTimesPo.setStock(stock + "");
        resourceStoreTimesV1InnerServiceSMOImpl.updateResourceStoreTimes(resourceStoreTimesPo);
    }
}
