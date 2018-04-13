package com.java110.feign.merchant;

import com.java110.common.util.ProtocolUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用用户服务失败时，返回
 * Created by wuxw on 2017/4/5.
 */
public class MerchantServiceFallback implements IMerchantService {
    /**
     * 查询用户信息失败
     * @param data
     * @return
     */
    @Override
    public String queryMerchantInfo(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soMerchantService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soMerchantServiceForOrderService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoMerchant(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoMerchantAttr(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soDeleteMerchantService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String queryMerchantInfoByOlId(String busiOrder) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String queryNeedDeleteMerchantInfoByOlId(String busiOrder) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }
}
