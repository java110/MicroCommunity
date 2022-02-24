package com.java110.common.listener.carInoutDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutDetailServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.car.CarInoutDetailPo;
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
 * 删除进出场详情信息 侦听
 * <p>
 * 处理节点
 * 1、businessCarInoutDetail:{} 进出场详情基本信息节点
 * 2、businessCarInoutDetailAttr:[{}] 进出场详情属性信息节点
 * 3、businessCarInoutDetailPhoto:[{}] 进出场详情照片信息节点
 * 4、businessCarInoutDetailCerdentials:[{}] 进出场详情证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteCarInoutDetailInfoListener")
@Transactional
public class DeleteCarInoutDetailInfoListener extends AbstractCarInoutDetailBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteCarInoutDetailInfoListener.class);
    @Autowired
    ICarInoutDetailServiceDao carInoutDetailServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_CAR_INOUT_DETAIL;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
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
            Object _obj = data.get(CarInoutDetailPo.class.getSimpleName());
            JSONArray businessCarInoutDetails = null;
            if (_obj instanceof JSONObject) {
                businessCarInoutDetails = new JSONArray();
                businessCarInoutDetails.add(_obj);
            } else {
                businessCarInoutDetails = (JSONArray) _obj;
            }
            //JSONObject businessCarInoutDetail = data.getJSONObject("businessCarInoutDetail");
            for (int _carInoutDetailIndex = 0; _carInoutDetailIndex < businessCarInoutDetails.size(); _carInoutDetailIndex++) {
                JSONObject businessCarInoutDetail = businessCarInoutDetails.getJSONObject(_carInoutDetailIndex);
                doBusinessCarInoutDetail(business, businessCarInoutDetail);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("detailId", businessCarInoutDetail.getString("detailId"));
                }

            }
        }


    }

    /**
     * 删除 instance数据
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //进出场详情信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //进出场详情信息
        List<Map> businessCarInoutDetailInfos = carInoutDetailServiceDaoImpl.getBusinessCarInoutDetailInfo(info);
        if (businessCarInoutDetailInfos != null && businessCarInoutDetailInfos.size() > 0) {
            for (int _carInoutDetailIndex = 0; _carInoutDetailIndex < businessCarInoutDetailInfos.size(); _carInoutDetailIndex++) {
                Map businessCarInoutDetailInfo = businessCarInoutDetailInfos.get(_carInoutDetailIndex);
                flushBusinessCarInoutDetailInfo(businessCarInoutDetailInfo, StatusConstant.STATUS_CD_INVALID);
                carInoutDetailServiceDaoImpl.updateCarInoutDetailInfoInstance(businessCarInoutDetailInfo);
                dataFlowContext.addParamOut("detailId", businessCarInoutDetailInfo.get("detail_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        //进出场详情信息
        List<Map> carInoutDetailInfo = carInoutDetailServiceDaoImpl.getCarInoutDetailInfo(info);
        if (carInoutDetailInfo != null && carInoutDetailInfo.size() > 0) {

            //进出场详情信息
            List<Map> businessCarInoutDetailInfos = carInoutDetailServiceDaoImpl.getBusinessCarInoutDetailInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessCarInoutDetailInfos == null || businessCarInoutDetailInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（carInoutDetail），程序内部异常,请检查！ " + delInfo);
            }
            for (int _carInoutDetailIndex = 0; _carInoutDetailIndex < businessCarInoutDetailInfos.size(); _carInoutDetailIndex++) {
                Map businessCarInoutDetailInfo = businessCarInoutDetailInfos.get(_carInoutDetailIndex);
                flushBusinessCarInoutDetailInfo(businessCarInoutDetailInfo, StatusConstant.STATUS_CD_VALID);
                carInoutDetailServiceDaoImpl.updateCarInoutDetailInfoInstance(businessCarInoutDetailInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "detailId 错误，不能自动生成（必须已经存在的detailId）" + businessCarInoutDetail);
        }
        //自动插入DEL
        autoSaveDelBusinessCarInoutDetail(business, businessCarInoutDetail);
    }

    public ICarInoutDetailServiceDao getCarInoutDetailServiceDaoImpl() {
        return carInoutDetailServiceDaoImpl;
    }

    public void setCarInoutDetailServiceDaoImpl(ICarInoutDetailServiceDao carInoutDetailServiceDaoImpl) {
        this.carInoutDetailServiceDaoImpl = carInoutDetailServiceDaoImpl;
    }
}
