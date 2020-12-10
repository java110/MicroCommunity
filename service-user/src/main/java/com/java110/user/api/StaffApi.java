package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.po.staffAppAuth.StaffAppAuthPo;
import com.java110.user.bmo.staffAppAuth.IDeleteStaffAppAuthBMO;
import com.java110.user.bmo.staffAppAuth.IGetStaffAppAuthBMO;
import com.java110.user.bmo.staffAppAuth.ISaveStaffAppAuthBMO;
import com.java110.user.bmo.staffAppAuth.IUpdateStaffAppAuthBMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/staff")
public class StaffApi {

    @Autowired
    private ISaveStaffAppAuthBMO saveStaffAppAuthBMOImpl;
    @Autowired
    private IUpdateStaffAppAuthBMO updateStaffAppAuthBMOImpl;
    @Autowired
    private IDeleteStaffAppAuthBMO deleteStaffAppAuthBMOImpl;

    @Autowired
    private IGetStaffAppAuthBMO getStaffAppAuthBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /staff/saveStaffAppAuth
     * @path /app/staff/saveStaffAppAuth
     */
    @RequestMapping(value = "/saveStaffAppAuth", method = RequestMethod.POST)
    public ResponseEntity<String> saveStaffAppAuth(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "appType", "请求报文中未包含appType");


        StaffAppAuthPo staffAppAuthPo = BeanConvertUtil.covertBean(reqJson, StaffAppAuthPo.class);
        return saveStaffAppAuthBMOImpl.save(staffAppAuthPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /staff/updateStaffAppAuth
     * @path /app/staff/updateStaffAppAuth
     */
    @RequestMapping(value = "/updateStaffAppAuth", method = RequestMethod.POST)
    public ResponseEntity<String> updateStaffAppAuth(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "appType", "请求报文中未包含appType");
        Assert.hasKeyAndValue(reqJson, "auId", "auId不能为空");


        StaffAppAuthPo staffAppAuthPo = BeanConvertUtil.covertBean(reqJson, StaffAppAuthPo.class);
        return updateStaffAppAuthBMOImpl.update(staffAppAuthPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /staff/deleteStaffAppAuth
     * @path /app/staff/deleteStaffAppAuth
     */
    @RequestMapping(value = "/deleteStaffAppAuth", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStaffAppAuth(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "auId", "auId不能为空");


        StaffAppAuthPo staffAppAuthPo = BeanConvertUtil.covertBean(reqJson, StaffAppAuthPo.class);
        return deleteStaffAppAuthBMOImpl.delete(staffAppAuthPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /staff/queryStaffAppAuth
     * @path /app/staff/queryStaffAppAuth
     */
    @RequestMapping(value = "/queryStaffAppAuth", method = RequestMethod.GET)
    public ResponseEntity<String> queryStaffAppAuth(@RequestHeader(value = "store-id") String storeId,
                                                    @RequestHeader(value = "user-id") String userId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setPage(page);
        staffAppAuthDto.setRow(row);
        staffAppAuthDto.setStoreId(storeId);
        staffAppAuthDto.setStaffId(userId);
        return getStaffAppAuthBMOImpl.get(staffAppAuthDto);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /staff/generatorQrCode
     * @path /app/staff/generatorQrCode
     */
    @RequestMapping(value = "/generatorQrCode", method = RequestMethod.GET)
    public ResponseEntity<String> generatorQrCode(@RequestHeader(value = "store-id") String storeId,
                                                  @RequestHeader(value = "user-id") String userId,
                                                  @RequestParam(value = "communityId") String communityId) {
        String ownerUrl = MappingCache.getValue("OWNER_WECHAT_URL")
                + "/app/staffAuth?storeId=" + storeId + "&staffId=" + userId
                + "&communityId=" + communityId;
        return ResultVo.createResponseEntity(ownerUrl);
    }
}
