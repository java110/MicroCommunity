package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.roomRenovation.IDeleteRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.IGetRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.ISaveRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.IUpdateRoomRenovationBMO;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.po.roomRenovation.RoomRenovationPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/roomRenovation")
public class RoomRenovationApi {

    @Autowired
    private ISaveRoomRenovationBMO saveRoomRenovationBMOImpl;
    @Autowired
    private IUpdateRoomRenovationBMO updateRoomRenovationBMOImpl;
    @Autowired
    private IDeleteRoomRenovationBMO deleteRoomRenovationBMOImpl;

    @Autowired
    private IGetRoomRenovationBMO getRoomRenovationBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/saveRoomRenovation
     * @path /app/roomRenovation/saveRoomRenovation
     */
    @RequestMapping(value = "/saveRoomRenovation", method = RequestMethod.POST)
    public ResponseEntity<String> saveRoomRenovation(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        //Assert.hasKeyAndValue(reqJson, "isViolation", "请求报文中未包含isViolation");

        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        roomRenovationPo.setState("1000");
        roomRenovationPo.setIsViolation("N");

        return saveRoomRenovationBMOImpl.save(roomRenovationPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/updateRoomRenovation
     * @path /app/roomRenovation/updateRoomRenovation
     */
    @RequestMapping(value = "/updateRoomRenovation", method = RequestMethod.POST)
    public ResponseEntity<String> updateRoomRenovation(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "isViolation", "请求报文中未包含isViolation");
        Assert.hasKeyAndValue(reqJson, "rId", "rId不能为空");


        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        return updateRoomRenovationBMOImpl.update(roomRenovationPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/deleteRoomRenovation
     * @path /app/roomRenovation/deleteRoomRenovation
     */
    @RequestMapping(value = "/deleteRoomRenovation", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRoomRenovation(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "rId", "rId不能为空");


        RoomRenovationPo roomRenovationPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationPo.class);
        return deleteRoomRenovationBMOImpl.delete(roomRenovationPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /roomRenovation/queryRoomRenovation
     * @path /app/roomRenovation/queryRoomRenovation
     */
    @RequestMapping(value = "/queryRoomRenovation", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovation(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        RoomRenovationDto roomRenovationDto = new RoomRenovationDto();
        roomRenovationDto.setPage(page);
        roomRenovationDto.setRow(row);
        roomRenovationDto.setCommunityId(communityId);
        return getRoomRenovationBMOImpl.get(roomRenovationDto);
    }
}
