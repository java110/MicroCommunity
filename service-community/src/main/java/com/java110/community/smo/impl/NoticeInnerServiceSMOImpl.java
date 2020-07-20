package com.java110.community.smo.impl;


import com.java110.community.dao.INoticeServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.community.INoticeInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 通知内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class NoticeInnerServiceSMOImpl extends BaseServiceSMO implements INoticeInnerServiceSMO {

    @Autowired
    private INoticeServiceDao noticeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<NoticeDto> queryNotices(@RequestBody NoticeDto noticeDto) {

        //校验是否传了 分页信息

        int page = noticeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            noticeDto.setPage((page - 1) * noticeDto.getRow());
        }

        List<NoticeDto> notices = BeanConvertUtil.covertBeanList(noticeServiceDaoImpl.getNoticeInfo(BeanConvertUtil.beanCovertMap(noticeDto)), NoticeDto.class);

        if (notices == null || notices.size() == 0) {
            return notices;
        }

        String[] userIds = getUserIds(notices);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (NoticeDto notice : notices) {
            refreshNotice(notice, users);
        }
        return notices;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param notice 小区通知信息
     * @param users  用户列表
     */
    private void refreshNotice(NoticeDto notice, List<UserDto> users) {
        for (UserDto user : users) {
            if (notice.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, notice);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param notices 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<NoticeDto> notices) {
        List<String> userIds = new ArrayList<String>();
        for (NoticeDto notice : notices) {
            userIds.add(notice.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryNoticesCount(@RequestBody NoticeDto noticeDto) {
        return noticeServiceDaoImpl.queryNoticesCount(BeanConvertUtil.beanCovertMap(noticeDto));
    }

    @Override
    public int updateNotice(@RequestBody NoticeDto noticeDto) {
        int flag = 1;
        noticeServiceDaoImpl.updateNoticeInfoInstance(BeanConvertUtil.beanCovertMap(noticeDto));
        return flag;
    }

    public INoticeServiceDao getNoticeServiceDaoImpl() {
        return noticeServiceDaoImpl;
    }

    public void setNoticeServiceDaoImpl(INoticeServiceDao noticeServiceDaoImpl) {
        this.noticeServiceDaoImpl = noticeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
