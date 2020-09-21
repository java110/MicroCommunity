package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractAttr.ContractAttrDto;
import com.java110.dto.contractType.ContractTypeDto;
import com.java110.dto.contractTypeSpec.ContractTypeSpecDto;
import com.java110.dto.contractTypeTemplate.ContractTypeTemplateDto;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractAttr.ContractAttrPo;
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
import com.java110.store.bmo.contractType.IDeleteContractTypeBMO;
import com.java110.store.bmo.contractType.IGetContractTypeBMO;
import com.java110.store.bmo.contractType.ISaveContractTypeBMO;
import com.java110.store.bmo.contractType.IUpdateContractTypeBMO;
import com.java110.store.bmo.contractTypeSpec.IDeleteContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeSpec.IGetContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeSpec.ISaveContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeSpec.IUpdateContractTypeSpecBMO;
import com.java110.store.bmo.contractTypeTemplate.IDeleteContractTypeTemplateBMO;
import com.java110.store.bmo.contractTypeTemplate.IGetContractTypeTemplateBMO;
import com.java110.store.bmo.contractTypeTemplate.ISaveContractTypeTemplateBMO;
import com.java110.store.bmo.contractTypeTemplate.IUpdateContractTypeTemplateBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contract/saveContract
     * @path /app/contract/saveContract
     */
    @RequestMapping(value = "/saveContract", method = RequestMethod.POST)
    public ResponseEntity<String> saveContract(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

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
        return updateContractBMOImpl.update(contractPo, reqJson);
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
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /contract/queryContract
     * @path /app/contract/queryContract
     */
    @RequestMapping(value = "/queryContract", method = RequestMethod.GET)
    public ResponseEntity<String> queryContract(@RequestHeader(value = "store-id") String storeId,
                                                @RequestParam(value = "state", required = false) String state,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "row") int row) {
        ContractDto contractDto = new ContractDto();
        contractDto.setPage(page);
        contractDto.setRow(row);
        contractDto.setStoreId(storeId);
        contractDto.setState(state);
        return getContractBMOImpl.get(contractDto);
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
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ContractTypeDto contractTypeDto = new ContractTypeDto();
        contractTypeDto.setPage(page);
        contractTypeDto.setRow(row);
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
    public ResponseEntity<String> queryContractTypeSpec(@RequestHeader(value = "store-id") String storeId,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row,
                                                        @RequestParam(value = "contractTypeId") String contractTypeId) {
        ContractTypeSpecDto contractTypeSpecDto = new ContractTypeSpecDto();
        contractTypeSpecDto.setPage(page);
        contractTypeSpecDto.setRow(row);
        contractTypeSpecDto.setStoreId(storeId);
        contractTypeSpecDto.setContractTypeId(contractTypeId);
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
    public ResponseEntity<String> saveContractTypeTemplate(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractTypeId", "请求报文中未包含contractTypeId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");


        ContractTypeTemplatePo contractTypeTemplatePo = BeanConvertUtil.covertBean(reqJson, ContractTypeTemplatePo.class);
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
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含storeId");
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
                                                            @RequestParam(value = "page") int page,
                                                            @RequestParam(value = "row") int row) {
        ContractTypeTemplateDto contractTypeTemplateDto = new ContractTypeTemplateDto();
        contractTypeTemplateDto.setPage(page);
        contractTypeTemplateDto.setRow(row);
        contractTypeTemplateDto.setStoreId(storeId);
        return getContractTypeTemplateBMOImpl.get(contractTypeTemplateDto);
    }

}
