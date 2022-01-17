package com.java110.common.listener.carInout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.car.CarInoutPo;
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
 * 修改进出场信息 侦听
 * <p>
 * 处理节点
 * 1、businessCarInout:{} 进出场基本信息节点
 * 2、businessCarInoutAttr:[{}] 进出场属性信息节点
 * 3、businessCarInoutPhoto:[{}] 进出场照片信息节点
 * 4、businessCarInoutCerdentials:[{}] 进出场证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCarInoutInfoListener")
@Transactional
public class UpdateCarInoutInfoListener extends AbstractCarInoutBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateCarInoutInfoListener.class);
    @Autowired
    private ICarInoutServiceDao carInoutServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_CAR_INOUT;
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

        //处理 businessCarInout 节点
        if (data.containsKey(CarInoutPo.class.getSimpleName())) {
            Object _obj = data.get(CarInoutPo.class.getSimpleName());
            JSONArray businessCarInouts = null;
            if (_obj instanceof JSONObject) {
                businessCarInouts = new JSONArray();
                businessCarInouts.add(_obj);
            } else {
                businessCarInouts = (JSONArray) _obj;
            }
            //JSONObject businessCarInout = data.getJSONObject("businessCarInout");
            for (int _carInoutIndex = 0; _carInoutIndex < businessCarInouts.size(); _carInoutIndex++) {
                JSONObject businessCarInout = businessCarInouts.getJSONObject(_carInoutIndex);
                doBusinessCarInout(business, businessCarInout);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("inoutId", businessCarInout.getString("inoutId"));
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

        //进出场信息
        List<Map> businessCarInoutInfos = carInoutServiceDaoImpl.getBusinessCarInoutInfo(info);
        if (businessCarInoutInfos != null && businessCarInoutInfos.size() > 0) {
            for (int _carInoutIndex = 0; _carInoutIndex < businessCarInoutInfos.size(); _carInoutIndex++) {
                Map businessCarInoutInfo = businessCarInoutInfos.get(_carInoutIndex);
                flushBusinessCarInoutInfo(businessCarInoutInfo, StatusConstant.STATUS_CD_VALID);
                carInoutServiceDaoImpl.updateCarInoutInfoInstance(businessCarInoutInfo);
                if (businessCarInoutInfo.size() == 1) {
                    dataFlowContext.addParamOut("inoutId", businessCarInoutInfo.get("inout_id"));
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
        //进出场信息
        List<Map> carInoutInfo = carInoutServiceDaoImpl.getCarInoutInfo(info);
        if (carInoutInfo != null && carInoutInfo.size() > 0) {

            //进出场信息
            List<Map> businessCarInoutInfos = carInoutServiceDaoImpl.getBusinessCarInoutInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCarInoutInfos == null || businessCarInoutInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（carInout），程序内部异常,请检查！ " + delInfo);
            }
            for (int _carInoutIndex = 0; _carInoutIndex < businessCarInoutInfos.size(); _carInoutIndex++) {
                Map businessCarInoutInfo = businessCarInoutInfos.get(_carInoutIndex);
                flushBusinessCarInoutInfo(businessCarInoutInfo, StatusConstant.STATUS_CD_VALID);
                carInoutServiceDaoImpl.updateCarInoutInfoInstance(businessCarInoutInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "inoutId 错误，不能自动生成（必须已经存在的inoutId）" + businessCarInout);
        }
        //自动保存DEL
        autoSaveDelBusinessCarInout(business, businessCarInout);

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
