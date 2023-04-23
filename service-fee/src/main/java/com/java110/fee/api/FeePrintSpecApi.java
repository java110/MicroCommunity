package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeePrintSpecDto;
import com.java110.fee.bmo.feePrintSpec.IDeleteFeePrintSpecBMO;
import com.java110.fee.bmo.feePrintSpec.IGetFeePrintSpecBMO;
import com.java110.fee.bmo.feePrintSpec.ISaveFeePrintSpecBMO;
import com.java110.fee.bmo.feePrintSpec.IUpdateFeePrintSpecBMO;
import com.java110.po.feePrintSpec.FeePrintSpecPo;
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
@RequestMapping(value = "/feePrintSpec")
public class FeePrintSpecApi {

    @Autowired
    private ISaveFeePrintSpecBMO saveFeePrintSpecBMOImpl;

    @Autowired
    private IUpdateFeePrintSpecBMO updateFeePrintSpecBMOImpl;

    @Autowired
    private IDeleteFeePrintSpecBMO deleteFeePrintSpecBMOImpl;

    @Autowired
    private IGetFeePrintSpecBMO getFeePrintSpecBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feePrintSpec/saveFeePrintSpec
     * @path /app/feePrintSpec/saveFeePrintSpec
     */
    @RequestMapping(value = "/saveFeePrintSpec", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeePrintSpec(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        FeePrintSpecPo feePrintSpecPo = BeanConvertUtil.covertBean(reqJson, FeePrintSpecPo.class);
        return saveFeePrintSpecBMOImpl.save(feePrintSpecPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feePrintSpec/updateFeePrintSpec
     * @path /app/feePrintSpec/updateFeePrintSpec
     */
    @RequestMapping(value = "/updateFeePrintSpec", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeePrintSpec(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "printId", "printId不能为空");
        FeePrintSpecPo feePrintSpecPo = BeanConvertUtil.covertBean(reqJson, FeePrintSpecPo.class);
        return updateFeePrintSpecBMOImpl.update(feePrintSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feePrintSpec/deleteFeePrintSpec
     * @path /app/feePrintSpec/deleteFeePrintSpec
     */
    @RequestMapping(value = "/deleteFeePrintSpec", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeePrintSpec(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "printId", "printId不能为空");
        FeePrintSpecPo feePrintSpecPo = BeanConvertUtil.covertBean(reqJson, FeePrintSpecPo.class);
        return deleteFeePrintSpecBMOImpl.delete(feePrintSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feePrintSpec/queryFeePrintSpec
     * @path /app/feePrintSpec/queryFeePrintSpec
     */
    @RequestMapping(value = "/queryFeePrintSpec", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeePrintSpec(@RequestParam(value = "communityId") String communityId,
                                                    @RequestParam(value = "specCd", required = false) String specCd,
                                                    @RequestParam(value = "page") int page,
                                                    @RequestParam(value = "row") int row) {
        FeePrintSpecDto feePrintSpecDto = new FeePrintSpecDto();
        feePrintSpecDto.setPage(page);
        feePrintSpecDto.setRow(row);
        feePrintSpecDto.setCommunityId(communityId);
        feePrintSpecDto.setSpecCd(specCd);
        return getFeePrintSpecBMOImpl.get(feePrintSpecDto);
    }
}
