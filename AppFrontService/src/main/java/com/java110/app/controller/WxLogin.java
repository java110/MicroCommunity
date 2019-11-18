package com.java110.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.smo.wxLogin.IWxLoginSMO;
import com.java110.core.base.controller.BaseController;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.dto.wxLogin.UserInfo;
import com.java110.dto.wxLogin.WxLoginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序登录处理类
 */
@RestController
public class WxLogin extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(WxLogin.class);

    @Autowired
    private IWxLoginSMO wxLoginSMOImpl;


    /**
     * 微信登录接口
     *
     * @param wxLoginInfo
     * @param request
     */
    @RequestMapping(path = "/loginWx", method = RequestMethod.POST)
    public ResponseEntity<String> loginWx(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        ResponseEntity<String> responseEntity = null;
        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
            logger.error("code is null");
            responseEntity = new ResponseEntity<>("code is null", HttpStatus.BAD_REQUEST);
            return responseEntity;
        }
        String sessionKey = null;
        String openId = null;
//        try {
//            /*WxMaJscode2SessionResult result = this.wxMaService.getUserService().getSessionInfo(code);
//            sessionKey = result.getSessionKey();
//            openId = result.getOpenid();*/
//        } catch (Exception e) {
//            logger.error("login fail by wx", e);
//            e.printStackTrace();
//        }

        IPageData pd = PageData.newInstance().builder("","", JSONObject.toJSONString(wxLoginInfo),"","","","");

       return wxLoginSMOImpl.doLogin(pd);

        //login first
        /*User user = userService.queryByOpenid(openId);
        if (user == null) {
            user = new User();
            user.setUsername(openId);
            //Md5Hash md5Hash = new Md5Hash(openId, openId,1024);
            user.setPassword(openId); //密码保存加密的openid
            user.setWeixinOpenid(openId);
            user.setAvatar(userInfo.getAvatarUrl());
            user.setNickname(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setUserLevel((byte) 0); //普通用户
            user.setStatus((byte) 0); //0可用 1禁用 2注销
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            //user.setRoleid("2"); //默认普通用户
            userService.add(user);
        } else {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setSessionKey(sessionKey);
            //LogUtil.info("登录 " + user.toString());
            if (userService.updateById(user) == 0) {
                //LogUtil.error("update login user error");
                return ResponseUtil.updatedDataFailed();
            }
        }
        String token = UserTokenManager.generateToken(user.getId());
        JSONObject jsonObject = new JSONObject();
        //生成token
        jsonObject.put("errno", "0");
        jsonObject.put("token", token);
        jsonObject.put("userInfo", userInfo);
        LOGGER.info(jsonObject);
        //LogUtil.info("login success");
        //LogUtil.info(jsonObject);
        return ResponseUtil.ok(jsonObject);*/

        //return new ResponseEntity<>("",HttpStatus.OK);


    }

    /**
     * 微信登出
     *
     * @return
     */
   /* @RequestMapping("/logout")
    public JSONObject logoutWx() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseUtil.ok();
    }*/

}
