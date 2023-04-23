package com.java110.community.smo.impl;


import com.java110.community.dao.IParkingAreaAttrServiceDao;
import com.java110.community.dao.IParkingAreaServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.parking.ParkingAreaDto;
import com.java110.dto.parking.ParkingAreaAttrDto;
import com.java110.intf.community.IParkingAreaInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 停车场内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ParkingAreaInnerServiceSMOImpl extends BaseServiceSMO implements IParkingAreaInnerServiceSMO {

    @Autowired
    private IParkingAreaServiceDao parkingAreaServiceDaoImpl;
    @Autowired
    private IParkingAreaAttrServiceDao parkingAreaAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ParkingAreaDto> queryParkingAreas(@RequestBody ParkingAreaDto parkingAreaDto) {

        //校验是否传了 分页信息

        int page = parkingAreaDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingAreaDto.setPage((page - 1) * parkingAreaDto.getRow());
        }

        List<ParkingAreaDto> parkingAreas = BeanConvertUtil.covertBeanList(parkingAreaServiceDaoImpl.getParkingAreaInfo(BeanConvertUtil.beanCovertMap(parkingAreaDto)), ParkingAreaDto.class);

        freshParkingAreas(parkingAreas);
        return parkingAreas;
    }

    private void freshParkingAreas(List<ParkingAreaDto> parkingAreas) {

        List<String> paIds = new ArrayList<>();

        if (parkingAreas == null || parkingAreas.size() < 1) {
            return;
        }
        for (ParkingAreaDto parkingArea : parkingAreas) {
            paIds.add(parkingArea.getPaId());
        }

        Map info = new HashMap();
        info.put("paIds", paIds.toArray(new String[paIds.size()]));
        List<ParkingAreaAttrDto> parkingAreaAttrDtos = BeanConvertUtil.covertBeanList(parkingAreaAttrServiceDaoImpl.getParkingAreaAttrInfo(info), ParkingAreaAttrDto.class);

        if (parkingAreaAttrDtos == null || parkingAreaAttrDtos.size() < 1) {
            return;
        }
        for (ParkingAreaDto parkingArea : parkingAreas) {
            List<ParkingAreaAttrDto> tmpParkingAreaAttrDtos = new ArrayList<>();
            for (ParkingAreaAttrDto parkingAreaAttrDto : parkingAreaAttrDtos) {
                if (parkingArea.getPaId().equals(parkingAreaAttrDto.getPaId())) {
                    tmpParkingAreaAttrDtos.add(parkingAreaAttrDto);
                }
            }
            parkingArea.setAttrs(tmpParkingAreaAttrDtos);
        }
    }


    @Override
    public int queryParkingAreasCount(@RequestBody ParkingAreaDto parkingAreaDto) {
        return parkingAreaServiceDaoImpl.queryParkingAreasCount(BeanConvertUtil.beanCovertMap(parkingAreaDto));
    }

    public IParkingAreaServiceDao getParkingAreaServiceDaoImpl() {
        return parkingAreaServiceDaoImpl;
    }

    public void setParkingAreaServiceDaoImpl(IParkingAreaServiceDao parkingAreaServiceDaoImpl) {
        this.parkingAreaServiceDaoImpl = parkingAreaServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
