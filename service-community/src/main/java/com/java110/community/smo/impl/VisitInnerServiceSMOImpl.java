package com.java110.community.smo.impl;

import com.java110.po.owner.VisitPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.community.dao.IVisitServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.intf.community.IVisitInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.visit.VisitDto;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 访客信息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class VisitInnerServiceSMOImpl extends BaseServiceSMO implements IVisitInnerServiceSMO {

    @Autowired
    private IVisitServiceDao visitServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<VisitDto> queryVisits(@RequestBody VisitDto visitDto) {

        //校验是否传了 分页信息

        int page = visitDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            visitDto.setPage((page - 1) * visitDto.getRow());
        }

        List<VisitDto> visits = BeanConvertUtil.covertBeanList(visitServiceDaoImpl.getVisitInfo(BeanConvertUtil.beanCovertMap(visitDto)), VisitDto.class);

        if (visits == null || visits.size() == 0) {
            return visits;
        }

        String[] userIds = getUserIds(visits);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (VisitDto visit : visits) {
            refreshVisit(visit, users);
        }
        return visits;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param visit 小区访客信息信息
     * @param users 用户列表
     */
    private void refreshVisit(VisitDto visit, List<UserDto> users) {
        for (UserDto user : users) {
            if (!StringUtil.isEmpty(visit.getCreateUserId()) && visit.getCreateUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, visit);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param visits 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<VisitDto> visits) {
        List<String> userIds = new ArrayList<String>();
        for (VisitDto visit : visits) {
            userIds.add(visit.getCreateUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryVisitsCount(@RequestBody VisitDto visitDto) {
        return visitServiceDaoImpl.queryVisitsCount(BeanConvertUtil.beanCovertMap(visitDto));
    }

    @Override
    public void saveVisit(@RequestBody VisitPo visitPo) {
        visitServiceDaoImpl.saveBusinessVisitInfo(BeanConvertUtil.beanCovertMap(visitPo));
    }

    @Override
    public void updateVisit(@RequestBody VisitPo visitPo) {
        visitServiceDaoImpl.updateVisitInfoInstance(BeanConvertUtil.beanCovertMap(visitPo));
    }

    public IVisitServiceDao getVisitServiceDaoImpl() {
        return visitServiceDaoImpl;
    }

    public void setVisitServiceDaoImpl(IVisitServiceDao visitServiceDaoImpl) {
        this.visitServiceDaoImpl = visitServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
