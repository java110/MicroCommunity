package com.java110.store.cmd.purchaseApplyDetail;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.basePrivilege.BasePrivilegeDto;
import com.java110.dto.purchaseApplyDetail.PurchaseApplyDetailDto;
import com.java110.intf.community.IMenuInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.purchaseApplyDetail.ApiPurchaseApplyDetailDataVo;
import com.java110.vo.api.purchaseApplyDetail.ApiPurchaseApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Java110Cmd(serviceCode = "purchaseApplyDetail.listPurchaseApplyDetails")
public class ListPurchaseApplyDetailsCmd extends Cmd {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        PurchaseApplyDetailDto purchaseApplyDetailDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDetailDto.class);

        //是否具有查看所有出入库明细的权限
        String userId = reqJson.getString("userId");
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewAllPurchaseApplyDetail");
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        purchaseApplyDetailDto.setUserId("");//申请人
        if (privileges.size()!=0) {
            purchaseApplyDetailDto.setUserId("");//创建人
        }else{
            purchaseApplyDetailDto.setCreateUserId(userId);//创建人
        }


        int count = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetailsCount(purchaseApplyDetailDto);

        List<ApiPurchaseApplyDetailDataVo> purchaseApplyDetails = null;

        if (count > 0) {
            purchaseApplyDetails = BeanConvertUtil.covertBeanList(purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto), ApiPurchaseApplyDetailDataVo.class);
        } else {
            purchaseApplyDetails = new ArrayList<>();
        }

        ApiPurchaseApplyDetailVo apiPurchaseApplyDetailVo = new ApiPurchaseApplyDetailVo();

        apiPurchaseApplyDetailVo.setTotal(count);
        apiPurchaseApplyDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPurchaseApplyDetailVo.setPurchaseApplyDetails(purchaseApplyDetails);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPurchaseApplyDetailVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
