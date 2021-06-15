package com.java110.job.bmo.businessDatabus.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.job.IBusinessDatabusInnerServiceSMO;
import com.java110.job.bmo.businessDatabus.IUpdateBusinessDatabusBMO;
import com.java110.po.businessDatabus.BusinessDatabusPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateBusinessDatabusBMOImpl")
public class UpdateBusinessDatabusBMOImpl implements IUpdateBusinessDatabusBMO {

    @Autowired
    private IBusinessDatabusInnerServiceSMO businessDatabusInnerServiceSMOImpl;

    /**
     * @param businessDatabusPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(BusinessDatabusPo businessDatabusPo) {

        int flag = businessDatabusInnerServiceSMOImpl.updateBusinessDatabus(businessDatabusPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
