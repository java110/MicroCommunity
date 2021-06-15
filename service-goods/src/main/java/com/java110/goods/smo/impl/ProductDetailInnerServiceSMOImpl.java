package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.productDetail.ProductDetailDto;
import com.java110.goods.dao.IProductDetailServiceDao;
import com.java110.intf.goods.IProductDetailInnerServiceSMO;
import com.java110.po.productDetail.ProductDetailPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductDetailInnerServiceSMOImpl extends BaseServiceSMO implements IProductDetailInnerServiceSMO {

    @Autowired
    private IProductDetailServiceDao productDetailServiceDaoImpl;


    @Override
    public int saveProductDetail(@RequestBody ProductDetailPo productDetailPo) {
        int saveFlag = 1;
        productDetailServiceDaoImpl.saveProductDetailInfo(BeanConvertUtil.beanCovertMap(productDetailPo));
        return saveFlag;
    }

    @Override
    public int updateProductDetail(@RequestBody ProductDetailPo productDetailPo) {
        int saveFlag = 1;
        productDetailServiceDaoImpl.updateProductDetailInfo(BeanConvertUtil.beanCovertMap(productDetailPo));
        return saveFlag;
    }

    @Override
    public int deleteProductDetail(@RequestBody ProductDetailPo productDetailPo) {
        int saveFlag = 1;
        productDetailPo.setStatusCd("1");
        productDetailServiceDaoImpl.updateProductDetailInfo(BeanConvertUtil.beanCovertMap(productDetailPo));
        return saveFlag;
    }

    @Override
    public List<ProductDetailDto> queryProductDetails(@RequestBody ProductDetailDto productDetailDto) {

        //校验是否传了 分页信息

        int page = productDetailDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productDetailDto.setPage((page - 1) * productDetailDto.getRow());
        }

        List<ProductDetailDto> productDetails = BeanConvertUtil.covertBeanList(productDetailServiceDaoImpl.getProductDetailInfo(BeanConvertUtil.beanCovertMap(productDetailDto)), ProductDetailDto.class);

        return productDetails;
    }


    @Override
    public int queryProductDetailsCount(@RequestBody ProductDetailDto productDetailDto) {
        return productDetailServiceDaoImpl.queryProductDetailsCount(BeanConvertUtil.beanCovertMap(productDetailDto));
    }

    public IProductDetailServiceDao getProductDetailServiceDaoImpl() {
        return productDetailServiceDaoImpl;
    }

    public void setProductDetailServiceDaoImpl(IProductDetailServiceDao productDetailServiceDaoImpl) {
        this.productDetailServiceDaoImpl = productDetailServiceDaoImpl;
    }
}
