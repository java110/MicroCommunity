package com.java110.common.bmo.logSystemError.impl;

import com.java110.common.bmo.logSystemError.IUpdateLogSystemErrorBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateLogSystemErrorBMOImpl")
public class UpdateLogSystemErrorBMOImpl implements IUpdateLogSystemErrorBMO {

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    /**
     * @param logSystemErrorPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(LogSystemErrorPo logSystemErrorPo) {

        int flag = logSystemErrorInnerServiceSMOImpl.updateLogSystemError(logSystemErrorPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
