package com.java110.common.listener.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.advert.AdvertPo;
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
 * 保存 广告信息信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveAdvertInfoListener")
@Transactional
public class SaveAdvertInfoListener extends AbstractAdvertBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveAdvertInfoListener.class);

    @Autowired
    private IAdvertServiceDao advertServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_ADVERT;
    }

    /**
     * 保存广告信息信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessAdvert 节点
        if (data.containsKey(AdvertPo.class.getSimpleName())) {
            Object bObj = data.get(AdvertPo.class.getSimpleName());
            JSONArray businessAdverts = null;
            if (bObj instanceof JSONObject) {
                businessAdverts = new JSONArray();
                businessAdverts.add(bObj);
            } else {
                businessAdverts = (JSONArray) bObj;
            }
            //JSONObject businessAdvert = data.getJSONObject("businessAdvert");
            for (int bAdvertIndex = 0; bAdvertIndex < businessAdverts.size(); bAdvertIndex++) {
                JSONObject businessAdvert = businessAdverts.getJSONObject(bAdvertIndex);
                doBusinessAdvert(business, businessAdvert);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("advertId", businessAdvert.getString("advertId"));
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

        //广告信息信息
        List<Map> businessAdvertInfo = advertServiceDaoImpl.getBusinessAdvertInfo(info);
        if (businessAdvertInfo != null && businessAdvertInfo.size() > 0) {
            reFreshShareColumn(info, businessAdvertInfo.get(0));
            advertServiceDaoImpl.saveAdvertInfoInstance(info);
            if (businessAdvertInfo.size() == 1) {
                dataFlowContext.addParamOut("advertId", businessAdvertInfo.get(0).get("advert_id"));
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
        //广告信息信息
        List<Map> advertInfo = advertServiceDaoImpl.getAdvertInfo(info);
        if (advertInfo != null && advertInfo.size() > 0) {
            reFreshShareColumn(paramIn, advertInfo.get(0));
            advertServiceDaoImpl.updateAdvertInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessAdvert 节点
     *
     * @param business       总的数据节点
     * @param businessAdvert 广告信息节点
     */
    private void doBusinessAdvert(Business business, JSONObject businessAdvert) {

        Assert.jsonObjectHaveKey(businessAdvert, "advertId", "businessAdvert 节点下没有包含 advertId 节点");

        if (businessAdvert.getString("advertId").startsWith("-")) {
            //刷新缓存
            //flushAdvertId(business.getDatas());

            businessAdvert.put("advertId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_advertId));

        }

        businessAdvert.put("bId", business.getbId());
        businessAdvert.put("operate", StatusConstant.OPERATE_ADD);
        //保存广告信息信息
        advertServiceDaoImpl.saveBusinessAdvertInfo(businessAdvert);

    }

    public IAdvertServiceDao getAdvertServiceDaoImpl() {
        return advertServiceDaoImpl;
    }

    public void setAdvertServiceDaoImpl(IAdvertServiceDao advertServiceDaoImpl) {
        this.advertServiceDaoImpl = advertServiceDaoImpl;
    }
}
