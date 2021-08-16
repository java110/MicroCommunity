package com.java110.oa.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.oaWorkflow.OaWorkflowDto;
import com.java110.oa.bmo.oaWorkflow.IDeleteOaWorkflowBMO;
import com.java110.oa.bmo.oaWorkflow.IGetOaWorkflowBMO;
import com.java110.oa.bmo.oaWorkflow.ISaveOaWorkflowBMO;
import com.java110.oa.bmo.oaWorkflow.IUpdateOaWorkflowBMO;
import com.java110.po.oaWorkflow.OaWorkflowPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/oaWorkflow")
public class OaWorkflowApi {

    @Autowired
    private ISaveOaWorkflowBMO saveOaWorkflowBMOImpl;
    @Autowired
    private IUpdateOaWorkflowBMO updateOaWorkflowBMOImpl;
    @Autowired
    private IDeleteOaWorkflowBMO deleteOaWorkflowBMOImpl;

    @Autowired
    private IGetOaWorkflowBMO getOaWorkflowBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /oaWorkflow/saveOaWorkflow
     * @path /app/oaWorkflow/saveOaWorkflow
     */
    @RequestMapping(value = "/saveOaWorkflow", method = RequestMethod.POST)
    public ResponseEntity<String> saveOaWorkflow(@RequestHeader(value = "store-id") String storeId,
                                                 @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "flowName", "请求报文中未包含flowName");
        Assert.hasKeyAndValue(reqJson, "flowType", "请求报文中未包含flowType");


        OaWorkflowPo oaWorkflowPo = BeanConvertUtil.covertBean(reqJson, OaWorkflowPo.class);
        oaWorkflowPo.setStoreId(storeId);
        return saveOaWorkflowBMOImpl.save(oaWorkflowPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /oaWorkflow/updateOaWorkflow
     * @path /app/oaWorkflow/updateOaWorkflow
     */
    @RequestMapping(value = "/updateOaWorkflow", method = RequestMethod.POST)
    public ResponseEntity<String> updateOaWorkflow(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "flowName", "请求报文中未包含flowName");
        Assert.hasKeyAndValue(reqJson, "flowType", "请求报文中未包含flowType");
        Assert.hasKeyAndValue(reqJson, "flowId", "flowId不能为空");


        OaWorkflowPo oaWorkflowPo = BeanConvertUtil.covertBean(reqJson, OaWorkflowPo.class);
        return updateOaWorkflowBMOImpl.update(oaWorkflowPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /oaWorkflow/deleteOaWorkflow
     * @path /app/oaWorkflow/deleteOaWorkflow
     */
    @RequestMapping(value = "/deleteOaWorkflow", method = RequestMethod.POST)
    public ResponseEntity<String> deleteOaWorkflow(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "flowId", "flowId不能为空");


        OaWorkflowPo oaWorkflowPo = BeanConvertUtil.covertBean(reqJson, OaWorkflowPo.class);
        return deleteOaWorkflowBMOImpl.delete(oaWorkflowPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /oaWorkflow/queryOaWorkflow
     * @path /app/oaWorkflow/queryOaWorkflow
     */
    @RequestMapping(value = "/queryOaWorkflow", method = RequestMethod.GET)
    public ResponseEntity<String> queryOaWorkflow(@RequestHeader(value = "store-id") String storeId,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "row") int row) {
        OaWorkflowDto oaWorkflowDto = new OaWorkflowDto();
        oaWorkflowDto.setPage(page);
        oaWorkflowDto.setRow(row);
        oaWorkflowDto.setStoreId(storeId);
        return getOaWorkflowBMOImpl.get(oaWorkflowDto);
    }
}
