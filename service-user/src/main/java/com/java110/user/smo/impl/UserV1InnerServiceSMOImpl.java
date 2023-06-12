/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.java110.user.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.user.dao.IUserV1ServiceDao;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.po.user.UserPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2021-09-13 08:40:33 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class UserV1InnerServiceSMOImpl extends BaseServiceSMO implements IUserV1InnerServiceSMO {

    @Autowired
    private IUserV1ServiceDao userV1ServiceDaoImpl;


    @Override
    public int saveUser(@RequestBody UserPo userPo) {
        int saveFlag = userV1ServiceDaoImpl.saveUserInfo(BeanConvertUtil.beanCovertMap(userPo));
        return saveFlag;
    }

    @Override
    public int updateUser(@RequestBody UserPo userPo) {
        int saveFlag = userV1ServiceDaoImpl.updateUserInfo(BeanConvertUtil.beanCovertMap(userPo));
        return saveFlag;
    }

    @Override
    public int deleteUser(@RequestBody UserPo userPo) {
        userPo.setStatusCd("1");
        int saveFlag = userV1ServiceDaoImpl.updateUserInfo(BeanConvertUtil.beanCovertMap(userPo));
        return saveFlag;
    }

    @Override
    public List<UserDto> queryUsers(@RequestBody UserDto userDto) {

        //校验是否传了 分页信息

        int page = userDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userDto.setPage((page - 1) * userDto.getRow());
        }

        List<UserDto> users = BeanConvertUtil.covertBeanList(userV1ServiceDaoImpl.getUserInfo(BeanConvertUtil.beanCovertMap(userDto)), UserDto.class);

        return users;
    }


    @Override
    public int queryUsersCount(@RequestBody UserDto userDto) {
        return userV1ServiceDaoImpl.queryUsersCount(BeanConvertUtil.beanCovertMap(userDto));
    }

    @Override
    public int queryStaffsNoInOrgCount(@RequestBody UserDto userDto) {
        return userV1ServiceDaoImpl.queryStaffsNoInOrgCount(BeanConvertUtil.beanCovertMap(userDto));
    }

    @Override
    public List<UserDto> queryStaffsNoInOrg(@RequestBody UserDto userDto) {
        //校验是否传了 分页信息

        int page = userDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userDto.setPage((page - 1) * userDto.getRow());
        }

        List<UserDto> users = BeanConvertUtil.covertBeanList(userV1ServiceDaoImpl.queryStaffsNoInOrg(BeanConvertUtil.beanCovertMap(userDto)), UserDto.class);

        return users;
    }

    @Override
    public String getUserIdByQrCode(@RequestBody String qrCode) {
        qrCode = CommonCache.getValue(qrCode);
        if(StringUtil.isEmpty(qrCode)){
            throw new IllegalArgumentException("二维码失效");
        }
        qrCode = AuthenticationFactory.AesDecrypt(qrCode, AuthenticationFactory.AES_KEY);
        JSONObject qrCodeJson = JSONObject.parseObject(qrCode);
        if (qrCodeJson == null || !qrCodeJson.containsKey("time")) {
            throw new IllegalArgumentException("二维码非法");
        }
        long time = qrCodeJson.getLongValue("time");
        if (DateUtil.getCurrentDate().getTime() - time > 5 * 60 * 1000) {
            throw new IllegalArgumentException("二维码失效");
        }

        return qrCodeJson.getString("userId");
    }

    @Override
    public String generatorUserIdQrCode(@RequestBody String userId) {
        JSONObject qrCodeJson = new JSONObject();
        qrCodeJson.put("userId", userId);
        qrCodeJson.put("time", DateUtil.getCurrentDate().getTime());
        String qrCode = AuthenticationFactory.AesEncrypt(qrCodeJson.toJSONString(), AuthenticationFactory.AES_KEY);
        String key = GenerateCodeFactory.getUUID();
        CommonCache.setValue(key, qrCode, CommonCache.defaultExpireTime);
        return key;
    }

}
