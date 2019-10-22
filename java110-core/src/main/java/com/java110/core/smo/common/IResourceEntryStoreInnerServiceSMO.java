package com.java110.core.smo.common;

import com.java110.core.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/commonApi")
public interface IResourceEntryStoreInnerServiceSMO {


    /**
     * <p>启动流程</p>
     *
     *
     * @return CommunityDto 对象数据
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.GET)
    public String startProcess();

    /**
     * 完成任务
     * @param processInstanceId
     */
    @RequestMapping(value = "/getTaskAndComplete", method = RequestMethod.GET)
    public void getTaskAndComplete(String processInstanceId);
}
