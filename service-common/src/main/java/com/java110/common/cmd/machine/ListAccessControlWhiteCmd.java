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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.intf.common.IAccessControlWhiteV1InnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.accessControlWhite.AccessControlWhiteDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Java110CmdDoc(title = "查询门禁授权白名单",
        description = "主要用于员工，外卖和访客授权门禁白名单",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/machine.listAccessControlWhite",
        resource = "commonDoc",
        author = "吴学文",
        serviceCode = "machine.listAccessControlWhite"
)

@Java110ParamsDoc(params = {
        @Java110ParamDoc(name = "page",type = "int",length = 11, remark = "分页信息"),
        @Java110ParamDoc(name = "row", type = "int",length = 11, remark = "行数"),
        @Java110ParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@Java110ResponseDoc(
        params = {
                @Java110ParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @Java110ParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @Java110ParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @Java110ParamDoc(parentNodeName = "data",name = "acwId", type = "String", remark = "ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "machineId", type = "String", remark = "门禁ID"),
                @Java110ParamDoc(parentNodeName = "data",name = "personName", type = "String", remark = "人员"),
                @Java110ParamDoc(parentNodeName = "data",name = "personTypeName", type = "String", remark = "人员类型"),
                @Java110ParamDoc(parentNodeName = "data",name = "tel", type = "String", remark = "电话"),
        }
)

@Java110ExampleDoc(
        reqBody="http://ip:port/app/machine.listAccessControlWhite?communityId=2022121921870161&page=1&row=10",
        resBody="{\"code\":0,\"data\":[{\"accessControlKey\":\"123123\",\"acwId\":\"102023012465380033\",\"communityId\":\"2022121921870161\",\"createTime\":\"2023-01-24 02:31:32\",\"endTime\":\"2023-01-26 02:10:00\",\"idCard\":\"\",\"machineCode\":\"M99889\",\"machineId\":\"102023012407190005\",\"machineName\":\"测试门禁\",\"page\":-1,\"personFace\":\"https://java110.oss-cn-beijing.aliyuncs.com/hc/img/20230124/c2a3feb4-ad04-47f8-896e-47e5f6f37869.jpg\",\"personId\":\"302023012495700039\",\"personName\":\"张发发\",\"personType\":\"2002\",\"personTypeName\":\"外卖人员\",\"records\":0,\"row\":0,\"startTime\":\"2023-01-24 02:10:00\",\"statusCd\":\"0\",\"tel\":\"18909711445\",\"total\":0}],\"msg\":\"成功\",\"page\":0,\"records\":1,\"rows\":0,\"total\":1}"
)

/**
 * 类表述：查询
 * 服务编码：machine.listAccessControlWhite
 * 请求路劲：/app/machine.ListAccessControlWhite
 * add by 吴学文 at 2023-01-24 00:53:53 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.listAccessControlWhite")
public class ListAccessControlWhiteCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListAccessControlWhiteCmd.class);
    @Autowired
    private IAccessControlWhiteV1InnerServiceSMO accessControlWhiteV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        AccessControlWhiteDto accessControlWhiteDto = BeanConvertUtil.covertBean(reqJson, AccessControlWhiteDto.class);

        int count = accessControlWhiteV1InnerServiceSMOImpl.queryAccessControlWhitesCount(accessControlWhiteDto);

        List<AccessControlWhiteDto> accessControlWhiteDtos = null;

        if (count > 0) {
            accessControlWhiteDtos = accessControlWhiteV1InnerServiceSMOImpl.queryAccessControlWhites(accessControlWhiteDto);
            refreshPhoto(accessControlWhiteDtos);
        } else {
            accessControlWhiteDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, accessControlWhiteDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void refreshPhoto(List<AccessControlWhiteDto> accessControlWhiteDtos) {
        if(accessControlWhiteDtos == null || accessControlWhiteDtos.size() < 1){
            return;
        }

        List<String> acwId = new ArrayList<>();
        for(AccessControlWhiteDto accessControlWhiteDto: accessControlWhiteDtos){
            acwId.add(accessControlWhiteDto.getAcwId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjIds(acwId.toArray(new String[acwId.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if(fileRelDtos == null || fileRelDtos.size() < 1){
            return ;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for(AccessControlWhiteDto accessControlWhiteDto: accessControlWhiteDtos){
            for(FileRelDto tmpFileRelDto : fileRelDtos){
                if(!accessControlWhiteDto.getAcwId().equals(tmpFileRelDto.getObjId())){
                    continue;
                }
                if(tmpFileRelDto.getFileSaveName().startsWith("http")){
                    accessControlWhiteDto.setPersonFace(tmpFileRelDto.getFileSaveName() );
                }else{
                    accessControlWhiteDto.setPersonFace(imgUrl +tmpFileRelDto.getFileSaveName() );
                }
            }
        }
    }
}
