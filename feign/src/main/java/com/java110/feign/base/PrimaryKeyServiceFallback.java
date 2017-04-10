package com.java110.feign.base;

import com.java110.common.util.ProtocolUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wuxw on 2017/4/11.
 */
public class PrimaryKeyServiceFallback implements IPrimaryKeyService {
    @Override
    public String queryPrimaryKey(@RequestParam("data") String data) {
        return ProtocolUtil.createResultMsg(ProtocolUtil.RETURN_MSG_ERROR,"调用主键查询超时",null);

    }
}
