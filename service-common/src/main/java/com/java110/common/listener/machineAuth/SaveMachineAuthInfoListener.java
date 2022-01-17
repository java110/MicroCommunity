package com.java110.common.listener.machineAuth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.machineAuth.MachineAuthPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.common.dao.IMachineAuthServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 设备权限信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveMachineAuthInfoListener")
@Transactional
public class SaveMachineAuthInfoListener extends AbstractMachineAuthBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveMachineAuthInfoListener.class);

    @Autowired
    private IMachineAuthServiceDao machineAuthServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE_AUTH;
    }

    /**
     * 保存设备权限信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessMachineAuth 节点
        if(data.containsKey(MachineAuthPo.class.getSimpleName())){
            Object bObj = data.get(MachineAuthPo.class.getSimpleName());
            JSONArray businessMachineAuths = null;
            if(bObj instanceof JSONObject){
                businessMachineAuths = new JSONArray();
                businessMachineAuths.add(bObj);
            }else {
                businessMachineAuths = (JSONArray)bObj;
            }
            //JSONObject businessMachineAuth = data.getJSONObject(MachineAuthPo.class.getSimpleName());
            for (int bMachineAuthIndex = 0; bMachineAuthIndex < businessMachineAuths.size();bMachineAuthIndex++) {
                JSONObject businessMachineAuth = businessMachineAuths.getJSONObject(bMachineAuthIndex);
                doBusinessMachineAuth(business, businessMachineAuth);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("authId", businessMachineAuth.getString("authId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //设备权限信息
        List<Map> businessMachineAuthInfo = machineAuthServiceDaoImpl.getBusinessMachineAuthInfo(info);
        if( businessMachineAuthInfo != null && businessMachineAuthInfo.size() >0) {
            reFreshShareColumn(info, businessMachineAuthInfo.get(0));
            machineAuthServiceDaoImpl.saveMachineAuthInfoInstance(info);
            if(businessMachineAuthInfo.size() == 1) {
                dataFlowContext.addParamOut("authId", businessMachineAuthInfo.get(0).get("auth_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("communityId")) {
            return;
        }

        if (!businessInfo.containsKey("community_id")) {
            return;
        }

        info.put("communityId", businessInfo.get("community_id"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //设备权限信息
        List<Map> machineAuthInfo = machineAuthServiceDaoImpl.getMachineAuthInfo(info);
        if(machineAuthInfo != null && machineAuthInfo.size() > 0){
            reFreshShareColumn(paramIn, machineAuthInfo.get(0));
            machineAuthServiceDaoImpl.updateMachineAuthInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessMachineAuth 节点
     * @param business 总的数据节点
     * @param businessMachineAuth 设备权限节点
     */
    private void doBusinessMachineAuth(Business business,JSONObject businessMachineAuth){

        Assert.jsonObjectHaveKey(businessMachineAuth,"authId","businessMachineAuth 节点下没有包含 authId 节点");

        if(businessMachineAuth.getString("authId").startsWith("-")){
            //刷新缓存
            //flushMachineAuthId(business.getDatas());

            businessMachineAuth.put("authId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_authId));

        }

        businessMachineAuth.put("bId",business.getbId());
        businessMachineAuth.put("operate", StatusConstant.OPERATE_ADD);
        //保存设备权限信息
        machineAuthServiceDaoImpl.saveBusinessMachineAuthInfo(businessMachineAuth);

    }
    @Override
    public IMachineAuthServiceDao getMachineAuthServiceDaoImpl() {
        return machineAuthServiceDaoImpl;
    }

    public void setMachineAuthServiceDaoImpl(IMachineAuthServiceDao machineAuthServiceDaoImpl) {
        this.machineAuthServiceDaoImpl = machineAuthServiceDaoImpl;
    }
}
