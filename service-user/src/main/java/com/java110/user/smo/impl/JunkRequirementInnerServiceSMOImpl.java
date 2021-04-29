package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.user.IJunkRequirementInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.junkRequirement.JunkRequirementDto;
import com.java110.user.dao.IJunkRequirementServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 旧货市场内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class JunkRequirementInnerServiceSMOImpl extends BaseServiceSMO implements IJunkRequirementInnerServiceSMO {

    @Autowired
    private IJunkRequirementServiceDao junkRequirementServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<JunkRequirementDto> queryJunkRequirements(@RequestBody JunkRequirementDto junkRequirementDto) {

        //校验是否传了 分页信息

        int page = junkRequirementDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            junkRequirementDto.setPage((page - 1) * junkRequirementDto.getRow());
        }

        List<JunkRequirementDto> junkRequirements = BeanConvertUtil.covertBeanList(junkRequirementServiceDaoImpl.getJunkRequirementInfo(BeanConvertUtil.beanCovertMap(junkRequirementDto)), JunkRequirementDto.class);

        return junkRequirements;
    }


    @Override
    public int queryJunkRequirementsCount(@RequestBody JunkRequirementDto junkRequirementDto) {
        return junkRequirementServiceDaoImpl.queryJunkRequirementsCount(BeanConvertUtil.beanCovertMap(junkRequirementDto));
    }

    public IJunkRequirementServiceDao getJunkRequirementServiceDaoImpl() {
        return junkRequirementServiceDaoImpl;
    }

    public void setJunkRequirementServiceDaoImpl(IJunkRequirementServiceDao junkRequirementServiceDaoImpl) {
        this.junkRequirementServiceDaoImpl = junkRequirementServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
