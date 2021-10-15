package com.java110.intf.fee;

import com.alibaba.fastjson.JSONArray;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.BillDto;
import com.java110.dto.fee.BillOweFeeDto;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.dto.fee.FeeDto;
import com.java110.po.fee.PayFeePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeInnerServiceSMO
 * @Description 费用接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeApi")
public interface IFeeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param feeDto 数据对象分享
     * @return FeeDto 对象数据
     */
    @RequestMapping(value = "/queryFees", method = RequestMethod.POST)
    List<FeeDto> queryFees(@RequestBody FeeDto feeDto);

    /**
     * <p>查询简单费用信息</p>
     *
     * @param feeDto 数据对象分享
     * @return FeeDto 对象数据
     */
    @RequestMapping(value = "/querySimpleFees", method = RequestMethod.POST)
    List<FeeDto> querySimpleFees(@RequestBody FeeDto feeDto);

    /**
     * <p>从Business中查询</p>
     *
     * @param feeDto 数据对象分享
     * @return FeeDto 对象数据
     */
    @RequestMapping(value = "/queryBusinessFees", method = RequestMethod.POST)
    List<FeeDto> queryBusinessFees(@RequestBody FeeDto feeDto);


    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeesCount", method = RequestMethod.POST)
    int queryFeesCount(@RequestBody FeeDto feeDto);


    /**
     * <p>查询费用信息</p>
     *
     * @param feeAttrDto 数据对象分享
     * @return FeeDto 对象数据
     */
    @RequestMapping(value = "/queryFeeByAttr", method = RequestMethod.POST)
    List<FeeDto> queryFeeByAttr(@RequestBody FeeAttrDto feeAttrDto);


    /**
     * 查询<p>费用</p>总记录数
     *
     * @param feeAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeByAttrCount", method = RequestMethod.POST)
    int queryFeeByAttrCount(@RequestBody FeeAttrDto feeAttrDto);


    /**
     * 查询 账期信息 总数
     *
     * @param billDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryBillCount", method = RequestMethod.POST)
    public int queryBillCount(@RequestBody BillDto billDto);

    /**
     * 查询 账期信息
     *
     * @param billDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryBills", method = RequestMethod.POST)
    public List<BillDto> queryBills(@RequestBody BillDto billDto);


    /**
     * 查询账单欠费
     *
     * @param feeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/computeBillOweFeeCount", method = RequestMethod.POST)
    public int computeBillOweFeeCount(@RequestBody FeeDto feeDto);

    /**
     * 查询账单欠费
     *
     * @param feeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/computeEveryOweFee", method = RequestMethod.POST)
    public List<FeeDto> computeEveryOweFee(@RequestBody FeeDto feeDto);


    /**
     * 查询账单欠费
     *
     * @param feeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/computeEveryOweFeeCount", method = RequestMethod.POST)
    public int computeEveryOweFeeCount(@RequestBody FeeDto feeDto);

    /**
     * 查询账单欠费
     *
     * @param feeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/computeBillOweFee", method = RequestMethod.POST)
    public List<FeeDto> computeBillOweFee(@RequestBody FeeDto feeDto);


    /**
     * 查询 欠费数量
     *
     * @param billDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryBillOweFeeCount", method = RequestMethod.POST)
    public int queryBillOweFeeCount(@RequestBody BillOweFeeDto billDto);

    /**
     * 查询 欠费信息
     *
     * @param billDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryBillOweFees", method = RequestMethod.POST)
    public List<BillOweFeeDto> queryBillOweFees(@RequestBody BillOweFeeDto billDto);

    /**
     * 保存欠费
     *
     * @param billDto
     * @return
     */
    @RequestMapping(value = "/insertBillOweFees", method = RequestMethod.POST)
    public int insertBillOweFees(@RequestBody BillOweFeeDto billDto);

    /**
     * 保存欠费
     *
     * @param billDto
     * @return
     */
    @RequestMapping(value = "/updateBillOweFees", method = RequestMethod.POST)
    public int updateBillOweFees(@RequestBody BillOweFeeDto billDto);

    /**
     * 保存账单
     *
     * @param billDto
     * @return
     */
    @RequestMapping(value = "/insertBill", method = RequestMethod.POST)
    public int insertBill(@RequestBody BillDto billDto);


    @RequestMapping(value = "/updateFee", method = RequestMethod.POST)
    public int updateFee(@RequestBody PayFeePo payFeePo);

    @RequestMapping(value = "/saveFee", method = RequestMethod.POST)
    int saveFee(@RequestBody List<PayFeePo> payFeePos);

    @RequestMapping(value = "/saveOneFee", method = RequestMethod.POST)
    int saveOneFee(@RequestBody PayFeePo payFeePo);


    @RequestMapping(value = "/getAssetsFee", method = RequestMethod.POST)
    public JSONArray getAssetsFee(@RequestBody String communityId);

    /**
     * 根据批次删除费用
     *
     * @param payFeePo
     * @return
     */
    @RequestMapping(value = "/deleteFeesByBatch", method = RequestMethod.POST)
    int deleteFeesByBatch(@RequestBody PayFeePo payFeePo);
}
