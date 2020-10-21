package com.java110.goods.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.groupBuy.GroupBuyDto;
import com.java110.dto.groupBuyBatch.GroupBuyBatchDto;
import com.java110.dto.groupBuyProduct.GroupBuyProductDto;
import com.java110.dto.groupBuyProductSpec.GroupBuyProductSpecDto;
import com.java110.dto.groupBuySetting.GroupBuySettingDto;
import com.java110.goods.bmo.groupBuy.IDeleteGroupBuyBMO;
import com.java110.goods.bmo.groupBuy.IGetGroupBuyBMO;
import com.java110.goods.bmo.groupBuy.ISaveGroupBuyBMO;
import com.java110.goods.bmo.groupBuy.IUpdateGroupBuyBMO;
import com.java110.goods.bmo.groupBuyBatch.IDeleteGroupBuyBatchBMO;
import com.java110.goods.bmo.groupBuyBatch.IGetGroupBuyBatchBMO;
import com.java110.goods.bmo.groupBuyBatch.ISaveGroupBuyBatchBMO;
import com.java110.goods.bmo.groupBuyBatch.IUpdateGroupBuyBatchBMO;
import com.java110.goods.bmo.groupBuyProduct.IDeleteGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProduct.IGetGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProduct.ISaveGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProduct.IUpdateGroupBuyProductBMO;
import com.java110.goods.bmo.groupBuyProductSpec.IDeleteGroupBuyProductSpecBMO;
import com.java110.goods.bmo.groupBuyProductSpec.IGetGroupBuyProductSpecBMO;
import com.java110.goods.bmo.groupBuyProductSpec.ISaveGroupBuyProductSpecBMO;
import com.java110.goods.bmo.groupBuyProductSpec.IUpdateGroupBuyProductSpecBMO;
import com.java110.goods.bmo.groupBuySetting.IDeleteGroupBuySettingBMO;
import com.java110.goods.bmo.groupBuySetting.IGetGroupBuySettingBMO;
import com.java110.goods.bmo.groupBuySetting.ISaveGroupBuySettingBMO;
import com.java110.goods.bmo.groupBuySetting.IUpdateGroupBuySettingBMO;
import com.java110.po.groupBuy.GroupBuyPo;
import com.java110.po.groupBuyBatch.GroupBuyBatchPo;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import com.java110.po.groupBuyProductSpec.GroupBuyProductSpecPo;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/groupBuy")
public class GroupBuyApi {

    @Autowired
    private ISaveGroupBuyBMO saveGroupBuyBMOImpl;
    @Autowired
    private IUpdateGroupBuyBMO updateGroupBuyBMOImpl;
    @Autowired
    private IDeleteGroupBuyBMO deleteGroupBuyBMOImpl;

    @Autowired
    private IGetGroupBuyBMO getGroupBuyBMOImpl;


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


    @Autowired
    private ISaveGroupBuyProductSpecBMO saveGroupBuyProductSpecBMOImpl;
    @Autowired
    private IUpdateGroupBuyProductSpecBMO updateGroupBuyProductSpecBMOImpl;
    @Autowired
    private IDeleteGroupBuyProductSpecBMO deleteGroupBuyProductSpecBMOImpl;

    @Autowired
    private IGetGroupBuyProductSpecBMO getGroupBuyProductSpecBMOImpl;


    @Autowired
    private ISaveGroupBuyBatchBMO saveGroupBuyBatchBMOImpl;
    @Autowired
    private IUpdateGroupBuyBatchBMO updateGroupBuyBatchBMOImpl;
    @Autowired
    private IDeleteGroupBuyBatchBMO deleteGroupBuyBatchBMOImpl;

    @Autowired
    private IGetGroupBuyBatchBMO getGroupBuyBatchBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/saveGroupBuy
     * @path /app/groupBuy/saveGroupBuy
     */
    @RequestMapping(value = "/saveGroupBuy", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuy(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "batchId", "请求报文中未包含batchId");
        Assert.hasKeyAndValue(reqJson, "buyCount", "请求报文中未包含buyCount");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "persionId", "请求报文中未包含persionId");


        GroupBuyPo groupBuyPo = BeanConvertUtil.covertBean(reqJson, GroupBuyPo.class);
        return saveGroupBuyBMOImpl.save(groupBuyPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/updateGroupBuy
     * @path /app/groupBuy/updateGroupBuy
     */
    @RequestMapping(value = "/updateGroupBuy", method = RequestMethod.POST)
    public ResponseEntity<String> updateGroupBuy(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "batchId", "请求报文中未包含batchId");
        Assert.hasKeyAndValue(reqJson, "buyCount", "请求报文中未包含buyCount");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "specId", "请求报文中未包含specId");
        Assert.hasKeyAndValue(reqJson, "persionId", "请求报文中未包含persionId");
        Assert.hasKeyAndValue(reqJson, "buyId", "buyId不能为空");


        GroupBuyPo groupBuyPo = BeanConvertUtil.covertBean(reqJson, GroupBuyPo.class);
        return updateGroupBuyBMOImpl.update(groupBuyPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/deleteGroupBuy
     * @path /app/groupBuy/deleteGroupBuy
     */
    @RequestMapping(value = "/deleteGroupBuy", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGroupBuy(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "buyId", "buyId不能为空");


        GroupBuyPo groupBuyPo = BeanConvertUtil.covertBean(reqJson, GroupBuyPo.class);
        return deleteGroupBuyBMOImpl.delete(groupBuyPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /groupBuy/queryGroupBuy
     * @path /app/groupBuy/queryGroupBuy
     */
    @RequestMapping(value = "/queryGroupBuy", method = RequestMethod.GET)
    public ResponseEntity<String> queryGroupBuy(@RequestHeader(value = "store-id") String storeId,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "row") int row) {
        GroupBuyDto groupBuyDto = new GroupBuyDto();
        groupBuyDto.setPage(page);
        groupBuyDto.setRow(row);
        groupBuyDto.setStoreId(storeId);
        return getGroupBuyBMOImpl.get(groupBuyDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/saveGroupBuyProduct
     * @path /app/groupBuy/saveGroupBuyProduct
     */
    @RequestMapping(value = "/saveGroupBuyProduct", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuyProduct(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "groupProdName", "请求报文中未包含groupProdName");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "groupProdDesc", "请求报文中未包含groupProdDesc");
        Assert.hasKeyAndValue(reqJson, "sort", "请求报文中未包含sort");

        Assert.hasKey(reqJson, "productSpecs", "请求报文中未包含商品规格信息");

        JSONArray productSpecs = reqJson.getJSONArray("productSpecs");

        if (productSpecs.size() < 1) {
            throw new IllegalArgumentException("请求报文中商品规格数量为0");
        }
        List<GroupBuyProductSpecPo> groupBuyProductSpecPos = new ArrayList<>();
        GroupBuyProductSpecPo groupBuyProductSpecPo = null;
        for (int specIndex = 0; specIndex < productSpecs.size(); specIndex++) {
            groupBuyProductSpecPo = BeanConvertUtil.covertBean(productSpecs.getJSONObject(specIndex), GroupBuyProductSpecPo.class);
            groupBuyProductSpecPo.setProductId(reqJson.getString("productId"));
            groupBuyProductSpecPo.setDefaultShow("F");
            groupBuyProductSpecPo.setGroupSales("1");
            groupBuyProductSpecPo.setStoreId(storeId);
            groupBuyProductSpecPos.add(groupBuyProductSpecPo);
        }
        GroupBuyProductPo groupBuyProductPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductPo.class);
        groupBuyProductPo.setStoreId(storeId);
        return saveGroupBuyProductBMOImpl.save(groupBuyProductPo,groupBuyProductSpecPos);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/updateGroupBuyProduct
     * @path /app/groupBuy/updateGroupBuyProduct
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
     * @serviceCode /groupBuy/deleteGroupBuyProduct
     * @path /app/groupBuy/deleteGroupBuyProduct
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
     * @serviceCode /groupBuy/queryGroupBuyProduct
     * @path /app/groupBuy/queryGroupBuyProduct
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
     * @serviceCode /groupBuy/saveGroupBuySetting
     * @path /app/groupBuy/saveGroupBuySetting
     */
    @RequestMapping(value = "/saveGroupBuySetting", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuySetting(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "groupBuyName", "请求报文中未包含groupBuyName");
        Assert.hasKeyAndValue(reqJson, "validHours", "请求报文中未包含validHours");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");


        GroupBuySettingPo groupBuySettingPo = BeanConvertUtil.covertBean(reqJson, GroupBuySettingPo.class);
        groupBuySettingPo.setStoreId(storeId);
        return saveGroupBuySettingBMOImpl.save(groupBuySettingPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/updateGroupBuySetting
     * @path /app/groupBuy/updateGroupBuySetting
     */
    @RequestMapping(value = "/updateGroupBuySetting", method = RequestMethod.POST)
    public ResponseEntity<String> updateGroupBuySetting(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "groupBuyName", "请求报文中未包含groupBuyName");
        Assert.hasKeyAndValue(reqJson, "validHours", "请求报文中未包含validHours");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        GroupBuySettingPo groupBuySettingPo = BeanConvertUtil.covertBean(reqJson, GroupBuySettingPo.class);
        groupBuySettingPo.setStoreId(storeId);
        return updateGroupBuySettingBMOImpl.update(groupBuySettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/deleteGroupBuySetting
     * @path /app/groupBuy/deleteGroupBuySetting
     */
    @RequestMapping(value = "/deleteGroupBuySetting", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGroupBuySetting(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "settingId", "settingId不能为空");


        GroupBuySettingPo groupBuySettingPo = BeanConvertUtil.covertBean(reqJson, GroupBuySettingPo.class);
        groupBuySettingPo.setStoreId(storeId);
        return deleteGroupBuySettingBMOImpl.delete(groupBuySettingPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /groupBuy/queryGroupBuySetting
     * @path /app/groupBuy/queryGroupBuySetting
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

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/saveGroupBuyProductSpec
     * @path /app/groupBuy/saveGroupBuyProductSpec
     */
    @RequestMapping(value = "/saveGroupBuyProductSpec", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuyProductSpec(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "groupStock", "请求报文中未包含groupStock");
        Assert.hasKeyAndValue(reqJson, "groupSales", "请求报文中未包含groupSales");
        Assert.hasKeyAndValue(reqJson, "defaultShow", "请求报文中未包含defaultShow");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");


        GroupBuyProductSpecPo groupBuyProductSpecPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductSpecPo.class);
        return saveGroupBuyProductSpecBMOImpl.save(groupBuyProductSpecPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/updateGroupBuyProductSpec
     * @path /app/groupBuy/updateGroupBuyProductSpec
     */
    @RequestMapping(value = "/updateGroupBuyProductSpec", method = RequestMethod.POST)
    public ResponseEntity<String> updateGroupBuyProductSpec(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "groupStock", "请求报文中未包含groupStock");
        Assert.hasKeyAndValue(reqJson, "groupSales", "请求报文中未包含groupSales");
        Assert.hasKeyAndValue(reqJson, "defaultShow", "请求报文中未包含defaultShow");
        Assert.hasKeyAndValue(reqJson, "productId", "请求报文中未包含productId");
        Assert.hasKeyAndValue(reqJson, "specId", "specId不能为空");


        GroupBuyProductSpecPo groupBuyProductSpecPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductSpecPo.class);
        return updateGroupBuyProductSpecBMOImpl.update(groupBuyProductSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/deleteGroupBuyProductSpec
     * @path /app/groupBuy/deleteGroupBuyProductSpec
     */
    @RequestMapping(value = "/deleteGroupBuyProductSpec", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGroupBuyProductSpec(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "specId", "specId不能为空");


        GroupBuyProductSpecPo groupBuyProductSpecPo = BeanConvertUtil.covertBean(reqJson, GroupBuyProductSpecPo.class);
        return deleteGroupBuyProductSpecBMOImpl.delete(groupBuyProductSpecPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /groupBuy/queryGroupBuyProductSpec
     * @path /app/groupBuy/queryGroupBuyProductSpec
     */
    @RequestMapping(value = "/queryGroupBuyProductSpec", method = RequestMethod.GET)
    public ResponseEntity<String> queryGroupBuyProductSpec(@RequestHeader(value = "store-id") String storeId,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        GroupBuyProductSpecDto groupBuyProductSpecDto = new GroupBuyProductSpecDto();
        groupBuyProductSpecDto.setPage(page);
        groupBuyProductSpecDto.setRow(row);
        groupBuyProductSpecDto.setStoreId(storeId);
        return getGroupBuyProductSpecBMOImpl.get(groupBuyProductSpecDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/saveGroupBuyBatch
     * @path /app/groupBuy/saveGroupBuyBatch
     */
    @RequestMapping(value = "/saveGroupBuyBatch", method = RequestMethod.POST)
    public ResponseEntity<String> saveGroupBuyBatch(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "batchStartTime", "请求报文中未包含batchStartTime");
        Assert.hasKeyAndValue(reqJson, "batchEndTime", "请求报文中未包含batchEndTime");


        GroupBuyBatchPo groupBuyBatchPo = BeanConvertUtil.covertBean(reqJson, GroupBuyBatchPo.class);
        groupBuyBatchPo.setStoreId(storeId);
        return saveGroupBuyBatchBMOImpl.save(groupBuyBatchPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/updateGroupBuyBatch
     * @path /app/groupBuy/updateGroupBuyBatch
     */
    @RequestMapping(value = "/updateGroupBuyBatch", method = RequestMethod.POST)
    public ResponseEntity<String> updateGroupBuyBatch(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {

        Assert.hasKeyAndValue(reqJson, "settingId", "请求报文中未包含settingId");
        Assert.hasKeyAndValue(reqJson, "batchStartTime", "请求报文中未包含batchStartTime");
        Assert.hasKeyAndValue(reqJson, "batchEndTime", "请求报文中未包含batchEndTime");
        Assert.hasKeyAndValue(reqJson, "batchId", "batchId不能为空");


        GroupBuyBatchPo groupBuyBatchPo = BeanConvertUtil.covertBean(reqJson, GroupBuyBatchPo.class);
        groupBuyBatchPo.setStoreId(storeId);
        return updateGroupBuyBatchBMOImpl.update(groupBuyBatchPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /groupBuy/deleteGroupBuyBatch
     * @path /app/groupBuy/deleteGroupBuyBatch
     */
    @RequestMapping(value = "/deleteGroupBuyBatch", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGroupBuyBatch(@RequestBody JSONObject reqJson, @RequestHeader(value = "store-id") String storeId) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "batchId", "batchId不能为空");


        GroupBuyBatchPo groupBuyBatchPo = BeanConvertUtil.covertBean(reqJson, GroupBuyBatchPo.class);
        groupBuyBatchPo.setStoreId(storeId);
        return deleteGroupBuyBatchBMOImpl.delete(groupBuyBatchPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /groupBuy/queryGroupBuyBatch
     * @path /app/groupBuy/queryGroupBuyBatch
     */
    @RequestMapping(value = "/queryGroupBuyBatch", method = RequestMethod.GET)
    public ResponseEntity<String> queryGroupBuyBatch(@RequestHeader(value = "store-id") String storeId,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        GroupBuyBatchDto groupBuyBatchDto = new GroupBuyBatchDto();
        groupBuyBatchDto.setPage(page);
        groupBuyBatchDto.setRow(row);
        groupBuyBatchDto.setStoreId(storeId);
        return getGroupBuyBatchBMOImpl.get(groupBuyBatchDto);
    }
}
