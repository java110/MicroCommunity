package com.java110.store.listener.smallWeChat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.smallWechatAttr.SmallWechatAttrPo;
import com.java110.store.dao.ISmallWechatAttrServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 微信属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveSmallWechatAttrInfoListener")
@Transactional
public class SaveSmallWechatAttrInfoListener extends AbstractSmallWechatAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveSmallWechatAttrInfoListener.class);

    @Autowired
    private ISmallWechatAttrServiceDao smallWechatAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_SMALL_WECHAT_ATTR;
    }

    /**
     * 保存微信属性信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessSmallWechatAttr 节点
        if (data.containsKey(SmallWechatAttrPo.class.getSimpleName())) {
            Object bObj = data.get(SmallWechatAttrPo.class.getSimpleName());
            JSONArray businessSmallWechatAttrs = null;
            if (bObj instanceof JSONObject) {
                businessSmallWechatAttrs = new JSONArray();
                businessSmallWechatAttrs.add(bObj);
            } else {
                businessSmallWechatAttrs = (JSONArray) bObj;
            }
            //JSONObject businessSmallWechatAttr = data.getJSONObject(SmallWechatAttrPo.class.getSimpleName());
            for (int bSmallWechatAttrIndex = 0; bSmallWechatAttrIndex < businessSmallWechatAttrs.size(); bSmallWechatAttrIndex++) {
                JSONObject businessSmallWechatAttr = businessSmallWechatAttrs.getJSONObject(bSmallWechatAttrIndex);
                doBusinessSmallWechatAttr(business, businessSmallWechatAttr);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessSmallWechatAttr.getString("attrId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //微信属性信息
        List<Map> businessSmallWechatAttrInfo = smallWechatAttrServiceDaoImpl.getBusinessSmallWechatAttrInfo(info);
        if (businessSmallWechatAttrInfo != null && businessSmallWechatAttrInfo.size() > 0) {
            reFreshShareColumn(info, businessSmallWechatAttrInfo.get(0));
            smallWechatAttrServiceDaoImpl.saveSmallWechatAttrInfoInstance(info);
            if (businessSmallWechatAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessSmallWechatAttrInfo.get(0).get("attr_id"));
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
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        //微信属性信息
        List<Map> smallWechatAttrInfo = smallWechatAttrServiceDaoImpl.getSmallWechatAttrInfo(info);
        if (smallWechatAttrInfo != null && smallWechatAttrInfo.size() > 0) {
            reFreshShareColumn(paramIn, smallWechatAttrInfo.get(0));
            smallWechatAttrServiceDaoImpl.updateSmallWechatAttrInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessSmallWechatAttr 节点
     *
     * @param business                总的数据节点
     * @param businessSmallWechatAttr 微信属性节点
     */
    private void doBusinessSmallWechatAttr(Business business, JSONObject businessSmallWechatAttr) {

        Assert.jsonObjectHaveKey(businessSmallWechatAttr, "attrId", "businessSmallWechatAttr 节点下没有包含 attrId 节点");

        if (businessSmallWechatAttr.getString("attrId").startsWith("-")) {
            //刷新缓存
            //flushSmallWechatAttrId(business.getDatas());

            businessSmallWechatAttr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessSmallWechatAttr.put("bId", business.getbId());
        businessSmallWechatAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存微信属性信息
        smallWechatAttrServiceDaoImpl.saveBusinessSmallWechatAttrInfo(businessSmallWechatAttr);

    }

    @Override
    public ISmallWechatAttrServiceDao getSmallWechatAttrServiceDaoImpl() {
        return smallWechatAttrServiceDaoImpl;
    }

    public void setSmallWechatAttrServiceDaoImpl(ISmallWechatAttrServiceDao smallWechatAttrServiceDaoImpl) {
        this.smallWechatAttrServiceDaoImpl = smallWechatAttrServiceDaoImpl;
    }
}
