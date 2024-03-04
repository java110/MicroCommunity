package com.java110.store.cmd.collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.purchase.PurchaseApplyDetailDto;
import com.java110.dto.purchase.PurchaseApplyDto;
import com.java110.dto.resource.ResourceStoreDto;
import com.java110.dto.resource.ResourceStoreTimesDto;
import com.java110.dto.user.UserStorehouseDto;
import com.java110.intf.common.IOaWorkflowActivitiInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resource.ResourceStoreTimesPo;
import com.java110.po.user.UserStorehousePo;
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
        serviceCode = "collection.resourceOut",
        seq = 11
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "applyOrderId", length = 30, remark = "采购申请单订单ID"),
        @Java110ParamDoc(name = "purchaseApplyDetailVo", type = "Array", length = 30, remark = "采购物品信息"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "purchaseQuantity", type = "Double", length = 30, remark = "数量"),
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
        reqBody = "{'applyOrderId':'123123','purchaseApplyDetailVo':[{'purchaseQuantity':'10.00','id':'123123','resId':'343434'}]}",
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
    private IPurchaseApplyDetailV1InnerServiceSMO purchaseApplyDetailV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IUserStorehouseInnerServiceSMO userStorehouseInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowActivitiInnerServiceSMO oaWorkflowUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单ID为空");
        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        if (purchaseApplyDetails == null || purchaseApplyDetails.size() < 1) {
            throw new CmdException("未包含领用物品");
        }
        String storeId = CmdContextUtils.getStoreId(context);
        double quanitity = 0;
        double stock = 0;
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(purchaseApplyDetail, "purchaseQuantity", "采购数量未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "id", "明细ID为空");
            Assert.hasKeyAndValue(purchaseApplyDetail, "timesId", "价格为空");
            quanitity = Double.parseDouble(purchaseApplyDetail.getString("quantity"));
            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(purchaseApplyDetail.getString("timesId"));
            resourceStoreTimesDto.setStoreId(storeId);
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);
            Assert.listOnlyOne(resourceStoreTimesDtos, "价格不存在");
            if (quanitity < 1) {
                throw new CmdException("申请数量不正确");
            }
            stock = Double.parseDouble(resourceStoreTimesDtos.get(0).getStock());
            if (quanitity > stock) {
                throw new CmdException(resourceStoreTimesDtos.get(0).getResCode() + "出库不足,库存为=" + stock + ",申请数为=" + quanitity);
            }
        }
    }

    /**
     * 物品领用-物品领用物品发放
     *
     * @param event   事件对象
     * @param context 数据上文对象
     * @param reqJson 请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        List<PurchaseApplyDetailDto> purchaseApplyDetailPos = new ArrayList<>();
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            PurchaseApplyDetailDto purchaseApplyDetailDto = BeanConvertUtil.covertBean(purchaseApplyDetail, PurchaseApplyDetailDto.class);
            purchaseApplyDetailPos.add(purchaseApplyDetailDto);
        }
        PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
        purchaseApplyPo.setApplyOrderId(reqJson.getString("applyOrderId"));
        // purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        Assert.listOnlyOne(purchaseApplyDtos, "出库单不存在");
        for (PurchaseApplyDetailDto tmpPurchaseApplyDetailDto : purchaseApplyDetailPos) {
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(tmpPurchaseApplyDetailDto.getResId());
            resourceStorePo.setPurchasePrice(tmpPurchaseApplyDetailDto.getPrice());
            resourceStorePo.setStock("-" + tmpPurchaseApplyDetailDto.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
            //查询物品资源信息
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(tmpPurchaseApplyDetailDto.getResId());
            resourceStoreDto.setShId(tmpPurchaseApplyDetailDto.getShId());
            List<ResourceStoreDto> resourceStoreDtos = resourceStoreInnerServiceSMOImpl.queryResourceStores(resourceStoreDto);
            Assert.listOnlyOne(resourceStoreDtos, "查询物品资源信息错误！");
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniUnitStock())) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            //获取最小计量单位数量
            BigDecimal miniUnitStock = new BigDecimal(resourceStoreDtos.get(0).getMiniUnitStock());
            if (StringUtil.isEmpty(resourceStoreDtos.get(0).getMiniStock())) {
                throw new IllegalArgumentException("最小计量总数不能为空！");
            }
            //获取领用发放的数量
            BigDecimal quantity = new BigDecimal(tmpPurchaseApplyDetailDto.getQuantity());
            //原库存总数
            BigDecimal stock = new BigDecimal(resourceStoreDtos.get(0).getStock());
            //计算领用成功后剩余的库存总数
            BigDecimal newStock = stock.subtract(quantity);
            //计算领用成功后剩余的最小计量总数
            BigDecimal nowMiniStock = newStock.multiply(miniUnitStock);
            resourceStorePo.setMiniStock(String.valueOf(nowMiniStock));
//            //获取采购前物品最小计量总数
//            BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock());
//            //计算采购的物品最小计量总数
//            BigDecimal purchaseQuantity = new BigDecimal(tmpPurchaseApplyDetailDto.getPurchaseQuantity());
//            BigDecimal purchaseMiniStock = purchaseQuantity.multiply(miniUnitStock);
//            //计算采购后物品最小计量总数
//            BigDecimal nowMiniStock = miniStock.subtract(purchaseMiniStock);
//            if (nowMiniStock.compareTo(BigDecimal.ZERO) == -1) {
//                throw new IllegalArgumentException("物品库存已经不足，请确认物品库存！");
//            }
            //           resourceStorePo.setMiniStock(String.valueOf(nowMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);
            // 保存至 物品 times表
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(tmpPurchaseApplyDetailDto.getApplyOrderId());
            resourceStoreTimesPo.setPrice(tmpPurchaseApplyDetailDto.getPrice());
            resourceStoreTimesPo.setStock("-" + tmpPurchaseApplyDetailDto.getPurchaseQuantity());
            resourceStoreTimesPo.setResCode(resourceStoreDtos.get(0).getResCode());
            resourceStoreTimesPo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setShId(tmpPurchaseApplyDetailDto.getShId());
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
            //todo 个人仓库中添加
            addPersonStorehouse(purchaseApplyDtos.get(0), resourceStoreDtos, tmpPurchaseApplyDetailDto);
            //领用明细表里的领用数量更新为实际领用数量
            PurchaseApplyDetailPo purchaseApplyDetailPo = new PurchaseApplyDetailPo();
            purchaseApplyDetailPo.setApplyOrderId(tmpPurchaseApplyDetailDto.getApplyOrderId());
            purchaseApplyDetailPo.setPurchaseQuantity(tmpPurchaseApplyDetailDto.getPurchaseQuantity());
            purchaseApplyDetailV1InnerServiceSMOImpl.updatePurchaseApplyDetail(purchaseApplyDetailPo);
        }
        //获取订单号
        String applyOrderId = purchaseApplyPo.getApplyOrderId();
        PurchaseApplyPo purchaseApply = new PurchaseApplyPo();
        purchaseApply.setApplyOrderId(applyOrderId);
        if (reqJson.containsKey("taskId")) {
            reqJson.put("auditCode", "1100");
            reqJson.put("auditMessage", "入库成功");
            reqJson.put("id", reqJson.getString("applyOrderId"));
            reqJson.put("storeId", CmdContextUtils.getStoreId(context));
            reqJson.put("nextUserId", reqJson.getString("staffId"));
            boolean isLastTask = oaWorkflowUserInnerServiceSMOImpl.completeTask(reqJson);
            if (isLastTask) {
                purchaseApply.setState(PurchaseApplyDto.STATE_END);
            } else {
                purchaseApply.setState(PurchaseApplyDto.STATE_DEALING);
            }
        } else {
            purchaseApply.setState(PurchaseApplyDto.STATE_AUDITED);
        }
        purchaseApply.setStatusCd("0");
        purchaseApplyInnerServiceSMOImpl.updatePurchaseApply(purchaseApply);
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "出库成功"));
    }

    /**
     * 向个人仓库中添加数据
     *
     * @param resourceStoreDtos
     */
    private void addPersonStorehouse(PurchaseApplyDto purchaseApplyDto, List<ResourceStoreDto> resourceStoreDtos, PurchaseApplyDetailDto purchaseApplyDetailDto) {
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
        userStorehousePo.setUserId(purchaseApplyDto.getUserId());
        userStorehousePo.setTimesId(purchaseApplyDetailDto.getTimesId());
        //查询物品 是否已经存在
        UserStorehouseDto userStorehouseDto = new UserStorehouseDto();
        userStorehouseDto.setResCode(resourceStoreDtos.get(0).getResCode());
        userStorehouseDto.setUserId(purchaseApplyDto.getUserId());
        userStorehouseDto.setStoreId(resourceStoreDtos.get(0).getStoreId());
        List<UserStorehouseDto> userStorehouseDtos = userStorehouseInnerServiceSMOImpl.queryUserStorehouses(userStorehouseDto);
        if (userStorehouseDtos == null || userStorehouseDtos.size() < 1) {
            userStorehousePo.setStock(purchaseApplyDetailDto.getPurchaseQuantity());
            if (!StringUtil.isEmpty(unitCode) && !StringUtil.isEmpty(miniUnitCode) && !StringUtil.isEmpty(miniUnitStock) && !unitCode.equals(miniUnitCode)) {
                //获取领取数量
                BigDecimal purchaseQuantity2 = new BigDecimal(purchaseApplyDetailDto.getPurchaseQuantity());
                BigDecimal miniUnitStock2 = new BigDecimal(miniUnitStock);
                //计算个人物品最小计量总数
                BigDecimal quantity = purchaseQuantity2.multiply(miniUnitStock2);
                userStorehousePo.setMiniStock(String.valueOf(quantity));
            } else {
                userStorehousePo.setMiniStock(purchaseApplyDetailDto.getPurchaseQuantity());
            }
            userStorehouseInnerServiceSMOImpl.saveUserStorehouses(userStorehousePo);
        } else {
            //获取个人物品领用后的库存
            BigDecimal purchaseQuantity3 = new BigDecimal(purchaseApplyDetailDto.getPurchaseQuantity());
            BigDecimal stock3 = new BigDecimal(userStorehouseDtos.get(0).getStock());
            BigDecimal total = purchaseQuantity3.add(stock3);
            userStorehousePo.setStock(String.valueOf(total));
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
}
