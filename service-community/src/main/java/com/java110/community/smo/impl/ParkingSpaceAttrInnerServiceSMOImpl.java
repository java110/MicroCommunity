package com.java110.community.smo.impl;


import com.java110.community.dao.IParkingSpaceAttrServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IParkingSpaceAttrInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.parking.ParkingSpaceAttrDto;
import com.java110.po.parkingSpaceAttr.ParkingSpaceAttrPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 车位属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ParkingSpaceAttrInnerServiceSMOImpl extends BaseServiceSMO implements IParkingSpaceAttrInnerServiceSMO {

    @Autowired
    private IParkingSpaceAttrServiceDao parkingSpaceAttrServiceDaoImpl;


    @Override
    public List<ParkingSpaceAttrDto> queryParkingSpaceAttrs(@RequestBody ParkingSpaceAttrDto parkingSpaceAttrDto) {

        //校验是否传了 分页信息

        int page = parkingSpaceAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingSpaceAttrDto.setPage((page - 1) * parkingSpaceAttrDto.getRow());
        }

        List<ParkingSpaceAttrDto> parkingSpaceAttrs = BeanConvertUtil.covertBeanList(parkingSpaceAttrServiceDaoImpl.getParkingSpaceAttrInfo(BeanConvertUtil.beanCovertMap(parkingSpaceAttrDto)), ParkingSpaceAttrDto.class);


        return parkingSpaceAttrs;
    }


    /**
     * 获取批量userId
     *
     * @param parkingSpaceAttrs 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ParkingSpaceAttrDto> parkingSpaceAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (ParkingSpaceAttrDto parkingSpaceAttr : parkingSpaceAttrs) {
            userIds.add(parkingSpaceAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryParkingSpaceAttrsCount(@RequestBody ParkingSpaceAttrDto parkingSpaceAttrDto) {
        return parkingSpaceAttrServiceDaoImpl.queryParkingSpaceAttrsCount(BeanConvertUtil.beanCovertMap(parkingSpaceAttrDto));
    }

    @Override
    public int saveParkingSpaceAttr(@RequestBody ParkingSpaceAttrPo parkingSpaceAttrPo ) {
        return parkingSpaceAttrServiceDaoImpl.saveParkingSpaceAttr(BeanConvertUtil.beanCovertMap(parkingSpaceAttrPo));
    }

    public IParkingSpaceAttrServiceDao getParkingSpaceAttrServiceDaoImpl() {
        return parkingSpaceAttrServiceDaoImpl;
    }

    public void setParkingSpaceAttrServiceDaoImpl(IParkingSpaceAttrServiceDao parkingSpaceAttrServiceDaoImpl) {
        this.parkingSpaceAttrServiceDaoImpl = parkingSpaceAttrServiceDaoImpl;
    }


}
