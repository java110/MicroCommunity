package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.userAddress.UserAddressDto;
import com.java110.po.userAddress.UserAddressPo;
import com.java110.user.bmo.userAddress.IDeleteUserAddressBMO;
import com.java110.user.bmo.userAddress.IGetUserAddressBMO;
import com.java110.user.bmo.userAddress.ISaveUserAddressBMO;
import com.java110.user.bmo.userAddress.IUpdateUserAddressBMO;
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
@RequestMapping(value = "/userAddress")
public class UserAddressApi {

    @Autowired
    private ISaveUserAddressBMO saveUserAddressBMOImpl;
    @Autowired
    private IUpdateUserAddressBMO updateUserAddressBMOImpl;
    @Autowired
    private IDeleteUserAddressBMO deleteUserAddressBMOImpl;

    @Autowired
    private IGetUserAddressBMO getUserAddressBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userAddress/saveUserAddress
     * @path /app/userAddress/saveUserAddress
     */
    @RequestMapping(value = "/saveUserAddress", method = RequestMethod.POST)
    public ResponseEntity<String> saveUserAddress(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");
        Assert.hasKeyAndValue(reqJson, "areaCode", "请求报文中未包含areaCode");
        Assert.hasKeyAndValue(reqJson, "postalCode", "请求报文中未包含postalCode");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "isDefault", "请求报文中未包含isDefault");

        UserAddressPo userAddressPo = BeanConvertUtil.covertBean(reqJson, UserAddressPo.class);
        userAddressPo.setbId("-1");
        return saveUserAddressBMOImpl.save(userAddressPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userAddress/updateUserAddress
     * @path /app/userAddress/updateUserAddress
     */
    @RequestMapping(value = "/updateUserAddress", method = RequestMethod.POST)
    public ResponseEntity<String> updateUserAddress(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");
        Assert.hasKeyAndValue(reqJson, "areaCode", "请求报文中未包含areaCode");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含address");
        Assert.hasKeyAndValue(reqJson, "isDefault", "请求报文中未包含isDefault");
        Assert.hasKeyAndValue(reqJson, "addressId", "addressId不能为空");


        UserAddressPo userAddressPo = BeanConvertUtil.covertBean(reqJson, UserAddressPo.class);
        return updateUserAddressBMOImpl.update(userAddressPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userAddress/deleteUserAddress
     * @path /app/userAddress/deleteUserAddress
     */
    @RequestMapping(value = "/deleteUserAddress", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUserAddress(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "addressId", "addressId不能为空");
        Assert.hasKeyAndValue(reqJson, "userId", "用户ID不能为空");


        UserAddressPo userAddressPo = BeanConvertUtil.covertBean(reqJson, UserAddressPo.class);
        return deleteUserAddressBMOImpl.delete(userAddressPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param userId 用户ID
     * @return
     * @serviceCode /userAddress/queryUserAddress
     * @path /app/userAddress/queryUserAddress
     */
    @RequestMapping(value = "/queryUserAddress", method = RequestMethod.GET)
    public ResponseEntity<String> queryUserAddress(@RequestParam(value = "userId") String userId,
                                                   @RequestParam(value = "addressId", required = false) String addressId,
                                                   @RequestParam(value = "isDefault", required = false) String isDefault,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        UserAddressDto userAddressDto = new UserAddressDto();
        userAddressDto.setPage(page);
        userAddressDto.setRow(row);
        userAddressDto.setUserId(userId);
        userAddressDto.setAddressId(addressId);
        userAddressDto.setIsDefault(isDefault);
        return getUserAddressBMOImpl.get(userAddressDto);
    }
}
