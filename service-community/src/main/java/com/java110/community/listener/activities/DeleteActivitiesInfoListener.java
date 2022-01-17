package com.java110.community.listener.activities;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IActivitiesServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.activities.ActivitiesPo;
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
 * 删除活动信息 侦听
 * <p>
 * 处理节点
 * 1、businessActivities:{} 活动基本信息节点
 * 2、businessActivitiesAttr:[{}] 活动属性信息节点
 * 3、businessActivitiesPhoto:[{}] 活动照片信息节点
 * 4、businessActivitiesCerdentials:[{}] 活动证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteActivitiesInfoListener")
@Transactional
public class DeleteActivitiesInfoListener extends AbstractActivitiesBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteActivitiesInfoListener.class);
    @Autowired
    IActivitiesServiceDao activitiesServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_ACTIVITIES;
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


        //处理 businessActivities 节点
        if (data.containsKey(ActivitiesPo.class.getSimpleName())) {
            Object _obj = data.get(ActivitiesPo.class.getSimpleName());
            JSONArray businessActivitiess = null;
            if (_obj instanceof JSONObject) {
                businessActivitiess = new JSONArray();
                businessActivitiess.add(_obj);
            } else {
                businessActivitiess = (JSONArray) _obj;
            }
            //JSONObject businessActivities = data.getJSONObject("businessActivities");
            for (int _activitiesIndex = 0; _activitiesIndex < businessActivitiess.size(); _activitiesIndex++) {
                JSONObject businessActivities = businessActivitiess.getJSONObject(_activitiesIndex);
                doBusinessActivities(business, businessActivities);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("activitiesId", businessActivities.getString("activitiesId"));
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

        //活动信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //活动信息
        List<Map> businessActivitiesInfos = activitiesServiceDaoImpl.getBusinessActivitiesInfo(info);
        if (businessActivitiesInfos != null && businessActivitiesInfos.size() > 0) {
            for (int _activitiesIndex = 0; _activitiesIndex < businessActivitiesInfos.size(); _activitiesIndex++) {
                Map businessActivitiesInfo = businessActivitiesInfos.get(_activitiesIndex);
                flushBusinessActivitiesInfo(businessActivitiesInfo, StatusConstant.STATUS_CD_INVALID);
                activitiesServiceDaoImpl.updateActivitiesInfoInstance(businessActivitiesInfo);
                dataFlowContext.addParamOut("activitiesId", businessActivitiesInfo.get("activities_id"));
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
        //活动信息
        List<Map> activitiesInfo = activitiesServiceDaoImpl.getActivitiesInfo(info);
        if (activitiesInfo != null && activitiesInfo.size() > 0) {

            //活动信息
            List<Map> businessActivitiesInfos = activitiesServiceDaoImpl.getBusinessActivitiesInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessActivitiesInfos == null || businessActivitiesInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（activities），程序内部异常,请检查！ " + delInfo);
            }
            for (int _activitiesIndex = 0; _activitiesIndex < businessActivitiesInfos.size(); _activitiesIndex++) {
                Map businessActivitiesInfo = businessActivitiesInfos.get(_activitiesIndex);
                flushBusinessActivitiesInfo(businessActivitiesInfo, StatusConstant.STATUS_CD_VALID);
                activitiesServiceDaoImpl.updateActivitiesInfoInstance(businessActivitiesInfo);
            }
        }
    }


    /**
     * 处理 businessActivities 节点
     *
     * @param business           总的数据节点
     * @param businessActivities 活动节点
     */
    private void doBusinessActivities(Business business, JSONObject businessActivities) {

        Assert.jsonObjectHaveKey(businessActivities, "activitiesId", "businessActivities 节点下没有包含 activitiesId 节点");

        if (businessActivities.getString("activitiesId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "activitiesId 错误，不能自动生成（必须已经存在的activitiesId）" + businessActivities);
        }
        //自动插入DEL
        autoSaveDelBusinessActivities(business, businessActivities);
    }

    public IActivitiesServiceDao getActivitiesServiceDaoImpl() {
        return activitiesServiceDaoImpl;
    }

    public void setActivitiesServiceDaoImpl(IActivitiesServiceDao activitiesServiceDaoImpl) {
        this.activitiesServiceDaoImpl = activitiesServiceDaoImpl;
    }
}
