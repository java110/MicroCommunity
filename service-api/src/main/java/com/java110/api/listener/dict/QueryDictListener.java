package com.java110.api.listener.dict;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.community.DictInnerServiceSMO;
import com.java110.dto.Dict.DictDto;
import com.java110.dto.Dict.DictQueryDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * <br>
 * Created by 离歌笑 on 10/22/2019
 * <p>
 */
@Java110Listener("queryDictListener")
public class QueryDictListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private DictInnerServiceSMO dictInnerServiceSMO;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_DICT_TYPE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取查询参数
        JSONObject reqJson = event.getDataFlowContext().getReqJson();
        List<DictDto> dictDtos = this.dictInnerServiceSMO.queryDict(BeanConvertUtil.covertBean(reqJson,DictQueryDto.class));
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(dictDtos), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
