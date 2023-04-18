package com.java110.community.api;

import com.java110.community.bmo.communitySetting.IDeleteCommunitySettingBMO;
import com.java110.community.bmo.communitySetting.IGetCommunitySettingBMO;
import com.java110.community.bmo.communitySetting.ISaveCommunitySettingBMO;
import com.java110.community.bmo.communitySetting.IUpdateCommunitySettingBMO;
import com.java110.dto.community.CommunitySettingDto;
import com.java110.po.communitySetting.CommunitySettingPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/communitySetting")
public class CommunitySettingApi {

    @Autowired
    private ISaveCommunitySettingBMO saveCommunitySettingBMOImpl;
    @Autowired
    private IUpdateCommunitySettingBMO updateCommunitySettingBMOImpl;
    @Autowired
    private IDeleteCommunitySettingBMO deleteCommunitySettingBMOImpl;

    @Autowired
    private IGetCommunitySettingBMO getCommunitySettingBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /communitySetting/saveCommunitySetting
     * @path /app/communitySetting/saveCommunitySetting
     */
    @RequestMapping(value = "/saveCommunitySetting", method = RequestMethod.POST)
    public ResponseEntity<String> saveCommunitySetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "settingType", "请求报文中未包含settingType");
        Assert.hasKeyAndValue(reqJson, "settingKey", "请求报文中未包含settingKey");


        CommunitySettingPo communitySettingPo = BeanConvertUtil.covertBean(reqJson, CommunitySettingPo.class);
        return saveCommunitySettingBMOImpl.save(communitySettingPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /communitySetting/updateCommunitySetting
     * @path /app/communitySetting/updateCommunitySetting
     */
    @RequestMapping(value = "/updateCommunitySetting", method = RequestMethod.POST)
    public ResponseEntity<String> updateCommunitySetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "settingType", "请求报文中未包含settingType");
        Assert.hasKeyAndValue(reqJson, "settingKey", "请求报文中未包含settingKey");
        Assert.hasKeyAndValue(reqJson, "csId", "csId不能为空");


        CommunitySettingPo communitySettingPo = BeanConvertUtil.covertBean(reqJson, CommunitySettingPo.class);
        return updateCommunitySettingBMOImpl.update(communitySettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /communitySetting/deleteCommunitySetting
     * @path /app/communitySetting/deleteCommunitySetting
     */
    @RequestMapping(value = "/deleteCommunitySetting", method = RequestMethod.POST)
    public ResponseEntity<String> deleteCommunitySetting(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "csId", "csId不能为空");


        CommunitySettingPo communitySettingPo = BeanConvertUtil.covertBean(reqJson, CommunitySettingPo.class);
        return deleteCommunitySettingBMOImpl.delete(communitySettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /communitySetting/queryCommunitySetting
     * @path /app/communitySetting/queryCommunitySetting
     */
    @RequestMapping(value = "/queryCommunitySetting", method = RequestMethod.GET)
    public ResponseEntity<String> queryCommunitySetting(@RequestParam(value = "communityId") String communityId,
                                                        @RequestParam(value = "settingType", required = false) String settingType,
                                                        @RequestParam(value = "settingName", required = false) String settingName,
                                                        @RequestParam(value = "settingKey", required = false) String settingKey,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        CommunitySettingDto communitySettingDto = new CommunitySettingDto();
        communitySettingDto.setSettingKey(settingKey);
        communitySettingDto.setSettingName(settingName);
        communitySettingDto.setSettingType(settingType);
        communitySettingDto.setPage(page);
        communitySettingDto.setRow(row);
        communitySettingDto.setCommunityId(communityId);
        return getCommunitySettingBMOImpl.get(communitySettingDto);
    }
}
