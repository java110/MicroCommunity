package com.java110.store.bmo.contract.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractAttr.ContractAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.store.bmo.contract.IUpdateContractBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("updateContractBMOImpl")
public class UpdateContractBMOImpl implements IUpdateContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IContractAttrInnerServiceSMO contractAttrInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    /**
     * @param contractPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ContractPo contractPo, JSONObject reqJson) {

        int flag = contractInnerServiceSMOImpl.updateContract(contractPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }

        noticeRentUpdateState(contractPo);

        if (!reqJson.containsKey("contractTypeSpecs")) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }


        JSONArray contractTypeSpecs = reqJson.getJSONArray("contractTypeSpecs");

        if (contractTypeSpecs.size() < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        for (int typeSpecIndex = 0; typeSpecIndex < contractTypeSpecs.size(); typeSpecIndex++) {
            updateContractAttr(contractTypeSpecs.getJSONObject(typeSpecIndex), contractPo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");

    }

    private void updateContractAttr(JSONObject jsonObject, ContractPo contractPo) {
        ContractAttrDto contractAttrDto = new ContractAttrDto();
        contractAttrDto.setContractId(contractPo.getContractId());
        contractAttrDto.setSpecCd(jsonObject.getString("specCd"));
        List<ContractAttrDto> contractAttrDtos = contractAttrInnerServiceSMOImpl.queryContractAttrs(contractAttrDto);

        Assert.listOnlyOne(contractAttrDtos, "未找到需要修改的合同属性");
        ContractAttrPo contractAttrPo = new ContractAttrPo();
        contractAttrPo.setAttrId(contractAttrDtos.get(0).getAttrId());
        contractAttrPo.setContractId(contractPo.getContractId());
        contractAttrPo.setSpecCd(jsonObject.getString("specCd"));
        contractAttrPo.setValue(jsonObject.getString("value"));
        contractAttrPo.setStoreId(contractPo.getStoreId());
        int count = contractAttrInnerServiceSMOImpl.updateContractAttr(contractAttrPo);

        if (count < 1) {
            throw new IllegalArgumentException("保存属性失败");
        }
    }

    /**
     * 修改 房屋租赁状态
     *
     * @param contractPo
     */
    private void noticeRentUpdateState(ContractPo contractPo) {

        ContractDto contractDto = new ContractDto();
        contractDto.setContractId(contractPo.getContractId());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        Assert.listOnlyOne(contractDtos, "未找到需要审核合同");

        if (!contractDtos.get(0).getObjType().equals(FeeDto.PAYER_OBJ_TYPE_ROOM)
                || StringUtil.isEmpty(contractDtos.get(0).getObjId())
                || contractDtos.get(0).getObjId().startsWith("-")) {
            return;
        }

        if (!StoreDto.STORE_ADMIN.equals(contractDtos.get(0).getStoreId())) {
            return;
        }
        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setRoomId(contractDtos.get(0).getObjId());
        rentingPoolDto.setState(RentingPoolDto.STATE_ADMIN_AUDIT);
        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        if (rentingPoolDtos == null || rentingPoolDtos.size() < 1) {
            return;
        }

        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        rentingPoolPo.setRentingId(rentingPoolDtos.get(0).getRentingId());

        rentingPoolPo.setState(RentingPoolDto.STATE_FINISH);

        rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
    }

}
