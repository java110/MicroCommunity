package com.java110.order.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.order.ICordersInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.corder.CorderDto;
import com.java110.dto.user.UserDto;
import com.java110.order.dao.ICorderServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class CorderInnerServiceSMOImpl extends BaseServiceSMO implements ICordersInnerServiceSMO {

    @Autowired
    private ICorderServiceDao corderServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CorderDto> queryCorders(@RequestBody  CorderDto corderDto) {

        //校验是否传了 分页信息

        int page = corderDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            corderDto.setPage((page - 1) * corderDto.getRow());
        }

        List<CorderDto> corders = BeanConvertUtil.covertBeanList(corderServiceDaoImpl.getCorderInfo(BeanConvertUtil.beanCovertMap(corderDto)), CorderDto.class);

        if (corders == null || corders.size() == 0) {
            return corders;
        }
        return corders;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param users 用户列表
     */
    private void refreshDemo(CorderDto orders, List<UserDto> users) {
        for (UserDto user : users) {
            if (orders.getUserId().equals(user.getUserId())) {
                orders.setUserName(user.getUserName());
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CorderDto> corders) {
        List<String> userIds = new ArrayList<String>();
        for (CorderDto order : corders) {
            userIds.add(order.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }


    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }

    @Override
    public int queryCordersCount(@RequestBody CorderDto corderDto) {
        int row = corderServiceDaoImpl.queryCordersCount(BeanConvertUtil.beanCovertMap(corderDto));
        return row;
    }
}
