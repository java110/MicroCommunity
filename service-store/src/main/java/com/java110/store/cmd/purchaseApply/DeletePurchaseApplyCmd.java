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
package com.java110.store.cmd.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailV1InnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyDetailPo;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：purchaseApply.deletePurchaseApply
 * 请求路劲：/app/purchaseApply.DeletePurchaseApply
 * add by 吴学文 at 2022-08-08 13:21:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "purchaseApply.deletePurchaseApply")
public class DeletePurchaseApplyCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeletePurchaseApplyCmd.class);

    @Autowired
    private IPurchaseApplyV1InnerServiceSMO purchaseApplyV1InnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO iPurchaseApplyUserInnerServiceSMO;

    @Autowired
    private IPurchaseApplyDetailV1InnerServiceSMO purchaseApplyDetailV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单号不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String applyOrderId = reqJson.getString("applyOrderId");
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setApplyOrderId(applyOrderId);
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        if (purchaseApplyDtos.size() != 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "采购申请单出现多条或者未找到采购申请单！");
            context.setResponseEntity(responseEntity);
            return;
        }
        if (!"1000".equals(purchaseApplyDtos.get(0).getState()) && PurchaseApplyDto.RES_ORDER_TYPE_ENTER.equals(purchaseApplyDtos.get(0).getResOrderType())) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您的采购申请订单状态已改变，无法进行取消操作！");
            context.setResponseEntity(responseEntity);
            return;
        }
        if (!"1000".equals(purchaseApplyDtos.get(0).getState()) && PurchaseApplyDto.RES_ORDER_TYPE_OUT.equals(purchaseApplyDtos.get(0).getResOrderType()) ) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "您的物品领用订单状态已改变，无法进行取消操作！");
            context.setResponseEntity(responseEntity);
            return;
        }

        deletePurchaseApply(reqJson);
        deletePurchaseApplyDetail(reqJson);


        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(reqJson, PurchaseApplyPo.class);
        iPurchaseApplyUserInnerServiceSMO.deleteTask(purchaseApplyPo);

        context.setResponseEntity(ResultVo.success());
    }

    /**
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private void deletePurchaseApply(JSONObject paramInJson) {
        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(paramInJson, PurchaseApplyPo.class);
        int flag = purchaseApplyV1InnerServiceSMOImpl.deletePurchaseApply(purchaseApplyPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


    }

    //删除订单明细
    private void deletePurchaseApplyDetail(JSONObject paramInJson) {
        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        purchaseApplyDetailDto.setApplyOrderId(paramInJson.getString("applyOrderId"));
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        int flag = 0;
        if (purchaseApplyDetailDtos.size() > 0) {
            for (PurchaseApplyDetailDto purchaseApplyDetail : purchaseApplyDetailDtos) {
                PurchaseApplyDetailPo purchaseApplyPo = BeanConvertUtil.covertBean(purchaseApplyDetail, PurchaseApplyDetailPo.class);
                flag = purchaseApplyDetailV1InnerServiceSMOImpl.deletePurchaseApplyDetail(purchaseApplyPo);

                if (flag < 1) {
                    throw new CmdException("删除数据失败");
                }
                //取消流程审批
                //查询任务
                PurchaseApplyDto purchaseDto = new PurchaseApplyDto();
                purchaseDto.setBusinessKey(purchaseApplyDetail.getApplyOrderId());
                List<PurchaseApplyDto> purchaseApplyDtoList = purchaseApplyInnerServiceSMOImpl.getActRuTaskId(purchaseDto);
                if (purchaseApplyDtoList != null && purchaseApplyDtoList.size() > 0) {
                    PurchaseApplyDto purchaseDto1 = new PurchaseApplyDto();
                    purchaseDto1.setActRuTaskId(purchaseApplyDtoList.get(0).getActRuTaskId());
                    purchaseDto1.setAssigneeUser("999999");
                    purchaseApplyInnerServiceSMOImpl.updateActRuTaskById(purchaseDto1);
                }
            }
        }
    }
}
