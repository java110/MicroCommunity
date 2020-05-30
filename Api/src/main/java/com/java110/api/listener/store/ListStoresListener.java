package com.java110.api.listener.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.store.IStoreInnerServiceSMO;
import com.java110.dto.store.StoreAttrDto;
import com.java110.dto.store.StoreDto;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.*;
import com.java110.utils.util.BeanConvertUtil;
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
@Java110Listener("listStoresListener")
public class ListStoresListener extends AbstractServiceApiListener {

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;


    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_STORES;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        StoreDto storeDto = BeanConvertUtil.covertBean(reqJson, StoreDto.class);
        int storeCount = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        List<StoreDto> storeDtos = null;
        List<ApiStoreDataVo> stores = null;
        if (storeCount > 0) {
            storeDtos = storeInnerServiceSMOImpl.getStores(storeDto);
            stores = BeanConvertUtil.covertBeanList(storeDtos, ApiStoreDataVo.class);
            refreshStoreAttr(stores);
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

    private void refreshStoreAttr(List<ApiStoreDataVo> stores) {
        StoreAttrDto storeAttrDto = new StoreAttrDto();
        storeAttrDto.setStoreIds(getStoreIds(stores));
        List<StoreAttrDto> storeAttrDtos = storeInnerServiceSMOImpl.getStoreAttrs(storeAttrDto);
        for (ApiStoreDataVo storeDataVo : stores) {
            List<StoreAttrDto> storeAttrs = new ArrayList<StoreAttrDto>();
            for (StoreAttrDto tmpStoreAttrDto : storeAttrDtos) {

                if(storeDataVo.getStoreId().equals(storeDataVo.getStoreId())){
                    storeAttrs.add(tmpStoreAttrDto);
                }

                if (!storeDataVo.getStoreId().equals(tmpStoreAttrDto.getStoreId())) {
                    continue;
                }
                if ("100201903001".equals(tmpStoreAttrDto.getSpecCd())) {
                    storeDataVo.setArtificialPerson(tmpStoreAttrDto.getValue());
                } else if ("100201903003".equals(tmpStoreAttrDto.getSpecCd())) {
                    storeDataVo.setEstablishment(tmpStoreAttrDto.getValue());
                } else if ("100201903005".equals(tmpStoreAttrDto.getSpecCd())) {
                    storeDataVo.setBusinessScope(tmpStoreAttrDto.getValue());
                }
            }
            storeDataVo.setStoreAttrDtoList(storeAttrs);
        }
    }

    /**
     * 查询商户ID
     *
     * @param apiStoreDataVos
     * @return
     */
    private String[] getStoreIds(List<ApiStoreDataVo> apiStoreDataVos) {
        List<String> storeIds = new ArrayList<>();
        for (ApiStoreDataVo storeDataVo : apiStoreDataVos) {
            storeIds.add(storeDataVo.getStoreId());
        }

        return storeIds.toArray(new String[storeIds.size()]);
    }


}
