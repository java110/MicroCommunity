package com.java110.common.bmo.logSystemError.impl;

import com.java110.common.bmo.logSystemError.IDeleteLogSystemErrorBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.ILogSystemErrorInnerServiceSMO;
import com.java110.po.logSystemError.LogSystemErrorPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteLogSystemErrorBMOImpl")
public class DeleteLogSystemErrorBMOImpl implements IDeleteLogSystemErrorBMO {

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    /**
     * @param logSystemErrorPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(LogSystemErrorPo logSystemErrorPo) {

        int flag = logSystemErrorInnerServiceSMOImpl.deleteLogSystemError(logSystemErrorPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
