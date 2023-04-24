package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.rentingPool.RentingPoolAttrDto;
import com.java110.intf.user.IRentingPoolAttrInnerServiceSMO;
import com.java110.po.rentingPoolAttr.RentingPoolAttrPo;
import com.java110.user.dao.IRentingPoolAttrServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 出租房屋属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RentingPoolAttrInnerServiceSMOImpl extends BaseServiceSMO implements IRentingPoolAttrInnerServiceSMO {

    @Autowired
    private IRentingPoolAttrServiceDao rentingPoolAttrServiceDaoImpl;


    @Override
    public int saveRentingPoolAttr(@RequestBody RentingPoolAttrPo rentingPoolAttrPo) {
        int saveFlag = 1;
        rentingPoolAttrServiceDaoImpl.saveRentingPoolAttrInfo(BeanConvertUtil.beanCovertMap(rentingPoolAttrPo));
        return saveFlag;
    }

    @Override
    public int updateRentingPoolAttr(@RequestBody RentingPoolAttrPo rentingPoolAttrPo) {
        int saveFlag = 1;
        rentingPoolAttrServiceDaoImpl.updateRentingPoolAttrInfo(BeanConvertUtil.beanCovertMap(rentingPoolAttrPo));
        return saveFlag;
    }

    @Override
    public int deleteRentingPoolAttr(@RequestBody RentingPoolAttrPo rentingPoolAttrPo) {
        int saveFlag = 1;
        rentingPoolAttrPo.setStatusCd("1");
        rentingPoolAttrServiceDaoImpl.updateRentingPoolAttrInfo(BeanConvertUtil.beanCovertMap(rentingPoolAttrPo));
        return saveFlag;
    }

    @Override
    public List<RentingPoolAttrDto> queryRentingPoolAttrs(@RequestBody RentingPoolAttrDto rentingPoolAttrDto) {

        //校验是否传了 分页信息

        int page = rentingPoolAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            rentingPoolAttrDto.setPage((page - 1) * rentingPoolAttrDto.getRow());
        }

        List<RentingPoolAttrDto> rentingPoolAttrs = BeanConvertUtil.covertBeanList(rentingPoolAttrServiceDaoImpl.getRentingPoolAttrInfo(BeanConvertUtil.beanCovertMap(rentingPoolAttrDto)), RentingPoolAttrDto.class);

        return rentingPoolAttrs;
    }


    @Override
    public int queryRentingPoolAttrsCount(@RequestBody RentingPoolAttrDto rentingPoolAttrDto) {
        return rentingPoolAttrServiceDaoImpl.queryRentingPoolAttrsCount(BeanConvertUtil.beanCovertMap(rentingPoolAttrDto));
    }

    public IRentingPoolAttrServiceDao getRentingPoolAttrServiceDaoImpl() {
        return rentingPoolAttrServiceDaoImpl;
    }

    public void setRentingPoolAttrServiceDaoImpl(IRentingPoolAttrServiceDao rentingPoolAttrServiceDaoImpl) {
        this.rentingPoolAttrServiceDaoImpl = rentingPoolAttrServiceDaoImpl;
    }
}
