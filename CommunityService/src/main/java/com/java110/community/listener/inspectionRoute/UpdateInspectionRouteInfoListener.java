package com.java110.community.listener.inspectionRoute;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.community.dao.IInspectionRouteServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改巡检路线信息 侦听
 *
 * 处理节点
 * 1、businessInspectionRoute:{} 巡检路线基本信息节点
 * 2、businessInspectionRouteAttr:[{}] 巡检路线属性信息节点
 * 3、businessInspectionRoutePhoto:[{}] 巡检路线照片信息节点
 * 4、businessInspectionRouteCerdentials:[{}] 巡检路线证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateInspectionRouteInfoListener")
@Transactional
public class UpdateInspectionRouteInfoListener extends AbstractInspectionRouteBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateInspectionRouteInfoListener.class);
    @Autowired
    private IInspectionRouteServiceDao inspectionRouteServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_INSPECTION_ROUTE;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessInspectionRoute 节点
        if(data.containsKey("businessInspectionRoute")){
            //处理 businessInspectionRoute 节点
            if(data.containsKey("businessInspectionRoute")){
                Object _obj = data.get("businessInspectionRoute");
                JSONArray businessInspectionRoutes = null;
                if(_obj instanceof JSONObject){
                    businessInspectionRoutes = new JSONArray();
                    businessInspectionRoutes.add(_obj);
                }else {
                    businessInspectionRoutes = (JSONArray)_obj;
                }
                //JSONObject businessInspectionRoute = data.getJSONObject("businessInspectionRoute");
                for (int _inspectionRouteIndex = 0; _inspectionRouteIndex < businessInspectionRoutes.size();_inspectionRouteIndex++) {
                    JSONObject businessInspectionRoute = businessInspectionRoutes.getJSONObject(_inspectionRouteIndex);
                    doBusinessInspectionRoute(business, businessInspectionRoute);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("inspectionRouteId", businessInspectionRoute.getString("inspectionRouteId"));
                    }
                }
            }
        }
    }


    /**
     * business to instance 过程
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //巡检路线信息
        List<Map> businessInspectionRouteInfos = inspectionRouteServiceDaoImpl.getBusinessInspectionRouteInfo(info);
        if( businessInspectionRouteInfos != null && businessInspectionRouteInfos.size() >0) {
            for (int _inspectionRouteIndex = 0; _inspectionRouteIndex < businessInspectionRouteInfos.size();_inspectionRouteIndex++) {
                Map businessInspectionRouteInfo = businessInspectionRouteInfos.get(_inspectionRouteIndex);
                flushBusinessInspectionRouteInfo(businessInspectionRouteInfo,StatusConstant.STATUS_CD_VALID);
                inspectionRouteServiceDaoImpl.updateInspectionRouteInfoInstance(businessInspectionRouteInfo);
                if(businessInspectionRouteInfo.size() == 1) {
                    dataFlowContext.addParamOut("inspectionRouteId", businessInspectionRouteInfo.get("inspection_routeId"));
                }
            }
        }

    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //巡检路线信息
        List<Map> inspectionRouteInfo = inspectionRouteServiceDaoImpl.getInspectionRouteInfo(info);
        if(inspectionRouteInfo != null && inspectionRouteInfo.size() > 0){

            //巡检路线信息
            List<Map> businessInspectionRouteInfos = inspectionRouteServiceDaoImpl.getBusinessInspectionRouteInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessInspectionRouteInfos == null || businessInspectionRouteInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（inspectionRoute），程序内部异常,请检查！ "+delInfo);
            }
            for (int _inspectionRouteIndex = 0; _inspectionRouteIndex < businessInspectionRouteInfos.size();_inspectionRouteIndex++) {
                Map businessInspectionRouteInfo = businessInspectionRouteInfos.get(_inspectionRouteIndex);
                flushBusinessInspectionRouteInfo(businessInspectionRouteInfo,StatusConstant.STATUS_CD_VALID);
                inspectionRouteServiceDaoImpl.updateInspectionRouteInfoInstance(businessInspectionRouteInfo);
            }
        }

    }



    /**
     * 处理 businessInspectionRoute 节点
     * @param business 总的数据节点
     * @param businessInspectionRoute 巡检路线节点
     */
    private void doBusinessInspectionRoute(Business business,JSONObject businessInspectionRoute){

        Assert.jsonObjectHaveKey(businessInspectionRoute,"inspectionRouteId","businessInspectionRoute 节点下没有包含 inspectionRouteId 节点");

        if(businessInspectionRoute.getString("inspectionRouteId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"inspectionRouteId 错误，不能自动生成（必须已经存在的inspectionRouteId）"+businessInspectionRoute);
        }
        //自动保存DEL
        autoSaveDelBusinessInspectionRoute(business,businessInspectionRoute);

        businessInspectionRoute.put("bId",business.getbId());
        businessInspectionRoute.put("operate", StatusConstant.OPERATE_ADD);
        //保存巡检路线信息
        inspectionRouteServiceDaoImpl.saveBusinessInspectionRouteInfo(businessInspectionRoute);

    }




    public IInspectionRouteServiceDao getInspectionRouteServiceDaoImpl() {
        return inspectionRouteServiceDaoImpl;
    }

    public void setInspectionRouteServiceDaoImpl(IInspectionRouteServiceDao inspectionRouteServiceDaoImpl) {
        this.inspectionRouteServiceDaoImpl = inspectionRouteServiceDaoImpl;
    }



}
