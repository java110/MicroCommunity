package com.java110.common.smo.impl;


import com.java110.common.dao.IAttrSpecServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attrSpec.AttrSpecDto;
import com.java110.intf.common.IAttrSpecInnerServiceSMO;
import com.java110.po.attrSpec.AttrSpecPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 属性规格表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AttrSpecInnerServiceSMOImpl extends BaseServiceSMO implements IAttrSpecInnerServiceSMO {

    @Autowired
    private IAttrSpecServiceDao attrSpecServiceDaoImpl;


    @Override
    public int saveAttrSpec(@RequestBody AttrSpecPo attrSpecPo) {
        int saveFlag = 1;
        attrSpecServiceDaoImpl.saveAttrSpecInfo(BeanConvertUtil.beanCovertMap(attrSpecPo));
        return saveFlag;
    }

    @Override
    public int updateAttrSpec(@RequestBody AttrSpecPo attrSpecPo) {
        int saveFlag = 1;
        attrSpecServiceDaoImpl.updateAttrSpecInfo(BeanConvertUtil.beanCovertMap(attrSpecPo));
        return saveFlag;
    }

    @Override
    public int deleteAttrSpec(@RequestBody AttrSpecPo attrSpecPo) {
        int saveFlag = 1;
        attrSpecPo.setStatusCd("1");
        attrSpecServiceDaoImpl.updateAttrSpecInfo(BeanConvertUtil.beanCovertMap(attrSpecPo));
        return saveFlag;
    }

    @Override
    public List<AttrSpecDto> queryAttrSpecs(@RequestBody AttrSpecDto attrSpecDto) {

        //校验是否传了 分页信息

        int page = attrSpecDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attrSpecDto.setPage((page - 1) * attrSpecDto.getRow());
        }

        List<AttrSpecDto> attrSpecs = BeanConvertUtil.covertBeanList(attrSpecServiceDaoImpl.getAttrSpecInfo(BeanConvertUtil.beanCovertMap(attrSpecDto)), AttrSpecDto.class);

        return attrSpecs;
    }


    @Override
    public int queryAttrSpecsCount(@RequestBody AttrSpecDto attrSpecDto) {
        return attrSpecServiceDaoImpl.queryAttrSpecsCount(BeanConvertUtil.beanCovertMap(attrSpecDto));
    }

    public IAttrSpecServiceDao getAttrSpecServiceDaoImpl() {
        return attrSpecServiceDaoImpl;
    }

    public void setAttrSpecServiceDaoImpl(IAttrSpecServiceDao attrSpecServiceDaoImpl) {
        this.attrSpecServiceDaoImpl = attrSpecServiceDaoImpl;
    }
}
