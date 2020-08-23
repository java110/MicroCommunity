package com.java110.api.listener.index;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.repair.RepairDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.common.IResourceEntryStoreInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

/**
 * @ClassName FloorDto
 * @Description 查询首页统计信息
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryIndexTodoTaskListener")
public class QueryIndexTodoTaskListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IResourceEntryStoreInnerServiceSMO resourceEntryStoreInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_INDEX_TODO_TASK;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        AuditUser auditUser = new AuditUser();
        auditUser.setStoreId(reqJson.getString("storeId"));
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setCommunityId(reqJson.getString("communityId"));
        //投诉待办
        long complaintCount = complaintUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        //投诉已办
        long complaintHisCount = complaintUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);


        //报修 待办
        RepairDto ownerRepairDto = new RepairDto();
        ownerRepairDto.setStaffId(reqJson.getString("userId"));
        ownerRepairDto.setCommunityId(reqJson.getString("communityId"));
        int repairCount = repairInnerServiceSMOImpl.queryStaffRepairsCount(ownerRepairDto);

        //报修已办
        int repairHisCount = repairInnerServiceSMOImpl.queryStaffFinishRepairsCount(ownerRepairDto);

        // 采购待办

        long purchaseCount = resourceEntryStoreInnerServiceSMOImpl.getUserTaskCount(auditUser);

        //采购已办
        long purchaseHisCount = resourceEntryStoreInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        //物品领用待办

        long collectionCount = goodCollectionUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        long collectionHisCount = goodCollectionUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);


        JSONObject paramOut = new JSONObject();
        paramOut.put("complaintCount", complaintCount);
        paramOut.put("complaintHisCount", complaintHisCount);
        paramOut.put("repairCount", repairCount);
        paramOut.put("repairHisCount", repairHisCount);
        paramOut.put("purchaseCount", purchaseCount);
        paramOut.put("purchaseHisCount", purchaseHisCount);
        paramOut.put("collectionCount", collectionCount);
        paramOut.put("collectionHisCount", collectionHisCount);
        dataFlowContext.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }

    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateIndexStatistic(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }


    public IParkingSpaceInnerServiceSMO getParkingSpaceInnerServiceSMOImpl() {
        return parkingSpaceInnerServiceSMOImpl;
    }

    public void setParkingSpaceInnerServiceSMOImpl(IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl) {
        this.parkingSpaceInnerServiceSMOImpl = parkingSpaceInnerServiceSMOImpl;
    }
}
