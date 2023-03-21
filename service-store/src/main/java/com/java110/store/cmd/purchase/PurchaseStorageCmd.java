package com.java110.store.cmd.purchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IResourceStoreInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.po.purchase.ResourceStorePo;
import com.java110.po.resourceStoreTimes.ResourceStoreTimesPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 直接入库
 *      * {"resourceStores":[{"resId":"852020061636590016","resName":"橡皮擦","resCode":"003","price":"100.00","stock":"0","description":"ada","quantity":"1"},
 *      * {"resId":"852020061729120031","resName":"文档柜","resCode":"002","price":"33.00","stock":"0","description":"蓝色","quantity":"1"}],
 *      * "description":"123123","endUserName":"1","endUserTel":"17797173942","file":"","resOrderType":"10000","staffId":"","staffName":""}
 */
@Java110Cmd(serviceCode = "/purchase/purchaseStorage")
public class PurchaseStorageCmd extends Cmd {


    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写申请采购的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");
        Assert.hasKeyAndValue(reqJson, "resOrderType", "必填，请填写申请类型");
    }

    /**
     * 直接采购入库
     * @param event              事件对象
     * @param context 数据上文对象
     * @param reqJson            请求报文
     * @throws CmdException
     * @throws ParseException
     */
    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = context.getReqHeaders().get("store-id");
        String userId = context.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "未包含用户");


        String userName = userDtos.get(0).getName();
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
            BigDecimal purchaseQuantity = new BigDecimal(purchaseApplyDetailPo.getPurchaseQuantity());
            //获取原有最小计量总数
            BigDecimal miniStock = new BigDecimal(resourceStore.getString("miniStock"));
            //获取最小单位数量
            BigDecimal newMiniStock = new BigDecimal(0);
            if (StringUtil.isEmpty(resourceStore.getString("miniUnitStock"))) {
                throw new IllegalArgumentException("最小计量单位数量不能为空！");
            }
            BigDecimal miniUnitStock = new BigDecimal(resourceStore.getString("miniUnitStock"));
            //计算最小计量总数
            if (StringUtil.isEmpty(resourceStore.getString("miniStock"))) {
                newMiniStock = purchaseQuantity.multiply(miniUnitStock);
            } else {
                newMiniStock = (purchaseQuantity.multiply(miniUnitStock)).add(miniStock);
            }
            resourceStorePo.setMiniStock(String.valueOf(newMiniStock));
            resourceStoreInnerServiceSMOImpl.updateResourceStore(resourceStorePo);

            // 保存至 物品 times表
            ResourceStoreTimesPo resourceStoreTimesPo = new ResourceStoreTimesPo();
            resourceStoreTimesPo.setApplyOrderId(purchaseApplyPo.getApplyOrderId());
            resourceStoreTimesPo.setPrice(purchaseApplyDetailPo.getPrice());
            resourceStoreTimesPo.setStock(purchaseApplyDetailPo.getPurchaseQuantity());
            resourceStoreTimesPo.setResCode(resourceStore.getString("resCode"));
            resourceStoreTimesPo.setStoreId(storeId);
            resourceStoreTimesPo.setShId(purchaseApplyDetailPo.getShId());
            resourceStoreTimesV1InnerServiceSMOImpl.saveOrUpdateResourceStoreTimes(resourceStoreTimesPo);

        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);
        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);
        if (saveFlag < 1) {
            context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "采购申请失败"));
            return;
        }
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        if (!purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT) && !purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_URGENT)) {
            purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
            purchaseApplyUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "采购申请成功"));

    }
}
