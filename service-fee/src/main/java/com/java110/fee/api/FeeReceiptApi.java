package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.fee.FeeReceiptDto;
import com.java110.dto.fee.FeeReceiptDtoNew;
import com.java110.dto.fee.FeeReceiptDetailDto;
import com.java110.dto.store.StoreUserDto;
import com.java110.fee.bmo.feeReceipt.IDeleteFeeReceiptBMO;
import com.java110.fee.bmo.feeReceipt.IGetFeeReceiptBMO;
import com.java110.fee.bmo.feeReceipt.ISaveFeeReceiptBMO;
import com.java110.fee.bmo.feeReceipt.IUpdateFeeReceiptBMO;
import com.java110.fee.bmo.feeReceiptDetail.IDeleteFeeReceiptDetailBMO;
import com.java110.fee.bmo.feeReceiptDetail.IGetFeeReceiptDetailBMO;
import com.java110.fee.bmo.feeReceiptDetail.ISaveFeeReceiptDetailBMO;
import com.java110.fee.bmo.feeReceiptDetail.IUpdateFeeReceiptDetailBMO;
import com.java110.intf.store.IStoreInnerServiceSMO;
import com.java110.po.feeReceipt.FeeReceiptPo;
import com.java110.po.feeReceiptDetail.FeeReceiptDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/feeReceipt")
public class FeeReceiptApi {

    @Autowired
    private ISaveFeeReceiptBMO saveFeeReceiptBMOImpl;
    @Autowired
    private IUpdateFeeReceiptBMO updateFeeReceiptBMOImpl;
    @Autowired
    private IDeleteFeeReceiptBMO deleteFeeReceiptBMOImpl;

    @Autowired
    private IGetFeeReceiptBMO getFeeReceiptBMOImpl;

    @Autowired
    private ISaveFeeReceiptDetailBMO saveFeeReceiptDetailBMOImpl;
    @Autowired
    private IUpdateFeeReceiptDetailBMO updateFeeReceiptDetailBMOImpl;
    @Autowired
    private IDeleteFeeReceiptDetailBMO deleteFeeReceiptDetailBMOImpl;

    @Autowired
    private IGetFeeReceiptDetailBMO getFeeReceiptDetailBMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMO;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeReceipt/saveFeeReceipt
     * @path /app/feeReceipt/saveFeeReceipt
     */
    @RequestMapping(value = "/saveFeeReceipt", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeReceipt(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "objName", "请求报文中未包含objName");


        FeeReceiptPo feeReceiptPo = BeanConvertUtil.covertBean(reqJson, FeeReceiptPo.class);
        return saveFeeReceiptBMOImpl.save(feeReceiptPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeReceipt/updateFeeReceipt
     * @path /app/feeReceipt/updateFeeReceipt
     */
    @RequestMapping(value = "/updateFeeReceipt", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeReceipt(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "objName", "请求报文中未包含objName");
        Assert.hasKeyAndValue(reqJson, "receiptId", "receiptId不能为空");


        FeeReceiptPo feeReceiptPo = BeanConvertUtil.covertBean(reqJson, FeeReceiptPo.class);
        return updateFeeReceiptBMOImpl.update(feeReceiptPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeReceipt/deleteFeeReceipt
     * @path /app/feeReceipt/deleteFeeReceipt
     */
    @RequestMapping(value = "/deleteFeeReceipt", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeReceipt(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "receiptId", "receiptId不能为空");


        FeeReceiptPo feeReceiptPo = BeanConvertUtil.covertBean(reqJson, FeeReceiptPo.class);
        return deleteFeeReceiptBMOImpl.delete(feeReceiptPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeReceipt/queryFeeReceipt
     * @path /app/feeReceipt/queryFeeReceipt
     */
    @RequestMapping(value = "/queryFeeReceipt", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeReceipt(@RequestParam(value = "communityId") String communityId,
                                                  @RequestParam(value = "objType", required = false) String objType,
                                                  @RequestParam(value = "objId", required = false) String objId,
                                                  @RequestParam(value = "roomId", required = false) String roomId,
                                                  @RequestParam(value = "payObjId", required = false) String payObjId,
                                                  @RequestParam(value = "receiptId", required = false) String receiptId,
                                                  @RequestParam(value = "receiptIds", required = false) String receiptIds,
                                                  @RequestParam(value = "detailIds", required = false) String detailIds,
                                                  @RequestParam(value = "qstartTime", required = false) String qstartTime,
                                                  @RequestParam(value = "qendTime", required = false) String qendTime,
                                                  @RequestParam(value = "feeId", required = false) String feeId,
                                                  @RequestParam(value = "page") int page,
                                                  @RequestParam(value = "row") int row,
                                                  @RequestHeader(value = "user_id") String userId) {
        //获取商户名称
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setUserId(userId);
        List<StoreUserDto> storeUserDtos = storeInnerServiceSMO.getStoreUserInfo(storeUserDto);
        String storeName = "";
        for (StoreUserDto storeUser : storeUserDtos) {
            storeName = storeUser.getName();
        }
        FeeReceiptDto feeReceiptDto = new FeeReceiptDto();
        feeReceiptDto.setPage(page);
        feeReceiptDto.setRow(row);
        feeReceiptDto.setCommunityId(communityId);
        feeReceiptDto.setReceiptId(receiptId);
        if (!StringUtil.isEmpty(receiptIds)) {
            feeReceiptDto.setReceiptIds(receiptIds.split(","));
        }
        if (!StringUtil.isEmpty(detailIds)) {
            feeReceiptDto.setDetailIds(detailIds.split(","));
        }
        feeReceiptDto.setObjType(objType);
        feeReceiptDto.setObjName(roomId);
        feeReceiptDto.setObjId(objId);
        feeReceiptDto.setStoreName(storeName);
        feeReceiptDto.setPayObjId(payObjId);
        feeReceiptDto.setFeeId(feeId);
        if (!StringUtil.isEmpty(qstartTime)) {
            feeReceiptDto.setQstartTime(qstartTime + " 00:00:00");
        }
        if (!StringUtil.isEmpty(qendTime)) {
            feeReceiptDto.setQendTime(qendTime + " 23:59:59");
        }
        return getFeeReceiptBMOImpl.get(feeReceiptDto);
    }


    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeReceipt/queryFeeReceiptNew
     * @path /app/feeReceipt/queryFeeReceiptNew
     */
    @RequestMapping(value = "/queryFeeReceiptNew", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeReceiptNew(@RequestParam(value = "communityId") String communityId,
                                                     @RequestParam(value = "objType", required = false) String objType,
                                                     @RequestParam(value = "roomName", required = false) String roomName,
                                                     @RequestParam(value = "type", required = false) String type,
                                                     @RequestParam(value = "qstartTime", required = false) String qstartTime,
                                                     @RequestParam(value = "qendTime", required = false) String qendTime,
                                                     @RequestParam(value = "page") int page,
                                                     @RequestParam(value = "row") int row) {
        FeeReceiptDtoNew feeReceiptDto = new FeeReceiptDtoNew();
        feeReceiptDto.setPage(page);
        feeReceiptDto.setRow(row);
        feeReceiptDto.setCommunityId(communityId);
        feeReceiptDto.setType(type);
        feeReceiptDto.setQstartTime(qstartTime);
        feeReceiptDto.setQendTime(qendTime);
        feeReceiptDto.setObjType(objType);
        feeReceiptDto.setObjName(roomName);
        return getFeeReceiptBMOImpl.gets(feeReceiptDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeReceipt/saveFeeReceiptDetail
     * @path /app/feeReceipt/saveFeeReceiptDetail
     */
    @RequestMapping(value = "/saveFeeReceiptDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveFeeReceiptDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");


        FeeReceiptDetailPo feeReceiptDetailPo = BeanConvertUtil.covertBean(reqJson, FeeReceiptDetailPo.class);
        return saveFeeReceiptDetailBMOImpl.save(feeReceiptDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeReceipt/updateFeeReceiptDetail
     * @path /app/feeReceipt/updateFeeReceiptDetail
     */
    @RequestMapping(value = "/updateFeeReceiptDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateFeeReceiptDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "amount", "请求报文中未包含amount");
        Assert.hasKeyAndValue(reqJson, "feeName", "请求报文中未包含feeName");
        Assert.hasKeyAndValue(reqJson, "receiptId", "receiptId不能为空");


        FeeReceiptDetailPo feeReceiptDetailPo = BeanConvertUtil.covertBean(reqJson, FeeReceiptDetailPo.class);
        return updateFeeReceiptDetailBMOImpl.update(feeReceiptDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /feeReceipt/deleteFeeReceiptDetail
     * @path /app/feeReceipt/deleteFeeReceiptDetail
     */
    @RequestMapping(value = "/deleteFeeReceiptDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFeeReceiptDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "receiptId", "receiptId不能为空");


        FeeReceiptDetailPo feeReceiptDetailPo = BeanConvertUtil.covertBean(reqJson, FeeReceiptDetailPo.class);
        return deleteFeeReceiptDetailBMOImpl.delete(feeReceiptDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /feeReceipt/queryFeeReceiptDetail
     * @path /app/feeReceipt/queryFeeReceiptDetail
     */
    @RequestMapping(value = "/queryFeeReceiptDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryFeeReceiptDetail(@RequestParam(value = "communityId") String communityId,
                                                        @RequestParam(value = "receiptId", required = false) String receiptId,
                                                        @RequestParam(value = "receiptIds", required = false) String receiptIds,
                                                        @RequestParam(value = "detailIds", required = false) String detailIds,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        FeeReceiptDetailDto feeReceiptDetailDto = new FeeReceiptDetailDto();
        feeReceiptDetailDto.setPage(page);
        feeReceiptDetailDto.setRow(row);
        feeReceiptDetailDto.setCommunityId(communityId);
        feeReceiptDetailDto.setReceiptId(receiptId);
        if (!StringUtil.isEmpty(receiptIds)) {
            feeReceiptDetailDto.setReceiptIds(receiptIds.split(","));
        }
        if (!StringUtil.isEmpty(detailIds)) {
            feeReceiptDetailDto.setDetailIds(detailIds.split(","));
        }
        return getFeeReceiptDetailBMOImpl.get(feeReceiptDetailDto);
    }

}
