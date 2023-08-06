package com.java110.job.msgNotify.tencent;

import com.alibaba.fastjson.JSONObject;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.vo.ResultVo;
import org.springframework.stereotype.Service;

@Service("tencentMsgNotifyImpl")
public class TencentMsgNotifyImpl implements IMsgNotify {
    @Override
    public ResultVo sendApplyReturnFeeMsg(String communityId,String userId, JSONObject content) {
        return null;
    }

    @Override
    public ResultVo sendOweFeeMsg(String communityId, String userId, JSONObject content) {
        return null;
    }

    @Override
    public ResultVo sendPayFeeMsg(String communityId, String userId, JSONObject content,String role) {
        return null;
    }

    @Override
    public ResultVo sendAddOwnerRepairMsg(String communityId, String userId, JSONObject content) {
        return null;
    }
}
