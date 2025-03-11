package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.machine.MachineTypeDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询小区设备类型树
 */
@Java110Cmd(serviceCode = "community.queryCommunityMachineTypeTree")
public class QueryCommunityMachineTypeTreeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validateAdmin(context);

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<MachineTypeDto> machineTypeDtos = null;

        MachineTypeDto machineTypeDto = new MachineTypeDto();

        machineTypeDtos = reportCommunityInnerServiceSMOImpl.queryCommunityMachineTypeTree(machineTypeDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(machineTypeDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (MachineTypeDto tmpMachineTypeDto : machineTypeDtos) {
            if (!hasInCommunity(tmpMachineTypeDto, communitys)) {
                communityInfo = new JSONObject();
                communityInfo.put("id", "c_" + tmpMachineTypeDto.getCommunityId());
                communityInfo.put("communityId", tmpMachineTypeDto.getCommunityId());
                communityInfo.put("text", tmpMachineTypeDto.getCommunityName());
                communityInfo.put("icon", "/img/org.png");
                communityInfo.put("children", new JSONArray());
                communitys.add(communityInfo);
            }
        }

        if(ListUtil.isNull(communitys)){
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }
        for(int cIndex = 0 ;cIndex < communitys.size();cIndex++){
            communityInfo = communitys.getJSONObject(cIndex);
            findOneMachineType(communityInfo, machineTypeDtos);

        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));
    }

    private void findOneMachineType(JSONObject communityInfo, List<MachineTypeDto> machineTypeDtos) {
        JSONArray childs = communityInfo.getJSONArray("children");
        JSONObject child = null;
        for (MachineTypeDto tmpMachineTypeDto : machineTypeDtos) {
            if(!communityInfo.getString("communityId").equals(tmpMachineTypeDto.getCommunityId())){
                continue;
            }
            if (StringUtil.isEmpty(tmpMachineTypeDto.getParentTypeId())) {
                child = new JSONObject();
                child.put("id", "m_" + tmpMachineTypeDto.getTypeId());
                child.put("typeId", tmpMachineTypeDto.getTypeId());
                child.put("communityId", communityInfo.getString("communityId"));
                child.put("text", tmpMachineTypeDto.getMachineTypeName());
                child.put("icon", "/img/org.png");
                childs.add(child);
            }
        }
        for(int cIndex = 0 ;cIndex < childs.size();cIndex++){
            child = childs.getJSONObject(cIndex);
            findChilds(child, machineTypeDtos);

        }

    }


    private void findChilds(JSONObject parentChild, List<MachineTypeDto> machineTypeDtos) {

        JSONArray childs = new JSONArray();
        JSONObject child = null;
        for (MachineTypeDto machineTypeDto : machineTypeDtos) {
            if (parentChild.getString("typeId").equals(machineTypeDto.getTypeId())) { // 他自己跳过
                continue;
            }
            if(StringUtil.isEmpty(machineTypeDto.getParentTypeId())){
                continue;
            }
            if (machineTypeDto.getParentTypeId().equals(parentChild.getString("typeId"))) {//二级
                child = new JSONObject();
                child.put("id", "m_" + machineTypeDto.getTypeId());
                child.put("typeId", machineTypeDto.getTypeId());
                child.put("communityId", parentChild.getString("communityId"));
                child.put("text", machineTypeDto.getMachineTypeName());
                child.put("icon", "/img/org.png");
                childs.add(child);
            }
        }

        if (ListUtil.isNull(childs)) {
            return;
        }

        parentChild.put("children",childs);

        for(int orgIndex = 0 ;orgIndex < childs.size();orgIndex++){
            child = childs.getJSONObject(orgIndex);
            findChilds(child, machineTypeDtos);

        }
    }


    private boolean hasInCommunity(MachineTypeDto tmpMachineTypeDto, JSONArray communitys) {
        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            if (!community.containsKey("communityId")) {
                continue;
            }
            if (StringUtil.isEmpty(community.getString("communityId"))) {
                continue;
            }
            if (community.getString("communityId").equals(tmpMachineTypeDto.getCommunityId())) {
                return true;
            }
        }
        return false;
    }
}
