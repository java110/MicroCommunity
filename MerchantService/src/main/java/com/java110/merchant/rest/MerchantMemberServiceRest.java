package com.java110.merchant.rest;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.controller.BaseController;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantMember;
import com.java110.feign.merchant.IMerchantMemberService;
import com.java110.merchant.smo.IMerchantMemberServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户成员操作
 * Created by wuxw on 2017/8/30.
 */
@RestController
public class MerchantMemberServiceRest extends BaseController implements IMerchantMemberService {

    @Autowired
    IMerchantMemberServiceSMO iMerchantMemberServiceSMO;



    /**
     * 通过Merchant对象中数据查询用户信息
     * 如,用户ID，名称
     * @param data
     * @return
     */
    @RequestMapping("/merchantService/queryMerchantInfo")
    public String queryMerchantMember(@RequestParam("data") String data){
        LoggerEngine.debug("queryMerchantInfo入参：" + data);


        String resultMerchantInfo = null;

        JSONObject reqMerchantJSON = null;
        try {
            reqMerchantJSON = this.simpleValidateJSON(data);
            MerchantMember oldMerchantMember = new MerchantMember();
            oldMerchantMember.setMerchantId(reqMerchantJSON.getString("merchantId"));
            resultMerchantInfo = iMerchantMemberServiceSMO.queryMerchantMember(oldMerchantMember);

        } catch (Exception e) {
            LoggerEngine.error("服务处理出现异常：", e);
            resultMerchantInfo = ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"服务处理出现异常"+e,null);
        } finally {
            LoggerEngine.debug("用户服务操作客户出参：" + resultMerchantInfo);
            return resultMerchantInfo;
        }
    }

}
