package com.java110.community.smo.impl;

import com.java110.community.dao.IApplyRoomDiscountRecordServiceDao;
import com.java110.intf.community.IApplyRoomDiscountRecordInnerServiceSMO;
import com.java110.dto.applyRoomDiscount.ApplyRoomDiscountRecordDto;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.applyRoomDiscountRecord.ApplyRoomDiscountRecordPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.user.UserDto;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 验房记录内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ApplyRoomDiscountRecordInnerServiceSMOImpl extends BaseServiceSMO implements IApplyRoomDiscountRecordInnerServiceSMO {

    @Autowired
    private IApplyRoomDiscountRecordServiceDao applyRoomDiscountRecordServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ApplyRoomDiscountRecordDto> queryApplyRoomDiscountRecords(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto) {

        //校验是否传了 分页信息

        int page = applyRoomDiscountRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applyRoomDiscountRecordDto.setPage((page - 1) * applyRoomDiscountRecordDto.getRow());
        }

        List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecords = BeanConvertUtil.covertBeanList(applyRoomDiscountRecordServiceDaoImpl.getApplyRoomDiscountRecordInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountRecordDto)), ApplyRoomDiscountRecordDto.class);

        if (applyRoomDiscountRecords == null || applyRoomDiscountRecords.size() == 0) {
            return applyRoomDiscountRecords;
        }

        String[] userIds = getUserIds(applyRoomDiscountRecords);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ApplyRoomDiscountRecordDto applyRoomDiscountRecord : applyRoomDiscountRecords) {
            refreshApplyRoomDiscountRecord(applyRoomDiscountRecord, users);
        }
        return applyRoomDiscountRecords;
    }

    @Override
    public List<ApplyRoomDiscountRecordDto> selectApplyRoomDiscountRecords(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto) {

        //校验是否传了 分页信息

        int page = applyRoomDiscountRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applyRoomDiscountRecordDto.setPage((page - 1) * applyRoomDiscountRecordDto.getRow());
        }

        List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecords = BeanConvertUtil.covertBeanList(applyRoomDiscountRecordServiceDaoImpl.selectApplyRoomDiscountRecordInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountRecordDto)), ApplyRoomDiscountRecordDto.class);

        if (applyRoomDiscountRecords == null || applyRoomDiscountRecords.size() == 0) {
            return applyRoomDiscountRecords;
        }

        String[] userIds = getUserIds(applyRoomDiscountRecords);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ApplyRoomDiscountRecordDto applyRoomDiscountRecord : applyRoomDiscountRecords) {
            refreshApplyRoomDiscountRecord(applyRoomDiscountRecord, users);
        }
        return applyRoomDiscountRecords;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param applyRoomDiscountRecord 小区验房记录信息
     * @param users                   用户列表
     */
    private void refreshApplyRoomDiscountRecord(ApplyRoomDiscountRecordDto applyRoomDiscountRecord, List<UserDto> users) {
        for (UserDto user : users) {
            if (applyRoomDiscountRecord.getArdrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, applyRoomDiscountRecord);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param applyRoomDiscountRecords 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ApplyRoomDiscountRecordDto> applyRoomDiscountRecords) {
        List<String> userIds = new ArrayList<String>();
        for (ApplyRoomDiscountRecordDto applyRoomDiscountRecord : applyRoomDiscountRecords) {
            userIds.add(applyRoomDiscountRecord.getArdrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryApplyRoomDiscountRecordsCount(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto) {
        return applyRoomDiscountRecordServiceDaoImpl.queryApplyRoomDiscountRecordsCount(BeanConvertUtil.beanCovertMap(applyRoomDiscountRecordDto));
    }

    @Override
    public int selectApplyRoomDiscountRecordsCount(@RequestBody ApplyRoomDiscountRecordDto applyRoomDiscountRecordDto) {
        return applyRoomDiscountRecordServiceDaoImpl.selectApplyRoomDiscountRecordsCount(BeanConvertUtil.beanCovertMap(applyRoomDiscountRecordDto));
    }

    @Override
    public int saveApplyRoomDiscountRecord(@RequestBody ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo) {
        return applyRoomDiscountRecordServiceDaoImpl.saveApplyRoomDiscountRecordInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountRecordPo));
    }

    @Override
    public int deleteApplyRoomDiscountRecord(@RequestBody ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo) {
        int saveFlag = 1;
        applyRoomDiscountRecordPo.setStatusCd("1");
        applyRoomDiscountRecordServiceDaoImpl.updateApplyRoomDiscountRecordInfoInstance(BeanConvertUtil.beanCovertMap(applyRoomDiscountRecordPo));
        return saveFlag;
    }

    public IApplyRoomDiscountRecordServiceDao getApplyRoomDiscountRecordServiceDaoImpl() {
        return applyRoomDiscountRecordServiceDaoImpl;
    }

    public void setApplyRoomDiscountRecordServiceDaoImpl(IApplyRoomDiscountRecordServiceDao applyRoomDiscountRecordServiceDaoImpl) {
        this.applyRoomDiscountRecordServiceDaoImpl = applyRoomDiscountRecordServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
