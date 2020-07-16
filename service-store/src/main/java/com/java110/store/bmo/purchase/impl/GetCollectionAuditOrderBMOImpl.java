package com.java110.store.bmo.purchase.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.store.bmo.purchase.IGetCollectionAuditOrderBMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import com.java110.vo.api.resourceOrder.ApiResourceOrderDataVo;
import com.java110.vo.api.resourceOrder.ApiResourceOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("getCollectionAuditOrderBMOImpl")
public class GetCollectionAuditOrderBMOImpl implements IGetCollectionAuditOrderBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Override
    public ResponseEntity<String> auditOrder(AuditUser auditUser) {

        long count = goodCollectionUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        List<ApiResourceOrderDataVo> auditOrders = null;

        if (count > 0) {
            List<PurchaseApplyDto> purchaseApplyDtos = goodCollectionUserInnerServiceSMOImpl.getUserTasks(auditUser);
            auditOrders = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ApiResourceOrderDataVo.class);
            for (ApiResourceOrderDataVo apiResourceOrderDataVo : auditOrders) {
                switch (apiResourceOrderDataVo.getState()) {
                    case "1000":
                        apiResourceOrderDataVo.setStateName("待审核");
                        break;
                    case "1001":
                        apiResourceOrderDataVo.setStateName("审核中");
                        break;
                    case "1002":
                        apiResourceOrderDataVo.setStateName("已审核");
                        break;
                }
                apiResourceOrderDataVo.setResOrderTypeName("出库申请");
            }
        } else {
            auditOrders = new ArrayList<>();
        }



        return ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) auditUser.getRow()),(int) count,auditOrders);


    }
}
