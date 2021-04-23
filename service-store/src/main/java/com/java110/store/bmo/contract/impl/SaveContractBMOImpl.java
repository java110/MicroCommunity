package com.java110.store.bmo.contract.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractType.ContractTypeDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.rentingPool.RentingPoolDto;
import com.java110.dto.store.StoreDto;
import com.java110.intf.IContractRoomInnerServiceSMO;
import com.java110.intf.common.IContractApplyUserInnerServiceSMO;
import com.java110.intf.store.IContractAttrInnerServiceSMO;
import com.java110.intf.store.IContractFileInnerServiceSMO;
import com.java110.intf.store.IContractInnerServiceSMO;
import com.java110.intf.store.IContractTypeInnerServiceSMO;
import com.java110.intf.user.IRentingPoolInnerServiceSMO;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.po.contractRoom.ContractRoomPo;
import com.java110.po.rentingPool.RentingPoolPo;
import com.java110.store.bmo.contract.ISaveContractBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
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

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl; // 房屋租赁

    @Autowired
    private IContractApplyUserInnerServiceSMO contractApplyUserInnerServiceSMOImpl;

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

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
        //校验合同编号是否重复
        ContractDto contractDto = new ContractDto();
        contractDto.setStoreId(contractPo.getStoreId());
        contractDto.setContractCode(contractPo.getContractCode());
        List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        if (contractDtos != null && contractDtos.size() > 0) {
            throw new IllegalArgumentException("合同" + "[" + contractPo.getContractCode() + "]已存在");
        }

        contractPo.setContractId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_contractId));
        //附件保存
        List<ContractFilePo> filePos = contractPo.getContractFilePo();
        int flag = contractInnerServiceSMOImpl.saveContract(contractPo);
        for (ContractFilePo file : filePos) {
            if (file.getFileRealName().length() > 0 && file.getFileSaveName().length() > 0) {
                file.setContractId(contractPo.getContractId());
                contractFileInnerServiceSMOImpl.saveContractFile(file);
            }
        }

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        saveContractRoomRel(reqJson, contractPo);


        //提交流程
        ContractDto tmpContractDto = BeanConvertUtil.covertBean(contractPo, ContractDto.class);
        tmpContractDto.setCurrentUserId(reqJson.getString("userId"));
        contractApplyUserInnerServiceSMOImpl.startProcess(tmpContractDto);

        if (StoreDto.STORE_ADMIN.equals(contractPo.getStoreId())) {
            noticeRentUpdateState(contractPo, audit);
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

    private void saveContractRoomRel(JSONObject reqJson, ContractPo contractPo) {

        //保存关联房屋
        if (!reqJson.containsKey("rooms")) {
            return;
        }
        JSONArray rooms = reqJson.getJSONArray("rooms");
        for (int conFileIndex = 0; conFileIndex < rooms.size(); conFileIndex++) {
            JSONObject resourceStore = rooms.getJSONObject(conFileIndex);
            ContractRoomPo contractRoomPo = BeanConvertUtil.covertBean(resourceStore, ContractRoomPo.class);
            contractRoomPo.setCrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_crId));
            contractRoomPo.setContractId(contractPo.getContractId());
            contractRoomPo.setStoreId(contractPo.getStoreId());
            contractRoomPo.setRoomName(
                    resourceStore.getString("floorNum") + "-"
                            + resourceStore.getString("unitNum") + "-" + resourceStore.getString("roomNum"));
            contractRoomInnerServiceSMOImpl.saveContractRoom(contractRoomPo);
        }
        //刷业主
    }

    /**
     * 修改 房屋租赁状态
     *
     * @param contractPo
     */
    private void noticeRentUpdateState(ContractPo contractPo, String audit) {

        if (!contractPo.getObjType().equals(FeeDto.PAYER_OBJ_TYPE_ROOM)
                || StringUtil.isEmpty(contractPo.getObjId())
                || contractPo.getObjId().startsWith("-")) {
            return;
        }
        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setRoomId(contractPo.getObjId());
        rentingPoolDto.setState(RentingPoolDto.STATE_APPLY_AGREE);
        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        if (rentingPoolDtos == null || rentingPoolDtos.size() < 1) {
            return;
        }


        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        rentingPoolPo.setRentingId(rentingPoolDtos.get(0).getRentingId());

        if (ContractTypeDto.NO_AUDIT.equals(audit)) {
            rentingPoolPo.setState(RentingPoolDto.STATE_FINISH);
        } else {
            rentingPoolPo.setState(RentingPoolDto.STATE_ADMIN_AUDIT);
        }
        rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
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
