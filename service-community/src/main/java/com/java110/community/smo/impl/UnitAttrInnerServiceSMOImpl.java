package com.java110.community.smo.impl;


import com.java110.community.dao.IUnitAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IUnitAttrInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.unitAttr.UnitAttrDto;
import com.java110.po.unitAttr.UnitAttrPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 单元属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UnitAttrInnerServiceSMOImpl extends BaseServiceSMO implements IUnitAttrInnerServiceSMO {

    @Autowired
    private IUnitAttrServiceDao unitAttrServiceDaoImpl;


    @Override
    public List<UnitAttrDto> queryUnitAttrs(@RequestBody UnitAttrDto unitAttrDto) {

        //校验是否传了 分页信息

        int page = unitAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            unitAttrDto.setPage((page - 1) * unitAttrDto.getRow());
        }

        List<UnitAttrDto> unitAttrs = BeanConvertUtil.covertBeanList(unitAttrServiceDaoImpl.getUnitAttrInfo(BeanConvertUtil.beanCovertMap(unitAttrDto)), UnitAttrDto.class);


        return unitAttrs;
    }



    @Override
    public int queryUnitAttrsCount(@RequestBody UnitAttrDto unitAttrDto) {
        return unitAttrServiceDaoImpl.queryUnitAttrsCount(BeanConvertUtil.beanCovertMap(unitAttrDto));
    }

    @Override
    public int saveUnitAttr(@RequestBody UnitAttrPo unitAttrPo) {
        return unitAttrServiceDaoImpl.saveUnitAttr(BeanConvertUtil.beanCovertMap(unitAttrPo));
    }

    public IUnitAttrServiceDao getUnitAttrServiceDaoImpl() {
        return unitAttrServiceDaoImpl;
    }

    public void setUnitAttrServiceDaoImpl(IUnitAttrServiceDao unitAttrServiceDaoImpl) {
        this.unitAttrServiceDaoImpl = unitAttrServiceDaoImpl;
    }

}
