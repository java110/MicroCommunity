package com.java110.user.bmo.staffAppAuth.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IStaffAppAuthInnerServiceSMO;
import com.java110.po.staffAppAuth.StaffAppAuthPo;
import com.java110.user.bmo.staffAppAuth.IUpdateStaffAppAuthBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStaffAppAuthBMOImpl")
public class UpdateStaffAppAuthBMOImpl implements IUpdateStaffAppAuthBMO {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    /**
     * @param staffAppAuthPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(StaffAppAuthPo staffAppAuthPo) {

        int flag = staffAppAuthInnerServiceSMOImpl.updateStaffAppAuth(staffAppAuthPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
