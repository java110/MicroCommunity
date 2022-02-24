package com.java110.store.listener.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.store.dao.IDemoServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 demo信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveDemoInfoListener")
@Transactional
public class SaveDemoInfoListener extends AbstractDemoBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveDemoInfoListener.class);

    @Autowired
    private IDemoServiceDao demoServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_DEMO_INFO;
    }

    /**
     * 保存demo信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessDemo 节点
        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_SAVE_DEMO_INFO)){
            Object bObj = data.get(BusinessTypeConstant.BUSINESS_TYPE_SAVE_DEMO_INFO);
            JSONArray businessDemos = null;
            if(bObj instanceof JSONObject){
                businessDemos = new JSONArray();
                businessDemos.add(bObj);
            }else {
                businessDemos = (JSONArray)bObj;
            }
            //JSONObject businessDemo = data.getJSONObject("businessDemo");
            for (int bDemoIndex = 0; bDemoIndex < businessDemos.size();bDemoIndex++) {
                JSONObject businessDemo = businessDemos.getJSONObject(bDemoIndex);
                doBusinessDemo(business, businessDemo);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("demoId", businessDemo.getString("demoId"));
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

        //demo信息
        List<Map> businessDemoInfo = demoServiceDaoImpl.getBusinessDemoInfo(info);
        if( businessDemoInfo != null && businessDemoInfo.size() >0) {
            reFreshShareColumn(info, businessDemoInfo.get(0));
            demoServiceDaoImpl.saveDemoInfoInstance(info);
            if(businessDemoInfo.size() == 1) {
                dataFlowContext.addParamOut("demoId", businessDemoInfo.get(0).get("demo_id"));
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

        if (info.containsKey("demoId")) {
            return;
        }

        if (!businessInfo.containsKey("demo_id")) {
            return;
        }

        info.put("demoId", businessInfo.get("demo_id"));
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
        //demo信息
        List<Map> demoInfo = demoServiceDaoImpl.getDemoInfo(info);
        if(demoInfo != null && demoInfo.size() > 0){
            reFreshShareColumn(paramIn, demoInfo.get(0));
            demoServiceDaoImpl.updateDemoInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessDemo 节点
     * @param business 总的数据节点
     * @param businessDemo demo节点
     */
    private void doBusinessDemo(Business business,JSONObject businessDemo){

        Assert.jsonObjectHaveKey(businessDemo,"demoId","businessDemo 节点下没有包含 demoId 节点");

        if(businessDemo.getString("demoId").startsWith("-")){
            //刷新缓存
            //flushDemoId(business.getDatas());

            businessDemo.put("demoId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_demoId));

        }

        businessDemo.put("bId",business.getbId());
        businessDemo.put("operate", StatusConstant.OPERATE_ADD);
        //保存demo信息
        demoServiceDaoImpl.saveBusinessDemoInfo(businessDemo);

    }

    public IDemoServiceDao getDemoServiceDaoImpl() {
        return demoServiceDaoImpl;
    }

    public void setDemoServiceDaoImpl(IDemoServiceDao demoServiceDaoImpl) {
        this.demoServiceDaoImpl = demoServiceDaoImpl;
    }
}
