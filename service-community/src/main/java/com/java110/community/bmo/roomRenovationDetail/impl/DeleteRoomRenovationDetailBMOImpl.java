package com.java110.community.bmo.roomRenovationDetail.impl;

import com.java110.community.bmo.roomRenovationDetail.IDeleteRoomRenovationDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRoomRenovationDetailBMOImpl")
public class DeleteRoomRenovationDetailBMOImpl implements IDeleteRoomRenovationDetailBMO {

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMOImpl;

    /**
     * @param roomRenovationDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(RoomRenovationDetailPo roomRenovationDetailPo) {

        int flag = roomRenovationDetailInnerServiceSMOImpl.deleteRoomRenovationDetail(roomRenovationDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
