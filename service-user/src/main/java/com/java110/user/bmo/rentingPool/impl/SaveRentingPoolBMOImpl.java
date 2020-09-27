package com.java110.user.bmo.rentingPool.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.user.bmo.rentingPool.ISaveRentingPoolBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveRentingPoolBMOImpl")
public class SaveRentingPoolBMOImpl implements ISaveRentingPoolBMO {

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingPoolPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(RentingPoolPo rentingPoolPo) {

        //查询 是否已经存在 房屋待出租的数据
        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setCommunityId(rentingPoolPo.getCommunityId());
        rentingPoolDto.setRoomId(rentingPoolPo.getRoomId());
        rentingPoolDto.setStates(new String[]{"0", "1", "2", "3", "4", "5", "7"});
        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        if (rentingPoolDtos != null && rentingPoolDtos.size() > 0) {
            throw new IllegalArgumentException("该房屋当前为" + rentingPoolDtos.get(0).getStateName() + ",不能再次出租");
        }

        rentingPoolPo.setRentingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rentingId));
        int flag = rentingPoolInnerServiceSMOImpl.saveRentingPool(rentingPoolPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
