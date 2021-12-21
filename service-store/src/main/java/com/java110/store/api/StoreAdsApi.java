package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.storeAds.StoreAdsDto;
import com.java110.po.storeAds.StoreAdsPo;
import com.java110.store.bmo.storeAds.IDeleteStoreAdsBMO;
import com.java110.store.bmo.storeAds.IGetStoreAdsBMO;
import com.java110.store.bmo.storeAds.ISaveStoreAdsBMO;
import com.java110.store.bmo.storeAds.IUpdateStoreAdsBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/storeAds")
public class StoreAdsApi {

    @Autowired
    private ISaveStoreAdsBMO saveStoreAdsBMOImpl;
    @Autowired
    private IUpdateStoreAdsBMO updateStoreAdsBMOImpl;
    @Autowired
    private IDeleteStoreAdsBMO deleteStoreAdsBMOImpl;

    @Autowired
    private IGetStoreAdsBMO getStoreAdsBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeAds/saveStoreAds
     * @path /app/storeAds/saveStoreAds
     */
    @RequestMapping(value = "/saveStoreAds", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreAds(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "adName", "请求报文中未包含adName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "advertType", "请求报文中未包含advertType");
        Assert.hasKeyAndValue(reqJson, "adType", "请求报文中未包含adType");
        Assert.hasKeyAndValue(reqJson, "url", "请求报文中未包含url");


        StoreAdsPo storeAdsPo = BeanConvertUtil.covertBean(reqJson, StoreAdsPo.class);
        return saveStoreAdsBMOImpl.save(storeAdsPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeAds/updateStoreAds
     * @path /app/storeAds/updateStoreAds
     */
    @RequestMapping(value = "/updateStoreAds", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreAds(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "adName", "请求报文中未包含adName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "advertType", "请求报文中未包含advertType");
        Assert.hasKeyAndValue(reqJson, "adType", "请求报文中未包含adType");
        Assert.hasKeyAndValue(reqJson, "url", "请求报文中未包含url");
        Assert.hasKeyAndValue(reqJson, "adsId", "adsId不能为空");

        StoreAdsPo storeAdsPo = BeanConvertUtil.covertBean(reqJson, StoreAdsPo.class);
        return updateStoreAdsBMOImpl.update(storeAdsPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeAds/deleteStoreAds
     * @path /app/storeAds/deleteStoreAds
     */
    @RequestMapping(value = "/deleteStoreAds", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreAds(@RequestBody JSONObject reqJson) {
//        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "adsId", "adsId不能为空");
        StoreAdsPo storeAdsPo = BeanConvertUtil.covertBean(reqJson, StoreAdsPo.class);
        return deleteStoreAdsBMOImpl.delete(storeAdsPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param shareId 小区ID
     * @return
     * @serviceCode /storeAds/queryStoreAds
     * @path /app/storeAds/queryStoreAds
     */
    @RequestMapping(value = "/queryStoreAds", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreAds(@RequestParam(value = "shareId", required = false) String shareId,
                                                @RequestParam(value = "adName", required = false) String adName,
                                                @RequestParam(value = "adType", required = false) String adType,
                                                @RequestParam(value = "advertType", required = false) String advertType,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "row") int row) {
        StoreAdsDto storeAdsDto = new StoreAdsDto();
        storeAdsDto.setPage(page);
        storeAdsDto.setRow(row);
        storeAdsDto.setShareId(shareId);
        storeAdsDto.setAdName(adName);
        storeAdsDto.setAdType(adType);
        storeAdsDto.setAdvertType(advertType);
        return getStoreAdsBMOImpl.get(storeAdsDto);
    }
}
