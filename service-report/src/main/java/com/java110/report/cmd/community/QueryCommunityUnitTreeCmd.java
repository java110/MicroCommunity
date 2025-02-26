package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.room.RoomDto;
import com.java110.dto.unit.UnitDto;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询小区单元树形结构
 */
@Java110Cmd(serviceCode = "community.queryCommunityUnitTree")
public class QueryCommunityUnitTreeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // must be administrator
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<UnitDto> unitDtos = null;

        UnitDto unitDto = new UnitDto();


        unitDtos = reportCommunityInnerServiceSMOImpl.queryCommunityUnitTree(unitDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(unitDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for(UnitDto tmpUnitDto:unitDtos){
            if(!hasInCommunitys(tmpUnitDto,communitys)){
                communityInfo = new JSONObject();
                communityInfo.put("id","c_"+tmpUnitDto.getCommunityId());
                communityInfo.put("communityId",tmpUnitDto.getCommunityId());
                communityInfo.put("text",tmpUnitDto.getCommunityName());
                communityInfo.put("icon","/img/org.png");
                communityInfo.put("children",new JSONArray());
                communitys.add(communityInfo);
            }
        }

    }

    private boolean hasInCommunitys(UnitDto tmpUnitDto,JSONArray communitys) {
        return true;
    }


}
