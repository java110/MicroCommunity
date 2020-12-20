package com.java110.common.bmo.machineTranslateError.impl;

import com.java110.common.bmo.machineTranslateError.IDeleteMachineTranslateErrorBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.java110.po.machineTranslateError.MachineTranslateErrorPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteMachineTranslateErrorBMOImpl")
public class DeleteMachineTranslateErrorBMOImpl implements IDeleteMachineTranslateErrorBMO {

    @Autowired
    private IMachineTranslateErrorInnerServiceSMO machineTranslateErrorInnerServiceSMOImpl;

    /**
     * @param machineTranslateErrorPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(MachineTranslateErrorPo machineTranslateErrorPo) {

        int flag = machineTranslateErrorInnerServiceSMOImpl.deleteMachineTranslateError(machineTranslateErrorPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
