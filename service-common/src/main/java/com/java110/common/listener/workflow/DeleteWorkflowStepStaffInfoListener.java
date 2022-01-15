package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowStepStaffServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.workflow.WorkflowStepStaffPo;
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
 * 删除工作流节点信息 侦听
 * <p>
 * 处理节点
 * 1、businessWorkflowStepStaff:{} 工作流节点基本信息节点
 * 2、businessWorkflowStepStaffAttr:[{}] 工作流节点属性信息节点
 * 3、businessWorkflowStepStaffPhoto:[{}] 工作流节点照片信息节点
 * 4、businessWorkflowStepStaffCerdentials:[{}] 工作流节点证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteWorkflowStepStaffInfoListener")
@Transactional
public class DeleteWorkflowStepStaffInfoListener extends AbstractWorkflowStepStaffBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteWorkflowStepStaffInfoListener.class);
    @Autowired
    IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW_STEP_STAFF;
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

        //处理 businessWorkflowStepStaff 节点
        if (data.containsKey(WorkflowStepStaffPo.class.getSimpleName())) {
            Object _obj = data.get(WorkflowStepStaffPo.class.getSimpleName());
            JSONArray businessWorkflowStepStaffs = null;
            if (_obj instanceof JSONObject) {
                businessWorkflowStepStaffs = new JSONArray();
                businessWorkflowStepStaffs.add(_obj);
            } else {
                businessWorkflowStepStaffs = (JSONArray) _obj;
            }
            //JSONObject businessWorkflowStepStaff = data.getJSONObject(WorkflowStepStaffPo.class.getSimpleName());
            for (int _workflowStepStaffIndex = 0; _workflowStepStaffIndex < businessWorkflowStepStaffs.size(); _workflowStepStaffIndex++) {
                JSONObject businessWorkflowStepStaff = businessWorkflowStepStaffs.getJSONObject(_workflowStepStaffIndex);
                doBusinessWorkflowStepStaff(business, businessWorkflowStepStaff);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("wssId", businessWorkflowStepStaff.getString("wssId"));
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

        //工作流节点信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //工作流节点信息
        List<Map> businessWorkflowStepStaffInfos = workflowStepStaffServiceDaoImpl.getBusinessWorkflowStepStaffInfo(info);
        if (businessWorkflowStepStaffInfos != null && businessWorkflowStepStaffInfos.size() > 0) {
            for (int _workflowStepStaffIndex = 0; _workflowStepStaffIndex < businessWorkflowStepStaffInfos.size(); _workflowStepStaffIndex++) {
                Map businessWorkflowStepStaffInfo = businessWorkflowStepStaffInfos.get(_workflowStepStaffIndex);
                flushBusinessWorkflowStepStaffInfo(businessWorkflowStepStaffInfo, StatusConstant.STATUS_CD_INVALID);
                workflowStepStaffServiceDaoImpl.updateWorkflowStepStaffInfoInstance(businessWorkflowStepStaffInfo);
                dataFlowContext.addParamOut("wssId", businessWorkflowStepStaffInfo.get("wss_id"));
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
        //工作流节点信息
        List<Map> workflowStepStaffInfo = workflowStepStaffServiceDaoImpl.getWorkflowStepStaffInfo(info);
        if (workflowStepStaffInfo != null && workflowStepStaffInfo.size() > 0) {

            //工作流节点信息
            List<Map> businessWorkflowStepStaffInfos = workflowStepStaffServiceDaoImpl.getBusinessWorkflowStepStaffInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessWorkflowStepStaffInfos == null || businessWorkflowStepStaffInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（workflowStepStaff），程序内部异常,请检查！ " + delInfo);
            }
            for (int _workflowStepStaffIndex = 0; _workflowStepStaffIndex < businessWorkflowStepStaffInfos.size(); _workflowStepStaffIndex++) {
                Map businessWorkflowStepStaffInfo = businessWorkflowStepStaffInfos.get(_workflowStepStaffIndex);
                flushBusinessWorkflowStepStaffInfo(businessWorkflowStepStaffInfo, StatusConstant.STATUS_CD_VALID);
                workflowStepStaffServiceDaoImpl.updateWorkflowStepStaffInfoInstance(businessWorkflowStepStaffInfo);
            }
        }
    }


    /**
     * 处理 businessWorkflowStepStaff 节点
     *
     * @param business                  总的数据节点
     * @param businessWorkflowStepStaff 工作流节点节点
     */
    private void doBusinessWorkflowStepStaff(Business business, JSONObject businessWorkflowStepStaff) {

        Assert.jsonObjectHaveKey(businessWorkflowStepStaff, "wssId", "businessWorkflowStepStaff 节点下没有包含 wssId 节点");

        if (businessWorkflowStepStaff.getString("wssId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "wssId 错误，不能自动生成（必须已经存在的wssId）" + businessWorkflowStepStaff);
        }
        //自动插入DEL
        autoSaveDelBusinessWorkflowStepStaff(business, businessWorkflowStepStaff);
    }

    @Override
    public IWorkflowStepStaffServiceDao getWorkflowStepStaffServiceDaoImpl() {
        return workflowStepStaffServiceDaoImpl;
    }

    public void setWorkflowStepStaffServiceDaoImpl(IWorkflowStepStaffServiceDao workflowStepStaffServiceDaoImpl) {
        this.workflowStepStaffServiceDaoImpl = workflowStepStaffServiceDaoImpl;
    }
}
