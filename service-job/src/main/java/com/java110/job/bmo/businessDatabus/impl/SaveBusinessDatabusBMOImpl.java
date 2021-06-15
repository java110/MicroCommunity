package com.java110.job.bmo.businessDatabus.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.job.IBusinessDatabusInnerServiceSMO;
import com.java110.job.bmo.businessDatabus.ISaveBusinessDatabusBMO;
import com.java110.po.businessDatabus.BusinessDatabusPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveBusinessDatabusBMOImpl")
public class SaveBusinessDatabusBMOImpl implements ISaveBusinessDatabusBMO {

    @Autowired
    private IBusinessDatabusInnerServiceSMO businessDatabusInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param businessDatabusPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(BusinessDatabusPo businessDatabusPo) {

        businessDatabusPo.setDatabusId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_databusId));
        int flag = businessDatabusInnerServiceSMOImpl.saveBusinessDatabus(businessDatabusPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
