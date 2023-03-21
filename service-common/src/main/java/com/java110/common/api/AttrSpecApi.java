package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.attrSpec.IDeleteAttrSpecBMO;
import com.java110.common.bmo.attrSpec.IGetAttrSpecBMO;
import com.java110.common.bmo.attrSpec.ISaveAttrSpecBMO;
import com.java110.common.bmo.attrSpec.IUpdateAttrSpecBMO;
import com.java110.dto.attrSpec.AttrSpecDto;
import com.java110.po.attrSpec.AttrSpecPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/attrSpec")
public class AttrSpecApi {

    @Autowired
    private ISaveAttrSpecBMO saveAttrSpecBMOImpl;
    @Autowired
    private IUpdateAttrSpecBMO updateAttrSpecBMOImpl;
    @Autowired
    private IDeleteAttrSpecBMO deleteAttrSpecBMOImpl;

    @Autowired
    private IGetAttrSpecBMO getAttrSpecBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attrSpec/saveAttrSpec
     * @path /app/attrSpec/saveAttrSpec
     */
    @RequestMapping(value = "/saveAttrSpec", method = RequestMethod.POST)
    public ResponseEntity<String> saveAttrSpec(@RequestBody JSONObject reqJson) {

        //Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "tableName", "请求报文中未包含tableName");
        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");
        Assert.hasKeyAndValue(reqJson, "specHoldplace", "请求报文中未包含specHoldplace");
        Assert.hasKeyAndValue(reqJson, "required", "请求报文中未包含required");
        Assert.hasKeyAndValue(reqJson, "specShow", "请求报文中未包含specShow");
        Assert.hasKeyAndValue(reqJson, "specValueType", "请求报文中未包含specValueType");
        Assert.hasKeyAndValue(reqJson, "specType", "请求报文中未包含specType");
        Assert.hasKeyAndValue(reqJson, "listShow", "请求报文中未包含listShow");


        AttrSpecPo attrSpecPo = BeanConvertUtil.covertBean(reqJson, AttrSpecPo.class);
        return saveAttrSpecBMOImpl.save(attrSpecPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attrSpec/updateAttrSpec
     * @path /app/attrSpec/updateAttrSpec
     */
    @RequestMapping(value = "/updateAttrSpec", method = RequestMethod.POST)
    public ResponseEntity<String> updateAttrSpec(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "tableName", "请求报文中未包含tableName");
        Assert.hasKeyAndValue(reqJson, "specName", "请求报文中未包含specName");
        Assert.hasKeyAndValue(reqJson, "specHoldplace", "请求报文中未包含specHoldplace");
        Assert.hasKeyAndValue(reqJson, "required", "请求报文中未包含required");
        Assert.hasKeyAndValue(reqJson, "specShow", "请求报文中未包含specShow");
        Assert.hasKeyAndValue(reqJson, "specValueType", "请求报文中未包含specValueType");
        Assert.hasKeyAndValue(reqJson, "specType", "请求报文中未包含specType");
        Assert.hasKeyAndValue(reqJson, "listShow", "请求报文中未包含listShow");
        Assert.hasKeyAndValue(reqJson, "specCd", "specCd不能为空");


        AttrSpecPo attrSpecPo = BeanConvertUtil.covertBean(reqJson, AttrSpecPo.class);
        return updateAttrSpecBMOImpl.update(attrSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attrSpec/deleteAttrSpec
     * @path /app/attrSpec/deleteAttrSpec
     */
    @RequestMapping(value = "/deleteAttrSpec", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAttrSpec(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "specId", "specId不能为空");


        AttrSpecPo attrSpecPo = BeanConvertUtil.covertBean(reqJson, AttrSpecPo.class);
        return deleteAttrSpecBMOImpl.delete(attrSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param tableName 小区表名
     * @return
     * @serviceCode /attrSpec/queryAttrSpec
     * @path /app/attrSpec/queryAttrSpec
     */
    @RequestMapping(value = "/queryAttrSpec", method = RequestMethod.GET)
    public ResponseEntity<String> queryAttrSpec(@RequestParam(value = "tableName", required = false) String tableName,
                                                @RequestParam(value = "specCd", required = false) String specCd,
                                                @RequestParam(value = "specId", required = false) String specId,
                                                @RequestParam(value = "specName", required = false) String specName,
                                                @RequestParam(value = "domain", required = false) String domain,
                                                @RequestParam(value = "page", required = false) int page,
                                                @RequestParam(value = "row", required = false) int row) {
        AttrSpecDto attrSpecDto = new AttrSpecDto();
        attrSpecDto.setTableName(tableName);
        attrSpecDto.setPage(page);
        attrSpecDto.setRow(row);
        attrSpecDto.setSpecCd(specCd);
        attrSpecDto.setSpecName(specName);
        attrSpecDto.setDomain(domain);
        attrSpecDto.setSpecId(specId);
        return getAttrSpecBMOImpl.get(attrSpecDto);
    }
}
