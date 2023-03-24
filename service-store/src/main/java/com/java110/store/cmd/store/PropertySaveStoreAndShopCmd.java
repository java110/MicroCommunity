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
import com.java110.dto.community.CommunityDto;
import com.java110.dto.shop.ShopDto;
import com.java110.dto.store.StoreDto;
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
import com.java110.po.org.OrgPo;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.po.privilegeUser.PrivilegeUserPo;
import com.java110.po.store.StorePo;
import com.java110.po.store.StoreUserPo;
import com.java110.po.storeShop.StoreShopPo;
import com.java110.po.storeShopCommunity.StoreShopCommunityPo;
import com.java110.po.user.UserPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.StoreUserRelConstant;
import com.java110.utils.constant.UserLevelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Java110CmdDoc(title = "物业公司添加商户和商铺功能",
        description = "此接口目前主要计划用于停车卷相关使用",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/store.propertySaveStoreAndShop",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "store.propertySaveStoreAndShop",
        seq = 5
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "shopName", length = 30, remark = "商铺名称 商家名称和商铺名称一致，如果需求需要不一致"),
        @Java110ParamDoc(name = "link", length = 30, remark = "管理员手机号"),
        @Java110ParamDoc(name = "password", length = 30, remark = "登录密码"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"shopName\":\"测试商家\",\"link\":\"18909714444\",\"password\":\"123456\",\"communityId\":\"2022081539020475\"}",
        resBody="{'code':0,'msg':'成功'}"
)

/**
 * 物业公司添加 商户和商铺功能
 * <p>
 * 此接口目前主要计划用于停车卷相关使用
 */
@Java110Cmd(serviceCode = "store.propertySaveStoreAndShop")
public class PropertySaveStoreAndShopCmd extends Cmd {
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
        Assert.hasKeyAndValue(reqJson, "shopName", "未包含商铺名称");
        Assert.hasKeyAndValue(reqJson, "link", "未包含手机号");
        Assert.hasKeyAndValue(reqJson, "password", "未包含密码");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {


        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(reqJson.getString("communityId"));
        List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

        Assert.listOnlyOne(communityDtos, "小区不存在");
        reqJson.put("communityName", communityDtos.get(0).getName());
        reqJson.put("areaCode", communityDtos.get(0).getCityCode());

        StoreDto storeDto = new StoreDto();
        storeDto.setTel(reqJson.getString("link"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        if (storeDtos != null && storeDtos.size() > 0) {
            throw new IllegalArgumentException(reqJson.getString("link") + "，手机号已经被占用 请更换或者联系管理员");
        }

        StorePo storePo = BeanConvertUtil.covertBean(reqJson, StorePo.class);
        storePo.setStoreId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        storePo.setStoreTypeCd(StoreDto.STORE_TYPE_MALL);
        storePo.setState(StoreDto.STATE_NORMAL);
        storePo.setName(reqJson.getString("shopName"));
        storePo.setUserId("-1");
        storePo.setAddress("无");
        storePo.setTel(reqJson.getString("link"));
        storePo.setNearByLandmarks("无");
        if (!reqJson.containsKey("mapY")) {
            storePo.setMapY("1");
        }
        if (!reqJson.containsKey("mapX")) {
            storePo.setMapX("1");
        }
        int flag = storeV1InnerServiceSMOImpl.saveStore(storePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //添加用户
        UserPo userPo = new UserPo();
        userPo.setTel(reqJson.getString("link"));
        userPo.setName(reqJson.getString("shopName"));
        userPo.setAddress("无");
        userPo.setUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userId));
        userPo.setScore("0");
        userPo.setAge("1");
        userPo.setEmail("无");
        userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ADMIN);
        userPo.setSex("1");
        userPo.setPassword(AuthenticationFactory.passwdMd5(reqJson.getString("password")));
        userPo.setbId("-1");
        flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存 商户和用户的关系
        StoreUserPo storeUserPo = new StoreUserPo();
        storeUserPo.setStoreUserId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        storeUserPo.setRelCd(StoreUserRelConstant.REL_ADMIN);
        storeUserPo.setStoreId(storePo.getStoreId());
        storeUserPo.setUserId(userPo.getUserId());
        flag = storeUserV1InnerServiceSMOImpl.saveStoreUser(storeUserPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存公司级组织
        OrgPo orgPo = new OrgPo();
        orgPo.setOrgName(storePo.getName());
        orgPo.setOrgLevel("1");
        orgPo.setOrgId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId));
        orgPo.setAllowOperation("F");
        orgPo.setBelongCommunityId("9999");
        orgPo.setParentOrgId(orgPo.getOrgId());
        orgPo.setStoreId(storePo.getStoreId());

        flag = orgV1InnerServiceSMOImpl.saveOrg(orgPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存分公司
        OrgPo orgHeadPo = new OrgPo();
        orgHeadPo.setOrgName("公司总部");
        orgHeadPo.setOrgLevel("2");
        orgHeadPo.setOrgId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId));
        orgHeadPo.setAllowOperation("F");
        orgHeadPo.setBelongCommunityId("9999");
        orgHeadPo.setParentOrgId(orgPo.getOrgId());
        orgHeadPo.setStoreId(storePo.getStoreId());
        flag = orgV1InnerServiceSMOImpl.saveOrg(orgHeadPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存部门
        OrgPo orgHeadPartPo = new OrgPo();
        orgHeadPartPo.setOrgName("总部办公室");
        orgHeadPartPo.setOrgLevel("3");
        orgHeadPartPo.setOrgId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId));
        orgHeadPartPo.setAllowOperation("F");
        orgHeadPartPo.setBelongCommunityId("9999");
        orgHeadPartPo.setParentOrgId(orgHeadPo.getOrgId());
        orgHeadPartPo.setStoreId(storePo.getStoreId());
        flag = orgV1InnerServiceSMOImpl.saveOrg(orgHeadPartPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }


        //添加组织 员工关系
        OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
        orgStaffRelPo.setOrgId(orgHeadPartPo.getOrgId());
        orgStaffRelPo.setStaffId(userPo.getUserId());
        orgStaffRelPo.setRelId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        orgStaffRelPo.setRelCd(StoreUserRelConstant.REL_ADMIN);
        orgStaffRelPo.setStoreId(storePo.getStoreId());
        flag = orgStaffRelV1InnerServiceSMOImpl.saveOrgStaffRel(orgStaffRelPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }


        String defaultPrivilege = MappingCache.getValue(MappingConstant.DOMAIN_DEFAULT_PRIVILEGE_ADMIN, StoreDto.STORE_TYPE_MALL);

        Assert.hasLength(defaultPrivilege, "未配置商城默认权限");
        PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
        privilegeUserPo.setPrivilegeFlag("1");
        privilegeUserPo.setStoreId(storePo.getStoreId());
        privilegeUserPo.setUserId(userPo.getUserId());
        privilegeUserPo.setpId(defaultPrivilege);
        privilegeUserPo.setPuId(GenerateCodeFactory.getGeneratorId("10"));

        flag = privilegeUserV1InnerServiceSMOImpl.savePrivilegeUser(privilegeUserPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存商铺
        saveShop(storePo, reqJson);

        // 关联 小区和商铺
        saveShopCommunity(reqJson);

        //开户
        addAccountDto(storePo, reqJson);
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
