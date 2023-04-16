package com.java110.store.bmo.contract.impl;

import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.dto.contract.ContractChangePlanDto;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IContractApplyUserInnerServiceSMO;
import com.java110.intf.common.IContractChangeUserInnerServiceSMO;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.store.bmo.contract.IGetContractBMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractBMOImpl")
public class GetContractBMOImpl implements IGetContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    @Autowired
    private IContractApplyUserInnerServiceSMO contractApplyUserInnerServiceSMOImpl;

    @Autowired
    private IContractChangeUserInnerServiceSMO contractChangeUserInnerServiceSMO;

    /**
     * @param contractDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ContractDto contractDto) {


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

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryContractChangeTask(AuditUser auditUser) {
        long count = contractChangeUserInnerServiceSMO.getUserTaskCount(auditUser);

        List<ContractChangePlanDto> contractChangePlanDtos = null;

        if (count > 0) {
            contractChangePlanDtos = contractChangeUserInnerServiceSMO.getUserTasks(auditUser);
        } else {
            contractChangePlanDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) auditUser.getRow()), new Long(count).intValue(), contractChangePlanDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryContractChangeHistoryTask(AuditUser auditUser) {

        long count = contractChangeUserInnerServiceSMO.getUserHistoryTaskCount(auditUser);

        List<ContractChangePlanDto> contractChangePlanDtos = null;

        if (count > 0) {
            contractChangePlanDtos = contractChangeUserInnerServiceSMO.getUserHistoryTasks(auditUser);
        } else {
            contractChangePlanDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) auditUser.getRow()), new Long(count).intValue(), contractChangePlanDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


    @Override
    public ResponseEntity<String> queryContractTask(AuditUser auditUser) {
        long count = contractApplyUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

        List<ContractDto> contractDtos = null;

        if (count > 0) {
            List<ContractDto> purchaseApplyDtos = contractApplyUserInnerServiceSMOImpl.getUserTasks(auditUser);
            contractDtos = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ContractDto.class);
        } else {
            contractDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) auditUser.getRow()), new Long(count).intValue(), contractDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryContractHistoryTask(AuditUser auditUser) {

        long count = contractApplyUserInnerServiceSMOImpl.getUserHistoryTaskCount(auditUser);

        List<ContractDto> contractDtos = null;

        if (count > 0) {
            List<ContractDto> purchaseApplyDtos = contractApplyUserInnerServiceSMOImpl.getUserHistoryTasks(auditUser);
            contractDtos = BeanConvertUtil.covertBeanList(purchaseApplyDtos, ContractDto.class);
        } else {
            contractDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) auditUser.getRow()), new Long(count).intValue(), contractDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
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
