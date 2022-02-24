package com.java110.store.listener.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.complaint.ComplaintPo;
import com.java110.store.dao.IComplaintServiceDao;
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
 * 修改投诉建议信息 侦听
 * <p>
 * 处理节点
 * 1、businessComplaint:{} 投诉建议基本信息节点
 * 2、businessComplaintAttr:[{}] 投诉建议属性信息节点
 * 3、businessComplaintPhoto:[{}] 投诉建议照片信息节点
 * 4、businessComplaintCerdentials:[{}] 投诉建议证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateComplaintInfoListener")
@Transactional
public class UpdateComplaintInfoListener extends AbstractComplaintBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateComplaintInfoListener.class);
    @Autowired
    private IComplaintServiceDao complaintServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMPLAINT;
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


        //处理 businessComplaint 节点
        if (data.containsKey(ComplaintPo.class.getSimpleName())) {
            Object _obj = data.get(ComplaintPo.class.getSimpleName());
            JSONArray businessComplaints = null;
            if (_obj instanceof JSONObject) {
                businessComplaints = new JSONArray();
                businessComplaints.add(_obj);
            } else {
                businessComplaints = (JSONArray) _obj;
            }
            //JSONObject businessComplaint = data.getJSONObject("businessComplaint");
            for (int _complaintIndex = 0; _complaintIndex < businessComplaints.size(); _complaintIndex++) {
                JSONObject businessComplaint = businessComplaints.getJSONObject(_complaintIndex);
                doBusinessComplaint(business, businessComplaint);
                if (_obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("complaintId", businessComplaint.getString("complaintId"));
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

        //投诉建议信息
        List<Map> businessComplaintInfos = complaintServiceDaoImpl.getBusinessComplaintInfo(info);
        if (businessComplaintInfos != null && businessComplaintInfos.size() > 0) {
            for (int _complaintIndex = 0; _complaintIndex < businessComplaintInfos.size(); _complaintIndex++) {
                Map businessComplaintInfo = businessComplaintInfos.get(_complaintIndex);
                flushBusinessComplaintInfo(businessComplaintInfo, StatusConstant.STATUS_CD_VALID);
                complaintServiceDaoImpl.updateComplaintInfoInstance(businessComplaintInfo);
                if (businessComplaintInfo.size() == 1) {
                    dataFlowContext.addParamOut("complaintId", businessComplaintInfo.get("complaint_id"));
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
        //投诉建议信息
        List<Map> complaintInfo = complaintServiceDaoImpl.getComplaintInfo(info);
        if (complaintInfo != null && complaintInfo.size() > 0) {

            //投诉建议信息
            List<Map> businessComplaintInfos = complaintServiceDaoImpl.getBusinessComplaintInfo(delInfo);
            //除非程序出错了，这里不会为空
            if (businessComplaintInfos == null || businessComplaintInfos.size() == 0) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（complaint），程序内部异常,请检查！ " + delInfo);
            }
            for (int _complaintIndex = 0; _complaintIndex < businessComplaintInfos.size(); _complaintIndex++) {
                Map businessComplaintInfo = businessComplaintInfos.get(_complaintIndex);
                flushBusinessComplaintInfo(businessComplaintInfo, StatusConstant.STATUS_CD_VALID);
                complaintServiceDaoImpl.updateComplaintInfoInstance(businessComplaintInfo);
            }
        }

    }


    /**
     * 处理 businessComplaint 节点
     *
     * @param business          总的数据节点
     * @param businessComplaint 投诉建议节点
     */
    private void doBusinessComplaint(Business business, JSONObject businessComplaint) {

        Assert.jsonObjectHaveKey(businessComplaint, "complaintId", "businessComplaint 节点下没有包含 complaintId 节点");

        if (businessComplaint.getString("complaintId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "complaintId 错误，不能自动生成（必须已经存在的complaintId）" + businessComplaint);
        }
        //自动保存DEL
        autoSaveDelBusinessComplaint(business, businessComplaint);

        businessComplaint.put("bId", business.getbId());
        businessComplaint.put("operate", StatusConstant.OPERATE_ADD);
        //保存投诉建议信息
        complaintServiceDaoImpl.saveBusinessComplaintInfo(businessComplaint);

    }


    public IComplaintServiceDao getComplaintServiceDaoImpl() {
        return complaintServiceDaoImpl;
    }

    public void setComplaintServiceDaoImpl(IComplaintServiceDao complaintServiceDaoImpl) {
        this.complaintServiceDaoImpl = complaintServiceDaoImpl;
    }


}
