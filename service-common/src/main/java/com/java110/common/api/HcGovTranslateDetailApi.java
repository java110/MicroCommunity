package com.java110.common.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.hcGovTranslateDetail.IDeleteHcGovTranslateDetailBMO;
import com.java110.common.bmo.hcGovTranslateDetail.IGetHcGovTranslateDetailBMO;
import com.java110.common.bmo.hcGovTranslateDetail.ISaveHcGovTranslateDetailBMO;
import com.java110.common.bmo.hcGovTranslateDetail.IUpdateHcGovTranslateDetailBMO;
import com.java110.dto.hcGovTranslate.HcGovTranslateDetailDto;
import com.java110.po.hcGovTranslateDetail.HcGovTranslateDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/hcGovTranslateDetail")
public class HcGovTranslateDetailApi {

    @Autowired
    private ISaveHcGovTranslateDetailBMO saveHcGovTranslateDetailBMOImpl;
    @Autowired
    private IUpdateHcGovTranslateDetailBMO updateHcGovTranslateDetailBMOImpl;
    @Autowired
    private IDeleteHcGovTranslateDetailBMO deleteHcGovTranslateDetailBMOImpl;

    @Autowired
    private IGetHcGovTranslateDetailBMO getHcGovTranslateDetailBMOImpl;

    /**
     * 微信保存消息模板
     * @serviceCode /hcGovTranslateDetail/saveHcGovTranslateDetail
     * @path /app/hcGovTranslateDetail/saveHcGovTranslateDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/saveHcGovTranslateDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveHcGovTranslateDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "tranId", "请求报文中未包含tranId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "reqBody", "请求报文中未包含reqBody");
        Assert.hasKeyAndValue(reqJson, "resBody", "请求报文中未包含resBody");


        HcGovTranslateDetailPo hcGovTranslateDetailPo = BeanConvertUtil.covertBean(reqJson, HcGovTranslateDetailPo.class);
        return saveHcGovTranslateDetailBMOImpl.save(hcGovTranslateDetailPo);
    }

    /**
     * 微信修改消息模板
     * @serviceCode /hcGovTranslateDetail/updateHcGovTranslateDetail
     * @path /app/hcGovTranslateDetail/updateHcGovTranslateDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/updateHcGovTranslateDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateHcGovTranslateDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "detailId", "请求报文中未包含detailId");
        Assert.hasKeyAndValue(reqJson, "tranId", "请求报文中未包含tranId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "reqBody", "请求报文中未包含reqBody");
        Assert.hasKeyAndValue(reqJson, "resBody", "请求报文中未包含resBody");
        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        HcGovTranslateDetailPo hcGovTranslateDetailPo = BeanConvertUtil.covertBean(reqJson, HcGovTranslateDetailPo.class);
        return updateHcGovTranslateDetailBMOImpl.update(hcGovTranslateDetailPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /hcGovTranslateDetail/deleteHcGovTranslateDetail
     * @path /app/hcGovTranslateDetail/deleteHcGovTranslateDetail
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/deleteHcGovTranslateDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteHcGovTranslateDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "detailId", "detailId不能为空");


        HcGovTranslateDetailPo hcGovTranslateDetailPo = BeanConvertUtil.covertBean(reqJson, HcGovTranslateDetailPo.class);
        return deleteHcGovTranslateDetailBMOImpl.delete(hcGovTranslateDetailPo);
    }

    /**
     * 微信删除消息模板
     * @serviceCode /hcGovTranslateDetail/queryHcGovTranslateDetail
     * @path /app/hcGovTranslateDetail/queryHcGovTranslateDetail
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/queryHcGovTranslateDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryHcGovTranslateDetail(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        HcGovTranslateDetailDto hcGovTranslateDetailDto = new HcGovTranslateDetailDto();
        hcGovTranslateDetailDto.setPage(page);
        hcGovTranslateDetailDto.setRow(row);
        hcGovTranslateDetailDto.setCommunityId(communityId);
        return getHcGovTranslateDetailBMOImpl.get(hcGovTranslateDetailDto);
    }
}
