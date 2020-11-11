package com.java110.user.bmo.rentingPoolAttr.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IRentingPoolAttrInnerServiceSMO;
import com.java110.po.rentingPoolAttr.RentingPoolAttrPo;
import com.java110.user.bmo.rentingPoolAttr.IUpdateRentingPoolAttrBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingPoolAttrBMOImpl")
public class UpdateRentingPoolAttrBMOImpl implements IUpdateRentingPoolAttrBMO {

    @Autowired
    private IRentingPoolAttrInnerServiceSMO rentingPoolAttrInnerServiceSMOImpl;

    /**
     * @param rentingPoolAttrPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(RentingPoolAttrPo rentingPoolAttrPo) {

        int flag = rentingPoolAttrInnerServiceSMOImpl.updateRentingPoolAttr(rentingPoolAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
