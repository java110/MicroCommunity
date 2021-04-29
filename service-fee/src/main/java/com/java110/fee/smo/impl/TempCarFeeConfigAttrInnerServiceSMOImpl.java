package com.java110.fee.smo.impl;


import com.java110.fee.dao.ITempCarFeeConfigAttrServiceDao;
import com.java110.intf.fee.ITempCarFeeConfigAttrInnerServiceSMO;
import com.java110.dto.tempCarFeeConfig.TempCarFeeConfigAttrDto;
import com.java110.intf.user.IUserInnerServiceSMO;
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
 * @Description 临时车收费标准属性内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class TempCarFeeConfigAttrInnerServiceSMOImpl extends BaseServiceSMO implements ITempCarFeeConfigAttrInnerServiceSMO {

    @Autowired
    private ITempCarFeeConfigAttrServiceDao tempCarFeeConfigAttrServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<TempCarFeeConfigAttrDto> queryTempCarFeeConfigAttrs(@RequestBody  TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto) {

        //校验是否传了 分页信息

        int page = tempCarFeeConfigAttrDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            tempCarFeeConfigAttrDto.setPage((page - 1) * tempCarFeeConfigAttrDto.getRow());
        }

        List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrs = BeanConvertUtil.covertBeanList(tempCarFeeConfigAttrServiceDaoImpl.getTempCarFeeConfigAttrInfo(BeanConvertUtil.beanCovertMap(tempCarFeeConfigAttrDto)), TempCarFeeConfigAttrDto.class);

        if (tempCarFeeConfigAttrs == null || tempCarFeeConfigAttrs.size() == 0) {
            return tempCarFeeConfigAttrs;
        }

        String[] userIds = getUserIds(tempCarFeeConfigAttrs);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (TempCarFeeConfigAttrDto tempCarFeeConfigAttr : tempCarFeeConfigAttrs) {
            refreshTempCarFeeConfigAttr(tempCarFeeConfigAttr, users);
        }
        return tempCarFeeConfigAttrs;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param tempCarFeeConfigAttr 小区临时车收费标准属性信息
     * @param users 用户列表
     */
    private void refreshTempCarFeeConfigAttr(TempCarFeeConfigAttrDto tempCarFeeConfigAttr, List<UserDto> users) {
        for (UserDto user : users) {
            if (tempCarFeeConfigAttr.getAttrId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, tempCarFeeConfigAttr);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param tempCarFeeConfigAttrs 小区楼信息
     * @return 批量userIds 信息
     */
     private String[] getUserIds(List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrs) {
        List<String> userIds = new ArrayList<String>();
        for (TempCarFeeConfigAttrDto tempCarFeeConfigAttr : tempCarFeeConfigAttrs) {
            userIds.add(tempCarFeeConfigAttr.getAttrId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryTempCarFeeConfigAttrsCount(@RequestBody TempCarFeeConfigAttrDto tempCarFeeConfigAttrDto) {
        return tempCarFeeConfigAttrServiceDaoImpl.queryTempCarFeeConfigAttrsCount(BeanConvertUtil.beanCovertMap(tempCarFeeConfigAttrDto));    }

    public ITempCarFeeConfigAttrServiceDao getTempCarFeeConfigAttrServiceDaoImpl() {
        return tempCarFeeConfigAttrServiceDaoImpl;
    }

    public void setTempCarFeeConfigAttrServiceDaoImpl(ITempCarFeeConfigAttrServiceDao tempCarFeeConfigAttrServiceDaoImpl) {
        this.tempCarFeeConfigAttrServiceDaoImpl = tempCarFeeConfigAttrServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
