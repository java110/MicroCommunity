package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.attrValue.IDeleteAttrValueBMO;
import com.java110.common.bmo.attrValue.IGetAttrValueBMO;
import com.java110.common.bmo.attrValue.ISaveAttrValueBMO;
import com.java110.common.bmo.attrValue.IUpdateAttrValueBMO;
import com.java110.dto.attrSpec.AttrValueDto;
import com.java110.po.attrValue.AttrValuePo;
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
@RequestMapping(value = "/attrValue")
public class AttrValueApi {

    @Autowired
    private ISaveAttrValueBMO saveAttrValueBMOImpl;
    @Autowired
    private IUpdateAttrValueBMO updateAttrValueBMOImpl;
    @Autowired
    private IDeleteAttrValueBMO deleteAttrValueBMOImpl;

    @Autowired
    private IGetAttrValueBMO getAttrValueBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attrValue/saveAttrValue
     * @path /app/attrValue/saveAttrValue
     */
    @RequestMapping(value = "/saveAttrValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveAttrValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");
        Assert.hasKeyAndValue(reqJson, "valueName", "请求报文中未包含valueName");
        Assert.hasKeyAndValue(reqJson, "valueShow", "请求报文中未包含valueShow");


        AttrValuePo attrValuePo = BeanConvertUtil.covertBean(reqJson, AttrValuePo.class);
        return saveAttrValueBMOImpl.save(attrValuePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attrValue/updateAttrValue
     * @path /app/attrValue/updateAttrValue
     */
    @RequestMapping(value = "/updateAttrValue", method = RequestMethod.POST)
    public ResponseEntity<String> updateAttrValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");
        Assert.hasKeyAndValue(reqJson, "valueName", "请求报文中未包含valueName");
        Assert.hasKeyAndValue(reqJson, "valueShow", "请求报文中未包含valueShow");
        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");


        AttrValuePo attrValuePo = BeanConvertUtil.covertBean(reqJson, AttrValuePo.class);
        return updateAttrValueBMOImpl.update(attrValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /attrValue/deleteAttrValue
     * @path /app/attrValue/deleteAttrValue
     */
    @RequestMapping(value = "/deleteAttrValue", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAttrValue(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");


        AttrValuePo attrValuePo = BeanConvertUtil.covertBean(reqJson, AttrValuePo.class);
        return deleteAttrValueBMOImpl.delete(attrValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param specCd 规格ID
     * @return
     * @serviceCode /attrValue/queryAttrValue
     * @path /app/attrValue/queryAttrValue
     * value: '',
     * valueShow: '',
     * valueName: '',
     */
    @RequestMapping(value = "/queryAttrValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryAttrValue(@RequestParam(value = "specCd",required = false) String specCd,
                                                 @RequestParam(value = "specId",required = false) String specId,
                                                 @RequestParam(value = "page", required = false) int page,
                                                 @RequestParam(value = "row", required = false) int row,
                                                 @RequestParam(value = "value", required = false) String value,
                                                 @RequestParam(value = "domain", required = false) String domain,
                                                 @RequestParam(value = "valueShow", required = false) String valueShow,
                                                 @RequestParam(value = "valueName", required = false) String valueName
    ) {
        AttrValueDto attrValueDto = new AttrValueDto();
        attrValueDto.setSpecCd(specCd);
        attrValueDto.setPage(page);
        attrValueDto.setRow(row);
        attrValueDto.setValue(value);
        attrValueDto.setValueName(valueName);
        attrValueDto.setValueShow(valueShow);
        attrValueDto.setDomain(domain);
        attrValueDto.setSpecId(specId);
        return getAttrValueBMOImpl.get(attrValueDto);
    }
}