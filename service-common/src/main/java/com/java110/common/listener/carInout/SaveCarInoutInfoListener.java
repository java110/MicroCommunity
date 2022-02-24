package com.java110.common.listener.carInout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.car.CarInoutPo;
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
 * 保存 进出场信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCarInoutInfoListener")
@Transactional
public class SaveCarInoutInfoListener extends AbstractCarInoutBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveCarInoutInfoListener.class);

    @Autowired
    private ICarInoutServiceDao carInoutServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_CAR_INOUT;
    }

    /**
     * 保存进出场信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessCarInout 节点
        if (data.containsKey(CarInoutPo.class.getSimpleName())) {
            Object bObj = data.get(CarInoutPo.class.getSimpleName());
            JSONArray businessCarInouts = null;
            if (bObj instanceof JSONObject) {
                businessCarInouts = new JSONArray();
                businessCarInouts.add(bObj);
            } else {
                businessCarInouts = (JSONArray) bObj;
            }
            //JSONObject businessCarInout = data.getJSONObject("businessCarInout");
            for (int bCarInoutIndex = 0; bCarInoutIndex < businessCarInouts.size(); bCarInoutIndex++) {
                JSONObject businessCarInout = businessCarInouts.getJSONObject(bCarInoutIndex);
                doBusinessCarInout(business, businessCarInout);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("inoutId", businessCarInout.getString("inoutId"));
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

        //进出场信息
        List<Map> businessCarInoutInfo = carInoutServiceDaoImpl.getBusinessCarInoutInfo(info);
        if (businessCarInoutInfo != null && businessCarInoutInfo.size() > 0) {
            reFreshShareColumn(info, businessCarInoutInfo.get(0));
            carInoutServiceDaoImpl.saveCarInoutInfoInstance(info);
            if (businessCarInoutInfo.size() == 1) {
                dataFlowContext.addParamOut("inoutId", businessCarInoutInfo.get(0).get("inout_id"));
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

        if (info.containsKey("inoutId")) {
            return;
        }

        if (!businessInfo.containsKey("inout_id")) {
            return;
        }

        info.put("inoutId", businessInfo.get("inout_id"));
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
        //进出场信息
        List<Map> carInoutInfo = carInoutServiceDaoImpl.getCarInoutInfo(info);
        if (carInoutInfo != null && carInoutInfo.size() > 0) {
            reFreshShareColumn(paramIn, carInoutInfo.get(0));
            carInoutServiceDaoImpl.updateCarInoutInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessCarInout 节点
     *
     * @param business         总的数据节点
     * @param businessCarInout 进出场节点
     */
    private void doBusinessCarInout(Business business, JSONObject businessCarInout) {

        Assert.jsonObjectHaveKey(businessCarInout, "inoutId", "businessCarInout 节点下没有包含 inoutId 节点");

        if (businessCarInout.getString("inoutId").startsWith("-")) {
            //刷新缓存
            //flushCarInoutId(business.getDatas());

            businessCarInout.put("inoutId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_inoutId));

        }

        businessCarInout.put("bId", business.getbId());
        businessCarInout.put("operate", StatusConstant.OPERATE_ADD);
        //保存进出场信息
        carInoutServiceDaoImpl.saveBusinessCarInoutInfo(businessCarInout);

    }

    public ICarInoutServiceDao getCarInoutServiceDaoImpl() {
        return carInoutServiceDaoImpl;
    }

    public void setCarInoutServiceDaoImpl(ICarInoutServiceDao carInoutServiceDaoImpl) {
        this.carInoutServiceDaoImpl = carInoutServiceDaoImpl;
    }
}
