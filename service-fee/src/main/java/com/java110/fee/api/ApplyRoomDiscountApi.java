package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.fee.bmo.applyRoomDiscount.IDeleteApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IGetApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.ISaveApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IUpdateApplyRoomDiscountBMO;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
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
@RequestMapping(value = "/applyRoomDiscount")
public class ApplyRoomDiscountApi {

    @Autowired
    private ISaveApplyRoomDiscountBMO saveApplyRoomDiscountBMOImpl;
    @Autowired
    private IUpdateApplyRoomDiscountBMO updateApplyRoomDiscountBMOImpl;
    @Autowired
    private IDeleteApplyRoomDiscountBMO deleteApplyRoomDiscountBMOImpl;

    @Autowired
    private IGetApplyRoomDiscountBMO getApplyRoomDiscountBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/saveApplyRoomDiscount
     * @path /app/applyRoomDiscount/saveApplyRoomDiscount
     */
    @RequestMapping(value = "/saveApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> saveApplyRoomDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "applyType", "请求报文中未包含applyType");


        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return saveApplyRoomDiscountBMOImpl.save(applyRoomDiscountPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/updateApplyRoomDiscount
     * @path /app/applyRoomDiscount/updateApplyRoomDiscount
     */
    @RequestMapping(value = "/updateApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> updateApplyRoomDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "roomName", "请求报文中未包含roomName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "applyType", "请求报文中未包含applyType");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");


        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return updateApplyRoomDiscountBMOImpl.update(applyRoomDiscountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/deleteApplyRoomDiscount
     * @path /app/applyRoomDiscount/deleteApplyRoomDiscount
     */
    @RequestMapping(value = "/deleteApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> deleteApplyRoomDiscount(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");


        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return deleteApplyRoomDiscountBMOImpl.delete(applyRoomDiscountPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /applyRoomDiscount/queryApplyRoomDiscount
     * @path /app/applyRoomDiscount/queryApplyRoomDiscount
     */
    @RequestMapping(value = "/queryApplyRoomDiscount", method = RequestMethod.GET)
    public ResponseEntity<String> queryApplyRoomDiscount(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "roomName", required = false) String roomName,
                                                         @RequestParam(value = "roomId", required = false) String roomId,
                                                         @RequestParam(value = "state", required = false) String state,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        ApplyRoomDiscountDto applyRoomDiscountDto = new ApplyRoomDiscountDto();
        applyRoomDiscountDto.setPage(page);
        applyRoomDiscountDto.setRow(row);
        applyRoomDiscountDto.setCommunityId(communityId);
        applyRoomDiscountDto.setRoomName(roomName);
        applyRoomDiscountDto.setRoomId(roomId);
        applyRoomDiscountDto.setState(state);
        return getApplyRoomDiscountBMOImpl.get(applyRoomDiscountDto);
    }
}
