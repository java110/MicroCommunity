package com.java110.community.smo.impl;


import com.java110.community.dao.IParkingAreaAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.parking.ParkingAreaAttrDto;
import com.java110.intf.community.IParkingAreaAttrInnerServiceSMO;
import com.java110.po.parkingAreaAttr.ParkingAreaAttrPo;
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
public class ParkingAreaAttrInnerServiceSMOImpl extends BaseServiceSMO implements IParkingAreaAttrInnerServiceSMO {

    @Autowired
    private IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl;


    @Override
    public List<ParkingAreaAttrDto> queryParkingAreaAttrs(@RequestBody ParkingAreaAttrDto parkingAreaAttrDto) {

        //校验是否传了 分页信息

        int page = parkingAreaAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingAreaAttrDto.setPage((page - 1) * parkingAreaAttrDto.getRow());
        }

        List<ParkingAreaAttrDto> parkingAreaAttrs = BeanConvertUtil.covertBeanList(parkingAreaAttrServiceDaoImpl.getParkingAreaAttrInfo(BeanConvertUtil.beanCovertMap(parkingAreaAttrDto)), ParkingAreaAttrDto.class);


        return parkingAreaAttrs;
    }



    @Override
    public int queryParkingAreaAttrsCount(@RequestBody ParkingAreaAttrDto parkingAreaAttrDto) {
        return parkingAreaAttrServiceDaoImpl.queryParkingAreaAttrsCount(BeanConvertUtil.beanCovertMap(parkingAreaAttrDto));
    }

    @Override
    public int saveParkingAreaAttr(@RequestBody ParkingAreaAttrPo parkingAreaAttrPo) {
        return parkingAreaAttrServiceDaoImpl.saveParkingAreaAttr(BeanConvertUtil.beanCovertMap(parkingAreaAttrPo));
    }

    public IParkingAreaAttrServiceDao getParkingAreaAttrServiceDaoImpl() {
        return parkingAreaAttrServiceDaoImpl;
    }

    public void setParkingAreaAttrServiceDaoImpl(IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl) {
        this.parkingAreaAttrServiceDaoImpl = parkingAreaAttrServiceDaoImpl;
    }


}
