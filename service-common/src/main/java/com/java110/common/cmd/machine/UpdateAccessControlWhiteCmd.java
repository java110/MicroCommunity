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
package com.java110.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.smo.IPhotoSMO;
import com.java110.doc.annotation.*;
import com.java110.intf.common.IAccessControlWhiteV1InnerServiceSMO;
import com.java110.po.accessControlWhite.AccessControlWhitePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Java110CmdDoc(title = "修改门禁授权白名单",
        description = "主要用于员工，外卖和访客修改授权门禁白名单",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/machine.updateAccessControlWhite",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "machine.updateAccessControlWhite"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "acwId", length = 30, remark = "ID"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @Java110ParamDoc(name = "machineId", length = 30, remark = "门禁ID"),
        @Java110ParamDoc(name = "personName", length = 30, remark = "人员名称"),
        @Java110ParamDoc(name = "tel", length = 30, remark = "电话"),
        @Java110ParamDoc(name = "idCard", length = 30, remark = "身份证"),
        @Java110ParamDoc(name = "personType", length = 30, remark = "人员类型 1001\t员工\n" +
                "2002\t外卖人员\n" +
                "3003\t快递人员\n" +
                "4004\t访客人员\n" +
                "5005\t其他人员"),
        @Java110ParamDoc(name = "startTime", length = 30, remark = "开始时间 YYYY-MM-DD hh24:mi:ss"),
        @Java110ParamDoc(name = "endTime", length = 30, remark = "结束时间 YYYY-MM-DD hh24:mi:ss"),
        @Java110ParamDoc(name = "accessControlKey", length = 30, remark = "门禁卡号 非必填"),
        @Java110ParamDoc(name = "photo", length = 30, remark = "人脸 "),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"acwId\":\"xxx\",\"machineId\":\"102023012407190005\",\"personName\":\"张快递\",\"tel\":\"18909714562\",\"idCard\":\"\",\"personType\":\"3003\",\"startTime\":\"2023-01-05 02:10:00\",\"endTime\":\"2023-02-08 02:10:00\",\"accessControlKey\":\"\",\"photo\":\"https://java110.oss-cn-beijing.aliyuncs.com/hc/img/20230124/ec4cfb4f-4953-44f2-89ab-383dc955b005.jpg\",\"communityId\":\"2022121921870161\"}",
        resBody="{\"code\":0,\"msg\":\"成功\"}"
)
/**
 * 类表述：更新
 * 服务编码：machine.updateAccessControlWhite
 * 请求路劲：/app/machine.UpdateAccessControlWhite
 * add by 吴学文 at 2023-01-24 00:53:53 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.updateAccessControlWhite")
public class UpdateAccessControlWhiteCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateAccessControlWhiteCmd.class);


    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "acwId", "acwId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AccessControlWhitePo accessControlWhitePo = BeanConvertUtil.covertBean(reqJson, AccessControlWhitePo.class);
        int flag = accessControlWhiteV1InnerServiceSMOImpl.updateAccessControlWhite(accessControlWhitePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        photoSMOImpl.savePhoto(reqJson, accessControlWhitePo.getAcwId(), reqJson.getString("communityId"));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
