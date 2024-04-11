package com.java110.job.smo.impl;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.log.LoggerFactory;
import com.java110.intf.job.IIotInnerServiceSMO;
import com.java110.job.adapt.hcIotNew.http.ISendIot;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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


    @Override
    public ResultVo postIot(@RequestBody JSONObject paramIn) {
        ResultVo resultVo = sendIotImpl.post("/iot/api/common.openCommonApi", paramIn);
        return resultVo;
    }

}
