package com.java110.store.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.bmo.purchase.IPurchaseApplyBMO;
import com.java110.store.bmo.purchase.IResourceEnterBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/purchase")
public class PurchaseApi {

    @Autowired
    private IPurchaseApplyBMO purchaseApplyBMOImpl;

    @Autowired
    private IResourceEnterBMO resourceEnterBMOImpl;

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
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_DEALING);

        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");

        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();

        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
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
}
