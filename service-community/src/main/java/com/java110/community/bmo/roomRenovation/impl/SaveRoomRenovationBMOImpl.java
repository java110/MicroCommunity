package com.java110.community.bmo.roomRenovation.impl;

import com.java110.community.bmo.roomRenovation.ISaveRoomRenovationBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IRoomRenovationInnerServiceSMO;
import com.java110.po.roomRenovation.RoomRenovationPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRoomRenovationBMOImpl")
public class SaveRoomRenovationBMOImpl implements ISaveRoomRenovationBMO {

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param roomRenovationPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RoomRenovationPo roomRenovationPo) {

        roomRenovationPo.setrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rId));
        int flag = roomRenovationInnerServiceSMOImpl.saveRoomRenovation(roomRenovationPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
