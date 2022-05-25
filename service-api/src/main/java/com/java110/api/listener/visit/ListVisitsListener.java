package com.java110.api.listener.visit;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.dto.visit.VisitDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.visit.ApiVisitDataVo;
import com.java110.vo.api.visit.ApiVisitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.java110.utils.constant.ServiceCodeVisitConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询小区侦听类
 */
@Java110Listener("listVisitsListener")
public class ListVisitsListener extends AbstractServiceApiListener {

    @Autowired
    private IVisitInnerServiceSMO visitInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitConstant.LIST_VISITS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    //键
    public static final String CAR_FREE_TIME = "CAR_FREE_TIME";

    //键
    public static final String VISIT_NUMBER = "VISIT_NUMBER";

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IVisitInnerServiceSMO getVisitInnerServiceSMOImpl() {
        return visitInnerServiceSMOImpl;
    }

    public void setVisitInnerServiceSMOImpl(IVisitInnerServiceSMO visitInnerServiceSMOImpl) {
        this.visitInnerServiceSMOImpl = visitInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.OK);
        if (reqJson.containsKey("addVisitType") && !StringUtil.isEmpty(reqJson.getString("addVisitType"))
                && "initAddVisitParameter".equals(reqJson.getString("addVisitType"))) {
            //预约车免费时长
            String freeTime = CommunitySettingFactory.getValue(reqJson.getString("communityId"), CAR_FREE_TIME);
            //预约车免费次数
            int number = Integer.parseInt(CommunitySettingFactory.getValue(reqJson.getString("communityId"), VISIT_NUMBER));
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
                        apiVisitDataVo.setUrl("/callComponent/download/getFile/file?fileId=" + visit.getFileSaveName() + "&communityId=-1");
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
