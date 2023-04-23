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
import com.java110.common.bmo.machine.IMachineOpenDoorBMO;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.CommunitySettingFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：machine.getCarMachineQrCodeUrl
 * 请求路劲：/app/machine.getCarMachineQrCodeUrl
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "machine.getCarMachineQrCodeUrl")
public class GetCarMachineQrCodeUrl extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(GetCarMachineQrCodeUrl.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMachineOpenDoorBMO machineOpenDoorBMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;
    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "boxId", "请求报文中未包含岗亭");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(reqJson.getString("communityId"));
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        String ownerUrl = UrlCache.getOwnerUrl();
        ownerUrl += ("/#/pages/fee/tempParkingFee?paId=" + getPaIds(reqJson)+"&communityId="+reqJson.getString("communityId"));
        if (smallWeChatDtos != null && smallWeChatDtos.size() > 0) {
            ownerUrl += ("&appId=" + smallWeChatDtos.get(0).getAppId());
        }

        if (reqJson.containsKey("machineId") && !"-1".equals(reqJson.getString("machineId"))) {
            ownerUrl += ("&machineId=" + reqJson.getString("machineId"));
        }

        String aliAppId = CommunitySettingFactory.getValue(reqJson.getString("communityId"),"APP_ID");
       if( !StringUtil.isEmpty(aliAppId)){
           ownerUrl += ("&aliAppId=" + aliAppId);
       }
        reqJson.put("url", ownerUrl);
        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(reqJson));
    }

    private String getPaIds(JSONObject reqJson) {
        if (reqJson.containsKey("boxId") && !StringUtil.isEmpty(reqJson.getString("boxId"))) {
            ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
            parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
            parkingBoxAreaDto.setCommunityId(reqJson.getString("communityId"));
            parkingBoxAreaDto.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_TRUE);
            List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);

            if (parkingBoxAreaDtos == null || parkingBoxAreaDtos.size() < 1) {
                throw new CmdException("未查到停车场信息");
            }


            return parkingBoxAreaDtos.get(0).getPaId();
        }
        return "";
    }
}
