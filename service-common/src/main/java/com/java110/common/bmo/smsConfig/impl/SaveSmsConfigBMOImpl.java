package com.java110.common.bmo.smsConfig.impl;

import com.java110.common.bmo.smsConfig.ISaveSmsConfigBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.ISmsConfigInnerServiceSMO;
import com.java110.po.smsConfig.SmsConfigPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveSmsConfigBMOImpl")
public class SaveSmsConfigBMOImpl implements ISaveSmsConfigBMO {

    @Autowired
    private ISmsConfigInnerServiceSMO smsConfigInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param smsConfigPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(SmsConfigPo smsConfigPo) {

        smsConfigPo.setSmsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_smsId));
        int flag = smsConfigInnerServiceSMOImpl.saveSmsConfig(smsConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
