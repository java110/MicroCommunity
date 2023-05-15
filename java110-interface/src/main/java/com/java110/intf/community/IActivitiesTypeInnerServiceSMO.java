package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.activities.ActivitiesTypeDto;
import com.java110.po.activitiesType.ActivitiesTypePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IActivitiesTypeInnerServiceSMO
 * @Description 信息分类接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/activitiesTypeApi")
public interface IActivitiesTypeInnerServiceSMO {


    @RequestMapping(value = "/saveActivitiesType", method = RequestMethod.POST)
    public int saveActivitiesType(@RequestBody ActivitiesTypePo activitiesTypePo);

    @RequestMapping(value = "/updateActivitiesType", method = RequestMethod.POST)
    public int updateActivitiesType(@RequestBody ActivitiesTypePo activitiesTypePo);

    @RequestMapping(value = "/deleteActivitiesType", method = RequestMethod.POST)
    public int deleteActivitiesType(@RequestBody ActivitiesTypePo activitiesTypePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param activitiesTypeDto 数据对象分享
     * @return ActivitiesTypeDto 对象数据
     */
    @RequestMapping(value = "/queryActivitiesTypes", method = RequestMethod.POST)
    List<ActivitiesTypeDto> queryActivitiesTypes(@RequestBody ActivitiesTypeDto activitiesTypeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param activitiesTypeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryActivitiesTypesCount", method = RequestMethod.POST)
    int queryActivitiesTypesCount(@RequestBody ActivitiesTypeDto activitiesTypeDto);
}
