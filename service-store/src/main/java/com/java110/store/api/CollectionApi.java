package com.java110.store.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.bmo.purchase.IGetCollectionAuditOrderBMO;
import com.java110.store.bmo.purchase.IGoodsCollectionBMO;
import com.java110.store.bmo.purchase.IResourceOutBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 物品领用 接口类
     *
     * @param reqJson
     * @param userId
     * @param userName
     * @param storeId
     * @return {"resourceStores":[{"resId":"852020070239060001","resName":"水性笔","resCode":"002","price":"2.00","stock":"2",
     * "description":"黑色","quantity":"1"}],"description":"123123","endUserName":"1","endUserTel":"17797173942","file":"",
     * "resOrderType":"20000","staffId":"","staffName":""}
     */
    @RequestMapping(value = "/goodsCollection", method = RequestMethod.POST)
    public ResponseEntity<String> goodsCollection(@RequestBody JSONObject reqJson,
                                                  @RequestHeader(value = "user-id") String userId,
                                                  @RequestHeader(value = "user-name") String userName,
                                                  @RequestHeader(value = "store-id") String storeId) {

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

        return goodsCollectionBMOImpl.collection(purchaseApplyPo);
    }

    /**
     * 查询审核单
     * @param page
     * @param row
     * @param userId
     * @param storeId
     * @return
     */
    @RequestMapping(value = "/getCollectionAuditOrder", method = RequestMethod.GET)
    public ResponseEntity<String> getCollectionAuditOrder(@RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row,
                                                          @RequestHeader(value = "user-id") String userId,
                                                          @RequestHeader(value = "store-id") String storeId){

        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(userId);
        auditUser.setPage(page);
        auditUser.setRow(row);
        auditUser.setStoreId(storeId);
        return getCollectionAuditOrderBMOImpl.auditOrder(auditUser);
    }

    /**
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
}
