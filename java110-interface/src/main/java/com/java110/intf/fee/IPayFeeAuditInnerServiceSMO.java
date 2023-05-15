package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.payFeeAudit.PayFeeAuditDto;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IPayFeeAuditInnerServiceSMO
 * @Description 缴费审核接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/payFeeAuditApi")
public interface IPayFeeAuditInnerServiceSMO {


    @RequestMapping(value = "/savePayFeeAudit", method = RequestMethod.POST)
    public int savePayFeeAudit(@RequestBody PayFeeAuditPo payFeeAuditPo);

    @RequestMapping(value = "/updatePayFeeAudit", method = RequestMethod.POST)
    public int updatePayFeeAudit(@RequestBody  PayFeeAuditPo payFeeAuditPo);

    @RequestMapping(value = "/deletePayFeeAudit", method = RequestMethod.POST)
    public int deletePayFeeAudit(@RequestBody  PayFeeAuditPo payFeeAuditPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param payFeeAuditDto 数据对象分享
     * @return PayFeeAuditDto 对象数据
     */
    @RequestMapping(value = "/queryPayFeeAudits", method = RequestMethod.POST)
    List<PayFeeAuditDto> queryPayFeeAudits(@RequestBody PayFeeAuditDto payFeeAuditDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param payFeeAuditDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryPayFeeAuditsCount", method = RequestMethod.POST)
    int queryPayFeeAuditsCount(@RequestBody PayFeeAuditDto payFeeAuditDto);
}
