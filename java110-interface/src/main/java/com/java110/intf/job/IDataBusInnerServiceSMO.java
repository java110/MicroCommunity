package com.java110.intf.job;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.task.TaskDto;
import com.java110.entity.order.Business;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITaskInnerServiceSMO
 * @Description dataBus统一处理类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/dataBusApi")
public interface IDataBusInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param businesses 业务
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    boolean exchange(@RequestBody List<Business> businesses);

}
