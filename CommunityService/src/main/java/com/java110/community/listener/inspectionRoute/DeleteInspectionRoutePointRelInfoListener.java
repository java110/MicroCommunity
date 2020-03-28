package com.java110.community.listener.inspectionRoute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionRoutePointRelServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
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
 * 删除巡检路线巡检点关系信息 侦听
 * <p>
 * 处理节点
 * 1、businessInspectionRoutePointRel:{} 巡检路线巡检点关系基本信息节点
 * 2、businessInspectionRoutePointRelAttr:[{}] 巡检路线巡检点关系属性信息节点
 * 3、businessInspectionRoutePointRelPhoto:[{}] 巡检路线巡检点关系照片信息节点
 * 4、businessInspectionRoutePointRelCerdentials:[{}] 巡检路线巡检点关系证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteInspectionRoutePointRelInfoListener")
@Transactional
public class DeleteInspectionRoutePointRelInfoListener extends AbstractInspectionRoutePointRelBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteInspectionRoutePointRelInfoListener.class);
    @Autowired
    IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_INSPECTION_ROUTE_POINT_REL;
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

        //处理 businessInspectionRoutePointRel 节点
        if (data.containsKey("businessInspectionRoutePointRel")) {
            //处理 businessInspectionRoutePointRel 节点
            if (data.containsKey("businessInspectionRoutePointRel")) {
                Object _obj = data.get("businessInspectionRoutePointRel");
                JSONArray businessInspectionRoutePointRels = null;
                if (_obj instanceof JSONObject) {
                    businessInspectionRoutePointRels = new JSONArray();
                    businessInspectionRoutePointRels.add(_obj);
                } else {
                    businessInspectionRoutePointRels = (JSONArray) _obj;
                }
                //JSONObject businessInspectionRoutePointRel = data.getJSONObject("businessInspectionRoutePointRel");
                for (int _inspectionRoutePointRelIndex = 0; _inspectionRoutePointRelIndex < businessInspectionRoutePointRels.size(); _inspectionRoutePointRelIndex++) {
                    JSONObject businessInspectionRoutePointRel = businessInspectionRoutePointRels.getJSONObject(_inspectionRoutePointRelIndex);
                    doBusinessInspectionRoutePointRel(business, businessInspectionRoutePointRel);
                    if (_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("irpRelId", businessInspectionRoutePointRel.getString("irpRelId"));
                    }
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

        //巡检路线巡检点关系信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //巡检路线巡检点关系信息
        List<Map> businessInspectionRoutePointRelInfos = inspectionRoutePointRelServiceDaoImpl.getBusinessInspectionRoutePointRelInfo(info);
        if (businessInspectionRoutePointRelInfos != null && businessInspectionRoutePointRelInfos.size() > 0) {
            for (int _inspectionRoutePointRelIndex = 0; _inspectionRoutePointRelIndex < businessInspectionRoutePointRelInfos.size(); _inspectionRoutePointRelIndex++) {
                Map businessInspectionRoutePointRelInfo = businessInspectionRoutePointRelInfos.get(_inspectionRoutePointRelIndex);
                flushBusinessInspectionRoutePointRelInfo(businessInspectionRoutePointRelInfo, StatusConstant.STATUS_CD_INVALID);
                inspectionRoutePointRelServiceDaoImpl.updateInspectionRoutePointRelInfoInstance(businessInspectionRoutePointRelInfo);
                dataFlowContext.addParamOut("irpRelId", businessInspectionRoutePointRelInfo.get("irp_rel_id"));
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
        //巡检路线巡检点关系信息
        List<Map> inspectionRoutePointRelInfo = inspectionRoutePointRelServiceDaoImpl.getInspectionRoutePointRelInfo(info);
        if (inspectionRoutePointRelInfo != null && inspectionRoutePointRelInfo.size() > 0) {

            //巡检路线巡检点关系信息
            List<Map> businessInspectionRoutePointRelInfos = inspectionRoutePointRelServiceDaoImpl.getBusinessInspectionRoutePointRelInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessInspectionRoutePointRelInfos == null || businessInspectionRoutePointRelInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（inspectionRoutePointRel），程序内部异常,请检查！ " + delInfo);
            }
            for (int _inspectionRoutePointRelIndex = 0; _inspectionRoutePointRelIndex < businessInspectionRoutePointRelInfos.size(); _inspectionRoutePointRelIndex++) {
                Map businessInspectionRoutePointRelInfo = businessInspectionRoutePointRelInfos.get(_inspectionRoutePointRelIndex);
                flushBusinessInspectionRoutePointRelInfo(businessInspectionRoutePointRelInfo, StatusConstant.STATUS_CD_VALID);
                inspectionRoutePointRelServiceDaoImpl.updateInspectionRoutePointRelInfoInstance(businessInspectionRoutePointRelInfo);
            }
        }
    }


    /**
     * 处理 businessInspectionRoutePointRel 节点
     *
     * @param business                        总的数据节点
     * @param businessInspectionRoutePointRel 巡检路线巡检点关系节点
     */
    private void doBusinessInspectionRoutePointRel(Business business, JSONObject businessInspectionRoutePointRel) {

        Assert.jsonObjectHaveKey(businessInspectionRoutePointRel, "irpRelId", "businessInspectionRoutePointRel 节点下没有包含 irpRelId 节点");

        if (businessInspectionRoutePointRel.getString("irpRelId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "irpRelId 错误，不能自动生成（必须已经存在的irpRelId）" + businessInspectionRoutePointRel);
        }
        //自动插入DEL
        autoSaveDelBusinessInspectionRoutePointRel(business, businessInspectionRoutePointRel);
    }

    public IInspectionRoutePointRelServiceDao getInspectionRoutePointRelServiceDaoImpl() {
        return inspectionRoutePointRelServiceDaoImpl;
    }

    public void setInspectionRoutePointRelServiceDaoImpl(IInspectionRoutePointRelServiceDao inspectionRoutePointRelServiceDaoImpl) {
        this.inspectionRoutePointRelServiceDaoImpl = inspectionRoutePointRelServiceDaoImpl;
    }
}
