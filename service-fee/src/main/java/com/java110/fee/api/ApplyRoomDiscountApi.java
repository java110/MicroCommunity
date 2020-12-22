package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountDto;
import com.java110.dto.applyRoomDiscountType.ApplyRoomDiscountTypeDto;
import com.java110.fee.bmo.applyRoomDiscount.IAuditApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IDeleteApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IGetApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.ISaveApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscount.IUpdateApplyRoomDiscountBMO;
import com.java110.fee.bmo.applyRoomDiscountType.IDeleteApplyRoomDiscountTypeBMO;
import com.java110.fee.bmo.applyRoomDiscountType.IGetApplyRoomDiscountTypeBMO;
import com.java110.fee.bmo.applyRoomDiscountType.ISaveApplyRoomDiscountTypeBMO;
import com.java110.fee.bmo.applyRoomDiscountType.IUpdateApplyRoomDiscountTypeBMO;
import com.java110.po.applyRoomDiscount.ApplyRoomDiscountPo;
import com.java110.po.applyRoomDiscountType.ApplyRoomDiscountTypePo;
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

    @Autowired
    private IAuditApplyRoomDiscountBMO auditApplyRoomDiscountBMOImpl;

    @Autowired
    private ISaveApplyRoomDiscountTypeBMO saveApplyRoomDiscountTypeBMOImpl;
    @Autowired
    private IUpdateApplyRoomDiscountTypeBMO updateApplyRoomDiscountTypeBMOImpl;
    @Autowired
    private IDeleteApplyRoomDiscountTypeBMO deleteApplyRoomDiscountTypeBMOImpl;

    @Autowired
    private IGetApplyRoomDiscountTypeBMO getApplyRoomDiscountTypeBMOImpl;

    /**
     * 优惠申请
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
     * 验房接口
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/updateApplyRoomDiscount
     * @path /app/applyRoomDiscount/updateApplyRoomDiscount
     */
    @RequestMapping(value = "/updateApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> updateApplyRoomDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含验房状态");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "checkRemark", "请求报文中未包含验房说明");
        Assert.hasKeyAndValue(reqJson, "discountId", "请求报文中未包含折扣");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");


        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return updateApplyRoomDiscountBMOImpl.update(applyRoomDiscountPo);
    }

    /**
     * 验房接口
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/auditApplyRoomDiscount
     * @path /app/applyRoomDiscount/auditApplyRoomDiscount
     */
    @RequestMapping(value = "/auditApplyRoomDiscount", method = RequestMethod.POST)
    public ResponseEntity<String> auditApplyRoomDiscount(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含验房状态");
        Assert.hasKeyAndValue(reqJson, "reviewRemark", "请求报文中未包含审核说明");
        Assert.hasKeyAndValue(reqJson, "ardId", "ardId不能为空");


        ApplyRoomDiscountPo applyRoomDiscountPo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountPo.class);
        return auditApplyRoomDiscountBMOImpl.audit(applyRoomDiscountPo);
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
     * 查询优惠申请
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


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/saveApplyRoomDiscountType
     * @path /app/applyRoomDiscount/saveApplyRoomDiscountType
     */
    @RequestMapping(value = "/saveApplyRoomDiscountType", method = RequestMethod.POST)
    public ResponseEntity<String> saveApplyRoomDiscountType(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");


        ApplyRoomDiscountTypePo applyRoomDiscountTypePo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountTypePo.class);
        return saveApplyRoomDiscountTypeBMOImpl.save(applyRoomDiscountTypePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/updateApplyRoomDiscountType
     * @path /app/applyRoomDiscount/updateApplyRoomDiscountType
     */
    @RequestMapping(value = "/updateApplyRoomDiscountType", method = RequestMethod.POST)
    public ResponseEntity<String> updateApplyRoomDiscountType(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "applyType", "applyType不能为空");


        ApplyRoomDiscountTypePo applyRoomDiscountTypePo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountTypePo.class);
        return updateApplyRoomDiscountTypeBMOImpl.update(applyRoomDiscountTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /applyRoomDiscount/deleteApplyRoomDiscountType
     * @path /app/applyRoomDiscount/deleteApplyRoomDiscountType
     */
    @RequestMapping(value = "/deleteApplyRoomDiscountType", method = RequestMethod.POST)
    public ResponseEntity<String> deleteApplyRoomDiscountType(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "applyType", "applyType不能为空");


        ApplyRoomDiscountTypePo applyRoomDiscountTypePo = BeanConvertUtil.covertBean(reqJson, ApplyRoomDiscountTypePo.class);
        return deleteApplyRoomDiscountTypeBMOImpl.delete(applyRoomDiscountTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /applyRoomDiscount/queryApplyRoomDiscountType
     * @path /app/applyRoomDiscount/queryApplyRoomDiscountType
     */
    @RequestMapping(value = "/queryApplyRoomDiscountType", method = RequestMethod.GET)
    public ResponseEntity<String> queryApplyRoomDiscountType(@RequestParam(value = "communityId") String communityId,
                                                             @RequestParam(value = "applyType", required = false) String applyType,
                                                             @RequestParam(value = "typeName", required = false) String typeName,
                                                             @RequestParam(value = "page") int page,
                                                             @RequestParam(value = "row") int row) {
        ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto = new ApplyRoomDiscountTypeDto();
        applyRoomDiscountTypeDto.setPage(page);
        applyRoomDiscountTypeDto.setRow(row);
        applyRoomDiscountTypeDto.setCommunityId(communityId);
        applyRoomDiscountTypeDto.setApplyType(applyType);
        applyRoomDiscountTypeDto.setTypeName(typeName);
        return getApplyRoomDiscountTypeBMOImpl.get(applyRoomDiscountTypeDto);
    }
}
