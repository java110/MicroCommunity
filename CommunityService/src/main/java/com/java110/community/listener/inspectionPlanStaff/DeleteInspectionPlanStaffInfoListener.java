package com.java110.community.listener.inspectionPlanStaff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInspectionPlanStaffServiceDao;
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
 * 删除执行计划人信息 侦听
 * <p>
 * 处理节点
 * 1、businessInspectionPlanStaff:{} 执行计划人基本信息节点
 * 2、businessInspectionPlanStaffAttr:[{}] 执行计划人属性信息节点
 * 3、businessInspectionPlanStaffPhoto:[{}] 执行计划人照片信息节点
 * 4、businessInspectionPlanStaffCerdentials:[{}] 执行计划人证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteInspectionPlanStaffInfoListener")
@Transactional
public class DeleteInspectionPlanStaffInfoListener extends AbstractInspectionPlanStaffBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteInspectionPlanStaffInfoListener.class);
    @Autowired
    IInspectionPlanStaffServiceDao inspectionPlanStaffServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PLAN_STAFF;
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

        //处理 businessInspectionPlanStaff 节点
        if (data.containsKey(BusinessTypeConstant.BUSINESS_TYPE_DELETE_PLAN_STAFF)) {
            Object _obj = data.get(BusinessTypeConstant.BUSINESS_TYPE_DELETE_PLAN_STAFF);
            JSONArray businessInspectionPlanStaffs = null;
            if (_obj instanceof JSONObject) {
                businessInspectionPlanStaffs = new JSONArray();
                businessInspectionPlanStaffs.add(_obj);
            } else {
                businessInspectionPlanStaffs = (JSONArray) _obj;
            }
            //JSONObject businessInspectionPlanStaff = data.getJSONObject("businessInspectionPlanStaff");
            for (int _inspectionPlanStaffIndex = 0; _inspectionPlanStaffIndex < businessInspectionPlanStaffs.size(); _inspectionPlanStaffIndex++) {
                JSONObject businessInspectionPlanStaff = businessInspectionPlanStaffs.getJSONObject(_inspectionPlanStaffIndex);
                doBusinessInspectionPlanStaff(business, businessInspectionPlanStaff);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("ipStaffId", businessInspectionPlanStaff.getString("ipStaffId"));
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

        //执行计划人信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //执行计划人信息
        List<Map> businessInspectionPlanStaffInfos = inspectionPlanStaffServiceDaoImpl.getBusinessInspectionPlanStaffInfo(info);
        if (businessInspectionPlanStaffInfos != null && businessInspectionPlanStaffInfos.size() > 0) {
            for (int _inspectionPlanStaffIndex = 0; _inspectionPlanStaffIndex < businessInspectionPlanStaffInfos.size(); _inspectionPlanStaffIndex++) {
                Map businessInspectionPlanStaffInfo = businessInspectionPlanStaffInfos.get(_inspectionPlanStaffIndex);
                flushBusinessInspectionPlanStaffInfo(businessInspectionPlanStaffInfo, StatusConstant.STATUS_CD_INVALID);
                inspectionPlanStaffServiceDaoImpl.updateInspectionPlanStaffInfoInstance(businessInspectionPlanStaffInfo);
                dataFlowContext.addParamOut("ipStaffId", businessInspectionPlanStaffInfo.get("ip_staff_id"));
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
        //执行计划人信息
        List<Map> inspectionPlanStaffInfo = inspectionPlanStaffServiceDaoImpl.getInspectionPlanStaffInfo(info);
        if (inspectionPlanStaffInfo != null && inspectionPlanStaffInfo.size() > 0) {

            //执行计划人信息
            List<Map> businessInspectionPlanStaffInfos = inspectionPlanStaffServiceDaoImpl.getBusinessInspectionPlanStaffInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessInspectionPlanStaffInfos == null || businessInspectionPlanStaffInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（inspectionPlanStaff），程序内部异常,请检查！ " + delInfo);
            }
            for (int _inspectionPlanStaffIndex = 0; _inspectionPlanStaffIndex < businessInspectionPlanStaffInfos.size(); _inspectionPlanStaffIndex++) {
                Map businessInspectionPlanStaffInfo = businessInspectionPlanStaffInfos.get(_inspectionPlanStaffIndex);
                flushBusinessInspectionPlanStaffInfo(businessInspectionPlanStaffInfo, StatusConstant.STATUS_CD_VALID);
                inspectionPlanStaffServiceDaoImpl.updateInspectionPlanStaffInfoInstance(businessInspectionPlanStaffInfo);
            }
        }
    }


    /**
     * 处理 businessInspectionPlanStaff 节点
     *
     * @param business                    总的数据节点
     * @param businessInspectionPlanStaff 执行计划人节点
     */
    private void doBusinessInspectionPlanStaff(Business business, JSONObject businessInspectionPlanStaff) {

        Assert.jsonObjectHaveKey(businessInspectionPlanStaff, "ipStaffId", "businessInspectionPlanStaff 节点下没有包含 ipStaffId 节点");

        if (businessInspectionPlanStaff.getString("ipStaffId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "ipStaffId 错误，不能自动生成（必须已经存在的ipStaffId）" + businessInspectionPlanStaff);
        }
        //自动插入DEL
        autoSaveDelBusinessInspectionPlanStaff(business, businessInspectionPlanStaff);
    }

    public IInspectionPlanStaffServiceDao getInspectionPlanStaffServiceDaoImpl() {
        return inspectionPlanStaffServiceDaoImpl;
    }

    public void setInspectionPlanStaffServiceDaoImpl(IInspectionPlanStaffServiceDao inspectionPlanStaffServiceDaoImpl) {
        this.inspectionPlanStaffServiceDaoImpl = inspectionPlanStaffServiceDaoImpl;
    }
}
