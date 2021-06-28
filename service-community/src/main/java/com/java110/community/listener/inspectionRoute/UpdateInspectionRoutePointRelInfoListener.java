package com.java110.community.listener.inspectionRoute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionRoutePointRelServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.inspection.InspectionRoutePo;
import com.java110.po.inspection.InspectionRoutePointRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改巡检路线信息 侦听
 * <p>
 * 处理节点
 * 1、businessInspectionRoute:{} 巡检路线基本信息节点
 * 2、businessInspectionRouteAttr:[{}] 巡检路线属性信息节点
 * 3、businessInspectionRoutePhoto:[{}] 巡检路线照片信息节点
 * 4、businessInspectionRouteCerdentials:[{}] 巡检路线证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateInspectionRoutePointRelInfoListener")
@Transactional
public class UpdateInspectionRoutePointRelInfoListener extends AbstractInspectionRoutePointRelBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateInspectionRoutePointRelInfoListener.class);
    @Autowired
    private IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_ROUTE_POINT_REL;
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

        //处理 businessInspectionRoute 节点
        if (data.containsKey(InspectionRoutePointRelPo.class.getSimpleName())) {
            Object _obj = data.get(InspectionRoutePointRelPo.class.getSimpleName());
            JSONArray businessInspectionRoutePointRels = null;
            if (_obj instanceof JSONObject) {
                businessInspectionRoutePointRels = new JSONArray();
                businessInspectionRoutePointRels.add(_obj);
            } else {
                businessInspectionRoutePointRels = (JSONArray) _obj;
            }
            for (int _inspectionRoutePointRelIndex = 0; _inspectionRoutePointRelIndex < businessInspectionRoutePointRels.size(); _inspectionRoutePointRelIndex++) {
                JSONObject businessInspectionRoutePointRel = businessInspectionRoutePointRels.getJSONObject(_inspectionRoutePointRelIndex);
                doBusinessInspectionRoutePointRel(business, businessInspectionRoutePointRel);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("irpRelId", businessInspectionRoutePointRel.getString("irpRelId"));
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

        //巡检路线与巡检点关系信息
        List<Map> businessInspectionRoutePointRelInfos = inspectionRoutePointRelServiceDaoImpl.getBusinessInspectionRoutePointRelInfo(info);
        if (businessInspectionRoutePointRelInfos != null && businessInspectionRoutePointRelInfos.size() > 0) {
            for (int _inspectionRoutePointRelIndex = 0; _inspectionRoutePointRelIndex < businessInspectionRoutePointRelInfos.size(); _inspectionRoutePointRelIndex++) {
                Map businessInspectionRoutePointRelInfo = businessInspectionRoutePointRelInfos.get(_inspectionRoutePointRelIndex);
                flushBusinessInspectionRoutePointRelInfo(businessInspectionRoutePointRelInfo, StatusConstant.STATUS_CD_VALID);
                inspectionRoutePointRelServiceDaoImpl.updateInspectionRoutePointRelInfoInstance(businessInspectionRoutePointRelInfo);
                if (businessInspectionRoutePointRelInfo.size() == 1) {
                    dataFlowContext.addParamOut("irpRelId", businessInspectionRoutePointRelInfo.get("irp_rel_id"));
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
        //巡检路线信息
        List<Map> inspectionRouteInfo = inspectionRoutePointRelServiceDaoImpl.getInspectionRoutePointRelInfo(info);
        if (inspectionRouteInfo != null && inspectionRouteInfo.size() > 0) {

            //巡检路线与巡检点关系信息
            List<Map> businessInspectionRoutePointRelInfos = inspectionRoutePointRelServiceDaoImpl.getBusinessInspectionRoutePointRelInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessInspectionRoutePointRelInfos == null || businessInspectionRoutePointRelInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（inspectionRoute），程序内部异常,请检查！ " + delInfo);
            }
            for (int _inspectionRoutePointRelIndex = 0; _inspectionRoutePointRelIndex < businessInspectionRoutePointRelInfos.size(); _inspectionRoutePointRelIndex++) {
                Map businessInspectionRoutePointRelInfo = businessInspectionRoutePointRelInfos.get(_inspectionRoutePointRelIndex);
                flushBusinessInspectionRoutePointRelInfo(businessInspectionRoutePointRelInfo, StatusConstant.STATUS_CD_VALID);
                inspectionRoutePointRelServiceDaoImpl.updateInspectionRoutePointRelInfoInstance(businessInspectionRoutePointRelInfo);
            }
        }

    }


    /**
     * 处理 businessInspectionRoute 节点
     *
     * @param business                总的数据节点
     * @param businessInspectionRoutePointRel 巡检路线节点
     */
    private void doBusinessInspectionRoutePointRel(Business business, JSONObject businessInspectionRoutePointRel) {

        Assert.jsonObjectHaveKey(businessInspectionRoutePointRel, "irpRelId", "businessInspectionRoutePointRel 节点下没有包含 irpRelId 节点");

        if (businessInspectionRoutePointRel.getString("irpRelId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "irpRelId 错误，不能自动生成（必须已经存在的irpRelId）" + businessInspectionRoutePointRel);
        }
        //自动保存DEL
        autoSaveDelBusinessInspectionRoutePointRel(business, businessInspectionRoutePointRel);

        businessInspectionRoutePointRel.put("bId", business.getbId());
        businessInspectionRoutePointRel.put("operate", StatusConstant.OPERATE_ADD);
        //保存巡检路线与巡检点关系信息
        inspectionRoutePointRelServiceDaoImpl.saveBusinessInspectionRoutePointRelInfo(businessInspectionRoutePointRel);

    }


    public IInspectionRoutePointRelServiceDao getInspectionRoutePointRelServiceDaoImpl() {
        return inspectionRoutePointRelServiceDaoImpl;
    }

    public void setInspectionRoutePointRelServiceDaoImpl(IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl) {
        this.inspectionRoutePointRelServiceDaoImpl = inspectionRoutePointRelServiceDaoImpl;
    }


}
