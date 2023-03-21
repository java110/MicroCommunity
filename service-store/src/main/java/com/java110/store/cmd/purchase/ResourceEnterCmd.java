package com.java110.store.cmd.purchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
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

@Java110CmdDoc(title = "采购入库",
        description = "主要用于 采购人员入库",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/purchase/resourceEnter",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "purchase.resourceEnter"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "applyOrderId", length = 30, remark = "采购申请单订单ID"),
        @Java110ParamDoc(name = "purchaseApplyDetailVo", type = "Array",length = 30, remark = "采购物品信息"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "purchaseQuantity", type = "Int",length = 30, remark = "数量"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "price", type = "String",length = 30, remark = "价格"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "id", type = "String",length = 30, remark = "采购明细ID"),
        @Java110ParamDoc(parentNodeName = "purchaseApplyDetailVo", name = "resId", type = "String",length = 30, remark = "物品ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{'applyOrderId':'123123','purchaseApplyDetailVo':[{'purchaseQuantity':'10','price':'1.3','id':'123123','resId':'343434'}]}",
        resBody="{'code':0,'msg':'成功'}"
)

/**
 * 采购人员采购入库功能
 */
@Java110Cmd(serviceCode = "/purchase/resourceEnter")
public class ResourceEnterCmd extends Cmd{

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单ID为空");
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(reqJson.getString("applyOrderId"));
        purchaseApplyDto.setStatusCd("0");
        List<PurchaseApplyDto> purchaseApplyDtoList = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        if(purchaseApplyDtoList!=null && PurchaseApplyDto.STATE_AUDITED.equals(purchaseApplyDtoList.get(0).getState())){
            throw new IllegalArgumentException("该订单已经处理，请刷新确认订单状态！");
        }
        JSONArray purchaseApplyDetails = reqJson.getJSONArray("purchaseApplyDetailVo");
        for (int detailIndex = 0; detailIndex < purchaseApplyDetails.size(); detailIndex++) {
            JSONObject purchaseApplyDetail = purchaseApplyDetails.getJSONObject(detailIndex);
            Assert.hasKeyAndValue(purchaseApplyDetail, "purchaseQuantity", "采购数量未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "price", "采购单价未填写");
            Assert.hasKeyAndValue(purchaseApplyDetail, "id", "明细ID为空");
        }
    }

    /**
     * 采采购申请-采购审核确认入库
     * @param event              事件对象
     * @param context 数据上文对象
     * @param reqJson            请求报文
     * @throws CmdException
     * @throws ParseException
     */

    @Override
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

        purchaseApplyDetailPos = purchaseApplyPo.getPurchaseApplyDetailPos();
        for (PurchaseApplyDetailPo purchaseApplyDetailPo : purchaseApplyDetailPos) {
            purchaseApplyDetailInnerServiceSMOImpl.updatePurchaseApplyDetail(purchaseApplyDetailPo);
            ResourceStorePo resourceStorePo = new ResourceStorePo();
            resourceStorePo.setResId(purchaseApplyDetailPo.getResId());
            resourceStorePo.setPurchasePrice(purchaseApplyDetailPo.getPrice());
            resourceStorePo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStorePo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_ENTER);
            //查询物品资源信息
            ResourceStoreDto resourceStoreDto = new ResourceStoreDto();
            resourceStoreDto.setResId(purchaseApplyDetailPo.getResId());
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
            //获取采购前物品最小计量总数
            BigDecimal miniStock = new BigDecimal(resourceStoreDtos.get(0).getMiniStock());
            //计算采购的物品最小计量总数
            BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
            BigDecimal purchaseMiniStock = purchaseQuantity.multiply(miniUnitStock);
            //计算采购后物品最小计量总数
            BigDecimal nowMiniStock = miniStock.add(purchaseMiniStock);
            resourceStorePo.setMiniStock(String.valueOf(nowMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);

            // 保存至 物品 times表
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
            resourceStoreTimesPo.setPrice(purchaseApplyDetailPo.getPrice());
            resourceStoreTimesPo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStoreTimesPo.setResCode(resourceStoreDtos.get(0).getResCode());
            resourceStoreTimesPo.setStoreId(resourceStoreDtos.get(0).getStoreId());
            resourceStoreTimesPo.setTimesId(GenerateCodeFactory.getGeneratorId("10"));
            resourceStoreTimesPo.setShId(purchaseApplyDetailPo.getShId());
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);
        }
        //获取订单号
        String applyOrderId = purchaseApplyPo.getApplyOrderId();
        PurchaseApplyPo purchaseApply = new PurchaseApplyPo();
        purchaseApply.setApplyOrderId(applyOrderId);
        purchaseApply.setState(PurchaseApplyDto.STATE_AUDITED);
        purchaseApply.setStatusCd("0");
        purchaseApplyInnerServiceSMOImpl.updatePurchaseApply(purchaseApply);
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "采购申请成功"));
    }
}
