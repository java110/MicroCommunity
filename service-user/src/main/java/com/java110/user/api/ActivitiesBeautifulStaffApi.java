package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.activitiesBeautifulStaff.ActivitiesBeautifulStaffDto;
import com.java110.po.activitiesBeautifulStaff.ActivitiesBeautifulStaffPo;
import com.java110.user.bmo.activitiesBeautifulStaff.IDeleteActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesBeautifulStaff.IGetActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesBeautifulStaff.ISaveActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesBeautifulStaff.IUpdateActivitiesBeautifulStaffBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/activitiesBeautifulStaff")
public class ActivitiesBeautifulStaffApi {

    @Autowired
    private ISaveActivitiesBeautifulStaffBMO saveActivitiesBeautifulStaffBMOImpl;
    @Autowired
    private IUpdateActivitiesBeautifulStaffBMO updateActivitiesBeautifulStaffBMOImpl;
    @Autowired
    private IDeleteActivitiesBeautifulStaffBMO deleteActivitiesBeautifulStaffBMOImpl;

    @Autowired
    private IGetActivitiesBeautifulStaffBMO getActivitiesBeautifulStaffBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesBeautifulStaff/saveActivitiesBeautifulStaff
     * @path /app/activitiesBeautifulStaff/saveActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/saveActivitiesBeautifulStaff", method = RequestMethod.POST)
    public ResponseEntity<String> saveActivitiesBeautifulStaff(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "activitiesNum", "请求报文中未包含activitiesNum");
        Assert.hasKeyAndValue(reqJson, "workContent", "请求报文中未包含workContent");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");


        ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo = BeanConvertUtil.covertBean(reqJson, ActivitiesBeautifulStaffPo.class);
        return saveActivitiesBeautifulStaffBMOImpl.save(activitiesBeautifulStaffPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesBeautifulStaff/updateActivitiesBeautifulStaff
     * @path /app/activitiesBeautifulStaff/updateActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/updateActivitiesBeautifulStaff", method = RequestMethod.POST)
    public ResponseEntity<String> updateActivitiesBeautifulStaff(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "activitiesNum", "请求报文中未包含activitiesNum");
        Assert.hasKeyAndValue(reqJson, "workContent", "请求报文中未包含workContent");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "beId", "beId不能为空");


        ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo = BeanConvertUtil.covertBean(reqJson, ActivitiesBeautifulStaffPo.class);
        return updateActivitiesBeautifulStaffBMOImpl.update(activitiesBeautifulStaffPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesBeautifulStaff/deleteActivitiesBeautifulStaff
     * @path /app/activitiesBeautifulStaff/deleteActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/deleteActivitiesBeautifulStaff", method = RequestMethod.POST)
    public ResponseEntity<String> deleteActivitiesBeautifulStaff(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "beId", "beId不能为空");


        ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo = BeanConvertUtil.covertBean(reqJson, ActivitiesBeautifulStaffPo.class);
        return deleteActivitiesBeautifulStaffBMOImpl.delete(activitiesBeautifulStaffPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /activitiesBeautifulStaff/queryActivitiesBeautifulStaff
     * @path /app/activitiesBeautifulStaff/queryActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/queryActivitiesBeautifulStaff", method = RequestMethod.GET)
    public ResponseEntity<String> queryActivitiesBeautifulStaff(@RequestHeader(value = "store-id", required = false) String storeId,
                                                                @RequestParam(value = "page") int page,
                                                                @RequestParam(value = "row") int row) {
        ActivitiesBeautifulStaffDto activitiesBeautifulStaffDto = new ActivitiesBeautifulStaffDto();
        activitiesBeautifulStaffDto.setPage(page);
        activitiesBeautifulStaffDto.setRow(row);
        activitiesBeautifulStaffDto.setStoreId(storeId);
        return getActivitiesBeautifulStaffBMOImpl.get(activitiesBeautifulStaffDto);
    }
}
