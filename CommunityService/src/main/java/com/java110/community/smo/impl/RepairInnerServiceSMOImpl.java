package com.java110.community.smo.impl;


import com.java110.community.dao.IRepairServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.repair.IRepairInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.repair.RepairDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 报修信息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RepairInnerServiceSMOImpl extends BaseServiceSMO implements IRepairInnerServiceSMO {

    @Autowired
    private IRepairServiceDao repairServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<RepairDto> queryRepairs(@RequestBody RepairDto repairDto) {

        //校验是否传了 分页信息

        int page = repairDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairDto.setPage((page - 1) * repairDto.getRow());
        }

        List<RepairDto> repairs = BeanConvertUtil.covertBeanList(repairServiceDaoImpl.getRepairInfo(BeanConvertUtil.beanCovertMap(repairDto)), RepairDto.class);


        return repairs;
    }


    @Override
    public int queryRepairsCount(@RequestBody RepairDto repairDto) {
        return repairServiceDaoImpl.queryRepairsCount(BeanConvertUtil.beanCovertMap(repairDto));
    }

    public IRepairServiceDao getRepairServiceDaoImpl() {
        return repairServiceDaoImpl;
    }

    public void setRepairServiceDaoImpl(IRepairServiceDao repairServiceDaoImpl) {
        this.repairServiceDaoImpl = repairServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
