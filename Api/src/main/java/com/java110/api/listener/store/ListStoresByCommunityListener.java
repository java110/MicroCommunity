package com.java110.api.listener.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.DataFlowFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.smo.community.ICommunityInnerServiceSMO;
import com.java110.core.smo.store.IStoreInnerServiceSMO;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.store.StoreDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.*;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.advert.ApiAdvertDataVo;
import com.java110.vo.api.advert.ApiAdvertVo;
import com.java110.vo.api.store.ApiStoreDataVo;
import com.java110.vo.api.store.ApiStoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存商户信息
 * Created by Administrator on 2019/3/29.
 */
@Java110Listener("listStoresByCommunityListener")
public class ListStoresByCommunityListener extends AbstractServiceApiListener {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_STORES_BY_COMMUNITY;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {


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
