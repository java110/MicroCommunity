package com.java110.common.listener.advert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.advert.AdvertPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改广告信息信息 侦听
 * <p>
 * 处理节点
 * 1、businessAdvert:{} 广告信息基本信息节点
 * 2、businessAdvertAttr:[{}] 广告信息属性信息节点
 * 3、businessAdvertPhoto:[{}] 广告信息照片信息节点
 * 4、businessAdvertCerdentials:[{}] 广告信息证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateAdvertInfoListener")
@Transactional
public class UpdateAdvertInfoListener extends AbstractAdvertBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateAdvertInfoListener.class);
    @Autowired
    private IAdvertServiceDao advertServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_ADVERT;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");


        //处理 businessAdvert 节点
        if (data.containsKey(AdvertPo.class.getSimpleName())) {
            Object _obj = data.get(AdvertPo.class.getSimpleName());
            JSONArray businessAdverts = null;
            if (_obj instanceof JSONObject) {
                businessAdverts = new JSONArray();
                businessAdverts.add(_obj);
            } else {
                businessAdverts = (JSONArray) _obj;
            }
            //JSONObject businessAdvert = data.getJSONObject("businessAdvert");
            for (int _advertIndex = 0; _advertIndex < businessAdverts.size(); _advertIndex++) {
                JSONObject businessAdvert = businessAdverts.getJSONObject(_advertIndex);
                doBusinessAdvert(business, businessAdvert);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("advertId", businessAdvert.getString("advertId"));
                }
            }
        }

    }


    /**
     * business to instance 过程
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
        List<Map> businessAdvertInfos = advertServiceDaoImpl.getBusinessAdvertInfo(info);
        if (businessAdvertInfos != null && businessAdvertInfos.size() > 0) {
            for (int _advertIndex = 0; _advertIndex < businessAdvertInfos.size(); _advertIndex++) {
                Map businessAdvertInfo = businessAdvertInfos.get(_advertIndex);
                flushBusinessAdvertInfo(businessAdvertInfo, StatusConstant.STATUS_CD_VALID);
                advertServiceDaoImpl.updateAdvertInfoInstance(businessAdvertInfo);
                if (businessAdvertInfo.size() == 1) {
                    dataFlowContext.addParamOut("advertId", businessAdvertInfo.get("advert_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //广告信息信息
        List<Map> advertInfo = advertServiceDaoImpl.getAdvertInfo(info);
        if (advertInfo != null && advertInfo.size() > 0) {

            //广告信息信息
            List<Map> businessAdvertInfos = advertServiceDaoImpl.getBusinessAdvertInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessAdvertInfos == null || businessAdvertInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（advert），程序内部异常,请检查！ " + delInfo);
            }
            for (int _advertIndex = 0; _advertIndex < businessAdvertInfos.size(); _advertIndex++) {
                Map businessAdvertInfo = businessAdvertInfos.get(_advertIndex);
                flushBusinessAdvertInfo(businessAdvertInfo, StatusConstant.STATUS_CD_VALID);
                advertServiceDaoImpl.updateAdvertInfoInstance(businessAdvertInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "advertId 错误，不能自动生成（必须已经存在的advertId）" + businessAdvert);
        }
        //自动保存DEL
        autoSaveDelBusinessAdvert(business, businessAdvert);

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
