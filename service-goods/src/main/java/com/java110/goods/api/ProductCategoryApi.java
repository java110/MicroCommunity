package com.java110.goods.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.product.ProductCategoryDto;
import com.java110.goods.bmo.productCategory.IDeleteProductCategoryBMO;
import com.java110.goods.bmo.productCategory.IGetProductCategoryBMO;
import com.java110.goods.bmo.productCategory.ISaveProductCategoryBMO;
import com.java110.goods.bmo.productCategory.IUpdateProductCategoryBMO;
import com.java110.po.product.ProductCategoryPo;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/productCategory")
public class ProductCategoryApi {

    @Autowired
    private ISaveProductCategoryBMO saveProductCategoryBMOImpl;
    @Autowired
    private IUpdateProductCategoryBMO updateProductCategoryBMOImpl;
    @Autowired
    private IDeleteProductCategoryBMO deleteProductCategoryBMOImpl;

    @Autowired
    private IGetProductCategoryBMO getProductCategoryBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /productCategory/saveProductCategory
     * @path /app/productCategory/saveProductCategory
     */
    @RequestMapping(value = "/saveProductCategory", method = RequestMethod.POST)
    public ResponseEntity<String> saveProductCategory(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "categoryName", "请求报文中未包含categoryName");
        Assert.hasKeyAndValue(reqJson, "categoryLevel", "请求报文中未包含categoryLevel");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "isShow", "请求报文中未包含isShow");


        ProductCategoryPo productCategoryPo = BeanConvertUtil.covertBean(reqJson, ProductCategoryPo.class);
        return saveProductCategoryBMOImpl.save(productCategoryPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /productCategory/updateProductCategory
     * @path /app/productCategory/updateProductCategory
     */
    @RequestMapping(value = "/updateProductCategory", method = RequestMethod.POST)
    public ResponseEntity<String> updateProductCategory(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "categoryName", "请求报文中未包含categoryName");
        Assert.hasKeyAndValue(reqJson, "categoryLevel", "请求报文中未包含categoryLevel");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "isShow", "请求报文中未包含isShow");
        Assert.hasKeyAndValue(reqJson, "categoryId", "categoryId不能为空");


        ProductCategoryPo productCategoryPo = BeanConvertUtil.covertBean(reqJson, ProductCategoryPo.class);
        return updateProductCategoryBMOImpl.update(productCategoryPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /productCategory/deleteProductCategory
     * @path /app/productCategory/deleteProductCategory
     */
    @RequestMapping(value = "/deleteProductCategory", method = RequestMethod.POST)
    public ResponseEntity<String> deleteProductCategory(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "categoryId", "categoryId不能为空");


        ProductCategoryPo productCategoryPo = BeanConvertUtil.covertBean(reqJson, ProductCategoryPo.class);
        return deleteProductCategoryBMOImpl.delete(productCategoryPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 商户ID
     * @return
     * @serviceCode /productCategory/queryProductCategory
     * @path /app/productCategory/queryProductCategory
     */
    @RequestMapping(value = "/queryProductCategory", method = RequestMethod.GET)
    public ResponseEntity<String> queryProductCategory(@RequestHeader(value = "store-id") String storeId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setPage(page);
        productCategoryDto.setRow(row);
        productCategoryDto.setStoreId(storeId);
        return getProductCategoryBMOImpl.get(productCategoryDto);
    }
}
