package com.java110.community.bmo.roomRenovation.impl;

import com.java110.community.bmo.roomRenovation.IDeleteRoomRenovationBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.community.IRoomRenovationInnerServiceSMO;
import com.java110.po.roomRenovation.RoomRenovationPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRoomRenovationBMOImpl")
public class DeleteRoomRenovationBMOImpl implements IDeleteRoomRenovationBMO {

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    /**
     * @param roomRenovationPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(RoomRenovationPo roomRenovationPo) {

        int flag = roomRenovationInnerServiceSMOImpl.deleteRoomRenovation(roomRenovationPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
