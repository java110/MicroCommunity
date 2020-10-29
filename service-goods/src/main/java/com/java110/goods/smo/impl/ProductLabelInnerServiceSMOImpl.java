package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.product.ProductDto;
import com.java110.dto.productLabel.ProductLabelDto;
import com.java110.dto.productSpecValue.ProductSpecValueDto;
import com.java110.goods.dao.IProductLabelServiceDao;
import com.java110.intf.goods.IProductLabelInnerServiceSMO;
import com.java110.po.productLabel.ProductLabelPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 产品标签内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ProductLabelInnerServiceSMOImpl extends BaseServiceSMO implements IProductLabelInnerServiceSMO {

    @Autowired
    private IProductLabelServiceDao productLabelServiceDaoImpl;


    @Override
    public int saveProductLabel(@RequestBody ProductLabelPo productLabelPo) {
        int saveFlag = 1;
        productLabelServiceDaoImpl.saveProductLabelInfo(BeanConvertUtil.beanCovertMap(productLabelPo));
        return saveFlag;
    }

    @Override
    public int updateProductLabel(@RequestBody ProductLabelPo productLabelPo) {
        int saveFlag = 1;
        productLabelServiceDaoImpl.updateProductLabelInfo(BeanConvertUtil.beanCovertMap(productLabelPo));
        return saveFlag;
    }

    @Override
    public int deleteProductLabel(@RequestBody ProductLabelPo productLabelPo) {
        int saveFlag = 1;
        productLabelPo.setStatusCd("1");
        productLabelServiceDaoImpl.updateProductLabelInfo(BeanConvertUtil.beanCovertMap(productLabelPo));
        return saveFlag;
    }

    @Override
    public List<ProductLabelDto> queryProductLabels(@RequestBody ProductLabelDto productLabelDto) {

        //校验是否传了 分页信息

        int page = productLabelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productLabelDto.setPage((page - 1) * productLabelDto.getRow());
        }
        List<ProductLabelDto> products = new ArrayList<>();
        List<Map> prods = productLabelServiceDaoImpl.getProductLabelInfo(BeanConvertUtil.beanCovertMap(productLabelDto));
        ProductLabelDto tmpProductDto = null;
        ProductSpecValueDto productSpecValueDto = null;
        for (Map prod : prods) {
            tmpProductDto = BeanConvertUtil.covertBean(prod, ProductLabelDto.class);
            productSpecValueDto = BeanConvertUtil.covertBean(prod, ProductSpecValueDto.class);
            tmpProductDto.setDefaultSpecValue(productSpecValueDto);
            products.add(tmpProductDto);
        }
        return products;
    }


    @Override
    public int queryProductLabelsCount(@RequestBody ProductLabelDto productLabelDto) {
        return productLabelServiceDaoImpl.queryProductLabelsCount(BeanConvertUtil.beanCovertMap(productLabelDto));
    }

    public IProductLabelServiceDao getProductLabelServiceDaoImpl() {
        return productLabelServiceDaoImpl;
    }

    public void setProductLabelServiceDaoImpl(IProductLabelServiceDao productLabelServiceDaoImpl) {
        this.productLabelServiceDaoImpl = productLabelServiceDaoImpl;
    }
}
