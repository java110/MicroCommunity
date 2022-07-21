package com.java110.dev.cmd.mapping;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.mapping.MappingDto;
import com.java110.intf.community.IMappingInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.mapping.ApiMappingDataVo;
import com.java110.vo.api.mapping.ApiMappingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "mapping.listMappings")
public class ListMappingsCmd extends Cmd {

    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        MappingDto mappingDto = BeanConvertUtil.covertBean(reqJson, MappingDto.class);

        int count = mappingInnerServiceSMOImpl.queryMappingsCount(mappingDto);

        List<ApiMappingDataVo> mappings = null;

        if (count > 0) {
            mappings = BeanConvertUtil.covertBeanList(mappingInnerServiceSMOImpl.queryMappings(mappingDto), ApiMappingDataVo.class);
        } else {
            mappings = new ArrayList<>();
        }

        ApiMappingVo apiMappingVo = new ApiMappingVo();

        apiMappingVo.setTotal(count);
        apiMappingVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMappingVo.setMappings(mappings);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMappingVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
