package com.java110.community.smo.impl;

import com.java110.community.dao.IFloorAttrServiceDao;
import com.java110.dto.UnitDto;
import com.java110.dto.floorAttr.FloorAttrDto;
import com.java110.intf.community.IFloorAttrInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IFloorServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IFloorInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.FloorDto;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 小区内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FloorInnerServiceSMOImpl extends BaseServiceSMO implements IFloorInnerServiceSMO {

    @Autowired
    private IFloorServiceDao floorServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IFloorAttrServiceDao floorAttrServiceDaoImpl;

    /**
     * 查询 信息
     *
     * @param page        封装查询条件
     * @param row         行数
     * @param communityId 小区ID
     * @return 小区对应的楼
     */
    @Override
    public List<FloorDto> queryFloors(@RequestParam("page") int page, @RequestParam("row") int row, @RequestParam("communityId") String communityId) {
        Map<String, Object> floorInfo = new HashMap<String, Object>();
        floorInfo.put("page", (page - 1) * row);
        floorInfo.put("row", row * page);
        floorInfo.put("communityId", communityId);
        List<FloorDto> floors = BeanConvertUtil.covertBeanList(floorServiceDaoImpl.queryFloors(floorInfo), FloorDto.class);


        if (floors == null || floors.size() == 0) {
            return floors;
        }

        String[] userIds = getUserIds(floors);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (FloorDto floor : floors) {
            refreshFloor(floor, users);
        }
        return floors;
    }

    /**
     * 查询小区对应总记录数
     *
     * @param communityId 小区ID
     * @return 小区对应的楼总记录数
     */
    @Override
    public int queryFloorsCount(String communityId) {
        return floorServiceDaoImpl.queryFloorsCount(communityId);
    }

    @Override
    public List<FloorDto> queryFloors(@RequestBody  FloorDto floorDto) {

        //校验是否传了 分页信息

        int page = floorDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            floorDto.setPage((page - 1) * floorDto.getRow());
        }


        List<FloorDto> floors = BeanConvertUtil.covertBeanList(
                floorServiceDaoImpl.queryFloors(BeanConvertUtil.beanCovertMap(floorDto)), FloorDto.class);


        if (floors == null || floors.size() == 0) {
            return floors;
        }

        String[] userIds = getUserIds(floors);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        List<String> floorIds = new ArrayList<>();
        for (FloorDto floor : floors) {
            floorIds.add(floor.getFloorId());
            refreshFloor(floor, users);
        }

        Map info = new HashMap();
        info.put("floorIds", floorIds.toArray(new String[floorIds.size()]));
        info.put("communityId", floors.get( 0 ).getCommunityId());
        List<FloorAttrDto> floorAttrDtos = BeanConvertUtil.covertBeanList(floorAttrServiceDaoImpl.getFloorAttrInfo(info), FloorAttrDto.class);

        if (floorAttrDtos == null || floorAttrDtos.size() < 1) {
            return floors;
        }


        for (FloorDto tmpFloorDto : floors) {
            List<FloorAttrDto> tmpCommunityAttrDtos = new ArrayList<>();
            for (FloorAttrDto floorAttrDto : floorAttrDtos) {
                if (tmpFloorDto.getCommunityId().equals(floorAttrDto.getCommunityId()) && tmpFloorDto.getFloorId().equals( floorAttrDto.getFloorId() )) {
                    tmpCommunityAttrDtos.add(floorAttrDto);
                }
            }
            tmpFloorDto.setFloorAttrDto(tmpCommunityAttrDtos);

        }



        return floors;
    }

    @Override
    public int queryFloorsCount(@RequestBody FloorDto floorDto) {
        return floorServiceDaoImpl.queryFloorsCount(BeanConvertUtil.beanCovertMap(floorDto));
    }

    @Override
    public List<UnitDto> queryFloorAndUnits(@RequestBody UnitDto unitDto) {

        return BeanConvertUtil.covertBeanList(floorServiceDaoImpl.queryFloorAndUnits(BeanConvertUtil.beanCovertMap(unitDto)),UnitDto.class);
    }


    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param floor 小区楼信息
     * @param users 用户列表
     */
    private void refreshFloor(FloorDto floor, List<UserDto> users) {
        for (UserDto user : users) {
            if (floor.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, floor);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param floors 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<FloorDto> floors) {
        List<String> userIds = new ArrayList<String>();
        for (FloorDto floor : floors) {
            userIds.add(floor.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    public IFloorServiceDao getFloorServiceDaoImpl() {
        return floorServiceDaoImpl;
    }

    public void setFloorServiceDaoImpl(IFloorServiceDao floorServiceDaoImpl) {
        this.floorServiceDaoImpl = floorServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
