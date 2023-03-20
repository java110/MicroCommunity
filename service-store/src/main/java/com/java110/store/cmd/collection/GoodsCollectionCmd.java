package com.java110.store.cmd.collection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.resourceStoreTimes.ResourceStoreTimesDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IResourceStoreTimesV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品领用
 */
@Java110Cmd(serviceCode = "/collection/goodsCollection")
public class GoodsCollectionCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;


    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;

    @Autowired
    private IResourceStoreTimesV1InnerServiceSMO resourceStoreTimesV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "resourceStores", "必填，请填写物品领用的物资");
        Assert.hasKeyAndValue(reqJson, "description", "必填，请填写采购申请说明");

        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        String storeId = context.getReqHeaders().get("store-id");

        if (resourceStores == null || resourceStores.size() < 1) {
            throw new CmdException("未包含领用物品");
        }
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            Assert.hasKeyAndValue(resourceStore, "timesId", "必填，未选择价格");

            ResourceStoreTimesDto resourceStoreTimesDto = new ResourceStoreTimesDto();
            resourceStoreTimesDto.setTimesId(resourceStore.getString("timesId"));
            resourceStoreTimesDto.setStoreId(storeId);
            List<ResourceStoreTimesDto> resourceStoreTimesDtos = resourceStoreTimesV1InnerServiceSMOImpl.queryResourceStoreTimess(resourceStoreTimesDto);

            Assert.listOnlyOne(resourceStoreTimesDtos, "价格不存在");

            resourceStore.put("resourceStoreTimesDtos",resourceStoreTimesDtos);
        }


    }

    /**
     * 物品领用申请-发起
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
        purchaseApplyPo.setResOrderType(PurchaseApplyDto.RES_ORDER_TYPE_OUT);
        purchaseApplyPo.setState(PurchaseApplyDto.STATE_WAIT_DEAL);
        purchaseApplyPo.setCreateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        purchaseApplyPo.setCreateUserId(userId);
        purchaseApplyPo.setCreateUserName(userName);
        purchaseApplyPo.setWarehousingWay(PurchaseApplyDto.WAREHOUSING_TYPE_APPLY);
        purchaseApplyPo.setCommunityId(reqJson.getString("communityId"));
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        List<PurchaseApplyDetailPo> purchaseApplyDetailPos = new ArrayList<>();
        List<ResourceStoreTimesDto> resourceStoreTimesDtos = null;
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            resourceStoreTimesDtos = (List<ResourceStoreTimesDto>) resourceStore.get("resourceStoreTimesDtos");
            resourceStore.put("originalStock", resourceStore.get("stock"));
            PurchaseApplyDetailPo purchaseApplyDetailPo = BeanConvertUtil.covertBean(resourceStore, PurchaseApplyDetailPo.class);
            purchaseApplyDetailPo.setId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_applyOrderId));
            purchaseApplyDetailPo.setPrice(resourceStoreTimesDtos.get(0).getPrice());
            purchaseApplyDetailPo.setTimesId(resourceStoreTimesDtos.get(0).getTimesId());
            purchaseApplyDetailPo.setOriginalStock(resourceStoreTimesDtos.get(0).getStock());
            //获取批次采购参考价格
            String consultPrice = null;
            JSONArray timeList = resourceStore.getJSONArray("times");
            if(resourceStore.containsKey("timesId") && !StringUtil.isEmpty(resourceStore.getString("timesId"))){
                for (int timesIndex = 0; timesIndex < timeList.size(); timesIndex++) {
                    JSONObject times = timeList.getJSONObject(timesIndex);
                    if(times.getString("timesId").toString().equals(resourceStore.getString("timesId").toString())){
                        consultPrice=times.getString("price");
                    }
                }
            }
            purchaseApplyDetailPo.setConsultPrice(consultPrice);
            purchaseApplyDetailPos.add(purchaseApplyDetailPo);
        }
        purchaseApplyPo.setPurchaseApplyDetailPos(purchaseApplyDetailPos);

        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);

        if (saveFlag < 1) {
            throw new CmdException("物品领用申请失败");
        }
        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        purchaseApplyDto.setNextStaffId(reqJson.getString("staffId"));
        if (!PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT.equals(purchaseApplyPo.getWarehousingWay())) {
            goodCollectionUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(ResultVo.CODE_OK, "物品领用成功"));
    }
}
