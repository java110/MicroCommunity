package com.java110.api.listener.advert;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.intf.common.IAdvertItemInnerServiceSMO;
import com.java110.dto.advert.AdvertItemDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeAdvertConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.advert.ApiAdvertItemDataVo;
import com.java110.vo.api.advert.ApiAdvertItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询小区侦听类
 */
@Java110Listener("listAdvertItemsListener")
public class ListAdvertItemsListener extends AbstractServiceApiListener {

    @Autowired
    private IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAdvertConstant.LIST_ADVERT_ITEMS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    public IAdvertItemInnerServiceSMO getAdvertItemInnerServiceSMOImpl() {
        return advertItemInnerServiceSMOImpl;
    }

    public void setAdvertItemInnerServiceSMOImpl(IAdvertItemInnerServiceSMO advertItemInnerServiceSMOImpl) {
        this.advertItemInnerServiceSMOImpl = advertItemInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {

        super.validatePageInfo(reqJson);

        Assert.hasKeyAndValue(reqJson, "advertId", "请求报文中未包含广告ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AdvertItemDto advertItemDto = BeanConvertUtil.covertBean(reqJson, AdvertItemDto.class);

        int count = advertItemInnerServiceSMOImpl.queryAdvertItemsCount(advertItemDto);

        List<ApiAdvertItemDataVo> advertItems = null;

        if (count > 0) {
            List<AdvertItemDto> advertItemDtos = advertItemInnerServiceSMOImpl.queryAdvertItems(advertItemDto);
            //refreshAdvertUrl(advertItemDtos);
            advertItems = BeanConvertUtil.covertBeanList(advertItemDtos, ApiAdvertItemDataVo.class);
        } else {
            advertItems = new ArrayList<>();
        }

        ApiAdvertItemVo apiAdvertItemVo = new ApiAdvertItemVo();

        apiAdvertItemVo.setTotal(count);
        apiAdvertItemVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiAdvertItemVo.setAdvertItems(advertItems);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiAdvertItemVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    private void refreshAdvertUrl(List<AdvertItemDto> advertItemDtos) {
        for(AdvertItemDto advertItemDto : advertItemDtos){
            if("8888".equals(advertItemDto.getItemTypeCd())){
                advertItemDto.setUrl("/callComponent/download/getFile/file?fileId=" + advertItemDto.getUrl() + "&communityId=" + advertItemDto.getCommunityId());
            }
        }
    }
}
