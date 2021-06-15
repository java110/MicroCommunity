package com.java110.acct.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.acct.bmo.systemGoldSetting.IDeleteSystemGoldSettingBMO;
import com.java110.acct.bmo.systemGoldSetting.IGetSystemGoldSettingBMO;
import com.java110.acct.bmo.systemGoldSetting.ISaveSystemGoldSettingBMO;
import com.java110.acct.bmo.systemGoldSetting.IUpdateSystemGoldSettingBMO;
import com.java110.dto.systemGoldSetting.SystemGoldSettingDto;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
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
@RequestMapping(value = "/systemGoldSetting")
public class SystemGoldSettingApi {

    @Autowired
    private ISaveSystemGoldSettingBMO saveSystemGoldSettingBMOImpl;
    @Autowired
    private IUpdateSystemGoldSettingBMO updateSystemGoldSettingBMOImpl;
    @Autowired
    private IDeleteSystemGoldSettingBMO deleteSystemGoldSettingBMOImpl;

    @Autowired
    private IGetSystemGoldSettingBMO getSystemGoldSettingBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /systemGoldSetting/saveSystemGoldSetting
     * @path /app/systemGoldSetting/saveSystemGoldSetting
     */
    @RequestMapping(value = "/saveSystemGoldSetting", method = RequestMethod.POST)
    public ResponseEntity<String> saveSystemGoldSetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "buyPrice", "请求报文中未包含buyPrice");
        Assert.hasKeyAndValue(reqJson, "usePrice", "请求报文中未包含usePrice");
        Assert.hasKeyAndValue(reqJson, "validity", "请求报文中未包含validity");


        SystemGoldSettingPo systemGoldSettingPo = BeanConvertUtil.covertBean(reqJson, SystemGoldSettingPo.class);
        return saveSystemGoldSettingBMOImpl.save(systemGoldSettingPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /systemGoldSetting/updateSystemGoldSetting
     * @path /app/systemGoldSetting/updateSystemGoldSetting
     */
    @RequestMapping(value = "/updateSystemGoldSetting", method = RequestMethod.POST)
    public ResponseEntity<String> updateSystemGoldSetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "buyPrice", "请求报文中未包含buyPrice");
        Assert.hasKeyAndValue(reqJson, "usePrice", "请求报文中未包含usePrice");
        Assert.hasKeyAndValue(reqJson, "validity", "请求报文中未包含validity");
        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        SystemGoldSettingPo systemGoldSettingPo = BeanConvertUtil.covertBean(reqJson, SystemGoldSettingPo.class);
        return updateSystemGoldSettingBMOImpl.update(systemGoldSettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /systemGoldSetting/deleteSystemGoldSetting
     * @path /app/systemGoldSetting/deleteSystemGoldSetting
     */
    @RequestMapping(value = "/deleteSystemGoldSetting", method = RequestMethod.POST)
    public ResponseEntity<String> deleteSystemGoldSetting(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        SystemGoldSettingPo systemGoldSettingPo = BeanConvertUtil.covertBean(reqJson, SystemGoldSettingPo.class);
        return deleteSystemGoldSettingBMOImpl.delete(systemGoldSettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @return
     * @serviceCode /systemGoldSetting/querySystemGoldSetting
     * @path /app/systemGoldSetting/querySystemGoldSetting
     */
    @RequestMapping(value = "/querySystemGoldSetting", method = RequestMethod.GET)
    public ResponseEntity<String> querySystemGoldSetting(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        SystemGoldSettingDto systemGoldSettingDto = new SystemGoldSettingDto();
        systemGoldSettingDto.setPage(page);
        systemGoldSettingDto.setRow(row);
        return getSystemGoldSettingBMOImpl.get(systemGoldSettingDto);
    }
}
