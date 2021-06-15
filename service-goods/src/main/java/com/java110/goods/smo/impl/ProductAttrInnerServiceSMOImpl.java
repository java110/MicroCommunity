package com.java110.goods.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.productAttr.ProductAttrDto;
import com.java110.goods.dao.IProductAttrServiceDao;
import com.java110.intf.goods.IProductAttrInnerServiceSMO;
import com.java110.po.productAttr.ProductAttrPo;
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
public class ProductAttrInnerServiceSMOImpl extends BaseServiceSMO implements IProductAttrInnerServiceSMO {

    @Autowired
    private IProductAttrServiceDao productAttrServiceDaoImpl;


    @Override
    public int saveProductAttr(@RequestBody ProductAttrPo productAttrPo) {
        int saveFlag = 1;
        productAttrServiceDaoImpl.saveProductAttrInfo(BeanConvertUtil.beanCovertMap(productAttrPo));
        return saveFlag;
    }

    @Override
    public int updateProductAttr(@RequestBody ProductAttrPo productAttrPo) {
        int saveFlag = 1;
        productAttrServiceDaoImpl.updateProductAttrInfo(BeanConvertUtil.beanCovertMap(productAttrPo));
        return saveFlag;
    }

    @Override
    public int deleteProductAttr(@RequestBody ProductAttrPo productAttrPo) {
        int saveFlag = 1;
        productAttrPo.setStatusCd("1");
        productAttrServiceDaoImpl.updateProductAttrInfo(BeanConvertUtil.beanCovertMap(productAttrPo));
        return saveFlag;
    }

    @Override
    public List<ProductAttrDto> queryProductAttrs(@RequestBody ProductAttrDto productAttrDto) {

        //校验是否传了 分页信息

        int page = productAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            productAttrDto.setPage((page - 1) * productAttrDto.getRow());
        }

        List<ProductAttrDto> productAttrs = BeanConvertUtil.covertBeanList(productAttrServiceDaoImpl.getProductAttrInfo(BeanConvertUtil.beanCovertMap(productAttrDto)), ProductAttrDto.class);

        return productAttrs;
    }


    @Override
    public int queryProductAttrsCount(@RequestBody ProductAttrDto productAttrDto) {
        return productAttrServiceDaoImpl.queryProductAttrsCount(BeanConvertUtil.beanCovertMap(productAttrDto));
    }

    public IProductAttrServiceDao getProductAttrServiceDaoImpl() {
        return productAttrServiceDaoImpl;
    }

    public void setProductAttrServiceDaoImpl(IProductAttrServiceDao productAttrServiceDaoImpl) {
        this.productAttrServiceDaoImpl = productAttrServiceDaoImpl;
    }
}
