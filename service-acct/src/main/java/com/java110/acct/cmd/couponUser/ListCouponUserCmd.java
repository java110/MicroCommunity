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
package com.java110.acct.cmd.couponUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.CommunityMemberDto;
import com.java110.dto.couponPool.CouponUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.intf.acct.ICouponUserV1InnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.user.IOwnerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：查询
 * 服务编码：couponUser.listCouponUser
 * 请求路劲：/app/couponUser.ListCouponUser
 * add by 吴学文 at 2021-11-24 00:19:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "couponUser.listCouponUser")
public class ListCouponUserCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListCouponUserCmd.class);
    @Autowired
    private ICouponUserV1InnerServiceSMO couponUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "tel", "未包含手机号");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CouponUserDto couponUserDto = BeanConvertUtil.covertBean(reqJson, CouponUserDto.class);

        //是否包含小区ID
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        communityMemberDto.setPage(1);
        communityMemberDto.setRow(1);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "小区物业不存在");

        //查询所有家庭成员
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setLink(reqJson.getString("tel"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setPage(1);
        ownerDto.setRow(1);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }

        ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            return;
        }
        List<String> tels = new ArrayList<>();
        for (OwnerDto tmpOwnerDto : ownerDtos) {
            tels.add(tmpOwnerDto.getLink());
        }

        couponUserDto.setTel("");
        couponUserDto.setTels(tels.toArray(new String[tels.size()]));

        int count = couponUserV1InnerServiceSMOImpl.queryCouponUsersCount(couponUserDto);

        List<CouponUserDto> couponUserDtos = null;

        if (count > 0) {
            couponUserDtos = couponUserV1InnerServiceSMOImpl.queryCouponUsers(couponUserDto);
        } else {
            couponUserDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, couponUserDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
