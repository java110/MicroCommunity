package com.java110.community.cmd.index;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.privilege.BasePrivilegeDto;
import com.java110.dto.repair.RepairDto;
import com.java110.dto.audit.AuditUser;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.common.IResourceEntryStoreInnerServiceSMO;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.community.IParkingSpaceInnerServiceSMO;
import com.java110.intf.community.IRepairInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "index.queryIndexTodoTask")
public class QueryIndexTodoTaskCmd extends Cmd {

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

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
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
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewListStaffRepairs");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges.size()==0) {
            ownerRepairDto.setStaffId(reqJson.getString("userId"));
        }
        ownerRepairDto.setCommunityId(reqJson.getString("communityId"));
        int repairCount = repairInnerServiceSMOImpl.queryStaffRepairsCount(ownerRepairDto);

        //报修已办
        basePrivilegeDto.setResource("/listStaffFinishRepairs");
        List<Map> privileges2 = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges2.size()==0) {
            ownerRepairDto.setStaffId(reqJson.getString("userId"));
        }else{
            ownerRepairDto.setStaffId("");
        }
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
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
