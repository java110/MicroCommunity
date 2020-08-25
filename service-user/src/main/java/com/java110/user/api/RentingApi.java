package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.rentingConfig.RentingConfigDto;
import com.java110.po.rentingConfig.RentingConfigPo;
import com.java110.user.bmo.rentingConfig.IDeleteRentingConfigBMO;
import com.java110.user.bmo.rentingConfig.IGetRentingConfigBMO;
import com.java110.user.bmo.rentingConfig.ISaveRentingConfigBMO;
import com.java110.user.bmo.rentingConfig.IUpdateRentingConfigBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/renting")
public class RentingApi {

    @Autowired
    private ISaveRentingConfigBMO saveRentingConfigBMOImpl;
    @Autowired
    private IUpdateRentingConfigBMO updateRentingConfigBMOImpl;
    @Autowired
    private IDeleteRentingConfigBMO deleteRentingConfigBMOImpl;

    @Autowired
    private IGetRentingConfigBMO getRentingConfigBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /renting/saveRentingConfig
     * @path /app/renting/saveRentingConfig
     */
    @RequestMapping(value = "/saveRentingConfig", method = RequestMethod.POST)
    public ResponseEntity<String> saveRentingConfig(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "rentingType", "请求报文中未包含rentingType");
        Assert.hasKeyAndValue(reqJson, "rentingFormula", "请求报文中未包含rentingFormula");
        Assert.hasKeyAndValue(reqJson, "servicePrice", "请求报文中未包含servicePrice");
        Assert.hasKeyAndValue(reqJson, "serviceOwnerRate", "请求报文中未包含serviceOwnerRate");
        Assert.hasKeyAndValue(reqJson, "serviceTenantRate", "请求报文中未包含serviceTenantRate");
        Assert.hasKeyAndValue(reqJson, "adminSeparateRate", "请求报文中未包含adminSeparateRate");
        Assert.hasKeyAndValue(reqJson, "proxySeparateRate", "请求报文中未包含proxySeparateRate");
        Assert.hasKeyAndValue(reqJson, "propertySeparateRate", "请求报文中未包含propertySeparateRate");


        RentingConfigPo rentingConfigPo = BeanConvertUtil.covertBean(reqJson, RentingConfigPo.class);
        return saveRentingConfigBMOImpl.save(rentingConfigPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /renting/updateRentingConfig
     * @path /app/renting/updateRentingConfig
     */
    @RequestMapping(value = "/updateRentingConfig", method = RequestMethod.POST)
    public ResponseEntity<String> updateRentingConfig(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "rentingType", "请求报文中未包含rentingType");
        Assert.hasKeyAndValue(reqJson, "rentingFormula", "请求报文中未包含rentingFormula");
        Assert.hasKeyAndValue(reqJson, "servicePrice", "请求报文中未包含servicePrice");
        Assert.hasKeyAndValue(reqJson, "serviceOwnerRate", "请求报文中未包含serviceOwnerRate");
        Assert.hasKeyAndValue(reqJson, "serviceTenantRate", "请求报文中未包含serviceTenantRate");
        Assert.hasKeyAndValue(reqJson, "adminSeparateRate", "请求报文中未包含adminSeparateRate");
        Assert.hasKeyAndValue(reqJson, "proxySeparateRate", "请求报文中未包含proxySeparateRate");
        Assert.hasKeyAndValue(reqJson, "propertySeparateRate", "请求报文中未包含propertySeparateRate");
        Assert.hasKeyAndValue(reqJson, "rentingConfigId", "rentingConfigId不能为空");


        RentingConfigPo rentingConfigPo = BeanConvertUtil.covertBean(reqJson, RentingConfigPo.class);
        return updateRentingConfigBMOImpl.update(rentingConfigPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /renting/deleteRentingConfig
     * @path /app/renting/deleteRentingConfig
     */
    @RequestMapping(value = "/deleteRentingConfig", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRentingConfig(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "rentingConfigId", "rentingConfigId不能为空");


        RentingConfigPo rentingConfigPo = BeanConvertUtil.covertBean(reqJson, RentingConfigPo.class);
        return deleteRentingConfigBMOImpl.delete(rentingConfigPo);
    }

    /**
     * 微信删除消息模板
     *
     * @return
     * @serviceCode /renting/queryRentingConfig
     * @path /app/renting/queryRentingConfig
     */
    @RequestMapping(value = "/queryRentingConfig", method = RequestMethod.GET)
    public ResponseEntity<String> queryRentingConfig(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        RentingConfigDto rentingConfigDto = new RentingConfigDto();
        rentingConfigDto.setPage(page);
        rentingConfigDto.setRow(row);
        return getRentingConfigBMOImpl.get(rentingConfigDto);
    }
}
