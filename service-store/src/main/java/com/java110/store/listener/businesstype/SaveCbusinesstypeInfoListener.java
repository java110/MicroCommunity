package com.java110.store.listener.businesstype;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.store.dao.ICbusinesstypeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 cbusinesstype信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCbusinesstypeInfoListener")
@Transactional
public class SaveCbusinesstypeInfoListener extends AbstractCbusinesstypeBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveCbusinesstypeInfoListener.class);

    @Autowired
    private ICbusinesstypeServiceDao cbusinesstypeServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_BUSINESSTYPE_INFO;
    }

    /**
     * 保存cbusinesstype信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCbusinesstype 节点
        if(data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_SAVE_BUSINESSTYPE_INFO)){
            Object bObj = data.get(BusinessTypeConstant.BUSINESS_TYPE_SAVE_BUSINESSTYPE_INFO);
            JSONArray businessCbusinesstypes = null;
            if(bObj instanceof JSONObject){
                businessCbusinesstypes = new JSONArray();
                businessCbusinesstypes.add(bObj);
            }else {
                businessCbusinesstypes = (JSONArray)bObj;
            }
            //JSONObject businessCbusinesstype = data.getJSONObject("businessCbusinesstype");
            for (int bCbusinesstypeIndex = 0; bCbusinesstypeIndex < businessCbusinesstypes.size();bCbusinesstypeIndex++) {
                JSONObject businessCbusinesstype = businessCbusinesstypes.getJSONObject(bCbusinesstypeIndex);
                doBusinessCbusinesstype(business, businessCbusinesstype);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("id", businessCbusinesstype.getString("id"));
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

        //cbusinesstype信息
        List<Map> businessCbusinesstypeInfo = cbusinesstypeServiceDaoImpl.getBusinessCbusinesstypeInfo(info);
        if( businessCbusinesstypeInfo != null && businessCbusinesstypeInfo.size() >0) {
            reFreshShareColumn(info, businessCbusinesstypeInfo.get(0));
            cbusinesstypeServiceDaoImpl.saveCbusinesstypeInfoInstance(info);
            if(businessCbusinesstypeInfo.size() == 1) {
                dataFlowContext.addParamOut("id", businessCbusinesstypeInfo.get(0).get("id"));
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

        if (info.containsKey("id")) {
            return;
        }

        if (!businessInfo.containsKey("id")) {
            return;
        }

        info.put("id", businessInfo.get("id"));
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
        //cbusinesstype信息
        List<Map> cbusinesstypeInfo = cbusinesstypeServiceDaoImpl.getCbusinesstypeInfo(info);
        if(cbusinesstypeInfo != null && cbusinesstypeInfo.size() > 0){
            reFreshShareColumn(paramIn, cbusinesstypeInfo.get(0));
            cbusinesstypeServiceDaoImpl.updateCbusinesstypeInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessCbusinesstype 节点
     * @param business 总的数据节点
     * @param businessCbusinesstype cbusinesstype节点
     */
    private void doBusinessCbusinesstype(Business business,JSONObject businessCbusinesstype){

        Assert.jsonObjectHaveKey(businessCbusinesstype,"id","businessCbusinesstype 节点下没有包含 id 节点");

        if(businessCbusinesstype.getString("id").startsWith("-")){
            //刷新缓存
            //flushCbusinesstypeId(business.getDatas());

            businessCbusinesstype.put("id",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_id));

        }

        businessCbusinesstype.put("bId",business.getbId());
        businessCbusinesstype.put("operate", StatusConstant.OPERATE_ADD);
        //保存cbusinesstype信息
        cbusinesstypeServiceDaoImpl.saveBusinessCbusinesstypeInfo(businessCbusinesstype);

    }

    @Override
    public ICbusinesstypeServiceDao getCbusinesstypeServiceDaoImpl() {
        return cbusinesstypeServiceDaoImpl;
    }

    public void setCbusinesstypeServiceDaoImpl(ICbusinesstypeServiceDao cbusinesstypeServiceDaoImpl) {
        this.cbusinesstypeServiceDaoImpl = cbusinesstypeServiceDaoImpl;
    }
}
