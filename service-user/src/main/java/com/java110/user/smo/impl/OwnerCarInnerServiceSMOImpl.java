package com.java110.user.smo.impl;

import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerCarAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.entity.assetImport.ImportCustomCreateFeeDto;
import com.java110.entity.assetImport.ImportRoomFee;
import com.java110.intf.user.IOwnerCarInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.user.dao.IOwnerCarAttrServiceDao;
import com.java110.user.dao.IOwnerCarServiceDao;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 车辆管理内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerCarInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerCarInnerServiceSMO {

    @Autowired
    private IOwnerCarServiceDao ownerCarServiceDaoImpl;

    @Autowired
    private IOwnerCarAttrServiceDao ownerCarAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OwnerCarDto> queryOwnerCars(@RequestBody OwnerCarDto ownerCarDto) {

        //校验是否传了 分页信息

        int page = ownerCarDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerCarDto.setPage((page - 1) * ownerCarDto.getRow());
        }

        List<OwnerCarDto> ownerCars = BeanConvertUtil.covertBeanList(ownerCarServiceDaoImpl.getOwnerCarInfo(BeanConvertUtil.beanCovertMap(ownerCarDto)), OwnerCarDto.class);

        if (ownerCars != null && ownerCars.size() > 0) {
            String[] carIds = getCarIds(ownerCars);
            Map attrParamInfo = new HashMap();
            attrParamInfo.put("carIds", carIds);
            attrParamInfo.put("statusCd", StatusConstant.STATUS_CD_VALID);
            List<OwnerCarAttrDto> ownerCarAttrDtoList = BeanConvertUtil.covertBeanList(ownerCarAttrServiceDaoImpl.getOwnerCarAttrInfo(attrParamInfo), OwnerCarAttrDto.class);
            for (OwnerCarDto ownerCarDto1 : ownerCars) {
                refreshOwnerCars(ownerCarDto1, ownerCarAttrDtoList);
            }
        }
        return ownerCars;
    }

    /**
     * 获取carId 信息
     *
     * @param ownerCars 房屋信息
     * @return carId
     */
    private String[] getCarIds(List<OwnerCarDto> ownerCars) {
        List<String> carIds = new ArrayList<String>();
        for (OwnerCarDto ownerCarDto : ownerCars) {
            carIds.add(ownerCarDto.getCarId());
        }

        return carIds.toArray(new String[carIds.size()]);
    }

    /**
     * @param ownerCarDto
     * @param ownerCarAttrDtoList
     */
    private void refreshOwnerCars(OwnerCarDto ownerCarDto, List<OwnerCarAttrDto> ownerCarAttrDtoList) {

        if (ownerCarAttrDtoList == null || ownerCarAttrDtoList.size() == 0) {
            return;
        }
        List<OwnerCarAttrDto> tmpOwnerCarAttrDtos = new ArrayList<>();
        for (OwnerCarAttrDto ownerCarAttrDto : ownerCarAttrDtoList) {
            if (!ownerCarAttrDto.getCarId().equals(ownerCarDto.getCarId())) {
                continue;
            }
            if (StringUtil.isEmpty(ownerCarAttrDto.getValueName())) {
                ownerCarAttrDto.setValueName(ownerCarAttrDto.getValue());
            }
            tmpOwnerCarAttrDtos.add(ownerCarAttrDto);
        }
        ownerCarDto.setOwnerCarAttrDto(tmpOwnerCarAttrDtos);
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param ownerCar 小区车辆管理信息
     * @param users    用户列表
     */
    private void refreshOwnerCar(OwnerCarDto ownerCar, List<UserDto> users) {
        for (UserDto user : users) {
            if (ownerCar.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, ownerCar);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param ownerCars 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerCarDto> ownerCars) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerCarDto ownerCar : ownerCars) {
            userIds.add(ownerCar.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnerCarsCount(@RequestBody OwnerCarDto ownerCarDto) {
        return ownerCarServiceDaoImpl.queryOwnerCarsCount(BeanConvertUtil.beanCovertMap(ownerCarDto));
    }

    @Override
    public List<ImportRoomFee> freshCarIds(@RequestBody List<ImportRoomFee> tmpImportCarFees) {
        List<String> carNums = new ArrayList<>();
        for (ImportRoomFee importRoomFee : tmpImportCarFees) {
            if (StringUtil.isEmpty(importRoomFee.getCarNum())) {
                continue;
            }
            carNums.add(importRoomFee.getCarNum());
        }

        if (carNums.size() < 1) {
            return tmpImportCarFees;
        }
        Map<String, Object> info = new HashMap<>();
        info.put("carNums", carNums.toArray(new String[carNums.size()]));
        info.put("communityId", tmpImportCarFees.get(0).getCommunityId());
        info.put("statusCd", "0");
        List<OwnerCarDto> ownerCarDtos = BeanConvertUtil.covertBeanList(ownerCarServiceDaoImpl.getOwnerCarInfo(info), OwnerCarDto.class);

        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            for (ImportRoomFee importRoomFee : tmpImportCarFees) {
                if (ownerCarDto.getCarNum().equals(importRoomFee.getCarNum())) {
                    importRoomFee.setCarId(ownerCarDto.getCarId());
                    importRoomFee.setOwnerId(ownerCarDto.getOwnerId());
                    importRoomFee.setOwnerName(ownerCarDto.getOwnerName());
                    importRoomFee.setOwnerLink(ownerCarDto.getLink());
                }
            }
        }
        return tmpImportCarFees;
    }

    public List<ImportCustomCreateFeeDto> freshCarIdsByImportCustomCreateFee(@RequestBody List<ImportCustomCreateFeeDto> tmpImportCarFees) {
        List<String> carNums = new ArrayList<>();
        for (ImportCustomCreateFeeDto importRoomFee : tmpImportCarFees) {
            if (StringUtil.isEmpty(importRoomFee.getCarNum())) {
                continue;
            }
            carNums.add(importRoomFee.getCarNum());
        }

        if (carNums.size() < 1) {
            return tmpImportCarFees;
        }
        Map<String, Object> info = new HashMap<>();
        info.put("carNums", carNums.toArray(new String[carNums.size()]));
        info.put("communityId", tmpImportCarFees.get(0).getCommunityId());
        info.put("statusCd", "0");
        List<OwnerCarDto> ownerCarDtos = BeanConvertUtil.covertBeanList(ownerCarServiceDaoImpl.getOwnerCarInfo(info), OwnerCarDto.class);

        for (OwnerCarDto ownerCarDto : ownerCarDtos) {
            for (ImportCustomCreateFeeDto importRoomFee : tmpImportCarFees) {
                if (ownerCarDto.getCarNum().equals(importRoomFee.getCarNum())) {
                    importRoomFee.setPayObjId(ownerCarDto.getCarId());
                    importRoomFee.setOwnerId(ownerCarDto.getOwnerId());
                    importRoomFee.setOwnerName(ownerCarDto.getOwnerName());
                    importRoomFee.setOwnerLink(ownerCarDto.getLink());
                }
            }
        }
        return tmpImportCarFees;
    }

    @Override
    public long queryOwnerParkingSpaceCount(@RequestBody OwnerCarDto ownerCarDto) {
        return ownerCarServiceDaoImpl.queryOwnerParkingSpaceCount(BeanConvertUtil.beanCovertMap(ownerCarDto));
    }

    public IOwnerCarServiceDao getOwnerCarServiceDaoImpl() {
        return ownerCarServiceDaoImpl;
    }

    public void setOwnerCarServiceDaoImpl(IOwnerCarServiceDao ownerCarServiceDaoImpl) {
        this.ownerCarServiceDaoImpl = ownerCarServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
