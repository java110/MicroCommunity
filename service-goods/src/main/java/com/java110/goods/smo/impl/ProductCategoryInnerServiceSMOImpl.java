package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.productCategory.ProductCategoryDto;
import com.java110.goods.dao.IProductCategoryServiceDao;
import com.java110.intf.goods.IProductCategoryInnerServiceSMO;
import com.java110.po.productCategory.ProductCategoryPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品目录内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductCategoryInnerServiceSMOImpl extends BaseServiceSMO implements IProductCategoryInnerServiceSMO {

    @Autowired
    private IProductCategoryServiceDao productCategoryServiceDaoImpl;


    @Override
    public int saveProductCategory(@RequestBody ProductCategoryPo productCategoryPo) {
        int saveFlag = 1;
        productCategoryServiceDaoImpl.saveProductCategoryInfo(BeanConvertUtil.beanCovertMap(productCategoryPo));
        return saveFlag;
    }

    @Override
    public int updateProductCategory(@RequestBody ProductCategoryPo productCategoryPo) {
        int saveFlag = 1;
        productCategoryServiceDaoImpl.updateProductCategoryInfo(BeanConvertUtil.beanCovertMap(productCategoryPo));
        return saveFlag;
    }

    @Override
    public int deleteProductCategory(@RequestBody ProductCategoryPo productCategoryPo) {
        int saveFlag = 1;
        productCategoryPo.setStatusCd("1");
        productCategoryServiceDaoImpl.updateProductCategoryInfo(BeanConvertUtil.beanCovertMap(productCategoryPo));
        return saveFlag;
    }

    @Override
    public List<ProductCategoryDto> queryProductCategorys(@RequestBody ProductCategoryDto productCategoryDto) {

        //校验是否传了 分页信息

        int page = productCategoryDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productCategoryDto.setPage((page - 1) * productCategoryDto.getRow());
        }

        List<ProductCategoryDto> productCategorys = BeanConvertUtil.covertBeanList(productCategoryServiceDaoImpl.getProductCategoryInfo(BeanConvertUtil.beanCovertMap(productCategoryDto)), ProductCategoryDto.class);

        return productCategorys;
    }


    @Override
    public int queryProductCategorysCount(@RequestBody ProductCategoryDto productCategoryDto) {
        return productCategoryServiceDaoImpl.queryProductCategorysCount(BeanConvertUtil.beanCovertMap(productCategoryDto));
    }

    public IProductCategoryServiceDao getProductCategoryServiceDaoImpl() {
        return productCategoryServiceDaoImpl;
    }

    public void setProductCategoryServiceDaoImpl(IProductCategoryServiceDao productCategoryServiceDaoImpl) {
        this.productCategoryServiceDaoImpl = productCategoryServiceDaoImpl;
    }
}
