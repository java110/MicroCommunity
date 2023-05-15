package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerRoomRelDto;
import com.java110.po.owner.OwnerRoomRelPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerRoomRelInnerServiceSMO
 * @Description 业主房屋接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerRoomRelApi")
public interface IOwnerRoomRelInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param ownerRoomRelDto 数据对象分享
     * @return OwnerRoomRelDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerRoomRels", method = RequestMethod.POST)
    List<OwnerRoomRelDto> queryOwnerRoomRels(@RequestBody OwnerRoomRelDto ownerRoomRelDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerRoomRelDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerRoomRelsCount", method = RequestMethod.POST)
    int queryOwnerRoomRelsCount(@RequestBody OwnerRoomRelDto ownerRoomRelDto);


    /**
     * <p>保存业主房屋关系</p>
     *
     * @param ownerRoomRelPo 关系对象
     * @return OwnerRoomRelDto 对象数据
     */
    @RequestMapping(value = "/saveOwnerRoomRels", method = RequestMethod.POST)
    int saveOwnerRoomRels(@RequestBody OwnerRoomRelPo ownerRoomRelPo);
    /**
     * <p>保存业主房屋关系</p>
     *
     * @param ownerRoomRelPo 关系对象
     * @return OwnerRoomRelDto 对象数据
     */
    @RequestMapping(value = "/saveBusinessOwnerRoomRels", method = RequestMethod.POST)
    int saveBusinessOwnerRoomRels(@RequestBody OwnerRoomRelPo ownerRoomRelPo);

    /**
     * <p>保存业主房屋关系</p>
     *
     * @param ownerRoomRelPo 关系对象
     * @return OwnerRoomRelDto 对象数据
     */
    @RequestMapping(value = "/updateOwnerRoomRels", method = RequestMethod.POST)
    int updateOwnerRoomRels(@RequestBody OwnerRoomRelPo ownerRoomRelPo);
}
