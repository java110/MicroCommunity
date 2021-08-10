package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.payFeeAudit.PayFeeAuditDto;
import com.java110.po.payFeeAudit.PayFeeAuditPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IPayFeeAuditInnerServiceSMO
 * @Description 缴费审核接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/initializePayFeeApi")
public interface IInitializePayFeeInnerServiceSMO {

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deletePayFee", method = RequestMethod.POST)
    int deletePayFee(@RequestBody Map communityId);
}
