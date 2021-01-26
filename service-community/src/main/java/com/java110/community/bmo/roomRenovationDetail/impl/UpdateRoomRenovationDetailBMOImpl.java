package com.java110.community.bmo.roomRenovationDetail.impl;

import com.java110.community.bmo.roomRenovationDetail.IUpdateRoomRenovationDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRoomRenovationDetailBMOImpl")
public class UpdateRoomRenovationDetailBMOImpl implements IUpdateRoomRenovationDetailBMO {

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMOImpl;

    /**
     * @param roomRenovationDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(RoomRenovationDetailPo roomRenovationDetailPo) {

        int flag = roomRenovationDetailInnerServiceSMOImpl.updateRoomRenovationDetail(roomRenovationDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
