package com.java110.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.service.context.DataQuery;
import com.java110.service.smo.IQueryServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Java110Cmd(serviceCode = "user.getUserInfo")
public class GetUserInfoCmd extends Cmd {

    @Autowired
    private IQueryServiceSMO queryServiceSMOImpl;

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = context.getReqHeaders().get("user-id");
        String storeId = context.getReqHeaders().get("store-id");
        DataQuery dataQuery = new DataQuery();
        dataQuery.setServiceCode("query.user.userInfo");
        JSONObject param = new JSONObject();
        param.put("userId", userId);
        dataQuery.setRequestParams(param);
        queryServiceSMOImpl.commonQueryService(dataQuery);
        ResponseEntity<String> responseEntity = dataQuery.getResponseEntity();

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            context.setResponseEntity(responseEntity);
            return;
        }
        JSONObject tmpUserInfo = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONObject resultUserInfo = new JSONObject();


        if (!StringUtil.isEmpty(storeId)) {
            StoreDto storeDto = new StoreDto();
            storeDto.setStoreId(storeId);
            storeDto.setPage(1);
            storeDto.setRow(1);
            List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
            if (storeDtos != null && storeDtos.size() > 0) {
                resultUserInfo.put("storeTypeCd", storeDtos.get(0).getStoreTypeCd());
            }
        }

        resultUserInfo.put("name", tmpUserInfo.getString("name"));
        resultUserInfo.put("address", tmpUserInfo.getString("address"));
        resultUserInfo.put("sex", tmpUserInfo.getString("sex"));
        resultUserInfo.put("localtionCd", tmpUserInfo.getString("localtionCd"));
        resultUserInfo.put("levelCd", tmpUserInfo.getString("levelCd"));
        //resultUserInfo.put("tel", CommonUtil.mobileEncrypt(tmpUserInfo.getString("tel")));
        resultUserInfo.put("tel", tmpUserInfo.getString("tel")); // 这里不加密了 因为前台很多地方直接 关联出 用户的手机号 所以 加密了 没法处理 modify by wuxw 2022-07-04
        resultUserInfo.put("email", tmpUserInfo.getString("email"));
        resultUserInfo.put("userId", tmpUserInfo.getString("userId"));
        String watermark = MappingCache.getValue(MappingConstant.DOMAIN_SYSTEM_SWITCH,"watermark");
        resultUserInfo.put("watermark", watermark);

        responseEntity = new ResponseEntity<String>(resultUserInfo.toJSONString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
