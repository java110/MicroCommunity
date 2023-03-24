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
package com.java110.store.cmd.property;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.doc.annotation.*;
import com.java110.dto.store.StoreAttrDto;
import com.java110.intf.store.IStoreAttrV1InnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.po.store.StoreAttrPo;
import com.java110.po.store.StorePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Java110CmdDoc(title = "修改物业公司",
        description = "主要提供给外系统修改物业公司",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/property.updateProperty",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "property.updateProperty",
        seq = 2
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "storeId", length = 30, remark = "物业编号"),
        @Java110ParamDoc(name = "name", length = 64, remark = "物业名称"),
        @Java110ParamDoc(name = "nearbyLandmarks", length = 64, remark = "地标，如xx 公园旁"),
        @Java110ParamDoc(name = "tel", length = 11, remark = "物业管理员电话 作为管理员账号密码 添加后请及时修改密码"),
        @Java110ParamDoc(name = "address", length = 11, remark = "公司地址"),
        @Java110ParamDoc(name = "corporation", length = 11, remark = "法人"),
        @Java110ParamDoc(name = "foundingTime", length = 11, remark = "成立日期"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@Java110ExampleDoc(
        reqBody="{\"storeId\":\"102022083062960025\",\"name\":\"培训物业公司\",\"address\":\"培训物业公司\",\"tel\":\"18909715555\",\"corporation\":\"无\",\"foundingTime\":\"2022-08-01\",\"nearByLandmarks\":\"123\"}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：更新
 * 服务编码：store.updateStore
 * 请求路劲：/app/store.UpdateStore
 * add by 吴学文 at 2022-02-28 10:46:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "property.updateProperty")
public class UpdatePropertyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdatePropertyCmd.class);


    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;


    @Autowired
    private IStoreAttrV1InnerServiceSMO storeAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "storeId", "storeId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        StorePo storePo = BeanConvertUtil.covertBean(reqJson, StorePo.class);
        int flag = storeV1InnerServiceSMOImpl.updateStore(storePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        if (reqJson.containsKey("corporation")) {

            StoreAttrDto storeAttrDto = new StoreAttrDto();
            storeAttrDto.setStoreId(reqJson.getString("storeId"));
            storeAttrDto.setSpecCd(StoreAttrDto.SPEC_CD_CORPORATION);
            List<StoreAttrDto> storeAttrDtos = storeAttrV1InnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
            if (storeAttrDtos == null || storeAttrDtos.size() < 1) {
                StoreAttrPo storeAttrPo = new StoreAttrPo();
                storeAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
                storeAttrPo.setSpecCd(StoreAttrDto.SPEC_CD_CORPORATION);
                storeAttrPo.setStoreId(storePo.getStoreId());
                storeAttrPo.setValue(reqJson.getString("corporation"));
                flag = storeAttrV1InnerServiceSMOImpl.saveStoreAttr(storeAttrPo);
            } else {
                StoreAttrPo storeAttrPo = new StoreAttrPo();
                storeAttrPo.setAttrId(storeAttrDtos.get(0).getAttrId());
                storeAttrPo.setSpecCd(StoreAttrDto.SPEC_CD_CORPORATION);
                storeAttrPo.setValue(reqJson.getString("corporation"));
                flag = storeAttrV1InnerServiceSMOImpl.updateStoreAttr(storeAttrPo);
            }

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }
        if (reqJson.containsKey("foundingTime")) {
            StoreAttrDto storeAttrDto = new StoreAttrDto();
            storeAttrDto.setStoreId(reqJson.getString("storeId"));
            storeAttrDto.setSpecCd(StoreAttrDto.SPEC_CD_FOUNDINGTIME);
            List<StoreAttrDto> storeAttrDtos = storeAttrV1InnerServiceSMOImpl.queryStoreAttrs(storeAttrDto);
            if (storeAttrDtos == null || storeAttrDtos.size() < 1) {
                StoreAttrPo storeAttrPo = new StoreAttrPo();
                storeAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
                storeAttrPo.setSpecCd(StoreAttrDto.SPEC_CD_FOUNDINGTIME);
                storeAttrPo.setStoreId(storePo.getStoreId());
                storeAttrPo.setValue(reqJson.getString("foundingTime"));
                flag = storeAttrV1InnerServiceSMOImpl.saveStoreAttr(storeAttrPo);
            } else {
                StoreAttrPo storeAttrPo = new StoreAttrPo();
                storeAttrPo.setAttrId(storeAttrDtos.get(0).getAttrId());
                storeAttrPo.setSpecCd(StoreAttrDto.SPEC_CD_FOUNDINGTIME);
                storeAttrPo.setValue(reqJson.getString("foundingTime"));
                flag = storeAttrV1InnerServiceSMOImpl.updateStoreAttr(storeAttrPo);
            }
            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
