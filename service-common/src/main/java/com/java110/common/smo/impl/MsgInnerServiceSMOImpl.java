package com.java110.common.smo.impl;


import com.java110.common.dao.IMsgServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.common.IMsgInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.msg.MsgDto;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 消息内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MsgInnerServiceSMOImpl extends BaseServiceSMO implements IMsgInnerServiceSMO {

    @Autowired
    private IMsgServiceDao msgServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MsgDto> queryMsgs(@RequestBody MsgDto msgDto) {

        //校验是否传了 分页信息

        int page = msgDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            msgDto.setPage((page - 1) * msgDto.getRow());
        }

        List<MsgDto> msgs = BeanConvertUtil.covertBeanList(msgServiceDaoImpl.getMsgInfo(BeanConvertUtil.beanCovertMap(msgDto)), MsgDto.class);

        return msgs;
    }


    @Override
    public int queryMsgsCount(@RequestBody MsgDto msgDto) {
        return msgServiceDaoImpl.queryMsgsCount(BeanConvertUtil.beanCovertMap(msgDto));
    }

    public IMsgServiceDao getMsgServiceDaoImpl() {
        return msgServiceDaoImpl;
    }

    public void setMsgServiceDaoImpl(IMsgServiceDao msgServiceDaoImpl) {
        this.msgServiceDaoImpl = msgServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
