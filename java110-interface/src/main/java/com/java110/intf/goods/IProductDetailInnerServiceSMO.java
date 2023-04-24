package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductDetailDto;
import com.java110.po.productDetail.ProductDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductDetailInnerServiceSMO
 * @Description 产品属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productDetailApi")
public interface IProductDetailInnerServiceSMO {


    @RequestMapping(value = "/saveProductDetail", method = RequestMethod.POST)
    public int saveProductDetail(@RequestBody ProductDetailPo productDetailPo);

    @RequestMapping(value = "/updateProductDetail", method = RequestMethod.POST)
    public int updateProductDetail(@RequestBody  ProductDetailPo productDetailPo);

    @RequestMapping(value = "/deleteProductDetail", method = RequestMethod.POST)
    public int deleteProductDetail(@RequestBody  ProductDetailPo productDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param productDetailDto 数据对象分享
     * @return ProductDetailDto 对象数据
     */
    @RequestMapping(value = "/queryProductDetails", method = RequestMethod.POST)
    List<ProductDetailDto> queryProductDetails(@RequestBody ProductDetailDto productDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductDetailsCount", method = RequestMethod.POST)
    int queryProductDetailsCount(@RequestBody ProductDetailDto productDetailDto);
}
