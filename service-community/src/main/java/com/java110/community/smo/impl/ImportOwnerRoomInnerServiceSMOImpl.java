package com.java110.community.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.FloorDto;
import com.java110.entity.assetImport.ImportOwnerRoomDto;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.community.IImportOwnerRoomInnerServiceSMO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 小区服务内部类
 */
@RestController
public class ImportOwnerRoomInnerServiceSMOImpl extends BaseServiceSMO implements IImportOwnerRoomInnerServiceSMO {
    private static Logger logger = LoggerFactory.getLogger(CommunityServiceSMOImpl.class);

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    public int saveOwnerRooms(@RequestBody List<ImportOwnerRoomDto> importOwnerRoomDtos) {

        if (importOwnerRoomDtos == null || importOwnerRoomDtos.size() < 1) {
            return 0;
        }

        // 1.0 查看 楼栋是否存在

        return 0;
    }


    private List<FloorDto> getFloorDto(List<ImportOwnerRoomDto> importOwnerRoomDtos) {

        List<FloorDto> floorDtos = new ArrayList<>();
        FloorDto tmpFloorDto = null;
        for (ImportOwnerRoomDto importOwnerRoomDto : importOwnerRoomDtos) {

            if (hasInFloors(floorDtos, importOwnerRoomDto)) {
                continue;
            }
            tmpFloorDto = new FloorDto();
            tmpFloorDto.setFloorNum(importOwnerRoomDto.getFloorNum());
            tmpFloorDto.setCommunityId(importOwnerRoomDto.getCommunityId());
            floorDtos.add(tmpFloorDto);
        }
        return floorDtos;
    }

    /**
     * 是否存在 floorDtos 对象中
     * @param floorDtos
     * @param importOwnerRoomDto
     * @return
     */
    private boolean hasInFloors(List<FloorDto> floorDtos, ImportOwnerRoomDto importOwnerRoomDto) {
        for (FloorDto floorDto : floorDtos) {
            if (importOwnerRoomDto.getFloorNum().equals(floorDto.getFloorNum())) {
                return true;
            }
        }
        return false;
    }

}
