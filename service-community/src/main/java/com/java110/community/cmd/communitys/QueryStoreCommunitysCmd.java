package com.java110.community.cmd.communitys;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.community.CommunityDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 查询商户小区
 * add by 吴学文 2021-09-18
 */
@Java110Cmd(serviceCode = "/communitys/queryStoreCommunitys")
public class QueryStoreCommunitysCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(QueryStoreCommunitysCmd.class);


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        CommunityMemberDto communityMemberDto = BeanConvertUtil.covertBean(reqJson, CommunityMemberDto.class);
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.getStoreCommunitys(communityMemberDto);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(communityDtos));
    }
}
