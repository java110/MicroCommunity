package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductLabelDto;
import com.java110.po.productLabel.ProductLabelPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductLabelInnerServiceSMO
 * @Description 产品标签接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productLabelApi")
public interface IProductLabelInnerServiceSMO {


    @RequestMapping(value = "/saveProductLabel", method = RequestMethod.POST)
    public int saveProductLabel(@RequestBody ProductLabelPo productLabelPo);

    @RequestMapping(value = "/updateProductLabel", method = RequestMethod.POST)
    public int updateProductLabel(@RequestBody ProductLabelPo productLabelPo);

    @RequestMapping(value = "/deleteProductLabel", method = RequestMethod.POST)
    public int deleteProductLabel(@RequestBody ProductLabelPo productLabelPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param productLabelDto 数据对象分享
     * @return ProductLabelDto 对象数据
     */
    @RequestMapping(value = "/queryProductLabels", method = RequestMethod.POST)
    List<ProductLabelDto> queryProductLabels(@RequestBody ProductLabelDto productLabelDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productLabelDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductLabelsCount", method = RequestMethod.POST)
    int queryProductLabelsCount(@RequestBody ProductLabelDto productLabelDto);
}
