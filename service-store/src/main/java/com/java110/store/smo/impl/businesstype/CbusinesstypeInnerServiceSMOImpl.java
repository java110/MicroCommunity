package com.java110.store.smo.impl.businesstype;


import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.store.ICbusinesstypeInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.businesstype.CbusinesstypeDto;
import com.java110.store.dao.ICbusinesstypeServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description cbusinesstype内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CbusinesstypeInnerServiceSMOImpl extends BaseServiceSMO implements ICbusinesstypeInnerServiceSMO {

    @Autowired
    private ICbusinesstypeServiceDao cbusinesstypeServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CbusinesstypeDto> queryCbusinesstypes(@RequestBody  CbusinesstypeDto cbusinesstypeDto) {

        //校验是否传了 分页信息

        int page = cbusinesstypeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            cbusinesstypeDto.setPage((page - 1) * cbusinesstypeDto.getRow());
        }

        List<CbusinesstypeDto> cbusinesstypes = BeanConvertUtil.covertBeanList(cbusinesstypeServiceDaoImpl.getCbusinesstypeInfo(BeanConvertUtil.beanCovertMap(cbusinesstypeDto)), CbusinesstypeDto.class);

        if (cbusinesstypes == null || cbusinesstypes.size() == 0) {
            return cbusinesstypes;
        }

        String[] userIds = getUserIds(cbusinesstypes);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (CbusinesstypeDto cbusinesstype : cbusinesstypes) {
            refreshCbusinesstype(cbusinesstype, users);
        }
        return cbusinesstypes;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param cbusinesstype 小区cbusinesstype信息
     * @param users 用户列表
     */
    private void refreshCbusinesstype(CbusinesstypeDto cbusinesstype, List<UserDto> users) {
        for (UserDto user : users) {
            if (cbusinesstype.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, cbusinesstype);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param cbusinesstypes 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CbusinesstypeDto> cbusinesstypes) {
        List<String> userIds = new ArrayList<String>();
        for (CbusinesstypeDto cbusinesstype : cbusinesstypes) {
            userIds.add(cbusinesstype.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCbusinesstypesCount(@RequestBody CbusinesstypeDto cbusinesstypeDto) {
        return cbusinesstypeServiceDaoImpl.queryCbusinesstypesCount(BeanConvertUtil.beanCovertMap(cbusinesstypeDto));    }

    public ICbusinesstypeServiceDao getCbusinesstypeServiceDaoImpl() {
        return cbusinesstypeServiceDaoImpl;
    }

    public void setCbusinesstypeServiceDaoImpl(ICbusinesstypeServiceDao cbusinesstypeServiceDaoImpl) {
        this.cbusinesstypeServiceDaoImpl = cbusinesstypeServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
