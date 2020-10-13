package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.product.ProductDto;
import com.java110.goods.dao.IProductServiceDao;
import com.java110.intf.goods.IProductInnerServiceSMO;
import com.java110.po.product.ProductPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductInnerServiceSMOImpl extends BaseServiceSMO implements IProductInnerServiceSMO {

    @Autowired
    private IProductServiceDao productServiceDaoImpl;


    @Override
    public int saveProduct(@RequestBody ProductPo productPo) {
        int saveFlag = 1;
        productServiceDaoImpl.saveProductInfo(BeanConvertUtil.beanCovertMap(productPo));
        return saveFlag;
    }

    @Override
    public int updateProduct(@RequestBody ProductPo productPo) {
        int saveFlag = 1;
        productServiceDaoImpl.updateProductInfo(BeanConvertUtil.beanCovertMap(productPo));
        return saveFlag;
    }

    @Override
    public int deleteProduct(@RequestBody ProductPo productPo) {
        int saveFlag = 1;
        productPo.setStatusCd("1");
        productServiceDaoImpl.updateProductInfo(BeanConvertUtil.beanCovertMap(productPo));
        return saveFlag;
    }

    @Override
    public List<ProductDto> queryProducts(@RequestBody ProductDto productDto) {

        //校验是否传了 分页信息

        int page = productDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productDto.setPage((page - 1) * productDto.getRow());
        }

        List<ProductDto> products = BeanConvertUtil.covertBeanList(productServiceDaoImpl.getProductInfo(BeanConvertUtil.beanCovertMap(productDto)), ProductDto.class);

        return products;
    }


    @Override
    public int queryProductsCount(@RequestBody ProductDto productDto) {
        return productServiceDaoImpl.queryProductsCount(BeanConvertUtil.beanCovertMap(productDto));
    }

    public IProductServiceDao getProductServiceDaoImpl() {
        return productServiceDaoImpl;
    }

    public void setProductServiceDaoImpl(IProductServiceDao productServiceDaoImpl) {
        this.productServiceDaoImpl = productServiceDaoImpl;
    }
}
