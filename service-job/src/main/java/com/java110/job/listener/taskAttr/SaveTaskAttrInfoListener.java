package com.java110.job.listener.taskAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.job.dao.ITaskAttrServiceDao;
import com.java110.po.taskAttr.TaskAttrPo;
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
 * 保存 定时任务属性信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveTaskAttrInfoListener")
@Transactional
public class SaveTaskAttrInfoListener extends AbstractTaskAttrBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveTaskAttrInfoListener.class);

    @Autowired
    private ITaskAttrServiceDao taskAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK_ATTR;
    }

    /**
     * 保存定时任务属性信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessTaskAttr 节点
        if (data.containsKey(TaskAttrPo.class.getSimpleName())) {
            Object bObj = data.get(TaskAttrPo.class.getSimpleName());
            JSONArray businessTaskAttrs = null;
            if (bObj instanceof JSONObject) {
                businessTaskAttrs = new JSONArray();
                businessTaskAttrs.add(bObj);
            } else {
                businessTaskAttrs = (JSONArray) bObj;
            }
            //JSONObject businessTaskAttr = data.getJSONObject(TaskAttrPo.class.getSimpleName());
            for (int bTaskAttrIndex = 0; bTaskAttrIndex < businessTaskAttrs.size(); bTaskAttrIndex++) {
                JSONObject businessTaskAttr = businessTaskAttrs.getJSONObject(bTaskAttrIndex);
                doBusinessTaskAttr(business, businessTaskAttr);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("attrId", businessTaskAttr.getString("attrId"));
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

        //定时任务属性信息
        List<Map> businessTaskAttrInfo = taskAttrServiceDaoImpl.getBusinessTaskAttrInfo(info);
        if (businessTaskAttrInfo != null && businessTaskAttrInfo.size() > 0) {
            reFreshShareColumn(info, businessTaskAttrInfo.get(0));
            taskAttrServiceDaoImpl.saveTaskAttrInfoInstance(info);
            if (businessTaskAttrInfo.size() == 1) {
                dataFlowContext.addParamOut("attrId", businessTaskAttrInfo.get(0).get("attr_id"));
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

        if (info.containsKey("task_id")) {
            return;
        }

        if (!businessInfo.containsKey("taskId")) {
            return;
        }

        info.put("task_id", businessInfo.get("taskId"));
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
        //定时任务属性信息
        List<Map> taskAttrInfo = taskAttrServiceDaoImpl.getTaskAttrInfo(info);
        if (taskAttrInfo != null && taskAttrInfo.size() > 0) {
            reFreshShareColumn(paramIn, taskAttrInfo.get(0));
            taskAttrServiceDaoImpl.updateTaskAttrInfoInstance(paramIn);
        }
    }


    /**
     * 处理 businessTaskAttr 节点
     *
     * @param business         总的数据节点
     * @param businessTaskAttr 定时任务属性节点
     */
    private void doBusinessTaskAttr(Business business, JSONObject businessTaskAttr) {

        Assert.jsonObjectHaveKey(businessTaskAttr, "attrId", "businessTaskAttr 节点下没有包含 attrId 节点");

        if (businessTaskAttr.getString("attrId").startsWith("-")) {
            //刷新缓存
            //flushTaskAttrId(business.getDatas());

            businessTaskAttr.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));

        }

        businessTaskAttr.put("bId", business.getbId());
        businessTaskAttr.put("operate", StatusConstant.OPERATE_ADD);
        //保存定时任务属性信息
        taskAttrServiceDaoImpl.saveBusinessTaskAttrInfo(businessTaskAttr);

    }

    @Override
    public ITaskAttrServiceDao getTaskAttrServiceDaoImpl() {
        return taskAttrServiceDaoImpl;
    }

    public void setTaskAttrServiceDaoImpl(ITaskAttrServiceDao taskAttrServiceDaoImpl) {
        this.taskAttrServiceDaoImpl = taskAttrServiceDaoImpl;
    }
}
