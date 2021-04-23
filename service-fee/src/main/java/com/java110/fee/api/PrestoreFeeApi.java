package com.java110.fee.api;


import com.alibaba.fastjson.JSONObject;
import com.java110.dto.prestoreFee.PrestoreFeeDto;
import com.java110.fee.bmo.prestoreFee.IDeletePrestoreFeeBMO;
import com.java110.fee.bmo.prestoreFee.IGetPrestoreFeeBMO;
import com.java110.fee.bmo.prestoreFee.ISavePrestoreFeeBMO;
import com.java110.fee.bmo.prestoreFee.IUpdatePrestoreFeeBMO;
import com.java110.po.prestoreFee.PrestoreFeePo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/prestoreFee")
public class PrestoreFeeApi {

    @Autowired
    private ISavePrestoreFeeBMO savePrestoreFeeBMOImpl;
    @Autowired
    private IUpdatePrestoreFeeBMO updatePrestoreFeeBMOImpl;
    @Autowired
    private IDeletePrestoreFeeBMO deletePrestoreFeeBMOImpl;

    @Autowired
    private IGetPrestoreFeeBMO getPrestoreFeeBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /prestoreFee/savePrestoreFee
     * @path /app/prestoreFee/savePrestoreFee
     */
    @RequestMapping(value = "/savePrestoreFee", method = RequestMethod.POST)
    public ResponseEntity<String> savePrestoreFee(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "prestoreFeeType", "请求报文中未包含prestoreFeeType");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "prestoreFeeObjType", "请求报文中未包含prestoreFeeObjType");
        Assert.hasKeyAndValue(reqJson, "prestoreFeeAmount", "请求报文中未包含prestoreFeeAmount");


        PrestoreFeePo prestoreFeePo = BeanConvertUtil.covertBean(reqJson, PrestoreFeePo.class);
        return savePrestoreFeeBMOImpl.save(prestoreFeePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /prestoreFee/updatePrestoreFee
     * @path /app/prestoreFee/updatePrestoreFee
     */
    @RequestMapping(value = "/updatePrestoreFee", method = RequestMethod.POST)
    public ResponseEntity<String> updatePrestoreFee(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "prestoreFeeType", "请求报文中未包含prestoreFeeType");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "prestoreFeeObjType", "请求报文中未包含prestoreFeeObjType");
        Assert.hasKeyAndValue(reqJson, "prestoreFeeAmount", "请求报文中未包含prestoreFeeAmount");
        Assert.hasKeyAndValue(reqJson, "prestoreFeeId", "prestoreFeeId不能为空");


        PrestoreFeePo prestoreFeePo = BeanConvertUtil.covertBean(reqJson, PrestoreFeePo.class);
        return updatePrestoreFeeBMOImpl.update(prestoreFeePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /prestoreFee/deletePrestoreFee
     * @path /app/prestoreFee/deletePrestoreFee
     */
    @RequestMapping(value = "/deletePrestoreFee", method = RequestMethod.POST)
    public ResponseEntity<String> deletePrestoreFee(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "prestoreFeeId", "prestoreFeeId不能为空");


        PrestoreFeePo prestoreFeePo = BeanConvertUtil.covertBean(reqJson, PrestoreFeePo.class);
        return deletePrestoreFeeBMOImpl.delete(prestoreFeePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /prestoreFee/queryPrestoreFee
     * @path /app/prestoreFee/queryPrestoreFee
     */
    @RequestMapping(value = "/queryPrestoreFee", method = RequestMethod.GET)
    public ResponseEntity<String> queryPrestoreFee(@RequestParam(value = "communityId") String communityId,
                                                   @RequestParam(value = "page") int page,
                                                   @RequestParam(value = "state" ,required = false) String state,
                                                   @RequestParam(value = "roomId" ,required = false) String roomId,
                                                   @RequestParam(value = "prestoreFeeType" ,required = false) String prestoreFeeType,
                                                   @RequestParam(value = "prestoreFeeAmount" ,required = false) String prestoreFeeAmount,
                                                   @RequestParam(value = "row") int row) {
        PrestoreFeeDto prestoreFeeDto = new PrestoreFeeDto();
        prestoreFeeDto.setPage(page);
        prestoreFeeDto.setRow(row);
        prestoreFeeDto.setState(state);
        prestoreFeeDto.setRoomId(roomId);
        prestoreFeeDto.setPrestoreFeeType(prestoreFeeType);
        prestoreFeeDto.setPrestoreFeeAmount(prestoreFeeAmount);
        prestoreFeeDto.setCommunityId(communityId);
        return getPrestoreFeeBMOImpl.get(prestoreFeeDto);
    }
}
