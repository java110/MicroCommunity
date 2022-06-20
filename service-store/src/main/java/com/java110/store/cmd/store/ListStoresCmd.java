package com.java110.store.cmd.store;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreAttrDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreAttrV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.store.ApiStoreDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "store.listStores")
public class ListStoresCmd extends Cmd {

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreAttrV1InnerServiceSMO storeAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        //只有运营可以看所有 商户信息
        if (StoreDto.STORE_TYPE_ADMIN.equals(storeDtos.get(0).getStoreTypeCd())) {
            reqJson.remove("storeId");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        StoreDto storeDto = BeanConvertUtil.covertBean(reqJson, StoreDto.class);
        storeDto.setUserId("");
        int count = storeV1InnerServiceSMOImpl.queryStoresCount(storeDto);
        List<StoreDto> storeDtos = null;
        List<ApiStoreDataVo> stores = null;
        if (count > 0) {
            storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
            stores = BeanConvertUtil.covertBeanList(storeDtos, ApiStoreDataVo.class);
            refreshStoreAttr(stores);
        } else {
            stores = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, stores);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }


    private void refreshStoreAttr(List<ApiStoreDataVo> stores) {
        StoreAttrDto storeAttrDto = new StoreAttrDto();
        storeAttrDto.setStoreIds(getStoreIds(stores));
        List<StoreAttrDto> storeAttrDtos = storeAttrV1InnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
        for (ApiStoreDataVo storeDataVo : stores) {
            List<StoreAttrDto> storeAttrs = new ArrayList<StoreAttrDto>();
            for (StoreAttrDto tmpStoreAttrDto : storeAttrDtos) {

                if (storeDataVo.getStoreId().equals(tmpStoreAttrDto.getStoreId())) {
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
