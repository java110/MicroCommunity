package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.dto.groupBuySetting.GroupBuySettingDto;
import com.java110.goods.bmo.groupBuyProduct.IDeleteGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProduct.IGetGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProduct.ISaveGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProduct.IUpdateGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuySetting.IDeleteGroupBuySettingBMO;
import com.java110.goods.bmo.groupBuySetting.IGetGroupBuySettingBMO;
import com.java110.goods.bmo.groupBuySetting.ISaveGroupBuySettingBMO;
import com.java110.goods.bmo.groupBuySetting.IUpdateGroupBuySettingBMO;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/groupBuyProduct")
public class GroupBuyProductApi {

    @Autowired
    private ISaveGroupBuyProductBMO saveGroupBuyProductBMOImpl;
    @Autowired
    private IUpdateGroupBuyProductBMO updateGroupBuyProductBMOImpl;
    @Autowired
    private IDeleteGroupBuyProductBMO deleteGroupBuyProductBMOImpl;

    @Autowired
    private IGetGroupBuyProductBMO getGroupBuyProductBMOImpl;

    @Autowired
    private ISaveGroupBuySettingBMO saveGroupBuySettingBMOImpl;
    @Autowired
    private IUpdateGroupBuySettingBMO updateGroupBuySettingBMOImpl;
    @Autowired
    private IDeleteGroupBuySettingBMO deleteGroupBuySettingBMOImpl;

    @Autowired
    private IGetGroupBuySettingBMO getGroupBuySettingBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuyProduct/saveGroupBuyProduct
     * @path /app/groupBuyProduct/saveGroupBuyProduct
     */
    @RequestMapping(value = "/saveGroupBuyProduct", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuyProduct(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "groupProdName", "请求报文中未包含groupProdName");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "groupProdDesc", "请求报文中未包含groupProdDesc");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");


        GroupBuyProductPo groupBuyProductPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductPo.class);
        groupBuyProductPo.setStoreId(storeId);
        return saveGroupBuyProductBMOImpl.save(groupBuyProductPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuyProduct/updateGroupBuyProduct
     * @path /app/groupBuyProduct/updateGroupBuyProduct
     */
    @RequestMapping(value = "/updateGroupBuyProduct", method = RequestMethod.POST)
    public ResponseEntity<String> updateGroupBuyProduct(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "groupProdName", "请求报文中未包含groupProdName");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "groupProdDesc", "请求报文中未包含groupProdDesc");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");
        Assert.hasKeyAndValue(reqJson, "groupId", "groupId不能为空");


        GroupBuyProductPo groupBuyProductPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductPo.class);
        groupBuyProductPo.setStoreId(storeId);
        return updateGroupBuyProductBMOImpl.update(groupBuyProductPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuyProduct/deleteGroupBuyProduct
     * @path /app/groupBuyProduct/deleteGroupBuyProduct
     */
    @RequestMapping(value = "/deleteGroupBuyProduct", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGroupBuyProduct(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "groupId", "groupId不能为空");


        GroupBuyProductPo groupBuyProductPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductPo.class);
        groupBuyProductPo.setStoreId(storeId);
        return deleteGroupBuyProductBMOImpl.delete(groupBuyProductPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户
     * @return
     * @serviceCode /groupBuyProduct/queryGroupBuyProduct
     * @path /app/groupBuyProduct/queryGroupBuyProduct
     */
    @RequestMapping(value = "/queryGroupBuyProduct", method = RequestMethod.GET)
    public ResponseEntity<String> queryGroupBuyProduct(@RequestHeader(value = "store-id") String storeId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        GroupBuyProductDto groupBuyProductDto = new GroupBuyProductDto();
        groupBuyProductDto.setPage(page);
        groupBuyProductDto.setRow(row);
        groupBuyProductDto.setStoreId(storeId);
        return getGroupBuyProductBMOImpl.get(groupBuyProductDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuySetting/saveGroupBuySetting
     * @path /app/groupBuySetting/saveGroupBuySetting
     */
    @RequestMapping(value = "/saveGroupBuySetting", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuySetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "groupBuyName", "请求报文中未包含groupBuyName");
        Assert.hasKeyAndValue(reqJson, "validHours", "请求报文中未包含validHours");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");


        GroupBuySettingPo groupBuySettingPo = BeanConvertUtil.covertBean(reqJson, GroupBuySettingPo.class);
        return saveGroupBuySettingBMOImpl.save(groupBuySettingPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuySetting/updateGroupBuySetting
     * @path /app/groupBuySetting/updateGroupBuySetting
     */
    @RequestMapping(value = "/updateGroupBuySetting", method = RequestMethod.POST)
    public ResponseEntity<String> updateGroupBuySetting(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "groupBuyName", "请求报文中未包含groupBuyName");
        Assert.hasKeyAndValue(reqJson, "validHours", "请求报文中未包含validHours");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        GroupBuySettingPo groupBuySettingPo = BeanConvertUtil.covertBean(reqJson, GroupBuySettingPo.class);
        return updateGroupBuySettingBMOImpl.update(groupBuySettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuySetting/deleteGroupBuySetting
     * @path /app/groupBuySetting/deleteGroupBuySetting
     */
    @RequestMapping(value = "/deleteGroupBuySetting", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGroupBuySetting(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        GroupBuySettingPo groupBuySettingPo = BeanConvertUtil.covertBean(reqJson, GroupBuySettingPo.class);
        return deleteGroupBuySettingBMOImpl.delete(groupBuySettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /groupBuySetting/queryGroupBuySetting
     * @path /app/groupBuySetting/queryGroupBuySetting
     */
    @RequestMapping(value = "/queryGroupBuySetting", method = RequestMethod.GET)
    public ResponseEntity<String> queryGroupBuySetting(@RequestHeader(value = "store-id") String storeId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        GroupBuySettingDto groupBuySettingDto = new GroupBuySettingDto();
        groupBuySettingDto.setPage(page);
        groupBuySettingDto.setRow(row);
        groupBuySettingDto.setStoreId(storeId);
        return getGroupBuySettingBMOImpl.get(groupBuySettingDto);
    }
}
