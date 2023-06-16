package com.java110.community.smo.impl;


import com.java110.community.dao.IRepairReturnVisitServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.repair.RepairReturnVisitDto;
import com.java110.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.java110.po.repair.RepairReturnVisitPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 报修回访内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class RepairReturnVisitInnerServiceSMOImpl extends BaseServiceSMO implements IRepairReturnVisitInnerServiceSMO {

    @Autowired
    private IRepairReturnVisitServiceDao repairReturnVisitServiceDaoImpl;


    @Override
    public int saveRepairReturnVisit(@RequestBody RepairReturnVisitPo repairReturnVisitPo) {
        int saveFlag = 1;
        repairReturnVisitServiceDaoImpl.saveRepairReturnVisitInfo(BeanConvertUtil.beanCovertMap(repairReturnVisitPo));
        return saveFlag;
    }

    @Override
    public int updateRepairReturnVisit(@RequestBody RepairReturnVisitPo repairReturnVisitPo) {
        int saveFlag = 1;
        repairReturnVisitServiceDaoImpl.updateRepairReturnVisitInfo(BeanConvertUtil.beanCovertMap(repairReturnVisitPo));
        return saveFlag;
    }

    @Override
    public int deleteRepairReturnVisit(@RequestBody RepairReturnVisitPo repairReturnVisitPo) {
        int saveFlag = 1;
        repairReturnVisitPo.setStatusCd("1");
        repairReturnVisitServiceDaoImpl.updateRepairReturnVisitInfo(BeanConvertUtil.beanCovertMap(repairReturnVisitPo));
        return saveFlag;
    }

    @Override
    public List<RepairReturnVisitDto> queryRepairReturnVisits(@RequestBody RepairReturnVisitDto repairReturnVisitDto) {

        //校验是否传了 分页信息

        int page = repairReturnVisitDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            repairReturnVisitDto.setPage((page - 1) * repairReturnVisitDto.getRow());
        }

        List<RepairReturnVisitDto> repairReturnVisits = BeanConvertUtil.covertBeanList(repairReturnVisitServiceDaoImpl.getRepairReturnVisitInfo(BeanConvertUtil.beanCovertMap(repairReturnVisitDto)), RepairReturnVisitDto.class);

        return repairReturnVisits;
    }


    @Override
    public int queryRepairReturnVisitsCount(@RequestBody RepairReturnVisitDto repairReturnVisitDto) {
        return repairReturnVisitServiceDaoImpl.queryRepairReturnVisitsCount(BeanConvertUtil.beanCovertMap(repairReturnVisitDto));
    }

    public IRepairReturnVisitServiceDao getRepairReturnVisitServiceDaoImpl() {
        return repairReturnVisitServiceDaoImpl;
    }

    public void setRepairReturnVisitServiceDaoImpl(IRepairReturnVisitServiceDao repairReturnVisitServiceDaoImpl) {
        this.repairReturnVisitServiceDaoImpl = repairReturnVisitServiceDaoImpl;
    }
}
