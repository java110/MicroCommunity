package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.product.ProductSpecDto;
import com.java110.goods.dao.IProductSpecServiceDao;
import com.java110.intf.goods.IProductSpecInnerServiceSMO;
import com.java110.po.product.ProductSpecPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品规格内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductSpecInnerServiceSMOImpl extends BaseServiceSMO implements IProductSpecInnerServiceSMO {

    @Autowired
    private IProductSpecServiceDao productSpecServiceDaoImpl;


    @Override
    public int saveProductSpec(@RequestBody ProductSpecPo productSpecPo) {
        int saveFlag = 1;
        productSpecServiceDaoImpl.saveProductSpecInfo(BeanConvertUtil.beanCovertMap(productSpecPo));
        return saveFlag;
    }

    @Override
    public int updateProductSpec(@RequestBody ProductSpecPo productSpecPo) {
        int saveFlag = 1;
        productSpecServiceDaoImpl.updateProductSpecInfo(BeanConvertUtil.beanCovertMap(productSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteProductSpec(@RequestBody ProductSpecPo productSpecPo) {
        int saveFlag = 1;
        productSpecPo.setStatusCd("1");
        productSpecServiceDaoImpl.updateProductSpecInfo(BeanConvertUtil.beanCovertMap(productSpecPo));
        return saveFlag;
    }

    @Override
    public List<ProductSpecDto> queryProductSpecs(@RequestBody ProductSpecDto productSpecDto) {

        //校验是否传了 分页信息

        int page = productSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productSpecDto.setPage((page - 1) * productSpecDto.getRow());
        }

        List<ProductSpecDto> productSpecs = BeanConvertUtil.covertBeanList(productSpecServiceDaoImpl.getProductSpecInfo(BeanConvertUtil.beanCovertMap(productSpecDto)), ProductSpecDto.class);

        return productSpecs;
    }


    @Override
    public int queryProductSpecsCount(@RequestBody ProductSpecDto productSpecDto) {
        return productSpecServiceDaoImpl.queryProductSpecsCount(BeanConvertUtil.beanCovertMap(productSpecDto));
    }

    public IProductSpecServiceDao getProductSpecServiceDaoImpl() {
        return productSpecServiceDaoImpl;
    }

    public void setProductSpecServiceDaoImpl(IProductSpecServiceDao productSpecServiceDaoImpl) {
        this.productSpecServiceDaoImpl = productSpecServiceDaoImpl;
    }
}
