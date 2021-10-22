package com.java110.core.context;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Cmd上下文实现
 * Created by wuxw on 2018/4/13.
 */
public class CmdDataFlow extends AbstractCmdDataFlowContext {

    public CmdDataFlow() {
    }

    public CmdDataFlow(Date startDate, String code) {
        super(startDate, code);
    }

    private String serviceCode;

    //rest 返回对象
    private ResponseEntity responseEntity;

    /**
     * 构建 OrderDataFlow 对象
     *
     * @param reqInfo
     * @param headerAll
     * @return
     * @throws Exception
     */
    public CmdDataFlow doBuilder(String reqInfo, Map<String, String> headerAll) throws Exception {
        String serviceCode = headerAll.get(CommonConstant.HTTP_SERVICE);
        Assert.hasLength(serviceCode, "未包含服务编码");
        this.setDataFlowId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        if (StringUtil.isJsonObject(reqInfo)) {
            //赋值请求报文
            this.setReqJson(JSONObject.parseObject(reqInfo));
        } else {
            this.setReqJson(new JSONObject());
        }
        this.setReqData(reqInfo);

        this.setServiceCode(serviceCode);
        //赋值 请求头信息
        this.setReqHeaders(headerAll);
        //构建返回头
        builderResHeaders();

        return this;
    }

    /**
     * 构建返回头信息
     */
    private void builderResHeaders() {
        Map<String, String> tmpResHeaders = new HashMap<String, String>();
        tmpResHeaders.put(CommonConstant.HTTP_TRANSACTION_ID, this.getReqHeaders().get(CommonConstant.HTTP_TRANSACTION_ID));
        tmpResHeaders.put(CommonConstant.HTTP_RES_TIME, DateUtil.getyyyyMMddhhmmssDateString());
        this.setResHeaders(tmpResHeaders);
    }

    @Override
    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }

    public void setResponseEntity(ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }
}
