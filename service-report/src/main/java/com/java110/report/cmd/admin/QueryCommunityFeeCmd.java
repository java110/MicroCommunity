package com.java110.report.cmd.admin;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.CmdContextUtils;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.report.statistics.IBaseDataStatistics;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "admin.queryCommunityFee")
public class QueryCommunityFeeCmd extends Cmd {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IBaseDataStatistics baseDataStatisticsImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_ADMIN);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统管理员账户操作");
        }

        if (!reqJson.containsKey("startTime")) {
            String startTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B);
            String endTime = startTime + " 23:59:59";
            reqJson.put("startTime", startTime);
            reqJson.put("endTime", endTime);
        }

        String startTime = reqJson.getString("startTime");
        if (StringUtil.isEmpty(startTime)) {
            startTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B);
            String endTime = startTime + " 23:59:59";
            reqJson.put("startTime", startTime);
            reqJson.put("endTime", endTime);
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        CommunityDto communityDto = new CommunityDto();
        communityDto.setPage(1);
        communityDto.setRow(100);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        List<Map> datas = null;
        if (communityDtos == null || communityDtos.isEmpty()) {
            datas = new ArrayList<>();
            Map data = new HashMap();
            data.put("communityId", "-1");
            data.put("communityName", "未添加小区");
            data.put("count", 0);
            data.put("receivedAmount", 0);

            datas.add(data);
            context.setResponseEntity(ResultVo.createResponseEntity(datas));
            return;
        }

        List<String> communityIds = new ArrayList<>();
        for (CommunityDto tmpCommunityDto : communityDtos) {
            communityIds.add(tmpCommunityDto.getCommunityId());
        }

        reqJson.put("communityIds", communityIds.toArray(new String[communityIds.size()]));
        datas = baseDataStatisticsImpl.getCommunityFeeDetailCount(reqJson);

        if (datas == null || datas.isEmpty()) {
            datas = new ArrayList<>();
            for (int communityIndex = 0; communityIndex < communityDtos.size(); communityIndex++) {
                if (communityIndex > 9) {
                    continue;
                }
                Map data = new HashMap();
                data.put("communityId", communityDtos.get(communityIndex).getCommunityId());
                data.put("communityName", communityDtos.get(communityIndex).getName());
                data.put("count", 0);
                data.put("receivedAmount", 0);
                datas = new ArrayList<>();
                datas.add(data);
            }
            context.setResponseEntity(ResultVo.createResponseEntity(datas));
            return;
        }

        for (Map data : datas) {
            for (CommunityDto tmpCommunityDto : communityDtos) {
                if (data.get("communityId").equals(tmpCommunityDto.getCommunityId())) {
                    data.put("communityName", tmpCommunityDto.getName());
                }
            }
        }

        context.setResponseEntity(ResultVo.createResponseEntity(datas));
    }
}
