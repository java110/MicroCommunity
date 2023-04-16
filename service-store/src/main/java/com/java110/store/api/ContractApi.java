package com.java110.store.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contract.ContractAttrDto;
import com.java110.dto.contract.ContractChangePlanDto;
import com.java110.dto.contract.ContractChangePlanDetailDto;
import com.java110.dto.contract.ContractChangePlanDetailAttrDto;
import com.java110.dto.contract.ContractCollectionPlanDto;
import com.java110.dto.contract.ContractRoomDto;
import com.java110.dto.contract.ContractTypeDto;
import com.java110.dto.contract.ContractTypeSpecDto;
import com.java110.dto.contract.ContractTypeTemplateDto;
import com.java110.entity.audit.AuditUser;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractAttr.ContractAttrPo;
import com.java110.po.contractChangePlan.ContractChangePlanPo;
import com.java110.po.contractChangePlanDetail.ContractChangePlanDetailPo;
import com.java110.po.contractChangePlanDetailAttr.ContractChangePlanDetailAttrPo;
import com.java110.po.contractChangePlanRoom.ContractChangePlanRoomPo;
import com.java110.po.contractCollectionPlan.ContractCollectionPlanPo;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.po.contractRoom.ContractRoomPo;
import com.java110.po.contractType.ContractTypePo;
import com.java110.po.contractTypeSpec.ContractTypeSpecPo;
import com.java110.po.contractTypeTemplate.ContractTypeTemplatePo;
import com.java110.store.bmo.contract.IDeleteContractBMO;
import com.java110.store.bmo.contract.IGetContractBMO;
import com.java110.store.bmo.contract.ISaveContractBMO;
import com.java110.store.bmo.contract.IUpdateContractBMO;
import com.java110.store.bmo.contractAttr.IDeleteContractAttrBMO;
import com.java110.store.bmo.contractAttr.IGetContractAttrBMO;
import com.java110.store.bmo.contractAttr.ISaveContractAttrBMO;
import com.java110.store.bmo.contractAttr.IUpdateContractAttrBMO;
import com.java110.store.bmo.contractChangePlan.IDeleteContractChangePlanBMO;
import com.java110.store.bmo.contractChangePlan.IGetContractChangePlanBMO;
import com.java110.store.bmo.contractChangePlan.ISaveContractChangePlanBMO;
import com.java110.store.bmo.contractChangePlan.IUpdateContractChangePlanBMO;
import com.java110.store.bmo.contractChangePlanDetail.IDeleteContractChangePlanDetailBMO;
import com.java110.store.bmo.contractChangePlanDetail.IGetContractChangePlanDetailBMO;
import com.java110.store.bmo.contractChangePlanDetail.ISaveContractChangePlanDetailBMO;
import com.java110.store.bmo.contractChangePlanDetail.IUpdateContractChangePlanDetailBMO;
import com.java110.store.bmo.contractChangePlanDetailAttr.IDeleteContractChangePlanDetailAttrBMO;
import com.java110.store.bmo.contractChangePlanDetailAttr.IGetContractChangePlanDetailAttrBMO;
import com.java110.store.bmo.contractChangePlanDetailAttr.ISaveContractChangePlanDetailAttrBMO;
import com.java110.store.bmo.contractChangePlanDetailAttr.IUpdateContractChangePlanDetailAttrBMO;
import com.java110.store.bmo.contractCollectionPlan.IDeleteContractCollectionPlanBMO;
import com.java110.store.bmo.contractCollectionPlan.IGetContractCollectionPlanBMO;
import com.java110.store.bmo.contractCollectionPlan.ISaveContractCollectionPlanBMO;
import com.java110.store.bmo.contractCollectionPlan.IUpdateContractCollectionPlanBMO;
import com.java110.store.bmo.contractRoom.IDeleteContractRoomBMO;
import com.java110.store.bmo.contractRoom.IGetContractRoomBMO;
import com.java110.store.bmo.contractRoom.ISaveContractRoomBMO;
import com.java110.store.bmo.contractRoom.IUpdateContractRoomBMO;
import com.java110.store.bmo.contractType.IDeleteContractTypeBMO;
import com.java110.store.bmo.contractType.IGetContractTypeBMO;
import com.java110.store.bmo.contractType.ISaveContractTypeBMO;
import com.java110.store.bmo.contractType.IUpdateContractTypeBMO;
import com.java110.store.bmo.contractTypeSpec.IDeleteContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeSpec.IGetContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeSpec.ISaveContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeSpec.IUpdateContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeTemplate.*;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/contract")
public class ContractApi {

    @Autowired
    private ISaveContractBMO saveContractBMOImpl;
    @Autowired
    private IUpdateContractBMO updateContractBMOImpl;
    @Autowired
    private IDeleteContractBMO deleteContractBMOImpl;

    @Autowired
    private IGetContractBMO getContractBMOImpl;

    @Autowired
    private ISaveContractTypeBMO saveContractTypeBMOImpl;
    @Autowired
    private IUpdateContractTypeBMO updateContractTypeBMOImpl;
    @Autowired
    private IDeleteContractTypeBMO deleteContractTypeBMOImpl;

    @Autowired
    private IGetContractTypeBMO getContractTypeBMOImpl;

    @Autowired
    private ISaveContractTypeSpecBMO saveContractTypeSpecBMOImpl;
    @Autowired
    private IUpdateContractTypeSpecBMO updateContractTypeSpecBMOImpl;
    @Autowired
    private IDeleteContractTypeSpecBMO deleteContractTypeSpecBMOImpl;

    @Autowired
    private IGetContractTypeSpecBMO getContractTypeSpecBMOImpl;

    @Autowired
    private ISaveContractAttrBMO saveContractAttrBMOImpl;
    @Autowired
    private IUpdateContractAttrBMO updateContractAttrBMOImpl;
    @Autowired
    private IDeleteContractAttrBMO deleteContractAttrBMOImpl;

    @Autowired
    private IGetContractAttrBMO getContractAttrBMOImpl;

    @Autowired
    private ISaveContractTypeTemplateBMO saveContractTypeTemplateBMOImpl;
    @Autowired
    private IUpdateContractTypeTemplateBMO updateContractTypeTemplateBMOImpl;
    @Autowired
    private IDeleteContractTypeTemplateBMO deleteContractTypeTemplateBMOImpl;

    @Autowired
    private IGetContractTypeTemplateBMO getContractTypeTemplateBMOImpl;


    @Autowired
    private ISaveContractChangePlanBMO saveContractChangePlanBMOImpl;
    @Autowired
    private IUpdateContractChangePlanBMO updateContractChangePlanBMOImpl;
    @Autowired
    private IDeleteContractChangePlanBMO deleteContractChangePlanBMOImpl;

    @Autowired
    private IGetContractChangePlanBMO getContractChangePlanBMOImpl;


    @Autowired
    private ISaveContractChangePlanDetailBMO saveContractChangePlanDetailBMOImpl;
    @Autowired
    private IUpdateContractChangePlanDetailBMO updateContractChangePlanDetailBMOImpl;
    @Autowired
    private IDeleteContractChangePlanDetailBMO deleteContractChangePlanDetailBMOImpl;

    @Autowired
    private IGetContractChangePlanDetailBMO getContractChangePlanDetailBMOImpl;

    @Autowired
    private ISaveContractChangePlanDetailAttrBMO saveContractChangePlanDetailAttrBMOImpl;
    @Autowired
    private IUpdateContractChangePlanDetailAttrBMO updateContractChangePlanDetailAttrBMOImpl;
    @Autowired
    private IDeleteContractChangePlanDetailAttrBMO deleteContractChangePlanDetailAttrBMOImpl;

    @Autowired
    private IGetContractChangePlanDetailAttrBMO getContractChangePlanDetailAttrBMOImpl;

    @Autowired
    private IPrintContractTemplateBMO printContractTemplateBMO;


    @Autowired
    private ISaveContractCollectionPlanBMO saveContractCollectionPlanBMOImpl;
    @Autowired
    private IUpdateContractCollectionPlanBMO updateContractCollectionPlanBMOImpl;
    @Autowired
    private IDeleteContractCollectionPlanBMO deleteContractCollectionPlanBMOImpl;

    @Autowired
    private IGetContractCollectionPlanBMO getContractCollectionPlanBMOImpl;

    @Autowired
    private ISaveContractRoomBMO saveContractRoomBMOImpl;
    @Autowired
    private IUpdateContractRoomBMO updateContractRoomBMOImpl;
    @Autowired
    private IDeleteContractRoomBMO deleteContractRoomBMOImpl;

    @Autowired
    private IGetContractRoomBMO getContractRoomBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContract
     * @path /app/contract/saveContract
     */
    @RequestMapping(value = "/saveContract", method = RequestMethod.POST)
    public ResponseEntity<String> saveContract(@RequestBody JSONObject reqJson,
                                               @RequestHeader(value = "store-id") String storeId,
                                               @RequestHeader(value = "user-id") String userId) {

        Assert.hasKeyAndValue(reqJson, "contractCode", "请求报文中未包含contractCode");
        Assert.hasKeyAndValue(reqJson, "contractName", "请求报文中未包含contractName");
        Assert.hasKeyAndValue(reqJson, "contractType", "请求报文中未包含contractType");
        Assert.hasKeyAndValue(reqJson, "partyA", "请求报文中未包含partyA");
        Assert.hasKeyAndValue(reqJson, "partyB", "请求报文中未包含partyB");
        Assert.hasKeyAndValue(reqJson, "aContacts", "请求报文中未包含aContacts");
        Assert.hasKeyAndValue(reqJson, "aLink", "请求报文中未包含aLink");
        Assert.hasKeyAndValue(reqJson, "bContacts", "请求报文中未包含bContacts");
        Assert.hasKeyAndValue(reqJson, "bLink", "请求报文中未包含bLink");
        Assert.hasKeyAndValue(reqJson, "operator", "请求报文中未包含operator");
        Assert.hasKeyAndValue(reqJson, "operatorLink", "请求报文中未包含operatorLink");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "signingTime", "请求报文中未包含signingTime");

        ContractPo contractPo = BeanConvertUtil.covertBean(reqJson, ContractPo.class);
        contractPo.setStoreId(storeId);
        contractPo.setStartUserId(userId);
        if (!reqJson.containsKey("contractParentId") || "-1".equals(reqJson.getString("contractParentId"))) {
            contractPo.setContractParentId("-1");
        }
        reqJson.put("userId", userId);


        if (reqJson.containsKey("contractFilePo")) {
            JSONArray contractFiles = reqJson.getJSONArray("contractFilePo");
            List<ContractFilePo> contractFilePos = new ArrayList<>();
            for (int conFileIndex = 0; conFileIndex < contractFiles.size(); conFileIndex++) {
                JSONObject resourceStore = contractFiles.getJSONObject(conFileIndex);
                ContractFilePo contractFilePo = BeanConvertUtil.covertBean(resourceStore, ContractFilePo.class);
                contractFilePo.setContractFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_contractFileId));
                contractFilePos.add(contractFilePo);
            }
            contractPo.setContractFilePo(contractFilePos);
        }


        return saveContractBMOImpl.save(contractPo, reqJson);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContract
     * @path /app/contract/updateContract
     */
    @RequestMapping(value = "/updateContract", method = RequestMethod.POST)
    public ResponseEntity<String> updateContract(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractCode", "请求报文中未包含contractCode");
        Assert.hasKeyAndValue(reqJson, "contractName", "请求报文中未包含contractName");
        Assert.hasKeyAndValue(reqJson, "contractType", "请求报文中未包含contractType");
        Assert.hasKeyAndValue(reqJson, "partyA", "请求报文中未包含partyA");
        Assert.hasKeyAndValue(reqJson, "partyB", "请求报文中未包含partyB");
        Assert.hasKeyAndValue(reqJson, "aContacts", "请求报文中未包含aContacts");
        Assert.hasKeyAndValue(reqJson, "aLink", "请求报文中未包含aLink");
        Assert.hasKeyAndValue(reqJson, "bContacts", "请求报文中未包含bContacts");
        Assert.hasKeyAndValue(reqJson, "bLink", "请求报文中未包含bLink");
        Assert.hasKeyAndValue(reqJson, "operator", "请求报文中未包含operator");
        Assert.hasKeyAndValue(reqJson, "operatorLink", "请求报文中未包含operatorLink");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "signingTime", "请求报文中未包含signingTime");
        Assert.hasKeyAndValue(reqJson, "contractId", "contractId不能为空");


        ContractPo contractPo = BeanConvertUtil.covertBean(reqJson, ContractPo.class);

        JSONArray contractFiles = reqJson.getJSONArray("contractFilePo");
        List<ContractFilePo> contractFilePos = new ArrayList<>();
        for (int conFileIndex = 0; conFileIndex < contractFiles.size(); conFileIndex++) {
            JSONObject resourceStore = contractFiles.getJSONObject(conFileIndex);
            ContractFilePo contractFilePo = BeanConvertUtil.covertBean(resourceStore, ContractFilePo.class);
            contractFilePo.setContractFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_contractFileId));
            contractFilePos.add(contractFilePo);
        }
        contractPo.setContractFilePo(contractFilePos);
        return updateContractBMOImpl.update(contractPo, reqJson);
    }


    /**
     * 结束合同
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/stopContract
     * @path /app/contract/stopContract
     */
    @RequestMapping(value = "/stopContract", method = RequestMethod.POST)
    public ResponseEntity<String> stopContract(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "contractId不能为空");

        ContractPo contractPo = BeanConvertUtil.covertBean(reqJson, ContractPo.class);
        contractPo.setState(ContractDto.STATE_COMPLAINT);
        return updateContractBMOImpl.update(contractPo, reqJson);
    }


    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/needAuditContract
     * @path /app/contract/needAuditContract
     */
    @RequestMapping(value = "/needAuditContract", method = RequestMethod.POST)
    public ResponseEntity<String> needAuditContract(
            @RequestHeader(value = "store-id") String storeId,
            @RequestHeader(value = "user-id") String userId,
            @RequestBody JSONObject reqJson) {
        ContractDto contractDto = new ContractDto();
        contractDto.setTaskId(reqJson.getString("taskId"));
        contractDto.setContractId(reqJson.getString("contractId"));
        contractDto.setStoreId(storeId);
        contractDto.setAuditCode(reqJson.getString("state"));
        contractDto.setAuditMessage(reqJson.getString("remark"));
        contractDto.setCurrentUserId(userId);

        return updateContractBMOImpl.needAuditContract(contractDto, reqJson);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/auditContract
     * @path /app/contract/auditContract
     */
    @RequestMapping(value = "/auditContract", method = RequestMethod.POST)
    public ResponseEntity<String> auditContract(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含状态");
        Assert.hasKeyAndValue(reqJson, "stateDesc", "请求报文中未包含状态");
        Assert.hasKeyAndValue(reqJson, "contractId", "contractId不能为空");


        ContractPo contractPo = BeanConvertUtil.covertBean(reqJson, ContractPo.class);
        return updateContractBMOImpl.update(contractPo, reqJson);
    }


    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContract
     * @path /app/contract/deleteContract
     */
    @RequestMapping(value = "/deleteContract", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContract(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "contractId", "contractId不能为空");


        ContractPo contractPo = BeanConvertUtil.covertBean(reqJson, ContractPo.class);
        return deleteContractBMOImpl.delete(contractPo);
    }



    /**
     * 合同起草待办
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractTask
     * @path /app/contract/queryContractTask
     */
    @RequestMapping(value = "/queryContractTask", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractTask(@RequestHeader(value = "store-id") String storeId,
                                                    @RequestHeader(value = "user-id") String userId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(userId);
        auditUser.setPage(page);
        auditUser.setRow(row);
        auditUser.setStoreId(storeId);

        return getContractBMOImpl.queryContractTask(auditUser);
    }

    /**
     * 合同起草已办
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractHistoryTask
     * @path /app/contract/queryContractHistoryTask
     */
    @RequestMapping(value = "/queryContractHistoryTask", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractHistoryTask(@RequestHeader(value = "store-id") String storeId,
                                                           @RequestHeader(value = "user-id") String userId,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {


        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(userId);
        auditUser.setPage(page);
        auditUser.setRow(row);
        auditUser.setStoreId(storeId);

        return getContractBMOImpl.queryContractHistoryTask(auditUser);
    }

    /**
     * 合同变更待办
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractChangeTask
     * @path /app/contract/queryContractChangeTask
     */
    @RequestMapping(value = "/queryContractChangeTask", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractChangeTask(@RequestHeader(value = "store-id") String storeId,
                                                          @RequestHeader(value = "user-id") String userId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row) {
        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(userId);
        auditUser.setPage(page);
        auditUser.setRow(row);
        auditUser.setStoreId(storeId);

        return getContractBMOImpl.queryContractChangeTask(auditUser);
    }

    /**
     * 合同变更已办
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractChangeHistoryTask
     * @path /app/contract/queryContractChangeHistoryTask
     */
    @RequestMapping(value = "/queryContractChangeHistoryTask", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractChangeHistoryTask(@RequestHeader(value = "store-id") String storeId,
                                                                 @RequestHeader(value = "user-id") String userId,
                                                                 @RequestParam(value = "page") int page,
                                                                 @RequestParam(value = "row") int row) {


        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(userId);
        auditUser.setPage(page);
        auditUser.setRow(row);
        auditUser.setStoreId(storeId);
        return getContractBMOImpl.queryContractChangeHistoryTask(auditUser);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractType
     * @path /app/contract/saveContractType
     */
    @RequestMapping(value = "/saveContractType", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractType(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id", required = false) String storeId) {

        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "audit", "请求报文中未包含audit");


        ContractTypePo contractTypePo = BeanConvertUtil.covertBean(reqJson, ContractTypePo.class);
        contractTypePo.setStoreId(storeId);
        return saveContractTypeBMOImpl.save(contractTypePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractType
     * @path /app/contract/updateContractType
     */
    @RequestMapping(value = "/updateContractType", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractType(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "audit", "请求报文中未包含audit");
        Assert.hasKeyAndValue(reqJson, "contractTypeId", "contractTypeId不能为空");


        ContractTypePo contractTypePo = BeanConvertUtil.covertBean(reqJson, ContractTypePo.class);
        return updateContractTypeBMOImpl.update(contractTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractType
     * @path /app/contract/deleteContractType
     */
    @RequestMapping(value = "/deleteContractType", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractType(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "contractTypeId", "contractTypeId不能为空");


        ContractTypePo contractTypePo = BeanConvertUtil.covertBean(reqJson, ContractTypePo.class);
        return deleteContractTypeBMOImpl.delete(contractTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractType
     * @path /app/contract/queryContractType
     */
    @RequestMapping(value = "/queryContractType", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractType(@RequestHeader(value = "store-id") String storeId,
                                                    @RequestParam(value = "audit", required = false) String audit,
                                                    @RequestParam(value = "typeName", required = false) String typeName,
                                                    @RequestParam(value = "contractTypeId", required = false) String contractTypeId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.setPage(page);
        contractTypeDto.setRow(row);
        contractTypeDto.setAudit(audit);
        contractTypeDto.setTypeName(typeName);
        contractTypeDto.setContractTypeId(contractTypeId);
        contractTypeDto.setStoreId(storeId);
        return getContractTypeBMOImpl.get(contractTypeDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractTypeSpec
     * @path /app/contract/saveContractTypeSpec
     */
    @RequestMapping(value = "/saveContractTypeSpec", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractTypeSpec(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        // Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "contractTypeId", "请求报文中未包含contractTypeId");
        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");
        Assert.hasKeyAndValue(reqJson, "specHoldplace", "请求报文中未包含specHoldplace");
        Assert.hasKeyAndValue(reqJson, "required", "请求报文中未包含required");
        Assert.hasKeyAndValue(reqJson, "specShow", "请求报文中未包含specShow");
        Assert.hasKeyAndValue(reqJson, "specValueType", "请求报文中未包含specValueType");
        Assert.hasKeyAndValue(reqJson, "specType", "请求报文中未包含specType");
        Assert.hasKeyAndValue(reqJson, "listShow", "请求报文中未包含listShow");


        ContractTypeSpecPo contractTypeSpecPo = BeanConvertUtil.covertBean(reqJson, ContractTypeSpecPo.class);
        contractTypeSpecPo.setStoreId(storeId);
        return saveContractTypeSpecBMOImpl.save(contractTypeSpecPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractTypeSpec
     * @path /app/contract/updateContractTypeSpec
     */
    @RequestMapping(value = "/updateContractTypeSpec", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractTypeSpec(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "contractTypeId", "请求报文中未包含contractTypeId");
        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");
        Assert.hasKeyAndValue(reqJson, "specHoldplace", "请求报文中未包含specHoldplace");
        Assert.hasKeyAndValue(reqJson, "required", "请求报文中未包含required");
        Assert.hasKeyAndValue(reqJson, "specShow", "请求报文中未包含specShow");
        Assert.hasKeyAndValue(reqJson, "specValueType", "请求报文中未包含specValueType");
        Assert.hasKeyAndValue(reqJson, "specType", "请求报文中未包含specType");
        Assert.hasKeyAndValue(reqJson, "listShow", "请求报文中未包含listShow");
        Assert.hasKeyAndValue(reqJson, "specCd", "specCd不能为空");


        ContractTypeSpecPo contractTypeSpecPo = BeanConvertUtil.covertBean(reqJson, ContractTypeSpecPo.class);
        return updateContractTypeSpecBMOImpl.update(contractTypeSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractTypeSpec
     * @path /app/contract/deleteContractTypeSpec
     */
    @RequestMapping(value = "/deleteContractTypeSpec", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractTypeSpec(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "specCd", "specCd不能为空");


        ContractTypeSpecPo contractTypeSpecPo = BeanConvertUtil.covertBean(reqJson, ContractTypeSpecPo.class);
        return deleteContractTypeSpecBMOImpl.delete(contractTypeSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /contract/queryContractTypeSpec
     * @path /app/contract/queryContractTypeSpec
     */
    @RequestMapping(value = "/queryContractTypeSpec", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractTypeSpec(@RequestParam(value = "specName", required = false) String specName,
                                                        @RequestParam(value = "specShow", required = false) String specShow,
                                                        @RequestParam(value = "specCd", required = false) String specCd,
                                                        @RequestHeader(value = "store-id") String storeId,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row,
                                                        @RequestParam(value = "contractTypeId") String contractTypeId) {
        ContractTypeSpecDto contractTypeSpecDto = new ContractTypeSpecDto();
        contractTypeSpecDto.setPage(page);
        contractTypeSpecDto.setRow(row);
        contractTypeSpecDto.setStoreId(storeId);
        contractTypeSpecDto.setContractTypeId(contractTypeId);
        contractTypeSpecDto.setSpecName(specName);
        contractTypeSpecDto.setSpecShow(specShow);
        contractTypeSpecDto.setSpecCd(specCd);
        return getContractTypeSpecBMOImpl.get(contractTypeSpecDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractAttr
     * @path /app/contract/saveContractAttr
     */
    @RequestMapping(value = "/saveContractAttr", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractAttr(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");


        ContractAttrPo contractAttrPo = BeanConvertUtil.covertBean(reqJson, ContractAttrPo.class);
        return saveContractAttrBMOImpl.save(contractAttrPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractAttr
     * @path /app/contract/updateContractAttr
     */
    @RequestMapping(value = "/updateContractAttr", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractAttr(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");


        ContractAttrPo contractAttrPo = BeanConvertUtil.covertBean(reqJson, ContractAttrPo.class);
        return updateContractAttrBMOImpl.update(contractAttrPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractAttr
     * @path /app/contract/deleteContractAttr
     */
    @RequestMapping(value = "/deleteContractAttr", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractAttr(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");


        ContractAttrPo contractAttrPo = BeanConvertUtil.covertBean(reqJson, ContractAttrPo.class);
        return deleteContractAttrBMOImpl.delete(contractAttrPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /contract/queryContractAttr
     * @path /app/contract/queryContractAttr
     */
    @RequestMapping(value = "/queryContractAttr", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractAttr(@RequestHeader(value = "store-id") String storeId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ContractAttrDto contractAttrDto = new ContractAttrDto();
        contractAttrDto.setPage(page);
        contractAttrDto.setRow(row);
        contractAttrDto.setStoreId(storeId);
        return getContractAttrBMOImpl.get(contractAttrDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractTypeTemplate
     * @path /app/contract/saveContractTypeTemplate
     */
    @RequestMapping(value = "/saveContractTypeTemplate", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractTypeTemplate(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "contractTypeId", "请求报文中未包含contractTypeId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");


        ContractTypeTemplatePo contractTypeTemplatePo = BeanConvertUtil.covertBean(reqJson, ContractTypeTemplatePo.class);
        contractTypeTemplatePo.setStoreId(storeId);
        return saveContractTypeTemplateBMOImpl.save(contractTypeTemplatePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractTypeTemplate
     * @path /app/contract/updateContractTypeTemplate
     */
    @RequestMapping(value = "/updateContractTypeTemplate", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractTypeTemplate(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractTypeId", "请求报文中未包含contractTypeId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");
        Assert.hasKeyAndValue(reqJson, "templateId", "templateId不能为空");


        ContractTypeTemplatePo contractTypeTemplatePo = BeanConvertUtil.covertBean(reqJson, ContractTypeTemplatePo.class);
        return updateContractTypeTemplateBMOImpl.update(contractTypeTemplatePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractTypeTemplate
     * @path /app/contract/deleteContractTypeTemplate
     */
    @RequestMapping(value = "/deleteContractTypeTemplate", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractTypeTemplate(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "templateId", "templateId不能为空");


        ContractTypeTemplatePo contractTypeTemplatePo = BeanConvertUtil.covertBean(reqJson, ContractTypeTemplatePo.class);
        return deleteContractTypeTemplateBMOImpl.delete(contractTypeTemplatePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractTypeTemplate
     * @path /app/contract/queryContractTypeTemplate
     */
    @RequestMapping(value = "/queryContractTypeTemplate", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractTypeTemplate(@RequestHeader(value = "store-id") String storeId,
                                                            @RequestParam(value = "contractTypeId", required = false) String contractTypeId,
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        ContractTypeTemplateDto contractTypeTemplateDto = new ContractTypeTemplateDto();
        contractTypeTemplateDto.setPage(page);
        contractTypeTemplateDto.setRow(row);
        contractTypeTemplateDto.setStoreId(storeId);
        contractTypeTemplateDto.setContractTypeId(contractTypeId);
        return getContractTypeTemplateBMOImpl.get(contractTypeTemplateDto);
    }


    /**
     * 微信保存消息模板
     * {"index":0,"contractName":"测试合同","contractCode":"1","contractType":"812021030474360091","partyA":"吴学文","partyB":"吴学文","aContacts":"吴学文",
     * "bContacts":"吴学文11","aLink":"18909711443","bLink":"18909711443","operator":"1","operatorLink":"13789876589","amount":"100.00",
     * "startTime":"2021-03-10 00:00:50","endTime":"2021-03-03 01:05:50","signingTime":"2021-03-02 00:00:50","param":"contractChangeMainBody",
     * "planType":"1001","changeRemark":"菜单"}
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractChangePlan
     * @path /app/contract/saveContractChangePlan
     */
    @RequestMapping(value = "/saveContractChangePlan", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractChangePlan(@RequestHeader(value = "store-id") String storeId,
                                                         @RequestHeader(value = "user-id") String userId,
                                                         @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "planType", "请求报文中未包含planType");

        ContractChangePlanPo contractChangePlanPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanPo.class);
        contractChangePlanPo.setStoreId(storeId);
        contractChangePlanPo.setChangePerson(userId);

        contractChangePlanPo.setState(ContractChangePlanDto.STATE_W);
        contractChangePlanPo.setRemark(reqJson.getString("changeRemark"));

        List<ContractChangePlanRoomPo> contractChangePlanRoomPos = new ArrayList<>();
        ContractChangePlanRoomPo contractChangePlanRoomPo = null;
        JSONObject roomInfo = null;
        if (reqJson.containsKey("rooms")) {
            JSONArray rooms = reqJson.getJSONArray("rooms");
            if (rooms != null && rooms.size() > 0) {
                for (int roomIndex = 0; roomIndex < rooms.size(); roomIndex++) {
//                    contractChangePlanRoomPos.add(BeanConvertUtil.covertBean(rooms.getJSONObject(roomIndex), ContractChangePlanRoomPo.class));
                    roomInfo = rooms.getJSONObject(roomIndex);
                    contractChangePlanRoomPo = BeanConvertUtil.covertBean(roomInfo, ContractChangePlanRoomPo.class);
                    contractChangePlanRoomPo.setRoomName(roomInfo.getString("floorNum")
                            + "-" + roomInfo.getString("unitNum")
                            + "-" + roomInfo.getString("roomNum"));
                    contractChangePlanRoomPos.add(contractChangePlanRoomPo);
                }
            }
        }

        ContractChangePlanDetailPo contractChangePlanDetailPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailPo.class);
        contractChangePlanDetailPo.setStoreId(storeId);
        return saveContractChangePlanBMOImpl.save(contractChangePlanPo, contractChangePlanDetailPo, contractChangePlanRoomPos, reqJson);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractChangePlan
     * @path /app/contract/updateContractChangePlan
     */
    @RequestMapping(value = "/updateContractChangePlan", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractChangePlan(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "planType", "请求报文中未包含planType");
        Assert.hasKeyAndValue(reqJson, "changePerson", "请求报文中未包含changePerson");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "planId", "planId不能为空");


        ContractChangePlanPo contractChangePlanPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanPo.class);
        return updateContractChangePlanBMOImpl.update(contractChangePlanPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractChangePlan
     * @path /app/contract/deleteContractChangePlan
     */
    @RequestMapping(value = "/deleteContractChangePlan", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractChangePlan(@RequestHeader(value = "store-id") String storeId,
                                                           @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "planId", "planId不能为空");

        ContractChangePlanPo contractChangePlanPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanPo.class);
        contractChangePlanPo.setStoreId(storeId);
        return deleteContractChangePlanBMOImpl.delete(contractChangePlanPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractChangePlan
     * @path /app/contract/queryContractChangePlan
     */
    @RequestMapping(value = "/queryContractChangePlan", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractChangePlan(@RequestHeader(value = "store-id") String storeId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row,
                                                          @RequestParam(value = "contractId", required = false) String contractId,
                                                          @RequestParam(value = "contractName", required = false) String contractName,
                                                          @RequestParam(value = "contractCode", required = false) String contractCode,
                                                          @RequestParam(value = "contractType", required = false) String contractType,
                                                          @RequestParam(value = "planId", required = false) String planId
    ) {
        ContractChangePlanDto contractChangePlanDto = new ContractChangePlanDto();
        contractChangePlanDto.setPage(page);
        contractChangePlanDto.setRow(row);
        contractChangePlanDto.setStoreId(storeId);
        contractChangePlanDto.setContractId(contractId);
        contractChangePlanDto.setContractName(contractName);
        contractChangePlanDto.setPlanId(planId);
        contractChangePlanDto.setContractCode(contractCode);
        contractChangePlanDto.setContractType(contractType);
        return getContractChangePlanBMOImpl.get(contractChangePlanDto);
    }


    /**
     * 合同变更审核
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/needAuditContractPlan
     * @path /app/contract/needAuditContractPlan
     */
    @RequestMapping(value = "/needAuditContractPlan", method = RequestMethod.POST)
    public ResponseEntity<String> needAuditContractPlan(
            @RequestHeader(value = "store-id") String storeId,
            @RequestHeader(value = "user-id") String userId,
            @RequestBody JSONObject reqJson) {
        ContractChangePlanDto contractChangePlanDto = new ContractChangePlanDto();
        contractChangePlanDto.setTaskId(reqJson.getString("taskId"));
        contractChangePlanDto.setPlanId(reqJson.getString("planId"));
        contractChangePlanDto.setContractId(reqJson.getString("contractId"));
        contractChangePlanDto.setStoreId(storeId);
        contractChangePlanDto.setAuditCode(reqJson.getString("state"));
        contractChangePlanDto.setAuditMessage(reqJson.getString("remark"));
        contractChangePlanDto.setCurrentUserId(userId);

        return updateContractBMOImpl.needAuditContractPlan(contractChangePlanDto, reqJson);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractChangePlanDetail
     * @path /app/contract/saveContractChangePlanDetail
     */
    @RequestMapping(value = "/saveContractChangePlanDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractChangePlanDetail(@RequestHeader(value = "store-id") String storeId,
                                                               @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractCode", "请求报文中未包含contractCode");
        Assert.hasKeyAndValue(reqJson, "contractName", "请求报文中未包含contractName");
        Assert.hasKeyAndValue(reqJson, "contractType", "请求报文中未包含contractType");
        Assert.hasKeyAndValue(reqJson, "partyA", "请求报文中未包含partyA");
        Assert.hasKeyAndValue(reqJson, "partyB", "请求报文中未包含partyB");
        Assert.hasKeyAndValue(reqJson, "aContacts", "请求报文中未包含aContacts");
        Assert.hasKeyAndValue(reqJson, "aLink", "请求报文中未包含aLink");
        Assert.hasKeyAndValue(reqJson, "bContacts", "请求报文中未包含bContacts");
        Assert.hasKeyAndValue(reqJson, "bLink", "请求报文中未包含bLink");
        Assert.hasKeyAndValue(reqJson, "operator", "请求报文中未包含operator");
        Assert.hasKeyAndValue(reqJson, "operatorLink", "请求报文中未包含operatorLink");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "signingTime", "请求报文中未包含signingTime");


        ContractChangePlanDetailPo contractChangePlanDetailPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailPo.class);
        contractChangePlanDetailPo.setStoreId(storeId);
        return saveContractChangePlanDetailBMOImpl.save(contractChangePlanDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractChangePlanDetail
     * @path /app/contract/updateContractChangePlanDetail
     */
    @RequestMapping(value = "/updateContractChangePlanDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractChangePlanDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractCode", "请求报文中未包含contractCode");
        Assert.hasKeyAndValue(reqJson, "contractName", "请求报文中未包含contractName");
        Assert.hasKeyAndValue(reqJson, "contractType", "请求报文中未包含contractType");
        Assert.hasKeyAndValue(reqJson, "partyA", "请求报文中未包含partyA");
        Assert.hasKeyAndValue(reqJson, "partyB", "请求报文中未包含partyB");
        Assert.hasKeyAndValue(reqJson, "aContacts", "请求报文中未包含aContacts");
        Assert.hasKeyAndValue(reqJson, "aLink", "请求报文中未包含aLink");
        Assert.hasKeyAndValue(reqJson, "bContacts", "请求报文中未包含bContacts");
        Assert.hasKeyAndValue(reqJson, "bLink", "请求报文中未包含bLink");
        Assert.hasKeyAndValue(reqJson, "operator", "请求报文中未包含operator");
        Assert.hasKeyAndValue(reqJson, "operatorLink", "请求报文中未包含operatorLink");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "signingTime", "请求报文中未包含signingTime");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ContractChangePlanDetailPo contractChangePlanDetailPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailPo.class);
        return updateContractChangePlanDetailBMOImpl.update(contractChangePlanDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractChangePlanDetail
     * @path /app/contract/deleteContractChangePlanDetail
     */
    @RequestMapping(value = "/deleteContractChangePlanDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractChangePlanDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        ContractChangePlanDetailPo contractChangePlanDetailPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailPo.class);
        return deleteContractChangePlanDetailBMOImpl.delete(contractChangePlanDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /contract/queryContractChangePlanDetail
     * @path /app/contract/queryContractChangePlanDetail
     */
    @RequestMapping(value = "/queryContractChangePlanDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractChangePlanDetail(@RequestHeader(value = "store-id") String storeId,
                                                                @RequestParam(value = "planId", required = false) String planId,
                                                                @RequestParam(value = "contractId", required = false) String contractId,
                                                                @RequestParam(value = "page") int page,
                                                                @RequestParam(value = "row") int row) {
        ContractChangePlanDetailDto contractChangePlanDetailDto = new ContractChangePlanDetailDto();
        contractChangePlanDetailDto.setPage(page);
        contractChangePlanDetailDto.setRow(row);
        contractChangePlanDetailDto.setStoreId(storeId);
        contractChangePlanDetailDto.setPlanId(planId);
        contractChangePlanDetailDto.setContractId(contractId);
        return getContractChangePlanDetailBMOImpl.get(contractChangePlanDetailDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractChangePlanDetailAttr
     * @path /app/contract/saveContractChangePlanDetailAttr
     */
    @RequestMapping(value = "/saveContractChangePlanDetailAttr", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractChangePlanDetailAttr(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");


        ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailAttrPo.class);
        return saveContractChangePlanDetailAttrBMOImpl.save(contractChangePlanDetailAttrPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractChangePlanDetailAttr
     * @path /app/contract/updateContractChangePlanDetailAttr
     */
    @RequestMapping(value = "/updateContractChangePlanDetailAttr", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractChangePlanDetailAttr(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");

        ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailAttrPo.class);
        return updateContractChangePlanDetailAttrBMOImpl.update(contractChangePlanDetailAttrPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractChangePlanDetailAttr
     * @path /app/contract/deleteContractChangePlanDetailAttr
     */
    @RequestMapping(value = "/deleteContractChangePlanDetailAttr", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractChangePlanDetailAttr(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");


        ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo = BeanConvertUtil.covertBean(reqJson, ContractChangePlanDetailAttrPo.class);
        return deleteContractChangePlanDetailAttrBMOImpl.delete(contractChangePlanDetailAttrPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractChangePlanDetailAttr
     * @path /app/contract/queryContractChangePlanDetailAttr
     */
    @RequestMapping(value = "/queryContractChangePlanDetailAttr", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractChangePlanDetailAttr(@RequestHeader(value = "store-id") String storeId,
                                                                    @RequestParam(value = "page") int page,
                                                                    @RequestParam(value = "row") int row) {
        ContractChangePlanDetailAttrDto contractChangePlanDetailAttrDto = new ContractChangePlanDetailAttrDto();
        contractChangePlanDetailAttrDto.setPage(page);
        contractChangePlanDetailAttrDto.setRow(row);
        contractChangePlanDetailAttrDto.setStoreId(storeId);
        return getContractChangePlanDetailAttrBMOImpl.get(contractChangePlanDetailAttrDto);
    }

    /**
     * 合同打印
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/printContractTemplate
     * @path /app/contract/printContractTemplate
     */
    @RequestMapping(value = "/printContractTemplate", method = RequestMethod.GET)
    public ResponseEntity<String> printContractTemplate(@RequestHeader(value = "store-id") String storeId,
                                                        @RequestParam(value = "contractTypeId", required = false) String contractTypeId,
                                                        @RequestParam(value = "contractId", required = false) String contractId,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        ContractTypeTemplateDto contractTypeTemplateDto = new ContractTypeTemplateDto();
        contractTypeTemplateDto.setPage(page);
        contractTypeTemplateDto.setRow(row);
        contractTypeTemplateDto.setStoreId(storeId);
        contractTypeTemplateDto.setContractTypeId(contractTypeId);

        ContractDto contractDto = new ContractDto();
        contractDto.setPage(page);
        contractDto.setRow(row);
        contractDto.setStoreId(storeId);
        contractDto.setContractId(contractId);

        ContractTypeSpecDto contractTypeSpecDto = new ContractTypeSpecDto();
        contractTypeSpecDto.setPage(page);
        contractTypeSpecDto.setRow(100);
        contractTypeSpecDto.setStoreId(storeId);
        contractTypeSpecDto.setContractTypeId(contractTypeId);

        return printContractTemplateBMO.get(contractTypeTemplateDto, contractDto, contractTypeSpecDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractCollectionPlan
     * @path /app/contract/saveContractCollectionPlan
     */
    @RequestMapping(value = "/saveContractCollectionPlan", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractCollectionPlan(@RequestHeader(value = "store-id") String storeId,
                                                             @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "planName", "请求报文中未包含planName");


        ContractCollectionPlanPo contractCollectionPlanPo = BeanConvertUtil.covertBean(reqJson, ContractCollectionPlanPo.class);
        contractCollectionPlanPo.setStoreId(storeId);
        return saveContractCollectionPlanBMOImpl.save(contractCollectionPlanPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractCollectionPlan
     * @path /app/contract/updateContractCollectionPlan
     */
    @RequestMapping(value = "/updateContractCollectionPlan", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractCollectionPlan(@RequestHeader(value = "store-id") String storeId,
                                                               @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "feeId", "请求报文中未包含feeId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "planName", "请求报文中未包含planName");
        Assert.hasKeyAndValue(reqJson, "planId", "planId不能为空");


        ContractCollectionPlanPo contractCollectionPlanPo = BeanConvertUtil.covertBean(reqJson, ContractCollectionPlanPo.class);
        contractCollectionPlanPo.setStoreId(storeId);
        return updateContractCollectionPlanBMOImpl.update(contractCollectionPlanPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractCollectionPlan
     * @path /app/contract/deleteContractCollectionPlan
     */
    @RequestMapping(value = "/deleteContractCollectionPlan", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractCollectionPlan(@RequestHeader(value = "store-id") String storeId,
                                                               @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "planId", "planId不能为空");


        ContractCollectionPlanPo contractCollectionPlanPo = BeanConvertUtil.covertBean(reqJson, ContractCollectionPlanPo.class);
        contractCollectionPlanPo.setStoreId(storeId);
        return deleteContractCollectionPlanBMOImpl.delete(contractCollectionPlanPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContractCollectionPlan
     * @path /app/contract/queryContractCollectionPlan
     */
    @RequestMapping(value = "/queryContractCollectionPlan", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractCollectionPlan(@RequestHeader(value = "store-id") String storeId,
                                                              @RequestParam(value = "communityId") String communityId,
                                                              @RequestParam(value = "page") int page,
                                                              @RequestParam(value = "row") int row) {
        ContractCollectionPlanDto contractCollectionPlanDto = new ContractCollectionPlanDto();
        contractCollectionPlanDto.setPage(page);
        contractCollectionPlanDto.setRow(row);
        contractCollectionPlanDto.setStoreId(storeId);
        return getContractCollectionPlanBMOImpl.get(contractCollectionPlanDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContractRoom
     * @path /app/contract/saveContractRoom
     */
    @RequestMapping(value = "/saveContractRoom", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractRoom(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");


        ContractRoomPo contractRoomPo = BeanConvertUtil.covertBean(reqJson, ContractRoomPo.class);
        return saveContractRoomBMOImpl.save(contractRoomPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/updateContractRoom
     * @path /app/contract/updateContractRoom
     */
    @RequestMapping(value = "/updateContractRoom", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractRoom(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "crId", "crId不能为空");


        ContractRoomPo contractRoomPo = BeanConvertUtil.covertBean(reqJson, ContractRoomPo.class);
        return updateContractRoomBMOImpl.update(contractRoomPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/deleteContractRoom
     * @path /app/contract/deleteContractRoom
     */
    @RequestMapping(value = "/deleteContractRoom", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractRoom(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "crId", "crId不能为空");


        ContractRoomPo contractRoomPo = BeanConvertUtil.covertBean(reqJson, ContractRoomPo.class);
        return deleteContractRoomBMOImpl.delete(contractRoomPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /contract/queryContractRoom
     * @path /app/contract/queryContractRoom
     */
    @RequestMapping(value = "/queryContractRoom", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractRoom(@RequestHeader(value = "store-id") String storeId,
                                                    @RequestParam(value = "contractId", required = false) String contractId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ContractRoomDto contractRoomDto = new ContractRoomDto();
        contractRoomDto.setPage(page);
        contractRoomDto.setRow(row);
        contractRoomDto.setStoreId(storeId);
        contractRoomDto.setContractId(contractId);
        return getContractRoomBMOImpl.get(contractRoomDto);
    }

}
