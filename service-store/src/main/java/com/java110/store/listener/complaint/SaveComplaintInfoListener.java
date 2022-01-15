package com.java110.store.listener.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.complaint.ComplaintPo;
import com.java110.store.dao.IComplaintServiceDao;
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
 * 保存 投诉建议信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveComplaintInfoListener")
public class SaveComplaintInfoListener extends AbstractComplaintBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveComplaintInfoListener.class);

    @Autowired
    private IComplaintServiceDao complaintServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMPLAINT;
    }

    /**
     * 保存投诉建议信息 business 表中
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessComplaint 节点
        if (data.containsKey(ComplaintPo.class.getSimpleName())) {
            Object bObj = data.get(ComplaintPo.class.getSimpleName());
            JSONArray businessComplaints = null;
            if (bObj instanceof JSONObject) {
                businessComplaints = new JSONArray();
                businessComplaints.add(bObj);
            } else {
                businessComplaints = (JSONArray) bObj;
            }
            //JSONObject businessComplaint = data.getJSONObject("businessComplaint");
            for (int bComplaintIndex = 0; bComplaintIndex < businessComplaints.size(); bComplaintIndex++) {
                JSONObject businessComplaint = businessComplaints.getJSONObject(bComplaintIndex);
                doBusinessComplaint(business, businessComplaint);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("complaintId", businessComplaint.getString("complaintId"));
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

        //投诉建议信息
        List<Map> businessComplaintInfo = complaintServiceDaoImpl.getBusinessComplaintInfo(info);
        if (businessComplaintInfo != null && businessComplaintInfo.size() > 0) {
            reFreshShareColumn(info, businessComplaintInfo.get(0));
            complaintServiceDaoImpl.saveComplaintInfoInstance(info);
            if (businessComplaintInfo.size() == 1) {
                dataFlowContext.addParamOut("complaintId", businessComplaintInfo.get(0).get("complaint_id"));
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

        if (info.containsKey("storeId")) {
            return;
        }

        if (!businessInfo.containsKey("store_id")) {
            return;
        }

        info.put("storeId", businessInfo.get("store_id"));
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
        //投诉建议信息
        List<Map> complaintInfo = complaintServiceDaoImpl.getComplaintInfo(info);
        if (complaintInfo != null && complaintInfo.size() > 0) {
            reFreshShareColumn(paramIn, complaintInfo.get(0));
            complaintServiceDaoImpl.updateComplaintInfoInstance(paramIn);
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
            //刷新缓存
            //flushComplaintId(business.getDatas());

            businessComplaint.put("complaintId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_complaintId));

        }

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
