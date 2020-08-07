package com.java110.fee.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.importFeeDetail.ImportFeeDetailDto;
import com.java110.fee.bmo.importFeeDetail.IDeleteImportFeeDetailBMO;
import com.java110.fee.bmo.importFeeDetail.IGetImportFeeDetailBMO;
import com.java110.fee.bmo.importFeeDetail.ISaveImportFeeDetailBMO;
import com.java110.fee.bmo.importFeeDetail.IUpdateImportFeeDetailBMO;
import com.java110.po.importFeeDetail.ImportFeeDetailPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/importFeeDetail")
public class ImportFeeDetailApi {

    @Autowired
    private ISaveImportFeeDetailBMO saveImportFeeDetailBMOImpl;
    @Autowired
    private IUpdateImportFeeDetailBMO updateImportFeeDetailBMOImpl;
    @Autowired
    private IDeleteImportFeeDetailBMO deleteImportFeeDetailBMOImpl;

    @Autowired
    private IGetImportFeeDetailBMO getImportFeeDetailBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /importFeeDetail/saveImportFeeDetail
     * @path /app/importFeeDetail/saveImportFeeDetail
     */
    @RequestMapping(value = "/saveImportFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> saveImportFeeDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ifdId", "请求报文中未包含ifdId");


        ImportFeeDetailPo importFeeDetailPo = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailPo.class);
        return saveImportFeeDetailBMOImpl.save(importFeeDetailPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /importFeeDetail/updateImportFeeDetail
     * @path /app/importFeeDetail/updateImportFeeDetail
     */
    @RequestMapping(value = "/updateImportFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> updateImportFeeDetail(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ifdId", "请求报文中未包含ifdId");
        Assert.hasKeyAndValue(reqJson, "ifdId", "ifdId不能为空");


        ImportFeeDetailPo importFeeDetailPo = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailPo.class);
        return updateImportFeeDetailBMOImpl.update(importFeeDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /importFeeDetail/deleteImportFeeDetail
     * @path /app/importFeeDetail/deleteImportFeeDetail
     */
    @RequestMapping(value = "/deleteImportFeeDetail", method = RequestMethod.POST)
    public ResponseEntity<String> deleteImportFeeDetail(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "ifdId", "ifdId不能为空");


        ImportFeeDetailPo importFeeDetailPo = BeanConvertUtil.covertBean(reqJson, ImportFeeDetailPo.class);
        return deleteImportFeeDetailBMOImpl.delete(importFeeDetailPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /importFeeDetail/queryImportFeeDetail
     * @path /app/importFeeDetail/queryImportFeeDetail
     */
    @RequestMapping(value = "/queryImportFeeDetail", method = RequestMethod.GET)
    public ResponseEntity<String> queryImportFeeDetail(@RequestParam(value = "communityId") String communityId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        ImportFeeDetailDto importFeeDetailDto = new ImportFeeDetailDto();
        importFeeDetailDto.setPage(page);
        importFeeDetailDto.setRow(row);
        importFeeDetailDto.setCommunityId(communityId);
        return getImportFeeDetailBMOImpl.get(importFeeDetailDto);
    }
}
