package com.java110.feign.merchant;

import com.java110.common.util.ProtocolUtil;

/**
 * Created by wuxw on 2017/8/30.
 */
public class MerchantMemberServiceFallBack implements IMerchantMemberService {
    @Override
    public String queryMerchantMember(String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soMerchantMemberService(String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soMerchantMemberServiceForOrderService(String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoMerchantMember(String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soDeleteMerchantMemberService(String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String queryNeedDeleteData(String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }
}
