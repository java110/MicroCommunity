package com.java110.community.bmo.assets;

import org.springframework.http.ResponseEntity;

/**
 * 资产查询接口类
 */
public interface IQueryAssetsOpenDoorBMO {

    /**
     * 查询资产信息
     * @param communityId
     * @return
     */
    ResponseEntity<String> query(String communityId);

    /**
     * 查询资产信息
     * @param communityId
     * @return
     */
    ResponseEntity<String> queryImage(String communityId);
}
