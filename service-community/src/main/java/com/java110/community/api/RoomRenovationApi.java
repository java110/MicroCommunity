package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.roomRenovation.IDeleteRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.IGetRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.ISaveRoomRenovationBMO;
import com.java110.community.bmo.roomRenovation.IUpdateRoomRenovationBMO;
import com.java110.community.bmo.roomRenovationDetail.IDeleteRoomRenovationDetailBMO;
import com.java110.community.bmo.roomRenovationDetail.IGetRoomRenovationDetailBMO;
import com.java110.community.bmo.roomRenovationDetail.ISaveRoomRenovationDetailBMO;
import com.java110.community.bmo.roomRenovationDetail.IUpdateRoomRenovationDetailBMO;
import com.java110.dto.roomRenovation.RoomRenovationDto;
import com.java110.dto.roomRenovationDetail.RoomRenovationDetailDto;
import com.java110.po.roomRenovation.RoomRenovationPo;
import com.java110.po.roomRenovationDetail.RoomRenovationDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Autowired
    private ISaveRoomRenovationDetailBMO saveRoomRenovationDetailBMOImpl;
    @Autowired
    private IUpdateRoomRenovationDetailBMO updateRoomRenovationDetailBMOImpl;
    @Autowired
    private IDeleteRoomRenovationDetailBMO deleteRoomRenovationDetailBMOImpl;

    @Autowired
    private IGetRoomRenovationDetailBMO getRoomRenovationDetailBMOImpl;

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


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/saveRoomRenovationDetail
     * @path /app/roomRenovation/saveRoomRenovationDetail
     */
    @RequestMapping(value = "/saveRoomRenovationDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveRoomRenovationDetail(
            @RequestHeader(value = "user-id") String userId,
            @RequestHeader(value = "user-name") String userName,
            @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "rId", "请求报文中未包含rId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "detailType", "请求报文中未包含detailType");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含remark");

        RoomRenovationDetailPo roomRenovationDetailPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationDetailPo.class);
        roomRenovationDetailPo.setStaffId(userId);
        roomRenovationDetailPo.setStaffName(userName);
        return saveRoomRenovationDetailBMOImpl.save(roomRenovationDetailPo);
    }


    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /roomRenovation/deleteRoomRenovationDetail
     * @path /app/roomRenovation/deleteRoomRenovationDetail
     */
    @RequestMapping(value = "/deleteRoomRenovationDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRoomRenovationDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        RoomRenovationDetailPo roomRenovationDetailPo = BeanConvertUtil.covertBean(reqJson, RoomRenovationDetailPo.class);
        return deleteRoomRenovationDetailBMOImpl.delete(roomRenovationDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /roomRenovation/queryRoomRenovationDetail
     * @path /app/roomRenovation/queryRoomRenovationDetail
     */
    @RequestMapping(value = "/queryRoomRenovationDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryRoomRenovationDetail(@RequestParam(value = "communityId") String communityId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row,
                                                            @RequestParam(value = "rId") String rId) {
        RoomRenovationDetailDto roomRenovationDetailDto = new RoomRenovationDetailDto();
        roomRenovationDetailDto.setPage(page);
        roomRenovationDetailDto.setRow(row);
        roomRenovationDetailDto.setCommunityId(communityId);
        roomRenovationDetailDto.setrId(rId);
        return getRoomRenovationDetailBMOImpl.get(roomRenovationDetailDto);
    }
}
