package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.FeeAttrDto;
import com.java110.po.fee.FeeAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeAttrInnerServiceSMO
 * @Description 费用属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/feeAttrApi")
public interface IFeeAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param feeAttrDto 数据对象分享
     * @return FeeAttrDto 对象数据
     */
    @RequestMapping(value = "/queryFeeAttrs", method = RequestMethod.POST)
    List<FeeAttrDto> queryFeeAttrs(@RequestBody FeeAttrDto feeAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param feeAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryFeeAttrsCount", method = RequestMethod.POST)
    int queryFeeAttrsCount(@RequestBody FeeAttrDto feeAttrDto);

    /**
     * 保存费用属性
     * @param feeAttrPos
     * @return
     */
    @RequestMapping(value = "/saveFeeAttrs", method = RequestMethod.POST)
    int saveFeeAttrs(@RequestBody List<FeeAttrPo> feeAttrPos);

    /**
     * 保存费用属性
     * @param feeAttrPo
     * @return
     */
    @RequestMapping(value = "/saveFeeAttr", method = RequestMethod.POST)
    int saveFeeAttr(@RequestBody FeeAttrPo feeAttrPo);

    /**
     * 修改费用属性
     * @param feeAttrPo
     * @return
     */
    @RequestMapping(value = "/updateFeeAttr", method = RequestMethod.POST)
    int updateFeeAttr(@RequestBody FeeAttrPo feeAttrPo);
}
