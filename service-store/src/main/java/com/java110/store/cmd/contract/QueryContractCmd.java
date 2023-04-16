package com.java110.store.cmd.contract;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.intf.common.IContractApplyUserInnerServiceSMO;
import com.java110.intf.common.IContractChangeUserInnerServiceSMO;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Java110Cmd(serviceCode = "/contract/queryContract")
public class QueryContractCmd extends Cmd {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    @Autowired
    private IContractApplyUserInnerServiceSMO contractApplyUserInnerServiceSMOImpl;

    @Autowired
    private IContractChangeUserInnerServiceSMO contractChangeUserInnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");

        ContractDto contractDto = BeanConvertUtil.covertBean(reqJson, ContractDto.class);
        contractDto.setStoreId(storeId);
        //如果是到期合同
        if ("1".equals(reqJson.getString("expiration"))) {
            contractDto.setNoStates(new String[]{ContractDto.STATE_COMPLAINT, ContractDto.STATE_FAIL});
            contractDto.setEndTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        }
        int count = contractInnerServiceSMOImpl.queryContractsCount(contractDto);

        List<ContractDto> contractDtos = null;
        if (count > 0) {
            contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);
            refreshAttr(contractDtos);
        } else {
            contractDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractDto.getRow()), count, contractDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 属性属性
     *
     * @param contractDtos
     */
    private void refreshAttr(List<ContractDto> contractDtos) {
        List<String> contractIds = new ArrayList<>();
        for (ContractDto contractDto : contractDtos) {
            contractIds.add(contractDto.getContractId());
        }


        if (contractIds.size() < 1) {
            return;
        }

        ContractAttrDto contractAttrDto = new ContractAttrDto();
        contractAttrDto.setContractIds(contractIds.toArray(new String[contractIds.size()]));
        List<ContractAttrDto> contractAttrDtos = contractAttrInnerServiceSMOImpl.queryContractAttrs(contractAttrDto);

        List<ContractAttrDto> attrs = null;
        for (ContractDto contractDto : contractDtos) {
            attrs = new ArrayList<>();
            for (ContractAttrDto tmpContractAttrDto : contractAttrDtos) {
                if (contractDto.getContractId().equals(tmpContractAttrDto.getContractId())) {
                    attrs.add(tmpContractAttrDto);
                }
            }
            contractDto.setAttrs(attrs);
        }


    }
}
