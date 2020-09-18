package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.contractType.ContractTypeDto;
import com.java110.po.contract.ContractPo;
import com.java110.po.contractType.ContractTypePo;
import com.java110.store.bmo.contract.IDeleteContractBMO;
import com.java110.store.bmo.contract.IGetContractBMO;
import com.java110.store.bmo.contract.ISaveContractBMO;
import com.java110.store.bmo.contract.IUpdateContractBMO;
import com.java110.store.bmo.contractType.IDeleteContractTypeBMO;
import com.java110.store.bmo.contractType.IGetContractTypeBMO;
import com.java110.store.bmo.contractType.ISaveContractTypeBMO;
import com.java110.store.bmo.contractType.IUpdateContractTypeBMO;
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
        return saveContractBMOImpl.save(contractPo);
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
        return updateContractBMOImpl.update(contractPo);
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
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "row") int row) {
        ContractDto contractDto = new ContractDto();
        contractDto.setPage(page);
        contractDto.setRow(row);
        contractDto.setStoreId(storeId);
        return getContractBMOImpl.get(contractDto);
    }

    /**
     * 微信保存消息模板
     * @serviceCode /contract/saveContractType
     * @path /app/contract/saveContractType
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveContractType", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractType(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "audit", "请求报文中未包含audit");


        ContractTypePo contractTypePo = BeanConvertUtil.covertBean(reqJson, ContractTypePo.class);
        return saveContractTypeBMOImpl.save(contractTypePo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /contract/updateContractType
     * @path /app/contract/updateContractType
     * @param reqJson
     * @return
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
     * @serviceCode /contract/deleteContractType
     * @path /app/contract/deleteContractType
     * @param reqJson
     * @return
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
     * @serviceCode /contract/queryContractType
     * @path /app/contract/queryContractType
     * @param storeId 商户ID
     * @return
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
}
