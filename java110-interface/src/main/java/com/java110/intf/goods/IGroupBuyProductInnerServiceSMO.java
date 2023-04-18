package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.groupBuy.GroupBuyProductDto;
import com.java110.po.groupBuyProduct.GroupBuyProductPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IGroupBuyProductInnerServiceSMO
 * @Description 拼团产品接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/groupBuyProductApi")
public interface IGroupBuyProductInnerServiceSMO {


    @RequestMapping(value = "/saveGroupBuyProduct", method = RequestMethod.POST)
    public int saveGroupBuyProduct(@RequestBody GroupBuyProductPo groupBuyProductPo);

    @RequestMapping(value = "/updateGroupBuyProduct", method = RequestMethod.POST)
    public int updateGroupBuyProduct(@RequestBody GroupBuyProductPo groupBuyProductPo);

    @RequestMapping(value = "/deleteGroupBuyProduct", method = RequestMethod.POST)
    public int deleteGroupBuyProduct(@RequestBody GroupBuyProductPo groupBuyProductPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param groupBuyProductDto 数据对象分享
     * @return GroupBuyProductDto 对象数据
     */
    @RequestMapping(value = "/queryGroupBuyProducts", method = RequestMethod.POST)
    List<GroupBuyProductDto> queryGroupBuyProducts(@RequestBody GroupBuyProductDto groupBuyProductDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param groupBuyProductDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryGroupBuyProductsCount", method = RequestMethod.POST)
    int queryGroupBuyProductsCount(@RequestBody GroupBuyProductDto groupBuyProductDto);
}
