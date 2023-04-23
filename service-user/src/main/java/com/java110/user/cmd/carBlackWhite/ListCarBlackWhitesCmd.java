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
package com.java110.user.cmd.carBlackWhite;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.machine.CarBlackWhiteDto;
import com.java110.dto.parking.ParkingBoxAreaDto;
import com.java110.intf.common.ICarBlackWhiteInnerServiceSMO;
import com.java110.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.java110.intf.user.ICarBlackWhiteV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.carBlackWhite.ApiCarBlackWhiteDataVo;
import com.java110.vo.api.carBlackWhite.ApiCarBlackWhiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：carBlackWhite.listCarBlackWhite
 * 请求路劲：/app/carBlackWhite.ListCarBlackWhite
 * add by 吴学文 at 2022-07-16 23:23:24 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "carBlackWhite.listCarBlackWhites")
public class ListCarBlackWhitesCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListCarBlackWhitesCmd.class);
    @Autowired
    private ICarBlackWhiteV1InnerServiceSMO carBlackWhiteV1InnerServiceSMOImpl;

    @Autowired
    private ICarBlackWhiteInnerServiceSMO carBlackWhiteInnerServiceSMOImpl;

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        CarBlackWhiteDto carBlackWhiteDto = BeanConvertUtil.covertBean(reqJson, CarBlackWhiteDto.class);
        carBlackWhiteDto.setPaIds(getPaIds(reqJson));

        int count = carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhitesCount(carBlackWhiteDto);

        List<ApiCarBlackWhiteDataVo> carBlackWhites = null;

        if (count > 0) {
            carBlackWhites = BeanConvertUtil.covertBeanList(carBlackWhiteInnerServiceSMOImpl.queryCarBlackWhites(carBlackWhiteDto), ApiCarBlackWhiteDataVo.class);
        } else {
            carBlackWhites = new ArrayList<>();
        }

        ApiCarBlackWhiteVo apiCarBlackWhiteVo = new ApiCarBlackWhiteVo();

        apiCarBlackWhiteVo.setTotal(count);
        apiCarBlackWhiteVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarBlackWhiteVo.setCarBlackWhites(carBlackWhites);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCarBlackWhiteVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }


    private String[] getPaIds(JSONObject reqJson) {
        if (reqJson.containsKey("boxId") && !StringUtil.isEmpty(reqJson.getString("boxId"))) {
            ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
            parkingBoxAreaDto.setBoxId(reqJson.getString("boxId"));
            parkingBoxAreaDto.setCommunityId(reqJson.getString("communityId"));
            List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);

            if (parkingBoxAreaDtos == null || parkingBoxAreaDtos.size() < 1) {
                throw new CmdException("未查到停车场信息");
            }
            List<String> paIds = new ArrayList<>();
            for (ParkingBoxAreaDto parkingBoxAreaDto1 : parkingBoxAreaDtos) {
                paIds.add(parkingBoxAreaDto1.getPaId());
            }
            String[] paIdss = paIds.toArray(new String[paIds.size()]);
            return paIdss;
        }
        return null;
    }
}
