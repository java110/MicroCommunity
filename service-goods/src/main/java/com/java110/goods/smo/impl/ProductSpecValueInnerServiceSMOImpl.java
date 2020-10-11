package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.dao.IProductSpecValueServiceDao;
import com.java110.intf.IProductSpecValueInnerServiceSMO;
import com.java110.po.productSpecValue.ProductSpecValuePo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品规格值内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductSpecValueInnerServiceSMOImpl extends BaseServiceSMO implements IProductSpecValueInnerServiceSMO {

    @Autowired
    private IProductSpecValueServiceDao productSpecValueServiceDaoImpl;


    @Override
    public int saveProductSpecValue(@RequestBody ProductSpecValuePo productSpecValuePo) {
        int saveFlag = 1;
        productSpecValueServiceDaoImpl.saveProductSpecValueInfo(BeanConvertUtil.beanCovertMap(productSpecValuePo));
        return saveFlag;
    }

    @Override
    public int updateProductSpecValue(@RequestBody ProductSpecValuePo productSpecValuePo) {
        int saveFlag = 1;
        productSpecValueServiceDaoImpl.updateProductSpecValueInfo(BeanConvertUtil.beanCovertMap(productSpecValuePo));
        return saveFlag;
    }

    @Override
    public int deleteProductSpecValue(@RequestBody ProductSpecValuePo productSpecValuePo) {
        int saveFlag = 1;
        productSpecValuePo.setStatusCd("1");
        productSpecValueServiceDaoImpl.updateProductSpecValueInfo(BeanConvertUtil.beanCovertMap(productSpecValuePo));
        return saveFlag;
    }

    @Override
    public List<ProductSpecValueDto> queryProductSpecValues(@RequestBody ProductSpecValueDto productSpecValueDto) {

        //校验是否传了 分页信息

        int page = productSpecValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productSpecValueDto.setPage((page - 1) * productSpecValueDto.getRow());
        }

        List<ProductSpecValueDto> productSpecValues = BeanConvertUtil.covertBeanList(productSpecValueServiceDaoImpl.getProductSpecValueInfo(BeanConvertUtil.beanCovertMap(productSpecValueDto)), ProductSpecValueDto.class);

        return productSpecValues;
    }

    @Override
    public List<ProductSpecValueDto> queryProductStockAndSales(@RequestBody ProductSpecValueDto productSpecValueDto) {
        List<ProductSpecValueDto> productSpecValues = BeanConvertUtil.covertBeanList(
                productSpecValueServiceDaoImpl.queryProductStockAndSales(BeanConvertUtil.beanCovertMap(productSpecValueDto)),
                ProductSpecValueDto.class);

        return productSpecValues;
    }


    @Override
    public int queryProductSpecValuesCount(@RequestBody ProductSpecValueDto productSpecValueDto) {
        return productSpecValueServiceDaoImpl.queryProductSpecValuesCount(BeanConvertUtil.beanCovertMap(productSpecValueDto));
    }

    public IProductSpecValueServiceDao getProductSpecValueServiceDaoImpl() {
        return productSpecValueServiceDaoImpl;
    }

    public void setProductSpecValueServiceDaoImpl(IProductSpecValueServiceDao productSpecValueServiceDaoImpl) {
        this.productSpecValueServiceDaoImpl = productSpecValueServiceDaoImpl;
    }
}
