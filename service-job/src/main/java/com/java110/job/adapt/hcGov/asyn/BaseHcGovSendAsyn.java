package com.java110.job.adapt.hcGov.asyn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface BaseHcGovSendAsyn {

    public JSONObject createHeadersOrBody(JSONObject body,String extCommunityId,String serviceCode,String secure);

    public void saveHcGovLog(JSONObject paramIn,String communityId,String topic,String objId,String secure);

    public void updateHcGovLog(JSONObject paramIn);

    public void sendKafka(String topic,JSONObject massage,String communityId,String objId,String secure);
}
