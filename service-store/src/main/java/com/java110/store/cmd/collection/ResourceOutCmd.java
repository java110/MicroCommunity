package com.java110.store.cmd.collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.userStorehouse.UserStorehouseDto;
import com.java110.intf.store.*;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * 采购人员入库 功能
 * 请求地址为/app/purchase/resourceEnter
 */

@Java110CmdDoc(title = "领用出库",
        description = "主要用于 仓库管理员领用出库",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/collection/resourceOut",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "collection.resourceOut"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "applyOrderId", length = 30, remark = "采购申请单订单ID"),
        @Java110ParamDoc(name = "purchaseApplyDetailVo", type = "Array", length = 30, remark = "采购物品信息"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "purchaseQuantity", type = "Int", length = 30, remark = "数量"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "id", type = "String", length = 30, remark = "采购明细ID"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "resId", type = "String", length = 30, remark = "物品ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{'applyOrderId':'123123','purchaseApplyDetailVo':[{'purchaseQuantity':'10','id':'123123','resId':'343434'}]}",
        resBody = "{'code':0,'msg':'成功'}"
)

/**
 * 物品领用申请出库
 */
@Java110Cmd(serviceCode = "/collection/resourceOut")
public class ResourceOutCmd extends Cmd {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单ID为空");
        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(purchaseApplyDetail, "purchaseQuantity", "采购数量未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "id", "明细ID为空");

        }
    }

    /**
     * 物品领用-物品领用物品发放
     * @param event              事件对象
     * @param context 数据上文对象
     * @param reqJson            请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(purchaseApplyDetail, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(reqJson.getString("applyOrderId"));
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);

        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        Assert.listOnlyOne(purchaseApplyDtos, "出库单不存在");
        purchaseApplyDetailPos = purchaseApplyPo.getPurchaseApplyDetailPos();
        for (PurchaseApplyDetailPo purchaseApplyDetailPo : purchaseApplyDetailPos) {
            purchaseApplyDetailInnerServiceSMOImpl.updatePurchaseApplyDetail(purchaseApplyDetailPo);
            //查询物品资源信息
            ResourceStoreDto resourceStore = new ResourceStoreDto();
            resourceStore.setResId(purchaseApplyDetailPo.getResId());
            List<ResourceStoreDto> resourceStores = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStore);
            Assert.listOnlyOne(resourceStores, "查询物品资源信息错误！");
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setStock("-" + purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
            //获取原物品最小计量总数
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            BigDecimal miniStock1 = new BigDecimal(resourceStores.get(0).getMiniStock());
            BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
            if (StringUtil.isEmpty(resourceStores.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            //获取物品最小计量单位数量
            BigDecimal miniUnitStock1 = new BigDecimal(resourceStores.get(0).getMiniUnitStock());
            //计算领用物品的最小计量总数
            BigDecimal applyQuantity = purchaseQuantity.multiply(miniUnitStock1);
            //计算物品领用后剩余的最小计量总数
            BigDecimal newMiniStock = miniStock1.subtract(applyQuantity);
            if (newMiniStock.compareTo(BigDecimal.ZERO) == -1) {
                throw new IllegalArgumentException("物品库存已经不足，请确认物品库存！");
            }
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);

            //加入 从库存中扣减
            subResourceStoreTimesStock(resourceStores.get(0).getResCode(), purchaseApplyDetailPo);

            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(purchaseApplyDetailPo.getResId());
            //查询物品资源
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            if (resourceStoreDtos == null || resourceStoreDtos.size() < 1) {
                continue;
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
            userStorehousePo.setResCode(resourceStoreDtos.get(0).getResCode());
            userStorehousePo.setResName(resourceStoreDtos.get(0).getResName());
            userStorehousePo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            userStorehousePo.setUserId(purchaseApplyDtos.get(0).getUserId());
            userStorehousePo.setTimesId(purchaseApplyDetailPo.getTimesId());
            //查询物品 是否已经存在
            UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
            userStorehouseDto.setResCode(resourceStoreDtos.get(0).getResCode());
            userStorehouseDto.setUserId(purchaseApplyDtos.get(0).getUserId());
            userStorehouseDto.setStoreId(resourceStoreDtos.get(0).getStoreId());
            List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
            if (userStorehouseDtos == null || userStorehouseDtos.size() < 1) {
                userStorehousePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
                if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                    //获取领取数量
                    BigDecimal purchaseQuantity2 = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
                    BigDecimal miniUnitStock2 = new BigDecimal(miniUnitStock);
                    //计算个人物品最小计量总数
                    BigDecimal quantity = purchaseQuantity2.multiply(miniUnitStock2);
                    userStorehousePo.setMiniStock(String.valueOf(quantity));
                } else {
                    userStorehousePo.setMiniStock(purchaseApplyDetailPo.getPurchaseQuantity());
                }
                userStorehouseInnerServiceSMOImpl.saveUserStorehouses(userStorehousePo);
            } else {
                //获取个人物品领用后的库存
                BigDecimal purchaseQuantity3 = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
                BigDecimal stock3 = new BigDecimal(userStorehouseDtos.get(0).getStock());
                BigDecimal total = purchaseQuantity3.add(stock3);
                userStorehousePo.setStock(total.toString());
                userStorehousePo.setUsId(userStorehouseDtos.get(0).getUsId());
                if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                    //获取本次领取数量
                    BigDecimal miniUnitStock3 = new BigDecimal(miniUnitStock);
                    //计算本次领取的个人物品最小计量总数
                    BigDecimal quantity = purchaseQuantity3.multiply(miniUnitStock3);
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

        //
        //获取订单号
        String applyOrderId = purchaseApplyPo.getApplyOrderId();
        PurchaseApplyPo purchaseApply = new PurchaseApplyPo();
        purchaseApply.setApplyOrderId(applyOrderId);
        purchaseApply.setState(PurchaseApplyDto.STATE_AUDITED);
        purchaseApply.setStatusCd("0");
        purchaseApplyInnerServiceSMOImpl.updatePurchaseApply(purchaseApply);
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "出库成功"));
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
        //resourceStoreTimesDto.setHasStock("Y");
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
