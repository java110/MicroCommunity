package com.java110.common.listener.carInoutDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutDetailServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.car.CarInoutDetailPo;
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
 * 保存 进出场详情信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCarInoutDetailInfoListener")
@Transactional
public class SaveCarInoutDetailInfoListener extends AbstractCarInoutDetailBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveCarInoutDetailInfoListener.class);

    @Autowired
    private ICarInoutDetailServiceDao carInoutDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT_DETAIL;
    }

    /**
     * 保存进出场详情信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCarInoutDetail 节点
        if (data.containsKey(CarInoutDetailPo.class.getSimpleName())) {
            Object bObj = data.get(CarInoutDetailPo.class.getSimpleName());
            JSONArray businessCarInoutDetails = null;
            if (bObj instanceof JSONObject) {
                businessCarInoutDetails = new JSONArray();
                businessCarInoutDetails.add(bObj);
            } else {
                businessCarInoutDetails = (JSONArray) bObj;
            }
            //JSONObject businessCarInoutDetail = data.getJSONObject("businessCarInoutDetail");
            for (int bCarInoutDetailIndex = 0; bCarInoutDetailIndex < businessCarInoutDetails.size(); bCarInoutDetailIndex++) {
                JSONObject businessCarInoutDetail = businessCarInoutDetails.getJSONObject(bCarInoutDetailIndex);
                doBusinessCarInoutDetail(business, businessCarInoutDetail);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("detailId", businessCarInoutDetail.getString("detailId"));
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

        //进出场详情信息
        List<Map> businessCarInoutDetailInfo = carInoutDetailServiceDaoImpl.getBusinessCarInoutDetailInfo(info);
        if (businessCarInoutDetailInfo != null && businessCarInoutDetailInfo.size() > 0) {
            reFreshShareColumn(info, businessCarInoutDetailInfo.get(0));
            carInoutDetailServiceDaoImpl.saveCarInoutDetailInfoInstance(info);
            if (businessCarInoutDetailInfo.size() == 1) {
                dataFlowContext.addParamOut("detailId", businessCarInoutDetailInfo.get(0).get("detail_id"));
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
        //进出场详情信息
        List<Map> carInoutDetailInfo = carInoutDetailServiceDaoImpl.getCarInoutDetailInfo(info);
        if (carInoutDetailInfo != null && carInoutDetailInfo.size() > 0) {
            reFreshShareColumn(paramIn, carInoutDetailInfo.get(0));
            carInoutDetailServiceDaoImpl.updateCarInoutDetailInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessCarInoutDetail 节点
     *
     * @param business               总的数据节点
     * @param businessCarInoutDetail 进出场详情节点
     */
    private void doBusinessCarInoutDetail(Business business, JSONObject businessCarInoutDetail) {

        Assert.jsonObjectHaveKey(businessCarInoutDetail, "detailId", "businessCarInoutDetail 节点下没有包含 detailId 节点");

        if (businessCarInoutDetail.getString("detailId").startsWith("-")) {
            //刷新缓存
            //flushCarInoutDetailId(business.getDatas());

            businessCarInoutDetail.put("detailId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));

        }

        businessCarInoutDetail.put("bId", business.getbId());
        businessCarInoutDetail.put("operate", StatusConstant.OPERATE_ADD);
        //保存进出场详情信息
        carInoutDetailServiceDaoImpl.saveBusinessCarInoutDetailInfo(businessCarInoutDetail);

    }

    public ICarInoutDetailServiceDao getCarInoutDetailServiceDaoImpl() {
        return carInoutDetailServiceDaoImpl;
    }

    public void setCarInoutDetailServiceDaoImpl(ICarInoutDetailServiceDao carInoutDetailServiceDaoImpl) {
        this.carInoutDetailServiceDaoImpl = carInoutDetailServiceDaoImpl;
    }
}
