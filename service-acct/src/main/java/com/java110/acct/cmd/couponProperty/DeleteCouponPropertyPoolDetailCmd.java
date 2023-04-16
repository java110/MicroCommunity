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
package com.java110.acct.cmd.couponProperty;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.couponPool.CouponPropertyPoolDto;
import com.java110.dto.couponPool.CouponPropertyPoolDetailDto;
import com.java110.dto.couponPool.CouponPropertyUserDto;
import com.java110.intf.acct.ICouponPropertyPoolDetailV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyUserV1InnerServiceSMO;
import com.java110.po.couponPropertyPool.CouponPropertyPoolPo;
import com.java110.po.couponPropertyPoolDetail.CouponPropertyPoolDetailPo;
import com.java110.po.couponPropertyUser.CouponPropertyUserPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.lock.DistributedLock;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：couponProperty.deleteCouponPropertyPool
 * 请求路劲：/app/couponProperty.DeleteCouponPropertyPool
 * add by 吴学文 at 2022-11-19 23:00:42 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "couponProperty.deleteCouponPropertyPoolDetail")
public class DeleteCouponPropertyPoolDetailCmd extends Cmd {
  private static Logger logger = LoggerFactory.getLogger(DeleteCouponPropertyPoolDetailCmd.class);

    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolDetailV1InnerServiceSMO couponPropertyPoolDetailV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyUserV1InnerServiceSMO couponPropertyUserV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "detailId", "赠送记录不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CouponPropertyPoolDetailDto couponPropertyPoolDetailDto = new CouponPropertyPoolDetailDto();
        couponPropertyPoolDetailDto.setCommunityId(reqJson.getString("communityId"));
        couponPropertyPoolDetailDto.setDetailId(reqJson.getString("detailId"));
        List<CouponPropertyPoolDetailDto> details = couponPropertyPoolDetailV1InnerServiceSMOImpl.queryCouponPropertyPoolDetails(couponPropertyPoolDetailDto);

        Assert.listOnlyOne(details,"未包含赠送记录");

        CouponPropertyUserDto couponPropertyUserDto = new CouponPropertyUserDto();
        couponPropertyUserDto.setCppId(details.get(0).getCppId());
        couponPropertyUserDto.setStock(details.get(0).getSendCount());
        List<CouponPropertyUserDto> couponPropertyUserDtos = couponPropertyUserV1InnerServiceSMOImpl.queryCouponPropertyUsers(couponPropertyUserDto);

        if(couponPropertyUserDtos == null || couponPropertyUserDtos.size() <1){
            throw new CmdException("优惠券已经被使用，无法退回");
        }

        String requestId = DistributedLock.getLockUUID();
        String key = this.getClass().getSimpleName() + reqJson.getString("cppId");
        try {
            DistributedLock.waitGetDistributedLock(key, requestId);

            CouponPropertyPoolDto couponPropertyPoolDto = new CouponPropertyPoolDto();
            couponPropertyPoolDto.setCppId(details.get(0).getCppId());
            List<CouponPropertyPoolDto> couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOImpl.queryCouponPropertyPools(couponPropertyPoolDto);

            if (couponPropertyPoolDtos == null || couponPropertyPoolDtos.size() < 1) {
                throw new CmdException("优惠券不存在");
            }

            int stock = Integer.parseInt(couponPropertyPoolDtos.get(0).getStock());

            int quantity = Integer.parseInt(details.get(0).getSendCount());



            //先加明细
            CouponPropertyPoolDetailPo couponPropertyPoolDetailPo = new CouponPropertyPoolDetailPo();
            couponPropertyPoolDetailPo.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
            couponPropertyPoolDetailPo.setDetailId(details.get(0).getDetailId());
            int flag = couponPropertyPoolDetailV1InnerServiceSMOImpl.deleteCouponPropertyPoolDetail(couponPropertyPoolDetailPo);
            if(flag < 1){
                throw new CmdException("删除赠送记录失败");
            }

            //优惠券扣除账户
            CouponPropertyPoolPo couponPropertyPoolPo = new CouponPropertyPoolPo();
            couponPropertyPoolPo.setCppId(couponPropertyPoolDtos.get(0).getCppId());
            couponPropertyPoolPo.setStock((stock + quantity) + "");
            flag = couponPropertyPoolV1InnerServiceSMOImpl.updateCouponPropertyPool(couponPropertyPoolPo);
            if(flag < 1){
                throw new CmdException("赠送失败");
            }

            //用户账户写入优惠券
            CouponPropertyUserPo couponPropertyUserPo = new CouponPropertyUserPo();
            couponPropertyUserPo.setCommunityId(couponPropertyPoolDtos.get(0).getCommunityId());
            couponPropertyUserPo.setCouponId(couponPropertyUserDtos.get(0).getCouponId());
            flag = couponPropertyUserV1InnerServiceSMOImpl.deleteCouponPropertyUser(couponPropertyUserPo);

            if(flag < 1){
                throw new CmdException("保存赠送业主记录失败");
            }
        } finally {
            DistributedLock.releaseDistributedLock(requestId, key);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
