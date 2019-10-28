package com.java110.common.smo.impl;


import com.java110.common.dao.IAuditMessageServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.audit.IAuditMessageInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.UserDto;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 审核原因内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class AuditMessageInnerServiceSMOImpl extends BaseServiceSMO implements IAuditMessageInnerServiceSMO {

    @Autowired
    private IAuditMessageServiceDao auditMessageServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<AuditMessageDto> queryAuditMessages(@RequestBody  AuditMessageDto auditMessageDto) {

        //校验是否传了 分页信息

        int page = auditMessageDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            auditMessageDto.setPage((page - 1) * auditMessageDto.getRow());
        }

        List<AuditMessageDto> auditMessages = BeanConvertUtil.covertBeanList(auditMessageServiceDaoImpl.getAuditMessageInfo(BeanConvertUtil.beanCovertMap(auditMessageDto)), AuditMessageDto.class);

        if (auditMessages == null || auditMessages.size() == 0) {
            return auditMessages;
        }

        String[] userIds = getUserIds(auditMessages);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (AuditMessageDto auditMessage : auditMessages) {
            refreshAuditMessage(auditMessage, users);
        }
        return auditMessages;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param auditMessage 小区审核原因信息
     * @param users 用户列表
     */
    private void refreshAuditMessage(AuditMessageDto auditMessage, List<UserDto> users) {
        for (UserDto user : users) {
            if (auditMessage.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, auditMessage);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param auditMessages 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<AuditMessageDto> auditMessages) {
        List<String> userIds = new ArrayList<String>();
        for (AuditMessageDto auditMessage : auditMessages) {
            userIds.add(auditMessage.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryAuditMessagesCount(@RequestBody AuditMessageDto auditMessageDto) {
        return auditMessageServiceDaoImpl.queryAuditMessagesCount(BeanConvertUtil.beanCovertMap(auditMessageDto));    }

    public IAuditMessageServiceDao getAuditMessageServiceDaoImpl() {
        return auditMessageServiceDaoImpl;
    }

    public void setAuditMessageServiceDaoImpl(IAuditMessageServiceDao auditMessageServiceDaoImpl) {
        this.auditMessageServiceDaoImpl = auditMessageServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
