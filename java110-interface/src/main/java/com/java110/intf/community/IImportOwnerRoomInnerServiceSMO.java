package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.entity.assetImport.ImportOwnerRoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFloorInnerServiceSMO
 * @Description 小区楼接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/importOwnerRoomApi")
public interface IImportOwnerRoomInnerServiceSMO {

    /**
     * 导入房产信息
     *
     * @param importOwnerRoomDtos 数据对象分享
     * @return CommunityDto 对象数据
     */
    @RequestMapping(value = "/saveOwnerRooms", method = RequestMethod.POST)
    int saveOwnerRooms(@RequestBody List<ImportOwnerRoomDto> importOwnerRoomDtos);


}
