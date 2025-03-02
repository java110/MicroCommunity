package com.java110.report.cmd.community;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.dict.DictDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.intf.community.ICommunityV1InnerServiceSMO;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.report.IReportCommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询小区费用项树
 */
@Java110Cmd(serviceCode = "community.queryCommunityFeeTypeTree")
public class QueryCommunityFeeTypeTreeCmd extends Cmd {

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        // must be administrator
        super.validateAdmin(context);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<CommunityDto> communityDtos = null;

        CommunityDto communityDto = new CommunityDto();


        communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);
        JSONArray communitys = new JSONArray();
        if (ListUtil.isNull(communityDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(communitys));
            return;
        }

        JSONObject communityInfo = null;
        for (CommunityDto tmpCommunityDto : communityDtos) {
            communityInfo = new JSONObject();
            communityInfo.put("id", "c_" + tmpCommunityDto.getCommunityId());
            communityInfo.put("communityId", tmpCommunityDto.getCommunityId());
            communityInfo.put("text", tmpCommunityDto.getName());
            communityInfo.put("icon", "/img/org.png");
            communityInfo.put("children", new JSONArray());
            communitys.add(communityInfo);

        }

        DictDto dictDto = new DictDto();
        dictDto.setTableName("pay_fee_config");
        dictDto.setTableColumns("fee_type_cd");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        JSONObject community = null;
        for (int cIndex = 0; cIndex < communitys.size(); cIndex++) {
            community = communitys.getJSONObject(cIndex);
            // find floor data in unitDtos
            findFeeTypeCd(community, dictDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(communitys));


    }

    /**
     * find community floor data
     *
     * @param community       current community
     */
    private void findFeeTypeCd(JSONObject community, List<DictDto> dictDtos) {
        JSONArray feeTypeCds = community.getJSONArray("children");
        JSONObject feeTypeCd = null;
        for (DictDto tmpDictDto : dictDtos) {
            feeTypeCd = new JSONObject();
            feeTypeCd.put("id", "f_" + GenerateCodeFactory.getUUID());
            feeTypeCd.put("feeTypeCd", tmpDictDto.getStatusCd());
            feeTypeCd.put("communityId", community.getString("communityId"));
            feeTypeCd.put("text", tmpDictDto.getName());
            feeTypeCd.put("icon", "/img/floor.png");
            feeTypeCd.put("children", new JSONArray());
            feeTypeCds.add(feeTypeCd);
        }

        JSONObject feeFlag = null;
        JSONArray feeFlags = null;
        for (int cIndex = 0; cIndex < feeTypeCds.size(); cIndex++) {
            feeTypeCd = feeTypeCds.getJSONObject(cIndex);
            feeFlags = feeTypeCd.getJSONArray("children");
            // find floor data in unitDtos
            feeFlag = new JSONObject();
            feeFlag.put("id", "l_"+GenerateCodeFactory.getUUID());
            feeFlag.put("feeFlag", "1003006");
            feeFlag.put("communityId", community.getString("communityId"));
            feeFlag.put("feeTypeCd", feeTypeCd.getString("feeTypeCd"));
            feeFlag.put("text", "周期性费用");
            feeFlag.put("icon", "/img/unit.png");
            feeFlags.add(feeFlag);

            feeFlag = new JSONObject();
            feeFlag.put("id", "l_"+GenerateCodeFactory.getUUID());
            feeFlag.put("feeFlag", "2006012");
            feeFlag.put("communityId", community.getString("communityId"));
            feeFlag.put("feeTypeCd", feeTypeCd.getString("feeTypeCd"));
            feeFlag.put("text", "一次性费用");
            feeFlag.put("icon", "/img/unit.png");
            feeFlags.add(feeFlag);
        }
    }



}
