package com.java110.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.account.AccountDto;
import com.java110.dto.shop.ShopDto;
import com.java110.dto.shopCommunity.ShopCommunityDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.dto.storeShop.StoreShopDto;
import com.java110.dto.storeShopCommunity.StoreShopCommunityDto;
import com.java110.intf.acct.IAccountBondObjInnerServiceSMO;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.mall.IShopCommunityInnerServiceSMO;
import com.java110.intf.mall.IShopInnerServiceSMO;
import com.java110.intf.store.*;
import com.java110.intf.user.IOrgV1InnerServiceSMO;
import com.java110.intf.user.IPrivilegeUserV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.account.AccountPo;
import com.java110.po.store.StorePo;
import com.java110.po.storeShop.StoreShopPo;
import com.java110.po.storeShopCommunity.StoreShopCommunityPo;
import com.java110.po.user.UserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Java110CmdDoc(title = "物业公司删除商户和商铺功能",
        description = "此接口目前主要计划用于停车卷相关使用",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/store.propertyDeleteStoreAndShop",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "store.propertyDeleteStoreAndShop",
        seq = 6
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "shopId", length = 30, remark = "商铺编号"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody = "{\"shopId\":\"123123123\",\"communityId\":\"2022081539020475\"}",
        resBody = "{'code':0,'msg':'成功'}"
)

/**
 * 物业公司删除 商户和商铺功能
 * <p>
 * 此接口目前主要计划用于停车卷相关使用
 */
@Java110Cmd(serviceCode = "store.propertyDeleteStoreAndShop")
public class PropertyDeleteStoreAndShopCmd extends Cmd {
    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreAttrV1InnerServiceSMO storeAttrV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private IStoreShopV1InnerServiceSMO storeShopV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IAccountBondObjInnerServiceSMO accountBondObjInnerServiceSMOImpl;

    @Autowired
    private IStoreShopCommunityV1InnerServiceSMO storeShopCommunityV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;


    @Autowired(required = false)
    private IShopInnerServiceSMO shopInnerServiceSMOImpl;

    @Autowired(required = false)
    private IShopCommunityInnerServiceSMO shopCommunityInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "shopId", "未包含商家商铺");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        //1.0 删除 商铺和小区的关系
        StoreShopCommunityDto shopCommunityDto = new StoreShopCommunityDto();
        shopCommunityDto.setCommunityId(reqJson.getString("communityId"));
        shopCommunityDto.setShopId(reqJson.getString("shopId"));

        List<ShopCommunityDto> storeShopCommunityDtos = null;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            storeShopCommunityDtos = shopCommunityInnerServiceSMOImpl.queryShopCommunitys(shopCommunityDto);
        } else {
            storeShopCommunityDtos =  storeShopCommunityV1InnerServiceSMOImpl.queryStoreShopCommunitys(shopCommunityDto);
        }

        if (storeShopCommunityDtos == null || storeShopCommunityDtos.size() < 1) {
            return;
        }
        StoreShopCommunityPo storeShopCommunityPo = new StoreShopCommunityPo();
        storeShopCommunityPo.setScId(storeShopCommunityDtos.get(0).getScId());
        int flag = 0;

        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            flag = shopCommunityInnerServiceSMOImpl.deleteShopCommunity(storeShopCommunityPo);
        } else {
            flag = storeShopCommunityV1InnerServiceSMOImpl.deleteStoreShopCommunity(storeShopCommunityPo);
        }
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


        List<ShopDto> storeShopDtos = null;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            ShopDto storeShopDto = new ShopDto();
            storeShopDto.setShopId(reqJson.getString("shopId"));
            storeShopDtos = shopInnerServiceSMOImpl.queryShops(storeShopDto);
        } else {
            StoreShopDto storeShopDto = new StoreShopDto();
            storeShopDto.setShopId(reqJson.getString("shopId"));
            storeShopDtos =  storeShopV1InnerServiceSMOImpl.queryStoreShops(storeShopDto);
        }

        if (storeShopDtos == null || storeShopDtos.size() < 1) {
            return;
        }
        StoreShopPo storeShopPo = new StoreShopPo();
        storeShopPo.setShopId(storeShopDtos.get(0).getShopId());
        flag = storeShopV1InnerServiceSMOImpl.deleteStoreShop(storeShopPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


        StorePo storePo = BeanConvertUtil.covertBean(reqJson, StorePo.class);
        storePo.setStoreId(storeShopDtos.get(0).getStoreId());
        flag = storeV1InnerServiceSMOImpl.deleteStore(storePo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setStoreId(storeShopDtos.get(0).getStoreId());

        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);

        if (storeUserDtos == null || storeUserDtos.size() < 1) {
            return;
        }

        UserPo userPo = null;
        for(StoreUserDto tmpStoreUserDto : storeUserDtos){
            userPo = new UserPo();
            userPo.setUserId(tmpStoreUserDto.getUserId());
            userV1InnerServiceSMOImpl.deleteUser(userPo);
        }



    }

    private void saveShopCommunity(JSONObject reqJson) {

        StoreShopCommunityPo storeShopCommunityPo = new StoreShopCommunityPo();
        storeShopCommunityPo.setAddress("无");
        storeShopCommunityPo.setCityCode(reqJson.getString("areaCode"));
        storeShopCommunityPo.setCodeName("无");
        storeShopCommunityPo.setCommunityId(reqJson.getString("communityId"));
        storeShopCommunityPo.setCommunityName(reqJson.getString("communityName"));
        storeShopCommunityPo.setEndTime("2050-01-01");
        storeShopCommunityPo.setMessage("物业添加");
        storeShopCommunityPo.setScId(GenerateCodeFactory.getGeneratorId("10"));
        storeShopCommunityPo.setShopId(reqJson.getString("shopId"));
        storeShopCommunityPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        storeShopCommunityPo.setState(StoreShopCommunityDto.STATE_SUCCESS);
        int flag = 0;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            flag = shopCommunityInnerServiceSMOImpl.saveShopCommunity(storeShopCommunityPo);
        } else {
            flag = storeShopCommunityV1InnerServiceSMOImpl.saveStoreShopCommunity(storeShopCommunityPo);
        }
        if (flag < 1) {
            throw new IllegalArgumentException("小区关联商铺失败");
        }
    }


    /**
     * 开户
     *
     * @param storePo
     * @param reqJson
     */
    private void addAccountDto(StorePo storePo, JSONObject reqJson) {

        AccountPo accountPo = new AccountPo();
        accountPo.setAmount("0");
        accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));
        accountPo.setObjId(storePo.getStoreId());
        accountPo.setObjType(AccountDto.OBJ_TYPE_STORE);
        accountPo.setAcctType(AccountDto.ACCT_TYPE_CASH);
        accountPo.setAcctName(storePo.getName());
        accountPo.setPartId(reqJson.getString("shopId"));
        accountPo.setLink(reqJson.getString("link"));
        accountInnerServiceSMOImpl.saveAccount(accountPo);
    }


    private void saveShop(StorePo storePo, JSONObject reqJson) {

        StoreShopPo shopPo = new StoreShopPo();
        shopPo.setShopName(reqJson.getString("shopName"));
        shopPo.setShopDesc("无");
        shopPo.setReturnPerson("无");
        shopPo.setReturnLink(reqJson.getString("link"));
        shopPo.setStoreId(storePo.getStoreId());
        shopPo.setSendAddress("无");
        shopPo.setReturnAddress("无");
        shopPo.setShopType("1012021070202890002");
        shopPo.setOpenType(ShopDto.OPEN_TYPE_SHOP);
        //shopPo.setShopRange("无");
        shopPo.setAreaCode(reqJson.getString("areaCode"));
        shopPo.setShopId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_shopId));
        shopPo.setShopLogo("");
        shopPo.setMapX(storePo.getMapX());
        shopPo.setMapY(storePo.getMapY());

        shopPo.setState(ShopDto.STATE_Y);
        //shopPo.setState(ShopDto.STATE_B);
        int flag = 0;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            flag = shopInnerServiceSMOImpl.saveShop(shopPo);
        } else {
            flag = storeShopV1InnerServiceSMOImpl.saveStoreShop(shopPo);
        }
        if (flag < 1) {
            throw new CmdException("保存商铺失败");
        }
        reqJson.put("shopId", shopPo.getShopId());
    }
}
