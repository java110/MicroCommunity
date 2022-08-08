package com.java110.store.cmd.purchaseApply;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.intf.common.IPurchaseApplyUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyV1InnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "purchaseApply.auditApplyOrder")
public class AuditApplyOrderCmd extends Cmd {

    @Autowired
    private IPurchaseApplyUserInnerServiceSMO purchaseApplyUserInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyV1InnerServiceSMO purchaseApplyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "applyOrderId", "订单号不能为空");
        Assert.hasKeyAndValue(reqJson, "taskId", "必填，请填写任务ID");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写审核状态");
        Assert.hasKeyAndValue(reqJson, "remark", "必填，请填写批注");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setTaskId(reqJson.getString("taskId"));
        purchaseApplyDto.setApplyOrderId(reqJson.getString("applyOrderId"));
        purchaseApplyDto.setStoreId(reqJson.getString("storeId"));
        purchaseApplyDto.setAuditCode(reqJson.getString("state"));
        purchaseApplyDto.setNoticeState(reqJson.getString("noticeState"));
        purchaseApplyDto.setAuditMessage(reqJson.getString("remark"));
        purchaseApplyDto.setCurrentUserId(reqJson.getString("userId"));
        purchaseApplyDto.setCommunityId(reqJson.getString("communityId"));
        purchaseApplyDto.setNextStaffId(reqJson.getString("nextUserId"));
        PurchaseApplyDto tmpPurchaseApplyDto = new PurchaseApplyDto();
        tmpPurchaseApplyDto.setApplyOrderId(reqJson.getString("applyOrderId"));
        tmpPurchaseApplyDto.setStoreId(reqJson.getString("storeId"));
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(tmpPurchaseApplyDto);
        Assert.listOnlyOne(purchaseApplyDtos, "采购申请单存在多条");
        purchaseApplyDto.setStartUserId(purchaseApplyDtos.get(0).getUserId());
        purchaseApplyDto.setResOrderType(purchaseApplyDtos.get(0).getResOrderType());
        purchaseApplyDto.setNextStaffId(reqJson.getString("nextUserId"));
        if (purchaseApplyDtos.get(0).getState().equals(purchaseApplyDto.STATE_WAIT_DEAL) && reqJson.getString("state").equals("1100")) {  //如果状态是未审核 并且是审核通过，就变成审核中
            PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
            purchaseApplyPo.setApplyOrderId(purchaseApplyDtos.get(0).getApplyOrderId());
            purchaseApplyPo.setState(purchaseApplyDto.STATE_DEALING);
            int flag = purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);

            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        } else if ((purchaseApplyDtos.get(0).getState().equals(purchaseApplyDto.STATE_DEALING) || purchaseApplyDtos.get(0).getState().equals(purchaseApplyDto.STATE_AUDITED))
                && reqJson.getString("state").equals("1100")) {  //如果状态是审核中 并且是审核通过，
            PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
            purchaseApplyPo.setApplyOrderId(purchaseApplyDtos.get(0).getApplyOrderId());
            int flag = purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);

            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        } else if (purchaseApplyDtos.get(0).getState().equals(purchaseApplyDto.STATE_NOT_PASS) && reqJson.getString("state").equals("1200")) {  //如果状态未通过 并且是结束，
            PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
            purchaseApplyPo.setApplyOrderId(purchaseApplyDtos.get(0).getApplyOrderId());
            int flag = purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);

            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        }

        boolean isLastTask = purchaseApplyUserInnerServiceSMOImpl.completeTask(purchaseApplyDto);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("成功", HttpStatus.OK);
        if (isLastTask && purchaseApplyDtos.get(0).getState().equals(purchaseApplyDto.STATE_AUDITED)) {//已经是已审核入库完成的状态进行更新状态
            updatePurchaseApply(reqJson);
        }
        if (reqJson.getString("state").equals("1200") && !isLastTask) {//审核拒绝时，状态变为拒绝状态
            PurchaseApplyPo purchaseApplyPo = new PurchaseApplyPo();
            purchaseApplyPo.setApplyOrderId(purchaseApplyDtos.get(0).getApplyOrderId());
            purchaseApplyPo.setState(purchaseApplyDto.STATE_NOT_PASS);
            int flag = purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);

            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        }
    }

    /**
     * @param paramInJson 接口调用放传入入参
     * @return 订单服务能够接受的报文
     */
    private void updatePurchaseApply(JSONObject paramInJson) {
        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
        purchaseApplyDto.setStoreId(paramInJson.getString("storeId"));
        purchaseApplyDto.setApplyOrderId(paramInJson.getString("applyOrderId"));
        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplys(purchaseApplyDto);
        Assert.listOnlyOne(purchaseApplyDtos, "存在多条记录，或不存在数据" + purchaseApplyDto.getApplyOrderId());
        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(BeanConvertUtil.beanCovertMap(purchaseApplyDtos.get(0)));
        PurchaseApplyPo purchaseApplyPo = BeanConvertUtil.covertBean(businessComplaint, PurchaseApplyPo.class);
        purchaseApplyPo.setState(purchaseApplyDto.STATE_END);
        int flag = purchaseApplyV1InnerServiceSMOImpl.updatePurchaseApply(purchaseApplyPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }
}
