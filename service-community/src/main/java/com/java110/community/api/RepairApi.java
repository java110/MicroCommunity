package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.repair.IAppraiseRepairBMO;
import com.java110.community.bmo.repairReturnVisit.IDeleteRepairReturnVisitBMO;
import com.java110.community.bmo.repairReturnVisit.IGetRepairReturnVisitBMO;
import com.java110.community.bmo.repairReturnVisit.ISaveRepairReturnVisitBMO;
import com.java110.community.bmo.repairReturnVisit.IUpdateRepairReturnVisitBMO;
import com.java110.dto.appraise.AppraiseDto;
import com.java110.dto.repairReturnVisit.RepairReturnVisitDto;
import com.java110.po.repairReturnVisit.RepairReturnVisitPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 报修 控制类
 */
@RestController
@RequestMapping("/repair")
public class RepairApi {
    @Autowired
    private IAppraiseRepairBMO appraiseRepairBMOImpl;

    @Autowired
    private ISaveRepairReturnVisitBMO saveRepairReturnVisitBMOImpl;
    @Autowired
    private IUpdateRepairReturnVisitBMO updateRepairReturnVisitBMOImpl;
    @Autowired
    private IDeleteRepairReturnVisitBMO deleteRepairReturnVisitBMOImpl;

    @Autowired
    private IGetRepairReturnVisitBMO getRepairReturnVisitBMOImpl;


    /**
     * 报修评价
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/appraiseRepair", method = RequestMethod.POST)
    public ResponseEntity<String> appraiseRepair(@RequestBody JSONObject reqJson) {
        AppraiseDto appraiseDto = BeanConvertUtil.covertBean(reqJson, AppraiseDto.class);
        return appraiseRepairBMOImpl.appraiseRepair(appraiseDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /repair/saveRepairReturnVisit
     * @path /app/repair/saveRepairReturnVisit
     */
    @RequestMapping(value = "/saveRepairReturnVisit", method = RequestMethod.POST)
    public ResponseEntity<String> saveRepairReturnVisit(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "repairId", "请求报文中未包含repairId");
        Assert.hasKeyAndValue(reqJson, "visitPersonId", "请求报文中未包含visitPersonId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");


        RepairReturnVisitPo repairReturnVisitPo = BeanConvertUtil.covertBean(reqJson, RepairReturnVisitPo.class);
        return saveRepairReturnVisitBMOImpl.save(repairReturnVisitPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /repair/updateRepairReturnVisit
     * @path /app/repair/updateRepairReturnVisit
     */
    @RequestMapping(value = "/updateRepairReturnVisit", method = RequestMethod.POST)
    public ResponseEntity<String> updateRepairReturnVisit(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "repairId", "请求报文中未包含repairId");
        Assert.hasKeyAndValue(reqJson, "visitPersonId", "请求报文中未包含visitPersonId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");
        Assert.hasKeyAndValue(reqJson, "visitId", "visitId不能为空");


        RepairReturnVisitPo repairReturnVisitPo = BeanConvertUtil.covertBean(reqJson, RepairReturnVisitPo.class);
        return updateRepairReturnVisitBMOImpl.update(repairReturnVisitPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /repair/deleteRepairReturnVisit
     * @path /app/repair/deleteRepairReturnVisit
     */
    @RequestMapping(value = "/deleteRepairReturnVisit", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRepairReturnVisit(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "visitId", "visitId不能为空");


        RepairReturnVisitPo repairReturnVisitPo = BeanConvertUtil.covertBean(reqJson, RepairReturnVisitPo.class);
        return deleteRepairReturnVisitBMOImpl.delete(repairReturnVisitPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /repair/queryRepairReturnVisit
     * @path /app/repair/queryRepairReturnVisit
     */
    @RequestMapping(value = "/queryRepairReturnVisit", method = RequestMethod.GET)
    public ResponseEntity<String> queryRepairReturnVisit(@RequestParam(value = "communityId") String communityId,
                                                         @RequestParam(value = "repairId") String repairId,
                                                         @RequestParam(value = "repairName") String repairName,
                                                         @RequestParam(value = "state") String state,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "row") int row) {
        RepairReturnVisitDto repairReturnVisitDto = new RepairReturnVisitDto();
        repairReturnVisitDto.setPage(page);
        repairReturnVisitDto.setRow(row);
        repairReturnVisitDto.setCommunityId(communityId);
        repairReturnVisitDto.setRepairId(repairId);
        repairReturnVisitDto.setState(state);

        return getRepairReturnVisitBMOImpl.get(repairReturnVisitDto);
    }
}
