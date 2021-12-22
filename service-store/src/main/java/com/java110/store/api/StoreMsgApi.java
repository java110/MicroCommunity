package com.java110.store.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.storeMsg.StoreMsgDto;
import com.java110.po.storeMsg.StoreMsgPo;
import com.java110.store.bmo.storeMsg.IDeleteStoreMsgBMO;
import com.java110.store.bmo.storeMsg.IGetStoreMsgBMO;
import com.java110.store.bmo.storeMsg.ISaveStoreMsgBMO;
import com.java110.store.bmo.storeMsg.IUpdateStoreMsgBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/storeMsg")
public class StoreMsgApi {

    @Autowired
    private ISaveStoreMsgBMO saveStoreMsgBMOImpl;
    @Autowired
    private IUpdateStoreMsgBMO updateStoreMsgBMOImpl;
    @Autowired
    private IDeleteStoreMsgBMO deleteStoreMsgBMOImpl;

    @Autowired
    private IGetStoreMsgBMO getStoreMsgBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeMsg/saveStoreMsg
     * @path /app/storeMsg/saveStoreMsg
     */
    @RequestMapping(value = "/saveStoreMsg", method = RequestMethod.POST)
    public ResponseEntity<String> saveStoreMsg(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "shareId", "请求报文中未包含shareId");
        Assert.hasKeyAndValue(reqJson, "msgFlag", "请求报文中未包含msgFlag");
        Assert.hasKeyAndValue(reqJson, "title", "请求报文中未包含title");


        StoreMsgPo storeMsgPo = BeanConvertUtil.covertBean(reqJson, StoreMsgPo.class);
        return saveStoreMsgBMOImpl.save(storeMsgPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeMsg/updateStoreMsg
     * @path /app/storeMsg/updateStoreMsg
     */
    @RequestMapping(value = "/updateStoreMsg", method = RequestMethod.POST)
    public ResponseEntity<String> updateStoreMsg(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "shareId", "请求报文中未包含shareId");
        Assert.hasKeyAndValue(reqJson, "msgFlag", "请求报文中未包含msgFlag");
        Assert.hasKeyAndValue(reqJson, "title", "请求报文中未包含title");
        Assert.hasKeyAndValue(reqJson, "msgId", "msgId不能为空");


        StoreMsgPo storeMsgPo = BeanConvertUtil.covertBean(reqJson, StoreMsgPo.class);
        return updateStoreMsgBMOImpl.update(storeMsgPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /storeMsg/deleteStoreMsg
     * @path /app/storeMsg/deleteStoreMsg
     */
    @RequestMapping(value = "/deleteStoreMsg", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStoreMsg(@RequestBody JSONObject reqJson) {
//        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "msgId", "msgId不能为空");

        StoreMsgPo storeMsgPo = BeanConvertUtil.covertBean(reqJson, StoreMsgPo.class);
        return deleteStoreMsgBMOImpl.delete(storeMsgPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param shareId 分片ID
     * @return
     * @serviceCode /storeMsg/queryStoreMsg
     * @path /app/storeMsg/queryStoreMsg
     */
    @RequestMapping(value = "/queryStoreMsg", method = RequestMethod.GET)
    public ResponseEntity<String> queryStoreMsg(@RequestParam(value = "shareId", required = false) String shareId,
                                                @RequestParam(value = "title", required = false) String title,
                                                @RequestParam(value = "msgFlag", required = false) String msgFlag,
                                                @RequestParam(value = "msgId", required = false) String msgId,
                                                @RequestParam(value = "page") int page,
                                                @RequestParam(value = "row") int row) {
        StoreMsgDto storeMsgDto = new StoreMsgDto();
        storeMsgDto.setPage(page);
        storeMsgDto.setRow(row);
        storeMsgDto.setShareId(shareId);
        storeMsgDto.setTitle(title);
        storeMsgDto.setMsgFlag(msgFlag);
        storeMsgDto.setMsgId(msgId);
        return getStoreMsgBMOImpl.get(storeMsgDto);
    }
}
