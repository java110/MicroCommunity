package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductDto;
import com.java110.po.product.ProductPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductInnerServiceSMO
 * @Description 产品接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productApi")
public interface IProductInnerServiceSMO {


    @RequestMapping(value = "/saveProduct", method = RequestMethod.POST)
    public int saveProduct(@RequestBody ProductPo productPo);

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public int updateProduct(@RequestBody ProductPo productPo);

    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    public int deleteProduct(@RequestBody ProductPo productPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param productDto 数据对象分享
     * @return ProductDto 对象数据
     */
    @RequestMapping(value = "/queryProducts", method = RequestMethod.POST)
    List<ProductDto> queryProducts(@RequestBody ProductDto productDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductsCount", method = RequestMethod.POST)
    int queryProductsCount(@RequestBody ProductDto productDto);
}
