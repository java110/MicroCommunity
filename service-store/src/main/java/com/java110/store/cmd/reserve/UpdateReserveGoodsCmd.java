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
package com.java110.store.cmd.reserve;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.reserve.ReserveGoodsDetailDto;
import com.java110.intf.store.IReserveGoodsDetailV1InnerServiceSMO;
import com.java110.intf.store.IReserveGoodsV1InnerServiceSMO;
import com.java110.po.reserveGoods.ReserveGoodsPo;
import com.java110.po.reserveGoodsDetail.ReserveGoodsDetailPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：reserveGoods.updateReserveGoods
 * 请求路劲：/app/reserveGoods.UpdateReserveGoods
 * add by 吴学文 at 2022-12-05 18:25:18 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "reserve.updateReserveGoods")
public class UpdateReserveGoodsCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateReserveGoodsCmd.class);


    @Autowired
    private IReserveGoodsV1InnerServiceSMO reserveGoodsV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsDetailV1InnerServiceSMO reserveGoodsDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "goodsId", "goodsId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReserveGoodsPo reserveGoodsPo = BeanConvertUtil.covertBean(reqJson, ReserveGoodsPo.class);
        int flag = reserveGoodsV1InnerServiceSMOImpl.updateReserveGoods(reserveGoodsPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        ReserveGoodsDetailDto reserveGoodsDetailDto = new ReserveGoodsDetailDto();
        reserveGoodsDetailDto.setGoodsId(reserveGoodsPo.getGoodsId());
        List<ReserveGoodsDetailDto> reserveGoodsDetailDtos = reserveGoodsDetailV1InnerServiceSMOImpl.queryReserveGoodsDetails(reserveGoodsDetailDto);
        if(reserveGoodsDetailDtos == null || reserveGoodsDetailDtos.size()< 1){
            ReserveGoodsDetailPo reserveGoodsDetailPo = new ReserveGoodsDetailPo();
            reserveGoodsDetailPo.setGoodsId(reserveGoodsPo.getGoodsId());
            reserveGoodsDetailPo.setCommunityId(reserveGoodsPo.getCommunityId());
            reserveGoodsDetailPo.setContent(reqJson.getString("content"));
            reserveGoodsDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId("11"));
            flag = reserveGoodsDetailV1InnerServiceSMOImpl.saveReserveGoodsDetail(reserveGoodsDetailPo);
            if (flag < 1) {
                throw new CmdException("保存详情失败");
            }
        }else{
            ReserveGoodsDetailPo reserveGoodsDetailPo = new ReserveGoodsDetailPo();
            reserveGoodsDetailPo.setGoodsId(reserveGoodsPo.getGoodsId());
            reserveGoodsDetailPo.setCommunityId(reserveGoodsPo.getCommunityId());
            reserveGoodsDetailPo.setContent(reqJson.getString("content"));
            reserveGoodsDetailPo.setDetailId(reserveGoodsDetailDtos.get(0).getDetailId());
            reserveGoodsDetailV1InnerServiceSMOImpl.updateReserveGoodsDetail(reserveGoodsDetailPo);
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
