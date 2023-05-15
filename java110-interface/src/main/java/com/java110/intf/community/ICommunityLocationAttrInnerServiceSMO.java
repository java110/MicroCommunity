package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.community.CommunityLocationAttrDto;
import com.java110.po.communityLocationAttr.CommunityLocationAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICommunityLocationAttrInnerServiceSMO
 * @Description 位置属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/communityLocationAttrApi")
public interface ICommunityLocationAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param communityLocationAttrDto 数据对象分享
     * @return CommunityLocationAttrDto 对象数据
     */
    @RequestMapping(value = "/queryCommunityLocationAttrs", method = RequestMethod.POST)
    List<CommunityLocationAttrDto> queryCommunityLocationAttrs(@RequestBody CommunityLocationAttrDto communityLocationAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityLocationAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommunityLocationAttrsCount", method = RequestMethod.POST)
    int queryCommunityLocationAttrsCount(@RequestBody CommunityLocationAttrDto communityLocationAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityLocationAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveCommunityLocationAttr", method = RequestMethod.POST)
    int saveCommunityLocationAttr(@RequestBody CommunityLocationAttrPo communityLocationAttrPo);
}
