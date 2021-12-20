package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.user.UserDto;
import com.java110.dto.userLogin.UserLoginDto;
import com.java110.po.userLogin.UserLoginPo;
import com.java110.user.bmo.userLogin.IDeleteUserLoginBMO;
import com.java110.user.bmo.userLogin.IGetUserLoginBMO;
import com.java110.user.bmo.userLogin.ISaveUserLoginBMO;
import com.java110.user.bmo.userLogin.IUpdateUserLoginBMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/userLogin")
public class UserLoginApi {

    @Autowired
    private ISaveUserLoginBMO saveUserLoginBMOImpl;
    @Autowired
    private IUpdateUserLoginBMO updateUserLoginBMOImpl;
    @Autowired
    private IDeleteUserLoginBMO deleteUserLoginBMOImpl;

    @Autowired
    private IGetUserLoginBMO getUserLoginBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userLogin/saveUserLogin
     * @path /app/userLogin/saveUserLogin
     */
    @RequestMapping(value = "/saveUserLogin", method = RequestMethod.POST)
    public ResponseEntity<String> saveUserLogin(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");
        Assert.hasKeyAndValue(reqJson, "userName", "请求报文中未包含userName");
        Assert.hasKeyAndValue(reqJson, "password", "请求报文中未包含password");


        UserLoginPo userLoginPo = BeanConvertUtil.covertBean(reqJson, UserLoginPo.class);
        return saveUserLoginBMOImpl.save(userLoginPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userLogin/updateUserLogin
     * @path /app/userLogin/updateUserLogin
     */
    @RequestMapping(value = "/updateUserLogin", method = RequestMethod.POST)
    public ResponseEntity<String> updateUserLogin(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "userId", "请求报文中未包含userId");
        Assert.hasKeyAndValue(reqJson, "userName", "请求报文中未包含userName");
        Assert.hasKeyAndValue(reqJson, "password", "请求报文中未包含password");
        Assert.hasKeyAndValue(reqJson, "loginId", "loginId不能为空");


        UserLoginPo userLoginPo = BeanConvertUtil.covertBean(reqJson, UserLoginPo.class);
        return updateUserLoginBMOImpl.update(userLoginPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userLogin/deleteUserLogin
     * @path /app/userLogin/deleteUserLogin
     */
    @RequestMapping(value = "/deleteUserLogin", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUserLogin(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "loginId", "loginId不能为空");


        UserLoginPo userLoginPo = BeanConvertUtil.covertBean(reqJson, UserLoginPo.class);
        return deleteUserLoginBMOImpl.delete(userLoginPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param userName 用户名称
     * @return
     * @serviceCode /userLogin/queryUserLogin
     * @path /app/userLogin/queryUserLogin
     */
    @RequestMapping(value = "/queryUserLogin", method = RequestMethod.GET)
    public ResponseEntity<String> queryUserLogin(@RequestParam(value = "userName", required = false) String userName,
                                                 @RequestParam(value = "parentOrgName", required = false) String parentOrgName,
                                                 @RequestParam(value = "orgName", required = false) String orgName,
                                                 @RequestParam(value = "page") int page,
                                                 @RequestParam(value = "row") int row) {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setPage(page);
        userLoginDto.setRow(row);
        userLoginDto.setUserName(userName);
        userLoginDto.setParentOrgName(parentOrgName);
        userLoginDto.setOrgName(orgName);
        return getUserLoginBMOImpl.get(userLoginDto);
    }

    /**
     * 根据code 查询用户信息
     *
     * @param hcCode
     * @serviceCode /userLogin/getUserInfoByHcCode
     * @return
     */
    @RequestMapping(value = "/getUserInfoByHcCode", method = RequestMethod.GET)
    public ResponseEntity<String> getUserInfoByHcCode(@RequestParam(value = "hcCode") String hcCode) {
        UserDto userDto = null;
//        String env = MappingCache.getValue("HC_ENV");
//        if ("DEV".equals(env) || "TEST".equals(env)) {
//            userDto = new UserDto();
//            userDto.setUserId("1000000020021010001");
//            userDto.setUserName("吴学文");
//            userDto.setAddress("青海省西宁市城中区申宁路");
//            userDto.setAge(30);
//            userDto.setEmail("928255095@qq.com");
//            userDto.setName("吴学文");
//            userDto.setSex("1");
//            userDto.setTel("18909711443");
//            return ResultVo.createResponseEntity(userDto);
//        }
        String userInfoStr = CommonCache.getAndRemoveValue(hcCode);
        userDto = JSONObject.parseObject(userInfoStr, UserDto.class);
        return ResultVo.createResponseEntity(userDto);
    }

    /**
     * 生成code
     *
     * @param userId
     * @serviceCode /userLogin/generatorHcCode
     * @return
     */
    @RequestMapping(value = "/generatorHcCode", method = RequestMethod.GET)
    public ResponseEntity<String> generatorHcCode(@RequestHeader(value = "user-id") String userId) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        return  getUserLoginBMOImpl.generatorHcCode(userDto);

    }
    /**
     * 生成code
     *
     * @param userId
     * @serviceCode /userLogin/deleteOpenId
     * @return
     */
    @RequestMapping(value = "/deleteOpenId", method = RequestMethod.POST)
    public ResponseEntity<String> deleteOpenId(@RequestParam(value = "userId", required = false) String userId) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        return  deleteUserLoginBMOImpl.deleteOpenId(userDto);

    }
}
