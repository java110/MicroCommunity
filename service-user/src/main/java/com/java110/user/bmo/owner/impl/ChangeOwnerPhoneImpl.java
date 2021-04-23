package com.java110.user.bmo.owner.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerAppUserInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.owner.OwnerAppUserPo;
import com.java110.po.owner.OwnerPo;
import com.java110.po.user.UserPo;
import com.java110.user.bmo.owner.IChangeOwnerPhone;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChangeOwnerPhoneImpl implements IChangeOwnerPhone {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;



    @Override
    @Java110Transactional
    public ResponseEntity<String> change(OwnerPo ownerPo) {

        String userId = ownerPo.getUserId();
        ownerPo.setUserId("");
        //修改业主手机号
        ownerInnerServiceSMOImpl.updateOwnerMember(ownerPo);

        UserPo userPo = new UserPo();
        userPo.setUserId(userId);
        userPo.setTel(ownerPo.getLink());
        userInnerServiceSMOImpl.updateUser(userPo);

        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setMemberId(ownerPo.getMemberId());
        ownerAppUserPo.setCommunityId(ownerPo.getCommunityId());
        ownerAppUserPo.setLink(ownerPo.getLink());
        ownerAppUserInnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);

        return ResultVo.success();
    }


}
