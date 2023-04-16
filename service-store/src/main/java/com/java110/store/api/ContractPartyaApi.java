package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.contract.ContractPartyaDto;
import com.java110.po.contractPartya.ContractPartyaPo;
import com.java110.store.bmo.contractPartya.IDeleteContractPartyaBMO;
import com.java110.store.bmo.contractPartya.IGetContractPartyaBMO;
import com.java110.store.bmo.contractPartya.ISaveContractPartyaBMO;
import com.java110.store.bmo.contractPartya.IUpdateContractPartyaBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/contractPartya")
public class ContractPartyaApi {

    @Autowired
    private ISaveContractPartyaBMO saveContractPartyaBMOImpl;
    @Autowired
    private IUpdateContractPartyaBMO updateContractPartyaBMOImpl;
    @Autowired
    private IDeleteContractPartyaBMO deleteContractPartyaBMOImpl;

    @Autowired
    private IGetContractPartyaBMO getContractPartyaBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contractPartya/saveContractPartya
     * @path /app/contractPartya/saveContractPartya
     */
    @RequestMapping(value = "/saveContractPartya", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractPartya(@RequestHeader(value = "store-id") String storeId,
                                                     @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "partyA", "请求报文中未包含partyA");
        Assert.hasKeyAndValue(reqJson, "aContacts", "请求报文中未包含aContacts");
        Assert.hasKeyAndValue(reqJson, "aLink", "请求报文中未包含aLink");


        ContractPartyaPo contractPartyaPo = BeanConvertUtil.covertBean(reqJson, ContractPartyaPo.class);
        contractPartyaPo.setStoreId(storeId);

        return saveContractPartyaBMOImpl.save(contractPartyaPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contractPartya/updateContractPartya
     * @path /app/contractPartya/updateContractPartya
     */
    @RequestMapping(value = "/updateContractPartya", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractPartya(@RequestHeader(value = "store-id") String storeId,
                                                       @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "partyA", "请求报文中未包含partyA");
        Assert.hasKeyAndValue(reqJson, "aContacts", "请求报文中未包含aContacts");
        Assert.hasKeyAndValue(reqJson, "aLink", "请求报文中未包含aLink");
        Assert.hasKeyAndValue(reqJson, "partyaId", "partyaId不能为空");


        ContractPartyaPo contractPartyaPo = BeanConvertUtil.covertBean(reqJson, ContractPartyaPo.class);
        contractPartyaPo.setStoreId(storeId);

        return updateContractPartyaBMOImpl.update(contractPartyaPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /contractPartya/deleteContractPartya
     * @path /app/contractPartya/deleteContractPartya
     */
    @RequestMapping(value = "/deleteContractPartya", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractPartya(@RequestHeader(value = "store-id") String storeId,
                                                       @RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "partyaId", "partyaId不能为空");


        ContractPartyaPo contractPartyaPo = BeanConvertUtil.covertBean(reqJson, ContractPartyaPo.class);
        contractPartyaPo.setStoreId(storeId);
        return deleteContractPartyaBMOImpl.delete(contractPartyaPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /contractPartya/queryContractPartya
     * @path /app/contractPartya/queryContractPartya
     */
    @RequestMapping(value = "/queryContractPartya", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractPartya(@RequestHeader(value = "store-id") String storeId,
                                                      @RequestParam(value = "partyA", required = false) String partyA,
                                                      @RequestParam(value = "aContacts", required = false) String aContacts,
                                                      @RequestParam(value = "aLink", required = false) String aLink,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ContractPartyaDto contractPartyaDto = new ContractPartyaDto();
        contractPartyaDto.setPage(page);
        contractPartyaDto.setRow(row);
        contractPartyaDto.setStoreId(storeId);
        contractPartyaDto.setPartyA(partyA);
        contractPartyaDto.setaContacts(aContacts);
        contractPartyaDto.setaLink(aLink);
        return getContractPartyaBMOImpl.get(contractPartyaDto);
    }
}
