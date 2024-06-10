package com.java110.job.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.MallDataDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.job.IMallInnerServiceSMO;
import com.java110.job.adapt.hcIot.IotConstant;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.job.mall.ISendMall;
import com.java110.utils.cache.MappingCache;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 定时任务属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MallInnerServiceSMOImpl extends BaseServiceSMO implements IMallInnerServiceSMO {
    private static final Logger logger = LoggerFactory.getLogger(MallInnerServiceSMOImpl.class);

    private static final String MALL_DOMAIN = "MALL";


    @Autowired
    private ISendMall sendMallImpl;


    @Override
    public ResultVo postMall(@RequestBody JSONObject paramIn) {
        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            return new ResultVo(ResultVo.CODE_OK,ResultVo.MSG_OK);
        }

        ResultVo resultVo = sendMallImpl.post("/mall/api/common.openCommonApi", paramIn);
        return resultVo;
    }

    @Override
    public ResultVo postMallData(@RequestBody MallDataDto mallDataDto) {

        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            return new ResultVo(ResultVo.CODE_OK,ResultVo.MSG_OK);
        }


        JSONObject paramIn = mallDataDto.getData();
        paramIn.put("mallApiCode", mallDataDto.getMallApiCode());
        ResultVo resultVo = sendMallImpl.post("/mall/api/common.openCommonApi", paramIn);
        return resultVo;
    }

    @Override
    public String generatorMallCode(@RequestBody UserDto userDto) {

        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
            throw new IllegalArgumentException("未部署商城系统");
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("userId", userDto.getUserId());
        paramIn.put("tel", userDto.getTel());
        paramIn.put("passwd", userDto.getPassword());
        paramIn.put("userName", userDto.getName());
        paramIn.put("address", userDto.getAddress());

        ResultVo resultVo = sendMallImpl.post("/mall/api/token.generatorCode", paramIn);

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            throw new IllegalArgumentException(resultVo.getMsg());
        }

        return resultVo.getData().toString();
    }

    @Override
    public ResultVo sendUserInfo(@RequestBody UserDto userDto) {

        String mallSwitch = MappingCache.getValue(MALL_DOMAIN, "MALL_SWITCH");

        if (!"ON".equals(mallSwitch)) {
           return new ResultVo(ResultVo.CODE_OK,ResultVo.MSG_OK);
        }

        JSONObject paramIn = new JSONObject();
        paramIn.put("userId", userDto.getUserId());
        paramIn.put("tel", userDto.getTel());
        paramIn.put("passwd", userDto.getPassword());
        paramIn.put("userName", userDto.getName());
        paramIn.put("address", userDto.getAddress());

        ResultVo resultVo = sendMallImpl.post("/mall/api/token.generatorCode", paramIn);

        return resultVo;
    }

}
