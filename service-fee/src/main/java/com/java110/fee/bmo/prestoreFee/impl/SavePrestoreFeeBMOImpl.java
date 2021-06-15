package com.java110.fee.bmo.prestoreFee.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.fee.bmo.prestoreFee.ISavePrestoreFeeBMO;
import com.java110.intf.fee.IPrestoreFeeInnerServiceSMO;
import com.java110.intf.store.IWechatSmsTemplateInnerServiceSMO;
import com.java110.po.prestoreFee.PrestoreFeePo;
import com.java110.po.wechatSmsTemplate.WechatSmsTemplatePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.prestoreFee.PrestoreFeeDto;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("savePrestoreFeeBMOImpl")
public class SavePrestoreFeeBMOImpl implements ISavePrestoreFeeBMO {

    @Autowired
    private IPrestoreFeeInnerServiceSMO prestoreFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param prestoreFeePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(PrestoreFeePo prestoreFeePo) {

        prestoreFeePo.setPrestoreFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_prestoreFeeId));
        int flag = prestoreFeeInnerServiceSMOImpl.savePrestoreFee(prestoreFeePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
