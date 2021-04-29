package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.product.ProductSpecDetailDto;
import com.java110.po.product.ProductSpecDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IProductSpecDetailInnerServiceSMO
 * @Description 产品规格明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/productSpecDetailApi")
public interface IProductSpecDetailInnerServiceSMO {


    @RequestMapping(value = "/saveProductSpecDetail", method = RequestMethod.POST)
    public int saveProductSpecDetail(@RequestBody ProductSpecDetailPo productSpecDetailPo);

    @RequestMapping(value = "/updateProductSpecDetail", method = RequestMethod.POST)
    public int updateProductSpecDetail(@RequestBody ProductSpecDetailPo productSpecDetailPo);

    @RequestMapping(value = "/deleteProductSpecDetail", method = RequestMethod.POST)
    public int deleteProductSpecDetail(@RequestBody ProductSpecDetailPo productSpecDetailPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param productSpecDetailDto 数据对象分享
     * @return ProductSpecDetailDto 对象数据
     */
    @RequestMapping(value = "/queryProductSpecDetails", method = RequestMethod.POST)
    List<ProductSpecDetailDto> queryProductSpecDetails(@RequestBody ProductSpecDetailDto productSpecDetailDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param productSpecDetailDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryProductSpecDetailsCount", method = RequestMethod.POST)
    int queryProductSpecDetailsCount(@RequestBody ProductSpecDetailDto productSpecDetailDto);
}
