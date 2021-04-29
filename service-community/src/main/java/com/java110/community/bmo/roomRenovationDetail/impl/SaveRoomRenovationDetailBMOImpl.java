package com.java110.community.bmo.roomRenovationDetail.impl;

import com.java110.community.bmo.roomRenovationDetail.ISaveRoomRenovationDetailBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IRoomRenovationDetailInnerServiceSMO;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRoomRenovationDetailBMOImpl")
public class SaveRoomRenovationDetailBMOImpl implements ISaveRoomRenovationDetailBMO {

    @Autowired
    private IRoomRenovationDetailInnerServiceSMO roomRenovationDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param roomRenovationDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RoomRenovationDetailPo roomRenovationDetailPo) {

        roomRenovationDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = roomRenovationDetailInnerServiceSMOImpl.saveRoomRenovationDetail(roomRenovationDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
