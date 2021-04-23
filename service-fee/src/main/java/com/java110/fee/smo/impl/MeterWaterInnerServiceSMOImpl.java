package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.fee.IMeterWaterInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.meterWater.MeterWaterDto;
import com.java110.dto.user.UserDto;
import com.java110.fee.dao.IMeterWaterServiceDao;
import com.java110.po.fee.PayFeePo;
import com.java110.po.meterWater.MeterWaterPo;
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
 * @Description 水电费内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MeterWaterInnerServiceSMOImpl extends BaseServiceSMO implements IMeterWaterInnerServiceSMO {

    @Autowired
    private IMeterWaterServiceDao meterWaterServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MeterWaterDto> queryMeterWaters(@RequestBody MeterWaterDto meterWaterDto) {

        //校验是否传了 分页信息

        int page = meterWaterDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            meterWaterDto.setPage((page - 1) * meterWaterDto.getRow());
        }

        List<MeterWaterDto> meterWaters = BeanConvertUtil.covertBeanList(meterWaterServiceDaoImpl.getMeterWaterInfo(BeanConvertUtil.beanCovertMap(meterWaterDto)), MeterWaterDto.class);

        if (meterWaters == null || meterWaters.size() == 0) {
            return meterWaters;
        }

        String[] userIds = getUserIds(meterWaters);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (MeterWaterDto meterWater : meterWaters) {
            refreshMeterWater(meterWater, users);
        }
        return meterWaters;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param meterWater 小区水电费信息
     * @param users      用户列表
     */
    private void refreshMeterWater(MeterWaterDto meterWater, List<UserDto> users) {
        for (UserDto user : users) {
            if (meterWater.getWaterId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, meterWater);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param meterWaters 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<MeterWaterDto> meterWaters) {
        List<String> userIds = new ArrayList<String>();
        for (MeterWaterDto meterWater : meterWaters) {
            userIds.add(meterWater.getWaterId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryMeterWatersCount(@RequestBody MeterWaterDto meterWaterDto) {
        return meterWaterServiceDaoImpl.queryMeterWatersCount(BeanConvertUtil.beanCovertMap(meterWaterDto));
    }

    @Override
    public int saveMeterWaters(@RequestBody List<MeterWaterPo> meterWaterPos) {
        List<Map> fees = new ArrayList<>();
        for (MeterWaterPo payFeePo : meterWaterPos) {
            fees.add(BeanConvertUtil.beanCovertMap(payFeePo));
        }

        Map info = new HashMap();
        info.put("meterWaterPos", fees);
        return meterWaterServiceDaoImpl.insertMeterWaters(info);
    }

    public IMeterWaterServiceDao getMeterWaterServiceDaoImpl() {
        return meterWaterServiceDaoImpl;
    }

    public void setMeterWaterServiceDaoImpl(IMeterWaterServiceDao meterWaterServiceDaoImpl) {
        this.meterWaterServiceDaoImpl = meterWaterServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
