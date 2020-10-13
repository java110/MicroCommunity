package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.sysDocument.IDeleteSysDocumentBMO;
import com.java110.common.bmo.sysDocument.IGetSysDocumentBMO;
import com.java110.common.bmo.sysDocument.ISaveSysDocumentBMO;
import com.java110.common.bmo.sysDocument.IUpdateSysDocumentBMO;
import com.java110.dto.sysDocument.SysDocumentDto;
import com.java110.po.sysDocument.SysDocumentPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/sysDocument")
public class SysDocumentApi {

    @Autowired
    private ISaveSysDocumentBMO saveSysDocumentBMOImpl;
    @Autowired
    private IUpdateSysDocumentBMO updateSysDocumentBMOImpl;
    @Autowired
    private IDeleteSysDocumentBMO deleteSysDocumentBMOImpl;

    @Autowired
    private IGetSysDocumentBMO getSysDocumentBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /sysDocument/saveSysDocument
     * @path /app/sysDocument/saveSysDocument
     */
    @RequestMapping(value = "/saveSysDocument", method = RequestMethod.POST)
    public ResponseEntity<String> saveSysDocument(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "docCode", "请求报文中未包含docCode");
        Assert.hasKeyAndValue(reqJson, "docContent", "请求报文中未包含docContent");


        SysDocumentPo sysDocumentPo = BeanConvertUtil.covertBean(reqJson, SysDocumentPo.class);
        return saveSysDocumentBMOImpl.save(sysDocumentPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /sysDocument/updateSysDocument
     * @path /app/sysDocument/updateSysDocument
     */
    @RequestMapping(value = "/updateSysDocument", method = RequestMethod.POST)
    public ResponseEntity<String> updateSysDocument(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "docCode", "请求报文中未包含docCode");
        Assert.hasKeyAndValue(reqJson, "docContent", "请求报文中未包含docContent");
        Assert.hasKeyAndValue(reqJson, "docId", "docId不能为空");


        SysDocumentPo sysDocumentPo = BeanConvertUtil.covertBean(reqJson, SysDocumentPo.class);
        return updateSysDocumentBMOImpl.update(sysDocumentPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /sysDocument/deleteSysDocument
     * @path /app/sysDocument/deleteSysDocument
     */
    @RequestMapping(value = "/deleteSysDocument", method = RequestMethod.POST)
    public ResponseEntity<String> deleteSysDocument(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "docId", "docId不能为空");


        SysDocumentPo sysDocumentPo = BeanConvertUtil.covertBean(reqJson, SysDocumentPo.class);
        return deleteSysDocumentBMOImpl.delete(sysDocumentPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param docCode 文档编码
     * @return
     * @serviceCode /sysDocument/querySysDocument
     * @path /app/sysDocument/querySysDocument
     */
    @RequestMapping(value = "/querySysDocument", method = RequestMethod.GET)
    public ResponseEntity<String> querySysDocument(@RequestParam(value = "docCode",required = false) String docCode,
                                                   @RequestParam(value = "docTitle",required = false) String docTitle,
                                                   @RequestParam(value = "docId",required = false) String docId,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "row") int row) {
        SysDocumentDto sysDocumentDto = new SysDocumentDto();
        sysDocumentDto.setPage(page);
        sysDocumentDto.setRow(row);
        sysDocumentDto.setDocCode(docCode);
        sysDocumentDto.setDocTitle(docTitle);
        sysDocumentDto.setDocId(docId);
        return getSysDocumentBMOImpl.get(sysDocumentDto);
    }
}
