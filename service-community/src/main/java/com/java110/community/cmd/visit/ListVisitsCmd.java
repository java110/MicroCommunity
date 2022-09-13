package com.java110.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.dto.visit.VisitDto;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.visit.ApiVisitDataVo;
import com.java110.vo.api.visit.ApiVisitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "visit.listVisits")
public class ListVisitsCmd extends Cmd {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);
        if (reqJson.containsKey("addVisitType") && !StringUtil.isEmpty(reqJson.getString("addVisitType"))
                && "initAddVisitParameter".equals(reqJson.getString("addVisitType"))) {
            //预约车免费时长
            String freeTime = CommunitySettingFactory.getValue(reqJson.getString("communityId"), CAR_FREE_TIME);
            String numStr = CommunitySettingFactory.getValue(reqJson.getString("communityId"), VISIT_NUMBER);
            int number = 999;
            if(StringUtil.isInteger(numStr)){
                number = Integer.parseInt(numStr);
            }
            Map initAddVisitParameter = new HashMap();
            initAddVisitParameter.put("freeTime", freeTime);
            initAddVisitParameter.put("freeTimes", number);
            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(initAddVisitParameter), HttpStatus.OK);
        } else {
            VisitDto visitDto = BeanConvertUtil.covertBean(reqJson, VisitDto.class);
            int count = visitInnerServiceSMOImpl.queryVisitsCount(visitDto);
            List<ApiVisitDataVo> visits = new ArrayList<>();
            if (count > 0) {
                List<VisitDto> visitDtos = visitInnerServiceSMOImpl.queryVisits(visitDto);
                for (VisitDto visit : visitDtos) {
                    ApiVisitDataVo apiVisitDataVo = BeanConvertUtil.covertBean(visit, ApiVisitDataVo.class);
                    if (!StringUtil.isEmpty(visit.getFileSaveName())) {
                        apiVisitDataVo.setUrl(visit.getFileSaveName());
                    }
                    visits.add(apiVisitDataVo);
                }
            } else {
                visits = new ArrayList<>();
            }

            ApiVisitVo apiVisitVo = new ApiVisitVo();
            apiVisitVo.setTotal(count);
            apiVisitVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
            apiVisitVo.setVisits(visits);

            responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiVisitVo), HttpStatus.OK);
        }
        context.setResponseEntity(responseEntity);

    }
}
