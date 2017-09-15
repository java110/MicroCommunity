package com.java110.feign.user;

import com.java110.common.util.ProtocolUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用用户服务失败时，返回
 * Created by wuxw on 2017/4/5.
 */
public class UserServiceFallback implements IUserService{
    /**
     * 查询用户信息失败
     * @param data
     * @return
     */
    @Override
    public String queryUserInfo(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String queryCustInfoByOlId(String busiOrder) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    /**
     * 根据购物车信息查询 需要作废的发起的报文
     *
     * 这里返回data信息
     * @param busiOrder
     * @return
     */
    @Override
    public String queryNeedDeleteCustInfoByOlId(String busiOrder){
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soUserService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soUserServiceForOrderService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoCust(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoCustAttr(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soDeleteCustService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }
}
