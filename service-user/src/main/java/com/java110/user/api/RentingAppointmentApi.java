package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.dto.store.StoreDto;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import com.java110.user.bmo.rentingAppointment.*;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 租赁 预约接口类 controller
 * <p>
 * add by 吴学文
 */
@RestController
@RequestMapping(value = "/rentingAppointment")
public class RentingAppointmentApi {

    @Autowired
    private ISaveRentingAppointmentBMO saveRentingAppointmentBMOImpl;
    @Autowired
    private IUpdateRentingAppointmentBMO updateRentingAppointmentBMOImpl;
    @Autowired
    private IDeleteRentingAppointmentBMO deleteRentingAppointmentBMOImpl;

    @Autowired
    private IGetRentingAppointmentBMO getRentingAppointmentBMOImpl;

    @Autowired
    private IConfirmRentingBMO confirmRentingBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingAppointment/saveRentingAppointment
     * @path /app/rentingAppointment/saveRentingAppointment
     */
    @RequestMapping(value = "/saveRentingAppointment", method = RequestMethod.POST)
    public ResponseEntity<String> saveRentingAppointment(@RequestBody JSONObject reqJson,
                                                         @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "tenantName", "请求报文中未包含tenantName");
        Assert.hasKeyAndValue(reqJson, "tenantSex", "请求报文中未包含tenantSex");
        Assert.hasKeyAndValue(reqJson, "tenantTel", "请求报文中未包含tenantTel");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "请求报文中未包含appointmentTime");


        RentingAppointmentPo rentingAppointmentPo = BeanConvertUtil.covertBean(reqJson, RentingAppointmentPo.class);
        rentingAppointmentPo.setStoreId(storeId);
        return saveRentingAppointmentBMOImpl.save(rentingAppointmentPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingAppointment/updateRentingAppointment
     * @path /app/rentingAppointment/updateRentingAppointment
     */
    @RequestMapping(value = "/updateRentingAppointment", method = RequestMethod.POST)
    public ResponseEntity<String> updateRentingAppointment(@RequestBody JSONObject reqJson) {

//        Assert.hasKeyAndValue(reqJson, "tenantName", "请求报文中未包含tenantName");
//        Assert.hasKeyAndValue(reqJson, "tenantSex", "请求报文中未包含tenantSex");
//        Assert.hasKeyAndValue(reqJson, "tenantTel", "请求报文中未包含tenantTel");
//        Assert.hasKeyAndValue(reqJson, "appointmentTime", "请求报文中未包含appointmentTime");
        Assert.hasKeyAndValue(reqJson, "appointmentId", "appointmentId不能为空");


        RentingAppointmentPo rentingAppointmentPo = BeanConvertUtil.covertBean(reqJson, RentingAppointmentPo.class);
        return updateRentingAppointmentBMOImpl.update(rentingAppointmentPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingAppointment/deleteRentingAppointment
     * @path /app/rentingAppointment/deleteRentingAppointment
     */
    @RequestMapping(value = "/deleteRentingAppointment", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRentingAppointment(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "appointmentId", "appointmentId不能为空");


        RentingAppointmentPo rentingAppointmentPo = BeanConvertUtil.covertBean(reqJson, RentingAppointmentPo.class);
        return deleteRentingAppointmentBMOImpl.delete(rentingAppointmentPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /rentingAppointment/queryRentingAppointment
     * @path /app/rentingAppointment/queryRentingAppointment
     */
    @RequestMapping(value = "/queryRentingAppointment", method = RequestMethod.GET)
    public ResponseEntity<String> queryRentingAppointment(@RequestHeader(value = "store-id") String storeId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row,
                                                          @RequestParam(value = "state", required = false) String state,
                                                          @RequestParam(value = "tenantName", required = false) String tenantName,
                                                          @RequestParam(value = "tenantTel", required = false) String tenantTel
    ) {
        RentingAppointmentDto rentingAppointmentDto = new RentingAppointmentDto();
        rentingAppointmentDto.setPage(page);
        rentingAppointmentDto.setRow(row);
        if (!StoreDto.STORE_ADMIN.equals(storeId)) {
            rentingAppointmentDto.setStoreId(storeId);
        }
        rentingAppointmentDto.setState(state);
        rentingAppointmentDto.setTenantName(tenantName);
        rentingAppointmentDto.setTenantTel(tenantTel);
        return getRentingAppointmentBMOImpl.get(rentingAppointmentDto);
    }


    /**
     * 确认租房
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingAppointment/confirmRenting
     * @path /app/rentingAppointment/confirmRenting
     */
    @RequestMapping(value = "/confirmRenting", method = RequestMethod.POST)
    public ResponseEntity<String> confirmRenting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "appointmentId", "appointmentId不能为空");
        Assert.hasKeyAndValue(reqJson, "rentingId", "rentingId不能为空");

        RentingAppointmentPo rentingAppointmentPo = BeanConvertUtil.covertBean(reqJson, RentingAppointmentPo.class);
        return confirmRentingBMOImpl.confirm(rentingAppointmentPo);
    }


}
