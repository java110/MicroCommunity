package com.java110.feign.product;

import com.java110.common.util.ProtocolUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用用户服务失败时，返回
 * Created by wuxw on 2017/4/5.
 */
public class ProductServiceFallback implements IProductService {
    /**
     * 查询用户信息失败
     * @param data
     * @return
     */
    @Override
    public String queryProductInfo(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soProductService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soProductServiceForOrderService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoProduct(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soBoProductAttr(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }

    @Override
    public String soDeleteProductService(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用服务超时",null);
    }
}
