package com.java110.store.bmo.contract.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contractType.ContractTypeDto;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractTypeInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.store.bmo.contract.ISaveContractBMO;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveContractBMOImpl")
public class SaveContractBMOImpl implements ISaveContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    @Autowired
    private IContractTypeInnerServiceSMO contractTypeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ContractPo contractPo, JSONObject reqJson) {

        //查询 合同是否需要审核
        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.setContractTypeId(contractPo.getContractType());
        contractTypeDto.setStoreId(contractPo.getStoreId());
        List<ContractTypeDto> contractTypeDtos = contractTypeInnerServiceSMOImpl.queryContractTypes(contractTypeDto);

        Assert.listOnlyOne(contractTypeDtos, "查询合同类型失败");

        String audit = contractTypeDtos.get(0).getAudit();

        if (ContractTypeDto.NO_AUDIT.equals(audit)) {
            contractPo.setState("22");
        } else {
            contractPo.setState("11");
        }

        contractPo.setContractId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_contractId));
        int flag = contractInnerServiceSMOImpl.saveContract(contractPo);

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }

        if (!reqJson.containsKey("contractTypeSpecs")) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        JSONArray contractTypeSpecs = reqJson.getJSONArray("contractTypeSpecs");

        if (contractTypeSpecs.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        for (int typeSpecIndex = 0; typeSpecIndex < contractTypeSpecs.size(); typeSpecIndex++) {
            saveContractAttr(contractTypeSpecs.getJSONObject(typeSpecIndex), contractPo);
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

    /**
     * 保存合同属性
     *
     * @param jsonObject
     */
    private void saveContractAttr(JSONObject jsonObject, ContractPo contractPo) {

        ContractAttrPo contractAttrPo = new ContractAttrPo();
        contractAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        contractAttrPo.setContractId(contractPo.getContractId());
        contractAttrPo.setSpecCd(jsonObject.getString("specCd"));
        contractAttrPo.setValue(jsonObject.getString("value"));
        contractAttrPo.setStoreId(contractPo.getStoreId());
        int count = contractAttrInnerServiceSMOImpl.saveContractAttr(contractAttrPo);

        if (count < 1) {
            throw new IllegalArgumentException("保存属性失败");
        }
    }

}
