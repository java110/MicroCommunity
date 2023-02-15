package com.java110.core.smo;

import com.alibaba.fastjson.JSONObject;

/**
 * 图片处理 服务类
 * add by wuxw 2023-01-19
 */
public interface IPhotoSMO {

    /**
     * 保存图片公共类，供cmd 调用
     * @param photo
     * @param objId
     * @return
     */
    int savePhoto(String photo,String objId, String communityId);

    /**
     * 保存图片公共类，供cmd 调用
     * @param photo
     * @param objId
     * @return
     */
    int savePhoto(String photo,String objId, String communityId,String relTypeCd);

    /**
     * 保存图片公共类，供cmd 调用
     * @param reqJson
     * @param objId
     * @return
     */
    int savePhoto(JSONObject reqJson, String objId, String communityId);
}
