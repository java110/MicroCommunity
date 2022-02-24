package com.java110.job.listener.taskAttr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.po.taskAttr.TaskAttrPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.job.dao.ITaskAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除定时任务属性信息 侦听
 *
 * 处理节点
 * 1、businessTaskAttr:{} 定时任务属性基本信息节点
 * 2、businessTaskAttrAttr:[{}] 定时任务属性属性信息节点
 * 3、businessTaskAttrPhoto:[{}] 定时任务属性照片信息节点
 * 4、businessTaskAttrCerdentials:[{}] 定时任务属性证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteTaskAttrInfoListener")
@Transactional
public class DeleteTaskAttrInfoListener extends AbstractTaskAttrBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteTaskAttrInfoListener.class);
    @Autowired
    ITaskAttrServiceDao taskAttrServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_TASK_ATTR;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

            //处理 businessTaskAttr 节点
            if(data.containsKey(TaskAttrPo.class.getSimpleName())){
                Object _obj = data.get(TaskAttrPo.class.getSimpleName());
                JSONArray businessTaskAttrs = null;
                if(_obj instanceof JSONObject){
                    businessTaskAttrs = new JSONArray();
                    businessTaskAttrs.add(_obj);
                }else {
                    businessTaskAttrs = (JSONArray)_obj;
                }
                //JSONObject businessTaskAttr = data.getJSONObject(TaskAttrPo.class.getSimpleName());
                for (int _taskAttrIndex = 0; _taskAttrIndex < businessTaskAttrs.size();_taskAttrIndex++) {
                    JSONObject businessTaskAttr = businessTaskAttrs.getJSONObject(_taskAttrIndex);
                    doBusinessTaskAttr(business, businessTaskAttr);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("attrId", businessTaskAttr.getString("attrId"));
                    }
                }

        }


    }

    /**
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //定时任务属性信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //定时任务属性信息
        List<Map> businessTaskAttrInfos = taskAttrServiceDaoImpl.getBusinessTaskAttrInfo(info);
        if( businessTaskAttrInfos != null && businessTaskAttrInfos.size() >0) {
            for (int _taskAttrIndex = 0; _taskAttrIndex < businessTaskAttrInfos.size();_taskAttrIndex++) {
                Map businessTaskAttrInfo = businessTaskAttrInfos.get(_taskAttrIndex);
                flushBusinessTaskAttrInfo(businessTaskAttrInfo,StatusConstant.STATUS_CD_INVALID);
                taskAttrServiceDaoImpl.updateTaskAttrInfoInstance(businessTaskAttrInfo);
                dataFlowContext.addParamOut("attrId",businessTaskAttrInfo.get("attr_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //定时任务属性信息
        List<Map> taskAttrInfo = taskAttrServiceDaoImpl.getTaskAttrInfo(info);
        if(taskAttrInfo != null && taskAttrInfo.size() > 0){

            //定时任务属性信息
            List<Map> businessTaskAttrInfos = taskAttrServiceDaoImpl.getBusinessTaskAttrInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessTaskAttrInfos == null ||  businessTaskAttrInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（taskAttr），程序内部异常,请检查！ "+delInfo);
            }
            for (int _taskAttrIndex = 0; _taskAttrIndex < businessTaskAttrInfos.size();_taskAttrIndex++) {
                Map businessTaskAttrInfo = businessTaskAttrInfos.get(_taskAttrIndex);
                flushBusinessTaskAttrInfo(businessTaskAttrInfo,StatusConstant.STATUS_CD_VALID);
                taskAttrServiceDaoImpl.updateTaskAttrInfoInstance(businessTaskAttrInfo);
            }
        }
    }



    /**
     * 处理 businessTaskAttr 节点
     * @param business 总的数据节点
     * @param businessTaskAttr 定时任务属性节点
     */
    private void doBusinessTaskAttr(Business business,JSONObject businessTaskAttr){

        Assert.jsonObjectHaveKey(businessTaskAttr,"attrId","businessTaskAttr 节点下没有包含 attrId 节点");

        if(businessTaskAttr.getString("attrId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"attrId 错误，不能自动生成（必须已经存在的attrId）"+businessTaskAttr);
        }
        //自动插入DEL
        autoSaveDelBusinessTaskAttr(business,businessTaskAttr);
    }
    @Override
    public ITaskAttrServiceDao getTaskAttrServiceDaoImpl() {
        return taskAttrServiceDaoImpl;
    }

    public void setTaskAttrServiceDaoImpl(ITaskAttrServiceDao taskAttrServiceDaoImpl) {
        this.taskAttrServiceDaoImpl = taskAttrServiceDaoImpl;
    }
}
