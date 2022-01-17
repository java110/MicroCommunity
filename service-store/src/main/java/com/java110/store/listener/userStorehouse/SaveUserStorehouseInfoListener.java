package com.java110.store.listener.userStorehouse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.userStorehouse.UserStorehousePo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.store.dao.IUserStorehouseServiceDao;
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
 * 保存 个人物品信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUserStorehouseInfoListener")
@Transactional
public class SaveUserStorehouseInfoListener extends AbstractUserStorehouseBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveUserStorehouseInfoListener.class);

    @Autowired
    private IUserStorehouseServiceDao userUserStorehousehouseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_STOREHOUSE;
    }

    /**
     * 保存个人物品信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessUserStorehouse 节点
        if(data.containsKey(UserStorehousePo.class.getSimpleName())){
            Object bObj = data.get(UserStorehousePo.class.getSimpleName());
            JSONArray businessUserStorehouses = null;
            if(bObj instanceof JSONObject){
                businessUserStorehouses = new JSONArray();
                businessUserStorehouses.add(bObj);
            }else {
                businessUserStorehouses = (JSONArray)bObj;
            }
            //JSONObject businessUserStorehouse = data.getJSONObject(UserStorehousePo.class.getSimpleName());
            for (int bUserStorehouseIndex = 0; bUserStorehouseIndex < businessUserStorehouses.size();bUserStorehouseIndex++) {
                JSONObject businessUserStorehouse = businessUserStorehouses.getJSONObject(bUserStorehouseIndex);
                doBusinessUserStorehouse(business, businessUserStorehouse);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("usId", businessUserStorehouse.getString("usId"));
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

        //个人物品信息
        List<Map> businessUserStorehouseInfo = userUserStorehousehouseServiceDaoImpl.getBusinessUserStorehouseInfo(info);
        if( businessUserStorehouseInfo != null && businessUserStorehouseInfo.size() >0) {
            reFreshShareColumn(info, businessUserStorehouseInfo.get(0));
            userUserStorehousehouseServiceDaoImpl.saveUserStorehouseInfoInstance(info);
            if(businessUserStorehouseInfo.size() == 1) {
                dataFlowContext.addParamOut("usId", businessUserStorehouseInfo.get(0).get("us_id"));
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

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
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
        //个人物品信息
        List<Map> userUserStorehousehouseInfo = userUserStorehousehouseServiceDaoImpl.getUserStorehouseInfo(info);
        if(userUserStorehousehouseInfo != null && userUserStorehousehouseInfo.size() > 0){
            reFreshShareColumn(paramIn, userUserStorehousehouseInfo.get(0));
            userUserStorehousehouseServiceDaoImpl.updateUserStorehouseInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessUserStorehouse 节点
     * @param business 总的数据节点
     * @param businessUserStorehouse 个人物品节点
     */
    private void doBusinessUserStorehouse(Business business,JSONObject businessUserStorehouse){

        Assert.jsonObjectHaveKey(businessUserStorehouse,"usId","businessUserStorehouse 节点下没有包含 usId 节点");

        if(businessUserStorehouse.getString("usId").startsWith("-")){
            //刷新缓存
            //flushUserStorehouseId(business.getDatas());

            businessUserStorehouse.put("usId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_usId));

        }

        businessUserStorehouse.put("bId",business.getbId());
        businessUserStorehouse.put("operate", StatusConstant.OPERATE_ADD);
        //保存个人物品信息
        userUserStorehousehouseServiceDaoImpl.saveBusinessUserStorehouseInfo(businessUserStorehouse);

    }
    @Override
    public IUserStorehouseServiceDao getUserStorehouseServiceDaoImpl() {
        return userUserStorehousehouseServiceDaoImpl;
    }

    public void setUserStorehouseServiceDaoImpl(IUserStorehouseServiceDao userUserStorehousehouseServiceDaoImpl) {
        this.userUserStorehousehouseServiceDaoImpl = userUserStorehousehouseServiceDaoImpl;
    }
}
