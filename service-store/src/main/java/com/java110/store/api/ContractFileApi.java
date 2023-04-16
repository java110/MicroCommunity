package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.contract.ContractFileDto;
import com.java110.po.contractFile.ContractFilePo;
import com.java110.store.bmo.contractFile.IDeleteContractFileBMO;
import com.java110.store.bmo.contractFile.IGetContractFileBMO;
import com.java110.store.bmo.contractFile.ISaveContractFileBMO;
import com.java110.store.bmo.contractFile.IUpdateContractFileBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/contractFile")
public class ContractFileApi {

    @Autowired
    private ISaveContractFileBMO saveContractFileBMOImpl;
    @Autowired
    private IUpdateContractFileBMO updateContractFileBMOImpl;
    @Autowired
    private IDeleteContractFileBMO deleteContractFileBMOImpl;

    @Autowired
    private IGetContractFileBMO getContractFileBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /contractFile/saveContractFile
     * @path /app/contractFile/saveContractFile
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveContractFile", method = RequestMethod.POST)
    public ResponseEntity<String> saveContractFile(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "fileRealName", "请求报文中未包含fileRealName");
        Assert.hasKeyAndValue(reqJson, "fileSaveName", "请求报文中未包含fileSaveName");


        ContractFilePo contractFilePo = BeanConvertUtil.covertBean(reqJson, ContractFilePo.class);
        return saveContractFileBMOImpl.save(contractFilePo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /contractFile/updateContractFile
     * @path /app/contractFile/updateContractFile
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateContractFile", method = RequestMethod.POST)
    public ResponseEntity<String> updateContractFile(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "contractId", "请求报文中未包含contractId");
        Assert.hasKeyAndValue(reqJson, "fileRealName", "请求报文中未包含fileRealName");
        Assert.hasKeyAndValue(reqJson, "fileSaveName", "请求报文中未包含fileSaveName");
        Assert.hasKeyAndValue(reqJson, "contractFileId", "contractFileId不能为空");


        ContractFilePo contractFilePo = BeanConvertUtil.covertBean(reqJson, ContractFilePo.class);
        return updateContractFileBMOImpl.update(contractFilePo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /contractFile/deleteContractFile
     * @path /app/contractFile/deleteContractFile
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteContractFile", method = RequestMethod.POST)
    public ResponseEntity<String> deleteContractFile(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "contractFileId", "contractFileId不能为空");

        ContractFilePo contractFilePo = BeanConvertUtil.covertBean(reqJson, ContractFilePo.class);
        return deleteContractFileBMOImpl.delete(contractFilePo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /contractFile/queryContractFile
     * @path /app/contractFile/queryContractFile
     * @param storeId 商户ID
     * @return
     */
    @RequestMapping(value = "/queryContractFile", method = RequestMethod.GET)
    public ResponseEntity<String> queryContractFile(@RequestHeader(value = "store-id") String storeId,
                                                    @RequestParam(value = "contractId", required = false) String contractId,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        ContractFileDto contractFileDto = new ContractFileDto();
        contractFileDto.setPage(page);
        contractFileDto.setRow(row);
        contractFileDto.setContractId(contractId);
        return getContractFileBMOImpl.get(contractFileDto);
    }
}
