package com.java110.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.store.ApiStoreDataVo;
import com.java110.vo.api.store.ApiStoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "store.listStoresByCommunity")
public class ListStoresByCommunityCmd extends Cmd {


    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        CommunityMemberDto communityMemberDto = BeanConvertUtil.covertBean(reqJson, CommunityMemberDto.class);
        int storeCount = communityInnerServiceSMOImpl.getCommunityMemberCount(communityMemberDto);
        List<CommunityMemberDto> communityMemberDtos = null;
        List<ApiStoreDataVo> stores = null;
        if (storeCount > 0) {
            communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);
            StoreDto storeDto = new StoreDto();
            storeDto.setStoreIds(getStoreIds(communityMemberDtos));
            List<StoreDto> storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);
            stores = BeanConvertUtil.covertBeanList(storeDtos, ApiStoreDataVo.class);
        } else {
            stores = new ArrayList<>();
        }

        ApiStoreVo apiStoreVo = new ApiStoreVo();

        apiStoreVo.setTotal(storeCount);
        apiStoreVo.setRecords((int) Math.ceil((double) storeCount / (double) reqJson.getInteger("row")));
        apiStoreVo.setStores(stores);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiStoreVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 查询商户ID
     *
     * @param communityMemberDtos
     * @return
     */
    private String[] getStoreIds(List<CommunityMemberDto> communityMemberDtos) {
        List<String> storeIds = new ArrayList<>();
        for (CommunityMemberDto communityMemberDto : communityMemberDtos) {
            storeIds.add(communityMemberDto.getMemberId());
        }

        return storeIds.toArray(new String[storeIds.size()]);
    }
}
