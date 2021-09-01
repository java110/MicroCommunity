package com.java110.community.smo.impl;


import com.java110.community.dao.IFloorAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IFloorAttrInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.floorAttr.FloorAttrDto;
import com.java110.po.floorAttr.FloorAttrPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 考勤班组属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FloorAttrInnerServiceSMOImpl extends BaseServiceSMO implements IFloorAttrInnerServiceSMO {

    @Autowired
    private IFloorAttrServiceDao floorAttrServiceDaoImpl;


    @Override
    public List<FloorAttrDto> queryFloorAttrs(@RequestBody FloorAttrDto floorAttrDto) {

        //校验是否传了 分页信息

        int page = floorAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            floorAttrDto.setPage((page - 1) * floorAttrDto.getRow());
        }

        List<FloorAttrDto> floorAttrs = BeanConvertUtil.covertBeanList(floorAttrServiceDaoImpl.getFloorAttrInfo(BeanConvertUtil.beanCovertMap(floorAttrDto)), FloorAttrDto.class);

        return floorAttrs;
    }


    @Override
    public int queryFloorAttrsCount(@RequestBody FloorAttrDto floorAttrDto) {
        return floorAttrServiceDaoImpl.queryFloorAttrsCount(BeanConvertUtil.beanCovertMap(floorAttrDto));
    }

    @Override
    public int saveFloorAttr(@RequestBody FloorAttrPo floorAttrPo) {
        return floorAttrServiceDaoImpl.saveFloorAttr(BeanConvertUtil.beanCovertMap(floorAttrPo));
    }

    @Override
    public int updateFloorAttrInfoInstance(@RequestBody FloorAttrPo floorAttrPo) {
        return floorAttrServiceDaoImpl.updateFloorAttrInfoInstance(BeanConvertUtil.beanCovertMap(floorAttrPo));
    }

    public IFloorAttrServiceDao getFloorAttrServiceDaoImpl() {
        return floorAttrServiceDaoImpl;
    }

    public void setFloorAttrServiceDaoImpl(IFloorAttrServiceDao floorAttrServiceDaoImpl) {
        this.floorAttrServiceDaoImpl = floorAttrServiceDaoImpl;
    }

}
