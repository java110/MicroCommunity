package com.java110.community.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.bmo.activitiesType.IDeleteActivitiesTypeBMO;
import com.java110.community.bmo.activitiesType.IGetActivitiesTypeBMO;
import com.java110.community.bmo.activitiesType.ISaveActivitiesTypeBMO;
import com.java110.community.bmo.activitiesType.IUpdateActivitiesTypeBMO;
import com.java110.dto.activities.ActivitiesTypeDto;
import com.java110.po.activitiesType.ActivitiesTypePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/activitiesType")
public class ActivitiesTypeApi {

    @Autowired
    private ISaveActivitiesTypeBMO saveActivitiesTypeBMOImpl;
    @Autowired
    private IUpdateActivitiesTypeBMO updateActivitiesTypeBMOImpl;
    @Autowired
    private IDeleteActivitiesTypeBMO deleteActivitiesTypeBMOImpl;

    @Autowired
    private IGetActivitiesTypeBMO getActivitiesTypeBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesType/saveActivitiesType
     * @path /app/activitiesType/saveActivitiesType
     */
    @RequestMapping(value = "/saveActivitiesType", method = RequestMethod.POST)
    public ResponseEntity<String> saveActivitiesType(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "defaultShow", "请求报文中未包含defaultShow");


        ActivitiesTypePo activitiesTypePo = BeanConvertUtil.covertBean(reqJson, ActivitiesTypePo.class);
        return saveActivitiesTypeBMOImpl.save(activitiesTypePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesType/updateActivitiesType
     * @path /app/activitiesType/updateActivitiesType
     */
    @RequestMapping(value = "/updateActivitiesType", method = RequestMethod.POST)
    public ResponseEntity<String> updateActivitiesType(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "defaultShow", "请求报文中未包含defaultShow");
        Assert.hasKeyAndValue(reqJson, "typeCd", "typeCd不能为空");


        ActivitiesTypePo activitiesTypePo = BeanConvertUtil.covertBean(reqJson, ActivitiesTypePo.class);
        return updateActivitiesTypeBMOImpl.update(activitiesTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesType/deleteActivitiesType
     * @path /app/activitiesType/deleteActivitiesType
     */
    @RequestMapping(value = "/deleteActivitiesType", method = RequestMethod.POST)
    public ResponseEntity<String> deleteActivitiesType(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "typeCd", "typeCd不能为空");


        ActivitiesTypePo activitiesTypePo = BeanConvertUtil.covertBean(reqJson, ActivitiesTypePo.class);
        return deleteActivitiesTypeBMOImpl.delete(activitiesTypePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /activitiesType/queryActivitiesType
     * @path /app/activitiesType/queryActivitiesType
     */
    @RequestMapping(value = "/queryActivitiesType", method = RequestMethod.GET)
    public ResponseEntity<String> queryActivitiesType(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "defaultShow", required=false) String defaultShow,
                                                      @RequestParam(value = "typeCd", required=false) String typeCd,
                                                      @RequestParam(value = "typeName", required=false) String typeName,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ActivitiesTypeDto activitiesTypeDto = new ActivitiesTypeDto();
        activitiesTypeDto.setPage(page);
        activitiesTypeDto.setRow(row);
        activitiesTypeDto.setDefaultShow(defaultShow);
        activitiesTypeDto.setCommunityId(communityId);
        if(!StringUtil.isEmpty(defaultShow)){
            activitiesTypeDto.setDefaultShow(defaultShow);
        }
        if(!StringUtil.isEmpty(typeCd)){
            activitiesTypeDto.setTypeCd(typeCd);
        }
        if(!StringUtil.isEmpty(typeCd)){
            activitiesTypeDto.setTypeCd(typeCd);
        }
        if(!StringUtil.isEmpty(typeName)){
            activitiesTypeDto.setTypeName(typeName);
        }
        return getActivitiesTypeBMOImpl.get(activitiesTypeDto);
    }
}
