package com.java110.user.listener.userAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.userAttr.UserAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.user.dao.IUserAttrServiceDao;
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
 * 保存 用户属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveUserAttrInfoListener")
@Transactional
public class SaveUserAttrInfoListener extends AbstractUserAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveUserAttrInfoListener.class);

    @Autowired
    private IUserAttrServiceDao userAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_USER_ATTR_INFO;
    }

    /**
     * 保存用户属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessUserAttr 节点
        if(data.containsKey(UserAttrPo.class.getSimpleName())){
            Object bObj = data.get(UserAttrPo.class.getSimpleName());
            JSONArray businessUserAttrs = null;
            if(bObj instanceof JSONObject){
                businessUserAttrs = new JSONArray();
                businessUserAttrs.add(bObj);
            }else {
                businessUserAttrs = (JSONArray)bObj;
            }
            //JSONObject businessUserAttr = data.getJSONObject(UserAttrPo.class.getSimpleName());
            for (int bUserAttrIndex = 0; bUserAttrIndex < businessUserAttrs.size();bUserAttrIndex++) {
                JSONObject businessUserAttr = businessUserAttrs.getJSONObject(bUserAttrIndex);
                doBusinessUserAttr(business, businessUserAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessUserAttr.getString("attrId"));
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

        //用户属性信息
        List<Map> businessUserAttrInfo = userAttrServiceDaoImpl.getBusinessUserAttrInfo(info);
        if( businessUserAttrInfo != null && businessUserAttrInfo.size() >0) {
            reFreshShareColumn(info, businessUserAttrInfo.get(0));
            userAttrServiceDaoImpl.saveUserAttrInfoInstance(info);
            if(businessUserAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessUserAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("userId")) {
            return;
        }

        if (!businessInfo.containsKey("user_id")) {
            return;
        }

        info.put("userId", businessInfo.get("user_id"));
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
        //用户属性信息
        List<Map> userAttrInfo = userAttrServiceDaoImpl.getUserAttrInfo(info);
        if(userAttrInfo != null && userAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, userAttrInfo.get(0));
            userAttrServiceDaoImpl.updateUserAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessUserAttr 节点
     * @param business 总的数据节点
     * @param businessUserAttr 用户属性节点
     */
    private void doBusinessUserAttr(Business business,JSONObject businessUserAttr){

        Assert.jsonObjectHaveKey(businessUserAttr,"attrId","businessUserAttr 节点下没有包含 attrId 节点");

        if(businessUserAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushUserAttrId(business.getDatas());

            businessUserAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessUserAttr.put("bId",business.getbId());
        businessUserAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存用户属性信息
        userAttrServiceDaoImpl.saveBusinessUserAttrInfo(businessUserAttr);

    }
    @Override
    public IUserAttrServiceDao getUserAttrServiceDaoImpl() {
        return userAttrServiceDaoImpl;
    }

    public void setUserAttrServiceDaoImpl(IUserAttrServiceDao userAttrServiceDaoImpl) {
        this.userAttrServiceDaoImpl = userAttrServiceDaoImpl;
    }
}
