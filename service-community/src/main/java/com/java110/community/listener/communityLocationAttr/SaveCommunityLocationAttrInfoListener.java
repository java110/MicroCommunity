package com.java110.community.listener.communityLocationAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.communityLocationAttr.CommunityLocationAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.community.dao.ICommunityLocationAttrServiceDao;
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
 * 保存 位置属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommunityLocationAttrInfoListener")
@Transactional
public class SaveCommunityLocationAttrInfoListener extends AbstractCommunityLocationAttrBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveCommunityLocationAttrInfoListener.class);

    @Autowired
    private ICommunityLocationAttrServiceDao communityLocationAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_LOCATION_ATTR;
    }

    /**
     * 保存位置属性信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCommunityLocationAttr 节点
        if(data.containsKey(CommunityLocationAttrPo.class.getSimpleName())){
            Object bObj = data.get(CommunityLocationAttrPo.class.getSimpleName());
            JSONArray businessCommunityLocationAttrs = null;
            if(bObj instanceof JSONObject){
                businessCommunityLocationAttrs = new JSONArray();
                businessCommunityLocationAttrs.add(bObj);
            }else {
                businessCommunityLocationAttrs = (JSONArray)bObj;
            }
            //JSONObject businessCommunityLocationAttr = data.getJSONObject(CommunityLocationAttrPo.class.getSimpleName());
            for (int bCommunityLocationAttrIndex = 0; bCommunityLocationAttrIndex < businessCommunityLocationAttrs.size();bCommunityLocationAttrIndex++) {
                JSONObject businessCommunityLocationAttr = businessCommunityLocationAttrs.getJSONObject(bCommunityLocationAttrIndex);
                doBusinessCommunityLocationAttr(business, businessCommunityLocationAttr);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessCommunityLocationAttr.getString("attrId"));
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

        //位置属性信息
        List<Map> businessCommunityLocationAttrInfo = communityLocationAttrServiceDaoImpl.getBusinessCommunityLocationAttrInfo(info);
        if( businessCommunityLocationAttrInfo != null && businessCommunityLocationAttrInfo.size() >0) {
            reFreshShareColumn(info, businessCommunityLocationAttrInfo.get(0));
            communityLocationAttrServiceDaoImpl.saveCommunityLocationAttrInfoInstance(info);
            if(businessCommunityLocationAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessCommunityLocationAttrInfo.get(0).get("attr_id"));
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
        //位置属性信息
        List<Map> communityLocationAttrInfo = communityLocationAttrServiceDaoImpl.getCommunityLocationAttrInfo(info);
        if(communityLocationAttrInfo != null && communityLocationAttrInfo.size() > 0){
            reFreshShareColumn(paramIn, communityLocationAttrInfo.get(0));
            communityLocationAttrServiceDaoImpl.updateCommunityLocationAttrInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessCommunityLocationAttr 节点
     * @param business 总的数据节点
     * @param businessCommunityLocationAttr 位置属性节点
     */
    private void doBusinessCommunityLocationAttr(Business business,JSONObject businessCommunityLocationAttr){

        Assert.jsonObjectHaveKey(businessCommunityLocationAttr,"attrId","businessCommunityLocationAttr 节点下没有包含 attrId 节点");

        if(businessCommunityLocationAttr.getString("attrId").startsWith("-")){
            //刷新缓存
            //flushCommunityLocationAttrId(business.getDatas());

            businessCommunityLocationAttr.put("attrId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessCommunityLocationAttr.put("bId",business.getbId());
        businessCommunityLocationAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存位置属性信息
        communityLocationAttrServiceDaoImpl.saveBusinessCommunityLocationAttrInfo(businessCommunityLocationAttr);

    }
    @Override
    public ICommunityLocationAttrServiceDao getCommunityLocationAttrServiceDaoImpl() {
        return communityLocationAttrServiceDaoImpl;
    }

    public void setCommunityLocationAttrServiceDaoImpl(ICommunityLocationAttrServiceDao communityLocationAttrServiceDaoImpl) {
        this.communityLocationAttrServiceDaoImpl = communityLocationAttrServiceDaoImpl;
    }
}
