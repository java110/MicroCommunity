package com.java110.feign.code;

import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.ProtocolUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用用户服务失败时，返回
 * Created by wuxw on 2017/4/5.
 */
@Component
public class CodeApiFallback implements ICodeApi{


    /**
     * 生成编码
     * @param prefix 前缀
     * @return
     */
    @Override
    @RequestMapping("/codeApi/generateCode")
    public String generateCode(@RequestParam("prefix") String prefix) {
        return ResponseConstant.RESULT_CODE_ERROR;
    }
}
