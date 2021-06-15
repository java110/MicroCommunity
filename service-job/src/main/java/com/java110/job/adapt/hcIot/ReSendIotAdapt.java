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
package com.java110.job.adapt.hcIot;

import com.alibaba.fastjson.JSONObject;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重新同步
 *
 * 当设备信息失败是可以重新送HC物联系统
 *
 * 物联网系统 代码地址 https://gitee.com/java110/MicroCommunityThings
 *
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 15:29
 */
@Component(value = "reSendIotAdapt")
public class ReSendIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcMachineAsynImpl;

    @Override
    public ResultVo reSendToIot(JSONObject paramIn) {

        Assert.hasKeyAndValue(paramIn, "machineTranslateId", "同步ID");
        Assert.hasKeyAndValue(paramIn, "communityId", "小区ID");

        hcMachineAsynImpl.reSendIot(paramIn);

        return new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

}
