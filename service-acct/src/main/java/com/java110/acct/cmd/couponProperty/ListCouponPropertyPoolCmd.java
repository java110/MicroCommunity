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
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.couponPool.CouponPropertyPoolConfigDto;
import com.java110.intf.acct.ICouponPropertyPoolConfigV1InnerServiceSMO;
import com.java110.intf.acct.ICouponPropertyPoolV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.couponPool.CouponPropertyPoolDto;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：查询
 * 服务编码：couponProperty.listCouponPropertyPool
 * 请求路劲：/app/couponProperty.ListCouponPropertyPool
 * add by 吴学文 at 2022-11-19 23:00:42 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "couponProperty.listCouponPropertyPool")
public class ListCouponPropertyPoolCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(ListCouponPropertyPoolCmd.class);
    @Autowired
    private ICouponPropertyPoolV1InnerServiceSMO couponPropertyPoolV1InnerServiceSMOImpl;

    @Autowired
    private ICouponPropertyPoolConfigV1InnerServiceSMO couponPropertyPoolConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

           CouponPropertyPoolDto couponPropertyPoolDto = BeanConvertUtil.covertBean(reqJson, CouponPropertyPoolDto.class);

           int count = couponPropertyPoolV1InnerServiceSMOImpl.queryCouponPropertyPoolsCount(couponPropertyPoolDto);

           List<CouponPropertyPoolDto> couponPropertyPoolDtos = null;

           if (count > 0) {
               couponPropertyPoolDtos = couponPropertyPoolV1InnerServiceSMOImpl.queryCouponPropertyPools(couponPropertyPoolDto);
               queryConfigs(couponPropertyPoolDtos);
           } else {
               couponPropertyPoolDtos = new ArrayList<>();
           }

           ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, couponPropertyPoolDtos);

           ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

           cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void queryConfigs(List<CouponPropertyPoolDto> couponPropertyPoolDtos) {

        if(couponPropertyPoolDtos == null || couponPropertyPoolDtos.size()<1){
            return ;
        }
        List<String> cppIds = new ArrayList<>();

        for(CouponPropertyPoolDto couponPropertyPoolDto: couponPropertyPoolDtos){
            cppIds.add(couponPropertyPoolDto.getCppId());
        }

        CouponPropertyPoolConfigDto couponPropertyPoolConfigDto = new CouponPropertyPoolConfigDto();
        couponPropertyPoolConfigDto.setCouponIds(cppIds.toArray(new String[cppIds.size()]));
        List<CouponPropertyPoolConfigDto> couponPropertyPoolConfigDtos
                = couponPropertyPoolConfigV1InnerServiceSMOImpl.queryCouponPropertyPoolConfigs(couponPropertyPoolConfigDto);
        List<CouponPropertyPoolConfigDto> tmpCouponPropertyPoolConfigDtos = null;
        for(CouponPropertyPoolDto couponPropertyPoolDto: couponPropertyPoolDtos){
            tmpCouponPropertyPoolConfigDtos = new ArrayList<>();
            for(CouponPropertyPoolConfigDto couponPropertyPoolConfigDto1: couponPropertyPoolConfigDtos){
                if(couponPropertyPoolDto.getCppId().equals(couponPropertyPoolConfigDto1.getCouponId())){
                    tmpCouponPropertyPoolConfigDtos.add(couponPropertyPoolConfigDto1);
                }
            }
            couponPropertyPoolDto.setConfigs(tmpCouponPropertyPoolConfigDtos);
        }

    }
}
