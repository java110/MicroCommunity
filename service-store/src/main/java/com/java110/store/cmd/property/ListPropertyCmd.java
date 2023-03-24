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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.intf.store.IStoreV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


@Java110CmdDoc(title = "查询物业公司",
        description = "主要提供给外系统查询物业公司",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/property.listProperty",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "property.listProperty",
        seq = 4
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "storeId", length = 30, remark = "物业编号"),
        @Java110ParamDoc(name = "name", length = 64, remark = "物业名称"),
        @Java110ParamDoc(name = "tel", length = 11, remark = "物业电话"),
        @Java110ParamDoc(name = "page",type="int", length = 11, remark = "页数"),
        @Java110ParamDoc(name = "row",type="int", length = 11, remark = "行数"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", length = 0, defaultValue = "成功", remark = "数据节点"),
                @Java110ParamDoc(parentNodeName = "data", name = "storeId", length = 30, remark = "物业编号"),
                @Java110ParamDoc(parentNodeName = "data", name = "name", length = 64, remark = "物业名称"),
                @Java110ParamDoc(parentNodeName = "data", name = "nearbyLandmarks", length = 64, remark = "地标，如xx 公园旁"),
                @Java110ParamDoc(parentNodeName = "data", name = "tel", length = 11, remark = "物业管理员电话 作为管理员账号密码 添加后请及时修改密码"),
                @Java110ParamDoc(parentNodeName = "data", name = "address", length = 11, remark = "公司地址"),
                @Java110ParamDoc(parentNodeName = "data", name = "corporation", length = 11, remark = "法人"),
                @Java110ParamDoc(parentNodeName = "data", name = "foundingTime", length = 11, remark = "成立日期"),
        }
)

@Java110ExampleDoc(
        reqBody="http://{ip}:{port}/app/property.listProperty?storeId=&name=&tel=&page=1&row=10",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：查询
 * 服务编码：store.listStore
 * 请求路劲：/app/store.ListStore
 * add by 吴学文 at 2022-02-28 10:46:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "property.listProperty")
public class ListPropertyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListPropertyCmd.class);

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(reqJson.getString("storeId"));
        List<StoreDto> storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);

        Assert.listOnlyOne(storeDtos, "非法操作");

        if (StoreDto.STORE_TYPE_ADMIN.equals(storeDtos.get(0).getStoreTypeCd())) {
            reqJson.remove("storeId");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        StoreDto storeDto = BeanConvertUtil.covertBean(reqJson, StoreDto.class);
        storeDto.setUserId("");
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_PROPERTY);

        StoreDto storeDto1 = BeanConvertUtil.covertBean(reqJson, StoreDto.class);
        storeDto1.setUserId("");
        storeDto1.setStoreTypeCd(StoreDto.STORE_TYPE_PROPERTY);

        int count = storeV1InnerServiceSMOImpl.queryStoresCount(storeDto);

        List<StoreDto> storeDtos = new ArrayList<>();

        if (count > 0) {
            List<StoreDto> storeList = storeV1InnerServiceSMOImpl.queryStores(storeDto);
            List<StoreDto> stores = storeInnerServiceSMOImpl.getStores(storeDto1);
            for (StoreDto store : storeList) {
                for (StoreDto store1 : stores) {
                    if (store.getStoreId().equals(store1.getStoreId())) {
                        store.setNearByLandmarks(store1.getNearByLandmarks());
                    }
                }
                storeDtos.add(store);
            }
//            storeDtos = storeV1InnerServiceSMOImpl.queryStores(storeDto);
        } else {
            storeDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, storeDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
