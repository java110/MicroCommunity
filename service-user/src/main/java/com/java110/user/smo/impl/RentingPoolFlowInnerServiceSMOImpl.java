package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.rentingPool.RentingPoolFlowDto;
import com.java110.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.java110.po.rentingPoolFlow.RentingPoolFlowPo;
import com.java110.user.dao.IRentingPoolFlowServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 出租流程内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RentingPoolFlowInnerServiceSMOImpl extends BaseServiceSMO implements IRentingPoolFlowInnerServiceSMO {

    @Autowired
    private IRentingPoolFlowServiceDao rentingPoolFlowServiceDaoImpl;


    @Override
    public int saveRentingPoolFlow(@RequestBody RentingPoolFlowPo rentingPoolFlowPo) {
        int saveFlag = 1;
        rentingPoolFlowServiceDaoImpl.saveRentingPoolFlowInfo(BeanConvertUtil.beanCovertMap(rentingPoolFlowPo));
        return saveFlag;
    }

    @Override
    public int updateRentingPoolFlow(@RequestBody RentingPoolFlowPo rentingPoolFlowPo) {
        int saveFlag = 1;
        rentingPoolFlowServiceDaoImpl.updateRentingPoolFlowInfo(BeanConvertUtil.beanCovertMap(rentingPoolFlowPo));
        return saveFlag;
    }

    @Override
    public int deleteRentingPoolFlow(@RequestBody RentingPoolFlowPo rentingPoolFlowPo) {
        int saveFlag = 1;
        rentingPoolFlowPo.setStatusCd("1");
        rentingPoolFlowServiceDaoImpl.updateRentingPoolFlowInfo(BeanConvertUtil.beanCovertMap(rentingPoolFlowPo));
        return saveFlag;
    }

    @Override
    public List<RentingPoolFlowDto> queryRentingPoolFlows(@RequestBody RentingPoolFlowDto rentingPoolFlowDto) {

        //校验是否传了 分页信息

        int page = rentingPoolFlowDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            rentingPoolFlowDto.setPage((page - 1) * rentingPoolFlowDto.getRow());
        }

        List<RentingPoolFlowDto> rentingPoolFlows = BeanConvertUtil.covertBeanList(rentingPoolFlowServiceDaoImpl.getRentingPoolFlowInfo(BeanConvertUtil.beanCovertMap(rentingPoolFlowDto)), RentingPoolFlowDto.class);

        return rentingPoolFlows;
    }


    @Override
    public int queryRentingPoolFlowsCount(@RequestBody RentingPoolFlowDto rentingPoolFlowDto) {
        return rentingPoolFlowServiceDaoImpl.queryRentingPoolFlowsCount(BeanConvertUtil.beanCovertMap(rentingPoolFlowDto));
    }

    public IRentingPoolFlowServiceDao getRentingPoolFlowServiceDaoImpl() {
        return rentingPoolFlowServiceDaoImpl;
    }

    public void setRentingPoolFlowServiceDaoImpl(IRentingPoolFlowServiceDao rentingPoolFlowServiceDaoImpl) {
        this.rentingPoolFlowServiceDaoImpl = rentingPoolFlowServiceDaoImpl;
    }
}
