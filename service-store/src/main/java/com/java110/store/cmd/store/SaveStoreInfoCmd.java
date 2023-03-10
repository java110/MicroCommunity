package com.java110.store.cmd.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.account.AccountDto;
import com.java110.dto.store.StoreDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.intf.acct.IAccountInnerServiceSMO;
import com.java110.intf.common.IWorkflowV1InnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.java110.intf.store.IStoreAttrV1InnerServiceSMO;
import com.java110.intf.store.IStoreUserV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.po.account.AccountPo;
import com.java110.po.org.OrgPo;
import com.java110.po.org.OrgStaffRelPo;
import com.java110.po.privilegeUser.PrivilegeUserPo;
import com.java110.po.store.StoreAttrPo;
import com.java110.po.store.StorePo;
import com.java110.po.store.StoreUserPo;
import com.java110.po.workflow.WorkflowPo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.StoreTypeConstant;
import com.java110.utils.constant.StoreUserRelConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@Java110Cmd(serviceCode = "save.store.info")
public class SaveStoreInfoCmd extends Cmd {

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
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupV1InnerServiceSMO menuGroupV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupCommunityV1InnerServiceSMO menuGroupCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, StorePo.class.getSimpleName(), "请求参数中未包含businessStore 节点，请确认");
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        addStore(reqJson);
        //添加员工
        addStaff(reqJson);
        //添加公司级组织
        addOrg(reqJson);
        //公司总部
        addStaffOrg(reqJson);
        addPurchase(reqJson);
        //businesses.add(storeBMOImpl.addCollection(paramObj)); //物品领用 各个小区设置各自的流程  废弃次操作
        contractApply(reqJson);
        contractChange(reqJson);
        //物品调拨流程
        allocationStorehouse(reqJson);

        //新建账户 目前只有商家创建账户
        JSONObject businessStoreObj = reqJson.getJSONObject(StorePo.class.getSimpleName());
        if (StoreTypeConstant.STORE_TYPE_MALL.equals(businessStoreObj.getString("storeTypeCd"))) {
            //物品调拨流程
            addAccount(reqJson, AccountDto.ACCT_TYPE_CASH);
            addAccount(reqJson, AccountDto.ACCT_TYPE_INTEGRAL);
//            businesses.add(storeBMOImpl.addAccount(paramObj,AccountDto.ACCT_TYPE_GOLD));
        }
        //赋权
        privilegeUserDefault(reqJson);
    }

    public void addAccount(JSONObject paramInJson, String acctType) {
        JSONObject businessStoreObj = paramInJson.getJSONObject(StorePo.class.getSimpleName());

        AccountPo accountPo = new AccountPo();
        accountPo.setAcctId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_acctId));

        accountPo.setAcctName(businessStoreObj.getString("name"));
        accountPo.setAcctType(acctType);
        accountPo.setAmount("0");
        accountPo.setObjId(paramInJson.getString("storeId"));
        accountPo.setObjType(AccountDto.OBJ_TYPE_STORE);
        accountPo.setLink(paramInJson.getString("link"));
        int flag = accountInnerServiceSMOImpl.saveAccount(accountPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }


    /**
     * 调拨审核
     *
     * @param paramInJson
     * @return
     */
    public void allocationStorehouse(JSONObject paramInJson) {

        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setCommunityId("9999"); //所有小区
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workflowPo.setFlowName("物品调拨");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(paramInJson.getString("storeId"));
        int flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    /**
     * 合同变更
     *
     * @param paramInJson
     * @return
     */
    public void contractChange(JSONObject paramInJson) {


        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setCommunityId("9999"); //所有小区
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workflowPo.setFlowName("合同变更");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_CONTRACT_CHANGE);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(paramInJson.getString("storeId"));
        int flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    /**
     * 合同申请续签
     *
     * @param paramInJson
     * @return
     */
    public void contractApply(JSONObject paramInJson) {

        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setCommunityId("9999"); //所有小区
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workflowPo.setFlowName("合同申请续签");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_CONTRACT_APPLY);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(paramInJson.getString("storeId"));
        int flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    public void addPurchase(JSONObject paramInJson) {

        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setCommunityId("9999"); //所有小区
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workflowPo.setFlowName("采购流程");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_PURCHASE);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(paramInJson.getString("storeId"));
        int flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    public void addStaffOrg(JSONObject paramInJson) {


        JSONObject businessOrgStaffRel = new JSONObject();
        businessOrgStaffRel.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        businessOrgStaffRel.put("storeId", paramInJson.getString("storeId"));
        businessOrgStaffRel.put("staffId", paramInJson.getJSONObject(StorePo.class.getSimpleName()).getString("userId"));
        businessOrgStaffRel.put("orgId", paramInJson.getString("levelOneOrgId"));
        businessOrgStaffRel.put("relCd", StoreUserRelConstant.REL_ADMIN);
        //计算 应收金额
        OrgStaffRelPo orgStaffRelPo = BeanConvertUtil.covertBean(businessOrgStaffRel, OrgStaffRelPo.class);

        int flag = orgStaffRelV1InnerServiceSMOImpl.saveOrgStaffRel(orgStaffRelPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    /**
     * 添加一级组织信息
     *
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    public void addOrg(JSONObject paramInJson) {

        String orgId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId);
        paramInJson.put("levelOneOrgId", orgId);

        JSONObject businessOrg = new JSONObject();
        businessOrg.put("orgName", paramInJson.getJSONObject(StorePo.class.getSimpleName()).getString("name"));
        businessOrg.put("orgLevel", "1");
        businessOrg.put("parentOrgId", orgId);
        businessOrg.put("belongCommunityId", "9999");
        businessOrg.put("orgId", orgId);
        businessOrg.put("allowOperation", "F");
        businessOrg.put("storeId", paramInJson.getString("storeId"));

        //计算 应收金额
        OrgPo orgPo = BeanConvertUtil.covertBean(businessOrg, OrgPo.class);

        int flag = orgV1InnerServiceSMOImpl.saveOrg(orgPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    /**
     * 添加商户
     *
     * @param paramInJson
     * @return
     */
    public void addStore(JSONObject paramInJson) {
        int flag = 0;
        String storeId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_storeId);
        paramInJson.put("storeId", storeId);
        if (paramInJson.containsKey(StorePo.class.getSimpleName())) {
            JSONObject businessStoreObj = paramInJson.getJSONObject(StorePo.class.getSimpleName());
            businessStoreObj.put("storeId", storeId);
            if (!businessStoreObj.containsKey("password")) {
                String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
                Assert.hasLength(staffDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
                businessStoreObj.put("password", staffDefaultPassword);
            }

            if (!businessStoreObj.containsKey("mapX")) {
                businessStoreObj.put("mapX", "");
            }

            if (!businessStoreObj.containsKey("mapY")) {
                businessStoreObj.put("mapY", "");
            }
            StorePo storePo = BeanConvertUtil.covertBean(businessStoreObj, StorePo.class);
            storePo.setState(StoreDto.STATE_NORMAL);
            flag = storeV1InnerServiceSMOImpl.saveStore(storePo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }

        if (paramInJson.containsKey(StoreAttrPo.class.getSimpleName())) {
            JSONArray attrs = paramInJson.getJSONArray(StoreAttrPo.class.getSimpleName());

            for (int businessStoreAttrIndex = 0; businessStoreAttrIndex < attrs.size(); businessStoreAttrIndex++) {
                JSONObject attr = attrs.getJSONObject(businessStoreAttrIndex);
                attr.put("storeId", storeId);
                attr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                StoreAttrPo storeAttrPo = BeanConvertUtil.covertBean(attr, StoreAttrPo.class);
                flag = storeAttrV1InnerServiceSMOImpl.saveStoreAttr(storeAttrPo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
        }
    }

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
    public void addStaff(JSONObject paramInJson) {

        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", paramInJson.getString("storeId"));
        businessStoreUser.put("storeUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_storeUserId));
        businessStoreUser.put("userId", paramInJson.getJSONObject(StorePo.class.getSimpleName()).getString("userId"));
        businessStoreUser.put("relCd", StoreUserRelConstant.REL_ADMIN);
        StoreUserPo storeUserPo = BeanConvertUtil.covertBean(businessStoreUser, StoreUserPo.class);
        int flag = storeUserV1InnerServiceSMOImpl.saveStoreUser(storeUserPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

    }

    /**
     * 用户赋权
     *
     * @return
     */
    private void privilegeUserDefault(JSONObject paramObj) {

        String defaultPrivilege = MappingCache.getValue(MappingConstant.DOMAIN_DEFAULT_PRIVILEGE_ADMIN, paramObj.getJSONObject(StorePo.class.getSimpleName()).getString("storeTypeCd"));

        Assert.hasLength(defaultPrivilege, "未配置默认权限");
        PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
        privilegeUserPo.setPrivilegeFlag("1");
        privilegeUserPo.setStoreId(paramObj.getString("storeId"));
        privilegeUserPo.setUserId(paramObj.getJSONObject(StorePo.class.getSimpleName()).getString("userId"));
        privilegeUserPo.setpId(defaultPrivilege);
        privilegeUserPo.setPuId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));

        int flag = privilegeUserV1InnerServiceSMOImpl.savePrivilegeUser(privilegeUserPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }
}
