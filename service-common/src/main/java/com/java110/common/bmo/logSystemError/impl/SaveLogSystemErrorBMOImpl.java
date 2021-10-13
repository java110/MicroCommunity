package com.java110.common.bmo.logSystemError.impl;

import com.java110.common.bmo.logSystemError.ISaveLogSystemErrorBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveLogSystemErrorBMOImpl")
public class SaveLogSystemErrorBMOImpl implements ISaveLogSystemErrorBMO {

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param logSystemErrorPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(LogSystemErrorPo logSystemErrorPo) {

        logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
        int flag = logSystemErrorInnerServiceSMOImpl.saveLogSystemError(logSystemErrorPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
