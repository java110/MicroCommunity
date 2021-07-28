package com.java110.common.bmo.smsConfig.impl;

import com.java110.common.bmo.smsConfig.IDeleteSmsConfigBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ISmsConfigInnerServiceSMO;
import com.java110.po.smsConfig.SmsConfigPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteSmsConfigBMOImpl")
public class DeleteSmsConfigBMOImpl implements IDeleteSmsConfigBMO {

    @Autowired
    private ISmsConfigInnerServiceSMO smsConfigInnerServiceSMOImpl;

    /**
     * @param smsConfigPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(SmsConfigPo smsConfigPo) {

        int flag = smsConfigInnerServiceSMOImpl.deleteSmsConfig(smsConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
