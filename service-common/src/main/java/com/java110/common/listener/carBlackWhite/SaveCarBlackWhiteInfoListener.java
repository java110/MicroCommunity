package com.java110.common.listener.carBlackWhite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarBlackWhiteServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.car.CarBlackWhitePo;
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
 * 保存 黑白名单信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCarBlackWhiteInfoListener")
@Transactional
public class SaveCarBlackWhiteInfoListener extends AbstractCarBlackWhiteBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveCarBlackWhiteInfoListener.class);

    @Autowired
    private ICarBlackWhiteServiceDao carBlackWhiteServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_BLACK_WHITE;
    }

    /**
     * 保存黑白名单信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCarBlackWhite 节点
        if (data.containsKey(CarBlackWhitePo.class.getSimpleName())) {
            Object bObj = data.get(CarBlackWhitePo.class.getSimpleName());
            JSONArray businessCarBlackWhites = null;
            if (bObj instanceof JSONObject) {
                businessCarBlackWhites = new JSONArray();
                businessCarBlackWhites.add(bObj);
            } else {
                businessCarBlackWhites = (JSONArray) bObj;
            }
            //JSONObject businessCarBlackWhite = data.getJSONObject("businessCarBlackWhite");
            for (int bCarBlackWhiteIndex = 0; bCarBlackWhiteIndex < businessCarBlackWhites.size(); bCarBlackWhiteIndex++) {
                JSONObject businessCarBlackWhite = businessCarBlackWhites.getJSONObject(bCarBlackWhiteIndex);
                doBusinessCarBlackWhite(business, businessCarBlackWhite);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("bwId", businessCarBlackWhite.getString("bwId"));
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

        //黑白名单信息
        List<Map> businessCarBlackWhiteInfo = carBlackWhiteServiceDaoImpl.getBusinessCarBlackWhiteInfo(info);
        if (businessCarBlackWhiteInfo != null && businessCarBlackWhiteInfo.size() > 0) {
            reFreshShareColumn(info, businessCarBlackWhiteInfo.get(0));
            carBlackWhiteServiceDaoImpl.saveCarBlackWhiteInfoInstance(info);
            if (businessCarBlackWhiteInfo.size() == 1) {
                dataFlowContext.addParamOut("bwId", businessCarBlackWhiteInfo.get(0).get("bw_id"));
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
        //黑白名单信息
        List<Map> carBlackWhiteInfo = carBlackWhiteServiceDaoImpl.getCarBlackWhiteInfo(info);
        if (carBlackWhiteInfo != null && carBlackWhiteInfo.size() > 0) {
            reFreshShareColumn(paramIn, carBlackWhiteInfo.get(0));
            carBlackWhiteServiceDaoImpl.updateCarBlackWhiteInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessCarBlackWhite 节点
     *
     * @param business              总的数据节点
     * @param businessCarBlackWhite 黑白名单节点
     */
    private void doBusinessCarBlackWhite(Business business, JSONObject businessCarBlackWhite) {

        Assert.jsonObjectHaveKey(businessCarBlackWhite, "bwId", "businessCarBlackWhite 节点下没有包含 bwId 节点");

        if (businessCarBlackWhite.getString("bwId").startsWith("-")) {
            //刷新缓存
            //flushCarBlackWhiteId(business.getDatas());

            businessCarBlackWhite.put("bwId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_bwId));

        }

        businessCarBlackWhite.put("bId", business.getbId());
        businessCarBlackWhite.put("operate", StatusConstant.OPERATE_ADD);
        //保存黑白名单信息
        carBlackWhiteServiceDaoImpl.saveBusinessCarBlackWhiteInfo(businessCarBlackWhite);

    }

    public ICarBlackWhiteServiceDao getCarBlackWhiteServiceDaoImpl() {
        return carBlackWhiteServiceDaoImpl;
    }

    public void setCarBlackWhiteServiceDaoImpl(ICarBlackWhiteServiceDao carBlackWhiteServiceDaoImpl) {
        this.carBlackWhiteServiceDaoImpl = carBlackWhiteServiceDaoImpl;
    }
}
