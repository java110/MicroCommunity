package com.java110.job.smo.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.IotDataDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.java110.job.adapt.hcIot.http.ISendIot;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 定时任务属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class IotInnerServiceSMOImpl extends BaseServiceSMO implements IIotInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(IotInnerServiceSMOImpl.class);


    @Autowired
    private ISendIot sendIotImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;


    @Override
    public ResultVo postIot(@RequestBody JSONObject paramIn) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return new ResultVo(ResultVo.CODE_ERROR, "未部署IOT系统");
        }
        ResultVo resultVo = sendIotImpl.post("/iot/api/common.openCommonApi", paramIn);
        return resultVo;
    }

    @Override
    public ResultVo postIotData(@RequestBody IotDataDto iotDataDto) {

        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return new ResultVo(ResultVo.CODE_ERROR, "未部署IOT系统");
        }

        JSONObject paramIn = iotDataDto.getData();
        paramIn.put("iotApiCode", iotDataDto.getIotApiCode());
        ResultVo resultVo = sendIotImpl.post("/iot/api/common.openCommonApi", paramIn);
        return resultVo;
    }


    @Override
    public ResultVo sendUserInfo(@RequestBody UserDto userDto) {

        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");

        if (!"ON".equals(iotSwitch)) {
            return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("userId", userDto.getUserId());
        paramIn.put("tel", userDto.getTel());
        paramIn.put("passwd", userDto.getPassword());
        paramIn.put("userName", userDto.getName());
        paramIn.put("address", userDto.getAddress());

        //todo 查询 房屋认证信息也同步到物联网

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userDto.getUserId());
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        JSONArray appUsers = new JSONArray();
        JSONObject appUser = null;
        if(!ListUtil.isNull(ownerAppUserDtos)){
            for(OwnerAppUserDto tmpOwnerAppUserDto:ownerAppUserDtos){
                if(StringUtil.isEmpty(tmpOwnerAppUserDto.getMemberId())){
                    continue;
                }
                if(tmpOwnerAppUserDto.getMemberId().startsWith("-")){
                    continue;
                }

                if(StringUtil.isEmpty(tmpOwnerAppUserDto.getRoomId())){
                    continue;
                }

                if(StringUtil.isEmpty(tmpOwnerAppUserDto.getRoomName())){
                    continue;
                }

                appUser = new JSONObject();
                appUser.put("ownerId",tmpOwnerAppUserDto.getMemberId());
                appUser.put("ownerName",tmpOwnerAppUserDto.getAppUserName());
                appUser.put("roomId",tmpOwnerAppUserDto.getRoomId());
                appUser.put("roomName",tmpOwnerAppUserDto.getRoomName());
                appUser.put("communityId",tmpOwnerAppUserDto.getCommunityId());
                appUser.put("communityName",tmpOwnerAppUserDto.getCommunityName());
                appUsers.add(appUser);
            }
            paramIn.put("appUsers", appUsers);
        }

        ResultVo resultVo = sendIotImpl.post("/iot/api/owner.transforOwnerUser", paramIn);

        return resultVo;
    }

}
