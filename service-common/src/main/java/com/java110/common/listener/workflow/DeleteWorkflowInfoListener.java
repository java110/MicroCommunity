package com.java110.common.listener.workflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IWorkflowServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.workflow.WorkflowPo;
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
 * 删除工作流信息 侦听
 * <p>
 * 处理节点
 * 1、businessWorkflow:{} 工作流基本信息节点
 * 2、businessWorkflowAttr:[{}] 工作流属性信息节点
 * 3、businessWorkflowPhoto:[{}] 工作流照片信息节点
 * 4、businessWorkflowCerdentials:[{}] 工作流证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteWorkflowInfoListener")
@Transactional
public class DeleteWorkflowInfoListener extends AbstractWorkflowBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteWorkflowInfoListener.class);
    @Autowired
    IWorkflowServiceDao workflowServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_WORKFLOW;
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

        //处理 businessWorkflow 节点
        if (data.containsKey(WorkflowPo.class.getSimpleName())) {
            Object _obj = data.get(WorkflowPo.class.getSimpleName());
            JSONArray businessWorkflows = null;
            if (_obj instanceof JSONObject) {
                businessWorkflows = new JSONArray();
                businessWorkflows.add(_obj);
            } else {
                businessWorkflows = (JSONArray) _obj;
            }
            //JSONObject businessWorkflow = data.getJSONObject(WorkflowPo.class.getSimpleName());
            for (int _workflowIndex = 0; _workflowIndex < businessWorkflows.size(); _workflowIndex++) {
                JSONObject businessWorkflow = businessWorkflows.getJSONObject(_workflowIndex);
                doBusinessWorkflow(business, businessWorkflow);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("flowId", businessWorkflow.getString("flowId"));
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

        //工作流信息
        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        //工作流信息
        List<Map> businessWorkflowInfos = workflowServiceDaoImpl.getBusinessWorkflowInfo(info);
        if (businessWorkflowInfos != null && businessWorkflowInfos.size() > 0) {
            for (int _workflowIndex = 0; _workflowIndex < businessWorkflowInfos.size(); _workflowIndex++) {
                Map businessWorkflowInfo = businessWorkflowInfos.get(_workflowIndex);
                flushBusinessWorkflowInfo(businessWorkflowInfo, StatusConstant.STATUS_CD_INVALID);
                workflowServiceDaoImpl.updateWorkflowInfoInstance(businessWorkflowInfo);
                dataFlowContext.addParamOut("flowId", businessWorkflowInfo.get("flow_id"));
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
        //工作流信息
        List<Map> workflowInfo = workflowServiceDaoImpl.getWorkflowInfo(info);
        if (workflowInfo != null && workflowInfo.size() > 0) {

            //工作流信息
            List<Map> businessWorkflowInfos = workflowServiceDaoImpl.getBusinessWorkflowInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessWorkflowInfos == null || businessWorkflowInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（workflow），程序内部异常,请检查！ " + delInfo);
            }
            for (int _workflowIndex = 0; _workflowIndex < businessWorkflowInfos.size(); _workflowIndex++) {
                Map businessWorkflowInfo = businessWorkflowInfos.get(_workflowIndex);
                flushBusinessWorkflowInfo(businessWorkflowInfo, StatusConstant.STATUS_CD_VALID);
                workflowServiceDaoImpl.updateWorkflowInfoInstance(businessWorkflowInfo);
            }
        }
    }


    /**
     * 处理 businessWorkflow 节点
     *
     * @param business         总的数据节点
     * @param businessWorkflow 工作流节点
     */
    private void doBusinessWorkflow(Business business, JSONObject businessWorkflow) {

        Assert.jsonObjectHaveKey(businessWorkflow, "flowId", "businessWorkflow 节点下没有包含 flowId 节点");

        if (businessWorkflow.getString("flowId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "flowId 错误，不能自动生成（必须已经存在的flowId）" + businessWorkflow);
        }
        //自动插入DEL
        autoSaveDelBusinessWorkflow(business, businessWorkflow);
    }

    @Override
    public IWorkflowServiceDao getWorkflowServiceDaoImpl() {
        return workflowServiceDaoImpl;
    }

    public void setWorkflowServiceDaoImpl(IWorkflowServiceDao workflowServiceDaoImpl) {
        this.workflowServiceDaoImpl = workflowServiceDaoImpl;
    }
}
